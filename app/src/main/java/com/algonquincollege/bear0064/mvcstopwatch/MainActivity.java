package com.algonquincollege.bear0064.mvcstopwatch;

import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import java.util.Observable;
import java.util.Observer;
import model.StopWatchModel;


/**
 *  To utilize threads by implementing a timer based application
 *  @author Caleb Bear bear0064@algonquinlive.com
 */


public class MainActivity extends AppCompatActivity implements Observer {

    private static final String ABOUT_DIALOG_TAG = "About";

    private FloatingActionButton fab;

    private DialogFragment      mAboutDialog;
    private StopWatchModel      mStopWatchModel;
    private TextView            mStopWatchView;
    private Runnable            mUpdateStopWatch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences settings = getSharedPreferences( getResources().getString(R.string.app_name), Context.MODE_PRIVATE );

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( mStopWatchModel.isRunning() ){
                    mStopWatchModel.stop();
                }else{
                    mStopWatchModel.start();
                }
            }
        });

        mAboutDialog = new AboutDialogFragment();

        mStopWatchModel = new StopWatchModel( settings.getInt("hours", 0),
                settings.getInt("minutes", 0),
                settings.getInt("seconds", 0),
                settings.getInt("tenthOfASecond", 0) );

        mStopWatchModel.addObserver( this );

        mStopWatchView = (TextView) findViewById( R.id.textViewStopwatch);

        mUpdateStopWatch = new Runnable() {
            @Override
            public void run() {

                mStopWatchView.setText( mStopWatchModel.toString() );

                if ( mStopWatchModel.isRunning() ) {

                    fab.setImageResource( android.R.drawable.ic_media_pause);

                } else {

                    fab.setImageResource( android.R.drawable.ic_media_play );
                }


            }
        };



        mStopWatchView.setText( mStopWatchModel.toString() );


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {

            mAboutDialog.show( getFragmentManager(), ABOUT_DIALOG_TAG);

            return true;
        }

        if (id == R.id.action_reset) {
            mStopWatchModel.reset();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void update(Observable observable, Object data) {

        runOnUiThread( mUpdateStopWatch );

    }

// Remember the user's settings for H:M:S:T

    @Override
    protected void onStop() {
        super.onStop();

        // We need an Editor object to make preference changes.
        SharedPreferences settings = getSharedPreferences( getResources().getString(R.string.app_name), Context.MODE_PRIVATE );
        SharedPreferences.Editor editor = settings.edit();

        editor.putInt( "hours",   mStopWatchModel.getmHours() );
        editor.putInt( "minutes", mStopWatchModel.getmMinutes() );
        editor.putInt( "seconds",  mStopWatchModel.getmSeconds() );
        editor.putInt( "tenthOfASecond", mStopWatchModel.getmTenthOfASecond() );

        // Commit the edits!
        editor.commit();
    }



}
