package com.example.datebase;

import android.support.annotation.NonNull;

import com.example.datebase.bean.GoodUpdate;
import com.example.datebase.bean.GoodsWeight;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BeanUtil {
    private GoodsWeight inGoodsWeight;
    private Optional<Statement> optionalStatement;
    private volatile static BeanUtil beanUtil;
    private volatile Connection connection;

    private BeanUtil() {

    }

    //获取单例
    public static BeanUtil getInstance() {
        if (beanUtil == null) {
            synchronized (BeanUtil.class) {
                if (beanUtil == null) {
                    beanUtil = new BeanUtil();
                }
            }
        }
        return beanUtil;
    }

    /**
     * @param url      数据库地址
     * @param name     数据库用户名字
     * @param passWord 密码
     * @return 返回连接是否成功
     */
    public boolean startConnect(String url, String name, String passWord) {
        Statement statement = Connect(url, name, passWord);
        if (statement != null) {
            optionalStatement = Optional.of(statement);
            return true;
        } else {
            return false;
        }
    }

    //关闭连接
    public void closeConnect() {
        try {
            if (connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        optionalStatement = Optional.empty();
        inGoodsWeight = null;
    }

    /**
     * @param id   要查询的重量
     * @param bean Bean类
     * @param <T>  泛型
     * @return Bean类的实例
     */
    //查询单个数据
    public <T> T singleSql(String id, Class<T> bean) {
        try {
            String sql = "select * from t_d_goodsweight where id=" + "\"" + id + "\"";
            ResultSet resultSet = optionalStatement.orElse(Connect(null, null, null)).executeQuery(sql);
            return single(resultSet, bean);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param sql  sql语句
     * @param bean
     * @param <T>
     * @return 查询集合
     */
    //查询多个数据
    public <T> List<T> multitudeSql(String sql, Class<T> bean) {
        try {
            ResultSet resultSet = optionalStatement.orElse(Connect(null, null, null)).executeQuery(sql);
            return multitude(resultSet, bean);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * @param goodsWeight 前面查询返回的实例
     * @param goodUpdate  回调
     * @return 是否成功
     */
    //更新单个数据
    public boolean update(GoodsWeight goodsWeight, GoodUpdate goodUpdate) {
        try {
            inGoodsWeight = goodsWeight.clone();
            String sql = updatePre(goodUpdate.update(goodsWeight));
            return optionalStatement.orElse(Connect(null, null, null)).execute(sql);
        } catch (CloneNotSupportedException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //删除数据
    public boolean delete(GoodsWeight goodsWeight) {
        String sql = "DELETE FROM t_d_goodsweight WHERE id =" + "\"" + goodsWeight.getId() + "\"";
        try {
            return optionalStatement.orElse(Connect(null, null, null)).execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    //添加数据
    public boolean add(GoodsWeight goodsWeight) {
        String sql=addPre(goodsWeight);
        try {
            return optionalStatement.orElse(Connect(null, null, null)).execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Statement Connect(String url, String name, String passWord) {
        if (url == null) {
            url = "jdbc:mysql://192.168.1.40:3306/cclcloud?useUnicode=true&characterEncoding=utf-8&Pooling=true";
        }
        if (name == null) {
            name = "zzpos";
        }
        if (passWord == null) {
            passWord = "123456";
        }
        try {
            Class<?> date = Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, name, passWord);
            System.out.println(">>>>>>>>>>." + connection.isClosed());
            return connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    private <T> T single(@NonNull ResultSet resultSet, @NonNull Class<T> bean) {
        try {
            //运用反射的构造方法创造T实例
            T temp = bean.getConstructor().newInstance();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            if (resultSet.next()) {
                for (int i = 0; i < resultSetMetaData.getColumnCount(); i++) {
                    //得到列的属性名字
                    String pro = resultSetMetaData.getColumnName(i + 1);
                    //得到传进来的类型所有的字段
                    Field field = bean.getDeclaredField(pro);
                    String setPro = "set" + pro.substring(0, 1).toUpperCase() + pro.substring(1);
                    //                System.out.println("创造的set方法" + setPro+"  方法中的参数 "+field.getType());
                    //得到方法  类型通过字段得到
                    Method tempMethod = bean.getMethod(setPro, field.getType());
                    //                System.out.println("读取resultSet中的值  "+resultSet.getObject(4));
                    //执行方法体
                    tempMethod.invoke(temp, resultSet.getObject(i + 1));
                }
            } else {
                throw new RuntimeException("resultSet异常");
            }
            return temp;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SQLException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    //根据resulSet 返回 集合
    private <T> List<T> multitude(@NonNull ResultSet resultSet, @NonNull Class<T> bean) {
        List<T> list = new ArrayList<>();
        try {

            ResultSetMetaData metaData = resultSet.getMetaData();
            int count = metaData.getColumnCount();
            while (resultSet.next()) {
                T temp = bean.getConstructor().newInstance();
                for (int i = 0; i < count; i++) {
                    String pro = metaData.getColumnName(i + 1);
                    Field field = bean.getDeclaredField(pro);
                    String setPro = "set" + pro.substring(0, 1).toUpperCase() + pro.substring(1);
                    Method tempMethod = bean.getMethod(setPro, field.getType());
                    tempMethod.invoke(temp, resultSet.getObject(i + 1));
                }
                list.add(temp);
            }
        /*    list.forEach((pa)->{
                System.out.println("Util"+pa.toString());
            });*/
            return list;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SQLException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }


    //用于生成修改sql语句
    private String updatePre(GoodsWeight goodsWeight) {
        StringBuilder sqlPre = new StringBuilder("update t_d_goodsweight set ");
        String sqlend = " WHERE id=" + "\"" + inGoodsWeight.getId() + "\"";
        if (inGoodsWeight != null) {
            Method[] methods = goodsWeight.getClass().getDeclaredMethods();
            StringBuilder builder = new StringBuilder();
            List<String> list = new ArrayList<>();
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().startsWith("get")) {
                    System.out.println("methodName " + methods[i].getName());
                    try {
                        Object out = methods[i].invoke(goodsWeight, null);
                        Object in = methods[i].invoke(inGoodsWeight, null);
                        String pro = methods[i].getName().toLowerCase().substring(3);
                        System.out.println("pro " + pro);
                        if (!out.toString().equals(in.toString())) {
                            builder.append(pro).append("=").append("\"").append(out).append("\"");
                            list.add(builder.toString());
                            builder.setLength(0);
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    sqlPre.append(list.get(i));
                } else {
                    sqlPre.append(",").append(list.get(i));
                }
            }

            String sql = sqlPre.toString() + sqlend;
            System.out.println("BeanUtil 的sql语句 " + sql);
            return sql;
        } else {
            throw new NullPointerException("请先调用 updatePre");
        }
    }

    private String addPre(GoodsWeight goodsWeight) {
        String sqlPre = "INSERT INTO t_d_goodsweight VALUES (";
        sqlPre = sqlPre + "\"" + goodsWeight.getBarcode() + "\"";
        sqlPre = sqlPre + ",\"" + goodsWeight.getQty() + "\"";
        sqlPre = sqlPre + ",\"" + goodsWeight.getWeight() + "\"";
        sqlPre = sqlPre + ",\"" + goodsWeight.getApprovedflag() + "\"";
        sqlPre = sqlPre + ",\"" + goodsWeight.getDeviationtype() + "\"";
        sqlPre = sqlPre + ",\"" + goodsWeight.getAbs_deviation() + "\"";
        sqlPre = sqlPre + ",\"" + goodsWeight.getPer_deviation() + "\"";
        sqlPre = sqlPre + ",\"" + goodsWeight.getGoodsattr() + "\"";
        sqlPre = sqlPre + ",\"" + goodsWeight.getRestype() + "\"";
        sqlPre = sqlPre + ",\"" + goodsWeight.getExpirydays() + "\"";
        sqlPre = sqlPre + ",\"" + goodsWeight.getSubcode()+ "\"";
        sqlPre = sqlPre + ",\"" + goodsWeight.getRemark()+ "\"";
        sqlPre = sqlPre + ",\"" + goodsWeight.getState()+ "\"";
        sqlPre = sqlPre + ",\"" + goodsWeight.getUpdate_name()+ "\"";
        sqlPre = sqlPre + ",\"" + goodsWeight.getUpdate_date()+ "\"";
        sqlPre = sqlPre + ",\"" + goodsWeight.getUpdate_by()+ "\"";
        sqlPre = sqlPre + ",\"" + goodsWeight.getCreate_name()+ "\"";
        sqlPre = sqlPre + ",\"" + goodsWeight.getCreate_date()+ "\"";
        sqlPre = sqlPre + ",\"" + goodsWeight.getCreate_by()+ "\"";
        String sql=sqlPre+");";
        return sql;
    }
}
