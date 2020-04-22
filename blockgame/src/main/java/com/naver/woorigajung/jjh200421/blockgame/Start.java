package com.naver.woorigajung.jjh200421.blockgame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class Start extends AppCompatActivity implements View.OnClickListener {

    TextView tvTime;    // 시간표시
    TextView tvPoint;    // 점수표시

    int time = 30;  // 시간값
    int point = 0;  // 점수값

    // 블럭이미지 리소스 배열
    int[] img = {R.drawable.block_red, R.drawable.block_green, R.drawable.block_blue};

    // 떨어지는 블럭의 ImageView 배열 객체
    ImageView[] iv = new ImageView[8]; // null 로 초기화되있음

    private Vibrator vibrator;  // 진동
    private SoundPool soundPool;    // 음향

    private int soundID_OK; // 음향 id : 블럭 맞추었을 때
    private int soundID_Error;  // 음향 id : 블럭 못 맞췄을 때

    private MediaPlayer mp; // 배경음악 재생

    final int DIALOG_TIMEOVER = 1; // 다이얼로그 ID

    Handler handler = new Handler(); // 시간

    // 게임진행 쓰레드
    class GameThread extends Thread {
        @Override
        public void run() {
            // 시간을 1초마다 다시 표시 (업데이트)
            // Handler 사용하여 화면 UI 업데이트

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (time >= 0) {
                        tvTime.setText("시간:" + time);
                        if (time > 0) {
                            time--;     // 1초 감소, 1초후에 다시 run() 수행
                            handler.postDelayed(this, 1000);
                        } else {
                            // time => 0 이 된경우
                            AlertDialog.Builder builder = new AlertDialog.Builder(Start.this);
                            builder.setTitle("타임아수")
                                    .setMessage("점수 : " + point)
                                    .setNegativeButton("그만하기", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            finish();   // 현재 화면 종료. 메인 화면으로 가기
                                        }
                                    })
                                    .setPositiveButton("다시하기", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // 게임 리셋하고, 새게임 시작
                                            time = 30;
                                            point = 0;
                                            tvTime.setText("시간:" + time);
                                            tvPoint.setText("점수:"+ point);
                                            GameThread g2 = new GameThread();
                                            g2.setDaemon(true);
                                            g2.start();

                                        }
                                    })
                                    ;
                            builder.show();
                        }
                    }
                }
            }, 1000);   // 1초후에 시간 표시


        }
    }   // end GameThread

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        // 상태바(status bar) 없애기, 반드시 setContentView()앞에서 처리
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.start);

        // 레이아웃 객체 (뷰) 초기화
        tvTime = findViewById(R.id.tvTime);
        tvPoint = findViewById(R.id.tvPoint);

        ImageView ivRed = findViewById(R.id.ivRed);
        ImageView ivGreen = findViewById(R.id.ivGreen);
        ImageView ivBlue = findViewById(R.id.ivBlue);

        iv[0] = findViewById(R.id.ivBlock1);
        iv[1] = findViewById(R.id.ivBlock2);
        iv[2] = findViewById(R.id.ivBlock3);
        iv[3] = findViewById(R.id.ivBlock4);
        iv[4] = findViewById(R.id.ivBlock5);
        iv[5] = findViewById(R.id.ivBlock6);
        iv[6] = findViewById(R.id.ivBlock7);
        iv[7] = findViewById(R.id.ivBlock8);

        // 게임이 시작되면, 초기화
        // 랜덤으로 블럭의 색상 지정
        for (int i = 0; i < iv.length; i++) {
            // 0, 1, 2 <-- red, greem, blue
            int num = new Random().nextInt(3);      // 0, 1, 2중의 랜덤 정수
            iv[i].setImageResource(img[num]);
            iv[i].setTag(num + ""); // 태그를 버튼의 색상 판단하기 위한 값으로 활용
        }

        // 점수 초기화
        point = 0;
        tvPoint.setText("점수:" + point);

        // r, g, b 버튼의 이벤트 리스너 등록
        ivRed.setOnClickListener(this);
        ivGreen.setOnClickListener(this);
        ivBlue.setOnClickListener(this);

        // 시간표시, 게임 진행 쓰레드 시작하기
        new GameThread().start();



    } // end onCreate()


    @Override
    public void onClick(View v) {
        // 버튼을 눌렀을 때 호출되는 콜백
        // 블럭과 같은 색깔의 버튼이 눌렸는지 판별, 같은 블럭이면 이미지 블럭 한칸씩 내려오기, 맨 위에는 새로운 블럭 생성

        boolean isOk = false; // 맞추었는지 판별 결과

        ImageView imageView = (ImageView) v;

        switch (imageView.getId()) {
            // 맨 아래 블럭 iv[7] 의  ImageView 의 색상과 일치하는 버튼인지 판정
            case R.id.ivRed:    // 빨강버튼 클릭시
                if ("0".equals(iv[7].getTag().toString())) isOk = true; // 빨강블럭의 tag값"0"


                break;
            case R.id.ivGreen:  // 초록버튼 클릭시
                if ("1".equals(iv[7].getTag().toString())) isOk = true; // 초록블럭의 tag값"1"


                break;
            case R.id.ivBlue:   // 파랑버튼 클릭시
                if ("2".equals(iv[7].getTag().toString())) isOk = true; // 파랑블럭의 tag값"2"

                break;
        }   // end switch

        if (isOk) {   // 버튼 색깔을 맞추었다면!
            // 위의 7개 블럭을 한칸 아래로 이동
            for (int i = iv.length - 2; i >= 0; i--) {
                int num = Integer.parseInt(iv[i].getTag().toString());  // "0", "1", "2"
                iv[i + 1].setImageResource(img[num]);
                iv[i + 1].setTag(num + "");
            } // end for

            // 가장 위의 블럭 ImageView 는 램덤으로 생성
            int num = new Random().nextInt(3);
            iv[0].setTag(num + "");
            iv[0].setImageResource(img[num]);


            // 진동 & 음향
            vibrator.vibrate(200);
            soundPool.play(soundID_OK, 1, 1, 0, 0, 1);

            // 점수 올리기
            point++;
            tvPoint.setText("점수:" + point);
        } else {    // 버튼 색깔이 틀리다면
            vibrator.vibrate(new long[]{20, 80, 20, 80, 20, 80}, -1);
            soundPool.play(soundID_Error, 1, 1, 0, 0, 1);

            // 점수 깍기
            point--;
            tvPoint.setText("점수:" + point);
        }
    }   // end if

    @Override
    // 앱이 가동상태일 때
    protected void onResume() {
        super.onResume();
        Log.d("myapp","onResume");
        // 자원획득
        // Vibrator 객체 얻어오기
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // SoundPool 객체
        soundPool = new SoundPool.Builder().setMaxStreams(5).build();
        soundID_OK = soundPool.load(Start.this, R.raw.gun3, 1);
        soundID_Error = soundPool.load(Start.this, R.raw.error, 1);

        // MediaPlayer 객체, 배경음악 연주 시작
        mp = MediaPlayer.create(getApplicationContext(), R.raw.bgm);
        mp.setLooping(false);   // 반복재생 안함
        mp.start(); // 배경음악 재생
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("myapp","onPause");
        // 자원해제
        if (mp != null) {
            mp.stop();
            mp.release();
        }
    }

    @Override
    protected void onStop() {
        Log.d("myapp","onStop");
    }
} // end Activity
