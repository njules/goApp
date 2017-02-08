package edu.kit.pse.gruppe1.goApp.client.controler.service;

import java.sql.Date;

import android.app.AlarmManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
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
    //Intent actions to broadcast results
    public static final String RESULT_CHANGE = "RESULT_CHANGE";
    public static final String RESULT_CREATE = "RESULT_CREATE";
    public static final String RESULT_GET = "RESULT_GET";

    public EventService() {
        super(NAME);
    }

    /**
     * creates an event
     *
     * @param name        name of the group chosen freely by a member of the group
     * @param destination the location which the user choose on a map to let his event take place there (coordinates) and gave it a name (string)
     * @param eventAdmin  the user who creates the event and is a group member
     * @param time        the time when the event is going to take place. This time can not be in the past
     * @param group       the group in which the event is created and which members are all invited
     * @return true, if method was successful, otherwise false
     */
    public void create(Context context, String name, Location destination, User eventAdmin, Date time, Group group) {
        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.EVENT_NAME.toString(), name);
            requestJson.put(JSONParameter.GRUOP_ID.toString(), group.getId());
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
        requestIntent.putExtra(UtilService.EVENT, new Event(0, name, time, destination, eventAdmin));
        requestIntent.setAction(ACTION_CREATE);

        context.startService(requestIntent);
    }

    /**
     * gets the event from the server database
     *
     * @param eventID the id to identify which event to get
     * @return an event object with attributes
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

        startService(requestIntent);
    }

    /**
     * if the event admin whishes to change the event. Only time, destination and name can be changed
     *
     * @param event the new instance of the event with the wanted attributes changed which will replace the old event
     * @return true, if method was successful, otherwise false
     */
    public void change(Context context, Event event) {
        JSONObject requestJson = new JSONObject();

        try {
            //TODO add Eventattributes which should be able to be changed
            requestJson.put(JSONParameter.EVENT_ID.toString(), event.getId());
            requestJson.put(JSONParameter.EVENT_NAME.toString(), event.getName());
            requestJson.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.CHANGE.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra(UtilService.JSON, requestJson.toString());
        requestIntent.setAction(ACTION_CHANGE);

        startService(requestIntent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        HTTPConnection connection = new HTTPConnection(SERVLET);
        Intent resultIntent = new Intent();
        JSONObject result;
        switch (intent.getAction()) {
            case ACTION_CREATE:
                //TODO start alarm for notification and locationSync
                result = connection.sendPostRequest(intent.getStringExtra(UtilService.JSON));
                resultIntent.setAction(RESULT_CREATE);
                if (UtilService.isError(result)) {
                    resultIntent.putExtra(UtilService.ERROR, UtilService.getError(result));
                } else {
                    try {
                        int id = result.getInt(JSONParameter.EVENT_ID.toString());
                        Event event = intent.getParcelableExtra(UtilService.EVENT);
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
                //TODO Status of participants
                if(UtilService.isError(result)){
                    resultIntent.putExtra(UtilService.ERROR,UtilService.getError(result));
                } else {
                    resultIntent.putExtra(UtilService.USERS, UtilService.getUsers(result));
                }
                break;
            case ACTION_CHANGE:
                result = connection.sendPostRequest(intent.getStringExtra(UtilService.JSON));
                resultIntent.setAction(RESULT_CHANGE);
                if(UtilService.isError(result)){
                    resultIntent.putExtra(UtilService.ERROR,UtilService.getError(result));
                }
                break;
            //TODO default case
        }

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this.getApplicationContext());
        manager.sendBroadcast(resultIntent);
    }
}