package com.bios.rangebar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class RangeBar extends View
{
    public enum State
    {
        IDLE,
        DRAG_LEFT,
        DRAG_RIGHT,
        DRAG_MIDDLE
    }

    public interface Listener
    {
        void onDrag(float pos_left, float pos_right, State mode);
    }

    private Listener listener;

    private int color_selected;
    private int color_not_selected;
    private int color_border;
    private int color_time_line;
    private int color_arrow;
    private float size_thumb;
    private float size_range_bar_height;
    private float size_range_bar_width;
    private float size_connections;
    private float radius;

    private Paint paint_time_line;
    private Paint paint_range_bar_selected;
    private Paint paint_overlay_left;
    private Paint paint_overlay_right;
    private Paint paint_thumb_left;
    private Paint paint_thumb_right;
    private Paint paint_connector_top;
    private Paint paint_connector_bottom;
    private Paint paint_arrow_left;
    private Paint paint_arrow_right;

    private MyRect rect_time_line;
    private MyRect rect_selected;
    private MyRect rect_overlay_left;
    private MyRect rect_overlay_right;
    private MyRoundRect rect_thumb_left;
    private MyRoundRect rect_thumb_right;
    private MyRect rect_connector_top;
    private MyRect rect_connector_bottom;
    private MyArrow arrow_left;
    private MyArrow arrow_right;

    private float pos_left;
    private float pos_right;
    private float last_down_pos_left;
    private float last_down_pos_right;
    private float middle_drag_pos_difference;

    private PointF last_touch_point;
    private State state = State.IDLE;

    private float last_setted_progress_left = 0.25f;
    private float last_setted_progress_right = 0.75f;

    public RangeBar(Context context)
    {
        super(context);
        initValues(context, null);
    }

    public RangeBar(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
        initValues(context, attrs);
        initObjects();
    }

    public RangeBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        initValues(context, attrs);
        initObjects();
    }

    public RangeBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        initValues(context, attrs);
        initObjects();
    }

    private void initValues(Context context, @Nullable AttributeSet attrs)
    {
        color_selected = Color.parseColor("#00000000");
        color_not_selected = Color.parseColor("#80000000");
        color_border = Color.parseColor("#ffffff");
        color_time_line = Color.parseColor("#456990");
        color_arrow = Color.parseColor("#000000");
        size_thumb = Helpers.dp2px(8f);
        radius = Helpers.dp2px(3f);
        size_range_bar_height = Helpers.dp2px(56);
        size_connections = Helpers.dp2px(2f);


        if (attrs != null)
        {
            TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RangeBar, 0, 0);

            try
            {
                color_selected = ta.getColor(R.styleable.RangeBar_color_selected, color_selected);
                color_not_selected = ta.getColor(R.styleable.RangeBar_color_not_selected, color_not_selected);
                color_border = ta.getColor(R.styleable.RangeBar_color_border, color_border);
                color_time_line = ta.getColor(R.styleable.RangeBar_color_time_line, color_time_line);
                color_arrow = ta.getColor(R.styleable.RangeBar_color_arrow, color_arrow);
                size_thumb = ta.getDimensionPixelSize(R.styleable.RangeBar_thumb_width, (int) size_thumb);
                radius = ta.getDimensionPixelSize(R.styleable.RangeBar_thumb_radius, (int) radius);
                size_connections = ta.getDimensionPixelSize(R.styleable.RangeBar_connections_size, (int) size_connections);
                size_range_bar_height = ta.getDimensionPixelSize(R.styleable.RangeBar_time_line_height, (int) size_range_bar_height);

                float progress_left = ta.getFloat(R.styleable.RangeBar_progress_left, last_setted_progress_left);
                float progress_right = ta.getFloat(R.styleable.RangeBar_progress_right, last_setted_progress_right);

                setProgressLeft(progress_left);
                setProgressRight(progress_right);

                if (radius > size_thumb)
                {
                    radius = size_thumb;
                }

                if (size_range_bar_height < Helpers.dp2px(10f))
                {
                    size_range_bar_height = Helpers.dp2px(10f);
                }
            }
            finally
            {
                ta.recycle();
            }
        }
    }

    private void initObjects()
    {
        paint_time_line = new Paint();
        paint_time_line.setColor(color_time_line);
        paint_time_line.setStyle(Paint.Style.FILL);
        paint_time_line.setAntiAlias(true);

        paint_range_bar_selected = new Paint();
        paint_range_bar_selected.setColor(color_selected);
        paint_range_bar_selected.setStyle(Paint.Style.FILL);
        paint_range_bar_selected.setAntiAlias(true);

        paint_overlay_left = new Paint();
        paint_overlay_left.setColor(color_not_selected);
        paint_overlay_left.setStyle(Paint.Style.FILL);
        paint_overlay_left.setAntiAlias(true);

        paint_overlay_right = new Paint();
        paint_overlay_right.setColor(color_not_selected);
        paint_overlay_right.setStyle(Paint.Style.FILL);
        paint_overlay_right.setAntiAlias(true);

        paint_thumb_left = new Paint();
        paint_thumb_left.setColor(color_border);
        paint_thumb_left.setStyle(Paint.Style.FILL);
        paint_thumb_left.setAntiAlias(true);

        paint_thumb_right = new Paint();
        paint_thumb_right.setColor(color_border);
        paint_thumb_right.setStyle(Paint.Style.FILL);
        paint_thumb_right.setAntiAlias(true);

        paint_connector_top = new Paint();
        paint_connector_top.setColor(color_border);
        paint_connector_top.setStyle(Paint.Style.FILL);
        paint_connector_top.setAntiAlias(true);

        paint_connector_bottom = new Paint();
        paint_connector_bottom.setColor(color_border);
        paint_connector_bottom.setStyle(Paint.Style.FILL);
        paint_connector_bottom.setAntiAlias(true);

        paint_arrow_left = new Paint();
        paint_arrow_left.setColor(color_arrow);
        paint_arrow_left.setStyle(Paint.Style.STROKE);
        paint_arrow_left.setStrokeWidth(Helpers.dp2px(2f));
        paint_arrow_left.setStrokeJoin(Paint.Join.ROUND);
        paint_arrow_left.setAntiAlias(true);

        paint_arrow_right = new Paint();
        paint_arrow_right.setColor(color_arrow);
        paint_arrow_right.setStyle(Paint.Style.STROKE);
        paint_arrow_right.setStrokeWidth(Helpers.dp2px(2f));
        paint_arrow_right.setStrokeJoin(Paint.Join.ROUND);
        paint_arrow_right.setAntiAlias(true);

        rect_time_line = new MyRect();
        rect_time_line.setPaint(paint_time_line);

        rect_selected = new MyRect();
        rect_selected.setPaint(paint_range_bar_selected);

        rect_overlay_left = new MyRect();
        rect_overlay_left.setPaint(paint_overlay_left);

        rect_overlay_right = new MyRect();
        rect_overlay_right.setPaint(paint_overlay_right);

        rect_thumb_left = new MyRoundRect();
        rect_thumb_left.setPaint(paint_thumb_left);
        rect_thumb_left.setRadius_top_left(radius);
        rect_thumb_left.setRadius_bottom_left(radius);

        rect_thumb_right = new MyRoundRect();
        rect_thumb_right.setPaint(paint_thumb_right);
        rect_thumb_right.setRadius_top_right(radius);
        rect_thumb_right.setRadius_bottom_right(radius);

        rect_connector_top = new MyRect();
        rect_connector_top.setPaint(paint_connector_top);

        rect_connector_bottom = new MyRect();
        rect_connector_bottom.setPaint(paint_connector_bottom);

        arrow_left = new MyArrow();
        arrow_left.setPaint(paint_arrow_left);

        arrow_right = new MyArrow();
        arrow_right.setPaint(paint_arrow_right);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int spec_width = MeasureSpec.getSize(widthMeasureSpec);
        int spec_height = MeasureSpec.getSize(heightMeasureSpec);

        float height = getPaddingTop() + size_range_bar_height + getPaddingBottom() + (size_connections * 2);
        int final_height = resolveSize((int) height, spec_height);
        setMeasuredDimension(spec_width, final_height);

        initSizes();
    }

    private void initSizes()
    {
        int width = getMeasuredWidth();

        size_range_bar_width = width - getPaddingLeft() - getPaddingRight() - (size_thumb * 2);

        rect_time_line.setLeft(getPaddingLeft() + size_thumb);
        rect_time_line.setTop(getPaddingTop() + size_connections);
        rect_time_line.setRight(width - getPaddingRight() - size_thumb);
        rect_time_line.setBottom(getPaddingTop() + size_connections + size_range_bar_height);

        setProgressLeft(last_setted_progress_left);
        setProgressRight(last_setted_progress_right);
//        pos_left = size_range_bar_width * pr;
//        pos_right = size_range_bar_width * 0.75f;
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if (canvas == null)
        {
            return;
        }

        drawTimeLine(canvas);
        drawOverlays(canvas);
        drawThumbs(canvas);
        drawConnections(canvas);
        drawArrows(canvas);
    }

    private void drawTimeLine(Canvas canvas)
    {
        rect_time_line.drawOnCanvas(canvas);
    }

    private void drawOverlays(Canvas canvas)
    {
        float top = getPaddingTop() + size_connections;
        float bottom = getPaddingTop() + size_connections + size_range_bar_height;

        rect_overlay_left.setLeft(getPaddingLeft() + size_thumb);
        rect_overlay_left.setTop(top);
        rect_overlay_left.setRight(getPaddingLeft() + size_thumb + pos_left);
        rect_overlay_left.setBottom(bottom);
        rect_overlay_left.drawOnCanvas(canvas);

        rect_overlay_right.setLeft(getPaddingLeft() + size_thumb + pos_right);
        rect_overlay_right.setRight(getPaddingLeft() + size_thumb + size_range_bar_width);
        rect_overlay_right.setTop(top);
        rect_overlay_right.setBottom(bottom);
        rect_overlay_right.drawOnCanvas(canvas);

        rect_selected.setLeft(rect_overlay_left.getRight());
        rect_selected.setTop(top);
        rect_selected.setRight(rect_overlay_right.getLeft());
        rect_selected.setBottom(bottom);
        rect_selected.drawOnCanvas(canvas);
    }

    private void drawThumbs(Canvas canvas)
    {
        float top = getPaddingTop();
        float bottom = getPaddingTop() + size_connections + size_range_bar_height + size_connections;

        rect_thumb_left.setTop(top);
        rect_thumb_left.setRight(rect_overlay_left.getRight());
        rect_thumb_left.setBottom(bottom);
        rect_thumb_left.setLeft(rect_overlay_left.getRight() - size_thumb);
        rect_thumb_left.drawOnCanvas(canvas);

        rect_thumb_right.setTop(top);
        rect_thumb_right.setLeft(rect_overlay_right.getLeft());
        rect_thumb_right.setBottom(bottom);
        rect_thumb_right.setRight(rect_thumb_right.getLeft() + size_thumb);
        rect_thumb_right.drawOnCanvas(canvas);
    }

    private void drawConnections(Canvas canvas)
    {
        float left = rect_overlay_left.getRight();
        float right = rect_thumb_right.getLeft();

        rect_connector_top.setTop(getPaddingTop());
        rect_connector_top.setBottom(getPaddingTop() + size_connections);
        rect_connector_top.setLeft(left);
        rect_connector_top.setRight(right);
        rect_connector_top.drawOnCanvas(canvas);

        rect_connector_bottom.setTop(getPaddingTop() + size_connections + size_range_bar_height);
        rect_connector_bottom.setBottom(rect_connector_bottom.getTop() + size_connections);
        rect_connector_bottom.setLeft(left);
        rect_connector_bottom.setRight(right);
        rect_connector_bottom.drawOnCanvas(canvas);
    }

    private void drawArrows(Canvas canvas)
    {
        float size = size_thumb / 2f;
        float middle_y = getPaddingTop() + size_connections + (size_range_bar_height / 2f);
        float offset = size / 1.6f;

        arrow_left.setBottom(new PointF(rect_thumb_left.getRight() - offset, middle_y + size));
        arrow_left.setMiddle(new PointF(rect_thumb_left.getLeft() + offset, middle_y));
        arrow_left.setTop(new PointF(rect_thumb_left.getRight() - offset, middle_y - size));
        arrow_left.drawOnCanvas(canvas);

        arrow_right.setBottom(new PointF(rect_thumb_right.getLeft() + offset, middle_y + size));
        arrow_right.setMiddle(new PointF(rect_thumb_right.getRight() - offset, middle_y));
        arrow_right.setTop(new PointF(rect_thumb_right.getLeft() + offset, middle_y - size));
        arrow_right.drawOnCanvas(canvas);
    }

    public void setProgressLeft(float progress)
    {
        if (progress < 0)
        {
            progress = 0;
        }

        if (progress > 1)
        {
            progress = 1;
        }

        if (progress > last_setted_progress_right)
        {
            progress = last_setted_progress_right;
        }

        last_setted_progress_left = progress;
        pos_left = size_range_bar_width * last_setted_progress_left;
        invalidate();
    }

    public void setProgressRight(float progress)
    {
        if (progress < 0)
        {
            progress = 0;
        }

        if (progress > 1)
        {
            progress = 1;
        }

        if (progress < last_setted_progress_left)
        {
            progress = last_setted_progress_left;
        }

        last_setted_progress_right = progress;
        pos_right = size_range_bar_width * last_setted_progress_right;
        invalidate();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int action = event.getAction();
        PointF touch_point = new PointF(event.getX(), event.getY());

        switch (action)
        {
            case MotionEvent.ACTION_DOWN:

                if (touch_point.y < getPaddingTop() || touch_point.y > getPaddingTop() + size_connections + size_range_bar_height + size_connections)
                {
                    return false;
                }

                last_touch_point = touch_point;
                if (touch_point.x <= rect_thumb_left.getRight())
                {
                    state = State.DRAG_LEFT;
                }
                else if (touch_point.x >= rect_thumb_right.getLeft())
                {
                    state = State.DRAG_RIGHT;
                }
                else if (rect_selected.isTouchInRect(touch_point))
                {
                    state = State.DRAG_MIDDLE;
                    last_down_pos_left = pos_left;
                    last_down_pos_right = pos_right;
                    middle_drag_pos_difference = pos_right - pos_left;
                }
                else
                {
                    return false;
                }

                return true;

            case MotionEvent.ACTION_MOVE:

                if (state == State.IDLE)
                {
                    return false;
                }

                if (state == State.DRAG_LEFT)
                {
                    float x_pos = touch_point.x;
                    if (x_pos > pos_right + getPaddingLeft() + size_thumb)
                    {
                        x_pos = pos_right + getPaddingLeft() + size_thumb;
                    }

                    if (x_pos < getPaddingLeft() + size_thumb)
                    {
                        x_pos = getPaddingLeft() + size_thumb;
                    }

                    pos_left = x_pos - getPaddingLeft() - size_thumb;
                    invalidate();
                }
                else if (state == State.DRAG_RIGHT)
                {
                    float x_pos = touch_point.x;
                    if (x_pos < pos_left + getPaddingLeft() + size_thumb)
                    {
                        x_pos = pos_left + getPaddingLeft() + size_thumb;
                    }

                    if (x_pos > rect_time_line.getRight())
                    {
                        x_pos = rect_time_line.getRight();
                    }

                    pos_right = x_pos - getPaddingLeft() - size_thumb;
                    invalidate();
                }
                else if (state == State.DRAG_MIDDLE)
                {
                    float difference = touch_point.x - last_touch_point.x;

                    float new_pos_left = (last_down_pos_left + difference);
                    float new_pos_right = (last_down_pos_right + difference);
                    float min_lift = 0;
                    float max_right = size_range_bar_width;

                    if (new_pos_left >= min_lift && new_pos_right <= max_right)
                    {
                        if (new_pos_right > size_range_bar_width - 5)
                        {
                            new_pos_right = size_range_bar_width;
                            new_pos_left = new_pos_right - middle_drag_pos_difference;
                        }

                        if (new_pos_left < 5)
                        {
                            new_pos_left = 0;
                            new_pos_right = new_pos_left + middle_drag_pos_difference;
                        }
                        pos_left = new_pos_left;
                        pos_right = new_pos_right;
                        invalidate();
                    }
                }

                notifyListener();
                break;

            case MotionEvent.ACTION_UP:
                state = State.IDLE;
                break;
        }

        return false;
    }

    private void notifyListener()
    {
        if (listener == null)
        {
            return;
        }

        float left = pos_left / size_range_bar_width;
        float right = pos_right / size_range_bar_width;
        listener.onDrag(left, right, state);
    }

    public void setListener(Listener listener)
    {
        this.listener = listener;
    }
}
