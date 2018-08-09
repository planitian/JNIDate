package com.example.administrator.jni;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortFinder;

import static com.example.administrator.jni.Util.bytesToHexFun3;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    byte sss;
    private Button select;
    private Button connect;
    private Button send;
    private Button show;
    private Button close;
    private TextView showData;

    String[] devices;
    String[] devicesPath;
    private SerialPortFinder serialPortFinder;
    private SerialPort serialPort;
    private Optional<OutputStream> outputStreamOptional;
    private OutputStream outputStream;
    private InputStream inputStream;
    private ExecutorService executorService;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            showData.setText((msg.obj) + "");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connect = findViewById(R.id.connect);
        send = findViewById(R.id.send);
        showData = findViewById(R.id.textView);
        show = findViewById(R.id.show);
        close = findViewById(R.id.close);
        select=findViewById(R.id.select);

        executorService = Executors.newCachedThreadPool();

        close.setOnClickListener(this);
        show.setOnClickListener(this);
        connect.setOnClickListener(this);
        send.setOnClickListener(this);
        select.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select:
//                startActivity(new Intent(MainActivity.this, SerialPortPreferences.class));
                DevicePre devicePre = new DevicePre();
                devicePre.setContext(MainActivity.this);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.content, devicePre, "ss");
                fragmentTransaction.show(devicePre);
                fragmentTransaction.commit();
                break;
            case R.id.connect:
                serialPortFinder = ((MyApplication) getApplication()).serialPortFinder;
                try {
                    serialPort = ((MyApplication) getApplication()).getSerialPort();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                ;
//                outputStreamOptional=Optional.ofNullable(serialPort.getOutputStream());
                inputStream = serialPort.getInputStream();
                outputStream = serialPort.getOutputStream();
                break;
            case R.id.send:
//                outputStreamOptional=Optional.ofNullable(serialPort.getOutputStream());
                String send = "FE050000FF009835";
                String close = "FE0500000000D9C5";
                try {
//                    outputStream = outputStreamOptional.orElseThrow(()->new NullPointerException("kongde"));
                    outputStream.write(Util.toBytes(close));
                    outputStream.flush();
                    //                    outputStream.close();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                break;
            case R.id.show:
                executorService.execute(() -> {
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    int size = 0;
                    byte[] buffer = new byte[18];
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    while (true) {
                        //                    System.out.println("inputStream.available()" + inputStream.available()+bufferedInputStream.available());
                        //                    System.out.println("IIIIIIIIII");
                        try {
                            if (bufferedInputStream.available() >= 18) {
                                //                                        size = inputStream.read(buffer,0,18);
                                size = bufferedInputStream.read(buffer);
                                System.out.println("Size >" + size);
                                if (size == 18) {
                                    System.out.println("!!!!!我读取了size" + size);
                                    //                                        String result = bytesToHexFun3(buffer);
                                    String result = bytesToHexFun3(buffer);
                                    Message message = Message.obtain();
                                    message.obj = result;
                                    System.out.println("inputdata  " + result);
                                    System.out.println("changdu>>" + result.length());
                                    handler.sendMessage(message);
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                });
                break;
            case R.id.close:
                executorService.shutdownNow();
                inputStream=null;
                outputStream=null;
                System.gc();
                serialPort.close();
                showData.setText("");
                break;

        }
    }

///>>>>>>>>>>
}
