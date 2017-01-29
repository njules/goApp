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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: hier herkömmlich
        // String strResponse = null;
        // String jsonString = null;
        // JSONParameter.Methods method = null;
        // response.setContentType("text/plain");
        // PrintWriter out = null;
        // // try {
        // out = response.getWriter();
        // jsonString = request.getReader().readLine();
        // // } catch (IOException e1) {
        // // strResponse = ServletUtils.createJSONError(ErrorCodes.IO_ERROR);
        // // out.println(strResponse);
        // // return;
        // // }
        //
        // if (jsonString == null) {
        // strResponse = ServletUtils.createJSONError(ErrorCodes.EMPTY_JSON);
        // out.println(strResponse);
        // return;
        // }
        // try {
        // JSONObject jsonRequest = new JSONObject(jsonString);
        // method = JSONParameter.Methods
        // .fromString(jsonRequest.getString(JSONParameter.Method.toString()));
        // switch (method) {
        // case LOGIN:
        // strResponse = login(jsonRequest);
        // break;
        // case REGISTER:
        // strResponse = register(jsonRequest);
        // break;
        // default:
        // strResponse = ServletUtils.createJSONError(ErrorCodes.METH_ERROR);
        // break;
        // }
        // out.println(strResponse);
        // } catch (JSONException e) {
        // strResponse = ServletUtils.createJSONError(ErrorCodes.READ_JSON);
        // out.println(strResponse);
        // }
        // TODO: IO-Error/Servlet Error werfen?

        String strResponse = null;
        JSONObject jsonRequest = null;
        JSONParameter.Methods method = null;
        PrintWriter out = null;
        ErrorCodes error = ErrorCodes.OK;

        out = response.getWriter();

        try {
            method = ServletUtils.getMethod(request, jsonRequest);
        } catch (JSONException e) {
            if (e.getMessage().equals(ErrorCodes.EMPTY_JSON.toString())) {
                error = ErrorCodes.EMPTY_JSON;
            } else {
                error = ErrorCodes.READ_JSON;
            }
        }

        switch (method) {
        case LOGIN:
            strResponse = login(jsonRequest);
            break;
        case REGISTER:
            strResponse = register(jsonRequest);
            break;
        default:
            if (error.equals(ErrorCodes.OK)) {
                error = ErrorCodes.READ_JSON;
            }
            strResponse = ServletUtils.createJSONError(error);
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
    private String register(JSONObject json) {
        int googleId = -1;
        JSONParameter.ErrorCodes error = ErrorCodes.OK;
        User user = null;
        String result = null;

        try {
            googleId = json.getInt(JSONParameter.ID.toString());
            String name = json.getString(JSONParameter.UserName.toString());
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
    private String login(JSONObject json) {
        int userID = -1;
        JSONParameter.ErrorCodes error = ErrorCodes.OK;
        User user = null;

        try {
            userID = json.getInt(JSONParameter.UserID.toString());
        } catch (JSONException e) {
            error = ErrorCodes.READ_JSON;
            return ServletUtils.createJSONError(error);
        }
        user = usrMang.getUser(userID);
        if (user != null) {
            return ServletUtils.createJSONUser(user);
        }
        // if User does not exist yet (getUser == null) register new User
        return register(json);
    }

}
