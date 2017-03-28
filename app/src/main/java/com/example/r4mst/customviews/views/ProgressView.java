package com.example.r4mst.customviews.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.r4mst.customviews.R;

public final class ProgressView extends View {

    private static final int SQUARE_SIZE = 20;
    private static final int WIDTH_BETWEEN_RECT = 100;

    private Rect mRectTopLeft;
    private Rect mRectBottomLeft;
    private Rect mRectTopRight;
    private Rect mRectBottomRight;
    private Rect mRectCenter;
    private Paint mMainPaint;
    private Paint mSecondaryPaint;

    public ProgressView(final Context _context) {
        super(_context);
        init(null);
    }

    public ProgressView(final Context _context, @Nullable final AttributeSet _attrs) {
        super(_context, _attrs);
        init(_attrs);
    }

    public ProgressView(final Context _context, @Nullable final AttributeSet _attrs, final int _defStyleAttr) {
        super(_context, _attrs, _defStyleAttr);
        init(_attrs);
    }

    public ProgressView(final Context _context, @Nullable final AttributeSet _attrs,
                        final int _defStyleAttr, final int _defStyleRes) {
        super(_context, _attrs, _defStyleAttr, _defStyleRes);
        init(_attrs);
    }

    private void init(@Nullable final AttributeSet _set) {
        mRectTopLeft = new Rect();
        mRectTopRight = new Rect();
        mRectBottomLeft = new Rect();
        mRectBottomRight = new Rect();
        mRectCenter = new Rect();

        mMainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMainPaint.setColor(getResources().getColor(R.color.colorYellowMain));

        mSecondaryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSecondaryPaint.setColor(getResources().getColor(R.color.colorYellowSecondary));
        mSecondaryPaint.setAlpha(80);
    }

    @Override
    protected void onDraw(final Canvas _canvas) {
        mRectTopLeft.left = SQUARE_SIZE;
        mRectTopLeft.top = SQUARE_SIZE;
        mRectTopLeft.right = mRectTopLeft.left + SQUARE_SIZE;
        mRectTopLeft.bottom = mRectTopLeft.top + SQUARE_SIZE;

        mRectTopRight.left = mRectTopLeft.right + WIDTH_BETWEEN_RECT;
        mRectTopRight.top = SQUARE_SIZE;
        mRectTopRight.right = mRectTopRight.left + SQUARE_SIZE;
        mRectTopRight.bottom = mRectTopRight.top + SQUARE_SIZE;

        mRectBottomLeft.left = SQUARE_SIZE;
        mRectBottomLeft.top = mRectTopLeft.bottom + WIDTH_BETWEEN_RECT;
        mRectBottomLeft.right = mRectBottomLeft.left + SQUARE_SIZE;
        mRectBottomLeft.bottom = mRectBottomLeft.top + SQUARE_SIZE;

        mRectBottomRight.left = mRectBottomLeft.right + WIDTH_BETWEEN_RECT;
        mRectBottomRight.top = mRectTopRight.bottom + WIDTH_BETWEEN_RECT;
        mRectBottomRight.right = mRectBottomRight.left + SQUARE_SIZE;
        mRectBottomRight.bottom = mRectBottomRight.top + SQUARE_SIZE;

        mRectCenter.left = 3 * SQUARE_SIZE;
        mRectCenter.top = 3 * SQUARE_SIZE;
        mRectCenter.right = mRectCenter.left + (WIDTH_BETWEEN_RECT - 2 * SQUARE_SIZE);
        mRectCenter.bottom = mRectCenter.top + (WIDTH_BETWEEN_RECT - 2 * SQUARE_SIZE);

        _canvas.drawRect(mRectTopLeft, mMainPaint);
        _canvas.drawRect(mRectTopRight, mMainPaint);
        _canvas.drawRect(mRectBottomLeft, mMainPaint);
        _canvas.drawRect(mRectBottomRight, mMainPaint);
        _canvas.drawRect(mRectCenter, mSecondaryPaint);

        // Draw top line
        _canvas.drawLine(mRectTopLeft.right, (mRectTopLeft.top + 0.5f * SQUARE_SIZE),
                mRectTopRight.left, (mRectTopLeft.top + 0.5f * SQUARE_SIZE), mMainPaint);
        // Draw bottom line
        _canvas.drawLine(mRectBottomLeft.left, (mRectBottomLeft.top + 0.5f * SQUARE_SIZE),
                mRectBottomRight.left, (mRectBottomLeft.top + 0.5f * SQUARE_SIZE), mMainPaint);
        // Draw left line
        _canvas.drawLine((mRectTopLeft.left + 0.5f * SQUARE_SIZE), mRectTopLeft.bottom,
                (mRectTopLeft.left + 0.5f * SQUARE_SIZE), mRectBottomLeft.top, mMainPaint);
        // Draw right line
        _canvas.drawLine((mRectTopRight.left + 0.5f * SQUARE_SIZE), mRectTopRight.bottom,
                (mRectTopRight.left + 0.5f * SQUARE_SIZE), mRectBottomRight.top, mMainPaint);
        // Draw line from mRectTopLeft to mRectBottomRight
        _canvas.drawLine(mRectTopLeft.right, mRectTopLeft.bottom, mRectBottomRight.left,
                mRectBottomRight.top, mMainPaint);
        // Draw line from mRectTopRight to mRectBottomLeft
        _canvas.drawLine(mRectTopRight.left, mRectTopRight.bottom, mRectBottomLeft.right,
                mRectBottomLeft.top, mMainPaint);
    }
}
