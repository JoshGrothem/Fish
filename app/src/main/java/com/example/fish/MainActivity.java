package com.example.fish;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Display;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private Thread calculateMovementThread;

    // FISH TANK ELEMENTS AND PROPERTIES
    private static ImageView fishImageView1;
    private static Fish mFish1;

    private static int tankWidth;
    private static int tankHeight;
    private FrameLayout fishTankLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fishTankLayout = (FrameLayout) findViewById(R.id.container);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        tankWidth = size.x + 400;
        tankHeight = size.y;

        int initialXPosition = 0;
        int initialYPosition = 0;
        mFish1 = new Fish(initialXPosition, initialYPosition, Fish.IsSwimming, tankWidth, tankHeight, 9);

        buildTank();

        calculateMovementThread = new Thread(calculateMovement);

        calculateMovementThread.start();
    }


    private void buildTank() {
        LayoutInflater layoutInflater;
        layoutInflater = (LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ImageView foliageImageView = (ImageView)
                layoutInflater.inflate(R.layout.foliage_layout, null);
        foliageImageView.setScaleX((float) 1.3);
        foliageImageView.setScaleY((float) 1.3);
        foliageImageView.setX((float) 0);
        foliageImageView.setY((float) 100);
        foliageImageView.setAlpha((float) .90);
        fishTankLayout.addView(foliageImageView, 0);

        fishImageView1 = (ImageView) layoutInflater.inflate(R.layout.fish_image, null);
        fishImageView1.setScaleX((float) .3);
        fishImageView1.setScaleY((float) .1);
        fishImageView1.setX(mFish1.x);
        fishImageView1.setY(mFish1.y);
        fishTankLayout.addView(fishImageView1, 0);


    }

    //******************* RUNNABLE **********************
    private Runnable calculateMovement = new Runnable() {
        private static final int DELAY = 100;

        public void run() {
            try {
                while (true) {
                    mFish1.move();

                    Thread.sleep(DELAY);
                    updateTankHandler.sendEmptyMessage(0);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    //***** HANDLER FOR UPDATING THE FISH BETWEEN SLEEP DELAYS ******

    public static Handler updateTankHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {

            fishImageView1.setScaleX((float) (.3 * mFish1.getFacingDirection()));

            fishImageView1.setX((float) mFish1.x);
            fishImageView1.setY((float) mFish1.y);
        }
    };
}
