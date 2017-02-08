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
    // TODO: String test wieder rausnehmen
    private String test;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        usrMang = new UserManagement();
        test = "inKlasse";
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: IO-Error/Servlet Error werfen?

        // TODO: wenn Methode register ->neues Token, bei LogIn prüfen

        String strResponse = null;
        JSONObject jsonRequest = null;
        JSONParameter.Methods method = null;
        PrintWriter out = null;
        ErrorCodes error = ErrorCodes.OK;

        out = response.getWriter();

        jsonRequest = ServletUtils.extractJSON(request, response);
        if (jsonRequest == null) {
            //response was set in extractJSON
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: was hier tun?
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
        int googleId = -1;
        JSONParameter.ErrorCodes error = ErrorCodes.OK;
        User user = null;
        JSONObject result = null;

        try {
            googleId = json.getInt(JSONParameter.GOOGLE_ID.toString());
            String name = json.getString(JSONParameter.USER_NAME.toString());
            user = usrMang.add(name, googleId);
        } catch (JSONException e) {
            error = ErrorCodes.READ_JSON;
            return ServletUtils.createJSONError(error);
        }
        if (user != null) {
            result = ServletUtils.createJSONUser(user);
        } else if (!error.equals(ErrorCodes.OK)) {
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
        int userID = -1;
        JSONParameter.ErrorCodes error = ErrorCodes.OK;
        User user = null;

        try {
            userID = json.getInt(JSONParameter.USER_ID.toString());
        } catch (JSONException e) {
            // could happen, if no user exists and then no UserID was set;
            userID = -1;
        }
        user = usrMang.getUser(userID);
        if (user != null) {
            return ServletUtils.createJSONUser(user);
        }
        // if User does not exist yet (getUser == null) register new User
        return register(json);
    }

}
