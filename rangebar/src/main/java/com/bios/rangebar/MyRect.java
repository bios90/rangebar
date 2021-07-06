package com.bios.rangebar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

class MyRect
{
    private float left;
    private float top;
    private float right;
    private float bottom;

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
        path.moveTo(left, top);
        path.lineTo(right, top);
        path.lineTo(right, bottom);
        path.lineTo(left, bottom);
        path.lineTo(left, top);

        canvas.drawPath(path, paint);
    }

    public boolean isTouchInRect(PointF touch)
    {
        boolean is_x_in_rect = touch.x >= left && touch.x <= right;
        boolean is_y_in_rect = touch.y >= top && touch.y <= bottom;

        return is_x_in_rect && is_y_in_rect;
    }
}
