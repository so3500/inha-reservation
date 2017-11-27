/*
Copyright 2014 Stephan Tittel and Yahoo Inc.


Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

/**
 * Widget that lets users select a minimum and maximum value on a given numerical range.
 * The range value types can be one of Long, Double, Integer, Float, Short, Byte or BigDecimal.<br>
 * <br>
 * Improved {@link android.view.MotionEvent} handling for smoother use, anti-aliased painting for improved aesthetics.
 *
 * @param <T> The Number type of the range values. One of Long, Double, Integer, Float, Short, Byte or BigDecimal.
 * @author Stephan Tittel (stephan.tittel@kom.tu-darmstadt.de)
 * @author Peter Sinnott (psinnott@gmail.com)
 * @author Thomas Barrasso (tbarrasso@sevenplusandroid.org)
 * @author Alex Florescu (florescu@yahoo-inc.com)
 * @author Michael Keppler (bananeweizen@gmx.de)
 */

package com.landvibe.kian82.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import com.example.inhareservation.R;
import java.math.BigDecimal;


public class RangeSeekBar<T extends Number> extends ImageView {

    public static final Integer DEFAULT_MINIMUM = 0;
    public static final Integer DEFAULT_MAXIMUM = 200;
    public static final Integer DEFAULT_MINIMUM2 = 8;
    public static final Integer DEFAULT_MAXIMUM2 = 91;
    public static final int HEIGHT_IN_DP = 30;
    public static final int TEXT_LATERAL_PADDING_IN_DP = 3;
    private static final int INITIAL_PADDING_IN_DP = 0;
    private final int LINE_HEIGHT_IN_DP =8;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paint3 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Bitmap thumbImage = BitmapFactory.decodeResource(getResources(), R.drawable.cal_left_arrow_off);
    private final Bitmap thumbImage2 = BitmapFactory.decodeResource(getResources(), R.drawable.cal_left_arrow_off);
    private final Bitmap thumbPressedImage = BitmapFactory.decodeResource(getResources(),
                                                                          R.drawable.seek_thumb_pressed);
    private final Bitmap thumbDisabledImage = BitmapFactory.decodeResource(getResources(),
                                                                           R.drawable.seek_thumb_disabled);
    private final float thumbWidth = thumbImage.getWidth();
    private final float thumbHalfWidth = 0.5f * thumbWidth;
    private final float thumbHalfHeight = 0.5f * thumbImage.getHeight();
    private float INITIAL_PADDING;
    private float padding;
    private T absoluteMinValue, absoluteMaxValue;
    private NumberType numberType;
    private double absoluteMinValuePrim, absoluteMaxValuePrim;
    private double normalizedMinValue = 0d;
    private double normalizedMaxValue = 1d;
    private Thumb pressedThumb = null;
    private boolean notifyWhileDragging = false;
    private OnRangeSeekBarChangeListener<T> listener;
    private String[] start_time_array = {"08:00","08:10","08:20","08:30","08:40","08:50", "09:00","09:10","09:20","09","09:40","09:50", "10:00","10:10","10:20","10:30","10:40","10:50", "11:00","11:10","11:20","11:30","11:40","11:50", "12:00","12:10","12:20","12:30","12:40","12:50", "13:00","13:10","13:20","13:30","13:40","13:50", "14:00","14:10","14:20","14:30","14:40","14:50", "15:00","15:10","15:20","15:30","15:40","15:50", "16:00","16:10","16:20","16:30","16:40","16:50", "17:00","17:10","17:20","17:30","17:40","17:50", "18:00","18:10","18:20","18:30","18:40","18:50", "19:00","19:10","19:20","19:30","19:40","19:50", "20:00","20:10","20:20","20:30","20:40","20:50", "21:00","21:10","21:20","21:30","21:40","21:50"};
	private String[] end_time_array = {"08:09","08:19","08:29","08:39","08:49","08:59", "09:09","09:19","09:29","09:39","09:49","09:59", "10:09","10:19","10:29","10:39","10:49","10:59", "11:09","11:19","11:29","11:39","11:49","11:59", "12:09","12:19","12:29","12:39","12:49","12:59", "13:09","13:19","13:29","13:39","13:49","13:59", "14:09","14:19","14:29","14:39","14:49","14:59", "15:00","15:19","15:29","15:39","15:49","15:59", "16:09","16:19","16:29","16:39","16:49","16:59", "17:09","17:19","17:29","17:39","17:49","17:59", "18:09","18:19","18:29","18:39","18:49","18:59", "19:09","19:19","19:29","19:39","19:49","19:59", "20:09","20:19","20:29","20:39","20:49","20:59", "21:09","21:19","21:29","21:39","21:49","21:59"};

