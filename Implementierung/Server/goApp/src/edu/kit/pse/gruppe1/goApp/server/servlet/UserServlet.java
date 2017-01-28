package edu.kit.pse.gruppe1.goApp.server.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import edu.kit.pse.gruppe1.goApp.server.database.management.UserManagement;
import edu.kit.pse.gruppe1.goApp.server.model.Event;
import edu.kit.pse.gruppe1.goApp.server.model.User;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.ErrorCodes;

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        response.getWriter().append("Served at: ").append(request.getContextPath());
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
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
    private String changeName(JSONObject json) {
        User user = null;
        JSONParameter.ErrorCodes error = ErrorCodes.OK;
        
        try {
            int userID = json.getInt(JSONParameter.UserID.toString());
            user = usrMang.getUser(userID);
            if (user == null) {
                error = ErrorCodes.DB_ERROR;
            }
            String name = json.getString(JSONParameter.UserName.toString());
            user.setName(name);
        } catch (JSONException e) {
            error = ErrorCodes.READ_JSON;
        }

        if (error != null && user != null) {
            if (!usrMang.update(user)) {
                error = ErrorCodes.DB_ERROR;
            }
        }

        return createJSONObject(user, error);

    }

    // TODO: JavaDocs
    // TODO: wie ganze Klassen Serialisieren
    private String createJSONObject(User user, JSONParameter.ErrorCodes error) {
        String result = null;
        JSONObject res = new JSONObject();
        try {
            if (error == null) {
                // TODO: User Serialisieren res.append(JSONParameter.User.toString(), user);
            } else {
                res.append(JSONParameter.ErrorCode.toString(), error);
            }
        } catch (JSONException e) {
            //res.append(JSONParameter.ErrorCode.toString(), ErrorCodes.WRITE_JSON);
        }
        result = res.toString();
        return result;
    }

    /**
     * A user can invoke this to retrieve any information about a given user such as groups he is a
     * member of and events he wants to participate or is invited to.
     * 
     * @param json
     *            This JSON object contains the user about whom the information shall be released.
     * @return Returns a JSON string containing information about the success of this operation.
     */
    private String getUser(JSONObject json) {
        User user = null;
        JSONParameter.ErrorCodes error = ErrorCodes.OK;

        try {
            int userID = json.getInt(JSONParameter.UserID.toString());
            user = this.usrMang.getUser(userID);
        } catch (JSONException e) {
            error = ErrorCodes.READ_JSON;
        }
        return createJSONObject(user, error);
    }

}
