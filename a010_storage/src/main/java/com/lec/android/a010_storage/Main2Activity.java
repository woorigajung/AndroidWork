package com.lec.android.a010_storage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 외부 메모리 (External Memory 혹은 Shared storage)
 *      사용자 영역 에 저장 ( sdcard 같은 외장 메모리를 의미하는게 아니다)
 *      메모리가 장착(mount)되어 있어야 사용 가능
 *      모든 앱에서 접근 가능 (공유 가능)
 *
 * 외부 메모리에 파일 읽기/쓰기
 *      1. 외부 메모리 장치가 있는지 확인해야한다 :  getExternalStorageState()
 *      2-1 AndroidManifest.xml 외부메모리 저장권한을 선언해야 함  (API 23 이전 버젼)
 *              WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE
 *      2-2 API 23+ (마시멜로): 새로운 권한 획득 방법 사용.
 *          Run-time 에 onRequestPermissionsResult() 사용 해야 한다!!!
 *          https://developer.android.com/training/permissions/requesting.html
 *
 *     3. 읽기/쓰기 경로를 지정한다
 */

public class Main2Activity extends AppCompatActivity {
    EditText etInput;
    Button btnSave, btnRead;
    TextView tvResult;

    final String [] PERMISSIONS={Manifest.permission.WRITE_EXTERNAL_STORAGE};
    final int REQUEST_CODE=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        etInput = findViewById(R.id.etInput);
        btnRead = findViewById(R.id.btnRead);
        btnSave = findViewById(R.id.btnSave);
        tvResult = findViewById(R.id.tvResult);

        //위험권한 획득
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && checkSelfPermission(String.valueOf(PERMISSIONS))== PackageManager.PERMISSION_DENIED)
        {
            requestPermissions(PERMISSIONS,REQUEST_CODE);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkExternalStorage()){
                    return;
                }
                String data=etInput.getText().toString();
                try {
                    File path=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    File file = new File(path,"external.txt"); //경로파일명
                    FileWriter writer=new FileWriter(file,false);

                    PrintWriter out=new PrintWriter(writer);
                    out.println(data);
                    out.close();
                    tvResult.setText("저장완료");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkExternalStorage()){
                    return;
                }


                try {
                    File path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    File file=new File(path,"external.txt");
                    BufferedReader bufferedReader=new BufferedReader(new FileReader(file));
                    StringBuffer data=new StringBuffer();
                    String str=bufferedReader.readLine();
                    while (str!=null) {
                        data.append(str);
                        str=bufferedReader.readLine();
                    }
                    tvResult.setText(data);
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }//onCreate
    //권한획득결과받으면 실행되는 콜백


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case REQUEST_CODE:

                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(),"권한승인",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"권한거부",Toast.LENGTH_SHORT).show();
                    //권한이없으면 더이상 진행못하게 해야함
                }

                return;
        }

    }//onRequestPermissionsResult

    //외부메모리 상태 확인 메소드
    protected boolean checkExternalStorage(){
        String state=Environment.getExternalStorageState();
        String msg;
        //외부메모리 상태체크
        if(Environment.MEDIA_MOUNTED.equals(state)){
            //읽기쓰기 모두 가능한상태
            msg="외부메모리 읽기쓰기 모두 가능한상태";
            Log.d("myapp",msg);

            return true;
        }else if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
            //읽기만 가능한상태 읽기전용용
            msg="외부메모리 읽기만 가능";
            Log.d("myapp",msg);
            return false;
        }else{
            msg="외부메모리 읽기쓰기 다안됨"+state;
            Log.d("myapp",msg);
            return false;
        }
    }
}