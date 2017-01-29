package edu.kit.pse.gruppe1.goApp.server.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.Methods;

/**
 * Servlet implementation class GroupServlet
 * 
 * This servlet contains all methods to create, manipulate and delete groups.
 */
@WebServlet("/GroupServlet")
public class GroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GroupServlet() {
        super();
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
        case CREATE:
            response.getWriter().println(create(jsonRequest));
            break;
        case DELETE:
            response.getWriter().println(delete(jsonRequest));
            break;
        case SET_NAME:
            response.getWriter().println(setName(jsonRequest));
            break;
        case DEL_MEM:
            response.getWriter().println(deleteMember(jsonRequest));
            break;
        case GET_EVENT:
            response.getWriter().println(getEvents(jsonRequest));
            break;
        case GET_GROUP:
            response.getWriter().println(getGroup(jsonRequest));
            break;
        case SET_FOUNDER:
            response.getWriter().println(setFounder(jsonRequest));
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
	 * This method creates a new group. A user can create a new group if has not yet reached the group limit. By creating a group his group count is incremented by one. He is also registered as the groups founder.
	 * @param json This JSON objects contains information about the group that is to be created such as the Name and the founding member.
	 * @return Returns a JSON string containing the ID of the created group.
	 */
	private String create(JSONObject json) {
		// TODO - implement GroupServlet.create
		throw new UnsupportedOperationException();
	}

	/**
	 * This method may be called by a founder to delete his group. The group will be removed from the database as well as all events and the users are removed from this group. The group count of all users in this group is reduced by one.
	 * @param json JSON object containing the ID of the group that is to be deleted.
	 * @return Returns a JSON string containing information about the success of this operation.
	 */
	private String delete(JSONObject json) {
		// TODO - implement GroupServlet.delete
		throw new UnsupportedOperationException();
	}

	/**
	 * This method may change the name of a given group. Only a groups founder may change its name.
	 * @param json JSON object containing the ID of the group on which the name change shall be executed and the new name of the group in a string.
	 * @return Returns a JSON string containing information about the success of this operation.
	 */
	private String setName(JSONObject json) {
		// TODO - implement GroupServlet.setName
		throw new UnsupportedOperationException();
	}

	/**
	 * A founder may call this method in order to remove members from his group. They will also be removed from all events in this group and their group count is decremented by one.
	 * @param json JSON object containing the ID of the group from which the member shall be removed and the user that should be removed.
	 * @return Returns a JSON string containing information about the success of this operation.
	 */
	private String deleteMember(JSONObject json) {
		// TODO - implement GroupServlet.deleteMember
		throw new UnsupportedOperationException();
	}

	/**
	 * Any user may request a list of events from any group he is a member of.
	 * @param json JSON object containing the ID of the group from which the events are requested.
	 * @return Returns a JSON string containing a List with all Events within that group.
	 */
	private String getEvents(JSONObject json) {
		// TODO - implement GroupServlet.getEvents
		throw new UnsupportedOperationException();
	}

	/**
	 * This method returns all relevant information about a given group and may be invoked by any member of that group.
	 * @param json JSON object containing the ID of the group about which the information is requested.
	 * @return Returns a JSON string containing information about the group such as members, events, the founder and users that have sent unanswered requests to this group.
	 */
	private String getGroup(JSONObject json) {
		// TODO - implement GroupServlet.getGroup
		throw new UnsupportedOperationException();
	}

	/**
	 * If a founder wishes to resign, he can call this method to transfer his rights to another user. Only a founder may invoke this method on his group. If a founder leaves his group this method is called automatically. Only a user that is currently a member in this group may be elected as the new groups founder.
	 * @param json JSON object containing the ID of the group whose founder shall be changed and the member of the group that is to become the founder.
	 * @return Returns a JSON string containing information about the success of this operation.
	 */
	private String setFounder(JSONObject json) {
		// TODO - implement GroupServlet.setFounder
		throw new UnsupportedOperationException();
	}
	
}
