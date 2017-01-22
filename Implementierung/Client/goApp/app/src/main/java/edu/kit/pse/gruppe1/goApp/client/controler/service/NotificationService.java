package edu.kit.pse.gruppe1.goApp.client.controler.service;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.HTTPConnection;
import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.JSONParameter;
import edu.kit.pse.gruppe1.goApp.client.model.Event;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;

/**
 * This Service is used to notify an User that an event has started even when the Application is closed via Notificationbar
 */
public class NotificationService extends IntentService{

	private static final String NAME = "NotificationService";
	private static final String ACTION_GET = "GET";
	private static final String SERVLET = "EventServlet";
	private static final int ERROR_ID = -1;
	public NotificationService() {
		super(NAME);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		int eventId = intent.getIntExtra("EventId",ERROR_ID);
		if(eventId == -1){
			//TODO Errorhandling
		}else{

		}
		//TODO use EventService??
        JSONObject requestJson = new JSONObject();
        try {
            requestJson.put(JSONParameter.EventID.toString(), eventId);
            requestJson.put(JSONParameter.Method.toString(), ACTION_GET);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HTTPConnection connection = new HTTPConnection(SERVLET);
        JSONObject result = connection.sendGetRequest(requestJson.toString());
        Event event = null;
        try {
            event = new Event(
                    result.getInt(JSONParameter.EventID.toString()),
                    result.getString(JSONParameter.EventName.toString()),
                    new Date(result.getLong(JSONParameter.EventTime.toString())));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        notifyUser(event);
	}

	/**
	 * send a notification to the user that the event is going to start
	 */
	private void notifyUser(Event event) {
		// TODO - implement NotificationService.notifyUser
		throw new UnsupportedOperationException();
	}

}