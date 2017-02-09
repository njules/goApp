package edu.kit.pse.gruppe1.goApp.client.controler.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.HTTPConnection;
import edu.kit.pse.gruppe1.goApp.client.controler.serverConnection.JSONParameter;
import edu.kit.pse.gruppe1.goApp.client.model.Event;
import edu.kit.pse.gruppe1.goApp.client.model.Group;
import edu.kit.pse.gruppe1.goApp.client.model.Location;
import edu.kit.pse.gruppe1.goApp.client.model.Preferences;
import edu.kit.pse.gruppe1.goApp.client.view.GroupActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;

/**
 * This Service is used to notify an User that an event has started even when the Application is closed via Notificationbar
 */
public class NotificationService extends IntentService{

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
	 * send a notification to the user that the event is going to start
	 */
	private void notifyUser(Group group) {
		Preferences.setGroup(group);

		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(this)
						.setSmallIcon(R.mipmap.ic_launcher)
						.setContentTitle(getString(R.string.notification_massage))
						.setContentText(group.getName());
		Intent resultIntent = new Intent(this, GroupActivity.class);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(0, mBuilder.build());
	}

}