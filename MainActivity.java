package com.example.termproject;

import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private int x, y;
    private String position;
    private boolean autoSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        restoreAppSettingsFromPrefs();
        restorePositionFromRestart();
    }

    private void restoreAppSettingsFromPrefs() {
        SharedPreferences preferences = getSharedPreferences ("saveLocation", MODE_PRIVATE);
        autoSave = preferences.getBoolean ("autoSave?", true);
        findViewById(R.id.action_toggle_auto_save);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_toggle_auto_save).setChecked(autoSave);
        return true;
    }
    

    private void restorePositionFromRestart() {
        if (autoSave){
            SharedPreferences location = getSharedPreferences("saveLocation", MODE_PRIVATE);
            ((TextView)findViewById(R.id.status)).setText(location.getString("savedPosition","Position: "));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        x = (int)event.getX();
        y = (int)event.getY();
        position = "Position: X: " + x + "\tY: " + y;
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE){
            ((TextView)findViewById(R.id.status)).setText(position);
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_toggle_auto_save){
            autoSave =! autoSave;
            if (item.isChecked())
                item.setChecked(false);
            else
                item.setChecked(true);
        }
        else if (item.getItemId() == R.id.action_about)
            showAbout();
        return true;
    }

    private void showAbout() {
        View v = findViewById(R.id.activity_main);
        Snackbar.make(v,getString(R.string.aboutText),Snackbar.LENGTH_INDEFINITE).show();
        //Utils.showInfoDialog(getApplicationContext(),"about",getString(R.string.aboutText));
        //above line of code was causing my program to crash so you said i should go back to snackbar but leave this in
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("savedPosition",((TextView)findViewById(R.id.status)).getText()+"");
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences location = getSharedPreferences("saveLocation", MODE_PRIVATE);
        SharedPreferences.Editor editor = location.edit();
        editor.putString("savedPosition",((TextView)findViewById(R.id.status)).getText()+"");
        editor.putBoolean ("autoSave?", autoSave);
        editor.apply();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ((TextView)findViewById(R.id.status)).setText(savedInstanceState.getString("savedPosition"));
    }
}
