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
        Runnable runnable = () -> BeanUtil.getInstance().startConnect(null,null,null);
        new Thread(runnable).start();
    }

    @OnClick(R.id.date_close)
    public void setDateClosea()  {
        BeanUtil.getInstance().closeConnect();
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
