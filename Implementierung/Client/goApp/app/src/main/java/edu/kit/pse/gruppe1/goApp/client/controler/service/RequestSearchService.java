package edu.kit.pse.gruppe1.goApp.client.controler.service;

import java.util.List;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.HTTPConnection;
import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.JSONParameter;
import edu.kit.pse.gruppe1.goApp.client.model.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static edu.kit.pse.gruppe1.goApp.client.controler.service.GroupService.ACTION_GET_EVENTS;

/**
 * This Service is used to list Requests
 */
public class RequestSearchService extends IntentService {

    public static final String NAME = "RequestSearchService";
    public static final String SERVLET = "RequestSearchServlet";
    //intent actions
    public static final String ACTION_GET_BY_USER = "GET_BY_USER";
    public static final String ACTION_GET_BY_GROUP = "GET_BY_GROUP";
    public static final String RESULT_GET_BY_GROUP = "RESULT_BY_GROUP";
    public static final String RESULT_GET_BY_USER = "RESULT_BY_USER";


    public RequestSearchService() {
        super(NAME);
    }

    /**
     * finds all Requests of a given user. . This is used to show all the requests to this user in the StartActivity
     *
     * @param user the user who started the requests
     * @return all requests the user send or null
     */
    public void getRequestsByUser(Context context, User user) {
        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.UserID.toString(), user.getId());
            requestJson.put(JSONParameter.Method.toString(), JSONParameter.Methods.GET_REQ_USR.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, RequestSearchService.class);
        requestIntent.putExtra(UtilService.JSON, requestJson.toString());
        requestIntent.setAction(ACTION_GET_BY_USER);

        context.startService(requestIntent);
    }

    /**
     * finds all access requests to a given group. This is used to present the requests to the founder of the group to let him decide about them in the GrouInfoActivity
     *
     * @param group the existing group which founder wants to access the requests
     * @return all request which are currently in the group or null if non exist
     */
    public void getRequestsByGroup(Context context, Group group) {

        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.GroupID.toString(), group.getId());
            requestJson.put(JSONParameter.Method.toString(), JSONParameter.Methods.GET_REQ_GRP.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra(UtilService.JSON, requestJson.toString());
        requestIntent.setAction(ACTION_GET_BY_GROUP);
        context.startService(requestIntent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        HTTPConnection connection = new HTTPConnection(SERVLET);
        Intent resultIntent = new Intent();
        JSONObject result;
        switch (intent.getAction()) {
            case ACTION_GET_BY_USER:
                result = connection.sendGetRequest(intent.getStringExtra(UtilService.JSON));
                resultIntent.putExtra(UtilService.GROUPS, UtilService.getGroups(result));
                resultIntent.setAction(RESULT_GET_BY_USER);
                break;
            case ACTION_GET_BY_GROUP:
                result = connection.sendGetRequest(intent.getStringExtra(UtilService.JSON));
                resultIntent.putExtra(UtilService.USERS, UtilService.getUsers(result));
                resultIntent.setAction(RESULT_GET_BY_GROUP);
                break;
            //TODO default case
        }
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this.getApplicationContext());
        manager.sendBroadcast(resultIntent);
    }
}