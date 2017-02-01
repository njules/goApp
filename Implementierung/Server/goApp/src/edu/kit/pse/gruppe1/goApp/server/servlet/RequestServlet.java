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
//        String strResponse = null;
//        String jsonString = null;
//        JSONParameter.Methods method = null;
//        response.setContentType("text/plain");
//        PrintWriter out = null;
//        // try {
//        out = response.getWriter();
//        jsonString = request.getReader().readLine();
//        // } catch (IOException e1) {
//        // strResponse = ServletUtils.createJSONError(ErrorCodes.IO_ERROR);
//        // out.println(strResponse);
//        // return;
//        // }
//
//        if (jsonString == null) {
//            strResponse = ServletUtils.createJSONError(ErrorCodes.EMPTY_JSON);
//            out.println(strResponse);
//            return;
//        }
//        try {
//            JSONObject jsonRequest = new JSONObject(jsonString);
//            method = JSONParameter.Methods
//                    .fromString(jsonRequest.getString(JSONParameter.Method.toString()));
//            switch (method) {
//            case CREATE:
//                strResponse = create(jsonRequest);
//                break;
//            case ACCEPT:
//                strResponse = accept(jsonRequest);
//                break;
//            case REJECT:
//                strResponse = reject(jsonRequest);
//                break;
//            default:
//                strResponse = ServletUtils.createJSONError(ErrorCodes.METH_ERROR);
//                break;
//            }
//            out.println(strResponse);
//        } catch (JSONException e) {
//            strResponse = ServletUtils.createJSONError(ErrorCodes.READ_JSON);
//            out.println(strResponse);
//        }
        
        //TODO: delete old one, if new does work
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
        case CREATE:
            strResponse = create(jsonRequest);
            break;
        case ACCEPT:
            strResponse = accept(jsonRequest);
            break;
        case REJECT:
            strResponse = reject(jsonRequest);
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
        // TODO: constrains in Datei festlegen und diese hier auslesen
        // TODO: weniger returns
        int userlimit = 20;
        int grouplimit = 50;
        int userID = -1;
        int newGroupID = -1;
        List<Group> groups = null;
        List<User> users = null;
        List<Group> reqGroups = null;
        List<User> reqUsers = null;
        int groupSum = 0;
        int userSum = 0;

        try {
            userID = json.getInt(JSONParameter.UserID.toString());
            newGroupID = json.getInt(JSONParameter.GroupID.toString());
        } catch (JSONException e) {
            return ServletUtils.createJSONError(ErrorCodes.READ_JSON).toString();
        }

        // Check Users Memberships and Group size
        if (userID != -1 && newGroupID != -1) {
            groups = grUsrMang.getGroups(userID);
            users = grUsrMang.getUsers(newGroupID);
            reqGroups = reqMang.getRequestByUser(userID);
            reqUsers = reqMang.getRequestByGroup(newGroupID);
            
            if (groups != null && users != null) {
                return ServletUtils.createJSONError(ErrorCodes.DB_ERROR).toString();
            }
            groupSum += groups.size();
            userSum += users.size();
            
            //it is possible, that there are no open requests
            if(reqGroups != null){
                groupSum += reqGroups.size();
            }
            if(reqUsers != null){
                userSum += reqUsers.size();
            }
            
            //check all sizes
            if (groupSum >= userlimit) {
                return ServletUtils.createJSONError(ErrorCodes.USR_LIMIT).toString();
            }else if (userSum >= grouplimit) {
                return ServletUtils.createJSONError(ErrorCodes.GRP_LIMIT).toString();
            }
        } 

        // Add new Request
        if (reqMang.add(newGroupID, userID)) {
            return ServletUtils.createJSONError(ErrorCodes.OK).toString();
        } else {
            return ServletUtils.createJSONError(ErrorCodes.DB_ERROR).toString();
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
    private String accept(JSONObject json) {
        JSONParameter.ErrorCodes error = ErrorCodes.OK;
        int userID = -1;
        int groupID = -1;
        Request req = null;

        // read User and Group ID from JSON
        try {
            userID = json.getInt(JSONParameter.UserID.toString());
            groupID = json.getInt(JSONParameter.GroupID.toString());
        } catch (JSONException e) {
            return ServletUtils.createJSONError(ErrorCodes.READ_JSON).toString();
        }

        req = reqMang.getRequest(userID, groupID);
        if (req != null) {
            grUsrMang.add(groupID, userID);
            reqMang.delete(groupID, userID);
        } else {
            error = ErrorCodes.DB_ERROR;
        }
        return ServletUtils.createJSONError(error).toString();
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
        JSONParameter.ErrorCodes error = ErrorCodes.OK;
        Request req = null;

        // read User and Group ID from JSON
        try {
            userID = json.getInt(JSONParameter.UserID.toString());
            groupID = json.getInt(JSONParameter.GroupID.toString());
        } catch (JSONException e) {
            return ServletUtils.createJSONError(ErrorCodes.READ_JSON).toString();
        }

        // delete request, if exists
        req = reqMang.getRequest(userID, groupID);
        if (req != null) {
            reqMang.delete(groupID, userID);
        } else {
            error = ErrorCodes.DB_ERROR;
        }
        return ServletUtils.createJSONError(error).toString();

    }
}
