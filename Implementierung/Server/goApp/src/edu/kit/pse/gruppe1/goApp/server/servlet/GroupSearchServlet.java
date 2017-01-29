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

import edu.kit.pse.gruppe1.goApp.server.database.management.GroupManagement;
import edu.kit.pse.gruppe1.goApp.server.database.management.GroupUserManagement;
import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.Methods;

/**
 * Servlet implementation class GroupSearchServlet
 * 
 * This servlet offers methods to search for groups with a specific name or in which a given user is the member.
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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONObject jsonRequest = ServletUtils.extractJSON(request, response);
        if (jsonRequest == null) {
            return;
        }
        Methods method;
        try {
            method = JSONParameter.Methods.fromString(jsonRequest.getString(JSONParameter.Method.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
            response.getWriter().println(ServletUtils.createJSONError(JSONParameter.ErrorCodes.READ_JSON));
            return;
        }
        switch (method) {
        case GET_GRP_NAME:
            response.getWriter().println(getGroupsByName(jsonRequest));
            break;
        case GET_GRP_MEM:
            response.getWriter().println(getGroupsByMember(jsonRequest));
            break;
        default: 
            response.getWriter().println(ServletUtils.createJSONError(JSONParameter.ErrorCodes.METH_ERROR));
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	/**
	 * Searches for all groups whose name begins with the given string.
	 * @param json The JSON object contains the string that is searched for in the groups names.
	 * @return Returns a JSON string containing a list of all the groups associated with this name.
	 */
	private String getGroupsByName(JSONObject json) {
        JSONObject response = new JSONObject();
        try {
            String name = json.getString(JSONParameter.GroupName.toString());
            List<Group> groups;
            groups = groupM.getGroupsByName(name);
            for (Group group : groups) {
                response.append(JSONParameter.GroupID.toString(), group.getGroupId());
                response.append(JSONParameter.GroupName.toString(), group.getName());
            }
            response.append(JSONParameter.ErrorCode.toString(), JSONParameter.ErrorCodes.OK);
        } catch (JSONException e) {
            e.printStackTrace();
            return ServletUtils.createJSONError(JSONParameter.ErrorCodes.READ_JSON).toString();
        }
        return response.toString();
	}

	/**
	 * Searches for all groups the given user is a member of.
	 * @param json The JSON object contains the user that is a member in the groups searched for.
	 * @return Returns a JSON string containing a list of all the groups in which the given user is a member.
	 */
	private String getGroupsByMember(JSONObject json) {
        JSONObject response = new JSONObject();
        try {
            int ID = Integer.parseInt(json.getString(JSONParameter.UserID.toString()));
            List<Group> groups;
            groups = groupUM.getGroups(ID);
            for (Group group : groups) {
                response.append(JSONParameter.GroupID.toString(), group.getGroupId());
                response.append(JSONParameter.GroupName.toString(), group.getName());
            }
            response.append(JSONParameter.ErrorCode.toString(), JSONParameter.ErrorCodes.OK);
        } catch (JSONException e) {
            e.printStackTrace();
            return ServletUtils.createJSONError(JSONParameter.ErrorCodes.READ_JSON).toString();
        }
        return response.toString();
	}
	
}
