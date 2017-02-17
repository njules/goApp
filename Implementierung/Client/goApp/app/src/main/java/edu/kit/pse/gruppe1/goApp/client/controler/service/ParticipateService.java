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
 * This Service provides methods to handle a users reaction towards an event
 */
public class ParticipateService extends IntentService {
    private static final String NAME = "ParticipateService";
    private static final String SERVLET = "ParticipateServlet";

    private static final String ACTION_STATUS = "status";
    /**
     * Action of the broadcasts intent with the results of setStatus(Context context, Event event, User user, Status status)
     */
    public static final String RESULT_STATUS = "resultStatus";

    public ParticipateService() {
        super(NAME);
    }

    /**
     * sets the status of the user in the event and broadcastes the new status or an errorcode as defined in Jsonparameter.ErrorCodes
     *
     * @param context the android context to start the service
     * @param event the event which the user changes the status in
     * @param user  the user who's status changes
     */
    public void setStatus(Context context, Event event, User user, Status status) {
        JSONObject requestJson = new JSONObject();

        try {
            requestJson.put(JSONParameter.EVENT_ID.toString(), event.getId());
            requestJson.put(JSONParameter.USER_ID.toString(), user.getId());
            requestJson.put(JSONParameter.STATUS.toString(), status.getValue());
            requestJson.put(JSONParameter.METHOD.toString(), JSONParameter.Methods.SET_STATUS.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent requestIntent = new Intent(context, this.getClass());
        requestIntent.putExtra(UtilService.JSON, requestJson.toString());
        requestIntent.putExtra(UtilService.STATUS, status.getValue());
        requestIntent.setAction(ACTION_STATUS);

        context.startService(requestIntent);    //starts the IntentService to communicate with the server on a new thread
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent.getAction() == ACTION_STATUS) {
            HTTPConnection connection = new HTTPConnection(SERVLET);
            Intent resultIntent = new Intent();
            JSONObject result = connection.sendPostRequest(intent.getStringExtra(UtilService.JSON));
            resultIntent.setAction(RESULT_STATUS);
            if (UtilService.isError(result)) {
                resultIntent.putExtra(UtilService.ERROR, UtilService.getError(result));
            } else {
                resultIntent.putExtra(UtilService.STATUS, intent.getIntExtra(UtilService.STATUS, 0));
            }
            LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this.getApplicationContext());
            manager.sendBroadcast(resultIntent);
        }
    }
}