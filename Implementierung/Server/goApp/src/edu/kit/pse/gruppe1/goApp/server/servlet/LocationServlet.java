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

import edu.kit.pse.gruppe1.goApp.server.algorithm.ClusterFacade;
import edu.kit.pse.gruppe1.goApp.server.database.management.EventManagement;
import edu.kit.pse.gruppe1.goApp.server.database.management.UserManagement;
import edu.kit.pse.gruppe1.goApp.server.model.Event;
import edu.kit.pse.gruppe1.goApp.server.model.Location;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.ErrorCodes;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.Methods;

/**
 * Servlet implementation class LocationServlet
 * 
 * This servlet can be used buy event participants to update their location and view the current
 * clustered location of other participants that are on the move.
 */
@WebServlet("/LocationServlet")
public class LocationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final EventManagement event;
    private final UserManagement eventUser;
    private ClusterFacade clusterer = new ClusterFacade();

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
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Evas Code kopiert
        String strResponse = null;
        JSONObject jsonRequest = null;
        JSONParameter.Methods method = null;
        PrintWriter out = null;
        ErrorCodes error = ErrorCodes.OK;

        out = response.getWriter();

        jsonRequest = ServletUtils.extractJSON(request, response);
        if (jsonRequest == null) {
            return;
        }

        try {
            method = JSONParameter.Methods
                    .fromString(jsonRequest.getString(JSONParameter.METHOD.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
            // response.getWriter()
            // .println(ServletUtils.createJSONError(JSONParameter.ErrorCodes.READ_JSON));
            error = ErrorCodes.READ_JSON;
            return;
        }

        if (method == null || !error.equals(ErrorCodes.OK)) {
            method = Methods.NONE;
        }

        switch (method) {
        case SYNC_LOC:
            if (!setGPS(jsonRequest)) {
                // response.getWriter()
                // .println(ServletUtils.createJSONError(JSONParameter.ErrorCodes.READ_JSON));
                error = ErrorCodes.READ_JSON;
            } else {
                // response.getWriter().println(getCluster(jsonRequest).toString());
                strResponse = getCluster(jsonRequest).toString();
            }
            break;
        default:
         //   response.getWriter()
          //          .println(ServletUtils.createJSONError(JSONParameter.ErrorCodes.METH_ERROR));
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
     * A user may update his current gps location if his status is "go" for at least one event. The
     * updated gps location will be used for the next clustering to update the groups location.
     * 
     * @param json
     *            The JSON object must contain the next location of the user.
     * @return Returns a JSON string containing information about the success of this operation.
     */
    private boolean setGPS(JSONObject json) {
        int userId = -1;
        double lat = -1;
        double lon = -1;
        try {
            userId = json.getInt(JSONParameter.USER_ID.toString());
            lat = json.getDouble(JSONParameter.LATITUDE.toString());
            lon = json.getDouble(JSONParameter.LONGITUDE.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        return eventUser.updateLocation(userId, new Location(lon, lat, null));
        // 45 Minutes after current time
    }

    /**
     * A user that participates in an event may request the clustered locations of all other
     * participants whose status is set to "go" if his own status is also set to "go" for this
     * specific event.
     * 
     * @param json
     *            The JSON object contains the event for which the user requests the cluster.
     * @return Returns a JSON string containing the results of the clustering algorithm.
     */
    private JSONObject getCluster(JSONObject json) {
        Event evt = null;
        int eventID = -1;
        try {
            eventID = json.getInt(JSONParameter.EVENT_ID.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return ServletUtils.createJSONError(JSONParameter.ErrorCodes.READ_JSON);
        }
        evt = event.getEvent(eventID);
        if (evt == null) {
            return ServletUtils.createJSONError(JSONParameter.ErrorCodes.DB_ERROR);
        }
        List<Location> cluster = clusterer.getClusteredLocations(evt);

        return ServletUtils.createJSONListLoc(cluster);
    }
}
