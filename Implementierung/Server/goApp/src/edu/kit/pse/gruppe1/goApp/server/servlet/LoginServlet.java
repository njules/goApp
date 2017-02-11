package edu.kit.pse.gruppe1.goApp.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import edu.kit.pse.gruppe1.goApp.server.database.management.UserManagement;
import edu.kit.pse.gruppe1.goApp.server.model.User;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.ErrorCodes;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.Methods;

/**
 * Servlet implementation class LoginServlet
 * 
 * This servlet is used to register users that start the app for the first time and login returning
 * users.
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserManagement usrMang;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        usrMang = new UserManagement();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String strResponse = null;
        JSONObject jsonRequest = null;
        JSONParameter.Methods method = null;
        PrintWriter out = null;
        ErrorCodes error = ErrorCodes.OK;

        out = response.getWriter();

        jsonRequest = ServletUtils.extractJSON(request, response);
        if (jsonRequest == null) {
            // response was set in extractJSON
            return;
        }

        try {
            method = JSONParameter.Methods
                    .fromString(jsonRequest.getString(JSONParameter.METHOD.toString()));
        } catch (JSONException e) {
            if (e.getMessage().equals(ErrorCodes.EMPTY_JSON.toString())) {
                error = ErrorCodes.EMPTY_JSON;
            } else {
                error = ErrorCodes.READ_JSON;
            }
        }

        if (method == null || !error.equals(ErrorCodes.OK)) {
            method = Methods.NONE;
        }
        switch (method) {
        case LOGIN:
            strResponse = login(jsonRequest).toString();
            break;
        case REGISTER:
            strResponse = register(jsonRequest).toString();
            break;
        default:
            if (error.equals(ErrorCodes.OK)) {
                error = ErrorCodes.READ_JSON;
            }
            strResponse = ServletUtils.createJSONError(error).toString();
            break;
        }
        out.println(strResponse);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Invoking this method creates a new user and registers his ID to the database. The new user
     * may then join groups. A user can register only once, if he returns to the app he must use the
     * login function.
     * 
     * @param json
     *            The JSON object contains a user ID to which the user shall be registered.
     * @return Returns a JSON string containing the user that just registered.
     */
    private JSONObject register(JSONObject json) {
        String googleToken = null;
        String googleId = null;
        JSONParameter.ErrorCodes error = ErrorCodes.OK;
        String name = null;
        User user = null;
        JSONObject result = null;

        try {
            googleToken = json.getString(JSONParameter.GOOGLE_TOKEN.toString());
        } catch (JSONException e) {
            error = ErrorCodes.READ_JSON;
            return ServletUtils.createJSONError(error);
        }
        googleId = ServletUtils.getGoogleIdByToken(googleToken);
        name = ServletUtils.getGoogleNameByToken(googleToken);
        user = usrMang.add(name, googleId);

        if (user != null) {
            result = ServletUtils.createJSONUser(user);
        } else {
            error = ErrorCodes.DB_ERROR;
        }
        if (!error.equals(ErrorCodes.OK)) {
            result = ServletUtils.createJSONError(error);
        }
        return result;
    }

    /**
     * If a user has already registered to the database but his client is currently not logged in,
     * he can call this method to regain access to the functions of this app. A user may only login
     * if he isn't already logged in and has registered himself previously at any point.
     * 
     * @param json
     *            This JSON object contains the user that wants to login.
     * @return Returns a JSON string containing the user that just logged in.
     */
    private JSONObject login(JSONObject json) {
        String googleToken = null;
        String googleId = null;
        User user = null;

        try {
            googleToken = json.getString(JSONParameter.GOOGLE_TOKEN.toString());
        } catch (JSONException e) {
            return ServletUtils.createJSONError(ErrorCodes.READ_JSON);
        }
        googleId = ServletUtils.getGoogleIdByToken(googleToken);

        if (ServletUtils.isUserAlreadyRegistrated(googleId)) {
            user = usrMang.getUserByGoogleId(googleId);
            return ServletUtils.createJSONUser(user);
        } else {
            return register(json);
        }
    }

}
