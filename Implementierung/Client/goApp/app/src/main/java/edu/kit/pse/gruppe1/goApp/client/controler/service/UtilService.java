package edu.kit.pse.gruppe1.goApp.client.controler.service;

import android.support.annotation.Nullable;
import android.util.Log;
import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.JSONParameter;
import edu.kit.pse.gruppe1.goApp.client.model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Timestamp;


/**
 * Created by Katharina Riesterer on 07.02.2017.
 */

public final class UtilService {

    //keys of extras in intents used for communication between activties and services
    public static final String JSON = "json";
    public static final String USERS = "users";
    public static final String GROUPS = "groups";
    public static final String USER = "user";
    public static final String LOCATION = "location";
    public static final String LOCATIONS = "locations";
    public static final String STATUS = "status";
    public static final String ERROR = "error";
    public static final String EVENTS = "events";
    public static final String NEW_EVENTS = "newEvents";
    public static final String ACCEPTED_EVENTS = "acceptedEvents";
    public static final String EVENT = "event";
    public static final String GROUP = "group";
    public static final String NAME = "name";
    public static final String STARTED_USERS = "Started_Users";
    //error messages
    private static final String USER_LIMIT = "Group is full";
    private static final String GROUP_LIMIT = "You can only be in 20 groups";
    private static final String SERVER_FAILED = "Our server has some problems";
    private static final String CONNECTION_FAILED = "Please check your internet connection";
    private static final String OK = "ok";


    /**
     * Extracts multiple groups from a jsonobject coming from the server
     * @param json the source to extract the groups from
     * @return an array of groups
     */
    @Nullable
    public static Group[] getGroups(JSONObject json) {
        try {
            JSONArray jsons = json.getJSONArray(JSONParameter.LIST_GROUP.toString());
            Group[] groups = new Group[jsons.length()];
            for (int i = 0; i < jsons.length(); i++) {
                JSONObject group = jsons.getJSONObject(i);
                User founder = new User(group.getInt(JSONParameter.USER_ID.toString()),
                        group.getString(JSONParameter.USER_NAME.toString()));
                groups[i] = new Group(group.getInt(JSONParameter.GROUP_ID.toString()),
                        group.getString(JSONParameter.GROUP_NAME.toString()),
                        founder);
            }

            return groups;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Extracts multiple users from a jsonobject coming from the server
     * @param json the source to extract the users from
     * @return an array of users
     */
    public static User[] getUsers(JSONObject json) {
        try {
            JSONArray jsons = json.getJSONArray(JSONParameter.LIST_USER.toString());
            User[] users = new User[jsons.length()];
            for (int i = 0; i < jsons.length(); i++) {
                JSONObject user = jsons.getJSONObject(i);
                users[i] = new User(user.getInt(JSONParameter.USER_ID.toString()),
                        user.getString(JSONParameter.USER_NAME.toString()));
            }
            return users;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Extracts multiple user who participate in an event from a jsonobject coming from the server
     * @param json the source to extract the users from
     * @return an array of users with their statuses
     */
    public static User[] getParticipants(JSONObject json,String parameter) {
        try {
            JSONArray jsons = json.getJSONArray(parameter);
            User[] users = new User[jsons.length()];
            for (int i = 0; i < jsons.length(); i++) {
                JSONObject user = jsons.getJSONObject(i);
                users[i] = new User(user.getInt(JSONParameter.USER_ID.toString()),
                        user.getString(JSONParameter.USER_NAME.toString()));
                users[i].setStatus(Status.valueOf(user.getString(JSONParameter.STATUS.toString())));
            }
            return users;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Extracts multiple locations from a json object coming from the server (used for clustering points)
     * @param json the source to extract the locations from
     * @return an array of locations
     */
    public static Location[] getLocations(JSONObject json) {
        try {

            JSONArray jsons = json.getJSONArray(JSONParameter.LIST_LOC.toString());
            Location[] locations = new Location[jsons.length()];
            for (int i = 0; i < jsons.length(); i++) {
                JSONObject location = jsons.getJSONObject(i);
                locations[i] = new Location(location.getDouble(JSONParameter.LATITUDE.toString()),
                        location.getDouble(JSONParameter.LONGITUDE.toString()),
                        "Central Point");
                //TODO location.getString(JSONParameter.LOC_NAME.toString())
            }
            return locations;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Tests if an error occurred on the server
     * @param json the source to extract the error from
     * @return true if an error occured and false if everything went oke
     */
    public static boolean isError(JSONObject json) {
        try {
            if (json.getInt(JSONParameter.ERROR_CODE.toString()) == JSONParameter.ErrorCodes.OK.getErrorCode()) {
                return false;
            } else {
                Log.e("ERROR", json.getString(JSONParameter.ERROR_CODE.toString()));
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return true;
        }
    }

    /**
     * gets an error massage depending on what kind of error occurred
     * @param json the source to extract the error message
     * @return a userfriendly string to be shown by the UI
     */
    public static String getError(JSONObject json) {
        int error = -1;
        try {
            error = json.getInt(JSONParameter.ERROR_CODE.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (error == JSONParameter.ErrorCodes.USR_LIMIT.getErrorCode()) {
            return USER_LIMIT;
        } else if (error == JSONParameter.ErrorCodes.GRP_LIMIT.getErrorCode()) {
            return GROUP_LIMIT;
        } else if (error == JSONParameter.ErrorCodes.CONNECTION_FAILED.getErrorCode()) {
            return CONNECTION_FAILED;
        } else if (error == JSONParameter.ErrorCodes.OK.getErrorCode()) {
            return OK;
        }else if(error == JSONParameter.ErrorCodes.EMPTY_LIST.getErrorCode()){
            return null;
        } else {
            return SERVER_FAILED;
        }
    }

    /**
     * Extracts multiple events from a json object coming from the server
     * @param jsons the source to extract the events from
     * @return an array of events
     */
    public static Event[] getEvents(JSONArray jsons) {
        try {
            Event[] events = new Event[jsons.length()];
            for (int i = 0; i < jsons.length(); i++) {
                JSONObject event = jsons.getJSONObject(i);
                Location destination = new Location(event.getDouble(JSONParameter.LATITUDE.toString()), event.getDouble(JSONParameter.LONGITUDE.toString()), event.getString(JSONParameter.LOC_NAME.toString()));
                User founder = new User(event.getInt(JSONParameter.USER_ID.toString()), event.getString(JSONParameter.USER_NAME.toString()));
                events[i] = new Event(
                        event.getInt(JSONParameter.EVENT_ID.toString()),
                        event.getString(JSONParameter.EVENT_NAME.toString()),
                        new Timestamp(event.getLong(JSONParameter.EVENT_TIME.toString())),
                        destination, founder);
            }
            return events;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * Extracts multiple events from a json object coming from the server
     * @param result the source to extract the events from
     * @return an array of events
     */
    public static Event[] getNewEvents(JSONObject result) {
        try {
            JSONArray jsons = result.getJSONArray(JSONParameter.NEW_EVENTS.toString());
            return getEvents(jsons);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Extracts multiple events from a json object coming from the server
     * @param result the source to extract the events from
     * @return an array of events
     */
    public static Event[] getAcceptedEvents(JSONObject result) {
        try {
            JSONArray jsons = result.getJSONArray(JSONParameter.ACC_EVENTS.toString());
            return getEvents(jsons);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
