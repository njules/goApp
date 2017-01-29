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

import edu.kit.pse.gruppe1.goApp.server.database.management.RequestManagement;
import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.User;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.ErrorCodes;

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
//            case GET_REQ_USR:
//                strResponse = getRequestsByUser(jsonRequest);
//                break;
//            case GET_REQ_GRP:
//                strResponse = getRequestsByGroup(jsonRequest);
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
        
     // TODO: delete old one, if new does work
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
        case GET_REQ_USR:
            strResponse = getRequestsByUser(jsonRequest);
            break;
        case GET_REQ_GRP:
            strResponse = getRequestsByGroup(jsonRequest);
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
            return ServletUtils.createJSONError(ErrorCodes.READ_JSON).toString();
        }

        grpFromUsr = reqMang.getRequestByUser(userID);

        return ServletUtils.createJSONListGrp(grpFromUsr).toString();
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
            return ServletUtils.createJSONError(ErrorCodes.READ_JSON).toString();
        }

        usrInGrp = reqMang.getRequestByGroup(groupID);

        return ServletUtils.createJSONListUsr(usrInGrp).toString();

    }

}
