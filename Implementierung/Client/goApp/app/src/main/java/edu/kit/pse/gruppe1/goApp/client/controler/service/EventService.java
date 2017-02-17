package edu.kit.pse.gruppe1.goApp.client.controler.service;

import java.sql.Timestamp;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.HTTPConnection;
import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.JSONParameter;
import edu.kit.pse.gruppe1.goApp.client.model.*;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This Service provides methods to handle a single Event
 */
public class EventService extends IntentService {

    private static final String NAME = "EventService";
    private static final String SERVLET = "EventServlet";
    //Intent actions to start the service
    private static final String ACTION_CREATE = "CREATE";
    private static final String ACTION_GET = "GET";
    private static final String ACTION_CHANGE = "CHANGE";
    /**
     * Action of the broadcasts intent with the results of change(Context context, Event event)
     */
    public static final String RESULT_CHANGE = "RESULT_CHANGE";
    /**
     * Action of the broadcasts intent with the results of create(Context context, String name, Location destination, User eventAdmin, Timestamp time, Group group)
     */
    public static final String RESULT_CREATE = "RESULT_CREATE";
    /**
     * Action of the broadcasts intent with the results of getEvent(Context context, int eventID)
     */
    public static final String RESULT_GET = "RESULT_GET";

    public EventService() {
        super(NAME);
    }

    /**
     * creates an event and broadcasts the new event locally or an errorcode as defined in Jsonparameter.ErrorCodes
     *
     * @param name        name of the group chosen freely by a member of the group
     * @param destination the location which the user choose on a map to let his event take place there (coordinates) and gave it a name (string)
     * @param eventAdmin  the user who creates the event and is a group member
     * @param time        the time when the event is going to take place. This time can not be in the past
     * @param group       the group in which the event is created and which members are all invited
     */
    public void create(Context context, String name, Location destination, User eventAdmin, Timestamp time, Group group) {
        JSONObject requestJson = new JSONObject();
        try {
            requestJson.put(JSONParameter.EVENT_NAME.toString(), name);
            requestJson.put(JSONParameter.GROUP_ID.toString(), group.getId());
            requestJson.put(JSONParameter.USER_ID.toString(), eventAdmin.getId());
            requestJson.put(JSONParameter.LATITUDE.toString(), destination.getLatitude());
            requestJson.put(JSONParameter.LONGITUDE.toString(), destination.getLongitude());
            requestJson.put(JSONParameter.LOC_NAME.toString(), destination.getName());
            requestJson.put(JSONParameter.EVENT_TIME.toString(), time.getTime());
            requestJson.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.CREATE.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra(UtilService.JSON, requestJson.toString());
        requestIntent.putExtra(UtilService.EVENT, new Event(0, name, time, destination, eventAdmin)); //to easy create the new Event if the server answers without error
        requestIntent.setAction(ACTION_CREATE);

        context.startService(requestIntent);    //starts the IntentService to communicate with the server on a new thread
    }

    /**
     * gets the events participantes from the server database and broadcasts lists of users acording to their status (started or participate)
     * or an errorcode as defined in Jsonparameter.ErrorCodes
     *
     * @param eventID the id to identify which events participants to get
     */
    public void getEvent(Context context, int eventID) {
        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.EVENT_ID.toString(), eventID);
            requestJson.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.GET_EVENT.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra(UtilService.JSON, requestJson.toString());
        requestIntent.setAction(ACTION_GET);

        context.startService(requestIntent); //starts the IntentService to communicate with the server on a new thread
    }

    /**
     * Changes attributes of an event. Only time, destination and name can be changed. Broadcasts if Error occurred
     *
     * @param event the new instance of the event with the wanted attributes changed which will replace the old event
     */
    public void change(Context context, Event event) {
        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.EVENT_ID.toString(), event.getId());
            requestJson.put(JSONParameter.EVENT_NAME.toString(), event.getName());
            requestJson.put(JSONParameter.EVENT_TIME.toString(), event.getTime().getTime());
            requestJson.put(JSONParameter.LATITUDE.toString(), event.getLocation().getLatitude());
            requestJson.put(JSONParameter.LONGITUDE.toString(), event.getLocation().getLongitude());
            requestJson.put(JSONParameter.LOC_NAME.toString(), event.getLocation().getName());
            requestJson.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.CHANGE.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra(UtilService.JSON, requestJson.toString());
        requestIntent.setAction(ACTION_CHANGE);

        context.startService(requestIntent); //starts the IntentService to communicate with the server on a new thread
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
                    resultIntent.putExtra(UtilService.ERROR, UtilService.getError(result)); //hands errors of the server to the activity in a userfriendly language
                } else {
                    try {
                        int id = result.getInt(JSONParameter.EVENT_ID.toString());
                        Event event = intent.getParcelableExtra(UtilService.EVENT);
                        Log.i("location", "" + id);
                        Event newEvent = new Event(id, event.getName(), event.getTime(), event.getLocation(), event.getCreator());
                        resultIntent.putExtra(UtilService.EVENT, newEvent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case ACTION_GET:
                result = connection.sendGetRequest(intent.getStringExtra(UtilService.JSON));
                resultIntent.setAction(RESULT_GET);
                Log.i("Participants", result.toString());
                if (UtilService.isError(result)) {
                    resultIntent.putExtra(UtilService.ERROR, UtilService.getError(result)); //hands errors of the server to the activity in a userfriendly language
                } else {
                    resultIntent.putExtra(UtilService.USERS, UtilService.getParticipants(result, JSONParameter.LIST_PART.toString()));
                    resultIntent.putExtra(UtilService.STARTED_USERS, UtilService.getParticipants(result, JSONParameter.LIST_START_PART.toString()));
                }
                break;
            case ACTION_CHANGE:
                result = connection.sendPostRequest(intent.getStringExtra(UtilService.JSON));
                resultIntent.setAction(RESULT_CHANGE);
                if (UtilService.isError(result)) {
                    resultIntent.putExtra(UtilService.ERROR, UtilService.getError(result)); //hands errors of the server to the activity in a userfriendly language
                }
                break;
            default:
                break;
        }

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this.getApplicationContext());
        manager.sendBroadcast(resultIntent);
    }
}