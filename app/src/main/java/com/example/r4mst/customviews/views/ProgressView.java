package com.example.r4mst.customviews.views;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.example.r4mst.customviews.R;
import com.example.r4mst.customviews.util.logger.LogManager;
import com.example.r4mst.customviews.util.logger.Logger;

public final class ProgressView extends View {

    private static final String TAG = "ProgressView";
    private static final int SQUARE_SIZE = 20;
    private static final int WIDTH_BETWEEN_RECT = 100;
    public static final int DURATION_ANIM_SMALL_RECT = 300;
    public static final int DURATION_BLINK_ANIM = 1000;

    private Context mContext;
    private Logger mLogger;

    private Rect mRectTopLeft;
    private Rect mRectBottomLeft;
    private Rect mRectTopRight;
    private Rect mRectBottomRight;
    private Rect mRectCenter;
    private Paint mMainPaint;
    private Paint mSecondaryPaint;
    private Path topPath;
    private Path bottomPath;
    private Path leftPath;
    private Path rightPath;
    private Path leftRightPath;
    private Path rightLeftPath;

    private int mSquareSize;
    private int mWidthBetweenRect;
    private int mColorMain;
    private int mColorSecondary;

    private float mTopLeftAnimValueTop;
    private float mTopLeftAnimValueLeft;
    private float mTopRightAnimValueLeft;
    private float mTopRightAnimValueTop;
    private float mBottomLeftAnimValueLeft;
    private float mBottomLeftAnimValueTop;
    private float mBottomRightAnimValueTop;
    private float mBottomRightAnimValueLeft;

    public ProgressView(final Context _context) {
        super(_context);
        mContext = _context;
        init(null);
    }

    public ProgressView(final Context _context, @Nullable final AttributeSet _attrs) {
        super(_context, _attrs);
        mContext = _context;
        init(_attrs);
    }

