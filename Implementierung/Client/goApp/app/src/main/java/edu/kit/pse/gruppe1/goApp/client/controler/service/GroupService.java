package edu.kit.pse.gruppe1.goApp.client.controler.service;

import java.sql.Date;
import java.util.List;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import edu.kit.pse.gruppe1.goApp.client.R;
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
    public static final String ACTION_CREATE = "CREATE_GROUP";
    public static final String ACTION_DELETE = "DELETE_GROUP";
    public static final String ACTION_GET = "GET_GROUP";
    public static final String ACTION_DELETE_MEMBER = "DELETE_MEMBER";
    public static final String ACTION_SET_NAME = "SET_NAME";
    public static final String ACTION_SET_FOUNDER = "SET_FOUNDER";
    public static final String ACTION_GET_EVENTS = "GET_EVENTS";
    public static final String RESULT_CREATE = "RESULT_CREATE_GROUP";
    public static final String RESULT_DELETE = "RESULT_DELETE_GROUP";
    public static final String RESULT_GET = "RESULT_GET_GROUP";
    public static final String RESULT_DELETE_MEMBER = "RESULT_DELETE_MEMBER";
    public static final String RESULT_SET_NAME = "RESULT_SET_NAME";
    public static final String RESULT_SET_FOUNDER = "RESULT_SET_FOUNDER";
    public static final String RESULT_GET_EVENTS = "RESULT_GET_EVENTS";
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
            requestJson.put(JSONParameter.Method.toString(), JSONParameter.Methods.CREATE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra(getString(R.string.JSON), requestJson.toString());
        requestIntent.setAction(ACTION_CREATE);

        context.startService(requestIntent);
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
            requestJson.put(JSONParameter.Method.toString(), JSONParameter.Methods.DELETE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra("Json", requestJson.toString());
        requestIntent.setAction(ACTION_DELETE);

        context.startService(requestIntent);
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
            requestJson.put(JSONParameter.Method.toString(), JSONParameter.Methods.DEL_MEM);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra("Json", requestJson.toString());
        requestIntent.setAction(ACTION_DELETE_MEMBER);

        context.startService(requestIntent);
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
            requestJson.put(JSONParameter.Method.toString(), JSONParameter.Methods.SET_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra("Json", requestJson.toString());
        requestIntent.setAction(ACTION_SET_NAME);

        context.startService(requestIntent);
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
            requestJson.put(JSONParameter.Method.toString(), JSONParameter.Methods.GET_GROUP);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra("Json", requestJson.toString());
        requestIntent.setAction(ACTION_GET);

        context.startService(requestIntent);
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
            requestJson.put(JSONParameter.Method.toString(), JSONParameter.Methods.SET_FOUNDER);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra("Json", requestJson.toString());
        requestIntent.setAction(ACTION_SET_FOUNDER);

        context.startService(requestIntent);
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
            //TODO JSON Parameter
            requestJson.put(JSONParameter.Method.toString(), ACTION_GET_EVENTS);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra("Json", requestJson.toString());
        requestIntent.setAction(ACTION_GET_EVENTS);

        context.startService(requestIntent);
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
                resultIntent.setAction(RESULT_CREATE);
                try {
                    //TODO what happens if error != 0
                    resultIntent.putExtra("ERROR", result.getInt(JSONParameter.ErrorCode.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            // TODO Methode soll die Gruppenmitglieder und Anfragen laden, nicht die Gruppe an sich.
            case ACTION_GET:
                result = connection.sendGetRequest(intent.getStringExtra("JSON"));
                resultIntent.setAction(RESULT_GET);
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
                resultIntent.setAction(RESULT_DELETE);
                try {
                    //TODO what happens if error != 0
                    resultIntent.putExtra("ERROR", result.getInt(JSONParameter.ErrorCode.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case ACTION_DELETE_MEMBER:
                result = connection.sendPostRequest(intent.getStringExtra("JSON"));
                resultIntent.setAction(RESULT_DELETE_MEMBER);
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
                resultIntent.setAction(RESULT_GET_EVENTS);
                break;
            case ACTION_SET_FOUNDER:
                result = connection.sendPostRequest(intent.getStringExtra("JSON"));
                resultIntent.setAction(RESULT_SET_FOUNDER);
                try {
                    //TODO what happens if error != 0
                    resultIntent.putExtra("ERROR", result.getInt(JSONParameter.ErrorCode.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case ACTION_SET_NAME:
                result = connection.sendPostRequest(intent.getStringExtra("JSON"));
                resultIntent.setAction(RESULT_SET_NAME);
                try {
                    //TODO what happens if error != 0
                    resultIntent.putExtra("ERROR", result.getInt(JSONParameter.ErrorCode.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
                //TODO default case

        }
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
                        new Date(jsons.getJSONObject(i).getLong(JSONParameter.EventTime.toString())),
                        new Location(result.getDouble(JSONParameter.Latitude.toString()), result.getDouble(JSONParameter.Longitude.toString()), result.getString(JSONParameter.LocationName.toString())));;
            }
            return events;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }
}