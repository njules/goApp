package edu.kit.pse.gruppe1.goApp.server.servlet;

import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import edu.kit.pse.gruppe1.goApp.server.database.management.EventManagement;
import edu.kit.pse.gruppe1.goApp.server.database.management.UserManagement;
import edu.kit.pse.gruppe1.goApp.server.model.Location;
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
	private final UserManagement eventUser;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LocationServlet() {
        super();
        event = new EventManagement();
        eventUser = new UserManagement();
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
        case SYNC_LOC:
            if (!setGPS(jsonRequest)) {
                response.getWriter().println(ServletUtils.createJSONError(JSONParameter.ErrorCodes.METH_ERROR));
            } else {
                response.getWriter().println(getCluster(jsonRequest));
            }
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
	private boolean setGPS(JSONObject json) {
		try {
		    int user = json.getInt(JSONParameter.USER_ID.toString());
		    int lat = json.getInt(JSONParameter.LATITUDE.toString());
		    int lon = json.getInt(JSONParameter.LONGITUDE.toString());
		    eventUser.getUser(user).setLocation(new Location(lon, lat, null));
		    return true;
		} catch (JSONException e) {
		    e.printStackTrace();
		    return false;
		}
	}

	/**
	 * A user that participates in an event may request the clustered locations of all other participants whose status is set to "go" if his own status is also set to "go" for this specific event.
	 * @param json The JSON object contains the event for which the user requests the cluster.
	 * @return Returns a JSON string containing the results of the clustering algorithm.
	 */
	private String getCluster(JSONObject json) {
	    try {
	        int eventID = json.getInt(JSONParameter.EVENT_ID.toString());
	        return ServletUtils.createJSONListLoc(new ArrayList<Location>(event.getEvent(eventID).getClusterPoints())).toString();
	    } catch (JSONException e) {
            e.printStackTrace();
            return ServletUtils.createJSONError(JSONParameter.ErrorCodes.READ_JSON).toString();
	    }
	}
}
