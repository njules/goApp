package edu.kit.pse.gruppe1.goApp.client.controler.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.JSONParameter;
import edu.kit.pse.gruppe1.goApp.client.model.*;

/**
 * This Service is needed to list various Groups at once.
 */
public class GroupSearchService extends IntentService{

    public static final String TAG = GroupSearchService.class.getSimpleName();
    public static final String ERROR_INPUT = "wrong Input";
    public static final String ERROR = "Ups, Error occured";

    public GroupSearchService(String name) {
        super(name);
    }

    /**
	 * finds all groups which the user is a member of. This is used to present the groups in the StartActivity
	 * @param user the user which groups are returned

	 */
	public void getGroupsByMember(User user) {
		if(user == null){
            Log.i(TAG, ERROR_INPUT);
        }
        JSONObject requestJson = new JSONObject();
        try {
            requestJson.put(JSONParameter.UserID.toString(), user.getId());
        } catch (JSONException e) {
            Log.i(TAG,ERROR);
        }
        Intent requestIntent = new Intent(this,GroupSearchService.class);
        requestIntent.putExtra("Json",requestJson.toString());

        startService(requestIntent);


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

	}
}