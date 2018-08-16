package com.example.administrator.jni;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main2Activity extends AppCompatActivity implements ReadSerialPort.DataWeight{

    @BindView(R.id.select)
    Button select;
    @BindView(R.id.send)
    EditText send;
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

   Handler handler=new Handler(){
       @Override
       public void handleMessage(Message msg) {
           super.handleMessage(msg);
           textView.setText(msg.obj.toString());
       }
   };

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
       serialPortFunction.sendEle("FE");
        Toast.makeText(this,"发送灯指令",Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.light_send)
    public void setLightSend(){
        String temp=send.getText().toString().trim();
        serialPortFunction.sendLight(temp);

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
        Message message=Message.obtain();
        message.obj=weight;
        handler.sendMessage(message);
    }
}
