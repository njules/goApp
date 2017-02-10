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
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import edu.kit.pse.gruppe1.goApp.client.R;
import edu.kit.pse.gruppe1.goApp.client.controler.service.LoginService;
import edu.kit.pse.gruppe1.goApp.client.controler.service.UtilService;
import edu.kit.pse.gruppe1.goApp.client.model.Preferences;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, View.OnClickListener {

    private static final int RC_SIGN_IN = 1111;
    GoogleApiClient googleApiClient;
    GoogleSignInOptions googleSignInOptions;
    LoginService loginService;
    ResultReceiver receiver;


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
            if (result.isSuccess()) {
                String idToken = result.getSignInAccount().getIdToken();
                Preferences.setIdToken(idToken);
                loginService.register(this, idToken);
            } else {
                Log.i("Login", result.getStatus().toString());
            }
        }
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activty);
        findViewById(R.id.sign_in).setOnClickListener(this);
        Toolbar loginToolbar = (Toolbar) findViewById(R.id.login_toolbar);
        setSupportActionBar(loginToolbar);
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.clientId)).build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();
        receiver = new ResultReceiver();
        loginService = new LoginService();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(LoginService.RESULT_LOGIN));

        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET};
        ActivityCompat.requestPermissions(this, permissions, 0);
    }

    private void SignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        Toast.makeText(this, "starting google sign in", Toast.LENGTH_SHORT).show();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        trySilentSignIn();
    }

    private void trySilentSignIn() {
        Auth.GoogleSignInApi.silentSignIn(googleApiClient).setResultCallback(new ResultCallback<GoogleSignInResult>() {

            @Override
            public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                if (googleSignInResult.isSuccess()) {
                   loginService.login(LoginActivity.this,googleSignInResult.getSignInAccount().getIdToken());
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

    private class ResultReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra(UtilService.ERROR) != null) {
                Toast.makeText(getApplicationContext(), intent.getStringExtra(UtilService.ERROR), Toast.LENGTH_LONG).show();
                return;
            }
            if (intent.getAction() == LoginService.RESULT_LOGIN) {
                    Toast.makeText(LoginActivity.this, "Login erfolgreich", Toast.LENGTH_SHORT).show();
                    StartActivity.start(LoginActivity.this);
            }
        }
    }
}