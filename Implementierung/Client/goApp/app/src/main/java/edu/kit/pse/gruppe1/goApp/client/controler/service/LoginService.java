package edu.kit.pse.gruppe1.goApp.client.controler.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.HTTPConnection;
import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.JSONParameter;
import edu.kit.pse.gruppe1.goApp.client.model.*;
import edu.kit.pse.gruppe1.goApp.client.view.LoginActivity;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This Service takes care of the identification process of a User and is needed whenever an User starts the App.
 */
public class LoginService extends IntentService {


    private static final String name = "LoginService";
    public static final String RESULT_LOGIN = "RESULT_LOGIN";
    private static final String ACTION_LOGIN = "ACTION_LOGIN";
    private static final String SERVLET = "LoginServlet";

    public LoginService() {

        super(name);
    }

    /**
     * checks if the user is already registers and gets the users data from the server database
     *
     * @param result the id of the user which has to be found in the server database
     * @return the user who is now logged in
     */
    public void login(Context context, GoogleSignInResult result) {

        Intent requestIntent = new Intent(context, LoginService.class);
        requestIntent.putExtra(UtilService.JSON, createJson(result).toString());
        requestIntent.setAction(ACTION_LOGIN);
        Log.i("Login",createJson(result).toString());
        context.startService(requestIntent);
    }
    // TODO - implement LoginService.login

    private JSONObject createJson(GoogleSignInResult result) {
        JSONObject requestJSON = new JSONObject();
        if (result.getSignInAccount().getIdToken() != null) {
            try {
                //TODO reichtiger JsonPArameter
                requestJSON.put(JSONParameter.GOOGLE_ID.toString(), result.getSignInAccount().getId());
                requestJSON.put(JSONParameter.USER_NAME.toString(), result.getSignInAccount().getDisplayName());
                requestJSON.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.REGISTER.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else{Log.i("Login","idToken is null");}
        return requestJSON;
    }
@Deprecated
    private User register() {
        // TODO - implement LoginService.register
        throw new UnsupportedOperationException();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("Login", "LoginService started");
        HTTPConnection connection = new HTTPConnection(SERVLET);
        Log.i("Login", intent.getStringExtra("JSON"));
        JSONObject result = connection.sendPostRequest(intent.getStringExtra(UtilService.JSON));
        Log.i("Login", "server answer received" + result.toString());
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
                //TODO ERROR MASSAGE
            }
        }
            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this.getApplicationContext());
            manager.sendBroadcast(resultIntent);

    }
}