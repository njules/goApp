package edu.kit.pse.gruppe1.goApp.client.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {


    GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();
    GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this).build();

   public void OnActivityResult(){

    }
    private void SignIn(){
        Auth.GoogleSignInApi.getSignInIntent(googleApiClient);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}