package com.silentdevelopers.facultyrater;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.silentdevelopers.facultyrater.login.LoginActivity;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Intent in = getIntent();
        Bundle bundle =  in.getBundleExtra("info");
        Log.i("StartAct", bundle.getInt("id")+"");
        TextView textView = (TextView)findViewById(R.id.textView_startAct_name);
        textView.setText(bundle.getInt("id")+"");
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
