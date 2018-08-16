package com.example.datebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.datebase.bean.GoodsWeight;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.date_connect)
    Button dateConnect;
    @BindView(R.id.search)
    TextView search;
    @BindView(R.id.send_message)
    Button sendMessage;
    @BindView(R.id.date_close)
    Button dateClose;
    @BindView(R.id.date_show)
    TextView dateShow;

    private Connection connection;
    private Optional<Statement> optionalStatement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.date_connect)
    public void setDateConnect(){
        /*new Thread(()->{
            try {
                Class<?> date=Class.forName("com.mysql.jdbc.Driver");
                connection= DriverManager.getConnection("jdbc:mysql:"+"//192.168.1.40:3306/cclcloud?useUnicode=true&characterEncoding=utf-8&Pooling=true","zzpos","123456");
                System.out.println("success"+connection.isClosed()+connection.isReadOnly());
                optionalStatement=Optional.ofNullable(connection.createStatement());
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }).start();*/
        Runnable runnable = () -> BeanUtil.getInstance().startConnect(null,null,null);
        new Thread(runnable).start();
    }

    @OnClick(R.id.date_close)
    public void setDateClosea()  {
        BeanUtil.getInstance().closeConnect();

    /*    Runnable runnable = () -> {
            Statement statement=optionalStatement.orElseGet(()->{
                try {
                    return connection.createStatement();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
           return null; });
            String sql = "select * from t_d_goodsweight limit 2";
            assert statement != null;
            ResultSet result = null;
            ResultSetMetaData metaData;
            try {
                result = statement.executeQuery(sql);
//                    System.out.println(result.getString(4)+result.getObject(4));


                //单个
//                    GoodsWeight goodsWeight=BeanUtil.single(result,GoodsWeight.class);
//                    assert goodsWeight != null;
//                    System.out.println(goodsWeight.toString());

                //集合

              *//*  List<GoodsWeight> re=BeanUtil.multitude(result,GoodsWeight.class);
                if (re!=null) {
                    re.forEach(goodsWeight -> {
                        System.out.println("main"+goodsWeight.toString());
                    });
                }

               String issuccess= BeanUtil.updatePre(re.get(0),(update)->{
                    update.setWeight(new BigDecimal(10));
                    update.setQty(100);
                     return update;
                });
                statement.execute(issuccess);
                Snackbar.make(dateClose,issuccess+"",Snackbar.LENGTH_SHORT).show();*//*

              *//*  metaData=result.getMetaData();
                int count=metaData.getColumnCount();
                for (int i = 0; i <count ; i++) {
                        Log.d("字段名",metaData.getColumnName(i+1));
                    System.out.println(">>>>>"+metaData.getColumnTypeName(1));
                        Log.d("字段类型", String.valueOf(metaData.getColumnType(i+1)));
                    System.out.println("字段label    "+metaData.getColumnLabel(i+1));
                }
               StringBuilder str= new StringBuilder();
                while (result.next()){
                    System.out.println(result.getString(4)+result.getObject(4));
                }*//*
            } catch (SQLException e) {
                e.printStackTrace();
            }
        };
      new Thread(runnable).start();*/
    }

    @OnClick(R.id.send_message)
    public void setDateShow(){
        Runnable runnable = () -> {
            GoodsWeight goodsWeight=BeanUtil.getInstance().singleSql("0006b438556311e7bf5eecd68a02f686",GoodsWeight.class);
            System.out.println(goodsWeight.toString());
            BeanUtil.getInstance().update(goodsWeight,(par)->{
                par.setQty(10000);
                return par;
            });
            BeanUtil.getInstance().delete(goodsWeight);
        };
        new Thread(runnable).start();
    }
}
