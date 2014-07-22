package com.android.animation.example;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Display;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class MainActivity extends Activity implements View.OnTouchListener {

    private int mImageOffset;

    private ImageView mImageView;

    private RelativeLayout mWrapper;

    private View mCorner1;
    private View mCorner2;
    private View mCorner3;
    private View mCorner4;

    private static final String IMAGE_TAG = "image_tag";

    private static final int VIBRATE_DURATION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.android_image);
        mImageView.setTag(IMAGE_TAG);
        mImageView.setOnLongClickListener(new ImageLongClickListener());
        mImageOffset = getResources().getDimensionPixelSize(R.dimen.image_size) / 2;

        mWrapper = (RelativeLayout) findViewById(R.id.wrapper);
        mWrapper.setOnDragListener(new ViewOnDragListener());


        mCorner1 = findViewById(R.id.corner_1);
        mCorner2 = findViewById(R.id.corner_2);
        mCorner3 = findViewById(R.id.corner_3);
        mCorner4 = findViewById(R.id.corner_4);


        mCorner1.setOnTouchListener(this);
        mCorner2.setOnTouchListener(this);
        mCorner3.setOnTouchListener(this);
        mCorner4.setOnTouchListener(this);

    }

    private class ImageLongClickListener implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View view) {

            Vibrator v = (Vibrator) MainActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(VIBRATE_DURATION);
            ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());

            String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
            ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

            view.startDrag(data, shadowBuilder, view, 0);
            view.setVisibility(View.INVISIBLE);
            return false;
        }
    }

    private class ViewOnDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            if (event.getAction() == DragEvent.ACTION_DROP) {
                View view = (View) event.getLocalState();

                int xLimit = (mWrapper.getWidth() / 2) - mImageOffset;
                int yLimit = (mWrapper.getHeight() / 2) - mImageOffset;

                float dropX = event.getX() - mImageOffset;
                float dropY = event.getY() - mImageOffset;

                view.setTranslationX(dropX);
                view.setTranslationY(dropY);
                view.setVisibility(View.VISIBLE);

                if (dropX < xLimit && dropY < yLimit) {
                    // Top left corner
                    view.animate().x(mCorner1.getX()).y(mCorner1.getY());
                } else if (dropX > xLimit && dropY < yLimit) {
                    // Top right corner
                    view.animate().x(mCorner2.getX()).y(mCorner2.getY());
                } else if (dropX < xLimit && dropY > yLimit) {
                    // Bottom left corner
                    view.animate().x(mCorner3.getX()).y(mCorner3.getY());
                } else if (dropX > xLimit && dropY > yLimit) {
                    // Bottom right corner
                    view.animate().x(mCorner4.getX()).y(mCorner4.getY());
                }
            }
            return true;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mImageView.animate()
                        .translationX(v.getX())
                        .translationY(v.getY());
                break;
        }
        return false;
    }
}