    public ProgressView(final Context _context, @Nullable final AttributeSet _attrs, final int _defStyleAttr) {
        super(_context, _attrs, _defStyleAttr);
        mContext = _context;
        init(_attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ProgressView(final Context _context, @Nullable final AttributeSet _attrs,
                        final int _defStyleAttr, final int _defStyleRes) {
        super(_context, _attrs, _defStyleAttr, _defStyleRes);
        mContext = _context;
        init(_attrs);
    }

    private void init(@Nullable final AttributeSet _set) {
        mLogger = LogManager.getLogger();
        mLogger.d(TAG, "init: init all variables");

        mRectTopLeft = new Rect();
        mRectTopRight = new Rect();
        mRectBottomLeft = new Rect();
        mRectBottomRight = new Rect();
        mRectCenter = new Rect();

        topPath = new Path();
        bottomPath = new Path();
        leftPath = new Path();
        rightPath = new Path();
        leftRightPath = new Path();
        rightLeftPath = new Path();

        mMainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mSecondaryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSecondaryPaint.setAlpha(80);

        if (_set == null) return;

        TypedArray typedArray = getContext().obtainStyledAttributes(_set, R.styleable.ProgressView);

        mColorMain = typedArray.getColor(R.styleable.ProgressView_color_main,
                ContextCompat.getColor(mContext, R.color.colorYellowMain));
        mColorSecondary = typedArray.getColor(R.styleable.ProgressView_color_secondary,
                ContextCompat.getColor(mContext, R.color.colorYellowSecondary));

        mSquareSize = typedArray.getDimensionPixelSize(R.styleable.ProgressView_square_size,
                SQUARE_SIZE);
        mWidthBetweenRect = typedArray.getDimensionPixelSize(R.styleable.ProgressView_width_between_rect,
                WIDTH_BETWEEN_RECT);

        mMainPaint.setColor(mColorMain);
        mSecondaryPaint.setColor(mColorSecondary);

        typedArray.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mTopLeftAnimValueLeft = mSquareSize;
        mTopLeftAnimValueTop = mSquareSize;
        mTopRightAnimValueLeft = 2 * mSquareSize + mWidthBetweenRect;
        mTopRightAnimValueTop = mSquareSize;
        mBottomLeftAnimValueLeft = mSquareSize;
        mBottomLeftAnimValueTop = 2 * mSquareSize + mWidthBetweenRect;
        mBottomRightAnimValueLeft = 2 * mSquareSize + mWidthBetweenRect;
        mBottomRightAnimValueTop = 2 * mSquareSize + mWidthBetweenRect;
    }

    @Override
    protected void onDraw(final Canvas _canvas) {

        mMainPaint.setStyle(Paint.Style.FILL);
        drawRects(_canvas);

        mMainPaint.setStyle(Paint.Style.STROKE);
        mMainPaint.setStrokeWidth(3);
        drawLines(_canvas);
    }

    private void drawLines(Canvas _canvas) {
        // Draw top line
        topPath.reset();
        topPath.moveTo((mTopLeftAnimValueLeft + 0.5f * mSquareSize), (mTopLeftAnimValueTop + 0.5f * mSquareSize));
        topPath.lineTo((mTopRightAnimValueLeft + 0.5f * mSquareSize), (mTopRightAnimValueTop + 0.5f * mSquareSize));
        _canvas.drawPath(topPath, mMainPaint);
        // Draw bottom line
        bottomPath.reset();
        bottomPath.moveTo((mBottomLeftAnimValueLeft + 0.5f * mSquareSize), (mBottomLeftAnimValueTop + 0.5f * mSquareSize));
        bottomPath.lineTo((mBottomRightAnimValueLeft + 0.5f * mSquareSize), (mBottomRightAnimValueTop + 0.5f * mSquareSize));
        _canvas.drawPath(bottomPath, mMainPaint);
        // Draw left line
        leftPath.reset();
        leftPath.moveTo((mTopLeftAnimValueLeft + 0.5f * mSquareSize), (mTopLeftAnimValueTop + 0.5f * mSquareSize));
        leftPath.lineTo((mBottomLeftAnimValueLeft + 0.5f * mSquareSize), (mBottomLeftAnimValueTop + 0.5f * mSquareSize));
        _canvas.drawPath(leftPath, mMainPaint);
        // Draw right line
        rightPath.reset();
        rightPath.moveTo((mTopRightAnimValueLeft + 0.5f * mSquareSize), (mTopRightAnimValueTop + 0.5f * mSquareSize));
        rightPath.lineTo((mBottomRightAnimValueLeft + 0.5f * mSquareSize), (mBottomRightAnimValueTop + 0.5f * mSquareSize));
        _canvas.drawPath(rightPath, mMainPaint);
        // Draw line from mRectTopLeft to mRectBottomRight
        leftRightPath.reset();
        leftRightPath.moveTo((mTopLeftAnimValueLeft + 0.5f * mSquareSize), (mTopLeftAnimValueTop + 0.5f * mSquareSize));
        leftRightPath.lineTo((mBottomRightAnimValueLeft + 0.5f * mSquareSize), (mBottomRightAnimValueTop + 0.5f * mSquareSize));
        _canvas.drawPath(leftRightPath, mMainPaint);
        // Draw line from mRectTopRight to mRectBottomLeft
        rightLeftPath.reset();
        rightLeftPath.moveTo((mTopRightAnimValueLeft + 0.5f * mSquareSize), (mTopRightAnimValueTop + 0.5f * mSquareSize));
        rightLeftPath.lineTo((mBottomLeftAnimValueLeft + 0.5f * mSquareSize), (mBottomLeftAnimValueTop + 0.5f * mSquareSize));
        _canvas.drawPath(rightLeftPath, mMainPaint);
    }

    private void drawRects(Canvas _canvas) {
        mRectTopLeft.left = (int) mTopLeftAnimValueLeft;
        mRectTopLeft.top = (int) mTopLeftAnimValueTop;
        mRectTopLeft.right = (int) (mTopLeftAnimValueLeft + mSquareSize);
        mRectTopLeft.bottom = (int) (mTopLeftAnimValueTop + mSquareSize);

        mRectTopRight.left = (int) mTopRightAnimValueLeft;
        mRectTopRight.top = (int) mTopRightAnimValueTop;
        mRectTopRight.right = (int) (mTopRightAnimValueLeft + mSquareSize);
        mRectTopRight.bottom = (int) (mTopRightAnimValueTop + mSquareSize);

        mRectBottomLeft.left = (int) mBottomLeftAnimValueLeft;
        mRectBottomLeft.top = (int) mBottomLeftAnimValueTop;
        mRectBottomLeft.right = (int) (mBottomLeftAnimValueLeft + mSquareSize);
        mRectBottomLeft.bottom = (int) (mBottomLeftAnimValueTop + mSquareSize);

        mRectBottomRight.left = (int) mBottomRightAnimValueLeft;
        mRectBottomRight.top = (int) mBottomRightAnimValueTop;
        mRectBottomRight.right = (int) (mBottomRightAnimValueLeft + mSquareSize);
        mRectBottomRight.bottom = (int) (mBottomRightAnimValueTop + mSquareSize);

        mRectCenter.left = (int) (2.5f * mSquareSize);
        mRectCenter.top = (int) (2.5f * mSquareSize);
        mRectCenter.right = (mRectCenter.left + (mWidthBetweenRect - mSquareSize));
        mRectCenter.bottom = (mRectCenter.top + (mWidthBetweenRect - mSquareSize));

        _canvas.drawRect(mRectTopLeft, mMainPaint);
        _canvas.drawRect(mRectTopRight, mMainPaint);
        _canvas.drawRect(mRectBottomLeft, mMainPaint);
        _canvas.drawRect(mRectBottomRight, mMainPaint);
        _canvas.drawRect(mRectCenter, mSecondaryPaint);
    }

    public final void startAnim() {
        mLogger.d(TAG, "startAnim");

        // blink animations
        ValueAnimator blinkAnimationCenterRect = getBlinkAnimationCenterRect();
        ValueAnimator blinkAnimationSmallRect = getBlinkAnimationSmallRect();

        // rect animations
        ValueAnimator topLeftAnimationTop = getTopLeftAnimationTop();
        ValueAnimator bottomRightAnimationTop = getBottomRightAnimationTop();
        ValueAnimator topRightAnimationLeft = getTopRightAnimationLeft();
        ValueAnimator bottomLeftAnimationLeft = getBottomLeftAnimationLeft();
        ValueAnimator topLeftAnimationLeft = getTopLeftAnimationLeft();
        ValueAnimator bottomRightAnimationLeft = getBottomRightAnimationLeft();
        ValueAnimator topRightAnimationTop = getTopRightAnimationTop();
        ValueAnimator bottomLeftAnimationTop = getBottomLeftAnimationTop();

        final AnimatorSet set = new AnimatorSet();
        set.playSequentially(blinkAnimationCenterRect, topLeftAnimationTop, bottomRightAnimationTop,
                topRightAnimationLeft, bottomLeftAnimationLeft, topLeftAnimationLeft,
                bottomRightAnimationLeft, topRightAnimationTop, bottomLeftAnimationTop,
                blinkAnimationSmallRect);

        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mLogger.d(TAG, "startAnim: onAnimationEnd");
                invalidate();
                reversAnim();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();
    }

    private void reversAnim() {
        mLogger.d(TAG, "reversAnim");
        // revers rect animations
        ValueAnimator reversBottomLeftAnimationTop = getReversBottomLeftAnimationTop();
        ValueAnimator reversTopRightAnimationTop = getReversTopRightAnimationTop();
        ValueAnimator reversBottomRightAnimationLeft = getReversBottomRightAnimationLeft();
        ValueAnimator reversTopLeftAnimationLeft = getReversTopLeftAnimationLeft();
        ValueAnimator reversBottomLeftAnimationLeft = getReversBottomLeftAnimationLeft();
        ValueAnimator reversTopRightAnimationLeft = getReversTopRightAnimationLeft();
        ValueAnimator reversBottomRightAnimationTop = getReversBottomRightAnimationTop();
        ValueAnimator reversTopLeftAnimationTop = getReversTopLeftAnimationTop();

        final AnimatorSet reversSet = new AnimatorSet();
        reversSet.playSequentially(reversBottomLeftAnimationTop, reversTopRightAnimationTop,
                reversBottomRightAnimationLeft, reversTopLeftAnimationLeft, reversBottomLeftAnimationLeft,
                reversTopRightAnimationLeft, reversBottomRightAnimationTop, reversTopLeftAnimationTop);

        reversSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mLogger.d(TAG, "reversAnim: onAnimationEnd");
                mSecondaryPaint.setAlpha(80);
                invalidate();
                startAnim();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        reversSet.start();
    }

    // blink animations
    @NonNull
    private ValueAnimator getBlinkAnimationSmallRect() {
        mLogger.d(TAG, "getBlinkAnimationSmallRect");

        ValueAnimator blinkAnimationSmallRect = ValueAnimator.ofInt(0, 250);
        blinkAnimationSmallRect.setRepeatCount(2);
        blinkAnimationSmallRect.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator _animation) {
                mMainPaint.setAlpha((int) _animation.getAnimatedValue());
                invalidate();
            }
        });
        blinkAnimationSmallRect.setDuration(DURATION_BLINK_ANIM);
        return blinkAnimationSmallRect;
    }

    @NonNull
    private ValueAnimator getBlinkAnimationCenterRect() {
        mLogger.d(TAG, "getBlinkAnimationCenterRect");

        ValueAnimator blinkAnimationCenterRect = ValueAnimator.ofInt(0, 80);
        blinkAnimationCenterRect.setRepeatCount(2);
        blinkAnimationCenterRect.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator _animation) {
                mSecondaryPaint.setAlpha((int) _animation.getAnimatedValue());
                invalidate();
            }
        });
        blinkAnimationCenterRect.setDuration(DURATION_BLINK_ANIM);
        blinkAnimationCenterRect.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mLogger.d(TAG, "getBlinkAnimationCenterRect: onAnimationEnd");
                mSecondaryPaint.setAlpha(0);
                invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        return blinkAnimationCenterRect;
    }

    // revers animations
    @NonNull
    private ValueAnimator getReversTopLeftAnimationTop() {
        mLogger.d(TAG, "getReversTopLeftAnimationTop");

        ValueAnimator reversTopLeftAnimationTop = ValueAnimator.ofInt(0, - mWidthBetweenRect / 2 - mSquareSize / 2);
        reversTopLeftAnimationTop.setRepeatMode(ValueAnimator.RESTART);
        reversTopLeftAnimationTop.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator _animation) {
                mTopLeftAnimValueTop = mWidthBetweenRect / 2 + 1.5f * mSquareSize + (int) _animation.getAnimatedValue();
                invalidate();
            }
        });
        reversTopLeftAnimationTop.setDuration(DURATION_ANIM_SMALL_RECT);
        return reversTopLeftAnimationTop;
    }

    @NonNull
    private ValueAnimator getReversBottomRightAnimationTop() {
        mLogger.d(TAG, "getReversBottomRightAnimationTop");

        ValueAnimator reversBottomRightAnimationTop = ValueAnimator.ofInt(0, mWidthBetweenRect / 2 + mSquareSize / 2);
        reversBottomRightAnimationTop.setRepeatMode(ValueAnimator.RESTART);
        reversBottomRightAnimationTop.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator _animation) {
                mBottomRightAnimValueTop = mWidthBetweenRect / 2 + 1.5f * mSquareSize + (int) _animation.getAnimatedValue();
                invalidate();
            }
        });
        reversBottomRightAnimationTop.setDuration(DURATION_ANIM_SMALL_RECT);
        return reversBottomRightAnimationTop;
    }

    @NonNull
    private ValueAnimator getReversTopRightAnimationLeft() {
        mLogger.d(TAG, "getReversTopRightAnimationLeft");

        ValueAnimator reversTopRightAnimationLeft = ValueAnimator.ofInt(0, mWidthBetweenRect / 2 + mSquareSize / 2);
        reversTopRightAnimationLeft.setRepeatMode(ValueAnimator.RESTART);
        reversTopRightAnimationLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator _animation) {
                mTopRightAnimValueLeft = mWidthBetweenRect / 2 + 1.5f * mSquareSize + (int) _animation.getAnimatedValue();
                invalidate();
            }
        });
        reversTopRightAnimationLeft.setDuration(DURATION_ANIM_SMALL_RECT);
        return reversTopRightAnimationLeft;
    }

    @NonNull
    private ValueAnimator getReversBottomLeftAnimationLeft() {
        mLogger.d(TAG, "getReversBottomLeftAnimationLeft");

        ValueAnimator reversBottomLeftAnimationLeft = ValueAnimator.ofInt(0, - mWidthBetweenRect / 2 - mSquareSize / 2);
        reversBottomLeftAnimationLeft.setRepeatMode(ValueAnimator.RESTART);
        reversBottomLeftAnimationLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator _animation) {
                mBottomLeftAnimValueLeft = mWidthBetweenRect / 2 + 1.5f * mSquareSize + (int) _animation.getAnimatedValue();
                invalidate();
            }
        });
        reversBottomLeftAnimationLeft.setDuration(DURATION_ANIM_SMALL_RECT);
        return reversBottomLeftAnimationLeft;
    }

    @NonNull
    private ValueAnimator getReversTopLeftAnimationLeft() {
        mLogger.d(TAG, "getReversTopLeftAnimationLeft");

        ValueAnimator reversTopLeftAnimationLeft = ValueAnimator.ofInt(0, - mWidthBetweenRect / 2 - mSquareSize / 2);
        reversTopLeftAnimationLeft.setRepeatMode(ValueAnimator.RESTART);
        reversTopLeftAnimationLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator _animation) {
                mTopLeftAnimValueLeft = mWidthBetweenRect / 2 + 1.5f * mSquareSize + (int) _animation.getAnimatedValue();
                invalidate();
            }
        });
        reversTopLeftAnimationLeft.setDuration(DURATION_ANIM_SMALL_RECT);
        return reversTopLeftAnimationLeft;
    }

    @NonNull
    private ValueAnimator getReversBottomRightAnimationLeft() {
        mLogger.d(TAG, "getReversBottomRightAnimationLeft");

        ValueAnimator reversBottomRightAnimationLeft = ValueAnimator.ofInt(0, mWidthBetweenRect / 2 + mSquareSize / 2);
        reversBottomRightAnimationLeft.setRepeatMode(ValueAnimator.RESTART);
        reversBottomRightAnimationLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator _animation) {
                mBottomRightAnimValueLeft = mWidthBetweenRect / 2 + 1.5f * mSquareSize + (int) _animation.getAnimatedValue();
                invalidate();
            }
        });
        reversBottomRightAnimationLeft.setDuration(DURATION_ANIM_SMALL_RECT);
        return reversBottomRightAnimationLeft;
    }

    @NonNull
    private ValueAnimator getReversTopRightAnimationTop() {
        mLogger.d(TAG, "getReversTopRightAnimationTop");

        ValueAnimator reversTopRightAnimationTop = ValueAnimator.ofInt(0, - mWidthBetweenRect / 2 - mSquareSize / 2);
        reversTopRightAnimationTop.setRepeatMode(ValueAnimator.RESTART);
        reversTopRightAnimationTop.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator _animation) {
                mTopRightAnimValueTop = mWidthBetweenRect / 2 + 1.5f * mSquareSize + (int) _animation.getAnimatedValue();
                invalidate();
            }
        });
        reversTopRightAnimationTop.setDuration(DURATION_ANIM_SMALL_RECT);
        return reversTopRightAnimationTop;
    }

    @NonNull
    private ValueAnimator getReversBottomLeftAnimationTop() {
        mLogger.d(TAG, "getReversBottomLeftAnimationTop");

        ValueAnimator reversBottomLeftAnimationTop = ValueAnimator.ofInt(0, mWidthBetweenRect / 2 + mSquareSize / 2);
        reversBottomLeftAnimationTop.setRepeatMode(ValueAnimator.RESTART);
        reversBottomLeftAnimationTop.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator _animation) {
                mBottomLeftAnimValueTop = mWidthBetweenRect / 2 + 1.5f * mSquareSize + (int) _animation.getAnimatedValue();
                invalidate();
            }
        });
        reversBottomLeftAnimationTop.setDuration(DURATION_ANIM_SMALL_RECT);
        return reversBottomLeftAnimationTop;
    }

    // animations
    @NonNull
    private ValueAnimator getBottomLeftAnimationTop() {
        mLogger.d(TAG, "getBottomLeftAnimationTop");

        ValueAnimator bottomLeftAnimationTop = ValueAnimator.ofInt(0, - mWidthBetweenRect / 2 - mSquareSize / 2);
        bottomLeftAnimationTop.setRepeatMode(ValueAnimator.RESTART);
        bottomLeftAnimationTop.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator _animation) {
                mBottomLeftAnimValueTop = mWidthBetweenRect + 2 * mSquareSize + (int) _animation.getAnimatedValue();
                invalidate();
            }
        });
        bottomLeftAnimationTop.setDuration(DURATION_ANIM_SMALL_RECT);
        return bottomLeftAnimationTop;
    }

    @NonNull
    private ValueAnimator getTopRightAnimationTop() {
        mLogger.d(TAG, "getTopRightAnimationTop");

        ValueAnimator topRightAnimationTop = ValueAnimator.ofInt(0, mWidthBetweenRect / 2 + mSquareSize / 2);
        topRightAnimationTop.setRepeatMode(ValueAnimator.RESTART);
        topRightAnimationTop.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator _animation) {
                mTopRightAnimValueTop = mSquareSize + (int) _animation.getAnimatedValue();
                invalidate();
            }
        });
        topRightAnimationTop.setDuration(DURATION_ANIM_SMALL_RECT);
        return topRightAnimationTop;
    }

    @NonNull
    private ValueAnimator getBottomRightAnimationLeft() {
        mLogger.d(TAG, "getBottomRightAnimationLeft");

        ValueAnimator bottomRightAnimationLeft = ValueAnimator.ofInt(0, - mWidthBetweenRect / 2 - mSquareSize / 2);
        bottomRightAnimationLeft.setRepeatMode(ValueAnimator.RESTART);
        bottomRightAnimationLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator _animation) {
                mBottomRightAnimValueLeft = mWidthBetweenRect + 2 * mSquareSize + (int) _animation.getAnimatedValue();
                invalidate();
            }
        });
        bottomRightAnimationLeft.setDuration(DURATION_ANIM_SMALL_RECT);
        return bottomRightAnimationLeft;
    }

    @NonNull
    private ValueAnimator getTopLeftAnimationLeft() {
        mLogger.d(TAG, "getTopLeftAnimationLeft");

        ValueAnimator topLeftAnimationLeft = ValueAnimator.ofInt(0, mWidthBetweenRect / 2 + mSquareSize / 2);
        topLeftAnimationLeft.setRepeatMode(ValueAnimator.RESTART);
        topLeftAnimationLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator _animation) {
                mTopLeftAnimValueLeft = mSquareSize + (int) _animation.getAnimatedValue();
                invalidate();
            }
        });
        topLeftAnimationLeft.setDuration(DURATION_ANIM_SMALL_RECT);
        return topLeftAnimationLeft;
    }

    @NonNull
    private ValueAnimator getBottomLeftAnimationLeft() {
        mLogger.d(TAG, "getBottomLeftAnimationLeft");

        ValueAnimator bottomLeftAnimationLeft = ValueAnimator.ofInt(0, mWidthBetweenRect / 2 + mSquareSize / 2);
        bottomLeftAnimationLeft.setRepeatMode(ValueAnimator.RESTART);
        bottomLeftAnimationLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator _animation) {
                mBottomLeftAnimValueLeft = mSquareSize + (int) _animation.getAnimatedValue();
                invalidate();
            }
        });
        bottomLeftAnimationLeft.setDuration(DURATION_ANIM_SMALL_RECT);
        return bottomLeftAnimationLeft;
    }

    @NonNull
    private ValueAnimator getTopRightAnimationLeft() {
        mLogger.d(TAG, "getTopRightAnimationLeft");

        ValueAnimator topRightAnimationLeft = ValueAnimator.ofInt(0, - mWidthBetweenRect / 2 - mSquareSize / 2);
        topRightAnimationLeft.setRepeatMode(ValueAnimator.RESTART);
        topRightAnimationLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator _animation) {
                mTopRightAnimValueLeft = mWidthBetweenRect + 2 * mSquareSize + (int) _animation.getAnimatedValue();
                invalidate();
            }
        });
        topRightAnimationLeft.setDuration(DURATION_ANIM_SMALL_RECT);
        return topRightAnimationLeft;
    }

    @NonNull
    private ValueAnimator getBottomRightAnimationTop() {
        mLogger.d(TAG, "getBottomRightAnimationTop");

        ValueAnimator bottomRightAnimationTop = ValueAnimator.ofInt(0, - mWidthBetweenRect / 2 - mSquareSize / 2);
        bottomRightAnimationTop.setRepeatMode(ValueAnimator.RESTART);
        bottomRightAnimationTop.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator _animation) {
                mBottomRightAnimValueTop = mWidthBetweenRect + 2 * mSquareSize + (int) _animation.getAnimatedValue();
                invalidate();
            }
        });
        bottomRightAnimationTop.setDuration(DURATION_ANIM_SMALL_RECT);
        return bottomRightAnimationTop;
    }

    @NonNull
    private ValueAnimator getTopLeftAnimationTop() {
        mLogger.d(TAG, "getTopLeftAnimationTop");

        ValueAnimator topLeftAnimationTop = ValueAnimator.ofInt(0, mWidthBetweenRect / 2 + mSquareSize / 2);
        topLeftAnimationTop.setRepeatMode(ValueAnimator.RESTART);
        topLeftAnimationTop.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator _animation) {
                mTopLeftAnimValueTop = mSquareSize + (int) _animation.getAnimatedValue();
                invalidate();
            }
        });
        topLeftAnimationTop.setDuration(DURATION_ANIM_SMALL_RECT);
        return topLeftAnimationTop;
    }

    /////
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = 4 * mSquareSize + mWidthBetweenRect;
        int width = resolveSizeAndState(size, widthMeasureSpec, 0);
        int height = resolveSizeAndState(size, heightMeasureSpec, 0);

        setMeasuredDimension(width, height);
    }

    public int getSquareSize() {
        return mSquareSize;
    }

    public void setSquareSize(int squareSize) {
        mSquareSize = squareSize;
        invalidate();
        requestLayout();
    }

    public int getWidthBetweenRect() {
        return mWidthBetweenRect;
    }

    public void setWidthBetweenRect(int widthBetweenRect) {
        mWidthBetweenRect = widthBetweenRect;
        invalidate();
        requestLayout();
    }

    public int getColorMain() {
        return mColorMain;
    }

    public void setColorMain(int colorMain) {
        mColorMain = colorMain;
        invalidate();
        requestLayout();
    }

    public int getColorSecondary() {
        return mColorSecondary;
    }

    public void setColorSecondary(int colorSecondary) {
        mColorSecondary = colorSecondary;
        invalidate();
        requestLayout();
    }
}
