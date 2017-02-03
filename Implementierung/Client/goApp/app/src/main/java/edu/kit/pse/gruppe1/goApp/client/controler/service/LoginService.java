package edu.kit.pse.gruppe1.goApp.client.controler.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import edu.kit.pse.gruppe1.goApp.client.model.*;
import edu.kit.pse.gruppe1.goApp.client.view.LoginActivity;

/**
 * This Service takes care of the identification process of a User and is needed whenever an User starts the App.
 */
public class LoginService extends IntentService{


	private static final String name = "LoginService";
	public static final String RESULT_LOGIN = "RESULT_LOGIN";

	public LoginService() {

		super(name);
	}

	/**
	 * checks if the user is already registers and gets the users data from the server database
	 * @param token the id of the user which has to be found in the server database
	 * @return the user who is now logged in
	 */
	public void login(Context context, String token) {

		if(!token.isEmpty()){

			Intent resultIntent = new Intent();

			//TODO communicate with the server
			resultIntent.putExtra("ERROR", true);
			resultIntent.setAction(RESULT_LOGIN);
			//TODO get Real user
			Preferences.setUser(new User(0,"Test"));
			LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this.getApplicationContext());
			manager.sendBroadcast(resultIntent);
		}
		// TODO - implement LoginService.login
	}

	private User register() {
		// TODO - implement LoginService.register
		throw new UnsupportedOperationException();
	}

	@Override
	protected void onHandleIntent(Intent intent) {

	}
}