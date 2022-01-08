package com.cn.boot.sample.iotdb.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.iotdb.session.pool.SessionPool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Chen Nan
 */
@Slf4j
//@Configuration
public class IotDbDruidConfig {
    @Value("${spring.iotdb.username}")
    private String username;

    @Value("${spring.iotdb.password}")
    private String password;

    @Value("${spring.iotdb.driver-name}")
    private String driverName;

    @Value("${spring.iotdb.url}")
    private String url;

    @Value("${spring.iotdb.initial-size:20}")
    private int initialSize;

    @Value("${spring.iotdb.min-idle:10}")
    private int minIdle;

    @Value("${spring.iotdb.max-active:500}")
    private int maxActive;

    @Value("${spring.iotdb.max-wait:60000}")
    private int maxWait;

    @Value("${spring.iotdb.remove-abandoned:true}")
    private boolean removeAbandoned;

    @Value("${spring.iotdb.remove-abandoned-timeout:30}")
    private int removeAbandonedTimeout;

    @Value("${spring.iotdb.time-between-eviction-runs-millis:60000}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${spring.iotdb.min-evictable-idle-time-millis:300000}")
    private int minEvictableIdleTimeMillis;

    @Value("${spring.iotdb.test-while-idle:false}")
    private boolean testWhileIdle;

    @Value("${spring.iotdb.test-on-borrow:false}")
    private boolean testOnBorrow;

    @Value("${spring.iotdb.test-on-return:false}")
    private boolean testOnReturn;

    private static DruidDataSource iotDbDataSource;

    //使用阿里的druid连接池
    private Connection getConnection() {
        if (iotDbDataSource == null) {
            iotDbDataSource = new DruidDataSource();
            //设置连接参数
            iotDbDataSource.setUrl(url);
            iotDbDataSource.setDriverClassName(driverName);
            iotDbDataSource.setUsername(username);
            iotDbDataSource.setPassword(password);
            //配置初始化大小、最小、最大
            iotDbDataSource.setInitialSize(initialSize);
            iotDbDataSource.setMinIdle(minIdle);
            iotDbDataSource.setMaxActive(maxActive);
            //配置获取连接等待超时的时间
            iotDbDataSource.setMaxWait(maxWait);
            //连接泄漏监测
            iotDbDataSource.setRemoveAbandoned(removeAbandoned);
            iotDbDataSource.setRemoveAbandonedTimeout(removeAbandonedTimeout);
            //配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
            iotDbDataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
            iotDbDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
            //防止过期
            iotDbDataSource.setTestWhileIdle(testWhileIdle);
            iotDbDataSource.setTestOnBorrow(testOnBorrow);
            iotDbDataSource.setTestOnReturn(testOnReturn);
        }

        Connection connection = null;
        try {
            connection = iotDbDataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("iotDB getConnection失败: error={}", e.getMessage());
        }
        return connection;
    }

    public void insert(String sql) {
        log.info("iotDB insert sql={}", sql);
        Connection connection = getConnection();
        Statement statement = null;
        try {
            if (connection != null) {
                statement = connection.createStatement();
                long systemTime = System.currentTimeMillis();
                statement.execute(sql);
                log.info("执行IotDb的sql----[{}],时间：[{}]ms", sql, System.currentTimeMillis() - systemTime);
            }
        } catch (SQLException e) {
            log.error("iotDB insert失败: error={}", e.getMessage());
        } finally {
            close(statement, connection);
        }
    }

    public List<Map<String, Object>> query(String sql) {
        Connection connection = getConnection();
        Statement statement = null;
        List<Map<String, Object>> resultList = null;
        ResultSet resultSet = null;
        try {
            if (connection != null) {
                statement = connection.createStatement();
                long systemTime = System.currentTimeMillis();
                resultSet = statement.executeQuery(sql);
                log.info("查询IotDb的sql----[{}],时间：[{}]ms", sql, System.currentTimeMillis() - systemTime);
                resultList = outputResult(resultSet);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("iotDB query失败: error={}", e.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                log.error("iotDB resultSet关闭异常: error={}", e.getMessage());
            }
            close(statement, connection);
        }
        return resultList;
    }

    private List<Map<String, Object>> outputResult(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> resultList = new ArrayList<>();
        if (resultSet != null) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                Map resultMap = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String colunmName = metaData.getColumnLabel(i);
                    if (colunmName.indexOf('.') >= 0) {
                        colunmName = colunmName.substring(colunmName.lastIndexOf('.') + 1);
                    }
                    if (colunmName.indexOf(')') >= 0) {//过滤 函数()括号
                        colunmName = colunmName.substring(0, colunmName.lastIndexOf(')'));
                    }
                    if (colunmName.equals("Time")) {//时序库自带的时间格式转换
                        Long timeStamp = Long.parseLong(resultSet.getString(i));
                        if (timeStamp > 0) {
                            Date d = new Date(timeStamp);
                            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                            resultMap.put("Time", sf.format(d));
                        }
                    } else {
                        resultMap.put(colunmName, resultSet.getString(i));
                    }
                }
                resultList.add(resultMap);
            }
        }

        return resultList;
    }


    private void close(Statement statement, Connection connection) {
        try {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (Exception e) {
//            e.printStackTrace();
            log.error("iotDB close失败: error={}", e.getMessage());
        }
    }
}
