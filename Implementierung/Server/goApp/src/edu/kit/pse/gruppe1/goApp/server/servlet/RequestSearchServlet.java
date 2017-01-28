package edu.kit.pse.gruppe1.goApp.server.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import edu.kit.pse.gruppe1.goApp.server.database.management.RequestManagement;
import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.User;

/**
 * Servlet implementation class RequestSearchServlet
 * 
 * This servlet returns active requests from a group or a user.
 */
@WebServlet("/RequestSearchServlet")
public class RequestSearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private RequestManagement reqMang;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public RequestSearchServlet() {
        super();
        reqMang = new RequestManagement();
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
     * This method fetches all active join requests from a given user. Only the user himself may
     * view all of his active join requests.
     * 
     * @param json
     *            The JSON object contains the user that is asking for the list of his join requests
     * @return Returns a JSON string containing a list of all join requests issued by the given
     *         user.
     */
    private String getRequestsByUser(JSONObject json) {
        int userID = -1;
        List<Group> grpFromUsr = null;

        try {
            userID = json.getInt(JSONParameter.UserID.toString());
        } catch (JSONException e) {
            return "UserID not found";
        }

        grpFromUsr = reqMang.getRequestByUser(userID);

        // TODO: wie Serialisieren
        return null;
    }

    /**
     * This method fetches all active join requests for a given group. Join requests may only be
     * viewed by the group founder.
     * 
     * @param json
     *            The JSON object contains the ID of the group from which the join requests shall be
     *            fetched.
     * @return Returns a JSON string containing a list of all active join requests for the given
     *         group..
     */
    private String getRequestsByGroup(JSONObject json) {
        int groupID = -1;
        List<User> usrInGrp = null;

        try {
            groupID = json.getInt(JSONParameter.GroupID.toString());
        } catch (JSONException e) {
            return "GroupID not found";
        }

        usrInGrp = reqMang.getRequestByGroup(groupID);

        // TODO: wie Serialisieren
        return null;

    }

}
