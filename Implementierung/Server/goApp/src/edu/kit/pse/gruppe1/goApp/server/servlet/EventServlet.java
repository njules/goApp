package edu.kit.pse.gruppe1.goApp.server.servlet;

import edu.kit.pse.gruppe1.goApp.server.database.management.EventManagement;
import edu.kit.pse.gruppe1.goApp.server.database.management.EventUserManagement;
import edu.kit.pse.gruppe1.goApp.server.model.Event;
import edu.kit.pse.gruppe1.goApp.server.model.Location;
import edu.kit.pse.gruppe1.goApp.server.model.Participant;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.ErrorCodes;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.Methods;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class EventServlet
 * 
 * This servlet is used to create, view and edit events within a given group.
 */
@WebServlet("/EventServlet")
public class EventServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private EventManagement eventMang;
    private EventUserManagement eventUsrMang;

    /**
     * Default constructor.
     */
    public EventServlet() {
        super();
        this.eventMang = new EventManagement();
        this.eventUsrMang = new EventUserManagement();
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
        case CREATE:
            strResponse = create(jsonRequest).toString();
            break;
        case GET_EVENT:
            strResponse = getParticipates(jsonRequest).toString();
            break;
        case CHANGE:
            strResponse = change(jsonRequest).toString();
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
     * Any user, that is a member of a group may create an event within this group. The member that
     * creates this event will be registered as the event admin. The admin has the right to change
     * data about his event. Each member of a group may only be admin of one event within this
     * group.
     * 
     * @param json
     *            This JSON object contains all information about the new event such as event time,
     *            location, event name and the user creating this event.
     * @return A JSON string containing the previously created event is returned.
     */
    private JSONObject create(JSONObject json) {
        Event event = null;
        Timestamp time = null;
        String name = null;
        Location location = null;
        double longitude = -1;
        double latitude = -1;
        String locName = null;
        int creatorID = -1;
        int groupID = -1;
        JSONParameter.ErrorCodes err = ErrorCodes.OK;

        // get all parameter from json
        try {
            name = json.getString(JSONParameter.EVENT_NAME.toString());
            longitude = json.getDouble(JSONParameter.LONGITUDE.toString());
            latitude = json.getDouble(JSONParameter.LATITUDE.toString());
            locName = json.getString(JSONParameter.LOC_NAME.toString());
            time = new Timestamp(json.getLong(JSONParameter.EVENT_TIME.toString()));
            creatorID = json.getInt(JSONParameter.USER_ID.toString());
            groupID = json.getInt(JSONParameter.GROUP_ID.toString());
        } catch (JSONException e) {
            err = ErrorCodes.READ_JSON;
        }
        location = new Location(longitude, latitude, locName);
        event = this.eventMang.add(name, location, time, creatorID, groupID);

        if (event == null) {
            err = ErrorCodes.DB_ERROR;
        }

        return ServletUtils.createJSONError(err);
    }

   /**
    * gets all participates to given event
    * @param json jsonString with eventID
    * @return List of Participates
    */
   private JSONObject getParticipates(JSONObject json) {
        List<Participant> part = null;
        try {
            int eventID = json.getInt(JSONParameter.EVENT_ID.toString());
            part = this.eventUsrMang.getParticipants(eventID);
        } catch (JSONException e) {
            return ServletUtils.createJSONError(ErrorCodes.READ_JSON);
        }

        return ServletUtils.createJSONListPart(part);
    }

    /**
     * This method may only be invoked by an event admin and he may only change the event, he
     * administrates. He may update all information such as name, location and date. He may also
     * elect a new admin for this event or delete it.
     * 
     * @param json
     *            The JSON object contains an event with the updated information.
     * @return A JSON string containing the updated information about the event is returned.
     */
    private JSONObject change(JSONObject json) {
        Event event = null;
        boolean valuesChanged = false;
        JSONParameter.ErrorCodes error = ErrorCodes.OK;
        int eventID = -1;

        try {
            eventID = json.getInt(JSONParameter.EVENT_ID.toString());
            event = this.eventMang.getEvent(eventID);
        } catch (JSONException e) {
            error = ErrorCodes.READ_JSON;
            return ServletUtils.createJSONError(error);
        }

        // for the following part: if attribute is in json Object - change value in Event
        try {
            String name = json.getString(JSONParameter.EVENT_NAME.toString());
            event.setName(name);
            valuesChanged = true;
        } catch (JSONException e) {
            // do nothing, because it can happen
        }
        try {
            double longitude = json.getDouble(JSONParameter.LONGITUDE.toString());
            double latitude = json.getDouble(JSONParameter.LATITUDE.toString());
            Location loc = event.getLocation();
            loc.setLatitude(latitude);
            loc.setLongitude(longitude);
            event.setLocation(loc);
            valuesChanged = true;
        } catch (JSONException e) {
            // do nothing, because it can happen
        }

        try {
            String locName = json.getString(JSONParameter.LOC_NAME.toString());
            Location loc = event.getLocation();
            loc.setName(locName);
            event.setLocation(loc);
            valuesChanged = true;
        } catch (JSONException e) {
            // do nothing, because it can happen
        }

        try {
            Timestamp time = (Timestamp) json.get(JSONParameter.EVENT_TIME.toString());
            event.setTimestamp(time);
            valuesChanged = true;
        } catch (JSONException e) {
            // do nothing, because it can happen
        }
        

        if (valuesChanged) {
            if (!eventMang.update(event)) {
                error = ErrorCodes.DB_ERROR;
            }
        }
        return ServletUtils.createJSONError(error);
    }
    
    // Code for use in future Version in change
    // try {
    // int creatorID = json.getInt(JSONParameter.USER_ID.toString());
    // UserManagement usMang = new UserManagement();
    // User creator = usMang.getUser(creatorID);
    // if (creator != null) {
    // event.setCreator(creator);
    // valuesChanged = true;
    // } else {
    // error = ErrorCodes.DB_ERROR;
    // }
    // } catch (JSONException e) {
    // // do nothing, because it can happen
    // }
    // try {
    // int groupID = json.getInt(JSONParameter.GRUOP_ID.toString());
    // GroupManagement grMang = new GroupManagement();
    // Group group = grMang.getGroup(groupID);
    // if (group != null) {
    // event.setGroup(group);
    // valuesChanged = true;
    // } else {
    // error = ErrorCodes.DB_ERROR;
    // }
    // } catch (JSONException e) {
    // // do nothing, because it can happen
    // }

}
