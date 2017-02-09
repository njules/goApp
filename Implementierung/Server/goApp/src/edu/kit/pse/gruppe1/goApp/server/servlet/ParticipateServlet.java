package edu.kit.pse.gruppe1.goApp.server.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import edu.kit.pse.gruppe1.goApp.server.database.management.EventUserManagement;
import edu.kit.pse.gruppe1.goApp.server.model.Status;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.Methods;

/**
 * Servlet implementation class ParticipateServlet
 * 
 * Users can access methods in this servlet to indicate whether they want to participate in an event from the group.
 */
@WebServlet("/ParticipateServlet")
public class ParticipateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final EventUserManagement eventUser;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ParticipateServlet() {
        super();
        eventUser = new EventUserManagement();
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
            method = JSONParameter.Methods.fromString(jsonRequest.getString(JSONParameter.METHOD.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
            response.getWriter().println(ServletUtils.createJSONError(JSONParameter.ErrorCodes.READ_JSON));
            return;
        }
        
        if (method == null) {
            method = Methods.NONE;
        }
        
        switch (method) {
        case SET_STATUS:
            response.getWriter().println(setStatus(jsonRequest));
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
 	 * A user may accept an invitation to any event in a group he is a member of. He may accept or reject any event only once and can not invoke any of these two methods again later on for the same event.
	 * @param json A JSON object that contains the user wanting to accept the invite and the event he wants to participate in.
	 * @return Returns a JSON string containing information about the success of this operation.
	 */
    @Deprecated
	private String accept(JSONObject json) {
        JSONObject response = new JSONObject();
        try {
            int event = Integer.parseInt(json.getString(JSONParameter.EVENT_ID.toString()));
            int user = Integer.parseInt(json.getString(JSONParameter.USER_ID.toString()));
            if (!eventUser.updateStatus(event, user, Status.PARTICIPATE)) {
                return ServletUtils.createJSONError(JSONParameter.ErrorCodes.METH_ERROR).toString();
            }
            response.append(JSONParameter.ERROR_CODE.toString(), JSONParameter.ErrorCodes.OK);
        } catch (JSONException e) {
            e.printStackTrace();
            return ServletUtils.createJSONError(JSONParameter.ErrorCodes.READ_JSON).toString();
        }
        return response.toString();
	}

	/**
	 * A user may reject an invitation to any event in a group he is a member of. He may accept or reject any event only once and can not invoke any of these two methods again later on for the same event.
	 * @param json A JSON object that contains the user wanting to decline the invite and the event he wants to abstain from.
	 * @return Returns a JSON string containing information about the success of this operation.
	 */
    @Deprecated
	private String reject(JSONObject json) {
        JSONObject response = new JSONObject();
        try {
            int event = Integer.parseInt(json.getString(JSONParameter.EVENT_ID.toString()));
            int user = Integer.parseInt(json.getString(JSONParameter.USER_ID.toString()));
            if (!eventUser.delete(event, user)) {
                return ServletUtils.createJSONError(JSONParameter.ErrorCodes.METH_ERROR).toString();
            }
            response.append(JSONParameter.ERROR_CODE.toString(), JSONParameter.ErrorCodes.OK);
        } catch (JSONException e) {
            e.printStackTrace();
            return ServletUtils.createJSONError(JSONParameter.ErrorCodes.READ_JSON).toString();
        }
        return response.toString();
	}
	
	/**
     * A user may reject and accept an invitation to any event in a group he is a member of. He may accept or reject any event only once and can not invoke any of these two methods again later on for the same event. Has he accepted an event, he is participating in it. While participating he can update his status to "go" in order to get access to the groups position and get tracked himself.
     * @param json A JSON object that contains the user wanting to update his status, the status he wants it to be set to and the event for which this action shall take place.
     * @return Returns a JSON string containing information about the success of this operation.
     */
	private String setStatus(JSONObject json) {
        try {
            int event = json.getInt(JSONParameter.EVENT_ID.toString());
            int user = json.getInt(JSONParameter.USER_ID.toString());
            //TODO read status
            Status status = null;
            if (!eventUser.updateStatus(event, user, status)) {
                return ServletUtils.createJSONError(JSONParameter.ErrorCodes.METH_ERROR).toString();
            }
            return ServletUtils.createJSONError(JSONParameter.ErrorCodes.OK).toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return ServletUtils.createJSONError(JSONParameter.ErrorCodes.READ_JSON).toString();
        }
	}
}
