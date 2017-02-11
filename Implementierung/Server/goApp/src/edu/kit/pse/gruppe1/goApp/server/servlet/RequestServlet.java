package edu.kit.pse.gruppe1.goApp.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
import edu.kit.pse.gruppe1.goApp.server.model.Request;
import edu.kit.pse.gruppe1.goApp.server.model.User;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.ErrorCodes;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.Methods;

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
        case CREATE:
            strResponse = create(jsonRequest).toString();
            break;
        case ACCEPT:
            strResponse = accept(jsonRequest).toString();
            break;
        case REJECT:
            strResponse = reject(jsonRequest).toString();
            break;
        default:
            if (error.equals(ErrorCodes.OK)) {
                error = ErrorCodes.READ_JSON;
                //TODO: Methodenfehler überall
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
     * This method creates a join request from a user to a group. The user may not have reached the
     * group limit already and the group may not have reached the user limit. Also the user may not
     * already be a member of this group.
     * 
     * @param json
     *            The JSON Object contains the user that posts the join request and the ID of the
     *            group the request is posted to.
     * @return Returns a JSON string containing information about the success of this operation.
     */
    private JSONObject create(JSONObject json) {
        int userlimit = ServletUtils.USERLIMIT;
        int grouplimit = ServletUtils.GROUPLIMIT;
        int userID = -1;
        int newGroupID = -1;
        List<Group> groups = null;
        List<User> users = null;
        List<Group> reqGroups = null;
        List<User> reqUsers = null;
        int groupSum = 0;
        int userSum = 0;

        try {
            userID = json.getInt(JSONParameter.USER_ID.toString());
            newGroupID = json.getInt(JSONParameter.GROUP_ID.toString());
        } catch (JSONException e) {
            return ServletUtils.createJSONError(ErrorCodes.READ_JSON);
        }

        // Check Users Memberships and Group size
        if (userID != -1 && newGroupID != -1) {
            groups = grUsrMang.getGroups(userID);
            users = grUsrMang.getUsers(newGroupID);
            reqGroups = reqMang.getRequestByUser(userID);
            reqUsers = reqMang.getRequestByGroup(newGroupID);

            // Check if User is already member of group
            for (Group g : groups) {
                if (g.getGroupId() == newGroupID) {
                    return ServletUtils.createJSONError(ErrorCodes.INTERACT_ERROR);
                }
            }

            if (groups == null || users == null) {
                return ServletUtils.createJSONError(ErrorCodes.DB_ERROR);
            }
            groupSum += groups.size();
            userSum += users.size();

            // it is possible, that there are no open requests
            if (reqGroups != null) {
                groupSum += reqGroups.size();
            }
            if (reqUsers != null) {
                userSum += reqUsers.size();
            }

            // check all sizes
            if (groupSum >= userlimit) {
                return ServletUtils.createJSONError(ErrorCodes.USR_LIMIT);
            } else if (userSum >= grouplimit) {
                return ServletUtils.createJSONError(ErrorCodes.GRP_LIMIT);
            }
        }

        // Add new Request
        if (reqMang.add(newGroupID, userID)) {
            return ServletUtils.createJSONError(ErrorCodes.OK);
        } else {
            return ServletUtils.createJSONError(ErrorCodes.DB_ERROR);
        }
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
    private JSONObject accept(JSONObject json) {
        JSONParameter.ErrorCodes error = ErrorCodes.OK;
        int userID = -1;
        int groupID = -1;
        Request req = null;

        // read User and Group ID from JSON
        try {
            userID = json.getInt(JSONParameter.USER_ID.toString());
            groupID = json.getInt(JSONParameter.GROUP_ID.toString());
        } catch (JSONException e) {
            return ServletUtils.createJSONError(ErrorCodes.READ_JSON);
        }

        req = reqMang.getRequest(groupID, userID);
        if (req != null) {
            if (!grUsrMang.add(groupID, userID) && !reqMang.delete(groupID, userID)) {
                error = ErrorCodes.DB_ERROR;
            }
        } else {
            error = ErrorCodes.DB_ERROR;
        }
        return ServletUtils.createJSONError(error);
    }

    /**
     * This method rejects a users join request. Only a group founder may reject join requests.
     * After a request has been rejected it is no longer valid.
     * 
     * @param json
     *            The JSON object contains the request that is to be rejected.
     * @return Returns a JSON string containing information about the success of this operation.
     */
    private JSONObject reject(JSONObject json) {
        int userID = -1;
        int groupID = -1;
        JSONParameter.ErrorCodes error = ErrorCodes.OK;
        Request req = null;

        // read User and Group ID from JSON
        try {
            userID = json.getInt(JSONParameter.USER_ID.toString());
            groupID = json.getInt(JSONParameter.GROUP_ID.toString());
        } catch (JSONException e) {
            return ServletUtils.createJSONError(ErrorCodes.READ_JSON);
        }

        // delete request, if exists
        req = reqMang.getRequest(groupID, userID);
        if (req != null) {
            if (!reqMang.delete(groupID, userID)) {
                error = ErrorCodes.DB_ERROR;
            }
        } else {
            error = ErrorCodes.DB_ERROR;
        }
        return ServletUtils.createJSONError(error);

    }
}
