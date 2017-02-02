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

import java.sql.Date;

/**
 * This Service provides methods to handle a Request.
 */
public class RequestService extends IntentService{

	public static final String NAME = "RequestService";
	public static final String ACTION_CREATE = "CREATE";
	public static final String ACTION_ACCEPT = "ACCEPT";
	public static final String ACTION_REJECT = "REJECT";
	public static final String RESULT_REJECT = "RESULT_REJECT";
	public static final String RESULT_ACCEPT = "RESULT_ACCEPT";
	public static final String RESULT_CREATE = "RESULT_CREATE";
	public static final String SERVLET = "RequestServlet";

    public RequestService() {
        super(NAME);
    }

    /**
	 * creates a new Request
	 * @param user the user who sends the request
	 * @param group the group the user wants to be a member of
	 * @return true, if method was successful, otherwise false
	 */
	public void create(Context context, User user, Group group) {
        JSONObject requestJson = new JSONObject();

        try {

            requestJson.put(JSONParameter.GroupID.toString(), group.getId());
            requestJson.put(JSONParameter.UserID.toString(), user.getId());
            requestJson.put(JSONParameter.Method.toString(), ACTION_CREATE);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra("Json", requestJson.toString());
        requestIntent.setAction(ACTION_CREATE);

        context.startService(requestIntent);
	}
//TODO differenz between accept and reject
	/**
	 * adds the user to the group and deletes the Request if the founder of the group wants the user in the group
	 * @param request the request the founder has made a decision about
	 * @return true, if method was successful, otherwise false
	 */
	public void accept(Context context, Request request) {
        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.GroupID.toString(), request.getGroup().getId());
            requestJson.put(JSONParameter.UserID.toString(), request.getUser().getId());
            requestJson.put(JSONParameter.Method.toString(), ACTION_ACCEPT);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra("Json", requestJson.toString());
        requestIntent.setAction(ACTION_ACCEPT);

        context.startService(requestIntent);
	}

	/**
	 * deletes the request if the founder decided that the user will not be in the group
	 * @param request the request the founder has made a decision about
	 * @return true, if method was successful, otherwise false
	 */
	public void reject(Context context, Request request) {
        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.GroupID.toString(), request.getGroup().getId());
            requestJson.put(JSONParameter.UserID.toString(), request.getUser().getId());
            requestJson.put(JSONParameter.Method.toString(), ACTION_REJECT);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra("Json", requestJson.toString());
        requestIntent.setAction(ACTION_REJECT);

        context.startService(requestIntent);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
        HTTPConnection connection = new HTTPConnection(SERVLET);
        Intent resultIntent = new Intent();
        JSONObject result;
        switch (intent.getAction()) {
            case ACTION_CREATE:
                result = connection.sendPostRequest(intent.getStringExtra("JSON"));
                try {
                    //TODO what happens if error != 0
                    resultIntent.putExtra("ERROR", result.getInt(JSONParameter.ErrorCode.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                resultIntent.setAction(RESULT_CREATE);
                break;
            case ACTION_REJECT:
                result = connection.sendPostRequest(intent.getStringExtra("JSON"));
                try {
                    //TODO what happens if error != 0
                    resultIntent.putExtra("ERROR", result.getInt(JSONParameter.ErrorCode.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                resultIntent.setAction(RESULT_REJECT);
                break;
            case ACTION_ACCEPT:
                result = connection.sendPostRequest(intent.getStringExtra("JSON"));
                try {
                    //TODO what happens if error != 0
                    resultIntent.putExtra("ERROR", result.getInt(JSONParameter.ErrorCode.toString()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                resultIntent.setAction(RESULT_ACCEPT);
                break;
            //TODO default case
        }
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this.getApplicationContext());
        manager.sendBroadcast(resultIntent);
	}
}