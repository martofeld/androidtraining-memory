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

    private Queue<Activity> activityStack = new ConcurrentLinkedQueue<Activity>();
    private LinkedList<Activity> activityCache = new LinkedList<Activity>();

    public static StepsTracker getInstance(){
        return INSTANCE;
    }

    /**
     * Add new step to track
     * @param activity Activity to track
     */
    public void addActivityStep(Activity activity){
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
            Activity activity = activityStack.poll();
            Log.d(TAG, activity.getLocalClassName());
            activityCache.add(activity);
        }
    }

    public void dumpAllSteps(){
        Log.d(TAG, "Dumping steps because of long press");

        if (!activityCache.isEmpty()){
            for (Activity activity : activityCache){
                Log.d(TAG, activity.getLocalClassName());
            }
        }

        trackCurrentInnerSteps();
    }
}
