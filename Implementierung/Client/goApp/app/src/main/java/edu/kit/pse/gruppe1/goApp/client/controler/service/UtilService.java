package edu.kit.pse.gruppe1.goApp.client.controler.service;

import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.JSONParameter;
import edu.kit.pse.gruppe1.goApp.client.model.Event;
import edu.kit.pse.gruppe1.goApp.client.model.Group;
import edu.kit.pse.gruppe1.goApp.client.model.Location;
import edu.kit.pse.gruppe1.goApp.client.model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;


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
    public static final String ACCEPTED_EVENTS = "acceptedEvents" ;
    public static final String EVENT = "event";
//TODO Events laden

    public static Group[] getGroups(JSONObject json){
        //TODO if(json == null)
        try {
            //TODO Error Code nicht beschreibung senden
            if (json.getJSONArray(JSONParameter.ErrorCode.toString()).getString(0).equals(JSONParameter.ErrorCodes.OK.toString())) {
                JSONArray name = json.getJSONArray(JSONParameter.GroupName.toString());
                JSONArray id = json.getJSONArray(JSONParameter.GroupID.toString());
                Group[] groups = new Group[name.length()];
                for (int i = 0; i < name.length(); i++) {
                    User user = new User(json.getInt(JSONParameter.UserID.toString()), json.getString(JSONParameter.UserName.toString()));
                    groups[i] = new Group(
                            (int) id.get(i),
                            (String) name.get(i),
                            user);
                }
                return groups;
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public static User[] getUsers(JSONObject json){
        //TODO if(json == null)
        try {
            //TODO Error Code nicht beschreibung senden
            if (json.getJSONArray(JSONParameter.ErrorCode.toString()).getString(0).equals(JSONParameter.ErrorCodes.OK.toString())) {
                JSONArray name = json.getJSONArray(JSONParameter.UserName.toString());
                JSONArray id = json.getJSONArray(JSONParameter.UserID.toString());
                User[] users = new User[name.length()];
                for (int i = 0; i < name.length(); i++) {
                    User user = new User((int) id.get(i), (String) name.get(i));
                }
                return users;
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String getError(JSONObject json){
        //TODO Erro massages
        return "ok";
    }

    private static Event[] getEvents(JSONObject result) {
        /*try {
            JSONArray jsons = result.getJSONArray(JSONParameter.GroupName.toString());
            Event[] events = new Event[jsons.length()];
            for (int i = 0; i < jsons.length(); i++) {
                events[i] = new Event(
                        jsons.getJSONObject(i).getInt(JSONParameter.EventID.toString()),
                        jsons.getJSONObject(i).getString(JSONParameter.EventName.toString()),
                        new Date(jsons.getJSONObject(i).getLong(JSONParameter.EventTime.toString())),
                        new Location(result.getDouble(JSONParameter.Latitude.toString()), result.getDouble(JSONParameter.Longitude.toString()), result.getString(JSONParameter.LocationName.toString())),
new User(jsons.getInt(JSONParameter.UserID.toString()),jsons.getString(JSONParameter.UserName.toString())  )              );;
            }
            return events;
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        return null;

    }

    public static Event[] getNewEvents(JSONObject result) {
        try {
            //TODO JsonParameter
            Event[] newEvents = getEvents(result.getJSONObject("newEvents"));
            return newEvents;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Event[] getAcceptedEvents(JSONObject result) {
        try {
            //TODO JsonParameter
            Event[] acceptedEvents = getEvents(result.getJSONObject("acceptedEvents"));
            return acceptedEvents;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Event getEvent(JSONObject result){
        Event event = null;
        /*try {
            event = new Event(
                    result.getInt(JSONParameter.EventID.toString()),
                    result.getString(JSONParameter.EventName.toString()),
                    new Date(result.getLong(JSONParameter.EventTime.toString())),
                    new Location(result.getDouble(JSONParameter.Latitude.toString()), result.getDouble(JSONParameter.Longitude.toString()), result.getString(JSONParameter.LocationName.toString())));
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        return event;
    }
}
