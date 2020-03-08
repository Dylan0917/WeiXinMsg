package db;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCUtils {
    // 1.获取一个唯一的连接池对象,
    private static DataSource ds;
    // 2. 通过动态代码块完成对连接池对象的赋值
    static{
        try {
            // 2.1 创建Properties对象, 用于读取Druid技术连接池对象的配置文件
            Properties pro = new Properties();
            // 2.2 通过类加载器, 加载配置文件
            pro.load(JDBCUtils.class.getClassLoader().getResourceAsStream("druid.properties"));
            // 2.3 对连接池对象进行赋值
            ds = DruidDataSourceFactory.createDataSource(pro);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 获取连接池对象的方法
    public static DataSource getDataSource(){
        return ds;
    }
    // 获取连接对象的方法
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
    // 关闭流资源的方法
    public static void close(ResultSet rs, Connection conn, Statement...stmts){
        if (rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmts != null && stmts.length != 0){
            for (Statement stmt : stmts) {
                if (stmt != null){
                    try {
                        stmt.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        if (conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
