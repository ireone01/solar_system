package com.wavez.trackerwater.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Shader;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.wavez.trackerwater.R;

import java.lang.ref.WeakReference;
import java.util.Locale;

public class WaterWaveView extends View {
    /*Displacement Animator*/
    private float shiftX1 = 0;
    private float waveVector = -0.25f;
    private int waveOffset = 25;
    private int speed = 25;
    private HandlerThread thread = new HandlerThread("WaterWaveView_" + hashCode());
    private Handler animHandler, uiHandler;

    /*brush*/
    private Paint mBorderPaint = new Paint(); //Paint of the sideline
    private Paint mViewPaint = new Paint(); //Paint of water level
    private Paint mWavePaint1 = new Paint(); //After wave coloring
    private Paint mWavePaint2 = new Paint(); //Front wave shading

    private Path mPathContent;
    private Path mPathBorder;

    /*Initial constant*/
    private static final int DEFAULT_PROGRESS = 405;
    private static final int DEFAULT_MAX = 1000;
    private static final int DEFAULT_STRONG = 50;
    public static final int DEFAULT_BEHIND_WAVE_COLOR = Color.parseColor("#90cbf9");
    public static final int DEFAULT_FRONT_WAVE_COLOR = Color.parseColor("#80c5fc");
    public static final int DEFAULT_BORDER_COLOR = Color.parseColor("#000000");
    private static final float DEFAULT_BORDER_WIDTH = 5f;
    public static final int DEFAULT_TEXT_COLOR = Color.parseColor("#000000");
    private static final boolean DEFAULT_ENABLE_ANIMATION = false;
    private static final boolean DEFAULT_HIDE_TEXT = false;

    /*Parameter value*/
    private float mShapePadding = 0f; //Indentation
    private int mProgress = DEFAULT_PROGRESS; //Water level
    private int mMax = DEFAULT_MAX; //Maximum water level
    private int mFrontWaveColor = DEFAULT_FRONT_WAVE_COLOR; //Front water wave color
    private int mBehindWaveColor = DEFAULT_BEHIND_WAVE_COLOR; //Back wave color
    private int mBorderColor = DEFAULT_BORDER_COLOR; //Edge color
    private float mBorderWidth = DEFAULT_BORDER_WIDTH; //Edge width
    private int mTextColor = DEFAULT_TEXT_COLOR; //font color
    private boolean isAnimation = DEFAULT_ENABLE_ANIMATION;
    private boolean isHideText = DEFAULT_HIDE_TEXT;
    private int mStrong = DEFAULT_STRONG; //crest
    private OnWaveStuffListener mListener;
    private Point screenSize = new Point(0, 0);

    public WaterWaveView(Context context) {
        this(context, null);
    }

