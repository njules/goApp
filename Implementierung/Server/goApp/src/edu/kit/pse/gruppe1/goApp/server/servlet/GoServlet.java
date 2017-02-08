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

import edu.kit.pse.gruppe1.goApp.server.database.management.EventUserManagement;
import edu.kit.pse.gruppe1.goApp.server.model.Status;
import edu.kit.pse.gruppe1.goApp.server.model.User;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.ErrorCodes;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.Methods;

/**
 * Servlet implementation class GoServlet
 * 
 * This servlets functions revolve around the go button and allow users to signal they started approaching the events location and view the status of other participants in this event.
 *
 */
@WebServlet("/GoServlet")
public class GoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final EventUserManagement eventUser;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoServlet() {
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
            method = JSONParameter.Methods.fromString(jsonRequest.getString(JSONParameter.Method.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
            response.getWriter().println(ServletUtils.createJSONError(JSONParameter.ErrorCodes.READ_JSON));
            return;
        }
        
        if (method == null) {
            method = Methods.NONE;
        }
        
        switch (method) {
        case GET_START:
            response.getWriter().println(getStartedParticipants(jsonRequest));
            break;
        case SET_START:
            response.getWriter().println(setStarted(jsonRequest));
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
	 * A participant that has previously set his status to "go" may request a list of fellow participants that are also on the way.
	 * @param json JSON object containing the event the participant list is requested for.
	 * @return Returns a JSON string containing a list with all participants of this event that have set their status to "go".
	 */
	private String getStartedParticipants(JSONObject json) {
        JSONObject response = new JSONObject();
        try {
            int event = json.getInt(JSONParameter.EventID.toString());
            Status status = Status.STARTED;
            List<User> userList = eventUser.getUserByStatus(status, event);
            for (User user : userList) {
                response.append(JSONParameter.UserID.toString(), user.getUserId());
                response.append(JSONParameter.UserName.toString(), user.getName());
            }
            response.append(JSONParameter.ErrorCode.toString(), JSONParameter.ErrorCodes.OK);
        } catch (JSONException e) {
            e.printStackTrace();
            return ServletUtils.createJSONError(JSONParameter.ErrorCodes.READ_JSON).toString();
        }
        return response.toString();
	}

	/**
	 * Allows the participant of an event to set his status to "go" which enables position tracking and viewing of other participants positions. The participant must have previously accepted the invitation to this event.
	 * @param json The JSON object contains the user that updates his status and the event he updates it for.
	 * @return Returns a JSON string containing information about the success of this operation.
	 */
	private String setStarted(JSONObject json) {
        JSONObject response = new JSONObject();
        try {
            int user = json.getInt(JSONParameter.UserID.toString());
            int event = json.getInt(JSONParameter.EventID.toString());
            Status status = Status.STARTED;
            if (eventUser.updateStatus(event, user, status)) {
                response.append(JSONParameter.ErrorCode.toString(), JSONParameter.ErrorCodes.OK);
            } else {
                return ServletUtils.createJSONError(JSONParameter.ErrorCodes.METH_ERROR).toString();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return ServletUtils.createJSONError(JSONParameter.ErrorCodes.READ_JSON).toString();
        }
        return response.toString();
	}

}
