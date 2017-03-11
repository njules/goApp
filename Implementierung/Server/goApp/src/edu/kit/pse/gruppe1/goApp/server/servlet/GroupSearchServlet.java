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

import edu.kit.pse.gruppe1.goApp.server.database.management.GroupManagement;
import edu.kit.pse.gruppe1.goApp.server.database.management.GroupUserManagement;
import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.ErrorCodes;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.Methods;

/**
 * Servlet implementation class GroupSearchServlet
 * 
 * This servlet offers methods to search for groups with a specific name or in which a given user is
 * the member.
 */
@WebServlet("/GroupSearchServlet")
public class GroupSearchServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final GroupManagement groupM;
    private final GroupUserManagement groupUM;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public GroupSearchServlet() {
        super();
        groupM = new GroupManagement();
        groupUM = new GroupUserManagement();
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
        case GET_GRP_NAME:
            strResponse = getGroupsByName(jsonRequest).toString();
            break;
        case GET_GRP_MEM:
            strResponse = getGroupsByMember(jsonRequest).toString();
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
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Searches for all groups whose name begins with the given string.
     * 
     * @param json
     *            The JSON object contains the string that is searched for in the groups names.
     * @return Returns a JSON string containing a list of all the groups associated with this name.
     */
    private JSONObject getGroupsByName(JSONObject json) {
        try {
            String name = json.getString(JSONParameter.GROUP_NAME.toString());
            List<Group> groups = groupM.getGroupsByName(name);
            if (groups == null) {
                return ServletUtils.createJSONError(JSONParameter.ErrorCodes.DB_ERROR);
            }
            return ServletUtils.createJSONListGrp(groups);
        } catch (JSONException e) {
            e.printStackTrace();
            return ServletUtils.createJSONError(JSONParameter.ErrorCodes.READ_JSON);
        }
    }

    /**
     * Searches for all groups the given user is a member of.
     * 
     * @param json
     *            The JSON object contains the user that is a member in the groups searched for.
     * @return Returns a JSON string containing a list of all the groups in which the given user is
     *         a member.
     */
    private JSONObject getGroupsByMember(JSONObject json) {
        try {
            int id = json.getInt(JSONParameter.USER_ID.toString());
            List<Group> groups = groupUM.getGroups(id);
            if (groups == null) {
                return ServletUtils.createJSONError(JSONParameter.ErrorCodes.DB_ERROR);
            }
            return ServletUtils.createJSONListGrp(groups);
        } catch (JSONException e) {
            e.printStackTrace();
            return ServletUtils.createJSONError(JSONParameter.ErrorCodes.READ_JSON);
        }
    }

}
