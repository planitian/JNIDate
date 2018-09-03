package com.example.administrator.jni;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.NavUtils;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortFinder;

import static android.content.Context.MODE_PRIVATE;
import static com.google.common.base.Preconditions.checkNotNull;

public class SerialPortFunction {
    private  volatile  static   SerialPortFunction serialPortFunction;
    private  ReadSerialPort readSerialPort;
    private ExecutorService executorService;


    private Context context;
    private SerialPort eleSerial;
    private SerialPort lightSerial;
    //双重锁定 避免多线程情况下发生错误
    public static SerialPortFunction getInstance(){
        if (serialPortFunction==null){
            synchronized (SerialPortFunction.class){
                if (serialPortFunction==null){
                    serialPortFunction=new SerialPortFunction();
                }
            }
        }
        return serialPortFunction;
    }

    private SerialPortFunction(){
        executorService= Executors.newCachedThreadPool();
    }

    public void setContext(Context context) {
        this.context = context;
    }

  //开始电子秤读取
    public void startSerialReadEle( ReadSerialPort.DataWeight dataWeight){
        if (eleSerial==null){
            try {
                eleSerial=getSerialPort("ele");
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        readSerialPort=new ReadSerialPort(eleSerial);
        readSerialPort.setDataWeight(dataWeight);
        executorService.submit(readSerialPort);
    }
  //发送电子秤指令
    public void sendEle(final String command){
        Runnable ele = () -> {
            if (eleSerial==null){
                try {
                    eleSerial=getSerialPort("ele");
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                eleSerial.getOutputStream().write(Util.toBytes(command));
                eleSerial.getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        executorService.submit(ele);
    }
   //发送灯指令
    public void sendLight(final String conmand){
        Runnable runnable = () -> {
            if (lightSerial==null){
                try {
                    lightSerial=getSerialPort("light");
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                lightSerial.getOutputStream().write(Util.toBytes(conmand));
                lightSerial.getOutputStream().flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        executorService.submit(runnable);
    }

    //关闭电子秤
    public void closeEle(){
        if (readSerialPort!=null){
            readSerialPort.isEixt=true;
            readSerialPort=null;
        }
        if (eleSerial!=null){
            eleSerial=null;
        }
    }
    //关闭灯
   public  void closeLight(){
        if (lightSerial!=null){
            lightSerial.close();
            lightSerial=null;
        }
   }


    private SerialPort getSerialPort(String devicesName) throws SecurityException, IOException, InvalidParameterException, InterruptedException {
            /* Read serial port parameters */
            String path;
            int baudrate;
            if (devicesName.equals("ele")) {
                //电子称的
                SharedPreferences sp = context.getSharedPreferences("com.example.administrator.jni_preferences", MODE_PRIVATE);
                path = sp.getString("electronic_port", "");
                baudrate = Integer.decode(sp.getString("electronic_baudrates", "-1"));
            } else {
                //灯的
                SharedPreferences sp = context.getSharedPreferences("com.example.administrator.jni_preferences", MODE_PRIVATE);
                path = sp.getString("light_port", "");
                baudrate = Integer.decode(sp.getString("light_baudrates", "-1"));
            }
        System.out.println("SerialPortFunction  串口 "+path+ "波特率  :"+baudrate);
            /* Check parameters */
            if ( (path.length() == 0) || (baudrate == -1)) {
                throw new InvalidParameterException();
            }

//            /* Open the serial port */
//            Process su;
//            su = Runtime.getRuntime().exec("/system/xbin/su");
//            String cmd = "chmod 777 " + path + "\n"
//                    + "exit\n";
//				/*String cmd = "chmod 777 /dev/s3c_serial0" + "\n"
//				+ "exit\n";*/
//            su.getOutputStream().write(cmd.getBytes());
//            if ((su.waitFor() != 0)) {
//                throw new SecurityException();
//            }
//            System.out.println(">>>>>>>>>"+path+"  "+baudrate);


        return  new SerialPort(new File(path), baudrate, 0);
    }
}
