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

import edu.kit.pse.gruppe1.goApp.server.database.management.GroupUserManagement;
import edu.kit.pse.gruppe1.goApp.server.database.management.RequestManagement;
import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.User;

/**
 * Servlet implementation class RequestServlet
 * 
 * Methods in this class exist so that a user can request to join a group and the group founder may
 * then accept or reject the join request.
 */
@WebServlet("/RequestServlet")
public class RequestServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private RequestManagement reqMang;
    private GroupUserManagement grUsrMang;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public RequestServlet() {
        super();
        reqMang = new RequestManagement();
        grUsrMang = new GroupUserManagement();
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
     * This method creates a join request from a user to a group. The user may not have reached the
     * group limit already and the group may not have reached the user limit. Also the user may not
     * already be a member of this group.
     * 
     * @param json
     *            The JSON Object contains the user that posts the join request and the ID of the
     *            group the request is posted to.
     * @return Returns a JSON string containing information about the success of this operation.
     */
    private String create(JSONObject json) {
        // TODO: contrains in Datei festlegen und diese hier auslesen
        // TODO: richtige Limits
        // TODO: bisherige Requests auch zählen?

        int userlimit = 20;
        int grouplimit = 50;
        int userID = -1;
        int newGroupID = -1;
        List<Group> groups = null;
        List<User> users = null;

        // Check Users Memberships
        try {
            userID = json.getInt(JSONParameter.UserID.toString());
        } catch (JSONException e) {
            return createErrorJSON("UserID not found");
        }
        if (userID != -1) {
            groups = grUsrMang.getGroups(userID);
            if (groups.size() >= userlimit) {
                return createErrorJSON("User limit reached");
            }
        } else {
            return createErrorJSON("No user found");
        }

        // Check Group size
        try {
            newGroupID = json.getInt(JSONParameter.GroupID.toString());
        } catch (JSONException e) {
            return createErrorJSON("GroupID not found");
        }
        if (newGroupID != -1) {
            users = grUsrMang.getUsers(newGroupID);
            if (users.size() >= grouplimit) {
                return createErrorJSON("Group limit reached");
            }
        } else {
            return createErrorJSON("No group found");
        }

        // Add new Request
        if (reqMang.add(newGroupID, userID)) {
            return createErrorJSON("");
        } else {
            return createErrorJSON("request could not be added");
        }
    }

    // TODO: JavaDocs
    // TODO: Fehlerfall
    private String createErrorJSON(String error) {
        String result = null;
        JSONObject res = new JSONObject();
        try {
            res.append(JSONParameter.ErrorCode.toString(), error).toString();
        } catch (JSONException e) {
            // TODO: was tun?
        }
        return res.toString();
    }

    /**
     * This method accepts a users join request. Only a group founder may accept join requests.
     * After a request has been accepted, the user is a new member of the group and the request is
     * no longer valid.
     * 
     * @param json
     *            The JSON object contains the request that is to be accepted.
     * @return Returns a JSON string containing information about the success of this operation.
     */
    private String accept(JSONObject json) {
        String error = null;
        int userID = -1;
        int groupID = -1;

        // read User and Group ID from JSON
        try {
            userID = json.getInt(JSONParameter.UserID.toString());
        } catch (JSONException e) {
            return "UserID not found";
        }

        try {
            groupID = json.getInt(JSONParameter.GroupID.toString());
        } catch (JSONException e) {
            return "GroupID not found";
        }

        error = checkRequest(userID, groupID);
        if (error.isEmpty()) {
            grUsrMang.add(groupID, userID);
            reqMang.delete(groupID, userID);
        }
        return createErrorJSON(error);

    }

    /**
     * This method rejects a users join request. Only a group founder may reject join requests.
     * After a request has been rejected it is no longer valid.
     * 
     * @param json
     *            The JSON object contains the request that is to be rejected.
     * @return Returns a JSON string containing information about the success of this operation.
     */
    private String reject(JSONObject json) {
        int userID = -1;
        int groupID = -1;
        String error = null;

        // read User and Group ID from JSON
        try {
            userID = json.getInt(JSONParameter.UserID.toString());
        } catch (JSONException e) {
            return "UserID not found";
        }

        try {
            groupID = json.getInt(JSONParameter.GroupID.toString());
        } catch (JSONException e) {
            return "GroupID not found";
        }

        // delete request, if exists
        error = checkRequest(userID, groupID);
        if (error.isEmpty()) {
            reqMang.delete(groupID, userID);
        }
        return createErrorJSON(error);

    }

    /**
     * TODO: restliches JavaDocs
     * 
     * @param json
     * @return error as String or empty String, if request exists
     */
    private String checkRequest(int userID, int groupID) {
        List<User> usrInGrp = null;
        boolean userFound = false;

        // if object is in RequestManagement then add user to group
        // TODO: evtl neue Funktion in RequestManagement
        usrInGrp = reqMang.getRequestByGroup(groupID);
        for (User usr : usrInGrp) {
            if (usr.getUserId() == userID) {
                userFound = true;
                break;
            }
        }

        if (!userFound) {
            return "request does not exist";
        } else {
            return "";
        }
    }

}
