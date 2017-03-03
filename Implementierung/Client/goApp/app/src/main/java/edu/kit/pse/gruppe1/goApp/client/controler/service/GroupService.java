package edu.kit.pse.gruppe1.goApp.client.controler.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.HTTPConnection;
import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.JSONParameter;
import edu.kit.pse.gruppe1.goApp.client.model.*;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This Service provides methods to handle a single Group.
 */
public class GroupService extends IntentService {

    private static final String NAME = "GroupService";
    private static final String SERVLET = "GroupServlet";
    //Intents action to start the Service
    private static final String ACTION_CREATE = "CREATE_GROUP";
    private static final String ACTION_DELETE = "DELETE_GROUP";
    private static final String ACTION_GET_MEMBERS = "GET_MEMBERS";
    private static final String ACTION_DELETE_MEMBER = "DELETE_MEMBER";
    private static final String ACTION_SET_NAME = "SET_NAME";
    private static final String ACTION_SET_FOUNDER = "SET_FOUNDER";
    private static final String ACTION_GET_EVENTS = "GET_EVENTS";
    /**
     * Action of the broadcasts intent with the results of create(Context context, String name, User founder)
     */
    public static final String RESULT_CREATE = "RESULT_CREATE_GROUP";
    /**
     * Action of the broadcasts intent with the results of delete(Context context, Group group)
     */
    public static final String RESULT_DELETE = "RESULT_DELETE_GROUP";
    /**
     * Action of the broadcasts intent with the results of getMembers(Context context, Group group)
     */
    public static final String RESULT_GET_MEMBERS = "RESULT_GET_MEMBERS";
    /**
     * Action of the broadcasts intent with the results of deleteMember(Context context, Group group, User user)
     */
    public static final String RESULT_DELETE_MEMBER = "RESULT_DELETE_MEMBER";
    /**
     * Action of the broadcasts intent with the results of setName(Context context, Group group, String newName)
     */
    public static final String RESULT_SET_NAME = "RESULT_SET_NAME";
    /**
     * Action of the broadcasts intent with the results of setFounder(Context context, Group group, User newFounder)
     */
    private static final String RESULT_SET_FOUNDER = "RESULT_SET_FOUNDER";
    /**
     * Action of the broadcasts intent with the results of getEvents(Context context, Group group, User user)
     */
    public static final String RESULT_GET_EVENTS = "RESULT_GET_EVENTS";

    public GroupService() {
        super(NAME);
    }


    /**
     * creates a new Group and broadcast an error code as defined in Jsonparameter.ErrorCodes
     *
     * @param context the android context to start the service
     * @param name    The name of the new group
     * @param founder The user who creates the group
     */
    public void create(Context context, String name, User founder) {
        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.GROUP_NAME.toString(), name);
            requestJson.put(JSONParameter.USER_ID.toString(), founder.getId());
            requestJson.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.CREATE.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra(UtilService.JSON, requestJson.toString());
        requestIntent.setAction(ACTION_CREATE);

