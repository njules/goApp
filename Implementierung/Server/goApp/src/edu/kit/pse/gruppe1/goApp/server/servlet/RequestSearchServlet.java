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
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.Methods;

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
        case GET_REQ_USR:
            strResponse = getRequestsByUser(jsonRequest).toString();
            break;
        case GET_REQ_GRP:
            strResponse = getRequestsByGroup(jsonRequest).toString();
            break;
        default:
            if (error.equals(ErrorCodes.OK)) {
                error = ErrorCodes.METH_ERROR;
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
     * This method fetches all active join requests from a given user. Only the user himself may
     * view all of his active join requests.
     * 
     * @param json
     *            The JSON object contains the user that is asking for the list of his join requests
     * @return Returns a JSON string containing a list of all join requests issued by the given
     *         user.
     */
    private JSONObject getRequestsByUser(JSONObject json) {
        int userID = -1;
        List<Group> grpFromUsr = null;

        try {
            userID = json.getInt(JSONParameter.USER_ID.toString());
        } catch (JSONException e) {
            return ServletUtils.createJSONError(ErrorCodes.READ_JSON);
        }

        grpFromUsr = reqMang.getRequestByUser(userID);

        return ServletUtils.createJSONListGrp(grpFromUsr);
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
    private JSONObject getRequestsByGroup(JSONObject json) {
        int groupID = -1;
        List<User> usrInGrp = null;

        try {
            groupID = json.getInt(JSONParameter.GROUP_ID.toString());
        } catch (JSONException e) {
            return ServletUtils.createJSONError(ErrorCodes.READ_JSON);
        }

        usrInGrp = reqMang.getRequestByGroup(groupID);

        return ServletUtils.createJSONListUsr(usrInGrp);

    }

}
