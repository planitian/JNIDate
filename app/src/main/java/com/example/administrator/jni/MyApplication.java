package com.example.administrator.jni;

import android.app.Application;
import android.content.SharedPreferences;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;

import android_serialport_api.SerialPort;
import android_serialport_api.SerialPortFinder;

public class MyApplication extends Application {
   public SerialPortFinder serialPortFinder=new SerialPortFinder();
   private SerialPort mSerialPort=null;
    public SerialPort getSerialPort() throws SecurityException, IOException, InvalidParameterException, InterruptedException {
        if (mSerialPort == null) {
            /* Read serial port parameters */
            SharedPreferences sp = getSharedPreferences("com.example.administrator.jni_preferences", MODE_PRIVATE);
            String path = sp.getString("DEVICE", "");
            int baudrate = Integer.decode(sp.getString("BAUDRATE", "-1"));

            /* Check parameters */
            if ( (path.length() == 0) || (baudrate == -1)) {
                throw new InvalidParameterException();
            }

            /* Open the serial port */
            Process su;
            su = Runtime.getRuntime().exec("/system/xbin/su");
            String cmd = "chmod 777 " + path + "\n"
                    + "exit\n";
				/*String cmd = "chmod 777 /dev/s3c_serial0" + "\n"
				+ "exit\n";*/
            su.getOutputStream().write(cmd.getBytes());
            if ((su.waitFor() != 0)) {
                throw new SecurityException();
            }
            System.out.println(">>>>>>>>>"+path+"  "+baudrate);
            mSerialPort = new SerialPort(new File(path), baudrate, 0);
        }
        return mSerialPort;
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
}
