package com.sr.swordrain;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.os.storage.StorageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class FragmentGameScreen extends Fragment {

    private ImageView imageViewSwordLeft,imageViewRight,imageViewSword1,imageViewSword2,imageViewSword3,imageViewSword4,imageViewSword5,imageViewPoisonEnemy,imageViewGameBg,
            imageViewGameBg2,imageViewGameBg3,imageViewGameBg4,imageViewGameBg5,imageViewGameBg6,imageViewGameBg7;

    private ViewGroup mainLayout;
    private ConstraintLayout constraintLayout;

    private int swordSpeed1,swordSpeed2,swordSpeed3,swordSpeed4,swordSpeed5,poisonenemySpeed,sword1X,sword1Y,sword2X,sword2Y,sword3X,sword3Y,sword4X,sword4Y,sword5X,sword5Y,poisonenemyX,poisonenemyY;
    private int xDelta = -50;

    private Timer timer = new Timer();
    private Handler handler = new Handler();

    private Boolean control = false;

    private TextView textViewSkor;
    private int skor=0;

    private Animation anim_sword_middle,anim_sword_left,anim_sword_right,alpha_one;

    private String direction;

    private MediaPlayer mp,mp1,mp2,mp3,mp4;

    private InterstitialAd interstitialAd;

    private RewardedVideoAd rewardedVideoAd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_game,container,false);
        mainLayout = view.findViewById(R.id.constraintLayout);

        imageViewSwordLeft = view.findViewById(R.id.imageViewSwordLeft);
        imageViewRight = view.findViewById(R.id.imageViewRight);
        constraintLayout = view.findViewById(R.id.constraintLayout);
        textViewSkor = view.findViewById(R.id.textViewSkor);
        imageViewSword1 = view.findViewById(R.id.imageViewSword1);
        imageViewSword2 = view.findViewById(R.id.imageViewSword2);
        imageViewSword3 = view.findViewById(R.id.imageViewSword3);
        imageViewSword4 = view.findViewById(R.id.imageViewSword4);
        imageViewSword5 = view.findViewById(R.id.imageViewSword5);
        imageViewGameBg = view.findViewById(R.id.imageViewGameBg);
        imageViewSword1.setX((int) Math.floor(Math.random() * (400)));
        imageViewSword1.setY((int) Math.floor(Math.random() * (-400)));

        imageViewSword2.setX((int) Math.floor(Math.random() * (400)));
        imageViewSword2.setY((int) Math.floor(Math.random() * (-400)));

        imageViewSword3.setX((int) Math.floor(Math.random() * (400)));
        imageViewSword3.setY((int) Math.floor(Math.random() * (-400)));

        imageViewSword4.setX((int) Math.floor(Math.random() * (400)));
        imageViewSword4.setY((int) Math.floor(Math.random() * (-400)));

        imageViewSword5.setX((int) Math.floor(Math.random() * (400)));
        imageViewSword5.setY((int) Math.floor(Math.random() * (-400)));

        interstitialAd = new InterstitialAd(getActivity());

        interstitialAd.setAdUnitId("ca-app-pub-2362473887540939/3719291265");

        interstitialAd.loadAd(new AdRequest.Builder().build());

        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getActivity());

        rewardedVideoAd.loadAd("ca-app-pub-2362473887540939/8816593323",new AdRequest.Builder().build());

        ActivityCompat.requestPermissions(getActivity(),new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_GRANTED);

        //imageViewPoisonEnemy = view.findViewById(R.id.imageViewPoisonEnemy);

        textViewSkor.setText(String.valueOf(skor));

        Bundle bundle = getArguments();
        direction = bundle.getString("direction");

        mp = MediaPlayer.create(getActivity(),R.raw.sw1);
        mp1 = MediaPlayer.create(getActivity(),R.raw.sw2);
        mp2 = MediaPlayer.create(getActivity(),R.raw.sw1);
        mp3 = MediaPlayer.create(getActivity(),R.raw.sw2);
        mp4 = MediaPlayer.create(getActivity(),R.raw.sw1);

        anim_sword_middle = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.anim_sword_middle);
        anim_sword_left = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.anim_sword_left);
        anim_sword_right = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.anim_sword_right);
        alpha_one = AnimationUtils.loadAnimation(getActivity().getApplicationContext(),R.anim.alpha_one);

        if (direction == "left"){
            imageViewSwordLeft.setImageResource(getActivity().getResources().getIdentifier("sword","drawable",getActivity().getPackageName()));
        }
        if (direction == "middle"){
            imageViewSwordLeft.setImageResource(getActivity().getResources().getIdentifier("sworddik","drawable",getActivity().getPackageName()));
        }
        if (direction == "right"){
            imageViewSwordLeft.setImageResource(getActivity().getResources().getIdentifier("swordright","drawable",getActivity().getPackageName()));
        }

        constraintLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (control == false){
                    control = true;

                    sword1X = (int) imageViewSword1.getX();
                    sword1Y = (int) imageViewSword1.getY();

                    sword2X = (int) imageViewSword2.getX();
                    sword2Y = (int) imageViewSword2.getY();

                    sword3X = (int) imageViewSword3.getX();
                    sword3Y = (int) imageViewSword3.getY();

                    sword4X = (int) imageViewSword4.getX();
                    sword4Y = (int) imageViewSword4.getY();

                    sword5X = (int) imageViewSword5.getX();
                    sword5Y = (int) imageViewSword5.getY();

                    /*poisonenemyX = (int) imageViewPoisonEnemy.getX();
                    poisonenemyY = (int) imageViewPoisonEnemy.getY();*/

                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {

                            handler.post(new Runnable() {
                                @Override
                                public void run() {

                                    imageViewSwordLeft.setOnTouchListener(onTouchListener());
                                    cisimHareket();
                                    cisimCarpisma();

                                }
                            });

                        }
                    },0,20);
                }

                return true;
            }
        });

        return view;
    }

    private View.OnTouchListener onTouchListener(){
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int x = ((int) event.getRawX())*3/4;

                switch (event.getAction() & MotionEvent.ACTION_MASK){

                    case MotionEvent.ACTION_MOVE:
                        ConstraintLayout.LayoutParams layoutParamss = (ConstraintLayout.LayoutParams) v.getLayoutParams();
                        layoutParamss.leftMargin = x-xDelta;
                        v.setLayoutParams(layoutParamss);
                        break;
                }

                mainLayout.invalidate();
                return true;
            }
        };

    }

    public void cisimHareket(){

        swordSpeed1 = Math.round(constraintLayout.getWidth()/100); // 420 / 48 gibi
        swordSpeed2 = Math.round(constraintLayout.getWidth()/100);
        swordSpeed3 = Math.round(constraintLayout.getWidth()/100);
        swordSpeed4 = Math.round(constraintLayout.getWidth()/100);
        swordSpeed5 = Math.round(constraintLayout.getWidth()/100);
        //poisonenemySpeed = Math.round(constraintLayout.getWidth()/200);

        if (skor > 200){
            swordSpeed2 = Math.round(constraintLayout.getWidth()/90);
            swordSpeed3 = Math.round(constraintLayout.getWidth()/90);
        }
        if (skor > 300){
            swordSpeed4 = Math.round(constraintLayout.getWidth()/80);
            swordSpeed5 = Math.round(constraintLayout.getWidth()/80);
        }
        if (skor > 600){
            swordSpeed2 = Math.round(constraintLayout.getWidth()/70);
            swordSpeed3 = Math.round(constraintLayout.getWidth()/70);
        }
        if (skor > 800){
            swordSpeed4 = Math.round(constraintLayout.getWidth()/60);
            swordSpeed5 = Math.round(constraintLayout.getWidth()/60);
        }
        if (skor > 1400){
            swordSpeed2 = Math.round(constraintLayout.getWidth()/50);
            swordSpeed3 = Math.round(constraintLayout.getWidth()/50);
        }
        if (skor > 1800){
            swordSpeed4 = Math.round(constraintLayout.getWidth()/40);
            swordSpeed5 = Math.round(constraintLayout.getWidth()/40);
        }
        if (skor > 2500){
            swordSpeed2 = Math.round(constraintLayout.getWidth()/30);
            swordSpeed3 = Math.round(constraintLayout.getWidth()/30);
        }
        if (skor > 3300){
            swordSpeed4 = Math.round(constraintLayout.getWidth()/20);
            swordSpeed5 = Math.round(constraintLayout.getWidth()/20);
        }
        if (skor > 4200){
            swordSpeed4 = Math.round(constraintLayout.getWidth()/10);
            swordSpeed5 = Math.round(constraintLayout.getWidth()/10);
        }

        sword1Y += swordSpeed1;
        sword2Y += swordSpeed2;
        sword3Y += swordSpeed3;
        sword4Y += swordSpeed4;
        sword5Y += swordSpeed5;
        //poisonenemyY += poisonenemySpeed;

        if (sword1Y > imageViewSwordLeft.getY()+imageViewSwordLeft.getHeight()){
            timer.cancel();
            timer = null;
            // oyun bitti
        }
        if (sword2Y > imageViewSwordLeft.getY()+imageViewSwordLeft.getHeight()){
            timer.cancel();
            timer = null;
            // oyun bitti
        }
        if (sword3Y > imageViewSwordLeft.getY()+imageViewSwordLeft.getHeight()){
            timer.cancel();
            timer = null;
            // oyun bitti
        }
        if (sword4Y > imageViewSwordLeft.getY()+imageViewSwordLeft.getHeight()){
            timer.cancel();
            timer = null;
            // oyun bitti
        }
        if (sword5Y > imageViewSwordLeft.getY()+imageViewSwordLeft.getHeight()){
            timer.cancel();
            timer = null;
            // oyun bitti
        }
        if(timer == null){

            final View alertDesign =LayoutInflater.from(getActivity()).inflate(R.layout.alert_view_score,null);

            TextView textViewScore = alertDesign.findViewById(R.id.textViewScore);
            TextView textViewBestScore = alertDesign.findViewById(R.id.textViewBestScore);

            final ImageView imageViewHome = alertDesign.findViewById(R.id.imageViewHome);
            ImageView imageViewReplay = alertDesign.findViewById(R.id.imageViewReplay);
            final ImageView imageViewShare = alertDesign.findViewById(R.id.imageViewShare);

            textViewScore.setText(": "+textViewSkor.getText().toString());

            SharedPreferences sp = getActivity().getSharedPreferences("sonuc",getActivity().MODE_PRIVATE);
            int bestScore = sp.getInt("bestScore",0);

            if (skor > bestScore){

                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("bestScore",skor);
                editor.commit();

                textViewBestScore.setText("Best Score : "+skor);
            }else {
                textViewBestScore.setText("Best Score : "+bestScore);
            }

            AlertDialog.Builder ad = new AlertDialog.Builder(getActivity());

            final AlertDialog add = ad.create();

            add.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

            add.setView(alertDesign);

            add.setCancelable(false);

            add.show();

            imageViewHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (rewardedVideoAd.isLoaded()){
                        rewardedVideoAd.show();
                    }

                    rewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
                        @Override
                        public void onRewardedVideoAdLoaded() {

                        }

                        @Override
                        public void onRewardedVideoAdOpened() {

                        }

                        @Override
                        public void onRewardedVideoStarted() {

                        }

                        @Override
                        public void onRewardedVideoAdClosed() {

                        }

                        @Override
                        public void onRewarded(RewardItem rewardItem) {
                            add.cancel();

                            startActivity(new Intent(getActivity(),MainActivity.class));

                        }

                        @Override
                        public void onRewardedVideoAdLeftApplication() {

                        }

                        @Override
                        public void onRewardedVideoAdFailedToLoad(int i) {

                        }

                        @Override
                        public void onRewardedVideoCompleted() {

                        }
                    });
                }
            });
            imageViewReplay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (interstitialAd.isLoaded()){
                        interstitialAd.show();
                    }

                    interstitialAd.setAdListener(new AdListener(){
                        @Override
                        public void onAdClosed() {

                            interstitialAd.loadAd(new AdRequest.Builder().build());
                            add.cancel();

                            FragmentManager fm = getActivity().getSupportFragmentManager();
                            FragmentTransaction ft = fm.beginTransaction();

                            FragmentGameScreen fragmentGameScreen = new FragmentGameScreen();

                            Bundle bundle = getArguments();
                            direction = bundle.getString("direction");

                            fragmentGameScreen.setArguments(bundle);

                            ft.add(R.id.frameLayout,fragmentGameScreen);
                            ft.commit();
                        }
                    });

                }
            });

            imageViewShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bitmap bitmap = getScreenShot(alertDesign);
                    store(bitmap,"ScreenShot.png");

                    Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getAbsolutePath()+"/MyFiles/ScreenShot.png");

                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.sr.swordrain");
                    shareIntent.putExtra(Intent.EXTRA_STREAM,uri);
                    shareIntent.setType("image/png");
                    startActivity(Intent.createChooser(shareIntent, "Show to Everyone"));

                }
            });

        }
        /*if (poisonenemyY > imageViewSwordLeft.getY()+imageViewSwordLeft.getHeight()){

            poisonenemyY = (int) Math.floor(Math.random() * (imageViewSword2.getHeight()-constraintLayout.getHeight()));
            poisonenemyX = (int) Math.floor(Math.random() * (constraintLayout.getWidth()-imageViewSword2.getWidth()));

        }

        imageViewPoisonEnemy.setY(poisonenemyY);
        imageViewPoisonEnemy.setX(poisonenemyX);*/

    }

    public static Bitmap getScreenShot(View view){
        View screenView = view.getRootView();
        screenView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
        screenView.setDrawingCacheEnabled(false);
        return bitmap;
    }

    public void store(Bitmap b,String fileName){
        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/MyFiles";
        File dir = new File(dirPath);
        if (!dir.exists()){
            dir.mkdirs();
        }
        File file = new File(dirPath,fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.PNG,100,fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cisimCarpisma(){

        if (skor == 0){
            imageViewGameBg.setColorFilter(getActivity().getResources().getColor(R.color.color1));
        }
        if (skor == 600){
            imageViewGameBg.setColorFilter(getActivity().getResources().getColor(R.color.color2));
            imageViewSword1.setImageResource(getActivity().getResources().getIdentifier("sword3","drawable",getActivity().getPackageName()));
            imageViewSword2.setImageResource(getActivity().getResources().getIdentifier("sword3","drawable",getActivity().getPackageName()));
            imageViewSword3.setImageResource(getActivity().getResources().getIdentifier("sword3","drawable",getActivity().getPackageName()));
            imageViewSword4.setImageResource(getActivity().getResources().getIdentifier("sword3","drawable",getActivity().getPackageName()));
            imageViewSword5.setImageResource(getActivity().getResources().getIdentifier("sword3","drawable",getActivity().getPackageName()));
        }
        if (skor == 1200){
            imageViewGameBg.setColorFilter(getActivity().getResources().getColor(R.color.color4));
            imageViewSword1.setImageResource(getActivity().getResources().getIdentifier("sword5","drawable",getActivity().getPackageName()));
            imageViewSword2.setImageResource(getActivity().getResources().getIdentifier("sword5","drawable",getActivity().getPackageName()));
            imageViewSword3.setImageResource(getActivity().getResources().getIdentifier("sword5","drawable",getActivity().getPackageName()));
            imageViewSword4.setImageResource(getActivity().getResources().getIdentifier("sword5","drawable",getActivity().getPackageName()));
            imageViewSword5.setImageResource(getActivity().getResources().getIdentifier("sword5","drawable",getActivity().getPackageName()));
        }
        if (skor == 1800){
            imageViewGameBg.setColorFilter(getActivity().getResources().getColor(R.color.color3));
            imageViewSword1.setImageResource(getActivity().getResources().getIdentifier("sword4","drawable",getActivity().getPackageName()));
            imageViewSword2.setImageResource(getActivity().getResources().getIdentifier("sword4","drawable",getActivity().getPackageName()));
            imageViewSword3.setImageResource(getActivity().getResources().getIdentifier("sword4","drawable",getActivity().getPackageName()));
            imageViewSword4.setImageResource(getActivity().getResources().getIdentifier("sword4","drawable",getActivity().getPackageName()));
            imageViewSword5.setImageResource(getActivity().getResources().getIdentifier("sword4","drawable",getActivity().getPackageName()));
        }
        if (skor == 2400){
            imageViewGameBg.setColorFilter(getActivity().getResources().getColor(R.color.color6));
            imageViewSword1.setImageResource(getActivity().getResources().getIdentifier("sword7","drawable",getActivity().getPackageName()));
            imageViewSword2.setImageResource(getActivity().getResources().getIdentifier("sword7","drawable",getActivity().getPackageName()));
            imageViewSword3.setImageResource(getActivity().getResources().getIdentifier("sword7","drawable",getActivity().getPackageName()));
            imageViewSword4.setImageResource(getActivity().getResources().getIdentifier("sword7","drawable",getActivity().getPackageName()));
            imageViewSword5.setImageResource(getActivity().getResources().getIdentifier("sword7","drawable",getActivity().getPackageName()));
        }
        if (skor == 3200){
            imageViewGameBg.setColorFilter(getActivity().getResources().getColor(R.color.color5));
            imageViewSword1.setImageResource(getActivity().getResources().getIdentifier("sword6","drawable",getActivity().getPackageName()));
            imageViewSword2.setImageResource(getActivity().getResources().getIdentifier("sword6","drawable",getActivity().getPackageName()));
            imageViewSword3.setImageResource(getActivity().getResources().getIdentifier("sword6","drawable",getActivity().getPackageName()));
            imageViewSword4.setImageResource(getActivity().getResources().getIdentifier("sword6","drawable",getActivity().getPackageName()));
            imageViewSword5.setImageResource(getActivity().getResources().getIdentifier("sword6","drawable",getActivity().getPackageName()));
        }
        if (skor == 4000) {
            imageViewGameBg.setColorFilter(getActivity().getResources().getColor(R.color.color7));
            imageViewSword1.setImageResource(getActivity().getResources().getIdentifier("sword8", "drawable", getActivity().getPackageName()));
            imageViewSword2.setImageResource(getActivity().getResources().getIdentifier("sword8", "drawable", getActivity().getPackageName()));
            imageViewSword3.setImageResource(getActivity().getResources().getIdentifier("sword8", "drawable", getActivity().getPackageName()));
            imageViewSword4.setImageResource(getActivity().getResources().getIdentifier("sword8", "drawable", getActivity().getPackageName()));
            imageViewSword5.setImageResource(getActivity().getResources().getIdentifier("sword8", "drawable", getActivity().getPackageName()));
        }


        // 1.cisim
        int swordMerkez1X = sword1X + imageViewSwordLeft.getWidth();
        int swordMerkez1Y = sword1Y + imageViewSwordLeft.getHeight();

        if (swordMerkez1Y >= imageViewSwordLeft.getY() && swordMerkez1Y <= imageViewSwordLeft.getY()+imageViewSwordLeft.getHeight()+100
                && swordMerkez1X >= imageViewSwordLeft.getX() && swordMerkez1X <= imageViewSwordLeft.getX()+imageViewSwordLeft.getWidth()+100){
            mp.start();

            sword1Y = (int) Math.floor(Math.random() * (imageViewSword2.getHeight()-constraintLayout.getHeight()));
            sword1X = (int) Math.floor(Math.random() * (constraintLayout.getWidth()-imageViewSword2.getWidth()));

            skor += 10;
            textViewSkor.setText(String.valueOf(skor));

            if (direction == "left"){
                imageViewSwordLeft.startAnimation(anim_sword_left);
            }
            if (direction == "middle"){
                imageViewSwordLeft.startAnimation(anim_sword_middle);
            }
            if (direction == "right"){
                imageViewSwordLeft.startAnimation(anim_sword_right);
            }

        }

        imageViewSword1.setY(sword1Y);
        imageViewSword1.setX(sword1X);

        // 2.cisim
        int swordMerkez2X = sword2X + imageViewSwordLeft.getWidth();
        int swordMerkez2Y = sword2Y + imageViewSwordLeft.getHeight();

        if (swordMerkez2Y >= imageViewSwordLeft.getY() && swordMerkez2Y <= imageViewSwordLeft.getY()+imageViewSwordLeft.getHeight()+100
                && swordMerkez2X >= imageViewSwordLeft.getX() && swordMerkez2X <= imageViewSwordLeft.getX()+imageViewSwordLeft.getWidth()+100){
            mp1.start();

            sword2Y = (int) Math.floor(Math.random() * (imageViewSword2.getHeight()-constraintLayout.getHeight()));
            sword2X = (int) Math.floor(Math.random() * (constraintLayout.getWidth()-imageViewSword2.getWidth()));

            skor += 10;
            textViewSkor.setText(String.valueOf(skor));

            imageViewSwordLeft.startAnimation(anim_sword_left);

            if (direction == "left"){
                imageViewSwordLeft.startAnimation(anim_sword_left);
            }
            if (direction == "middle"){
                imageViewSwordLeft.startAnimation(anim_sword_middle);
            }
            if (direction == "right"){
                imageViewSwordLeft.startAnimation(anim_sword_right);
            }

        }

        imageViewSword2.setY(sword2Y);
        imageViewSword2.setX(sword2X);

        // 3.cisim
        int swordMerkez3X = sword3X + imageViewSwordLeft.getWidth();
        int swordMerkez3Y = sword3Y + imageViewSwordLeft.getHeight();

        if (swordMerkez3Y >= imageViewSwordLeft.getY() && swordMerkez3Y <= imageViewSwordLeft.getY()+imageViewSwordLeft.getHeight()+100
                && swordMerkez3X >= imageViewSwordLeft.getX() && swordMerkez3X <= imageViewSwordLeft.getX()+imageViewSwordLeft.getWidth()+100){
            mp2.start();

            sword3Y = (int) Math.floor(Math.random() * (imageViewSword2.getHeight()-constraintLayout.getHeight()));
            sword3X = (int) Math.floor(Math.random() * (constraintLayout.getWidth()-imageViewSword2.getWidth()));

            skor += 10;
            textViewSkor.setText(String.valueOf(skor));

            imageViewSwordLeft.startAnimation(anim_sword_left);

            if (direction == "left"){
                imageViewSwordLeft.startAnimation(anim_sword_left);
            }
            if (direction == "middle"){
                imageViewSwordLeft.startAnimation(anim_sword_middle);
            }
            if (direction == "right"){
                imageViewSwordLeft.startAnimation(anim_sword_right);
            }

        }

        imageViewSword3.setY(sword3Y);
        imageViewSword3.setX(sword3X);

        // 4.cisim
        int swordMerkez4X = sword4X + imageViewSwordLeft.getWidth();
        int swordMerkez4Y = sword4Y + imageViewSwordLeft.getHeight();

        if (swordMerkez4Y >= imageViewSwordLeft.getY() && swordMerkez4Y <= imageViewSwordLeft.getY()+imageViewSwordLeft.getHeight()+100
                && swordMerkez4X >= imageViewSwordLeft.getX() && swordMerkez4X <= imageViewSwordLeft.getX()+imageViewSwordLeft.getWidth()+100){
            mp3.start();

            sword4Y = (int) Math.floor(Math.random() * (imageViewSword2.getHeight()-constraintLayout.getHeight()));
            sword4X = (int) Math.floor(Math.random() * (constraintLayout.getWidth()-imageViewSword2.getWidth()));

            skor += 10;
            textViewSkor.setText(String.valueOf(skor));

            imageViewSwordLeft.startAnimation(anim_sword_left);

            if (direction == "left"){
                imageViewSwordLeft.startAnimation(anim_sword_left);
            }
            if (direction == "middle"){
                imageViewSwordLeft.startAnimation(anim_sword_middle);
            }
            if (direction == "right"){
                imageViewSwordLeft.startAnimation(anim_sword_right);
            }

        }

        imageViewSword4.setY(sword4Y);
        imageViewSword4.setX(sword4X);

        // 5.cisim
        int swordMerkez5X = sword5X + imageViewSwordLeft.getWidth();
        int swordMerkez5Y = sword5Y + imageViewSwordLeft.getHeight();

        if (swordMerkez5Y >= imageViewSwordLeft.getY() && swordMerkez5Y <= imageViewSwordLeft.getY()+imageViewSwordLeft.getHeight()+100
                && swordMerkez5X >= imageViewSwordLeft.getX() && swordMerkez5X <= imageViewSwordLeft.getX()+imageViewSwordLeft.getWidth()+100){
            mp4.start();

            sword5Y = (int) Math.floor(Math.random() * (imageViewSword2.getHeight()-constraintLayout.getHeight()));
            sword5X = (int) Math.floor(Math.random() * (constraintLayout.getWidth()-imageViewSword2.getWidth()));

            skor += 10;
            textViewSkor.setText(String.valueOf(skor));

            imageViewSwordLeft.startAnimation(anim_sword_left);

            if (direction == "left"){
                imageViewSwordLeft.startAnimation(anim_sword_left);
            }
            if (direction == "middle"){
                imageViewSwordLeft.startAnimation(anim_sword_middle);
            }
            if (direction == "right"){
                imageViewSwordLeft.startAnimation(anim_sword_right);
            }

        }

        imageViewSword5.setY(sword5Y);
        imageViewSword5.setX(sword5X);

        // poison enemy
        /*int poisonMerkezX = poisonenemyX + imageViewPoisonEnemy.getWidth();
        int posionMerkezY = poisonenemyY + imageViewPoisonEnemy.getHeight();

        if (posionMerkezY >= imageViewSwordLeft.getY() && posionMerkezY <= imageViewSwordLeft.getY()+imageViewSwordLeft.getHeight()+100
                && poisonMerkezX >= imageViewSwordLeft.getX() && poisonMerkezX <= imageViewSwordLeft.getX()+imageViewSwordLeft.getWidth()+100){
            mp4.start();

            timer.cancel();
            timer = null;

        }*/

    }

/*    public boolean speedOp(final ImageView imageViewPoisonEnemy) {
        boolean speedOp = false;

        if (imageViewPoisonEnemy.getY() < imageViewSwordLeft.getY()+imageViewSwordLeft.getHeight()){

            final Handler ha=new Handler();
            ha.postDelayed(new Runnable() {

                @Override
                public void run() {
                    //call function

                    poisonenemySpeed = Math.round(constraintLayout.getWidth()/120);
                    poisonenemyY += poisonenemySpeed;

                    imageViewPoisonEnemy.setY(poisonenemyY);
                    imageViewPoisonEnemy.setX(poisonenemyX);

                    ha.postDelayed(this, 20);
                }
            }, 10000);

            speedOp = true;
        }

        if (imageViewPoisonEnemy.getY() > imageViewSwordLeft.getY()+imageViewSwordLeft.getHeight()){

            poisonenemyY = (int) Math.floor(Math.random() * (imageViewPoisonEnemy.getHeight()-constraintLayout.getHeight()));
            poisonenemyX = (int) Math.floor(Math.random() * (constraintLayout.getWidth()-imageViewPoisonEnemy.getWidth()));


            imageViewPoisonEnemy.setY(poisonenemyY);
            imageViewPoisonEnemy.setX(poisonenemyX);

            speedOp = true;

        }

        return speedOp;
    }*/

}
