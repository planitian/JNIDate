package com.example.administrator.jni;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Locale;

public class Util {

    /**
     * @param hex 十六进制字符串
     * @return 二进制字符串
     */
    public static String hexStringToBinary(String hex) {
        hex = hex.toUpperCase();
        String result = "";
        int max = hex.length();
        for (int i = 0; i < max; i++) {
            char c = hex.charAt(i);
            switch (c) {
                case '0':
                    result += "0000";
                    break;
                case '1':
                    result += "0001";
                    break;
                case '2':
                    result += "0010";
                    break;
                case '3':
                    result += "0011";
                    break;
                case '4':
                    result += "0100";
                    break;
                case '5':
                    result += "0101";
                    break;
                case '6':
                    result += "0110";
                    break;
                case '7':
                    result += "0111";
                    break;
                case '8':
                    result += "1000";
                    break;
                case '9':
                    result += "1001";
                    break;
                case 'A':
                    result += "1010";
                    break;
                case 'B':
                    result += "1011";
                    break;
                case 'C':
                    result += "1100";
                    break;
                case 'D':
                    result += "1101";
                    break;
                case 'E':
                    result += "1110";
                    break;
                case 'F':
                    result += "1111";
                    break;
            }
        }
        return result;
    }

    /**
     * 将16进制字符串转换为byte[]
     *
     * @param str
     * @return
     */
    public static byte[] toBytes(String str) {
        if (str == null || str.trim().equals("")) {
            return new byte[0];
        }
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }

    public static String bytesToHexFun3(byte[] bytes) {
        StringBuilder buf = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) { // 使用String的format方法进行转换
            buf.append(String.format("%02x", b & 0xff));
        }
        return buf.toString();
    }

    public static String bytesToHexString(byte[] bArray) {
        StringBuilder sb = new StringBuilder(bArray.length);
        String sTemp;
        for (byte aBArray : bArray) {
            sTemp = Integer.toHexString(0xFF & aBArray);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 字节数组转为普通字符串（ASCII对应的字符）
     *
     * @param bytearray byte[]
     * @return String
     */
    public static String bytetoString(byte[] bytearray) {
        String result = "";
        char temp;

        int length = bytearray.length;
        for (int i = 0; i < length; i++) {
            temp = (char) bytearray[i];
            result += temp;
        }
        return result;
    }


    /**
     * @param b 字节
     * @return 8位型的二进制字符串
     */
    public static String zeroize(byte b) {
        String binaryString = Integer.toBinaryString(b);
        String str = "00000000";
        return str.substring(binaryString.length()) + binaryString;
    }


    /**
     * @param src      源数组
     * @param specific 动态参数
     * @return 返回源数组同等类型的数组
     */
    @SuppressWarnings("unchecked")
    public   static  <T> T[] arrayAdd(T[] src, T... specific) {
//返回类的组件类型的数组。如果这个类并不代表一个数组类，此方法返回null。
        Class<?> type = src.getClass().getComponentType();
        T[] temp = (T[]) Array.newInstance(type, src.length + specific.length);
        System.arraycopy(src, 0, temp, 0, src.length);
        System.arraycopy(specific, 0, temp, src.length, specific.length);
        return temp;
    }

    /**
     * @param content  要删除内容的数组
     * @param specific 删除的内容
     * @return 删除指定内容后的数组
     */
    public  static  <T> T[] arraySpeDel(T[] content, T specific) {

        int len = content.length;
        for (int i = 0; i < content.length; i++) {
            if (content[i].equals(specific)) {
                System.arraycopy(content, i + 1, content, i, len - 1 - i);
                break;
            }
        }
        return Arrays.copyOf(content, len - 1);
    }



}
