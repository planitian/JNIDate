package com.example.datebase.bean;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class GoodsWeight  implements GoodNull,Cloneable,Serializable {
    private String id;
    private String barcode;
    private int qty;
    private BigDecimal weight;
    private  String approvedflag;
    private int deviationtype;
    private BigDecimal abs_deviation;
    private BigDecimal per_deviation;
    private String goodsattr;
    private int restype;
    private int expirydays;
    private String subcode;
    private String remark;
    private String state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getApprovedflag() {
        return approvedflag;
    }

    public void setApprovedflag(String approvedflag) {
        this.approvedflag = approvedflag;
    }

    public int getDeviationtype() {
        return deviationtype;
    }

    public void setDeviationtype(int deviationtype) {
        this.deviationtype = deviationtype;
    }

    public BigDecimal getAbs_deviation() {
        return abs_deviation;
    }

    public void setAbs_deviation(BigDecimal abs_deviation) {
        this.abs_deviation = abs_deviation;
    }

    public BigDecimal getPer_deviation() {
        return per_deviation;
    }

    public void setPer_deviation(BigDecimal per_deviation) {
        this.per_deviation = per_deviation;
    }

    public String getGoodsattr() {
        return goodsattr;
    }

    public void setGoodsattr(String goodsattr) {
        this.goodsattr = goodsattr;
    }

    public int getRestype() {
        return restype;
    }

    public void setRestype(int restype) {
        this.restype = restype;
    }

    public int getExpirydays() {
        return expirydays;
    }

    public void setExpirydays(int expirydays) {
        this.expirydays = expirydays;
    }

    public String getSubcode() {
        return subcode;
    }

    public void setSubcode(String subcode) {
        this.subcode = subcode;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUpdate_name() {
        return update_name;
    }

    public void setUpdate_name(String update_name) {
        this.update_name = update_name;
    }

    public Timestamp getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(Timestamp update_date) {
        this.update_date = update_date;
    }

    public String getUpdate_by() {
        return update_by;
    }

    public void setUpdate_by(String update_by) {
        this.update_by = update_by;
    }

    public String getCreate_name() {
        return create_name;
    }

    public void setCreate_name(String create_name) {
        this.create_name = create_name;
    }

    public Timestamp getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Timestamp create_date) {
        this.create_date = create_date;
    }

    public String getCreate_by() {
        return create_by;
    }

    public void setCreate_by(String create_by) {
        this.create_by = create_by;
    }

    private String update_name;
    private Timestamp update_date;
    private String update_by;
    private String create_name;
    private Timestamp create_date;
    private String create_by;

    @Override
    public String toString() {
        return "GoodsWeight{" +
                "id='" + id + '\'' +
                ", barcode='" + barcode + '\'' +
                ", qty=" + qty +
                ", weight=" + weight +
                ", approvedflag='" + approvedflag + '\'' +
                ", deviationtype=" + deviationtype +
                ", abs_deviation=" + abs_deviation +
                ", per_deviation=" + per_deviation +
                ", goodsattr='" + goodsattr + '\'' +
                ", restype=" + restype +
                ", expirydays=" + expirydays +
                ", subcode='" + subcode + '\'' +
                ", remark='" + remark + '\'' +
                ", state='" + state + '\'' +
                ", update_name='" + update_name + '\'' +
                ", update_date=" + update_date +
                ", update_by='" + update_by + '\'' +
                ", create_name='" + create_name + '\'' +
                ", create_date=" + create_date +
                ", create_by='" + create_by + '\'' +
                '}';
    }



    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public GoodsWeight clone() throws CloneNotSupportedException {
        GoodsWeight clone = (GoodsWeight) super.clone();
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bout);
            out.writeObject(this);//注意writeObject  这个方法
            out.close();
            ByteArrayInputStream bin = new ByteArrayInputStream(bout.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bin);
            Object ret = in.readObject();
            in.close();
            return (GoodsWeight) ret;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
