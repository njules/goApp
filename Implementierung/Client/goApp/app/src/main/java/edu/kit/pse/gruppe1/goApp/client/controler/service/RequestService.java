package edu.kit.pse.gruppe1.goApp.client.controler.service;

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

import java.sql.Date;

/**
 * This Service provides methods to handle a Request.
 */
public class RequestService extends IntentService{

	private static final String NAME = "RequestService";
    private static final String SERVLET = "RequestServlet";
    //Intnt actions
	private static final String ACTION_CREATE = "CREATE";
	private static final String ACTION_ACCEPT = "ACCEPT";
	private static final String ACTION_REJECT = "REJECT";
	public static final String RESULT_REJECT = "RESULT_REJECT_REQUEST";
	public static final String RESULT_ACCEPT = "RESULT_ACCEPT_REQUEST";
	public static final String RESULT_CREATE = "RESULT_CREATE_REQUEST";


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
            requestJson.put(JSONParameter.GROUP_ID.toString(), group.getId());
            requestJson.put(JSONParameter.USER_ID.toString(), user.getId());
            requestJson.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.CREATE.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra(UtilService.JSON, requestJson.toString());
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
            requestJson.put(JSONParameter.GROUP_ID.toString(), request.getGroup().getId());
            requestJson.put(JSONParameter.USER_ID.toString(), request.getUser().getId());
            requestJson.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.ACCEPT);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra(UtilService.JSON, requestJson.toString());
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
            requestJson.put(JSONParameter.GROUP_ID.toString(), request.getGroup().getId());
            requestJson.put(JSONParameter.USER_ID.toString(), request.getUser().getId());
            requestJson.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.REJECT);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra(UtilService.JSON, requestJson.toString());
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
                result = connection.sendPostRequest(intent.getStringExtra(UtilService.JSON));
                resultIntent.setAction(RESULT_CREATE);
                if(UtilService.isError(result)){
                    resultIntent.putExtra(UtilService.ERROR,UtilService.getError(result));
                }
                break;
            case ACTION_REJECT:
                result = connection.sendPostRequest(intent.getStringExtra(UtilService.JSON));
                resultIntent.setAction(RESULT_REJECT);
                if(UtilService.isError(result)){
                    resultIntent.putExtra(UtilService.ERROR,UtilService.getError(result));
                }
                break;
            case ACTION_ACCEPT:
                result = connection.sendPostRequest(intent.getStringExtra(UtilService.JSON));
                resultIntent.setAction(RESULT_ACCEPT);
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