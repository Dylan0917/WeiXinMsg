package db;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultUtils {

    private String sql;
    private int[] paramsTypes;
    private List parameters;

    public ResultUtils() {
//        paramsTypes = new int[0];
        parameters = new ArrayList();
    }

    public PreparedStatement createPreparedStatement(Connection conn)
            throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sql);
        setParameterValues(ps, parameters.toArray());
        return ps;
    }


    public void setParameterValues(PreparedStatement ps, Object[] parameters)
            throws SQLException {
        for (int i = 0; i < parameters.length; i++) {

            Object value = parameters[i];
            int type = this.paramsTypes[i];
            if (value == null) {
                ps.setNull(i + 1, type);
            } else {
                switch (type) {
                    // 布尔值
                    case Types.BOOLEAN:
                        if (value instanceof Boolean) {
                            ps.setBoolean(i + 1, ((Boolean) value).booleanValue());
                        } else {
                            ps.setObject(i + 1, value);
                        }
                        break;

                    // 字符串
                    case Types.CHAR:
                    case Types.VARCHAR:
                        if (value instanceof String) {
                            String s = (String) value;
                            ps.setString(i + 1, s);
                        } else {
                            ps.setObject(i + 1, value);
                        }
                        break;
                    // 数字
                    case Types.BIT:
                    case Types.TINYINT:
                    case Types.SMALLINT:
                    case Types.INTEGER:
                    case Types.BIGINT:
                    case Types.REAL:
                    case Types.FLOAT:
                    case Types.DOUBLE:
                    case Types.DECIMAL:
                    case Types.NUMERIC:
                        if (value instanceof Number) {
                            setNumericObject(ps, i + 1, (Number) value, type);
                        } else if (value instanceof String) {
                            setNumericObject(ps, i + 1,
                                    getNumber((String) value, type), type);
                        } else {
                            ps.setObject(i + 1, value);
                        }
                        break;

                    // 二进制流
                    case Types.BINARY:
                    case Types.VARBINARY:
                    case Types.LONGVARBINARY:

                    case Types.BLOB:
                    case Types.CLOB:
//                        setLobObject(ps, i + 1, value, type);
                        break;

                    // 日期日期
                    case Types.DATE:
                        if (value instanceof Date) {
                            ps.setDate(i + 1, (Date) value);
                        } else if (value instanceof java.util.Date) {
                            ps.setDate(i + 1, new Date(
                                    ((java.util.Date) value).getTime()));
                        } else {
                            ps.setObject(i + 1, value);
                        }
                        break;
                    case Types.TIMESTAMP:
                        if (value instanceof Timestamp) {
                            ps.setTimestamp(i + 1, (Timestamp) value);
                        } else if (value instanceof java.util.Date) {
                            ps.setTimestamp(i + 1, new Timestamp(
                                    ((java.util.Date) value).getTime()));
                        } else {
                            ps.setObject(i + 1, value);
                        }
                        break;
                    case Types.TIME:
                        if (value instanceof Time) {
                            ps.setTime(i + 1, (Time) value);
                        } else if (value instanceof Timestamp) {
                            Timestamp xT = (Timestamp) value;
                            ps.setTime(i + 1, new Time(xT.getTime()));
                        } else {
                            ps.setObject(i + 1, value);
                        }
                        break;
                    // 其他情况
                    default:
                        ps.setObject(i + 1, value);
                        break;
                }
            }
        }

    }
    /**
     * 根据具体参数获取参数Sql类型
     *
     * @param objs
     * @return
     */
    public static int[] getSqlTypes(Object... objs) throws Exception {
        if (objs == null || objs.length == 0) {
            return null;
        }                         // throws TsException
        int[] types = new int[objs.length];
        int i = 0;
        for (Object o : objs) {
            if (null == o) {
                throw new Exception("参数不允许为null，如果要传null请传空字符串！");
            } else if (o instanceof String) {
                types[i] = Types.VARCHAR;
            } else if (o instanceof Integer) {
                types[i] = Types.INTEGER;
            } else if (o instanceof Float) {
                types[i] = Types.FLOAT;
            } else if (o instanceof Double) {
                types[i] = Types.DOUBLE;
            } else if (o instanceof BigDecimal) {
                types[i] = Types.NUMERIC;
            } else if (o instanceof Date) {
                types[i] = Types.DATE;
            } else if (o instanceof Boolean) {
                types[i] = Types.BIT;
            } else if (o instanceof Byte) {
                types[i] = Types.TINYINT;
            } else if (o instanceof Short) {
                types[i] = Types.SMALLINT;
            } else if (o instanceof Long) {
                types[i] = Types.BIGINT;
            } else if (o instanceof Byte[]) {
                types[i] = Types.VARBINARY;
            } else if (o instanceof Time) {
                types[i] = Types.TIME;
            } else if (o instanceof Timestamp) {
                types[i] = Types.TIMESTAMP;
            } else if (o instanceof Blob) {
                types[i] = Types.BLOB;
            } else if (o instanceof Clob) {
                types[i] = Types.CLOB;
            } else if (o instanceof Array) {
                types[i] = Types.ARRAY;
            } else if (o instanceof Ref) {
                types[i] = Types.REF;
            } else if (o instanceof URL) {
                types[i] = Types.DATALINK;
            } else {
                types[i] = Types.VARCHAR;
            }
            i++;
        }
        return types;
    }

    public int[] getParamsTypes() {
        return paramsTypes;
    }

    public void setParamsTypes(int[] paramsTypes) {
        this.paramsTypes = paramsTypes;
    }
    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List getParameters() { return parameters;
    }

    public void setParameters(List parameters) { this.parameters = parameters;
    }

    private void setNumericObject(PreparedStatement ps, int parameterIndex,
                                  Number parameterObj, int type) throws SQLException {

        switch (type) {

            case Types.BIT:
            case Types.TINYINT:
            case Types.SMALLINT:
            case Types.INTEGER:
                ps.setInt(parameterIndex, parameterObj.intValue());
                break;

            case Types.BIGINT:
                ps.setLong(parameterIndex, parameterObj.longValue());
                break;

            case Types.REAL:
                ps.setFloat(parameterIndex, parameterObj.floatValue());
                break;

            case Types.FLOAT:
            case Types.DOUBLE:
                ps.setDouble(parameterIndex, parameterObj.doubleValue());
                break;

            case Types.DECIMAL:
            case Types.NUMERIC:
                if (parameterObj instanceof BigDecimal) {
                    ps.setBigDecimal(parameterIndex, (BigDecimal) parameterObj);
                } else if (parameterObj instanceof BigInteger) {
                    ps.setBigDecimal(parameterIndex, new BigDecimal(
                            (BigInteger) parameterObj));
                } else {
                    ps.setBigDecimal(parameterIndex,
                            new BigDecimal(parameterObj.doubleValue()));
                }
                break;
        }
    }

    private Number getNumber(String str, int type) {
        switch (type) {
            case Types.BIT:
                boolean b = "true".equalsIgnoreCase(str);
                return b ? new Integer(1) : new Integer(0);

            case Types.TINYINT:
            case Types.SMALLINT:
            case Types.INTEGER:
                return Integer.valueOf(str);

            case Types.BIGINT:
                return Long.valueOf(str);

            case Types.REAL:
                return Float.valueOf(str);

            case Types.FLOAT:
            case Types.DOUBLE:
                return Double.valueOf(str);

            case Types.DECIMAL:
            case Types.NUMERIC:
            default:
                return new BigDecimal(str);
        }
    }

    /**
     *
     * @param rs
     * @param cls
     * @return
     * @throws Exception
     */
    public static List resultSetToListBean(ResultSet rs, Class cls)throws Exception {
        //取得Method
        Method[] methods = cls.getDeclaredMethods();
        System.out.println(methods[0].getName());
        List lst = new ArrayList();
        // 用于获取列数、或者列类型
        ResultSetMetaData meta = rs.getMetaData();
        Object obj = null;
        while (rs.next()) {
            // 获取formbean实例对象
            obj = cls.newInstance(); // 用Class.forName方法实例化对象和new创建实例化对象是有很大区别的，它要求JVM首先从类加载器中查找类，然后再实例化，并且能执行类中的静态方法。而new仅仅是新建一个对象实例
            // 循环获取指定行的每一列的信息
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                // 当前列名
                String colName = meta.getColumnName(i);
                // 设置方法名
                String setMethodName = "set" + colName;
                setMethodName = setMethodName.replaceAll("_","");
                //遍历Method
                for (int j = 0; j < methods.length; j++) {
                    if (methods[j].getName().equalsIgnoreCase(setMethodName)) {
                        setMethodName = methods[j].getName();
                        System.out.println(setMethodName);
                        // 获取当前位置的值，返回Object类型
                        Object value = rs.getObject(colName);
                        if(value == null){
                            continue;
                        }
                        //实行Set方法
                        try {
                            //// 利用反射获取对象
                            //JavaBean内部属性和ResultSet中一致时候
                            Method setMethod = obj.getClass().getMethod(
                                    setMethodName, value.getClass());
                            setMethod.invoke(obj, value);
                        } catch (Exception e) {
                            //JavaBean内部属性和ResultSet中不一致时候，使用String来输入值。
                            e.printStackTrace();
                        }
                    }
                }
            }
            lst.add(obj);
        }
        return lst;
    }
    public static void resultSetToListMap(ResultSet rs) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
        ResultSetMetaData md = rs.getMetaData(); //获得结果集结构信息,元数据
        int columnCount = md.getColumnCount();   //获得列数
        while (rs.next()) {
            Map<String,Object> rowData = new HashMap<String,Object>();
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(md.getColumnName(i), rs.getObject(i));
            }
            list.add(rowData);
        }
    }
}
