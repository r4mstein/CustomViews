package com.example.r4mst.customviews.views;

import android.content.Context;
import android.content.res.TypedArray;
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

    private int mColorMain;
    private int mColorSecondary;
    private int mSquareSize;
    private int mWidthBetweenRect;

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

        mSecondaryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSecondaryPaint.setAlpha(80);

        if (_set == null) return;

        TypedArray typedArray = getContext().obtainStyledAttributes(_set, R.styleable.ProgressView);

        mColorMain = typedArray.getColor(R.styleable.ProgressView_color_main,
                getResources().getColor(R.color.colorYellowMain));
        mColorSecondary = typedArray.getColor(R.styleable.ProgressView_color_secondary,
                getResources().getColor(R.color.colorYellowSecondary));

        mSquareSize = typedArray.getDimensionPixelSize(R.styleable.ProgressView_square_size,
                SQUARE_SIZE);
        mWidthBetweenRect = typedArray.getDimensionPixelSize(R.styleable.ProgressView_width_between_rect,
                WIDTH_BETWEEN_RECT);

        mMainPaint.setColor(mColorMain);
        mSecondaryPaint.setColor(mColorSecondary);

        typedArray.recycle();

    }

    @Override
    protected void onDraw(final Canvas _canvas) {

        mRectTopLeft.left = mSquareSize;
        mRectTopLeft.top = mSquareSize;
        mRectTopLeft.right = mRectTopLeft.left + mSquareSize;
        mRectTopLeft.bottom = mRectTopLeft.top + mSquareSize;

        mRectTopRight.left = mRectTopLeft.right + mWidthBetweenRect;
        mRectTopRight.top = mSquareSize;
        mRectTopRight.right = mRectTopRight.left + mSquareSize;
        mRectTopRight.bottom = mRectTopRight.top + mSquareSize;

        mRectBottomLeft.left = mSquareSize;
        mRectBottomLeft.top = mRectTopLeft.bottom + mWidthBetweenRect;
        mRectBottomLeft.right = mRectBottomLeft.left + mSquareSize;
        mRectBottomLeft.bottom = mRectBottomLeft.top + mSquareSize;

        mRectBottomRight.left = mRectBottomLeft.right + mWidthBetweenRect;
        mRectBottomRight.top = mRectTopRight.bottom + mWidthBetweenRect;
        mRectBottomRight.right = mRectBottomRight.left + mSquareSize;
        mRectBottomRight.bottom = mRectBottomRight.top + mSquareSize;

        mRectCenter.left = 3 * mSquareSize;
        mRectCenter.top = 3 * mSquareSize;
        mRectCenter.right = mRectCenter.left + (mWidthBetweenRect - 2 * mSquareSize);
        mRectCenter.bottom = mRectCenter.top + (mWidthBetweenRect - 2 * mSquareSize);

        _canvas.drawRect(mRectTopLeft, mMainPaint);
        _canvas.drawRect(mRectTopRight, mMainPaint);
        _canvas.drawRect(mRectBottomLeft, mMainPaint);
        _canvas.drawRect(mRectBottomRight, mMainPaint);
        _canvas.drawRect(mRectCenter, mSecondaryPaint);

        // Draw top line
        _canvas.drawLine(mRectTopLeft.right, (mRectTopLeft.top + 0.5f * mSquareSize),
                mRectTopRight.left, (mRectTopLeft.top + 0.5f * mSquareSize), mMainPaint);
        // Draw bottom line
        _canvas.drawLine(mRectBottomLeft.left, (mRectBottomLeft.top + 0.5f * mSquareSize),
                mRectBottomRight.left, (mRectBottomLeft.top + 0.5f * mSquareSize), mMainPaint);
        // Draw left line
        _canvas.drawLine((mRectTopLeft.left + 0.5f * mSquareSize), mRectTopLeft.bottom,
                (mRectTopLeft.left + 0.5f * mSquareSize), mRectBottomLeft.top, mMainPaint);
        // Draw right line
        _canvas.drawLine((mRectTopRight.left + 0.5f * mSquareSize), mRectTopRight.bottom,
                (mRectTopRight.left + 0.5f * mSquareSize), mRectBottomRight.top, mMainPaint);
        // Draw line from mRectTopLeft to mRectBottomRight
        _canvas.drawLine(mRectTopLeft.right, mRectTopLeft.bottom, mRectBottomRight.left,
                mRectBottomRight.top, mMainPaint);
        // Draw line from mRectTopRight to mRectBottomLeft
        _canvas.drawLine(mRectTopRight.left, mRectTopRight.bottom, mRectBottomLeft.right,
                mRectBottomLeft.top, mMainPaint);
    }
}
