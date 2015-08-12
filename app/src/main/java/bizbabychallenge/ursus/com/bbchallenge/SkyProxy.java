package bizbabychallenge.ursus.com.bbchallenge;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

/**
 * Created by someHui on 2015/8/6.
 */
public class SkyProxy{
    public static SkyProxy SUNSET = new SkyProxy( 0xFFa08020, 0xFF204080);
    public static SkyProxy DAY = new SkyProxy(0xFFe4f0c9,0xFF0396ff,0.8f,R.drawable.sun,120);
    public static SkyProxy TWILGHT =new SkyProxy(0xFFff360e,0xFFf9c700);
    public static SkyProxy NIGHT = new SkyProxy(0xFF000040,0xFF000010,0.6f,R.drawable.moon,120);

    public SkyProxy(int topColor, int bottomColor) {
        this.topColor = topColor;
        this.bottomColor = bottomColor;
        this.skyDrawable = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,new int[]{topColor,bottomColor});
        this.skyDrawable.setDither(true);
        this.haveMainStar = false;
    }

    public SkyProxy(int topColor, int bottomColor, float mainStarChance, int mainStarResId, int mainStarSize) {
        this(topColor, bottomColor);
        this.mainStarChance = mainStarChance;
        this.haveMainStar = true;
        this.mainStarResId = mainStarResId;
        this.mainStarSize = mainStarSize;
    }
    private Drawable skyDrawable;
    private int topColor;
    private int bottomColor;
    private float mainStarChance;
    private boolean haveMainStar;
    private int mainStarResId;
    private int mainStarSize;

    public int getTopColor() {
        return topColor;
    }

    public int getBottomColor() {
        return bottomColor;
    }

    public Drawable getSkyDrawable() {
        return skyDrawable;
    }

    public float getMainStarChance() {
        return mainStarChance;
    }

    public boolean isHaveMainStar() {
        return haveMainStar;
    }

    public int getMainStarResId() {
        return mainStarResId;
    }

    public int getMainStarSize() {
        return mainStarSize;
    }
}
