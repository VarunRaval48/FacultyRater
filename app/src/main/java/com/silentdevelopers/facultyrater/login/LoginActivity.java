package com.silentdevelopers.facultyrater.login;

import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.auth.api.Auth;
import com.silentdevelopers.facultyrater.R;
import com.silentdevelopers.facultyrater.StartActivity;
import com.silentdevelopers.facultyrater.main_body.MainBodyActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    private GoogleApiClient mGoogleApiClient;

    private int RC_SIGN_IN = 50, REQUEST_CODE_PICK_ACCOUNT = 999,
            REQUEST_AUTHORIZATION = 998, REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 997;

    String email, scope, oAuthscopes, faculyrater_accesstoken, email_initials, name, login_type;
    static boolean handling = false, inflate_retry;

    private SignInButton signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        scope = "audience:server:client_id:611036220045-sjstaa7r37ufc1t4q0iotb1otng8ktj2.apps.googleusercontent.com";
        oAuthscopes = "oauth2:" + "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/plus.login";

//        usingGso();
        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);

        signInButton.setOnClickListener(this);
    }

    public boolean isDeviceOnline() {
        ConnectivityManager comMng = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo netInfo = comMng.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected())
            return true;
        return false;
    }


    public void pickUserAccount() {

        if (isDeviceOnline()) {
            Log.i("Picking", "Picking User Account");
            String accountTypes[] = new String[]{"com.google"}; //com.google.android.legacyimap for all types of accounts
            Intent in = AccountPicker.newChooseAccountIntent(null, null, accountTypes, true, null, null, null, null);

            startActivityForResult(in, REQUEST_CODE_PICK_ACCOUNT);
        } else {
            Log.i("Network", "Not able to connect");
            Toast.makeText(getApplicationContext(), "Connect to a network to Log in", Toast.LENGTH_SHORT).show();
            inflateRetry();
        }
    }

    protected void inflateRetry() {
        Toast.makeText(this, "Connection Failure try Again", Toast.LENGTH_SHORT).show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("Activity Result", "Request Code " + requestCode + " Result Code " + resultCode);
        if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
            if (resultCode == RESULT_OK) {
                email = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                Log.i("MainSlider", email);
                if (!email.endsWith("nirmauni.ac.in")) {
                    Log.i("Toast", "Wrong Account");
                    Toast.makeText(this, "You must chose nirmauni account", Toast.LENGTH_SHORT).show();
//                    inflateRetry();
                    inflate_retry = true;
                } else if (isDeviceOnline()) {
                    new GetUsername(LoginActivity.this, email, scope, oAuthscopes).execute();
                } else {
                    Toast.makeText(getApplicationContext(), "Network is not accessible. Please recheck.", Toast.LENGTH_SHORT).show();
//                    inflateRetry();
                    inflate_retry = true;
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "You must chose an account to Login", Toast.LENGTH_SHORT).show();
                inflateRetry();
                inflate_retry = true;
            }
        } else if (requestCode == REQUEST_AUTHORIZATION) {
            if (resultCode == RESULT_OK) {
                new GetUsername(this, email, scope, oAuthscopes).execute();
            } else {
                inflateRetry();
                inflate_retry = true;
            }
        } else if (requestCode == REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR) {
            //called when returning from GooglePlayServices Exception
            inflateRetry();
            inflate_retry = true;
        } else if (requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    protected void tryNewToken(){
        new GetUsername(this, email, scope, oAuthscopes).execute();

    }

    public void setAccessToken(String accessToken){
        faculyrater_accesstoken = accessToken;
    }

    public void setUsername(Bundle arg){


        Log.i("Checking", "Calling sendtoserver class");
        Log.i("name", arg.getString("name"));
        Log.i("email", arg.getString("email"));

//            new SendTokenToServerClass(arg.getString("email"), arg).execute();
        //TODO send access token to server

        validateAndGo(arg);
    }

    private void validateAndGo(Bundle arg){

        String name = arg.getString("name");
        String email = arg.getString("email").split("@")[0];
        arg.putString("email", email);

        SharedPreferences.Editor editor= PreferenceManager.getDefaultSharedPreferences(this).edit();

        editor.putString("FacultyRater_name", name);
        editor.putString("FacultyRater_email", email);
        editor.putString("FacultyRater_access_token", faculyrater_accesstoken);

        editor.commit();

        Intent intent = new Intent(this, MainBodyActivity.class);
        intent.putExtra("user_info", arg);
        intent.putExtra("intent_type", "new_login");

        startActivity(intent);
        finish();
    }

        public class GetUsername extends AsyncTask<Object, Void, Bundle> {

        LoginActivity act;
        String email, scope, oAuthscopes, accessToken;
        List<NameValuePair> nameValuePairs;
        URL serverCheckuser;
        boolean waiting = false;

        ProgressDialog progressDialog;

        GetUsername(LoginActivity act, String email, String scope, String oAuthScopes) {
            this.act = act;
            this.email = email;
            this.scope = scope;
            this.oAuthscopes = oAuthScopes;

            nameValuePairs = new ArrayList<>();
        }


        protected void onPreExecute() {
//            showSpinner();

            progressDialog = new ProgressDialog(LoginActivity.this, ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected Bundle doInBackground(Object... params) {

            Bundle values = new Bundle();

            try {
                accessToken = fetchAccessToken();

                Log.i("Before", "Before waiting " + handling);

                if (accessToken != null) {

                    Log.i("LoginActivity", "After fetching access token");
                    Log.i("Access Token", accessToken);

                    setAccessToken(accessToken);

                    URL url = new URL("https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=" + accessToken);
                    StringBuffer val = returnJson(url);

                    Log.i("Value :", val.toString());

                    JSONObject reader;
                    reader = new JSONObject(val.toString());


                    name = (String) reader.get("name");
                    values.putString("email", (String) reader.get("email"));
                    values.putString("name", name);

                    values.putBoolean("verified", reader.getBoolean("verified_email"));

                } else {
//                    inflateRetry();
                    values = null;
                }
            } catch (IOException e) {
//                doOnUIInflate("retry", e);
                GoogleAuthUtil.invalidateToken(getApplicationContext(), accessToken);
                values.putString("exc", "FileIO");
                e.printStackTrace();
            } catch (JSONException e) {
//                doOnUIInflate("retry", e);
                values.putString("exc", "JSON");
                e.printStackTrace();
            }
            return values;
        }

        protected void onProgressUpdate(Void... values) {
        }

        protected void onPostExecute(Bundle result) {

        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
//            inflateRetry();
//            validateAndGo(result);
            if (result != null) {
                String temp = result.getString("exc");

                if(temp==null){
                    setUsername(result);
                }
                else if(result.getString("exc").equals("FileIO")){
//                    Toast.makeText(getApplicationContext(), "Unable to get account", Toast.LENGTH_SHORT).show();
                        tryNewToken();
                }
                else if(result.getString("exc").equals("JSON")){

                }
            }
        }

        protected String fetchAccessToken() throws IOException {
            try {
                return GoogleAuthUtil.getToken(act, email, oAuthscopes);
            } catch (GooglePlayServicesAvailabilityException e) {
                handling = true;
                Log.i("Before", "Before calling handle Exception gPlay " + handling);
                act.handleException(e);
            } catch (UserRecoverableAuthException e) {
                handling = true;
                Log.i("Before", "Before calling handle Exception recoverable");
                act.handleException(e);
            } catch (GoogleAuthException e) {
                e.printStackTrace();
            }
            return null;
        }

        StringBuffer returnJson(URL url) throws IOException {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            InputStream iStream = conn.getInputStream();

            BufferedReader read = new BufferedReader(new InputStreamReader(iStream));

            String line;
            StringBuffer val = new StringBuffer();

            while ((line = read.readLine()) != null) {
                val.append(line);
            }

            return val;
        }

        StringBuffer validate_and_get(URL url) throws IOException {

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            InputStream iStream = conn.getInputStream();

            BufferedReader read = new BufferedReader(new InputStreamReader(iStream));

            String line;
            StringBuffer val = new StringBuffer();

            while ((line = read.readLine()) != null) {
                val.append(line);
            }

            return val;
        }
    }

    public void handleException(final Exception e) {
        handling = true;
        runOnUiThread(new Runnable() {
            public void run() {
                if (e instanceof GooglePlayServicesAvailabilityException) {
                    int statusCode = ((GooglePlayServicesAvailabilityException) e)
                            .getConnectionStatusCode();
                    Dialog dialog = GooglePlayServicesUtil.getErrorDialog(statusCode,
                            LoginActivity.this, REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                    Log.i("Check First", "Going to handle");
                    dialog.show();
                } else if (e instanceof UserRecoverableAuthException) {
                    Intent intent = ((UserRecoverableAuthException) e).getIntent();
                    startActivityForResult(intent, REQUEST_AUTHORIZATION);
                }
            }
        });
        handling = false;
//        notify();
    }


    private void usingGso(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton.setScopes(gso.getScopeArray());

        signInButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.sign_in_button:
//                signIn();
                pickUserAccount();
                break;

        }
    }

    private void signIn(){

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result){

        if(result.isSuccess()){
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.i("Logged user", acct.getDisplayName());
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.i("Connection Failure", "Connection failure");
        Toast.makeText(this, "Connect to Network", Toast.LENGTH_SHORT).show();
    }

}
