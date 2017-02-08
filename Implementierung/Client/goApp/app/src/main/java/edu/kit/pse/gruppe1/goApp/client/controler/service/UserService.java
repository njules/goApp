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
 * This Service provides methods to handle a single User.
 */
public class UserService extends IntentService{

	public static final String NAME = "UserService";
    public static final String SERVLET = "UserServlet";
    //Intnt actions
	public static final String ACTION_CHANGE = "CHANGE";
	public static final String RESULT_CHANGE = "RESULT_CHANGE";


	public UserService() {
		super(NAME);
	}

	/**
	 * changes the old name into the new name of a user
	 * @param user The user who changes his name
	 * @param name The new name
	 * @return true, if method was successful, otherwise false
	 */
	public void changeName(Context context, User user, String name) {
        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.UserID.toString(), user.getId());
            requestJson.put(JSONParameter.UserName.toString(), name);

            requestJson.put(JSONParameter.Method.toString(), JSONParameter.Methods.CHANGE.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra(UtilService.JSON, requestJson.toString());
        requestIntent.setAction(ACTION_CHANGE);

        context.startService(requestIntent);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
        HTTPConnection connection = new HTTPConnection(SERVLET);
        Intent resultIntent = new Intent();
        JSONObject result = connection.sendPostRequest(intent.getStringExtra(UtilService.JSON));
        resultIntent.setAction(RESULT_CHANGE);
        try {
            //TODO what happens if error != 0
            resultIntent.putExtra(UtilService.ERROR, result.getInt(JSONParameter.ErrorCode.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this.getApplicationContext());
        manager.sendBroadcast(resultIntent);
	}
}