        context.startService(requestIntent);    //starts the IntentService to communicate with the server on a new thread
    }

    /**
     * deletes a group and broadcasts an error code as defined in Jsonparameter.ErrorCodes
     *
     * @param context the android context to start the service
     * @param group The group to be deleted
     */
    public void delete(Context context, Group group) {
        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.GROUP_ID.toString(), group.getId());
            requestJson.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.DELETE.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra(UtilService.JSON, requestJson.toString());
        requestIntent.setAction(ACTION_DELETE);

        context.startService(requestIntent);    //starts the IntentService to communicate with the server on a new thread
    }

    /**
     * removes a member from the group and broadcasts an error code as defined in Jsonparameter.ErrorCodes
     *
     * @param context the android context to start the service
     * @param group The group in which the user currently is but will be deleted from
     * @param user  The user who will be removed from the group
     */
    public void deleteMember(Context context, Group group, User user) {
        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.GROUP_ID.toString(), group.getId());
            requestJson.put(JSONParameter.USER_ID.toString(), user.getId());
            requestJson.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.DEL_MEM.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra(UtilService.JSON, requestJson.toString());
        requestIntent.setAction(ACTION_DELETE_MEMBER);

        context.startService(requestIntent);    //starts the IntentService to communicate with the server on a new thread
    }

    /**
     * changes the name of the group and broadcasts an error code as defined in Jsonparameter.ErrorCodes
     *
     * @param context the android context to start the service
     * @param group   The group which's founder changed the name
     * @param newName The new name of the group
     */
    public void setName(Context context, Group group, String newName) {
        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.GROUP_ID.toString(), group.getId());
            requestJson.put(JSONParameter.GROUP_NAME.toString(), newName);
            requestJson.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.SET_NAME.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra(UtilService.JSON, requestJson.toString());
        requestIntent.setAction(ACTION_SET_NAME);
        requestIntent.putExtra(UtilService.NAME, newName);//to change the group name on the client easy and with less internet usage
        context.startService(requestIntent);    //starts the IntentService to communicate with the server on a new thread
    }

    /**
     * gets the groups members from the server database and broadcasts them as an array or an error code as defined in Jsonparameter.ErrorCodes
     *
     * @param context the android context to start the service
     * @param group The group which members to find
     */
    public void getMembers(Context context, Group group) {
        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.GROUP_ID.toString(), group.getId());
            requestJson.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.GET_MEMBERS.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra(UtilService.JSON, requestJson.toString());
        requestIntent.setAction(ACTION_GET_MEMBERS);

        context.startService(requestIntent);     //starts the IntentService to communicate with the server on a new thread
    }

    /**
     * changes the founder of the group and broadcasts an error code as defined in Jsonparameter.ErrorCodes
     *
     * @param context the android context to start the service
     * @param group      The group which founder changes
     * @param newFounder The user who gets the rights of the founder
     */
    public void setFounder(Context context, Group group, User newFounder) {
        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.USER_ID.toString(), newFounder.getId());
            requestJson.put(JSONParameter.GROUP_ID.toString(), group.getId());
            requestJson.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.SET_FOUNDER.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra(UtilService.JSON, requestJson.toString());
        requestIntent.setAction(ACTION_SET_FOUNDER);

        context.startService(requestIntent);     //starts the IntentService to communicate with the server on a new thread
    }

    /**
     * gets all events in the group and broadcasts them acording the the current userrs status in different arrays or an error code as defined in Jsonparameter.ErrorCodes
     *
     * @param context the android context to start the service
     * @param group The existing group to get events from
     * @param user  The user which status to consider
     */
    public void getEvents(Context context, Group group, User user) {
        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.GROUP_ID.toString(), group.getId());
            requestJson.put(JSONParameter.USER_ID.toString(), user.getId());
            requestJson.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.GET_EVENT);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra(UtilService.JSON, requestJson.toString());
        requestIntent.setAction(ACTION_GET_EVENTS);

        context.startService(requestIntent);     //starts the IntentService to communicate with the server on a new thread
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        HTTPConnection connection = new HTTPConnection(SERVLET);
        Intent resultIntent = new Intent();
        JSONObject result;
        switch (intent.getAction()) {
            case ACTION_CREATE:
                result = connection.sendPostRequest(intent.getStringExtra(UtilService.JSON));
                resultIntent.setAction(RESULT_CREATE);
                if (UtilService.isError(result)) {
                    resultIntent.putExtra(UtilService.ERROR, UtilService.getError(result));
                }
                break;
            case ACTION_GET_MEMBERS:
                result = connection.sendGetRequest(intent.getStringExtra(UtilService.JSON));
                resultIntent.setAction(RESULT_GET_MEMBERS);
                if (UtilService.isError(result)) {
                    resultIntent.putExtra(UtilService.ERROR, UtilService.getError(result));
                } else {
                    resultIntent.putExtra(UtilService.USERS, UtilService.getUsers(result));
                }
                break;
            case ACTION_DELETE:
                result = connection.sendPostRequest(intent.getStringExtra(UtilService.JSON));
                resultIntent.setAction(RESULT_DELETE);
                if (UtilService.isError(result)) {
                    resultIntent.putExtra(UtilService.ERROR, UtilService.getError(result));
                }
                break;
            case ACTION_DELETE_MEMBER:
                result = connection.sendPostRequest(intent.getStringExtra(UtilService.JSON));
                resultIntent.setAction(RESULT_DELETE_MEMBER);
                if (UtilService.isError(result)) {
                    resultIntent.putExtra(UtilService.ERROR, UtilService.getError(result));
                }
                break;
            case ACTION_GET_EVENTS:
                result = connection.sendGetRequest(intent.getStringExtra(UtilService.JSON));
                resultIntent.setAction(RESULT_GET_EVENTS);
                if (UtilService.isError(result)) {
                    resultIntent.putExtra(UtilService.ERROR, UtilService.getError(result));
                } else {
                    resultIntent.putExtra(UtilService.NEW_EVENTS, UtilService.getNewEvents(result));
                    resultIntent.putExtra(UtilService.ACCEPTED_EVENTS, UtilService.getAcceptedEvents(result));
                }
                break;
            case ACTION_SET_FOUNDER:
                result = connection.sendPostRequest(intent.getStringExtra(UtilService.JSON));
                resultIntent.setAction(RESULT_SET_FOUNDER);
                if (UtilService.isError(result)) {
                    resultIntent.putExtra(UtilService.ERROR, UtilService.getError(result));
                }
                break;
            case ACTION_SET_NAME:
                result = connection.sendPostRequest(intent.getStringExtra(UtilService.JSON));
                resultIntent.setAction(RESULT_SET_NAME);
                if (UtilService.isError(result)) {
                    resultIntent.putExtra(UtilService.ERROR, UtilService.getError(result));
                } else {
                    resultIntent.putExtra(UtilService.NAME, intent.getStringExtra(UtilService.NAME));
                }
                break;
            default:
                break;

        }
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this.getApplicationContext());
        manager.sendBroadcast(resultIntent);
    }

}