package com.example.administrator.jni;

import android.app.Application;
import android.graphics.Path;
import android.support.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Optional;

import android_serialport_api.SerialPort;

import static com.google.common.base.Preconditions.checkNotNull;

public class ReadSerialPort implements Runnable {
    public void setDataWeight(DataWeight dataWeight) {
        this.dataWeight = dataWeight;
    }

    //接口回调 用于发送数据
    private DataWeight dataWeight;


    private InputStream inputStream;
    private Optional<BufferedInputStream> optionalBufferedInputStream;
    volatile boolean isEixt = false;//决定是否推出线程
    private Optional<SerialPort> optionalSerialPort;
    private static ReadSerialPort readSerialPort;
    private byte[] bytes;
    private int amend = 0;//用于修正在读取输入流时候，数组的内容没有正确的排序
    private int count = 18; //字节数组的大小

    private int quInt = 5;//分度值取整，因为机子默认是5 ，所以下方未做解析
    private BigDecimal sign;//重量正负
    private boolean isNetWeight;//是否是净重
    private boolean isTare = false;//有无皮重。默认没有
    private boolean outScale = false;//是否显示重量超出量程范围 默认没有超出
    private boolean isStabilize = true;//重量是否稳定，默认稳定
    private BigDecimal dvalue = new BigDecimal(0);      //分度值
    private int weightMaxCount = 3;//重量判断三次，如果稳定输出
    private BigDecimal nowWeight = new BigDecimal(0);//实时重量
    private BigDecimal tempWeight = new BigDecimal(0);//缓存的重量，用于和实时重量进行比较
//    //单例模式
//   public static  ReadSerialPort  getSingleton(@NonNull SerialPort serialPort){
//         if (readSerialPort==null){
//             synchronized (ReadSerialPort.class){
//                 if (readSerialPort==null){
//                     readSerialPort=new ReadSerialPort();
//                 }
//             }
//         }
//         return readSerialPort;
//   }

    public ReadSerialPort(SerialPort serialPort) {
        this.optionalSerialPort = Optional.ofNullable(serialPort);
        bytes = new byte[count];
    }

    @Override
    public void run() {
        try {
            inputStream = optionalSerialPort.orElseThrow(() -> new NullPointerException("SerialPort元素不存在")).getInputStream();
            optionalBufferedInputStream = Optional.of(new BufferedInputStream(inputStream));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            isEixt = true;
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int len = 0;
        int temp;
        int tempTare;
        BufferedInputStream bufferedInputStream = optionalBufferedInputStream.orElseGet(() -> new BufferedInputStream(inputStream));
        while (!isEixt) {
            try {
                if (bufferedInputStream.available() > count - 1) {
                    len = bufferedInputStream.read(bytes, 0, count - amend - 1);
                    amend = 0;
                    if (len == -1)
                        break;
                    if (len == count && bytes[count - 1] == 10 && bytes[0] == 2) {
                        String SWA = Util.zeroize(bytes[1]);
                        switch (SWA.substring(0, 4)) {
                            case "001":
                                dvalue = new BigDecimal(10);
                                break;
                            case "010":
                                dvalue = new BigDecimal(1);
                                break;
                            case "011":
                                dvalue = new BigDecimal(0.1);
                                break;
                            case "100":
                                dvalue = new BigDecimal(0.01);
                                break;
                            case "101":
                                dvalue = new BigDecimal(0.001);
                                break;
                            default:
                                dvalue = new BigDecimal(0.001);
                                break;
                        }

                        String SWB = Util.zeroize(bytes[2]);
                        char[] chars = SWB.toCharArray();
                        //皮重
                        if (chars[0] == 0) {
                            isTare = true;
                        } else {
                            isTare = false;
                        }
                        //重量正负
                        if (chars[1] == 0) {
                            sign = new BigDecimal(1);
                        } else {
                            sign = new BigDecimal(-1);
                        }
                        //是否在量程范围内
                        if (chars[2] == 0) {
                            outScale = false;
                        } else {
                            outScale = true;
                        }
                        //重量是否稳定
                        if (chars[3] == 0) {
                            isStabilize = true;
                        } else {
                            isStabilize = false;
                        }

                        temp = (bytes[4] - 48) * 100000 + (bytes[5] - 48) * 10000 + (bytes[6] - 48) * 1000 + (bytes[7] - 48) * 100 + (bytes[8] - 48) * 10 + (bytes[9] - 48);
                        tempTare = (bytes[10] - 48) * 100000 + (bytes[11] - 48) * 10000 + (bytes[12] - 48) * 1000 + (bytes[13] - 48) * 100 + (bytes[14] - 48) * 10 + (bytes[15] - 48);
                        if (isTare) {
                            tempWeight = MoneyUtil.moneyMul(new BigDecimal(temp - tempTare), dvalue, sign);
                        } else {
                            tempWeight = MoneyUtil.moneyMul(new BigDecimal(temp), dvalue, sign);
                        }
                        //用于表示误差范围
                        BigDecimal upper = MoneyUtil.moneySub(tempWeight, new BigDecimal(0.005));
                        BigDecimal lower = MoneyUtil.moneyAdd(tempWeight, new BigDecimal(0.005));
                        if (MoneyUtil.moneyComp(nowWeight, upper) > 0 && MoneyUtil.moneyComp(nowWeight, lower) < 0) {
                            weightMaxCount--;
                            tempWeight = new BigDecimal(0);
                            if (weightMaxCount == 0 && isStabilize) {
                                nowWeight = tempWeight;
                                weightMaxCount = 3;
                                checkNotNull(dataWeight);
                                dataWeight.weight(nowWeight.doubleValue());
                            }
                        } else {
                            nowWeight = tempWeight;
                            tempWeight = new BigDecimal(0);
                        }

                    } else {
                        for (int i = 0; i < bytes.length; i++) {
                            if (bytes[i] == 10) {
                                amend = i;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    interface DataWeight {
        void weight(Double weight);
    }
}
