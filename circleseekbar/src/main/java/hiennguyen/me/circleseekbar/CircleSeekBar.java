package hiennguyen.me.circleseekbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.ContextCompat;


public class CircleSeekBar extends View {

    public static final int MIN = 0;
    public static final int MAX = 100;

    private static final int ANGLE_OFFSET = -90;
    private static final float INVALID_VALUE = -1;
    private static final int TEXT_SIZE_DEFAULT = 72;

    /**
     * Current point value.
     */
    private int mProgressDisplay = MIN;
    /**
     * The min value of progress value.
     */
    private int mMin = MIN;

    /**
     * The maximum value that {@link CircleSeekBar } can be set.
     */
    private int mMax = MAX;

    /**
     * The increment/decrement value for each movement of progress.
     */
    private int mStep = 1;

    private int mArcWidth = 8;
    private int mProgressWidth = 12;

    //
    // internal variables
    //
    /**
     * The counts of point update to determine whether to change previous progress.
     */
    private int mUpdateTimes = 0;
    private float mPreviousProgress = -1;
    private float mCurrentProgress = 0;

    /**
     * Determine whether reach max of point.
     */
    private boolean isMax = false;

    /**
     * Determine whether reach min of point.
     */
    private boolean isMin = false;

    // For Arc
    private RectF mArcRect = new RectF();
    private Paint mArcPaint;

    // For Progress
    private Paint mProgressPaint;
    private float mProgressSweep;
    private int mProgressColor = 0;

    //For Text progress
    private Paint mTextPaint;
    private int mTextSize = TEXT_SIZE_DEFAULT;
    private Rect mTextRect = new Rect();
    private boolean mIsShowText = true;

    private int mCenterX;
    private int mCenterY;
    private int mCircleRadius;

    /**
     * The drawable for circle indicator of Seekbar
     */
    Drawable mThumbDrawable;

    // Coordinator (X, Y) of Indicator icon
    private int mThumbX;
    private int mThumbY;
    private int mThumbSize;

    private int mPadding;
    private double mAngle;
    private boolean mIsThumbSelected = false;

    private OnSeekBarChangedListener mOnSeekBarChangeListener;


    public CircleSeekBar(Context context) {
        super(context);
        init(context, null);
    }


    public CircleSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void setStep(int mStep) {
        this.mStep = mStep;
    }

    public void setThumbDrawable(Drawable mIndicatorIcon) {
        this.mThumbDrawable = mIndicatorIcon;
    }

    public void setProgressColor(int colorRes) {
        this.mProgressColor = colorRes;
        mProgressPaint.setColor(colorRes);
    }

    public void setArcWidth(int mArcWidth) {
        this.mArcWidth = mArcWidth;
    }

    public void setProgressWidth(int mProgressWidth) {
        this.mProgressWidth = mProgressWidth;
    }

