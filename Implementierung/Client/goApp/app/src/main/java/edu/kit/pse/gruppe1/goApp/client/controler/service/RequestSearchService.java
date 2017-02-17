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
 * This Service is used to list Requests
 */
public class RequestSearchService extends IntentService {

    private static final String NAME = "RequestSearchService";
    private static final String SERVLET = "RequestSearchServlet";
    //intent actions
    private static final String ACTION_GET_BY_USER = "GET_BY_USER";
    private static final String ACTION_GET_BY_GROUP = "GET_BY_GROUP";
    /**
     * Action of the broadcasts intent with the results of getRequestsByGroup(Context context, Group group)
     */
    public static final String RESULT_GET_BY_GROUP = "RESULT_BY_GROUP";
    /**
     * Action of the broadcasts intent with the results of getRequestsByUser(Context context, User user)
     */
    public static final String RESULT_GET_BY_USER = "RESULT_BY_USER";


    public RequestSearchService() {
        super(NAME);
    }

    /**
     * finds all Requests of a given user.This is used to show all the requests to this user in the StartActivity.
     * The result is locally broadcast with all groups in an array or an errorcode as defined in Jsonparameter.ErrorCodes
     *
     * @param context the android context to start the service
     * @param user    the user who started the requests
     */
    public void getRequestsByUser(Context context, User user) {
        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.USER_ID.toString(), user.getId());
            requestJson.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.GET_REQ_USR.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, RequestSearchService.class);
        requestIntent.putExtra(UtilService.JSON, requestJson.toString());
        requestIntent.setAction(ACTION_GET_BY_USER);

        context.startService(requestIntent);    //starts the IntentService to communicate with the server on a new thread
    }

    /**
     * finds all access requests to a given group. This is used to present the requests to the founder of the group to let him decide about them in the GrouInfoActivity.
     * The result is locally broadcast with all users in an array or an errorcode as defined in Jsonparameter.ErrorCodes
     *
     * @param context the android context to start the service
     * @param group   the existing group which founder wants to access the requests
     */
    public void getRequestsByGroup(Context context, Group group) {

        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.GROUP_ID.toString(), group.getId());
            requestJson.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.GET_REQ_GRP.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra(UtilService.JSON, requestJson.toString());
        requestIntent.setAction(ACTION_GET_BY_GROUP);
        context.startService(requestIntent);    //starts the IntentService to communicate with the server on a new thread
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        HTTPConnection connection = new HTTPConnection(SERVLET);
        Intent resultIntent = new Intent();
        JSONObject result;
        switch (intent.getAction()) {
            case ACTION_GET_BY_USER:
                result = connection.sendGetRequest(intent.getStringExtra(UtilService.JSON));
                resultIntent.setAction(RESULT_GET_BY_USER);
                if (UtilService.isError(result)) {
                    resultIntent.putExtra(UtilService.ERROR, UtilService.getError(result));
                } else {
                    resultIntent.putExtra(UtilService.GROUPS, UtilService.getGroups(result));
                }
                break;
            case ACTION_GET_BY_GROUP:
                result = connection.sendGetRequest(intent.getStringExtra(UtilService.JSON));
                resultIntent.setAction(RESULT_GET_BY_GROUP);
                if (UtilService.isError(result)) {
                    resultIntent.putExtra(UtilService.ERROR, UtilService.getError(result));
                } else {
                    resultIntent.putExtra(UtilService.USERS, UtilService.getUsers(result));
                }
                break;
            default:
                break;
        }
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this.getApplicationContext());
        manager.sendBroadcast(resultIntent);
    }
}