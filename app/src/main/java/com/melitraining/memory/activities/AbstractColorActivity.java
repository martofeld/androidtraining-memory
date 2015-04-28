package com.melitraining.memory.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.melitraining.memory.R;
import com.melitraining.memory.activities.colors.BlueActivity;
import com.melitraining.memory.activities.colors.CyanActivity;
import com.melitraining.memory.activities.colors.GreenActivity;
import com.melitraining.memory.activities.colors.RedActivity;
import com.melitraining.memory.tracking.StepsTracker;
import com.melitraining.memory.util.GlobalCounter;

/**
 * Created by ngiagnoni on 11/12/14.
 */
public abstract class AbstractColorActivity extends Activity {
    TextView counter;
    RelativeLayout container;
    Drawable drawable;
    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abstract_color);
        drawable = decodeSampledBitmapFromResource(getResources(), getResourceId(), 10, 10);

        container = (RelativeLayout) findViewById(R.id.color);
        container.setBackground(drawable);
        container.setOnTouchListener(listener);

        counter = (TextView) findViewById(R.id.counter);
        counter.setText(String.valueOf(GlobalCounter.COUNTER));

        StepsTracker.getInstance().addActivityStep(this.getClass().getName());
    }


    static final int MIN_DISTANCE = 100;
    private float downX, downY, upX, upY;

    private void startColorActivity(Class clasz) {
        counter = null;
        container = null;
        drawable = null;
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
     *
     * @return Solid color
     */
    protected abstract int getBackColor();

    /**
     * Get a bitmap background drawable for my current container
     *
     * @return Background drawable
     */
    protected abstract Drawable getDrawable();

    protected abstract int getResourceId();

    @Override
    public void onTrimMemory(int level) {
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            StepsTracker.getInstance().trackCurrentSteps();
        }

        super.onTrimMemory(level);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Drawable decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return new BitmapDrawable(res, BitmapFactory.decodeResource(res, resId, options));
    }
}
