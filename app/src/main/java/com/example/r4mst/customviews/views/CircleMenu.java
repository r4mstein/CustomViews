package com.example.r4mst.customviews.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.r4mst.customviews.R;
import com.example.r4mst.customviews.util.logger.LogManager;
import com.example.r4mst.customviews.util.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public final class CircleMenu extends View {

    private static final String TAG = "CircleMenu";
    private static final int RADIUS = 200;
    private static final int COUNT_OF_SECTORS = 6;
    private static final int MAIN_STROKE = 6;
    private static final int SECONDARY_STROKE = 10;

    private RectF mOval;
    private Paint mStrokePaint;
    private Paint mCenterStrokePaint;
    private Paint mBackgroundPaint;
    private Paint mChosenSectorPaint;
    private Paint mCenterPaint;
    private Context mContext;
    private GestureDetector mGestureDetector;
    private ValueAnimator mRotateAnimator;
    private Matrix mMatrix;

    private double mStartAngle;
    private float mGlobalStartAngel;
    private float mStartAngelForPainting;
    private float mCircleCenter;
    private boolean[] mQuadrantTouched;
    private float mStartX;
    private float mStartY;
    private int mSectorAngle;

    private OnMenuItemClickListener mItemClickListener;
    private List<Integer> mIconsForMenu = new ArrayList<>();
    private List<Bitmap> mBitmaps = new ArrayList<>();
    private Resources mResources;

    private int mRadius;
    private int mCountOfSectors;
    private int mMainStroke;
    private int mSecondaryStroke;
    private int mColorMain;
    private int mColorSecondary;
    private int mColorBackground;

    private Logger mLogger;

    public CircleMenu(final Context _context) {
        super(_context);
        mContext = _context;
        init(null);
    }

    public CircleMenu(final Context _context, @Nullable final AttributeSet _attrs) {
        super(_context, _attrs);
        mContext = _context;
        init(_attrs);
    }

    public CircleMenu(final Context _context, @Nullable final AttributeSet _attrs,
                      final int _defStyleAttr) {
        super(_context, _attrs, _defStyleAttr);
        mContext = _context;
        init(_attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CircleMenu(final Context _context, @Nullable final AttributeSet _attrs,
                      final int _defStyleAttr, final int _defStyleRes) {
        super(_context, _attrs, _defStyleAttr, _defStyleRes);
        mContext = _context;
        init(_attrs);
    }

    private void init(final AttributeSet _set) {
        mLogger = LogManager.getLogger();
        mLogger.d(TAG, "init: init all variables");

        mOval = new RectF();

        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStrokePaint.setStyle(Paint.Style.STROKE);

        mCenterStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterStrokePaint.setStyle(Paint.Style.STROKE);

        mChosenSectorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mChosenSectorPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        TypedArray typedArray = getContext().obtainStyledAttributes(_set, R.styleable.CircleMenu);

        mRadius = typedArray.getDimensionPixelSize(R.styleable.CircleMenu_radius, RADIUS);
        mCountOfSectors = typedArray.getInt(R.styleable.CircleMenu_count_of_sectors,
                COUNT_OF_SECTORS);
        mMainStroke = typedArray.getDimensionPixelSize(R.styleable.CircleMenu_main_stroke, MAIN_STROKE);
        mSecondaryStroke = typedArray.getDimensionPixelSize(R.styleable.CircleMenu_secondary_stroke,
                SECONDARY_STROKE);
        mColorMain = typedArray.getColor(R.styleable.CircleMenu_color_menu_main,
                ContextCompat.getColor(mContext, R.color.colorCircleMenuMain));
        mColorSecondary = typedArray.getColor(R.styleable.CircleMenu_color_menu_secondary,
                ContextCompat.getColor(mContext, R.color.colorCircleMenuSecondary));
        mColorBackground = typedArray.getColor(R.styleable.CircleMenu_color_background,
                ContextCompat.getColor(mContext, R.color.colorCircleMenuBackground));

        typedArray.recycle();

        mStrokePaint.setColor(mColorMain);
        mStrokePaint.setStrokeWidth(mMainStroke);
        mCenterStrokePaint.setColor(mColorSecondary);
        mCenterStrokePaint.setStrokeWidth(mSecondaryStroke);
        mChosenSectorPaint.setColor(mColorSecondary);
        mChosenSectorPaint.setStrokeWidth(mMainStroke);
        mBackgroundPaint.setColor(mColorBackground);
        mCenterPaint.setColor(mColorMain);

        mSectorAngle = 360 / mCountOfSectors;
        mCircleCenter = mRadius + 0.5f * mMainStroke;
        mOval.set(mMainStroke, mMainStroke, 2 * mRadius, 2 * mRadius);

        mQuadrantTouched = new boolean[]{false, false, false, false, false};
        mGlobalStartAngel = 0;
        mResources = mContext.getResources();
        mMatrix = new Matrix();
        mGestureDetector = new GestureDetector(mContext, new MenuGestureListener());
    }

    @Override
    protected void onDraw(final Canvas _canvas) {

        // draw background circle
        _canvas.drawCircle(mCircleCenter, mCircleCenter, mRadius - 0.5f * mMainStroke, mBackgroundPaint);

        // draw sectors
        for (int i = 0; i < mCountOfSectors; i++) {
            _canvas.drawArc(mOval, mGlobalStartAngel + i * mSectorAngle, mSectorAngle, true, mStrokePaint);
        }

        // draw stroke of the central circle
        _canvas.drawCircle(mCircleCenter, mCircleCenter, mRadius / 6, mCenterStrokePaint);

        // paint the chosen sector
        paintChosenSector(_canvas);

        // draw icons for menu
        drawIconsForMenu(_canvas);

        // draw the central circle
        _canvas.drawCircle(mCircleCenter, mCircleCenter, mRadius / 6, mCenterPaint);

        // draw stroke of the main circle
        _canvas.drawCircle(mCircleCenter, mCircleCenter, mRadius - 0.5f * mMainStroke, mStrokePaint);
    }

    @Override
    protected void onMeasure(final int _widthMeasureSpec, final int _heightMeasureSpec) {
        int size = 2 * mRadius + mMainStroke;
        int width = resolveSizeAndState(size, _widthMeasureSpec, 0);
        int height = resolveSizeAndState(size, _heightMeasureSpec, 0);

        setMeasuredDimension(width, height);
    }

    @Override
    public boolean onTouchEvent(final MotionEvent _event) {

        float touchedRadius = (float) Math.sqrt(Math.pow(_event.getX() - mCircleCenter, 2)
                + Math.pow(_event.getY() - mCircleCenter, 2));
        if (touchedRadius < mRadius) {
            switch (_event.getAction()) {
                case (MotionEvent.ACTION_DOWN):
                    //reset rotating
                    if (mRotateAnimator != null && mRotateAnimator.isStarted())
                        mRotateAnimator.cancel();
                    // reset the touched quadrants
                    for (int i = 0; i < mQuadrantTouched.length; i++) {
                        mQuadrantTouched[i] = false;
                    }
                    //turn mStartAngle between 0 and 360 degrees
                    if (mGlobalStartAngel > 360 || mGlobalStartAngel < -360)
                        mGlobalStartAngel = mGlobalStartAngel % 360;
                    mStartX = _event.getX();
                    mStartY = _event.getY();
                    mStartAngle = getAngle(mStartX, mStartY);
                    break;
                case (MotionEvent.ACTION_MOVE):
                    double currentAngle = getAngle(_event.getX(), _event.getY());
                    mGlobalStartAngel = mGlobalStartAngel + (float) (currentAngle - mStartAngle);
                    invalidate();
                    mStartAngle = currentAngle;
                    break;
                case (MotionEvent.ACTION_UP):
                    if (mStartX == _event.getX() && mStartY == _event.getY()) {
                        if (mGlobalStartAngel < 0) mGlobalStartAngel = mGlobalStartAngel + 360;

                        double angleForPaint = (getAngle(mStartX, mStartY) - mGlobalStartAngel) % 360;
                        if (angleForPaint < 0) angleForPaint = angleForPaint + 360;

                        for (int i = 0; i < mCountOfSectors; i++) {
                            if ((mSectorAngle * i <= angleForPaint) && angleForPaint < mSectorAngle * (i + 1)) {
                                if (mItemClickListener != null)
                                    mItemClickListener.onItemClick(mIconsForMenu.get(i));

                                mStartAngelForPainting = mSectorAngle * i;
                                invalidate();

                                double animAngle = (270 - mSectorAngle / 2 - mStartAngelForPainting - mGlobalStartAngel);
                                if (animAngle < -180) animAngle = 360 + animAngle;
                                if (animAngle > 180) animAngle = -360 + animAngle;

                                beginAnimation((int) animAngle);
                                break;
                            }
                        }
                    }
                    break;
            }
            // set the touched quadrant to true
            mQuadrantTouched[getQuadrant(_event.getX() - mCircleCenter, _event.getY() - mCircleCenter)] = true;

            mGestureDetector.onTouchEvent(_event);
        }

        return true;
    }

    private void beginAnimation(final int _animAngle) {
        mLogger.d(TAG, "beginAnimation: _animAngle = " + _animAngle);

        final float startAngel = mGlobalStartAngel;
        mRotateAnimator = ValueAnimator.ofInt(0, _animAngle);
        mRotateAnimator.setDuration(1500);
        mRotateAnimator.setRepeatMode(ValueAnimator.RESTART);
        mRotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                mGlobalStartAngel = startAngel + (int) animator.getAnimatedValue();
                invalidate();
            }
        });
        mRotateAnimator.start();
    }

    private void drawIconsForMenu(final Canvas _canvas) {
        if (mBitmaps != null)
            if (mBitmaps.size() > 0) {
                for (int i = 0; i < mCountOfSectors; i++) {
                    _canvas.drawBitmap(rotateBitmap(mBitmaps.get(i), mGlobalStartAngel + 90 + mSectorAngle / 2 + mSectorAngle * i), null,
                            putBitmapTo((int) (mGlobalStartAngel + i * mSectorAngle + mSectorAngle / 2)), null);
                }
            }
    }

    private Bitmap rotateBitmap(final Bitmap _source, final float _angle) {
        mMatrix.reset();
        mMatrix.postRotate(_angle);
        return Bitmap.createBitmap(_source, 0, 0, _source.getWidth(), _source.getHeight(), mMatrix, true);
    }

    private RectF putBitmapTo(final int _angle) {
        float locationX = (float) (mCircleCenter + ((mRadius / 17 * 11) * Math.cos(Math.toRadians(_angle))));
        float locationY = (float) (mCircleCenter + ((mRadius / 17 * 11) * Math.sin(Math.toRadians(_angle))));
        RectF rectF = new RectF(locationX - mRadius / 8, locationY - mRadius / 8,
                locationX + mRadius / 8, locationY + mRadius / 8);

        mMatrix.reset();
        mMatrix.setRotate(_angle, locationX, locationY);
        mMatrix.mapRect(rectF);
        return rectF;
    }

    private void paintChosenSector(final Canvas _canvas) {
        _canvas.drawArc(mOval, (mGlobalStartAngel + mStartAngelForPainting), mSectorAngle, true, mChosenSectorPaint);
    }

    private double getAngle(final double _xTouch, final double _yTouch) {
        mLogger.d(TAG, "getAngle: _xTouch = " + _xTouch + ", _yTouch = " + _yTouch);

        double x = _xTouch - mCircleCenter;
        double y = _yTouch - mCircleCenter;

        switch (getQuadrant(x, y)) {
            case 1:
                return Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            case 2:
                return 180 - Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            case 3:
                return 180 + (-1 * Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
            case 4:
                return 360 + Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            default:
                return 0;
        }
    }

    private int getQuadrant(final double _x, final double _y) {
        mLogger.d(TAG, "getQuadrant: _x = " + _x + ", _y = " + _y);

        if (_x >= 0) {
            return _y >= 0 ? 1 : 4;
        } else {
            return _y >= 0 ? 2 : 3;
        }
    }

    private class MenuGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(final MotionEvent _e1, final MotionEvent _e2,
                               final float _velocityX, final float _velocityY) {
            if (mGlobalStartAngel > 360 || mGlobalStartAngel < -360)
                mGlobalStartAngel = mGlobalStartAngel % 360;

            beginAnimation(getAngleForAnimation(_e1, _e2) * 2);
           return true;
        }

        private int getAngleForAnimation(final MotionEvent _e1, final MotionEvent _e2) {
            double startAngle = getAngle(_e1.getX(), _e1.getY());
            double endAngle = getAngle(_e2.getX(), _e2.getY());
            int startQuadrant = getQuadrant(_e1.getX() - mCircleCenter, _e1.getY() - mCircleCenter);
            int endQuadrant = getQuadrant(_e2.getX() - mCircleCenter, _e2.getY() - mCircleCenter);
            final int resultAngle;
            if ((startQuadrant == 1 && endQuadrant == 1 && startAngle > endAngle)
                    || (startQuadrant == 2 && endQuadrant == 2 && startAngle > endAngle)
                    || (startQuadrant == 3 && endQuadrant == 3 && startAngle > endAngle)
                    || (startQuadrant == 4 && endQuadrant == 4 && startAngle > endAngle)
                    || (startQuadrant == 1 && endQuadrant == 2 && mQuadrantTouched[3])
                    || (startQuadrant == 1 && endQuadrant == 3 && mQuadrantTouched[4])
                    || (startQuadrant == 1 && endQuadrant == 4 && !mQuadrantTouched[3])
                    || (startQuadrant == 2 && endQuadrant == 1 && !mQuadrantTouched[3])
                    || (startQuadrant == 2 && endQuadrant == 3 && mQuadrantTouched[4])
                    || (startQuadrant == 2 && endQuadrant == 4 && mQuadrantTouched[1])
                    || (startQuadrant == 3 && endQuadrant == 1 && mQuadrantTouched[2])
                    || (startQuadrant == 3 && endQuadrant == 2 && !mQuadrantTouched[1])
                    || (startQuadrant == 3 && endQuadrant == 4 && mQuadrantTouched[1])
                    || (startQuadrant == 4 && endQuadrant == 1 && mQuadrantTouched[3])
                    || (startQuadrant == 4 && endQuadrant == 2 && mQuadrantTouched[3])
                    || (startQuadrant == 4 && endQuadrant == 3 && !mQuadrantTouched[2])) {

                resultAngle = -1 * (int) Math.abs(endAngle - startAngle);

            } else {
                // the normal rotation
                resultAngle = (int) Math.abs(endAngle - startAngle);
            }
            mLogger.d(TAG, "getAngleForAnimation: resultAngle = " + resultAngle);
            return resultAngle;
        }
    }

    public interface OnMenuItemClickListener {
        void onItemClick(int itemId);
    }

    public void setItemClickListener(final OnMenuItemClickListener _itemClickListener) {
        mItemClickListener = _itemClickListener;
    }

    // Getters and Setters
    public List<Integer> getIconsForMenu() {
        return mIconsForMenu;
    }

    public void setIconsForMenu(List<Integer> iconsForMenu) {
        if (mCountOfSectors == 0)
            throw new IllegalArgumentException("Amount of sectors can not be equal zero");
        if (iconsForMenu.size() == 0 || iconsForMenu.size() != mCountOfSectors)
            throw new IllegalArgumentException("Some problems with count of icons");
        this.mIconsForMenu.clear();
        this.mIconsForMenu.addAll(iconsForMenu);
        if (iconsForMenu.size() != 0) {
            for (int i = 0; i < iconsForMenu.size(); i++) {
                mBitmaps.add(BitmapFactory
                        .decodeResource(mResources, iconsForMenu.get(i)));
            }
        }
        invalidate();
    }

    public int getRadius() {
        return mRadius;
    }

    public void setRadius(int radius) {
        mRadius = radius;
        mCircleCenter = mRadius + 0.5f * mMainStroke;
        mOval.set(mMainStroke, mMainStroke, 2 * mRadius, 2 * mRadius);
        invalidate();
    }

    public int getCountOfSectors() {
        return mCountOfSectors;
    }

    public void setCountOfSectors(int countOfSectors) {
        mCountOfSectors = countOfSectors;
        mSectorAngle = 360 / mCountOfSectors;
        invalidate();
    }

    public int getMainStroke() {
        return mMainStroke;
    }

    public void setMainStroke(int mainStroke) {
        mMainStroke = mainStroke;
        mStrokePaint.setStrokeWidth(mMainStroke);
        mChosenSectorPaint.setStrokeWidth(mMainStroke);
        mCircleCenter = mRadius + 0.5f * mMainStroke;
        mOval.set(mMainStroke, mMainStroke, 2 * mRadius, 2 * mRadius);
        invalidate();
    }

    public int getSecondaryStroke() {
        return mSecondaryStroke;
    }

    public void setSecondaryStroke(int secondaryStroke) {
        mSecondaryStroke = secondaryStroke;
        mCenterStrokePaint.setStrokeWidth(mSecondaryStroke);
        invalidate();
    }

    public int getColorMain() {
        return mColorMain;
    }

    public void setColorMain(int colorMain) {
        mColorMain = colorMain;
        mStrokePaint.setColor(mColorMain);
        mCenterPaint.setColor(mColorMain);
        invalidate();
    }

    public int getColorSecondary() {
        return mColorSecondary;
    }

    public void setColorSecondary(int colorSecondary) {
        mColorSecondary = colorSecondary;
        mCenterStrokePaint.setColor(mColorSecondary);
        mChosenSectorPaint.setColor(mColorSecondary);
        invalidate();
    }

    public int getColorBackground() {
        return mColorBackground;
    }

    public void setColorBackground(int colorBackground) {
        mColorBackground = colorBackground;
        mBackgroundPaint.setColor(mColorBackground);
        invalidate();
    }
}
