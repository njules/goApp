package edu.kit.pse.gruppe1.goApp.client.controler.service;

import android.util.Log;
import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.JSONParameter;
import edu.kit.pse.gruppe1.goApp.client.model.Event;
import edu.kit.pse.gruppe1.goApp.client.model.Group;
import edu.kit.pse.gruppe1.goApp.client.model.Location;
import edu.kit.pse.gruppe1.goApp.client.model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.sql.Timestamp;


/**
 * Created by Katharina Riesterer on 07.02.2017.
 */

public final class UtilService {

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
    private static final String USER_LIMIT = "Group is full";
    private static final String GROUP_LIMIT = "You can only be in 20 groups";
    private static final String SERVER_FAILED = "Our server has some problems";
    private static final String CONNECTION_FAILED = "Please check your internet connection";
    private static final String OK = "ok";

    public static Group[] getGroups(JSONObject json) {
        //TODO if(json == null)
        try {
            JSONArray jsons = json.getJSONArray(JSONParameter.LIST_GROUP.toString());
            Group[] groups = new Group[jsons.length()];
            for (int i = 0; i < jsons.length() ; i++) {
                JSONObject group = jsons.getJSONObject(i);
                User founder = new User(group.getInt(JSONParameter.USER_ID.toString()),
                        group.getString(JSONParameter.USER_NAME.toString()));
                groups[i] = new Group(group.getInt(JSONParameter.GRUOP_ID.toString()),
                        group.getString(JSONParameter.GROUP_NAME.toString()),
                        founder);
            }
                /*JSONArray name = json.getJSONArray(JSONParameter.GROUP_NAME.toString());
                JSONArray id = json.getJSONArray(JSONParameter.GRUOP_ID.toString());
                Group[] groups = new Group[name.length()];
                for (int i = 0; i < name.length(); i++) {
                    User user = new User(json.getInt(JSONParameter.USER_ID.toString()), json.getString(JSONParameter.USER_NAME.toString()));
                    groups[i] = new Group(
                            (int) id.get(i),
                            (String) name.get(i),
                            user);
            }*/
            return groups;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User[] getUsers(JSONObject json) {
        //TODO if(json == null)
        try {
                JSONArray name = json.getJSONArray(JSONParameter.USER_NAME.toString());
                JSONArray id = json.getJSONArray(JSONParameter.USER_ID.toString());
                User[] users = new User[name.length()];
                for (int i = 0; i < name.length(); i++) {
                    User user = new User((int) id.get(i), (String) name.get(i));
                return users;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Location[] getLocations(JSONObject json) {
        JSONArray latitude = null;
        try {
            latitude = json.getJSONArray(JSONParameter.LATITUDE.toString());

            JSONArray longitude = json.getJSONArray(JSONParameter.LONGITUDE.toString());
            JSONArray name = json.getJSONArray(JSONParameter.LOC_NAME.toString());
            Location[] locations = new Location[latitude.length()];
            for (int i = 0; i < name.length(); i++) {
                locations[i] = new Location(
                        (double) longitude.get(i),
                        (double) latitude.get(i),
                        (String) name.get(i));
            }
            return locations;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isError(JSONObject json) {
        try {
            if (json.getString(JSONParameter.ERROR_CODE.toString()).equals(JSONParameter.ErrorCodes.OK.toString())) {
                return false;
            } else {
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return true;
        }
    }

    public static String getError(JSONObject json) {
        //TODO Erro massages
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
        } else {
            return SERVER_FAILED;
        }
    }

    private static Event[] getEvents(JSONObject result) {
        try {
            JSONArray jsons = result.getJSONArray(JSONParameter.GROUP_NAME.toString());
            Event[] events = new Event[jsons.length()];
            for (int i = 0; i < jsons.length(); i++) {
                Location destination = new Location(jsons.getJSONObject(i).getDouble(JSONParameter.LATITUDE.toString()), jsons.getJSONObject(i).getDouble(JSONParameter.LONGITUDE.toString()), result.getString(JSONParameter.LOC_NAME.toString()));
                User founder = new User(jsons.getJSONObject(i).getInt(JSONParameter.USER_ID.toString()), jsons.getJSONObject(i).getString(JSONParameter.USER_NAME.toString()));
                events[i] = new Event(
                        jsons.getJSONObject(i).getInt(JSONParameter.EVENT_ID.toString()),
                        jsons.getJSONObject(i).getString(JSONParameter.EVENT_NAME.toString()),
                        new Timestamp(jsons.getJSONObject(i).getLong(JSONParameter.EVENT_TIME.toString())),
                        destination, founder);
            }
            return events;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static Event[] getNewEvents(JSONObject result) {
        try {
            Event[] newEvents = getEvents(result.getJSONObject(JSONParameter.NEW_EVENTS.toString()));
            return newEvents;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Event[] getAcceptedEvents(JSONObject result) {
        try {
            Event[] acceptedEvents = getEvents(result.getJSONObject(JSONParameter.ACC_EVENTS.toString()));
            return acceptedEvents;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Event getEvent(JSONObject result) {
        Event event = null;
        try {
            Location destination = new Location(result.getDouble(JSONParameter.LATITUDE.toString()), result.getDouble(JSONParameter.LONGITUDE.toString()), result.getString(JSONParameter.LOC_NAME.toString()));
            User founder = new User(result.getInt(JSONParameter.USER_ID.toString()), result.getString(JSONParameter.USER_NAME.toString()));
            event = new Event(
                    result.getInt(JSONParameter.EVENT_ID.toString()),
                    result.getString(JSONParameter.EVENT_NAME.toString()),
                    new Timestamp(result.getLong(JSONParameter.EVENT_TIME.toString())),
                    destination, founder);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return event;
    }
}
