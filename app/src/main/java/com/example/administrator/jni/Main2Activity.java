package com.example.administrator.jni;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main2Activity extends AppCompatActivity implements ReadSerialPort.DataWeight{

    @BindView(R.id.select)
    Button select;
    @BindView(R.id.send)
    Button send;
    @BindView(R.id.show)
    Button show;
    @BindView(R.id.close)
    Button close;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.light_send)
    Button lightSend;
    private SerialPortFunction serialPortFunction;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        serialPortFunction=SerialPortFunction.getInstance();
        serialPortFunction.setContext(this);
    }

    @OnClick(R.id.select)
    public void setSelect(){
        DevicePre devicePre = new DevicePre();
        devicePre.setContext(Main2Activity.this);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content, devicePre, "ss");
        fragmentTransaction.show(devicePre);
        fragmentTransaction.commit();
    }


    @OnClick(R.id.send)
    public void setSend(){
       serialPortFunction.sendEle("");
    }
    @OnClick(R.id.light_send)
    public void setLightSend(){
        serialPortFunction.sendLight("FE050000FF009835");
    }

    @OnClick(R.id.show)
    public void setShow(){
      serialPortFunction.startSerialReadEle(this);
    }

    @OnClick(R.id.close)
    public void setClose(){
        serialPortFunction.closeEle();
        serialPortFunction.closeLight();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void weight(Double weight) {
        System.out.println("Main2Activity weight"+weight);
        runOnUiThread(()->{
            textView.setText(weight.toString());
        });
    }
}
