package bizbabychallenge.ursus.com.bbchallenge;

/**
 * Created by someHui on 2015/8/6.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.nineoldandroids.animation.TimeAnimator;

import java.util.ArrayList;

/**
 * Created by someHui on 2015/8/5.
 */
public class BizBabyChallenge extends FrameLayout {
    private TimeAnimator mClockSignal;
    private ArrayList<ObstacleView> mObstacleInScene = new ArrayList<>();
    private float mCamera_absVelocity = 0.3f;//px per millsec left(-) right(+)

    public float getCamera_absVelocity() {
        return mCamera_absVelocity;
    }

    public void setCamera_absVelocity(float camera_absVelocity) {
        this.mCamera_absVelocity = camera_absVelocity;
    }

    private final static float SCREEN_DISTANCE = 8000f;//FIXME using screen width

    private static final String TAG = "babychallenge";

    /**
     * see {@link #BizBabyChallenge(Context, AttributeSet, int)}
     */
    public BizBabyChallenge(Context context) {
        super(context, null);
    }

    /**
     * see {@link #BizBabyChallenge(Context, AttributeSet, int)}
     */
    public BizBabyChallenge(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    /**
     * see {@link #BizBabyChallenge(Context, AttributeSet, int)}
     */
    @TargetApi(21)
    public BizBabyChallenge(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * main constructor
     */
    public BizBabyChallenge(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFocusable(true);
        //TODO init PARAMS
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        reset();
        start();
    }

//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//
//    }

    private void start() {
        mClockSignal.start();
    }

    private void reset() {
        cleanAllClockElement();
        setSky(getRandomSky());
        addClockElements();
        mClockSignal = new TimeAnimator();
        mClockSignal.setTimeListener(new TimeAnimator.TimeListener() {
            @Override
            public void onTimeUpdate(TimeAnimator animation, long totalTime, long deltaTime) {
                final int count = getChildCount();
                for (int i = 0; i < count; i++) {
                    final View v = getChildAt(i);
                    if (v instanceof ClockElement) {
                        ((ClockElement) v).step(totalTime, deltaTime);
                        if (v instanceof SceneryView) {
                            SceneryView sv = ((SceneryView) v);
                            if (v.getTranslationX() + 2 * sv.width < 0) {
                                v.setTranslationX(getWidth());
                            } else if (v.getTranslationX() - sv.width > BizBabyChallenge.this.getWidth()) {
                                v.setTranslationX(-sv.width);
                            }
                        }
                    }
                }
            }
        });
    }

    private void cleanAllClockElement() {
        for (int i = getChildCount() - 1; i >= 0; i--) {
            final View v = getChildAt(i);
            if (v instanceof ClockElement) {
                removeViewAt(i);
            }
        }
        mObstacleInScene.clear();
    }

    private void addClockElements() {
        int size = 14;
        for (int i = size - 1; i >= 0; i--) {
            final SceneryView elememt = new Building(getContext(), i * (200 + randomFloat(-50, 50)), 0xFF000000 + (int) (0xFFFFFF * Math.random()));
            elememt.addedIntoView(this);
        }
    }

    private void setSky(SkyProxy sky) {
        setBackground(sky.getSkyDrawable());
        if (sky.isHaveMainStar()) {
            final Star star = new Star(getContext(), sky.getMainStarResId(), sky.getMainStarSize());
            star.addedIntoView(this);
        }
    }

    private SkyProxy getRandomSky() {
        SkyProxy sky = null;
        int type = (int) (Math.random() * 1000) % 4;
        switch (type) {
            case 0:
                sky = SkyProxy.SUNSET;
                break;
            case 1:
                sky = SkyProxy.DAY;
                break;
            case 2:
                sky = SkyProxy.TWILGHT;
                break;
            case 3:
                sky = SkyProxy.NIGHT;
                break;
        }
        return sky;
    }

    private interface ClockElement {
        public void step(long totalTime, long deltaTime);
    }

    private abstract class ObstacleView extends ImageView implements ClockElement {
        public ObstacleView(Context context) {
            super(context);
        }

        public ObstacleView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public ObstacleView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @TargetApi(21)
        public ObstacleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public void step(long totalTime, long deltaTime) {

        }

        public abstract boolean intersects();
    }

    private class SceneryView extends FrameLayout implements ClockElement {
        public int width;
        public int height;
        public float z_distance; // z>=0
        public float abs_velocity;

        public SceneryView(Context context) {
            super(context);
        }

        public void addedIntoView(BizBabyChallenge view) {
            view.addView(this, new LayoutParams(width, height));
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "hahah", Toast.LENGTH_LONG).show();
                    setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void step(long totalTime, long deltaTime) {
            setTranslationX(getTranslationX() + getViewRelativeSpeed() * deltaTime);
        }

        public float getViewRelativeSpeed() {
            return (abs_velocity - mCamera_absVelocity) * SCREEN_DISTANCE / (z_distance + SCREEN_DISTANCE);
        }
    }

    private final static int BUILDING_MIN_HEIGHT = 200;
    private final static int BUILDING_MAX_HEIGHT = 300;
    private final static int BUILDING_MIN_WIDTH = 160;
    private final static int BUILDING_MAX_WIDTH = 400;

    private class Building extends SceneryView {
        public Building(Context context, float distance, int color) {
            super(context);
            this.z_distance = distance;
            float[] hsvColor = new float[3];
            Color.colorToHSV(color, hsvColor);
            hsvColor[1] = 0.3f;
            hsvColor[2] = 1f / (z_distance / 500 + 1f);
            setBackgroundColor(Color.HSVToColor(hsvColor));
            width = (int) (randomFloat(BUILDING_MIN_WIDTH, BUILDING_MAX_WIDTH) * (SCREEN_DISTANCE / (z_distance + SCREEN_DISTANCE)));
            int viewFix = (int) ((z_distance * BizBabyChallenge.this.getHeight()) / (2 * (SCREEN_DISTANCE + z_distance)));
            height = (int) randomFloat(BUILDING_MIN_HEIGHT, BUILDING_MAX_HEIGHT) + viewFix;
        }

        @Override
        public void addedIntoView(BizBabyChallenge view) {
            LayoutParams layoutParams = new LayoutParams(width, height);
            layoutParams.gravity = Gravity.BOTTOM;
            view.addView(this, layoutParams);
            setTranslationX(randomFloat(-width, BizBabyChallenge.this.getWidth() + width));
        }
    }

    private class Star extends SceneryView {
        public Star(Context context, int resId, int size) {
            super(context);
            width = size;
            height = size;
            abs_velocity = -0.05f;
            z_distance = 500000;
            setBackgroundResource(resId);
        }

        @Override
        public void addedIntoView(BizBabyChallenge view) {
            LayoutParams layoutParams = new LayoutParams(width, height);
            view.addView(this, layoutParams);
            setTranslationX(randomFloat(width, BizBabyChallenge.this.getWidth() - width));
            setTranslationY(randomFloat(0, BizBabyChallenge.this.getHeight() / 2));

        }
    }

    private class Cloud extends SceneryView{
        public Cloud(Context context, int resId, int size) {
            super(context);
            width = size;
            height = size;
            abs_velocity = -0.05f;
            z_distance = 500000;
            setBackgroundResource(resId);
        }

        @Override
        public void addedIntoView(BizBabyChallenge view) {
            LayoutParams layoutParams = new LayoutParams(width, height);
            view.addView(this, layoutParams);
            setTranslationX(randomFloat(width, BizBabyChallenge.this.getWidth() - width));
            setTranslationY(randomFloat(0, BizBabyChallenge.this.getHeight() / 2));

        }
    }


    public static final float lerp(float x, float a, float b) {
        return (b - a) * x + a;
    }

    public static final float randomFloat(float a, float b) {
        return lerp((float) Math.random(), a, b);
    }

    private static class Params {
        //public float ;
    }
}
