package edu.kit.pse.gruppe1.goApp.server.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import edu.kit.pse.gruppe1.goApp.server.database.management.EventManagement;
import edu.kit.pse.gruppe1.goApp.server.database.management.EventUserManagement;
import edu.kit.pse.gruppe1.goApp.server.model.Location;
import edu.kit.pse.gruppe1.goApp.server.model.Status;
import edu.kit.pse.gruppe1.goApp.server.model.User;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.ErrorCodes;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.Methods;

/**
 * Servlet implementation class LocationServlet
 * 
 * This servlet can be used buy event participants to update their location and view the current clustered location of other participants that are on the move.
 */
@WebServlet("/LocationServlet")
public class LocationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final EventManagement event;
	private final EventUserManagement eventUser;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LocationServlet() {
        super();
        event = new EventManagement();
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
        case GET_CLUSTER:
            response.getWriter().println(getCluster(jsonRequest));
            break;
        case SET_GPS:
            response.getWriter().println(setGPS(jsonRequest));
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
	 * A user may update his current gps location if his status is "go" for at least one event. The updated gps location will be used for the next clustering to update the groups location.
	 * @param json The JSON object must contain the next location of the user.
	 * @return Returns a JSON string containing information about the success of this operation.
	 */
	private String setGPS(JSONObject json) {
		JSONObject response = new JSONObject();
		try {
		    int event = json.getInt(JSONParameter.EVENT_ID.toString());
		    int user = json.getInt(JSONParameter.USER_ID.toString());
		    int lat = json.getInt(JSONParameter.LATITUDE.toString());
		    int lon = json.getInt(JSONParameter.LONGITUDE.toString());
		    Location location = new Location(lon, lat, null);
		    List<User> userList = eventUser.getUserByStatus(Status.STARTED, event);
		    for (User participant : userList) {
		        if (participant.getUserId() == user) {
		            participant.setLocation(location);
		            location = null;
		        }
		    }
		    if (location != null) {
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
	 * A user that participates in an event may request the clustered locations of all other participants whose status is set to "go" if his own status is also set to "go" for this specific event.
	 * @param json The JSON object contains the event for which the user requests the cluster.
	 * @return Returns a JSON string containing the results of the clustering algorithm.
	 */
	private String getCluster(JSONObject json) {
	    JSONObject response = new JSONObject();
	    try {
	        int eventID = Integer.parseInt(json.getString(JSONParameter.EVENT_ID.toString()));
	        Set<Location> locations = event.getEvent(eventID).getClusterPoints();
	        for (Location location:locations) {
	            response.append(JSONParameter.LATITUDE.toString(), location.getLatitude());
	            response.append(JSONParameter.LONGITUDE.toString(), location.getLongitude());
	            response.append(JSONParameter.LOC_NAME.toString(), location.getName());
	        }
	        response.append(JSONParameter.ERROR_CODE.toString(), JSONParameter.ErrorCodes.OK);
	    } catch (JSONException e) {
            e.printStackTrace();
            return ServletUtils.createJSONError(JSONParameter.ErrorCodes.READ_JSON).toString();
	    }
        return response.toString();
	}
}