    /**
     * Default color of a {@link RangeSeekBar}, #FF33B5E5. This is also known as "Ice Cream Sandwich" blue.
     */
    public static final int DEFAULT_COLOR = Color.argb(0xFF, 0x33, 0xB5, 0xE5);
    /**
     * An invalid pointer id.
     */
    public static final int INVALID_POINTER_ID = 255;

    // Localized constants from MotionEvent for compatibility
    // with API < 8 "Froyo".
    public static final int ACTION_POINTER_UP = 0x6, ACTION_POINTER_INDEX_MASK = 0x0000ff00, ACTION_POINTER_INDEX_SHIFT = 8;

    private float mDownMotionX;

    private int mActivePointerId = INVALID_POINTER_ID;

    private int mScaledTouchSlop;
   private boolean isFixed;
    private boolean mIsDragging;

    private int mTextOffset;
    private int mTextSize;
    private int mDistanceToTop;
    private RectF mRect;

    private static final int DEFAULT_TEXT_SIZE_IN_DP = 14;
    private static final int DEFAULT_TEXT_DISTANCE_TO_BUTTON_IN_DP = 8;
    private static final int DEFAULT_TEXT_DISTANCE_TO_TOP_IN_DP = 8;
    private boolean mSingleThumb;

    public RangeSeekBar(Context context) {
        super(context);
        init(context, null);
    }
    
    public RangeSeekBar(Context context , Boolean isFixed)  {
        super(context);
        this.isFixed = isFixed;
        init(context, null);
    }

