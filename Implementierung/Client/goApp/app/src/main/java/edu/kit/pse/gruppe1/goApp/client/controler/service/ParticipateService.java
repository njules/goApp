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

import static edu.kit.pse.gruppe1.goApp.client.controler.service.RequestService.ACTION_ACCEPT;

/**
 * This Service provides methods to handle a users reaction towards an event
 */
public class ParticipateService extends IntentService{
	private static final String NAME = "ParticipateService";
	private static final String ACTION_ACCEPT = "ACCEPT";
	private static final String ACTION_REJECT = "REJECT";
	private static final String SERVLET = "ParticipateServlet";
	private ParticipateService() {
		super(NAME);
	}

	/**
	 * The user is added to the event as a participant this includes starting a Timer for the Notification Broadcast. This method is used if the user wants to participate and enters this decision in the GroupActivity
	 * @param event the event which the user want to participate in. User and event have to be in the same group
	 * @param user the user who wants to participate and is in the same group as the event
	 * @return true, if method was successful, otherwise false
	 */
	public void accept(Context context, Event event, User user) {
		JSONObject requestJson = new JSONObject();

		try {
			requestJson.put(JSONParameter.EventID.toString(), event.getId());
			requestJson.put(JSONParameter.UserID.toString(), user.getId());
			requestJson.put(JSONParameter.Method.toString(), ACTION_ACCEPT);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		Intent requestIntent = new Intent(context, this.getClass());
		requestIntent.putExtra("Json", requestJson.toString());
		requestIntent.setAction(ACTION_ACCEPT);

		startService(requestIntent);
	}

	/**
	 * the connection between the user and the event is deleted
	 * @param event the event which the user doesn't want to join
	 * @param user the user who doesn't want to participate
	 * @return true, if method was successful, otherwise false
	 */
	public void reject(Context context, Event event, User user) {
		JSONObject requestJson = new JSONObject();

		try {
			requestJson.put(JSONParameter.EventID.toString(), event.getId());
			requestJson.put(JSONParameter.UserID.toString(), user.getId());
			requestJson.put(JSONParameter.Method.toString(), ACTION_REJECT);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		Intent requestIntent = new Intent(context, this.getClass());
		requestIntent.putExtra("Json", requestJson.toString());
		requestIntent.setAction(ACTION_REJECT);

		startService(requestIntent);

	}

	@Override
	protected void onHandleIntent(Intent intent) {
		HTTPConnection connection = new HTTPConnection(SERVLET);
		Intent resultIntent = new Intent();
		JSONObject result;
		switch (intent.getAction()) {
			case ACTION_REJECT:
				result = connection.sendPostRequest(intent.getStringExtra("JSON"));
				try {
					//TODO what happens if error != 0
					resultIntent.putExtra("ERROR", result.getInt(JSONParameter.ErrorCode.toString()));
				} catch (JSONException e) {
					e.printStackTrace();
				}

				break;
			case ACTION_ACCEPT:
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
}