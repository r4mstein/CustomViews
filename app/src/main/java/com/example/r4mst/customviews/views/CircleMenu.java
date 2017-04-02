package com.example.r4mst.customviews.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import com.example.r4mst.customviews.util.logger.LogManager;
import com.example.r4mst.customviews.util.logger.Logger;

public final class CircleMenu extends View {

    private static final String TAG = "CircleMenu";
    private static final int RADIUS = 200;
    private static final int PADDING = 40;
    public static final float COUNT_OF_SECTORS = 6;
    
    private RectF mOval;
    private Paint mStrokePaintPaint;
    private Paint mCenterStrokePaint;
    private Paint mBackgroundPaint;
    private Paint mCenterPaint;
    
    private Logger mLogger;

    public CircleMenu(final Context _context) {
        super(_context);
        init(null);
    }

    public CircleMenu(final Context _context, @Nullable final AttributeSet _attrs) {
        super(_context, _attrs);
        init(_attrs);
    }

    public CircleMenu(final Context _context, @Nullable final AttributeSet _attrs,
                      final int _defStyleAttr) {
        super(_context, _attrs, _defStyleAttr);
        init(_attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CircleMenu(final Context _context, @Nullable final AttributeSet _attrs,
                      final int _defStyleAttr, final int _defStyleRes) {
        super(_context, _attrs, _defStyleAttr, _defStyleRes);
        init(_attrs);
    }

    private void init(final AttributeSet _set) {
        mLogger = LogManager.getLogger();

        mOval = new RectF();
        mOval.set(PADDING, PADDING, 2 * RADIUS, 2 * RADIUS);

        mStrokePaintPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mStrokePaintPaint.setColor(Color.RED);
        mStrokePaintPaint.setStyle(Paint.Style.STROKE);
        mStrokePaintPaint.setStrokeWidth(5);

        mCenterStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterStrokePaint.setColor(Color.WHITE);
        mCenterStrokePaint.setStyle(Paint.Style.STROKE);
        mCenterStrokePaint.setStrokeWidth(5);

        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(Color.BLUE);

        mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterPaint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(final Canvas _canvas) {
        _canvas.drawCircle(RADIUS + 0.5f * PADDING, RADIUS + 0.5f * PADDING,
                RADIUS - 0.5f * PADDING, mBackgroundPaint);

        float start;
        float angle = 360 / COUNT_OF_SECTORS;
        for (int i = 0; i < COUNT_OF_SECTORS; i++) {
            start = i * angle;
            _canvas.drawArc(mOval, start, angle, true, mStrokePaintPaint);
        }

        _canvas.drawCircle(RADIUS + 0.5f * PADDING, RADIUS + 0.5f * PADDING,
                RADIUS - 0.5f * PADDING, mStrokePaintPaint);

        _canvas.drawCircle(RADIUS + 0.5f * PADDING, RADIUS + 0.5f * PADDING,
                RADIUS / 6, mCenterPaint);
        _canvas.drawCircle(RADIUS + 0.5f * PADDING, RADIUS + 0.5f * PADDING,
                RADIUS / 6, mCenterStrokePaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = 2 * RADIUS + PADDING;
        int width = resolveSizeAndState(size, widthMeasureSpec, 0);
        int height = resolveSizeAndState(size, heightMeasureSpec, 0);

        setMeasuredDimension(width, height);
    }
}
