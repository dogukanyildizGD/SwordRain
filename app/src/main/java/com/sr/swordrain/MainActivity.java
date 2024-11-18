package com.sr.swordrain;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView imageViewStartButton,imageViewLeft,imageViewMiddle,imageViewRight,imageViewSwordMain;
    private FrameLayout frameLayout;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final View alertViewDesign = LayoutInflater.from(this).inflate(R.layout.select_cursor,null);

        imageViewStartButton = findViewById(R.id.imageViewStartButton);
        imageViewLeft = alertViewDesign.findViewById(R.id.imageViewLeft);
        imageViewMiddle = alertViewDesign.findViewById(R.id.imageViewMiddle);
        imageViewRight = alertViewDesign.findViewById(R.id.imageViewRight);
        imageViewSwordMain = findViewById(R.id.imageViewSwordMain);
        frameLayout = alertViewDesign.findViewById(R.id.frameLayout);

        animation = AnimationUtils.loadAnimation(this,R.anim.anim_sword_main);

        imageViewSwordMain.startAnimation(animation);

        imageViewStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);

                final AlertDialog add = ad.create();

                add.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                add.setView(alertViewDesign);

                add.show();

                imageViewLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        add.cancel();

                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();

                        FragmentGameScreen fragmentGameScreen = new FragmentGameScreen();

                        Bundle bundle = new Bundle();
                        bundle.putString("direction", "left");

                        fragmentGameScreen.setArguments(bundle);

                        ft.addToBackStack(null);
                        ft.add(R.id.frameLayout,fragmentGameScreen);
                        ft.commit();

                    }
                });

                imageViewMiddle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        add.cancel();

                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();

                        FragmentGameScreen fragmentGameScreen = new FragmentGameScreen();

                        Bundle bundle = new Bundle();
                        bundle.putString("direction", "middle");

                        fragmentGameScreen.setArguments(bundle);

                        ft.addToBackStack(null);
                        ft.add(R.id.frameLayout,fragmentGameScreen);
                        ft.commit();

                    }
                });

                imageViewRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        add.cancel();

                        FragmentManager fm = getSupportFragmentManager();
                        FragmentTransaction ft = fm.beginTransaction();

                        FragmentGameScreen fragmentGameScreen = new FragmentGameScreen();

                        Bundle bundle = new Bundle();
                        bundle.putString("direction", "right");

                        fragmentGameScreen.setArguments(bundle);

                        ft.addToBackStack(null);
                        ft.add(R.id.frameLayout,fragmentGameScreen);
                        ft.commit();

                    }
                });

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,MainActivity.class));
    }
}