    public RangeSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RangeSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private T extractNumericValueFromAttributes(TypedArray a, int attribute, int defaultValue) {
        TypedValue tv = a.peekValue(attribute);
        if (tv == null) {
            return (T) Integer.valueOf(defaultValue);
        }

        int type = tv.type;
        if (type == TypedValue.TYPE_FLOAT) {
            return (T) Float.valueOf(a.getFloat(attribute, defaultValue));
        } else {
            return (T) Integer.valueOf(a.getInteger(attribute, defaultValue));
        }
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs == null) {
            setRangeToDefaultValues();
        } else {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RangeSeekBar, 0, 0);
            setRangeValues(
                    extractNumericValueFromAttributes(a, R.styleable.RangeSeekBar_absoluteMinValue, DEFAULT_MINIMUM2),
                    extractNumericValueFromAttributes(a, R.styleable.RangeSeekBar_absoluteMaxValue, DEFAULT_MAXIMUM2));
            mSingleThumb = a.getBoolean(R.styleable.RangeSeekBar_singleThumb, false);
            a.recycle();
        }

        setValuePrimAndNumberType();

        INITIAL_PADDING = PixelUtil.dpToPx(context, INITIAL_PADDING_IN_DP);

        mTextSize = PixelUtil.dpToPx(context, DEFAULT_TEXT_SIZE_IN_DP);
        mDistanceToTop = PixelUtil.dpToPx(context, DEFAULT_TEXT_DISTANCE_TO_TOP_IN_DP);
        mTextOffset = this.mTextSize + PixelUtil.dpToPx(context,
                                                        DEFAULT_TEXT_DISTANCE_TO_BUTTON_IN_DP) + this.mDistanceToTop;

        float lineHeight = PixelUtil.dpToPx(context, LINE_HEIGHT_IN_DP);
        mRect = new RectF(INITIAL_PADDING,
                           lineHeight+mTextOffset/3 ,
                          getWidth() - INITIAL_PADDING,
                          mTextOffset + lineHeight / 2);

        // make RangeSeekBar focusable. This solves focus handling issues in case EditText widgets are being used along with the RangeSeekBar within ScollViews.
        setFocusable(true);
        setFocusableInTouchMode(true);
        mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public void setRangeValues(T minValue, T maxValue) {
        this.absoluteMinValue = minValue;
        this.absoluteMaxValue = maxValue;
        setValuePrimAndNumberType();
    }
    
    

    public boolean isFixed() {
		return isFixed;
	}

	public void setFixed(boolean isFixed) {
		this.isFixed = isFixed;
	}

	@SuppressWarnings("unchecked")
    // only used to set default values when initialised from XML without any values specified
    private void setRangeToDefaultValues() {
        this.absoluteMinValue = (T) DEFAULT_MINIMUM;
        this.absoluteMaxValue = (T) DEFAULT_MAXIMUM;
        setValuePrimAndNumberType();
    }

    private void setValuePrimAndNumberType() {
        absoluteMinValuePrim = absoluteMinValue.doubleValue();
        absoluteMaxValuePrim = absoluteMaxValue.doubleValue();
        numberType = NumberType.fromNumber(absoluteMinValue);
    }

    public void resetSelectedValues() {
        setSelectedMinValue(absoluteMinValue);
        setSelectedMaxValue(absoluteMaxValue);
    }

    public boolean isNotifyWhileDragging() {
        return notifyWhileDragging;
    }

    /**
     * Should the widget notify the listener callback while the user is still dragging a thumb? Default is false.
     *
     * @param flag
     */
    public void setNotifyWhileDragging(boolean flag) {
        this.notifyWhileDragging = flag;
    }

    /**
     * Returns the absolute minimum value of the range that has been set at construction time.
     *
     * @return The absolute minimum value of the range.
     */
    public T getAbsoluteMinValue() {
        return absoluteMinValue;
    }

    /**
     * Returns the absolute maximum value of the range that has been set at construction time.
     *
     * @return The absolute maximum value of the range.
     */
    public T getAbsoluteMaxValue() {
        return absoluteMaxValue;
    }

    /**
     * Returns the currently selected min value.
     *
     * @return The currently selected min value.
     */
    public T getSelectedMinValue() {
        return normalizedToValue(normalizedMinValue);
    }

    /**
     * Sets the currently selected minimum value. The widget will be invalidated and redrawn.
     *
     * @param value The Number value to set the minimum value to. Will be clamped to given absolute minimum/maximum range.
     */
    public void setSelectedMinValue(T value) {
        // in case absoluteMinValue == absoluteMaxValue, avoid division by zero when normalizing.
        if (0 == (absoluteMaxValuePrim - absoluteMinValuePrim)) {
            setNormalizedMinValue(0d);
        } else {
            setNormalizedMinValue(valueToNormalized(value));
        }
    }

    /**
     * Returns the currently selected max value.
     *
     * @return The currently selected max value.
     */
    public T getSelectedMaxValue() {
        return normalizedToValue(normalizedMaxValue);
    }

    /**
     * Sets the currently selected maximum value. The widget will be invalidated and redrawn.
     *
     * @param value The Number value to set the maximum value to. Will be clamped to given absolute minimum/maximum range.
     */
    public void setSelectedMaxValue(T value) {
        // in case absoluteMinValue == absoluteMaxValue, avoid division by zero when normalizing.
        if (0 == (absoluteMaxValuePrim - absoluteMinValuePrim)) {
            setNormalizedMaxValue(1d);
        } else {
            setNormalizedMaxValue(valueToNormalized(value));
        }
    }

    /**
     * Registers given listener callback to notify about changed selected values.
     *
     * @param listener The listener to notify about changed selected values.
     */
    public void setOnRangeSeekBarChangeListener(OnRangeSeekBarChangeListener<T> listener) {
        this.listener = listener;
    }

    /**
     * Handles thumb selection and movement. Notifies listener callback on certain events.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

    	if(isFixed()==true)
    	{
        if (!isEnabled()) {
            return false;
        }

        int pointerIndex;

        final int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                // Remember where the motion event started
                mActivePointerId = event.getPointerId(event.getPointerCount() - 1);
                pointerIndex = event.findPointerIndex(mActivePointerId);
                mDownMotionX = event.getX(pointerIndex);

                pressedThumb = evalPressedThumb(mDownMotionX);

                // Only handle thumb presses.
                if (pressedThumb == null) {
                    return super.onTouchEvent(event);
                }

                setPressed(true);
                invalidate();
                onStartTrackingTouch();
                trackTouchEvent(event);
                attemptClaimDrag();

                break;
            case MotionEvent.ACTION_MOVE:
                if (pressedThumb != null) {

                    if (mIsDragging) {
                        trackTouchEvent(event);
                    } else {
                        // Scroll to follow the motion event
                        pointerIndex = event.findPointerIndex(mActivePointerId);
                        final float x = event.getX(pointerIndex);

                        if (Math.abs(x - mDownMotionX) > mScaledTouchSlop) {
                            setPressed(true);
                            invalidate();
                            onStartTrackingTouch();
                            trackTouchEvent(event);
                            attemptClaimDrag();
                        }
                    }

                    if (notifyWhileDragging && listener != null) {
                        listener.onRangeSeekBarValuesChanged(this, getSelectedMinValue(), getSelectedMaxValue());
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mIsDragging) {
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                    setPressed(false);
                } else {
                    // Touch up when we never crossed the touch slop threshold
                    // should be interpreted as a tap-seek to that location.
                    onStartTrackingTouch();
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                }

                pressedThumb = null;
                invalidate();
                if (listener != null) {
                    listener.onRangeSeekBarValuesChanged(this, getSelectedMinValue(), getSelectedMaxValue());
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN: {
                final int index = event.getPointerCount() - 1;
                // final int index = ev.getActionIndex();
                mDownMotionX = event.getX(index);
                mActivePointerId = event.getPointerId(index);
                invalidate();
                break;
            }
            case MotionEvent.ACTION_POINTER_UP:
                onSecondaryPointerUp(event);
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                if (mIsDragging) {
                    onStopTrackingTouch();
                    setPressed(false);
                }
                invalidate(); // see above explanation
                break;
        }
    	}
        return true;
    }

    private final void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = (ev.getAction() & ACTION_POINTER_INDEX_MASK) >> ACTION_POINTER_INDEX_SHIFT;

        final int pointerId = ev.getPointerId(pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose
            // a new active pointer and adjust accordingly.
            // TODO: Make this decision more intelligent.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mDownMotionX = ev.getX(newPointerIndex);
            mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }

    private final void trackTouchEvent(MotionEvent event) {
        final int pointerIndex = event.findPointerIndex(mActivePointerId);
        final float x = event.getX(pointerIndex);

        if (Thumb.MIN.equals(pressedThumb) && !mSingleThumb) {
            setNormalizedMinValue(screenToNormalized(x));
        } else if (Thumb.MAX.equals(pressedThumb)) {
            setNormalizedMaxValue(screenToNormalized(x));
        }
    }

    /**
     * Tries to claim the user's drag motion, and requests disallowing any ancestors from stealing events in the drag.
     */
    private void attemptClaimDrag() {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
    }

    /**
     * This is called when the user has started touching this widget.
     */
    void onStartTrackingTouch() {
        mIsDragging = true;
    }

    /**
     * This is called when the user either releases his touch or the touch is canceled.
     */
    void onStopTrackingTouch() {
        mIsDragging = false;
    }

    /**
     * Ensures correct size of the widget.
     */
    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 200;
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec)) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        }

        int height = thumbImage.getHeight() + PixelUtil.dpToPx(getContext(), HEIGHT_IN_DP);
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(heightMeasureSpec)) {
            height = Math.min(height, MeasureSpec.getSize(heightMeasureSpec));
        }
        setMeasuredDimension(width, height);
    }

    /**
     * Draws the widget on the given canvas.
     */
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setTextSize(mTextSize);
        paint.setStyle(Style.FILL);
        paint.setColor(Color.TRANSPARENT);
        paint.setAntiAlias(true);
        paint2.setTextSize(18);
        paint2.setStyle(Style.FILL);
        paint2.setColor(Color.TRANSPARENT);
        paint.setAntiAlias(true);
       

        if(isFixed == true)
        {
        // draw min and max labels
        String minLabel = getContext().getString(R.string.demo_min_label);
        String maxLabel = getContext().getString(R.string.demo_max_label);
        float minMaxLabelSize = Math.max(paint.measureText(minLabel), paint.measureText(maxLabel));
      // float minMaxHeight = mTextOffset + thumbHalfHeight + mTextSize / 3;
        float minMaxHeight = mTextOffset + thumbHalfHeight ;
        canvas.drawText(minLabel, 0, minMaxHeight, paint);
       canvas.drawText(maxLabel, getWidth() - minMaxLabelSize, minMaxHeight, paint);
       padding = INITIAL_PADDING;

     //    draw seek bar background line
        mRect.left = padding;
        mRect.right = getWidth() - padding;
 
        }
       // canvas.drawRect(mRect, paint);

        boolean selectedValuesAreDefault = (getSelectedMinValue().equals(getAbsoluteMinValue()) &&
                getSelectedMaxValue().equals(getAbsoluteMaxValue()));

        int colorToUseForButtonsAndHighlightedLine = selectedValuesAreDefault ?
                Color.GRAY :    // default values
                DEFAULT_COLOR; //non default, filter is active
        
        int colorToUseForButtonsAndHighlightedLine2 = selectedValuesAreDefault ?
                Color.WHITE :    // default values
                DEFAULT_COLOR; //non default, filter is active
        float lineHeight = PixelUtil.dpToPx(getContext(), LINE_HEIGHT_IN_DP);
        // draw seek bar active range line
        mRect.left = normalizedToScreen(normalizedMinValue);
        mRect.right = normalizedToScreen(normalizedMaxValue);
        if(isFixed()==true)
        	paint2.setColor(Color.RED);
        else
        	 paint2.setColor( Color.argb(0x8F, 0xFF, 0xFF, 0xFF));

        paint3.setColor( Color.argb(0xFF, 0x2c, 0x3e, 0x50));
        paint3.setStrokeWidth(2);
        canvas.drawRect(mRect, paint2);
        if(absoluteMaxValuePrim != 9)
        canvas.drawLine(normalizedToScreen(normalizedMaxValue),  lineHeight+mTextOffset/3,normalizedToScreen(normalizedMaxValue),  mTextOffset + lineHeight / 2, paint3);
        final float min =  (float) (padding + 0.91725 * (getWidth() - 2 * padding));

        paint2.setColor( Color.WHITE);
       
       
        canvas.drawLine(min,  mTextOffset + lineHeight / 2,getWidth() -min,  mTextOffset + lineHeight / 2, paint2);

        float line_postion_x=min;
        float scale_postion_x= min-9;
        int time_value = 22;
       for (int i = 0; i <15; i++)
       {
    	   canvas.drawLine(line_postion_x,  mTextOffset + lineHeight / 2,line_postion_x , mTextOffset + lineHeight / 2 +10, paint2);
    	  
          canvas.drawText(String.valueOf(time_value), scale_postion_x, mTextOffset + lineHeight / 2 +25, paint2);
    	   
    	   line_postion_x += (getWidth()-min*2)/14;
    	   scale_postion_x +=   (getWidth()-min*2)/14;
    	   if(i ==12)
    		   scale_postion_x +=3;
    	   time_value--;
       }

        
        
        
      
 if(isFixed==true)
 {
        
    //     draw minimum thumb if not a single thumb control
        if (!mSingleThumb) {
            drawThumb(normalizedToScreen(normalizedMinValue), Thumb.MIN.equals(pressedThumb), canvas,
                    selectedValuesAreDefault);
        }

        // draw maximum thumb
       drawThumb(normalizedToScreen(normalizedMaxValue), Thumb.MAX.equals(pressedThumb), canvas,
                  selectedValuesAreDefault);

        // draw the text if sliders have moved from default edges
        if (!selectedValuesAreDefault) {
            paint.setTextSize(mTextSize);
            paint.setColor(Color.WHITE);
            // give text a bit more space here so it doesn't get cut off
            int offset = PixelUtil.dpToPx(getContext(), TEXT_LATERAL_PADDING_IN_DP);
            String minText = "";
            String maxText ="";
           if((Integer)getSelectedMinValue() >0 && (Integer) getSelectedMaxValue() <84)
           {
        	   minText = start_time_array[(Integer)getSelectedMinValue()-8];
        	   maxText = end_time_array[(Integer) getSelectedMaxValue()-9];
           }
           else 
           {
        	   minText = "error";
        	   maxText ="error";
           }
           
           
            float minTextWidth = paint.measureText(minText) + offset;
            float maxTextWidth = paint.measureText(maxText) + offset;

            if (!mSingleThumb) {
                canvas.drawText(minText,
                                normalizedToScreen(normalizedMinValue) - minTextWidth * 0.5f,
                                mDistanceToTop + mTextSize,
                                paint);

            }

            canvas.drawText(maxText,
                            normalizedToScreen(normalizedMaxValue) - maxTextWidth * 0.5f,
                            mDistanceToTop + mTextSize,
                            paint);
        }
 }
    }

    /**
     * Overridden to save instance state when device orientation changes. This method is called automatically if you assign an id to the RangeSeekBar widget using the {@link #setId(int)} method. Other members of this class than the normalized min and max values don't need to be saved.
     */
    @Override
    protected Parcelable onSaveInstanceState() {
        final Bundle bundle = new Bundle();
        bundle.putParcelable("SUPER", super.onSaveInstanceState());
        bundle.putDouble("MIN", normalizedMinValue);
        bundle.putDouble("MAX", normalizedMaxValue);
        return bundle;
    }

    /**
     * Overridden to restore instance state when device orientation changes. This method is called automatically if you assign an id to the RangeSeekBar widget using the {@link #setId(int)} method.
     */
    @Override
    protected void onRestoreInstanceState(Parcelable parcel) {
        final Bundle bundle = (Bundle) parcel;
        super.onRestoreInstanceState(bundle.getParcelable("SUPER"));
        normalizedMinValue = bundle.getDouble("MIN");
        normalizedMaxValue = bundle.getDouble("MAX");
    }

    /**
     * Draws the "normal" resp. "pressed" thumb image on specified x-coordinate.
     *
     * @param screenCoord The x-coordinate in screen space where to draw the image.
     * @param pressed     Is the thumb currently in "pressed" state?
     * @param canvas      The canvas to draw upon.
     */
    private void drawThumb(float screenCoord, boolean pressed, Canvas canvas, boolean areSelectedValuesDefault) {
        Bitmap buttonToDraw;
        if (areSelectedValuesDefault) {
            buttonToDraw = thumbDisabledImage;
        } else {
            buttonToDraw = pressed ? thumbPressedImage : thumbImage;
        }

        canvas.drawBitmap(buttonToDraw, screenCoord - thumbHalfWidth,
                          mTextOffset,
                          paint);
    }

    /**
     * Decides which (if any) thumb is touched by the given x-coordinate.
     *
     * @param touchX The x-coordinate of a touch event in screen space.
     * @return The pressed thumb or null if none has been touched.
     */
    private Thumb evalPressedThumb(float touchX) {
        Thumb result = null;
        boolean minThumbPressed = isInThumbRange(touchX, normalizedMinValue);
        boolean maxThumbPressed = isInThumbRange(touchX, normalizedMaxValue);
        if (minThumbPressed && maxThumbPressed) {
            // if both thumbs are pressed (they lie on top of each other), choose the one with more room to drag. this avoids "stalling" the thumbs in a corner, not being able to drag them apart anymore.
            result = (touchX / getWidth() > 0.5f) ? Thumb.MIN : Thumb.MAX;
        } else if (minThumbPressed) {
            result = Thumb.MIN;
        } else if (maxThumbPressed) {
            result = Thumb.MAX;
        }
        return result;
    }

    /**
     * Decides if given x-coordinate in screen space needs to be interpreted as "within" the normalized thumb x-coordinate.
     *
     * @param touchX               The x-coordinate in screen space to check.
     * @param normalizedThumbValue The normalized x-coordinate of the thumb to check.
     * @return true if x-coordinate is in thumb range, false otherwise.
     */
    private boolean isInThumbRange(float touchX, double normalizedThumbValue) {
        return Math.abs(touchX - normalizedToScreen(normalizedThumbValue)) <= thumbHalfWidth;
    }

    /**
     * Sets normalized min value to value so that 0 <= value <= normalized max value <= 1. The View will get invalidated when calling this method.
     *
     * @param value The new normalized min value to set.
     */
    private void setNormalizedMinValue(double value) {
        normalizedMinValue = Math.max(0d, Math.min(1d, Math.min(value, normalizedMaxValue)));
        invalidate();
    }

    /**
     * Sets normalized max value to value so that 0 <= normalized min value <= value <= 1. The View will get invalidated when calling this method.
     *
     * @param value The new normalized max value to set.
     */
    private void setNormalizedMaxValue(double value) {
        normalizedMaxValue = Math.max(0d, Math.min(1d, Math.max(value, normalizedMinValue)));
        invalidate();
    }

    /**
     * Converts a normalized value to a Number object in the value space between absolute minimum and maximum.
     *
     * @param normalized
     * @return
     */
    @SuppressWarnings("unchecked")
    private T normalizedToValue(double normalized) {
        double v = absoluteMinValuePrim + normalized * (absoluteMaxValuePrim - absoluteMinValuePrim);
        // TODO parameterize this rounding to allow variable decimal points
        return (T) numberType.toNumber(Math.round(v * 100) / 100d);
    }

    /**
     * Converts the given Number value to a normalized double.
     *
     * @param value The Number value to normalize.
     * @return The normalized double.
     */
    private double valueToNormalized(T value) {
        if (0 == absoluteMaxValuePrim - absoluteMinValuePrim) {
            // prevent division by zero, simply return 0.
            return 0d;
        }
        return (value.doubleValue() - absoluteMinValuePrim) / (absoluteMaxValuePrim - absoluteMinValuePrim);
    }

    /**
     * Converts a normalized value into screen space.
     *
     * @param normalizedCoord The normalized value to convert.
     * @return The converted value in screen space.
     */
    private float normalizedToScreen(double normalizedCoord) {
        return (float) (padding + normalizedCoord * (getWidth() - 2 * padding));
    }

    /**
     * Converts screen space x-coordinates into normalized values.
     *
     * @param screenCoord The x-coordinate in screen space to convert.
     * @return The normalized value.
     */
    private double screenToNormalized(float screenCoord) {
        int width = getWidth();
        if (width <= 2 * padding) {
            // prevent division by zero, simply return 0.
            return 0d;
        } else {
            double result = (screenCoord - padding) / (width - 2 * padding);
            return Math.min(1d, Math.max(0d, result));
        }
    }

    /**
     * Callback listener interface to notify about changed range values.
     *
     * @param <T> The Number type the RangeSeekBar has been declared with.
     * @author Stephan Tittel (stephan.tittel@kom.tu-darmstadt.de)
     */
    public interface OnRangeSeekBarChangeListener<T> {

        public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, T minValue, T maxValue);
    }

    /**
     * Thumb constants (min and max).
     */
    private static enum Thumb {
        MIN, MAX
    }

    ;

    /**
     * Utility enumeration used to convert between Numbers and doubles.
     *
     * @author Stephan Tittel (stephan.tittel@kom.tu-darmstadt.de)
     */
    private static enum NumberType {
        LONG, DOUBLE, INTEGER, FLOAT, SHORT, BYTE, BIG_DECIMAL;

        public static <E extends Number> NumberType fromNumber(E value) throws IllegalArgumentException {
            if (value instanceof Long) {
                return LONG;
            }
            if (value instanceof Double) {
                return DOUBLE;
            }
            if (value instanceof Integer) {
                return INTEGER;
            }
            if (value instanceof Float) {
                return FLOAT;
            }
            if (value instanceof Short) {
                return SHORT;
            }
            if (value instanceof Byte) {
                return BYTE;
            }
            if (value instanceof BigDecimal) {
                return BIG_DECIMAL;
            }
         
            throw new IllegalArgumentException("Number class '" + value.getClass().getName() + "' is not supported");
        }

        public Number toNumber(double value) {
            switch (this) {
                case LONG:
                    return Long.valueOf((long) value);
                case DOUBLE:
                    return value;
                case INTEGER:
                    return Integer.valueOf((int) value);
                case FLOAT:
                    return Float.valueOf((float)value);
                case SHORT:
                    return Short.valueOf((short) value);
                case BYTE:
                    return Byte.valueOf((byte) value);
                case BIG_DECIMAL:
                    return BigDecimal.valueOf(value);
            }
            throw new InstantiationError("can't convert " + this + " to a Number object");
        }
    }

}