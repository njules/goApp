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

import static edu.kit.pse.gruppe1.goApp.client.controler.service.RequestService.ACTION_ACCEPT;

/**
 * This Service provides methods to handle a users reaction towards an event
 */
public class ParticipateService extends IntentService {
    private static final String NAME = "ParticipateService";
    private static final String SERVLET = "ParticipateServlet";

    private static final String ACTION_STATUS = "status";
    public static final String RESULT_STATUS = "resultStatus";
    //TODO real numbers and JsonPArameter
    public static final int ACCEPT = 1;
    public static final int REJECT = 2;
    public static final int START = 3;
    //reject == delete Event

    public ParticipateService() {
        super(NAME);
    }

    /**
     * the connection between the user and the event is deleted
     *
     * @param event the event which the user doesn't want to join
     * @param user  the user who doesn't want to participate
     * @return true, if method was successful, otherwise false
     */
    public void setStatus(Context context, Event event, User user, int status) {
        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.EVENT_ID.toString(), event.getId());
            requestJson.put(JSONParameter.USER_ID.toString(), user.getId());
            //TODO jsonparameter setstatus & status
            requestJson.put(JSONParameter.STATUS.toString(), status);
            requestJson.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.SET_STATUS.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra(UtilService.JSON, requestJson.toString());
        requestIntent.putExtra(UtilService.STATUS, status);
        requestIntent.setAction(ACTION_STATUS);

        context.startService(requestIntent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        HTTPConnection connection = new HTTPConnection(SERVLET);
        Intent resultIntent = new Intent();
        JSONObject result = connection.sendPostRequest(intent.getStringExtra(UtilService.JSON));
        resultIntent.setAction(RESULT_STATUS);
        if(UtilService.isError(result)){
            resultIntent.putExtra(UtilService.ERROR,UtilService.getError(result));
        }else {
            //TODO Nur Status?
            resultIntent.putExtra(UtilService.STATUS, intent.getIntExtra(UtilService.STATUS, 0));
        }

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this.getApplicationContext());
        manager.sendBroadcast(resultIntent);
    }
}