package com.bios.rangebar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

class MyArrow
{
    private PointF bottom;
    private PointF middle;
    private PointF top;
    private Path path = new Path();
    private Paint paint;

    public void setBottom(PointF bottom)
    {
        this.bottom = bottom;
    }

    public void setMiddle(PointF middle)
    {
        this.middle = middle;
    }

    public void setTop(PointF top)
    {
        this.top = top;
    }

    public void setPaint(Paint paint)
    {
        this.paint = paint;
    }

    public void drawOnCanvas(Canvas canvas)
    {
        path.reset();
        path.moveTo(bottom.x, bottom.y);
        path.lineTo(middle.x, middle.y);
        path.lineTo(top.x, top.y);
        canvas.drawPath(path, paint);
    }
}
