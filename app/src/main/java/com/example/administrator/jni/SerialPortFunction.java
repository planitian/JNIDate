package com.example.administrator.jni;

import android_serialport_api.SerialPort;

import static com.google.common.base.Preconditions.checkNotNull;

public class SerialPortFunction {
    private  volatile  static   SerialPortFunction serialPortFunction;
    private  ReadSerialPort readSerialPort;
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

    public void startSerialRead(SerialPort serialPort, ReadSerialPort.DataWeight dataWeight){
         readSerialPort=new ReadSerialPort(serialPort);
         readSerialPort.setDataWeight(dataWeight);
         readSerialPort.run();
    }

    public void close(){
        readSerialPort.isEixt=true;
        readSerialPort=null;
    }



}
