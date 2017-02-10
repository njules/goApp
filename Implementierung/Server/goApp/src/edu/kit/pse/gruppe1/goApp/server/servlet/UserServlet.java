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
 * Servlet implementation class UserServlet
 * 
 * This servlet is used by users to access and manage information about them.
 */
@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserManagement usrMang;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
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
        case CHANGE:
            strResponse = changeName(jsonRequest).toString();
            break;
        // case GET_USER:
        // strResponse = getUser(jsonRequest);
        // break;
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
     * This method allows a user to change his name to a string value of his choice. A user may only
     * change his own name.
     * 
     * @param json
     *            The JSON object contains the user that wants to change his name and the string
     *            with the name the user wants to change it to.
     * @return Returns a JSON string containing information about the success of this operation.
     */
    private JSONObject changeName(JSONObject json) {
        User user = null;
        JSONParameter.ErrorCodes error = ErrorCodes.OK;

        try {
            int userID = json.getInt(JSONParameter.USER_ID.toString());
            user = usrMang.getUser(userID);
            if (user == null) {
                error = ErrorCodes.DB_ERROR;
            }
            String name = json.getString(JSONParameter.USER_NAME.toString());
            user.setName(name);
        } catch (JSONException e) {
            error = ErrorCodes.READ_JSON;
        }

        if (error != null && user != null) {
            if (!usrMang.update(user)) {
                error = ErrorCodes.DB_ERROR;
            }
        }

        return ServletUtils.createJSONError(error);

    }

    /**
     * NOT USED (YET) BY Client
     * 
     * A user can invoke this to retrieve any information about a given user such as groups he is a
     * member of and events he wants to participate or is invited to.
     * 
     * @param json
     *            This JSON object contains the user about whom the information shall be released.
     * @return Returns a JSON string containing information about the success of this operation.
     */
    @SuppressWarnings("unused")
    private JSONObject getUser(JSONObject json) {
        User user = null;
        JSONParameter.ErrorCodes error = ErrorCodes.OK;
        JSONObject result = null;

        try {
            int userID = json.getInt(JSONParameter.USER_ID.toString());
            user = this.usrMang.getUser(userID);
        } catch (JSONException e) {
            error = ErrorCodes.READ_JSON;
        }
        if (error.equals(ErrorCodes.OK)) {
            result = ServletUtils.createJSONUser(user);
        } else {
            result = ServletUtils.createJSONError(error);
        }

        return result;
    }

}
