package com.bios.rangebar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

class MyRoundRect
{
    private float left;
    private float top;
    private float right;
    private float bottom;

    private float radius_top_left;
    private float radius_top_right;
    private float radius_bottom_right;
    private float radius_bottom_left;

    private Paint paint;
    private Path path = new Path();

    public void setLeft(float left)
    {
        this.left = left;
    }

    public void setTop(float top)
    {
        this.top = top;
    }

    public void setRight(float right)
    {
        this.right = right;
    }

    public void setBottom(float bottom)
    {
        this.bottom = bottom;
    }

    public void setRadius_top_left(float radius_top_left)
    {
        this.radius_top_left = radius_top_left;
    }

    public void setRadius_top_right(float radius_top_right)
    {
        this.radius_top_right = radius_top_right;
    }

    public void setRadius_bottom_right(float radius_bottom_right)
    {
        this.radius_bottom_right = radius_bottom_right;
    }

    public void setRadius_bottom_left(float radius_bottom_left)
    {
        this.radius_bottom_left = radius_bottom_left;
    }

    public void setPaint(Paint paint)
    {
        this.paint = paint;
    }

    public float getLeft()
    {
        return left;
    }

    public float getTop()
    {
        return top;
    }

    public float getRight()
    {
        return right;
    }

    public float getBottom()
    {
        return bottom;
    }

    public void drawOnCanvas(Canvas canvas)
    {
        path.reset();
        path.moveTo(left, top + radius_top_left);
        path.quadTo(left, top, left + radius_top_left, top);
        path.lineTo(right - radius_top_right, top);
        path.quadTo(right, top, right, top + radius_top_right);
        path.lineTo(right, bottom - radius_bottom_right);
        path.quadTo(right, bottom, right - radius_bottom_right, bottom);
        path.lineTo(left + radius_bottom_left, bottom);
        path.quadTo(left, bottom, left, bottom - radius_bottom_left);
        path.lineTo(left, top - radius_top_left);

        canvas.drawPath(path, paint);
    }

    public boolean isTouchInRect(PointF touch)
    {
        boolean is_x_in_rect = touch.x >= left && touch.x <= right;
        boolean is_y_in_rect = touch.y >= top && touch.y <= bottom;

        return is_x_in_rect && is_y_in_rect;
    }
}
