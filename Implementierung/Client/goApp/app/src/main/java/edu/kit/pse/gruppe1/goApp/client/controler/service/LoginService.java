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

/**
 * This Service takes care of the identification process of a User and is needed whenever an User starts the App.
 */
public class LoginService extends IntentService {


    private static final String NAME = "LoginService";
    private static final String SERVLET = "LoginServlet";

    private static final String ACTION_LOGIN = "ACTION_LOGIN";
    /**
     * Action of the broadcasts intent with the results of login(Context context, String token)
     */
    public static final String RESULT_LOGIN = "RESULT_LOGIN";

    public LoginService() {
        super(NAME);
    }

    /**
     * checks if the user is already registered and gets the users data from the server database.
     * If the user is not already registered he/she is added to the database.
     * It saves the user who's logged in Preferences or broadcasts an error code as defined in Jsonparameter.ErrorCodes
     *
     * @param context the android context to start the service
     * @param token the google IdToken of the user which has to be found in the server database
     */
    public void login(Context context, String token) {
        Intent requestIntent = new Intent(context, LoginService.class);
        requestIntent.putExtra(UtilService.JSON, createLogin(token).toString());
        requestIntent.setAction(ACTION_LOGIN);
        context.startService(requestIntent);    //starts the IntentService to communicate with the server on a new thread
    }

    /**
     * creates the Json for a server request to login
     * @param token the google id token
     * @return the jsonobject which is send to the server
     */
    private JSONObject createLogin(String token) {
        JSONObject requestJSON = new JSONObject();
        if (token != null) {
            try {
                requestJSON.put(JSONParameter.GOOGLE_TOKEN.toString(), token);
                requestJSON.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.LOGIN.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Log.i("Login", "idToken is null");
        }
        return requestJSON;
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        HTTPConnection connection = new HTTPConnection(SERVLET);
        JSONObject result = connection.sendPostRequest(intent.getStringExtra(UtilService.JSON));
        Intent resultIntent = new Intent();
        resultIntent.setAction(RESULT_LOGIN);
        if (UtilService.isError(result)) {
            resultIntent.putExtra(UtilService.ERROR, UtilService.getError(result));
        } else {
            try {
                String name = result.getString(JSONParameter.USER_NAME.toString());
                int id = result.getInt(JSONParameter.USER_ID.toString());
                Preferences.setUser(new User(id, name));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this.getApplicationContext());
        manager.sendBroadcast(resultIntent);
    }
}