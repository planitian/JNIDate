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

public class Main2Activity extends AppCompatActivity implements ReadSerialPort.DataWeight {


    @BindView(R.id.content)
    FrameLayout content;
    @BindView(R.id.select)
    Button select;
    @BindView(R.id.ele_content)
    EditText eleContent;
    @BindView(R.id.ele_send)
    Button eleSend;
    @BindView(R.id.red_open)
    Button redOpen;
    @BindView(R.id.red_close)
    Button redClose;
    @BindView(R.id.green_open)
    Button greenOpen;
    @BindView(R.id.green_close)
    Button greenClose;
    @BindView(R.id.blue_open)
    Button blueOpen;
    @BindView(R.id.blue_close)
    Button blueClose;
    @BindView(R.id.light_close)
    Button lightClose;
    @BindView(R.id.show)
    TextView show;
    @BindView(R.id.ele_open)
    Button eleOpen;
    @BindView(R.id.ele_close)
    Button eleClose;
    private SerialPortFunction serialPortFunction;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            show.setText(msg.obj.toString());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        serialPortFunction = SerialPortFunction.getInstance();
        serialPortFunction.setContext(this);
    }

    //选择串口
    @OnClick(R.id.select)
    public void setSelect() {
        DevicePre devicePre = new DevicePre();
        devicePre.setContext(Main2Activity.this);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content, devicePre, "ss");
        fragmentTransaction.show(devicePre);
        fragmentTransaction.commit();
    }

    @OnClick(R.id.ele_open)
    public void setEleOpen() {
        serialPortFunction.startSerialReadEle(this::weight);
    }

    //电子秤发送
    @OnClick(R.id.ele_send)
    public void setEleSend() {
        serialPortFunction.sendEle(eleContent.toString().trim());
        popUp(eleContent.toString().trim());
    }

    @OnClick(R.id.ele_close)
    public void setEleClose() {
        serialPortFunction.closeEle();
    }

    //红灯开
    @OnClick(R.id.red_open)
    public void setRedOpen() {
        ligthCloseAll();
        String redOpen = "FE050000FF009835";
        popUp(redOpen);
        serialPortFunction.sendLight(redOpen);

    }

    //红灯关
    @OnClick(R.id.red_close)
    public void setRedClose() {
        String redClose = "FE0500000000D9C5";
        popUp(redClose);
        serialPortFunction.sendLight(redClose);
    }

    //绿灯开
    @OnClick(R.id.green_open)
    public void setGreenOpen() {
        ligthCloseAll();
        String greenOpen = "FE050002FF0039F5";
        popUp(greenOpen);
        serialPortFunction.sendLight(greenOpen);

    }

    //绿灯关
    @OnClick(R.id.green_close)
    public void setGreenClose() {
        String greenClose = "FE05000200007805";
        popUp(greenClose);
        serialPortFunction.sendLight(greenClose);
    }

    //蓝灯开
    @OnClick(R.id.blue_open)
    public void setBlueOpen() {
        ligthCloseAll();
        String blueOpen = "FE050001FF00C9F5";
        popUp(blueOpen);
        serialPortFunction.sendLight(blueOpen);
    }

    @OnClick(R.id.blue_close)
    public void setBlueClose() {
        String blueClose = "FE05000100008805";
        popUp(blueClose);
        serialPortFunction.sendLight(blueClose);
    }

    //灯串口关闭
    @OnClick(R.id.light_close)
    public void setLightClose() {
        serialPortFunction.closeEle();
    }


    public void popUp(String content) {
        Toast.makeText(this, content, Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void weight(String weight) {
        System.out.println("Main2Activity weight" + weight);
        Message message = Message.obtain();
        message.obj = weight;
        handler.sendMessage(message);
    }

    //关闭所有灯
    public void ligthCloseAll() {
        redClose.performClick();
        blueClose.performClick();
        greenClose.performClick();
    }
}
