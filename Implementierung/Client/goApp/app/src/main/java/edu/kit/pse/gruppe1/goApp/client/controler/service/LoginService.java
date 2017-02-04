package edu.kit.pse.gruppe1.goApp.client.controler.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
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
        requestIntent.putExtra("JSON", createJson(result).toString());
        requestIntent.setAction(ACTION_LOGIN);
        Log.i("Login",createJson(result).toString());
        context.startService(requestIntent);
    }
    // TODO - implement LoginService.login

    private JSONObject createJson(GoogleSignInResult result) {
        JSONObject requestJSON = new JSONObject();
        if (result.getSignInAccount().getIdToken() != null) {
            try {
                requestJSON.put(JSONParameter.ID.toString(), result.getSignInAccount().getId());
                requestJSON.put(JSONParameter.UserName.toString(), result.getSignInAccount().getDisplayName());
                requestJSON.put(JSONParameter.Method.toString(), JSONParameter.Methods.LOGIN);
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
        Log.i("Login","LoginService started");
        HTTPConnection connection = new HTTPConnection(SERVLET);
        Log.i("Login","starting connection with the server");
        JSONObject result = connection.sendGetRequest(intent.getStringExtra("JSON"));
        Log.i("Login","server answer received");
        Intent resultIntent = new Intent();
        resultIntent.setAction(RESULT_LOGIN);
        try {
            int error = result.getInt(JSONParameter.ErrorCode.toString());
            if (error == 0) {
                String name = result.getString(JSONParameter.UserName.toString());
                int id = result.getInt(JSONParameter.UserID.toString());
                Preferences.setUser(new User(id, name));
                resultIntent.putExtra("ERROR", true);
            } else resultIntent.putExtra("ERROR", false);
        } catch (JSONException e) {
            e.printStackTrace();
            resultIntent.putExtra("ERROR", false);
        }
        resultIntent.setAction(RESULT_LOGIN);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this.getApplicationContext());
        manager.sendBroadcast(resultIntent);

    }
}