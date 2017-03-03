package edu.kit.pse.gruppe1.goApp.client.view;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.controler.service.LoginService;
import edu.kit.pse.gruppe1.goApp.client.controler.service.UtilService;
import edu.kit.pse.gruppe1.goApp.client.model.Preferences;

import java.util.Objects;

/**
 * The LoginActivity is the first Activity to start when the goApp starts.
 * The User needs to sign in with his Google Account to continue.
 */
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 1111;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions googleSignInOptions;
    private LoginService loginService;
    private ResultReceiver receiver;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
            if (result.isSuccess()) {
                String idToken = result.getSignInAccount().getIdToken();
                Preferences.setIdToken(idToken);
                loginService.login(this, idToken);
            } else {
                Log.i("Login", result.getStatus().toString());
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiver = new ResultReceiver();
        loginService = new LoginService();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(LoginService.RESULT_LOGIN));

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.clientId)).build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
                .addConnectionCallbacks(this).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();

        setContentView(R.layout.login_activty);
        findViewById(R.id.sign_in).setOnClickListener(this);
        Toolbar loginToolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(loginToolbar);

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET};
        ActivityCompat.requestPermissions(this, permissions, 0);
    }

    /**
     * starts google sign in flow with a signInIntent. Results are received in OnActivityResult()
     */
    private void SignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        Toast.makeText(this, R.string.starting_google_sign_in, Toast.LENGTH_SHORT).show();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        trySilentSignIn();
    }


    /**
     * starts google silent sign in with an intent and starts loginService.login() with the received idToken
     */
    private void trySilentSignIn() {
        Auth.GoogleSignInApi.silentSignIn(googleApiClient).setResultCallback(new ResultCallback<GoogleSignInResult>() {

            @Override
            public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                if (googleSignInResult.isSuccess()) {
                    loginService.login(LoginActivity.this, googleSignInResult.getSignInAccount().getIdToken());
                }
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onClick(View view) {
        SignIn();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    /**
     * The ResultReceiver evaluates return messages from earlier started Services.
     */
    private class ResultReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // If the intent shows any kind of error the user will be notified.
            if (intent.getStringExtra(UtilService.ERROR) != null) {
                Toast.makeText(getApplicationContext(), intent.getStringExtra(UtilService.ERROR), Toast.LENGTH_LONG).show();
                return;
            }
            //starts the StartActivity after a successful login
            if (Objects.equals(intent.getAction(), LoginService.RESULT_LOGIN)) {
                Toast.makeText(LoginActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                finish();
                StartActivity.start(LoginActivity.this);
            }
        }
    }
}