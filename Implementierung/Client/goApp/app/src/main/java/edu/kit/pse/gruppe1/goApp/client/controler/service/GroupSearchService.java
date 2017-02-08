package edu.kit.pse.gruppe1.goApp.client.controler.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.HTTPConnection;
import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.JSONParameter;
import edu.kit.pse.gruppe1.goApp.client.model.*;

/**
 * This Service is needed to list various Groups at once.
 */
public class GroupSearchService extends IntentService {

    public static final String SERVLET = "GroupSearchServlet";
    private static final String name = "GroupSearchService";
    //Intent action to start the service
    public static final String ACTION_GET_BY_NAME = "GET_BY_NAME";
    public static final String ACTION_GET_BY_MEMBER = "GET_BY_MEMBER";
    //Intent action to broadcast results
    public static final String RESULT_GET_BY_MEMBER = "RESULT_BY_MEMBER";
    public static final String RESULT_GET_BY_NAME = "RESULT_BY_NAME";


    public GroupSearchService() {
        super(name);
    }


    /**
     * finds all groups which the user is a member of. This is used to present the groups in the StartActivity
     *
     * @param user the user which groups are returned
     */
    public void getGroupsByMember(Context context, User user) {
        //TODO what if Parameter == null
        if (user == null) {
        }
        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.UserID.toString(),user.getId());
            requestJson.put(JSONParameter.Method.toString(), JSONParameter.Methods.GET_GRP_MEM.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent requestIntent = new Intent(context, GroupSearchService.class);
        requestIntent.putExtra(UtilService.JSON, requestJson.toString());
        requestIntent.setAction(ACTION_GET_BY_MEMBER);
        context.startService(requestIntent);
    }

    /**
     * finds all groups which name include the given string to show the results of a search request by the user using the search function of the NewGroupActivity
     *
     * @param name the string which the user typed in the NewGroupActivity to find a new group he wants to be member of with that name
     * @return all groups the name is included in the group name or null
     */
    public void getGroupsByName(Context context, String name) {

        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.GroupName.toString(), name);
            requestJson.put(JSONParameter.Method.toString(), JSONParameter.Methods.GET_GRP_NAME.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra(UtilService.JSON, requestJson.toString());
        requestIntent.setAction(ACTION_GET_BY_NAME);
        context.startService(requestIntent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Communication with server
        Log.i("GroupSearch",intent.getStringExtra(UtilService.JSON));
        HTTPConnection connection = new HTTPConnection(SERVLET);
        JSONObject result = connection.sendGetRequest(intent.getStringExtra(UtilService.JSON));
        Log.i("SearchGroup",result.toString());
        Group[] groups = UtilService.getGroups(result);
        //result for the activtiy
        Intent resultIntent = new Intent();
        resultIntent.putExtra(UtilService.GROUPS, groups);
        switch (intent.getAction()) {
            case ACTION_GET_BY_MEMBER:
                resultIntent.setAction(RESULT_GET_BY_MEMBER);
                break;
            case ACTION_GET_BY_NAME:
                resultIntent.setAction(RESULT_GET_BY_NAME);
                break;
            //TODO default:
        }

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this.getApplicationContext());
        manager.sendBroadcast(resultIntent);
    }
}