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
 * This Service provides methods to handle a Request.
 */
public class RequestService extends IntentService {

    private static final String NAME = "RequestService";
    private static final String SERVLET = "RequestServlet";
    //Intent actions
    private static final String ACTION_CREATE = "CREATE";
    private static final String ACTION_ACCEPT = "ACCEPT";
    private static final String ACTION_REJECT = "REJECT";
    private static final String ACTION_DELETE = "ACTION_DELETE";

    /**
     * Action of the broadcasts intent with the results of reject(Context context, Request request)
     */
    public static final String RESULT_REJECT = "RESULT_REJECT_REQUEST";
    /**
     * Action of the broadcasts intent with the results of accept(Context context, Request request)
     */
    public static final String RESULT_ACCEPT = "RESULT_ACCEPT_REQUEST";
    /**
     * Action of the broadcasts intent with the results of create(Context context, User user, Group group)
     */
    public static final String RESULT_CREATE = "RESULT_CREATE_REQUEST";
    /**
     * Action of the broadcasts intent with the results of delete(Context context, Request request)
     */
    public static final String RESULT_DELETE = "RESULT_DELETE";

    public RequestService() {
        super(NAME);
    }

    /**
     * creates a new Request.
     * Broadcasts if an error occurred as defined in Jsonparameter.ErrorCodes
     *
     * @param context the android context to start the service
     * @param user  the user who sends the request
     * @param group the group the user wants to be a member of
     */
    public void create(Context context, User user, Group group) {
        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.GROUP_ID.toString(), group.getId());
            requestJson.put(JSONParameter.USER_ID.toString(), user.getId());
            requestJson.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.CREATE.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra(UtilService.JSON, requestJson.toString());
        requestIntent.setAction(ACTION_CREATE);

        context.startService(requestIntent);    //starts the IntentService to communicate with the server on a new thread
    }

    /**
     * Adds the user to the group and deletes the Request if the founder of the group wants the user to be in the group
     * Broadcasts if an error occurred as defined in Jsonparameter.ErrorCodes
     *
     * @param context the android context to start the service
     * @param request the request the founder has made a decision about
     */
    public void accept(Context context, Request request) {
        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.GROUP_ID.toString(), request.getGroup().getId());
            requestJson.put(JSONParameter.USER_ID.toString(), request.getUser().getId());
            requestJson.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.ACCEPT);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra(UtilService.JSON, requestJson.toString());
        requestIntent.setAction(ACTION_ACCEPT);

        context.startService(requestIntent);    //starts the IntentService to communicate with the server on a new thread
    }

    /**
     * Deletes a Requests from the server if the user changed his/her mind to enter a group.
     * Broadcasts if an error occurred as defined in Jsonparameter.ErrorCodes
     *
     * @param context the android context to start the service
     * @param request the request the user wants to delete
     */
    public void delete(Context context, Request request) {
        Intent requestIntent = deleteRequest(context, request);
        requestIntent.setAction(ACTION_DELETE);
        context.startService(requestIntent);    //starts the IntentService to communicate with the server on a new thread
    }

    /**
     * Deletes a request from the server if the founder rejects the user
     * Broadcasts if an error occurred as defined in Jsonparameter.ErrorCodes
     *
     * @param context the android context to start the service
     * @param request the request the founder has made a decision about
     */
    public void reject(Context context, Request request) {
        Intent requestIntent = deleteRequest(context, request);
        requestIntent.setAction(ACTION_REJECT);
        context.startService(requestIntent);    //starts the IntentService to communicate with the server on a new thread
    }

    /**
     * Adds the request and methods.reject to the json that will be send to the server. And adds this json to the return intent.
     *
     * @param context the android context to create an intent
     * @param request the request that is added to the json
     * @return an intent to start the service
     */
    private Intent deleteRequest(Context context, Request request) {
        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.GROUP_ID.toString(), request.getGroup().getId());
            requestJson.put(JSONParameter.USER_ID.toString(), request.getUser().getId());
            requestJson.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.REJECT); // reject and delete are on the server the same
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra(UtilService.JSON, requestJson.toString());
        return requestIntent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        HTTPConnection connection = new HTTPConnection(SERVLET);
        Intent resultIntent = new Intent();
        JSONObject result;
        switch (intent.getAction()) {
            case ACTION_CREATE:
                result = connection.sendPostRequest(intent.getStringExtra(UtilService.JSON));
                resultIntent.setAction(RESULT_CREATE);
                if (UtilService.isError(result)) {
                    resultIntent.putExtra(UtilService.ERROR, UtilService.getError(result));
                }
                break;
            case ACTION_REJECT:
                result = connection.sendPostRequest(intent.getStringExtra(UtilService.JSON));
                resultIntent.setAction(RESULT_REJECT);
                if (UtilService.isError(result)) {
                    resultIntent.putExtra(UtilService.ERROR, UtilService.getError(result));
                }
                break;
            case ACTION_DELETE:
                result = connection.sendPostRequest(intent.getStringExtra(UtilService.JSON));
                resultIntent.setAction(RESULT_DELETE);
                if (UtilService.isError(result)) {
                    resultIntent.putExtra(UtilService.ERROR, UtilService.getError(result));
                }
                break;
            case ACTION_ACCEPT:
                result = connection.sendPostRequest(intent.getStringExtra(UtilService.JSON));
                resultIntent.setAction(RESULT_ACCEPT);
                if (UtilService.isError(result)) {
                    resultIntent.putExtra(UtilService.ERROR, UtilService.getError(result));
                }
                break;
            default:
                break;
        }
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this.getApplicationContext());
        manager.sendBroadcast(resultIntent);
    }
}