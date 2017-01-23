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
    public static final String ACTION_GET_BY_USER = "GET_BY_USER";
    public static final String ACTION_GET_BY_GROUP = "GET_BY_GROUP";
    public static final String SERVLET = "RequestSearchServlet";

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
            requestJson.put(JSONParameter.Method.toString(), ACTION_GET_BY_USER);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra("Json", requestJson.toString());
        requestIntent.setAction(ACTION_GET_BY_USER);

        startService(requestIntent);
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
            requestJson.put(JSONParameter.Method.toString(), ACTION_GET_BY_GROUP);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra("Json", requestJson.toString());
        requestIntent.setAction(ACTION_GET_BY_GROUP);

        startService(requestIntent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        HTTPConnection connection = new HTTPConnection(SERVLET);
        Intent resultIntent = new Intent();
        resultIntent.setAction(intent.getAction());
        JSONObject result;
        switch (intent.getAction()) {
            case ACTION_GET_BY_USER:
                result = connection.sendGetRequest(intent.getStringExtra("JSON"));
                resultIntent.putExtra("groups", getGroups(result));
                break;
            case ACTION_GET_BY_GROUP:
                result = connection.sendGetRequest(intent.getStringExtra("JSON"));
                resultIntent.putExtra("users", getUsers(result));
                break;
            //TODO default case
        }
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this.getApplicationContext());
        manager.sendBroadcast(resultIntent);
    }

    //TODO structure of the json? jsonPArameter of the array
    private Group[] getGroups(JSONObject result) {
        try {
            JSONArray jsons = result.getJSONArray(JSONParameter.GroupName.toString());
            Group[] groups = new Group[jsons.length()];
            for (int i = 0; i < jsons.length(); i++) {
                groups[i] = new Group(
                        (int) jsons.getJSONObject(i).get(JSONParameter.GroupID.toString()),
                        (String) jsons.getJSONObject(i).get(JSONParameter.GroupName.toString()));
            }
            return groups;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private User[] getUsers(JSONObject result) {
        try {
            //TODO structure of the json? jsonPArameter of the array
            JSONArray jsons = result.getJSONArray(JSONParameter.UserName.toString());
            User[] users = new User[jsons.length()];
            for (int i = 0; i < jsons.length(); i++) {
                users[i] = new User(
                        jsons.getJSONObject(i).getInt(JSONParameter.UserID.toString()),
                        jsons.getJSONObject(i).getString(JSONParameter.UserName.toString()));
            }
            return users;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}