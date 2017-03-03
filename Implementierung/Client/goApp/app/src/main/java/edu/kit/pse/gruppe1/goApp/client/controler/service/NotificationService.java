package edu.kit.pse.gruppe1.goApp.client.controler.service;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.model.Group;
import edu.kit.pse.gruppe1.goApp.client.model.Preferences;
import edu.kit.pse.gruppe1.goApp.client.view.LoginActivity;

/**
 * This Service is used to notify an User that an Event is going to start even when the Application is closed.
 */
public class NotificationService extends IntentService {

    private static final String NAME = "NotificationService";
    private static final String ACTION_GET = "GET";
    private static final String SERVLET = "EventServlet";
    private static final int ERROR_ID = -1;

    public NotificationService() {
        super(NAME);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("NOTIFICATION SERVICE", "Start");
        Group group = intent.getParcelableExtra(UtilService.GROUP);
        notifyUser(group);
    }

    /**
     * Sends a notification to the user that the Event is going to start
     *
     * @param group The group in which the Event is going to take place.
     */
    private void notifyUser(Group group) {
        Preferences.setGroup(group);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(getString(R.string.notification_massage))
                        .setAutoCancel(true)
                        .setContentText(group.getName());
        Intent resultIntent = new Intent(this, LoginActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }

}