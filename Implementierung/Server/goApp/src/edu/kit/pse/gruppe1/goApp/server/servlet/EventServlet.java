package edu.kit.pse.gruppe1.goApp.server.servlet;

import edu.kit.pse.gruppe1.goApp.server.database.management.EventManagement;
import edu.kit.pse.gruppe1.goApp.server.database.management.GroupManagement;
import edu.kit.pse.gruppe1.goApp.server.database.management.UserManagement;
import edu.kit.pse.gruppe1.goApp.server.model.Event;
import edu.kit.pse.gruppe1.goApp.server.model.Group;
import edu.kit.pse.gruppe1.goApp.server.model.Location;
import edu.kit.pse.gruppe1.goApp.server.model.User;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.ErrorCodes;
import edu.kit.pse.gruppe1.goApp.server.servlet.JSONParameter.Methods;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;

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

    /**
     * Default constructor.
     */
    public EventServlet() {
        super();
        this.eventMang = new EventManagement();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // String strResponse = null;
        // String jsonString = null;
        // JSONParameter.Methods method = null;
        // response.setContentType("text/plain");
        // PrintWriter out = null;
        // // try {
        // out = response.getWriter();
        // jsonString = request.getReader().readLine();
        // // } catch (IOException e1) {
        // // strResponse = ServletUtils.createJSONError(ErrorCodes.IO_ERROR);
        // // out.println(strResponse);
        // // return;
        // // }
        //
        // if (jsonString == null) {
        // strResponse = ServletUtils.createJSONError(ErrorCodes.EMPTY_JSON);
        // out.println(strResponse);
        // return;
        // }
        // try {
        // JSONObject jsonRequest = new JSONObject(jsonString);
        // method = JSONParameter.Methods
        // .fromString(jsonRequest.getString(JSONParameter.Method.toString()));
        // switch (method) {
        // case CREATE:
        // strResponse = create(jsonRequest);
        // break;
        // case GET_EVENT:
        // strResponse = getEvent(jsonRequest);
        // break;
        // case CHANGE:
        // strResponse = change(jsonRequest);
        // break;
        // default:
        // strResponse = ServletUtils.createJSONError(ErrorCodes.METH_ERROR);
        // break;
        // }
        // out.println(strResponse);
        // } catch (JSONException e) {
        // strResponse = ServletUtils.createJSONError(ErrorCodes.READ_JSON);
        // out.println(strResponse);
        // }

        // TODO: delete old one, if new does work
        String strResponse = null;
        JSONObject jsonRequest = null;
        JSONParameter.Methods method = null;
        PrintWriter out = null;
        ErrorCodes error = ErrorCodes.OK;

        out = response.getWriter();

        try {
            method = ServletUtils.getMethod(request, jsonRequest);
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
            strResponse = create(jsonRequest);
            break;
        case GET_EVENT:
            strResponse = getEvent(jsonRequest);
            break;
        case CHANGE:
            strResponse = change(jsonRequest);
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
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
    private String create(JSONObject json) {
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
            name = json.getString(JSONParameter.EventName.toString());
            longitude = json.getDouble(JSONParameter.Longitude.toString());
            latitude = json.getDouble(JSONParameter.Latitude.toString());
            locName = json.getString(JSONParameter.LocationName.toString());
            time = new Timestamp(json.getLong(JSONParameter.EventTime.toString()));
            creatorID = json.getInt(JSONParameter.UserID.toString());
            groupID = json.getInt(JSONParameter.GroupID.toString());
        } catch (JSONException e) {
            err = ErrorCodes.READ_JSON;
        }
        location = new Location(longitude, latitude, locName);
        event = this.eventMang.add(name, location, time, creatorID, groupID);

        if (event == null) {
            err = ErrorCodes.DB_ERROR;
        }

        return createJSONObject(event, err);
    }

    /**
     * calls methods for creating Error or Event JSONObject
     * 
     * @param event
     *            Event to serialize
     * @param error
     *            Error to serialize
     * @return String with serialized JSONObject
     */
    private String createJSONObject(Event event, JSONParameter.ErrorCodes error) {
        JSONObject result = null;
        if (error.equals(ErrorCodes.OK)) {
            result = ServletUtils.createJSONEvent(event);
        } else {
            result = ServletUtils.createJSONError(error);
        }
        return result.toString();
    }

    /**
     * A method used to access information about an event. Every user, that can see this event may
     * request information about it. Users that are not a member of the group may not view the
     * groups events. Accessible information includes name, location, admin and time.
     * 
     * @param json
     *            A JSON object containing the event about which the information is requested.
     * @return A JSON string containing all information about the given event is returned.
     */
    private String getEvent(JSONObject json) {
        Event event = null;
        JSONParameter.ErrorCodes error = ErrorCodes.OK;

        try {
            int eventID = json.getInt(JSONParameter.EventID.toString());
            event = this.eventMang.getEvent(eventID);
        } catch (JSONException e) {
            error = ErrorCodes.READ_JSON;
        }

        return createJSONObject(event, error);

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
    private String change(JSONObject json) {
        Event event = null;
        boolean valuesChanged = false;
        JSONParameter.ErrorCodes error = ErrorCodes.OK;
        int eventID = -1;

        try {
            eventID = json.getInt(JSONParameter.EventID.toString());
            event = this.eventMang.getEvent(eventID);
        } catch (JSONException e) {
            error = ErrorCodes.READ_JSON;
            return createJSONObject(event, error);
        }

        // for the following part: if attribute is in json Object - change value in Event
        try {
            String name = json.getString(JSONParameter.EventName.toString());
            event.setName(name);
            valuesChanged = true;
        } catch (JSONException e) {
            // do nothing, because it can happen
        }
        try {
            double longitude = json.getDouble(JSONParameter.Longitude.toString());
            double latitude = json.getDouble(JSONParameter.Latitude.toString());
            Location loc = event.getLocation();
            loc.setLatitude(latitude);
            loc.setLongitude(longitude);
            event.setLocation(loc);
            valuesChanged = true;
        } catch (JSONException e) {
            // do nothing, because it can happen
        }

        try {
            String locName = json.getString(JSONParameter.LocationName.toString());
            Location loc = event.getLocation();
            loc.setName(locName);
            event.setLocation(loc);
            valuesChanged = true;
        } catch (JSONException e) {
            // do nothing, because it can happen
        }

        try {
            Timestamp time = (Timestamp) json.get(JSONParameter.EventTime.toString());
            event.setTimestamp(time);
            valuesChanged = true;
        } catch (JSONException e) {
            // do nothing, because it can happen
        }

        try {
            int creatorID = json.getInt(JSONParameter.UserID.toString());
            UserManagement usMang = new UserManagement();
            User creator = usMang.getUser(creatorID);
            if (creator != null) {
                event.setCreator(creator);
                valuesChanged = true;
            } else {
                error = ErrorCodes.DB_ERROR;
            }
        } catch (JSONException e) {
            // do nothing, because it can happen
        }
        try {
            int groupID = json.getInt(JSONParameter.GroupID.toString());
            GroupManagement grMang = new GroupManagement();
            Group group = grMang.getGroup(groupID);
            if (group != null) {
                event.setGroup(group);
                valuesChanged = true;
            } else {
                error = ErrorCodes.DB_ERROR;
            }
        } catch (JSONException e) {
            // do nothing, because it can happen
        }

        if (valuesChanged) {
            eventMang.update(event);
        }
        event = this.eventMang.getEvent(eventID);
        return createJSONObject(event, error);
    }

}
