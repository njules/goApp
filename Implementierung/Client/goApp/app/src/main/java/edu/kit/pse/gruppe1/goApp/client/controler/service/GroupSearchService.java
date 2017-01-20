package edu.kit.pse.gruppe1.goApp.client.controler.service;

import android.app.IntentService;
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
public class GroupSearchService extends IntentService{

    public static final String TAG = GroupSearchService.class.getSimpleName();
    public static final String ERROR_INPUT = "wrong Input";
    public static final String ERROR = "Ups, Error occured";
    private boolean running = false;
    private static final String name = "GroupSearchService";

    public GroupSearchService() {
        super(name);
    }


    /**
	 * finds all groups which the user is a member of. This is used to present the groups in the StartActivity
	 * @param user the user which groups are returned

	 */
	public void getGroupsByMember(Context context,User user) {
		if(user == null) {
            throw new IllegalArgumentException();
        }
        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.UserID.toString(), user.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context,GroupSearchService.class);
        requestIntent.putExtra("Json",requestJson.toString());

        startService(requestIntent);
        running = true;


    }

	/**
	 * finds all groups which name include the given string to show the results of a search request by the user using the search function of the NewGroupActivity
	 * @param name the string which the user typed in the NewGroupActivity to find a new group he wants to be member of with that name
	 * @return all groups the name is included in the group name or null
	 */
	private List<Group> getGroupsByName(String name) {
		// TODO - implement GroupSearchService.getGroupsByName
		throw new UnsupportedOperationException();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
        Group[]groups = getGroups();
        Intent resultIntent = new Intent();
        Bundle groupBundle = new Bundle();
        resultIntent.putExtra("groups",groupBundle);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this.getApplicationContext());
        manager.sendBroadcast(resultIntent);
    }

    public boolean isRunning() {
        return running;
    }

    private Group[] getGroups(){
        Group[] group = new Group[20];
        for (int i = 0; i < 20 ; i++) {
            group[i] = new Group(i,"name"+i);
        }
        return  group;
    }

    @Nullable
    private Group[] getGroups(String json){
        HTTPConnection connection = new HTTPConnection("GroupSearchServlet");
        JSONObject result = connection.sendGetRequest(json);
        try {
           JSONArray jsons = result.getJSONArray(JSONParameter.GroupName.toString());
            Group[] groups = new Group[jsons.length()];
            for (int i = 0; i < jsons.length(); i++) {
                groups[i] = new Group(
                        (int)jsons.getJSONObject(i).get(JSONParameter.GroupID.toString()),
                        (String)jsons.getJSONObject(i).get(JSONParameter.GroupName.toString()));
                return groups;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}