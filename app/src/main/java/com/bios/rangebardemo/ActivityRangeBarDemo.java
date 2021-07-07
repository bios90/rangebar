package com.bios.rangebardemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.bios.rangebar.RangeBar;

public class ActivityRangeBarDemo extends AppCompatActivity
{
    private RangeBar rb_1;
    private TextView rb_1_left, rb_1_right, rb_1_mode;

    private RangeBar rb_2;
    private TextView rb_2_left, rb_2_right, rb_2_mode;

    private RangeBar rb_3;
    private TextView rb_3_left, rb_3_right, rb_3_mode;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_range_bar_demo);

        rb_1 = findViewById(R.id.rb_1);
        rb_1_left = findViewById(R.id.rb_1_left);
        rb_1_right = findViewById(R.id.rb_1_right);
        rb_1_mode = findViewById(R.id.rb_1_mode);

        rb_2 = findViewById(R.id.rb_2);
        rb_2_left = findViewById(R.id.rb_2_left);
        rb_2_right = findViewById(R.id.rb_2_right);
        rb_2_mode = findViewById(R.id.rb_2_mode);

        rb_3 = findViewById(R.id.rb_3);
        rb_3_left = findViewById(R.id.rb_3_left);
        rb_3_right = findViewById(R.id.rb_3_right);
        rb_3_mode = findViewById(R.id.rb_3_mode);


        RangeBar rb_1 = findViewById(R.id.rb_1);
        rb_1.setRangeChangeListener(new RangeBar.Listener()
        {
            @Override
            public void onDrag(float pos_left, float pos_right, RangeBar.State mode)
            {
                Log.i("RangeBar", "Position left is " + pos_left + " | Position right is " + pos_right);
                Log.i("RangeBar", "Drag mode is " + mode);
            }
        });

        float pos_left = rb_1.getLeftPos();
        float pos_right = rb_1.getLeftPos();

        rb_1.setProgressRight(0.8f);
        rb_1.setProgressLeft(0.5f);

        Log.i("RangeBar", "Position left is " + pos_left + " | Position right is " + pos_right);


        rb_1.setRangeChangeListener(new RangeBar.Listener()
        {
            @Override
            public void onDrag(float pos_left, float pos_right, RangeBar.State mode)
            {
                Log.i("RangeBar", "Position left is " + pos_left + " | Position right is " + pos_right + " | and mode is " + mode);
                rb_1_left.setText("" + pos_left);
                rb_1_right.setText("" + pos_right);
                rb_1_mode.setText(mode.toString());
            }
        });

        rb_2.setRangeChangeListener(new RangeBar.Listener()
        {
            @Override
            public void onDrag(float pos_left, float pos_right, RangeBar.State mode)
            {
                Log.i("RangeBar", "Position left is " + pos_left + " | Position right is " + pos_right + " | and mode is " + mode);
                rb_2_left.setText("" + pos_left);
                rb_2_right.setText("" + pos_right);
                rb_2_mode.setText(mode.toString());
            }
        });

        rb_3.setRangeChangeListener(new RangeBar.Listener()
        {
            @Override
            public void onDrag(float pos_left, float pos_right, RangeBar.State mode)
            {
                Log.i("RangeBar", "Position left is " + pos_left + " | Position right is " + pos_right + " | and mode is " + mode);
                rb_3_left.setText("" + pos_left);
                rb_3_right.setText("" + pos_right);
                rb_3_mode.setText(mode.toString());
            }
        });
    }
}