    public WaterWaveView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaterWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        /*Get xml parameters*/
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircularWaterWaveView, defStyleAttr, 0);

        /*Set xml parameters*/
        mFrontWaveColor = attributes.getColor(R.styleable.CircularWaterWaveView_frontColor, DEFAULT_FRONT_WAVE_COLOR);
        mBehindWaveColor = attributes.getColor(R.styleable.CircularWaterWaveView_behindColor, DEFAULT_BEHIND_WAVE_COLOR);
        mBorderColor = attributes.getColor(R.styleable.CircularWaterWaveView_borderColor, DEFAULT_BORDER_COLOR);
        mTextColor = attributes.getColor(R.styleable.CircularWaterWaveView_textColor, DEFAULT_TEXT_COLOR);
        mProgress = attributes.getInt(R.styleable.CircularWaterWaveView_progress, DEFAULT_PROGRESS);
        mMax = attributes.getInt(R.styleable.CircularWaterWaveView_max, DEFAULT_MAX);
        mBorderWidth = attributes.getDimension(R.styleable.CircularWaterWaveView_borderWidthSize, DEFAULT_BORDER_WIDTH);
        mStrong = attributes.getInt(R.styleable.CircularWaterWaveView_strong, DEFAULT_STRONG);
        mShapePadding = attributes.getDimension(R.styleable.CircularWaterWaveView_shapePadding, 0f);
        isAnimation = attributes.getBoolean(R.styleable.CircularWaterWaveView_animatorEnable, DEFAULT_ENABLE_ANIMATION);
        isHideText = attributes.getBoolean(R.styleable.CircularWaterWaveView_textHidden, DEFAULT_HIDE_TEXT);

        /*Set anti-aliasing & set to "line"*/
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(mBorderWidth);
        mBorderPaint.setColor(mBorderColor);

        /*Create shader*/
        mWavePaint1.setStrokeWidth(2f);
        mWavePaint1.setAntiAlias(true);
        mWavePaint1.setColor(mBehindWaveColor);
        mWavePaint2.setStrokeWidth(2f);
        mWavePaint2.setAntiAlias(true);
        mWavePaint2.setColor(mFrontWaveColor);

        /*Turn on animation thread*/
        thread.start();
        animHandler = new Handler(thread.getLooper());
        uiHandler = new UIHandler(new WeakReference<View>(this));

        // Đảm bảo animation bắt đầu khi khởi tạo
        screenSize = new Point(getWidth(), getHeight());
        Message message = Message.obtain(uiHandler);
        message.sendToTarget();

        // Bắt đầu animation nếu isAnimation được bật
        if (isAnimation) {
            startAnimation();
        }
    }

    public void setProgress(int progress) {
        if (progress <= mMax) {
            if (mListener != null) {
                mListener.onStuffing(progress, mMax);
            }
            mProgress = progress;
            createShader();
            Message message = Message.obtain(uiHandler);
            message.sendToTarget();
        }
    }

    public int getProgress() {
        return mProgress;
    }

    public void startAnimation() {
        isAnimation = true;
        if (getWidth() > 0 && getHeight() > 0) {
            animHandler.removeCallbacksAndMessages(null);
            animHandler.post(new Runnable() {
                @Override
                public void run() {
                    shiftX1 += waveVector; //Displacement
                    createShader();
                    Message message = Message.obtain(uiHandler);
                    message.sendToTarget();
                    if (isAnimation) {
                        animHandler.postDelayed(this, speed);
                    }
                }
            });
        }
    }

    public void stopAnimation() {
        isAnimation = false;
    }

    public OnWaveStuffListener getListener() {
        return mListener;
    }

    public void setListener(OnWaveStuffListener mListener) {
        this.mListener = mListener;
    }

    /**
     * Set maximum value
     */
    public void setMax(int max) {
        if (mMax != max) {
            if (max >= mProgress) {
                mMax = max;
                createShader();
                Message message = Message.obtain(uiHandler);
                message.sendToTarget();
            }
        }
    }

    public int getMax() {
        return mMax;
    }

    /**
     * Set the edge color
     */
    public void setBorderColor(int color) {
        mBorderColor = color;
        mBorderPaint.setColor(mBorderColor);
        createShader();
        Message message = Message.obtain(uiHandler);
        message.sendToTarget();
    }

    /**
     * Set the front wave color
     */
    public void setFrontWaveColor(int color) {
        mFrontWaveColor = color;
        mWavePaint2.setColor(mFrontWaveColor);
        createShader();
        Message message = Message.obtain(uiHandler);
        message.sendToTarget();
    }

    /**
     * Set the color of the back wave
     */
    public void setBehindWaveColor(int color) {
        mBehindWaveColor = color;
        mWavePaint1.setColor(mBehindWaveColor);
        createShader();
        Message message = Message.obtain(uiHandler);
        message.sendToTarget();
    }

    /**
     * Set text color
     */
    public void setTextColor(int color) {
        mTextColor = color;
        createShader();
        Message message = Message.obtain(uiHandler);
        message.sendToTarget();
    }

    /**
     * Set border width
     */
    public void setBorderWidth(float width) {
        mBorderWidth = width;
        mBorderPaint.setStrokeWidth(mBorderWidth);
        createShader();
        Message message = Message.obtain(uiHandler);
        message.sendToTarget();
    }

    /**
     * Set animation speed
     * Fast -> Slow
     * 0...∞
     */
    public void setAnimationSpeed(int speed) {
        if (speed < 0) {
            throw new IllegalArgumentException("The speed must be greater than 0.");
        }
        this.speed = speed;
        Message message = Message.obtain(uiHandler);
        message.sendToTarget();
    }

    /**
     * Set how much the front and back water waves are refreshed each time
     * 0-100
     */
    public void setWaveVector(float offset) {
        if (offset < 0 || offset > 100) {
            throw new IllegalArgumentException("The vector of wave must be between 0 and 100.");
        }
        this.waveVector = (offset - 50f) / 50f;
        createShader();
        Message message = Message.obtain(uiHandler);
        message.sendToTarget();
    }

    /**
     * Set whether the font is hidden
     */
    public void setHideText(boolean hidden) {
        this.isHideText = hidden;
        Message message = Message.obtain(uiHandler);
        message.sendToTarget();
    }

    /**
     * Set the displacement of the water wave before and after
     * 1-100
     */
    public void setWaveOffset(int offset) {
        this.waveOffset = offset;
        createShader();
        Message message = Message.obtain(uiHandler);
        message.sendToTarget();
    }

    /**
     * Set crest
     * 0-100
     */
    public void setWaveStrong(int strong) {
        this.mStrong = strong;
        createShader();
        Message message = Message.obtain(uiHandler);
        message.sendToTarget();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenSize = new Point(w, h);
        createShader();
        if (isAnimation) {
            startAnimation();
        }
    }

    /**
     * Create a fill shader
     * y = Asin(ωx+φ)+h Waveform formula (sinusoidal function) y = waveLevel * Math.sin(w * x1 + shiftX) + level
     * φ (initial phase x): the offset of the X-axis of the waveform $shiftX
     *ω (angular frequency): minimum positive period T=2π/|ω| $w
     * A (wave amplitude): the size of the hump $waveLevel
     * h (initial phase y): Y-axis offset of the waveform $level
     * <p>
     * Bézier curve
     * B(t) = X(1-t)^2 + 2t(1-t)Y + Zt^2 , 0 <= t <= n
     */
    private void createShader() {
        if (screenSize.x <= 0 || screenSize.y <= 0) {
            return;
        }
        int viewSize = Math.min(screenSize.x, screenSize.y);
        double w = (2.0f * Math.PI) / viewSize;

        /*Build the canvas*/
        Bitmap bitmap = Bitmap.createBitmap(viewSize, viewSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        float level = ((((float) (mMax - mProgress)) / (float) mMax) * viewSize) + ((screenSize.y / 2) - (viewSize / 2)); //Height of water level
        int x2 = viewSize + 1;//width
        int y2 = viewSize + 1;//height
        float zzz = (((float) viewSize * ((waveOffset - 50) / 100f)) / ((float) viewSize / 6.25f));
        float shiftX2 = shiftX1 + zzz; //The difference between the front and rear waves
        int waveLevel = mStrong * (viewSize / 20) / 100;  // viewSize / 20

        for (int x1 = 0; x1 < x2; x1++) {
            /*Post wave (Overwrite)*/
            float y1 = (float) (waveLevel * Math.sin(w * x1 + shiftX1) + level);
            canvas.drawLine((float) x1, y1, (float) x1, y2, mWavePaint1);
            /*Build front wave*/
            y1 = (float) (waveLevel * Math.sin(w * x1 + shiftX2) + level);
            canvas.drawLine((float) x1, y1, (float) x1, y2, mWavePaint2);
        }

        mViewPaint.setShader(new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.CLAMP));
    }

    @Override
    protected void onDetachedFromWindow() {
        if (animHandler != null) {
            animHandler.removeCallbacksAndMessages(null);
        }
        if (thread != null) {
            thread.quit();
        }
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // Khởi động lại animation nếu nó bị dừng bởi onDetachedFromWindow
        if (thread == null || !thread.isAlive()) {
            thread = new HandlerThread("WaterWaveView_" + hashCode());
            thread.start();
            animHandler = new Handler(thread.getLooper());
        }

        if (isAnimation) {
            startAnimation();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, screenSize.x, screenSize.y, mViewPaint);

        /*Draw sidelines*/
        if (mBorderWidth > 0) {
            canvas.drawRect(0, 0, screenSize.x, screenSize.y, mBorderPaint);
        }

        if (!isHideText) {
            /*Create percentage text*/
            float percent = (mProgress * 100) / (float) mMax;
            String text = String.format(Locale.TAIWAN, "%.1f", percent) + "%";
            TextPaint textPaint = new TextPaint();
            textPaint.setColor(mTextColor);
            textPaint.setTextSize(Math.min(screenSize.x, screenSize.y) / 2f);
            textPaint.setAntiAlias(true);
            float textHeight = textPaint.descent() + textPaint.ascent();
            canvas.drawText(text, (screenSize.x - textPaint.measureText(text)) / 2.0f, (screenSize.y - textHeight) / 2.0f, textPaint);
        }
    }

    private static class UIHandler extends Handler {
        private final View mView;

        UIHandler(WeakReference<View> view) {
            super(Looper.getMainLooper());
            mView = view.get();
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mView != null) {
                mView.invalidate();
            }
        }
    }

    public interface OnWaveStuffListener {
        void onStuffing(int progress, int max);
    }
}