package edu.kit.pse.gruppe1.goApp.client.controler.service;

import java.sql.Date;
import java.util.List;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.HTTPConnection;
import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.JSONParameter;
import edu.kit.pse.gruppe1.goApp.client.model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This Service provides methods to handle a single Group.
 */
public class GroupService extends IntentService {

    public static final String NAME = "GroupService";
    public static final String ACTION_CREATE = "CREATE";
    public static final String ACTION_DELETE = "DELETE";
    public static final String ACTION_GET = "GET";
    public static final String ACTION_DELETE_MEMBER = "DELETE_MEMBER";
    public static final String ACTION_SET_NAME = "SET_NAME";
    public static final String ACTION_SET_FOUNDER = "SET_FOUNDER";
    public static final String ACTION_GET_EVENTS = "GET_EVENTS";
    public static final String SERVLET = "GroupServlet";

    public GroupService() {
        super(NAME);
    }


    /**
     * creates a new Group
     *
     * @param name    The name of the new group
     * @param founder The user who creates the group
     * @return true, if method was successful, otherwise false
     */
    public void create(Context context, String name, User founder) {
        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.GroupName.toString(), name);
            requestJson.put(JSONParameter.UserID.toString(), founder.getId());
            requestJson.put(JSONParameter.Method.toString(), ACTION_CREATE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra("Json", requestJson.toString());
        requestIntent.setAction(ACTION_CREATE);

        startService(requestIntent);
    }

    /**
     * deletes a group
     *
     * @param group The group to be deleted
     *              �
     * @return true, if methode was successful, otherwise false
     */
    public void delete(Context context, Group group) {
        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.GroupID.toString(), group.getId());
            requestJson.put(JSONParameter.Method.toString(), ACTION_DELETE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra("Json", requestJson.toString());
        requestIntent.setAction(ACTION_DELETE);

        startService(requestIntent);
    }

    /**
     * removes a member from the group
     *
     * @param group The group in which the user currently is but will be deleted from
     * @param user  The user who will be removed from the group
     * @return true, if method was successful, otherwise false
     */
    public void deleteMember(Context context, Group group, User user) {
        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.GroupID.toString(), group.getId());
            requestJson.put(JSONParameter.UserID.toString(), user.getId());
            requestJson.put(JSONParameter.Method.toString(), ACTION_DELETE_MEMBER);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra("Json", requestJson.toString());
        requestIntent.setAction(ACTION_DELETE_MEMBER);

        startService(requestIntent);
    }

    /**
     * changes the name of the group
     *
     * @param group   The group which's founder changed the name
     * @param newName The new name of the group
     * @return true, if method was successful, otherwise false
     */
    public void setName(Context context, Group group, String newName) {
        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.GroupID.toString(), group.getId());
            requestJson.put(JSONParameter.GroupName.toString(), newName);
            requestJson.put(JSONParameter.Method.toString(), ACTION_SET_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra("Json", requestJson.toString());
        requestIntent.setAction(ACTION_SET_NAME);

        startService(requestIntent);
    }

    /**
     * gets the group from the server database
     *
     * @param groupID The unique id of the group to find it
     * @return the group with the given id or null if it doesn't exist
     */
    public void getGroup(Context context, int groupID) {
        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.GroupID.toString(), groupID);
            requestJson.put(JSONParameter.Method.toString(), ACTION_GET);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra("Json", requestJson.toString());
        requestIntent.setAction(ACTION_GET);

        startService(requestIntent);
    }

    /**
     * changes the founder of the group
     *
     * @param group      The group which founder changes
     * @param newFounder The user who gets the rights of the founder
     * @return true, if method was successful, otherwise false
     */
    public void setFounder(Context context, Group group, User newFounder) {
        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.UserID.toString(), newFounder.getId());
            requestJson.put(JSONParameter.Method.toString(), ACTION_SET_FOUNDER);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra("Json", requestJson.toString());
        requestIntent.setAction(ACTION_SET_FOUNDER);

        startService(requestIntent);
    }

    /**
     * Returns all events which are associated with the group or null if no events exist in this group
     *
     * @param group The existing group to get events from
     * @return all event in the group or null
     */
    public void getEvents(Context context, Group group) {
        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.GroupID.toString(), group.getId());
            requestJson.put(JSONParameter.Method.toString(), ACTION_GET_EVENTS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra("Json", requestJson.toString());
        requestIntent.setAction(ACTION_GET_EVENTS);

        startService(requestIntent);
    }

    //TODO change model classes of client after successful change
    @Override
    protected void onHandleIntent(Intent intent) {
        HTTPConnection connection = new HTTPConnection(SERVLET);
        Intent resultIntent = new Intent();
        JSONObject result;
        switch (intent.getAction()) {
            case ACTION_CREATE:
                result = connection.sendPostRequest(intent.getStringExtra("JSON"));
                resultIntent.setAction(intent.getAction());
                try {
                    //TODO what happens if error != 0
                    resultIntent.putExtra("ERROR", result.getInt(JSONParameter.ErrorCode.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case ACTION_GET:
                result = connection.sendGetRequest(intent.getStringExtra("JSON"));
                try {
                    User user = new User(result.getInt(JSONParameter.UserID.toString()), result.getString(JSONParameter.UserName.toString()));
                    Group group = new Group(result.getInt(JSONParameter.GroupID.toString()), result.getString(JSONParameter.GroupName.toString()), user);
                    resultIntent.putExtra("group", group);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case ACTION_DELETE:
                result = connection.sendPostRequest(intent.getStringExtra("JSON"));
                try {
                    //TODO what happens if error != 0
                    resultIntent.putExtra("ERROR", result.getInt(JSONParameter.ErrorCode.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case ACTION_DELETE_MEMBER:
                result = connection.sendPostRequest(intent.getStringExtra("JSON"));
                try {
                    //TODO what happens if error != 0
                    resultIntent.putExtra("ERROR", result.getInt(JSONParameter.ErrorCode.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case ACTION_GET_EVENTS:
                result = connection.sendGetRequest(intent.getStringExtra("JSON"));
                resultIntent.putExtra("events", getEvents(result));
                break;
            case ACTION_SET_FOUNDER:
                result = connection.sendPostRequest(intent.getStringExtra("JSON"));
                try {
                    //TODO what happens if error != 0
                    resultIntent.putExtra("ERROR", result.getInt(JSONParameter.ErrorCode.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case ACTION_SET_NAME:
                result = connection.sendPostRequest(intent.getStringExtra("JSON"));
                try {
                    //TODO what happens if error != 0
                    resultIntent.putExtra("ERROR", result.getInt(JSONParameter.ErrorCode.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
                //TODO default case

        }
        resultIntent.setAction(intent.getAction());

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this.getApplicationContext());
        manager.sendBroadcast(resultIntent);
    }

    private Event[] getEvents(JSONObject result) {
        try {
            JSONArray jsons = result.getJSONArray(JSONParameter.GroupName.toString());
            Event[] events = new Event[jsons.length()];
            for (int i = 0; i < jsons.length(); i++) {
                events[i] = new Event(
                        jsons.getJSONObject(i).getInt(JSONParameter.EventID.toString()),
                        jsons.getJSONObject(i).getString(JSONParameter.EventName.toString()),
                        new Date(jsons.getJSONObject(i).getLong(JSONParameter.EventTime.toString())));
            }
            return events;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }
}