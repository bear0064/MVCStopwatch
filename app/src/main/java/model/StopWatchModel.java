package model;

import android.content.SharedPreferences;

import java.text.NumberFormat;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Retribution on 2015-11-11.
 */
public class StopWatchModel extends Observable {

    private static final NumberFormat NF;
    private int          mHours;
    private boolean      mIsRunning;
    private int          mMinutes;
    private int          mSeconds;
    private int          mTenthOfASecond;
    private TimerTask    mTimerTask;
    private Timer        mTimer;

    public int getmHours() {
        return mHours;
    }

    public int getmMinutes() {
        return mMinutes;
    }

    public int getmSeconds() {
        return mSeconds;
    }

    public int getmTenthOfASecond() {
        return mTenthOfASecond;
    }


//Init all class variables

    static {

        NF = NumberFormat.getInstance();
        NF.setMinimumIntegerDigits( 2 );
        NF.setMinimumIntegerDigits( 2 );

    }


    public StopWatchModel() {
        this(0, 0, 0, 0);
    }

/** Construct a StopwatchModel set to hours, minutes, seconds, and tenth of a second.
 * @param hours initial hours
 * @param minutes initial minutes
 * @param seconds initial seconds
 * @param tenthofAsecond initial tenthofAsecond
 */

    public StopWatchModel(int hours, int minutes, int seconds, int tenthofAsecond) {

        super();

        this.mHours = hours;
        this.mMinutes = minutes;
        this.mSeconds = seconds;
        this.mTenthOfASecond = tenthofAsecond;

        mIsRunning = false;

        mTimer = new Timer();
    }

    /** answer if the stopwatch is running
     * @return boolean
     */

    public boolean isRunning() {

        return mIsRunning;
    }


    /** reset this stopwatch to 0
     *
     */

    public void reset() {
        mHours = mMinutes = mSeconds = mTenthOfASecond = 0;

        //model has changed state

        this.updateObservers();
    }


    /**
     * Start this stopwatch
     *
     * only start a stopped stopwatch
     */
    public void start() {
        if (this.isRunning() == false) {

            mTimerTask = new StopWatchTask();
            mTimer.scheduleAtFixedRate( mTimerTask, 0L, 100l);
            mIsRunning = true;

        }


        this.updateObservers();

    }

    /** stop this stopwatch
     *
     *
     * only stop a running stopwatch
     */



    public void stop() {

        if (this.isRunning() == true ){

            mTimerTask.cancel();
            mIsRunning = false;
        }

        this.updateObservers();

    }


    /**
     * returns a string that represents the value of this object
     * @return a string representation of the receiver
     */

    @Override
    public String toString() {

        return (NF.format(mHours)
                + ":" + NF.format(mMinutes)
                + ":" + NF.format(mSeconds)
                + ":" + mTenthOfASecond);
    }


    /**
     * notify all registered observers that the model has changed
     */

    private void updateObservers() {

        this.setChanged();
        this.notifyObservers();
    }

    private class StopWatchTask extends TimerTask {

        /**
         * One tenth of a second of time has gone by, and the timer has gone off
         * Increment the number of tenth of a seconds, and do time arithmetic
         * finally, inform all registered observers that the model has changed
         *
         */

        @Override
        public void run() {

            mTenthOfASecond++;
            if (mTenthOfASecond == 10) {
                mTenthOfASecond = 0;
                mSeconds++;
                if (mSeconds >= 60) {
                    mSeconds = 0;
                    mMinutes++;
                    if (mMinutes >= 60) {
                        mMinutes = 0;
                        mHours++;

                    }
                }
            }

            updateObservers();
        }
    }


}