    public void setTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
    }

    public void setIsShowText(boolean mIsShowText) {
        this.mIsShowText = mIsShowText;
    }

    public void setProgressDisplay(int progressDisplay) {
        mProgressDisplay = progressDisplay;
        mProgressDisplay = (mProgressDisplay > mMax) ? mMax : mProgressDisplay;
        mProgressDisplay = (mProgressDisplay < mMin) ? mMin : mProgressDisplay;
        mProgressSweep = (float) mProgressDisplay / valuePerDegree();
        mAngle = Math.PI / 2 - (mProgressSweep * Math.PI) / 180;
    }

    public void setProgressDisplayAndInvalidate(int progressDisplay) {
        setProgressDisplay(progressDisplay);
        if(mOnSeekBarChangeListener != null) {
            mOnSeekBarChangeListener.onPointsChanged(this, mProgressDisplay, false);
        }
        invalidate();
    }

    public int getProgressDisplay() {
        return mProgressDisplay;
    }

    public int getMin() {
        return mMin;
    }

    public int getMax() {
        return mMax;
    }

    public int getStep() {
        return mStep;
    }

    public float getCurrentProgress() {
        return mCurrentProgress;
    }

    public double getAngle() {
        return mAngle;
    }

    private void init(Context context, AttributeSet attrs) {

        final float density = context.getResources().getDisplayMetrics().density;
        int progressColor = ContextCompat.getColor(context, R.color.color_progress);
        int arcColor = ContextCompat.getColor(context, mProgressColor == 0 ? R.color.color_arc : mProgressColor);
        int textColor = ContextCompat.getColor(context, R.color.color_text);
        mProgressWidth = (int) (density * mProgressWidth);
        mArcWidth = (int) (density * mArcWidth);
        mTextSize = (int) (density * mTextSize);

        mThumbDrawable = ContextCompat.getDrawable(context, R.drawable.ic_circle_seekbar);
        if (attrs != null) {
            final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleSeekBar, 0, 0);
            Drawable indicator = typedArray.getDrawable(R.styleable.CircleSeekBar_csb_thumbDrawable);
            if (indicator != null) mThumbDrawable = indicator;

            mProgressDisplay = typedArray.getInteger(R.styleable.CircleSeekBar_csb_progress, mProgressDisplay);
            mThumbSize = typedArray.getDimensionPixelSize(R.styleable.CircleSeekBar_csb_thumbSize, 50);

            mMin = typedArray.getInteger(R.styleable.CircleSeekBar_csb_min, mMin);
            mMax = typedArray.getInteger(R.styleable.CircleSeekBar_csb_max, mMax);
            mStep = typedArray.getInteger(R.styleable.CircleSeekBar_csb_step, mStep);


            mTextSize = (int) typedArray.getDimension(R.styleable.CircleSeekBar_csb_textSize, mTextSize);
            textColor = typedArray.getColor(R.styleable.CircleSeekBar_csb_textColor, textColor);
            mIsShowText = typedArray.getBoolean(R.styleable.CircleSeekBar_csb_isShowText, mIsShowText);

            mProgressWidth = (int) typedArray.getDimension(R.styleable.CircleSeekBar_csb_progressWidth, mProgressWidth);
            progressColor = typedArray.getColor(R.styleable.CircleSeekBar_csb_progressColor, progressColor);

            mArcWidth = (int) typedArray.getDimension(R.styleable.CircleSeekBar_csb_arcWidth, mArcWidth);
            arcColor = typedArray.getColor(R.styleable.CircleSeekBar_csb_arcColor, arcColor);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                int all = getPaddingLeft() + getPaddingRight() + getPaddingBottom() + getPaddingTop() + getPaddingEnd() + getPaddingStart();
                mPadding = all / 6;
            } else {
                mPadding = (getPaddingLeft() + getPaddingRight() + getPaddingBottom() + getPaddingTop()) / 4;
            }
            typedArray.recycle();
        }

        // range check
        mProgressDisplay = (mProgressDisplay > mMax) ? mMax : mProgressDisplay;
        mProgressDisplay = (mProgressDisplay < mMin) ? mMin : mProgressDisplay;

        mProgressSweep = (float) mProgressDisplay / valuePerDegree();
        mAngle = Math.PI / 2 - (mProgressSweep * Math.PI) / 180;
        mCurrentProgress = Math.round(mProgressSweep * valuePerDegree());

        mArcPaint = new Paint();
        mArcPaint.setColor(arcColor);
        mArcPaint.setAntiAlias(true);
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(mArcWidth);

        mProgressPaint = new Paint();
        mProgressPaint.setColor(progressColor);
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeWidth(mProgressWidth);

        mTextPaint = new Paint();
        mTextPaint.setColor(textColor);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(mTextSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        final int min = Math.min(w, h);

        // find circle's rectangle points
        int alignLeft = (w - min) / 2;
        int alignTop = (h - min) / 2;
        int alignRight = alignLeft + min;
        int alignBottom = alignTop + min;

        // save circle coordinates
        mCenterX = alignRight / 2 + (w - alignRight) / 2;
        mCenterY = alignBottom / 2 + (h - alignBottom) / 2;


        float progressDiameter = min - mPadding;
        mCircleRadius = (int) (progressDiameter / 2);
        float top = h / 2 - (progressDiameter / 2);
        float left = w / 2 - (progressDiameter / 2);
        mArcRect.set(left, top, left + progressDiameter, top + progressDiameter);

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if(mIsShowText) {
            // draw the text
            String textPoint = String.valueOf(mProgressDisplay);
            mTextPaint.getTextBounds(textPoint, 0, textPoint.length(), mTextRect);
            // center the text
            int xPos = canvas.getWidth() / 2 - mTextRect.width() / 2;
            int yPos = (int) ((mArcRect.centerY()) - ((mTextPaint.descent() + mTextPaint.ascent()) / 2));
            canvas.drawText(String.valueOf(mProgressDisplay), xPos, yPos, mTextPaint);
        }

        // draw the arc and progress
        canvas.drawCircle(mCenterX, mCenterY, mCircleRadius, mArcPaint);
        canvas.drawArc(mArcRect, ANGLE_OFFSET, mProgressSweep, false, mProgressPaint);

        // find thumb position
        mThumbX = (int) (mCenterX + mCircleRadius * Math.cos(mAngle));
        mThumbY = (int) (mCenterY - mCircleRadius * Math.sin(mAngle));

        mThumbDrawable.setBounds(mThumbX - mThumbSize / 2, mThumbY - mThumbSize / 2,
                mThumbX + mThumbSize / 2, mThumbY + mThumbSize / 2);
        mThumbDrawable.draw(canvas);
    }

    private float valuePerDegree() {
        return mMax / 360.0f;
    }

    /**
     * Invoked when slider starts moving or is currently moving. This method calculates and sets position and angle of the thumb.
     *
     * @param touchX Where is the touch identifier now on X axis
     * @param touchY Where is the touch identifier now on Y axis
     */
    private void updateProgressState(int touchX, int touchY) {
        int distanceX = touchX - mCenterX;
        int distanceY = mCenterY - touchY;
        //noinspection SuspiciousNameCombination
        double c = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
        mAngle = Math.acos(distanceX / c);
        if (distanceY < 0) {
            mAngle = -mAngle;
        }
        mProgressSweep = (float) (90 - (mAngle * 180) / Math.PI);
        if (mProgressSweep < 0) mProgressSweep += 360;
        int progress = Math.round(mProgressSweep * valuePerDegree());
        updateProgress(progress, true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                // start moving the thumb (this is the first touch)
                int x = (int) event.getX();
                int y = (int) event.getY();
                if (x < mThumbX + mThumbSize && x > mThumbX - mThumbSize && y < mThumbY + mThumbSize
                        && y > mThumbY - mThumbSize) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    mIsThumbSelected = true;
                    updateProgressState(x, y);
                    if(mOnSeekBarChangeListener != null) {
                        mOnSeekBarChangeListener.onStartTrackingTouch(this);
                    }
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // still moving the thumb (this is not the first touch)
                if (mIsThumbSelected) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    updateProgressState(x, y);
                }
                break;
            }

            case MotionEvent.ACTION_UP: {
                // finished moving (this is the last touch)
                getParent().requestDisallowInterceptTouchEvent(false);
                mIsThumbSelected = false;
                if(mOnSeekBarChangeListener != null)
                    mOnSeekBarChangeListener.onStopTrackingTouch(this);
                break;
            }
        }

        // redraw the whole component
        return true;
    }

    private void updateProgress(int progress, boolean fromUser) {

        // detect points change closed to max or min
        final int maxDetectValue = (int) ((double) mMax * 0.99);
        final int minDetectValue = (int) ((double) mMax * 0.005) + mMin;

        mUpdateTimes++;
        if (progress == INVALID_VALUE) {
            return;
        }

        // avoid accidentally touch to become max from original point
        if (progress > maxDetectValue && mPreviousProgress == INVALID_VALUE) {
            return;
        }


        // record previous and current progress change
        if (mUpdateTimes == 1) {
            mCurrentProgress = progress;
        } else {
            mPreviousProgress = mCurrentProgress;
            mCurrentProgress = progress;
        }

        mProgressDisplay = progress - (progress % mStep);

        /**
         * Determine whether reach max or min to lock point update event.
         *
         * When reaching max, the progress will drop from max (or maxDetectPoints ~ max
         * to min (or min ~ minDetectPoints) and vice versa.
         *
         * If reach max or min, stop increasing / decreasing to avoid exceeding the max / min.
         */
        if (mUpdateTimes > 1 && !isMin && !isMax) {
            if (mPreviousProgress >= maxDetectValue && mCurrentProgress <= minDetectValue &&
                    mPreviousProgress > mCurrentProgress) {
                isMax = true;
                progress = mMax;
                mProgressDisplay = mMax;
                mProgressSweep = 360;
                if (mOnSeekBarChangeListener != null) {
                    mOnSeekBarChangeListener.onPointsChanged(this, progress, fromUser);
                }
                invalidate();
            } else if ((mCurrentProgress >= maxDetectValue
                    && mPreviousProgress <= minDetectValue
                    && mCurrentProgress > mPreviousProgress) || mCurrentProgress <= mMin) {
                isMin = true;
                progress = mMin;
                mProgressDisplay = mMin;
                mProgressSweep = mMin / valuePerDegree();
                if (mOnSeekBarChangeListener != null) {
                    mOnSeekBarChangeListener.onPointsChanged(this, progress, fromUser);
                }
                invalidate();
            }
        } else {

            // Detect whether decreasing from max or increasing from min, to unlock the update event.
            // Make sure to check in detect range only.
            if (isMax & (mCurrentProgress < mPreviousProgress) && mCurrentProgress >= maxDetectValue) {
                isMax = false;
            }
            if (isMin
                    && (mPreviousProgress < mCurrentProgress)
                    && mPreviousProgress <= minDetectValue && mCurrentProgress <= minDetectValue
                    && mProgressDisplay >= mMin) {
                isMin = false;
            }
        }

        if (!isMax && !isMin) {
            progress = (progress > mMax) ? mMax : progress;
            progress = (progress < mMin) ? mMin : progress;

            if (mOnSeekBarChangeListener != null) {
                progress = progress - (progress % mStep);

                mOnSeekBarChangeListener.onPointsChanged(this, progress, fromUser);
            }
            invalidate();
        }
    }

    public void setSeekBarChangeListener(OnSeekBarChangedListener seekBarChangeListener) {
        this.mOnSeekBarChangeListener = seekBarChangeListener;
    }


    public interface OnSeekBarChangedListener {
        /**
         * Notification that the point value has changed.
         *
         * @param circleSeekBar The CircleSeekBar view whose value has changed
         * @param points        The current point value.
         * @param fromUser      True if the point change was triggered by the user.
         */
        void onPointsChanged(CircleSeekBar circleSeekBar, int points, boolean fromUser);

        void onStartTrackingTouch(CircleSeekBar circleSeekBar);

        void onStopTrackingTouch(CircleSeekBar circleSeekBar);
    }


}
