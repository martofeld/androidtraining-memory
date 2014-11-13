package com.melitraining.memory.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.melitraining.memory.R;
import com.melitraining.memory.activities.colors.CyanActivity;
import com.melitraining.memory.activities.colors.BlueActivity;
import com.melitraining.memory.activities.colors.GreenActivity;
import com.melitraining.memory.activities.colors.RedActivity;
import com.melitraining.memory.tracking.StepsTracker;
import com.melitraining.memory.util.GlobalCounter;

/**
 * Created by ngiagnoni on 11/12/14.
 */
public abstract class AbstractColorActivity extends Activity {
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abstract_color);
        RelativeLayout container = (RelativeLayout) findViewById(R.id.color);
        TextView counter = (TextView) findViewById(R.id.counter);
        counter.setText(String.valueOf(GlobalCounter.COUNTER));
        container.setBackground(getDrawable());
        container.setOnTouchListener(listener);
        StepsTracker.getInstance().addActivityStep(this);
    }


    static final int MIN_DISTANCE = 100;
    private float downX, downY, upX, upY;

    private void startColorActivity(Class clasz){
        GlobalCounter.COUNTER++;
        Intent i = new Intent(this, clasz);
        startActivity(i);
    }

    private void onRightToLeftSwipe() {
        startColorActivity(GreenActivity.class);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void onLeftToRightSwipe() {

        startColorActivity(BlueActivity.class);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void onTopToBottomSwipe() {
        startColorActivity(RedActivity.class);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
    }

    public void onBottomToTopSwipe() {
        startColorActivity(CyanActivity.class);
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_up);
    }

    public void onLongTap() {
        StepsTracker.getInstance().dumpAllSteps();
    }

    final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
        public void onLongPress(MotionEvent e) {
            onLongTap();
        }
    });

    @Override
    public void onBackPressed() {
        GlobalCounter.COUNTER--;
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private View.OnTouchListener listener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (gestureDetector.onTouchEvent(event))
                return true;

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    downX = event.getX();
                    downY = event.getY();
                    return true;
                }
                case MotionEvent.ACTION_UP: {
                    upX = event.getX();
                    upY = event.getY();

                    float deltaX = downX - upX;
                    float deltaY = downY - upY;

                    if (Math.abs(deltaX) > MIN_DISTANCE) {
                        if (deltaX < 0) {
                            onLeftToRightSwipe();
                            return true;
                        }
                        if (deltaX > 0) {
                            onRightToLeftSwipe();
                            return true;
                        }
                    }

                    if (Math.abs(deltaY) > MIN_DISTANCE) {
                        if (deltaY < 0) {
                            onTopToBottomSwipe();
                            return true;
                        }
                        if (deltaY > 0) {
                            onBottomToTopSwipe();
                            return true;
                        }
                    }

                    return false;
                }
            }
            return false;
        }
    };

    /**
     * Get a solid background color for my current container
     * @return Solid color
     */
    protected abstract int getBackColor();

    /**
     * Get a bitmap background drawable for my current container
     * @return Background drawable
     */
    protected abstract Drawable getDrawable();

    @Override
    public void onTrimMemory(int level) {
        if (level == TRIM_MEMORY_UI_HIDDEN){
            StepsTracker.getInstance().trackCurrentSteps();
        }

        super.onTrimMemory(level);
    }
}
