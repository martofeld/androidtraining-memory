package com.melitraining.memory.tracking;

import android.app.Activity;
import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by ngiagnoni on 11/12/14.
 */
public class StepsTracker {

    /**
     * Singleton reference
     */
    private static final StepsTracker INSTANCE = new StepsTracker();

    private static final String TAG = StepsTracker.class.getName();

    /**
     * Private Constructor to avoid public instances
     */
    private StepsTracker(){}

    private Queue<String> activityStack = new ConcurrentLinkedQueue<String>();
    private LinkedList<String> activityCache = new LinkedList<String>();

    public static StepsTracker getInstance(){
        return INSTANCE;
    }

    /**
     * Add new step to track
     * @param activity Activity to track
     */
    public void addActivityStep(String activity){
        activityStack.offer(activity);
    }

    public void trackCurrentSteps(){
        if (activityStack.isEmpty())
            return;

        Log.d(TAG, "Dumping steps until UI was hidden");
        trackCurrentInnerSteps();
    }

    /**
     * Dump steps in Logcat
     */
    private void trackCurrentInnerSteps(){
        while (!activityStack.isEmpty()){
            String activity = activityStack.poll();
            Log.d(TAG, activity + "DSADSA");
            activityCache.add(activity);
        }
    }

    public void dumpAllSteps(){
        Log.d(TAG, "Dumping steps because of long press");

        if (!activityCache.isEmpty()){
            for (String activity : activityCache){
                Log.d(TAG, activity + "ASDASD");
            }
        }

        trackCurrentInnerSteps();
    }
}
