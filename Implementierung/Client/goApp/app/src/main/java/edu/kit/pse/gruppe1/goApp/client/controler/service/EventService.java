package edu.kit.pse.gruppe1.goApp.client.controler.service;

import java.sql.Date;

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

    public static final String NAME = "EventService";
    public static final String ACTION_CREATE = "CREATE";
    public static final String RESULT_CREATE = "RESULT_CREATE";
    public static final String ACTION_GET = "GET";
    public static final String RESULT_GET = "RESULT_GET";
    public static final String ACTION_CHANGE = "CHANGE";
    public static final String RESULT_CHANGE = "RESULT_CHANGE";
    public static final String SERVLET = "EventServlet";


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
            requestJson.put(JSONParameter.EventName.toString(), name);
            requestJson.put(JSONParameter.GroupID.toString(), group.getId());
            requestJson.put(JSONParameter.Latitude.toString(), destination.getLatitude());
            requestJson.put(JSONParameter.Longitude.toString(), destination.getLongitude());
            requestJson.put(JSONParameter.EventTime.toString(), time.getTime());
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
     * gets the event from the server database
     *
     * @param eventID the id to identify which event to get
     * @return an event object with attributes
     */
    public void getEvent(Context context, int eventID) {
        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.EventID.toString(), eventID);
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
     * if the event admin whishes to change the event. Only time, destination and name can be changed
     *
     * @param event the new instance of the event with the wanted attributes changed which will replace the old event
     * @return true, if method was successful, otherwise false
     */
    public void change(Context context, Event event) {
        JSONObject requestJson = new JSONObject();

        try {
            //TODO add Eventattributes which should be able to be changed
            requestJson.put(JSONParameter.EventID.toString(), event.getId());
            requestJson.put(JSONParameter.EventName.toString(), event.getName());
            requestJson.put(JSONParameter.Method.toString(), ACTION_CHANGE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra("Json", requestJson.toString());
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
                result = connection.sendPostRequest(intent.getStringExtra("JSON"));
                resultIntent.setAction(intent.getAction());
                try {
                    //TODO what happens if error != 0
                    resultIntent.putExtra("ERROR", result.getInt(JSONParameter.ErrorCode.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                resultIntent.setAction(RESULT_CREATE);
                break;
            //TODO Methode soll Teilnehmer und "Mittelpunkte" laden.
            case ACTION_GET:
                result = connection.sendGetRequest(intent.getStringExtra("JSON"));
                //TODO Exaptions and errors
                Event event = null;
                try {
                    event = new Event(
                            result.getInt(JSONParameter.EventID.toString()),
                            result.getString(JSONParameter.EventName.toString()),
                            new Date(result.getLong(JSONParameter.EventTime.toString())),
                            new Location(result.getDouble(JSONParameter.Latitude.toString()), result.getDouble(JSONParameter.Longitude.toString()), result.getString(JSONParameter.LocationName.toString())));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                resultIntent.putExtra("Event", event);
                resultIntent.setAction(RESULT_GET);

                break;
            case ACTION_CHANGE:
                result = connection.sendPostRequest(intent.getStringExtra("JSON"));
                try {
                    //TODO what happens if error != 0
                    resultIntent.putExtra("ERROR", result.getInt(JSONParameter.ErrorCode.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                resultIntent.setAction(RESULT_CHANGE);
                break;
            //TODO default case
        }

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this.getApplicationContext());
        manager.sendBroadcast(resultIntent);
    }
}