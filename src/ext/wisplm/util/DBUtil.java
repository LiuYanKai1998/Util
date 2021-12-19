package ext.wisplm.util;


import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import wt.method.MethodContext;
import wt.method.RemoteMethodServer;
import wt.pom.Transaction;
import wt.pom.WTConnection;

/**
 * 用于Oracle数据库操作
 *
 */
public class DBUtil {

	private static final String CLASSNAME = DBUtil.class.getName();
    private static Logger logger          = Logger.getLogger(DBUtil.class);
    private static final boolean TEST     = true;

    public static WTConnection getWtConnection() throws RemoteException, InvocationTargetException {
    	if (!RemoteMethodServer.ServerFlag) {
            return (WTConnection) RemoteMethodServer.getDefault().invoke("getWtConnection", CLASSNAME,null,
                    new Class[]{},
                    new Object[]{});
        }
        try {
            WTConnection conn = (WTConnection) MethodContext.getContext().getConnection();
            return conn;
        } catch (Exception e) {
            logger.error("获取数据库连接失败", e);
        }
        return null;
    }

    public static void freeWTConnection(WTConnection wtconnection) {
        if (!wtconnection.isTransactionActive()) {
            MethodContext.getContext().freeConnection();
        }
    }
    
    public static Connection getTestConnection() throws ClassNotFoundException, SQLException{
    	Class.forName(oracle.jdbc.driver.OracleDriver.class.getName());
    	String url  = "jdbc:oracle:thin:@wisplm.com:1521:wind";
    	String user = "pdm10";
    	String password = "pdm10";
    	Connection conn = DriverManager.getConnection(url, user, password);
    	return conn;
    }
    
    
    /**
     * <p>获取Windchill数据库连接<p>
     * 
     * @author loong
     * @return
     * @throws Exception
     */
    public static Connection getConnection() throws Exception{
    	if(TEST){
    		return getTestConnection();
    	}
    	if (!RemoteMethodServer.ServerFlag) {
            return (Connection) RemoteMethodServer.getDefault().invoke("getConnection", CLASSNAME,null,
                    new Class[]{},new Object[]{});
        }
    	MethodContext mc = MethodContext.getContext();
    	return ((WTConnection) mc.getConnection()).getConnection();
    }
	/**
	 * <p>执行查询SQL<p>
	 * 
	 * @author loong
	 * @param sql 执行的查询SQL
	 * @return 查询结果集
	 * @throws Exception
	 */
	public static ResultSet executeQuery(String sql, Connection conn) throws Exception {
		logger.debug("executeSql>>>>>>>"+sql);
        PreparedStatement stmt= conn.prepareStatement(sql);
        return stmt.executeQuery(sql);
    }

    /**
     * <p>执行插入，执行int,Long,String,Double,Timestamp数据类型<p>
     *
     * @author loong
     * @param tableName 数据库表名
     * @param map  <字段名，值>
     * @return
     * @throws Exception
     */
    public static boolean insertWithConnection(String tableName, HashMap<String,Object> map) throws Exception {
        Iterator<Entry<String, Object>> iter = map.entrySet().iterator();
        //字段名
        String fields = "";
        //可变符号'?'
        String wh = "";
        ArrayList<Object> al = new ArrayList<Object>();
        while (iter.hasNext()) {
            Entry<String,Object> entry = (Entry<String,Object>) iter.next();
            if (!"".equals(fields)) {
            	fields += ",";
            	wh += ",";
            }
            fields += entry.getKey();
            wh += "?";
            al.add(entry.getValue());
        }
        return insertWithConnection(tableName, fields, wh, al);
    }
 
    private static boolean insertWithConnection(String tableName, String fields, String wh, ArrayList<Object> al) throws Exception {
        PreparedStatement stmt =null;
        boolean ret =false;
        String sql = "insert into " + tableName + "(" + fields + ") values (" + wh + ")";
        try{
        	Connection conn = getConnection();
            stmt = conn.prepareStatement(sql);
            for (int i = 0; i < al.size(); i++) {
                Object obj = al.get(i);
                if (obj instanceof String) {
                    stmt.setString(i + 1, (String) obj);
                }else if (obj instanceof Long) {
                    stmt.setLong(i + 1, ((Long) obj).longValue());
                }else if(obj instanceof Integer) {
                    stmt.setInt(i + 1, ((Integer) obj).intValue());
                }else if(obj instanceof Double){
                	stmt.setDouble(i+1, (Double)obj);
                }else if (obj instanceof Timestamp) {
                    stmt.setTimestamp(i + 1, (Timestamp) obj);
                }
            }
            int updateSize =  stmt.executeUpdate();
            if(updateSize > 0) {
                ret = true;
            }
            logger.debug("insert " + updateSize + " data");
        }finally{
            if(stmt != null){
                stmt.close();
            }
        }
        return ret;
    }
    
    /**
     * <p>执行插入，执行int,Long,String,Double,Timestamp数据类型<p>
     *
     * @author loong
     * @param tableName 数据库表名
     * @param map  <字段名，值>
     * @return
     * @throws Exception
     */
    public static boolean insertWithWTConnection(String tableName, HashMap<String,Object> map) throws Exception {
        Iterator<Entry<String, Object>> iter = map.entrySet().iterator();
        //字段名
        String fields = "";
        //可变符号'?'
        String wh = "";
        ArrayList<Object> al = new ArrayList<Object>();
        while (iter.hasNext()) {
            Entry<String,Object> entry = (Entry<String,Object>) iter.next();
            if (!"".equals(fields)) {
            	fields += ",";
            	wh += ",";
            }
            fields += entry.getKey();
            wh += "?";
            al.add(entry.getValue());
        }
        WTConnection conn = getWtConnection();
        return insertWithWTConnection(conn,tableName, fields, wh, al);
    }

    /**
     * <p>执行插入<p>
     *
     * @author loong
     * @param tableName 表名
     * @param fields 字段名
     * @param wh 可变符号'?'
     * @param al 对应值
     * @return
     * @throws Exception
     */   
    public static boolean insertWithWTConnection(WTConnection conn,String tableName, String fields, String wh, ArrayList<Object> al) throws Exception {
        PreparedStatement stmt =null;
        boolean ret =false;
        String sql = "insert into " + tableName + "(" + fields + ") values (" + wh + ")";
        try{
            stmt = conn.prepareStatement(sql);
            for (int i = 0; i < al.size(); i++) {
                Object obj = al.get(i);
                if (obj instanceof String) {
                    stmt.setString(i + 1, (String) obj);
                }else if (obj instanceof Long) {
                    stmt.setLong(i + 1, ((Long) obj).longValue());
                }else if(obj instanceof Integer) {
                    stmt.setInt(i + 1, ((Integer) obj).intValue());
                }else if(obj instanceof Double){
                	stmt.setDouble(i+1, (Double)obj);
                }else if (obj instanceof Timestamp) {
                    stmt.setTimestamp(i + 1, (Timestamp) obj);
                }
            }
            int updateSize =  stmt.executeUpdate();
            if(updateSize > 0) {
                ret = true;
            }
            logger.debug("insert " + updateSize + " data");
        }finally{
        	conn.freeStatement(sql, stmt, true);
        }
        return ret;
    }

    private static PreparedStatement insertPreparedStatement(String tableName, String fields, String wh, ArrayList<Object> al,WTConnection con) throws Exception {
        PreparedStatement stmt =null;

        stmt = con.prepareStatement("insert into " + tableName + "(" + fields + ") values (" + wh + ")");
        for (int i = 0; i < al.size(); i++) {
            Object obj = al.get(i);
            if (obj instanceof String) {
                stmt.setString(i + 1, (String) obj);
            }else if (obj instanceof Long) {
                stmt.setLong(i + 1, ((Long) obj).longValue());
            }else if(obj instanceof Integer) {
                stmt.setInt(i + 1, ((Integer) obj).intValue());
            }else if(obj instanceof Double){
            	stmt.setDouble(i+1, (Double)obj);
            }else if (obj instanceof Timestamp) {
                stmt.setTimestamp(i + 1, (Timestamp) obj);
            }
        }
        return stmt;
    }
    
    /**
     * <p>构造update语句并执行<p>
     *
     * @author loong
     * @param tableName 表名
     * @param fieldMap   更新字段 <字段名，值>
     * @param conditionMap  更新条件 <字段名，值>,无条件传入null
     * @return
     * @throws Exception
     */
    public static boolean executeUpdateByJdbc(String tableName, Map<String,Object> fieldMap, Map<String,Object> conditionMap) throws Exception {
    	return executeUpdateByJdbc(generateUpdateSql(tableName,fieldMap,conditionMap));
    }
    
    public static String generateUpdateSql(String tableName, Map<String,Object> fieldMap, Map<String,Object> conditionMap){
    	String sql = "update " + tableName + " set "; 
    	if(fieldMap != null && fieldMap.size() > 0){
    		Set<String> fkeys = fieldMap.keySet();
    		int size = fkeys.size();
    		int i    = 0;
    		for (String key : fkeys) {
    			i++;
    			if(!"".equals(key) && !"TABLENAME".equalsIgnoreCase(key)){
    				Object obj = fieldMap.get(key);
    				if(obj instanceof String){
    					sql = sql + key + "='" + obj +"' ";
    					if(i != size - 1){
    						sql+=", ";
    					}
    				}
    				if (obj instanceof Long 
    						|| obj instanceof Integer
    						|| obj instanceof Double) {
    					sql = sql + key + "=" + obj +" ";
    					if(i != size - 1){
    						sql+=", ";
    					}
    				}
    			}
    		}
    	}
    	if(conditionMap != null && conditionMap.size() > 0){
    		sql += " where ";
    		Set<String> ckeys = conditionMap.keySet();
    		int i = 0;
    		for (String key : ckeys) {
    			if(!"".equals(key)){
    				Object obj = conditionMap.get(key);
    				if(obj instanceof String){
    					sql = sql + key + "='" + obj +"'";
    				}
    				if (obj instanceof Long 
    						|| obj instanceof Integer 
    						|| obj instanceof Double) {
    					sql = sql + key + "=" + obj;
    				}
    				i++;
    			}
    			if(i < ckeys.size()){
    				sql += " and ";
    			}
    		}
    	}
    	logger.debug("update sql>>>>>>"+sql);
    	return sql;
    }
	/**
	 * <p>执行更新SQL<p>
	 *
	 * @author loong
	 * @param sql 执行的更新SQL
	 * @return
	 * @throws Exception
	 */
    public static boolean executeUpdateByJdbc(String sql) throws Exception {
    	logger.debug("executeSql>>>>>>>" +  sql);
    	Connection conn = getConnection();
    	return executeUpdateByJdbc(conn,sql);
    }
    
    public static void executeUpdateByJdbc(List<String> sqls) throws Exception {
    	logger.debug("executeSql>>>>>>>" +  sqls);
    	Connection conn = getConnection();
    	for(String sql : sqls){
    		executeUpdateByJdbc(conn,sql);
    	}
    }
    
    public static boolean executeUpdateByJdbc(Connection conn,String sql) throws Exception {
    	logger.debug("executeSql>>>>>>>" +  sql);
        PreparedStatement stmt =null;
        boolean ret = false;
        try{
            stmt= conn.prepareStatement(sql);
            int updateSize =  stmt.executeUpdate();
            if(updateSize > 0) {
                ret = true;
            }
        }finally{
            if(stmt!=null){
                stmt.close();
            }
        }
        return ret;
    }
    
    /**
     * 执行SQL语句
     *
     * @param sql
     * @return
     * @throws Exception 
     */
    public static int executeUpdateWithTransaction(String sql) throws Exception {
        return executeUpdateWithTransaction(sql, new Object[0],true);
    }

    /**
     * 通过WTConnection执行多个sql语句,并通过OOTB事物进行控制
     * @description
     * @param sqls
     * @throws Exception
     */
    public static void executeUpdateWithTransaction(List<String> sqls,boolean transaction) throws Exception {
    	Transaction tx = null;
    	try{
    		if(transaction){
        		tx = new Transaction();
        		tx.start();
    		}
        	for(String sql : sqls){
        		executeUpdateWithTransaction(sql, new Object[0],false);
        	}
        	if(transaction){
            	tx.commit();
            	tx = null;
        	}
    	}finally{
    		if(tx != null){
    			tx.rollback();
    		}
    	}
    }
    
    /**
     * 执行SQL语句
     *
     * @param sql
     * @param parameters
     *            参数
     * @return
     * @throws Exception 
     */
    public static int executeUpdateWithTransaction(String sql, Object[] parameters,boolean startTransaction) throws Exception {
        WTConnection con = null;
        try {
            con = getWtConnection();
            Transaction transaction = null;
            if (con != null) {
            	if(startTransaction){
                    transaction = new Transaction();
                    transaction.start();
            	}

                PreparedStatement stmt = null;
                try {
                    stmt = prepareStatement(sql, parameters, con);
                    int result = stmt.executeUpdate();
                	if(startTransaction){
                        transaction.commit();
                        transaction = null;
                	}
                    return result;
                } catch (SQLException e) {
                    logger.error("执行sql语句错误：" + sql, e);
                    throw e;
                } finally {
                    if (stmt != null)
                        con.freeStatement(sql, stmt, true);
                    if (transaction != null) {
                        transaction.rollback();
                        logger.debug("执行以下语句取消事务：" + sql);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("执行sql语句错误：" + sql, e);
            throw e;
        }
        return -1;
    }

    /**
     * 使用sql语句和参数创建statement
     *
     * @param sql
     * @param parameters
     * @param con
     * @return
     * @throws SQLException
     */
    private static PreparedStatement prepareStatement(String sql, Object[] parameters, WTConnection con)
            throws SQLException {
        PreparedStatement stmt;
        stmt = con.prepareStatement(sql);
        for (int i = 1; i <= parameters.length; i++) {
            Object obj = parameters[i - 1];
            if (obj instanceof String) {
                stmt.setString(i, (String) obj);
            }
            if (obj instanceof Timestamp) {
                stmt.setTimestamp(i, (Timestamp) obj);
            }
            if (obj instanceof Date) {
                stmt.setDate(i, (Date) obj);
            }
            if (obj instanceof Integer) {
                stmt.setInt(i, ((Integer) obj).intValue());
            }
            if (obj instanceof Double) {
                stmt.setDouble(i, ((Double) obj).doubleValue());
            }
        }
        return stmt;
    }

    /**
     * 查询一条数据返回Map
     *
     * @param sql
     * @return
     * @throws InvocationTargetException 
     * @throws RemoteException 
     */
    public static Map<String, Object> queryForMap(String sql) throws RemoteException, InvocationTargetException {
        WTConnection conn = getWtConnection();
        if (conn != null) {
            return queryForMap(conn, sql);
        }
        return null;
    }

    /**
     * 查询一条数据返回Map
     *
     * @param sql
     * @return
     */
    public static Map<String, Object> queryForMap(WTConnection conn, String sql) {
        if (conn == null)
            return null;
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                ResultSetMetaData meta = rs.getMetaData();

                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    map.put(meta.getColumnName(i).toLowerCase(), rs.getObject(i));
                }
                return map;
            }

        } catch (SQLException e) {
            logger.error("执行sql语句错误：" + sql, e);
            return null;
        } finally {
            if (stmt != null)
                conn.freeStatement(sql, stmt, true);
        }
        return null;
    }

    /**
     * 查询数据返回List<Map>
     *
     * @param sql
     * @return
     * @throws InvocationTargetException 
     * @throws RemoteException 
     */
    public static List queryForList(String sql) throws RemoteException, InvocationTargetException {
        WTConnection conn = getWtConnection();
        if (conn != null) {
            return queryForList(conn, sql);
        }
        return new ArrayList();
    }

    /**
     * 查询数据返回List<Map>
     *
     * @param sql
     * @return
     */
    public static List queryForList(WTConnection conn, String sql) {
        PreparedStatement stmt = null;
        List ResultList = new ArrayList();
        try {
            stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            List columnNames = null;
            while (rs.next()) {
                if (columnNames == null) {
                    columnNames = new ArrayList();
                    ResultSetMetaData meta = rs.getMetaData();
                    for (int i = 1; i <= meta.getColumnCount(); i++) {
                        columnNames.add(meta.getColumnName(i).toLowerCase());
                    }
                }
                Map map = new HashMap();

                for (int i = 0; i < columnNames.size(); i++) {
                    map.put(columnNames.get(i), rs.getObject((String) columnNames.get(i)));
                }
                ResultList.add(map);
            }

        } catch (SQLException e) {
            logger.error("执行sql语句错误：" + sql, e);
        } finally {
            if (stmt != null)
                conn.freeStatement(sql, stmt, true);
        }
        return ResultList;
    }

    /**
     * 查询一条数据返回数量
     *
     * @param sql
     * @return
     * @throws InvocationTargetException 
     * @throws RemoteException 
     */
    public static int queryForInt(String sql) throws RemoteException, InvocationTargetException {
        WTConnection conn = getWtConnection();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            logger.error("执行sql语句错误：" + sql, e);
            return 0;
        } finally {
            if (stmt != null)
                conn.freeStatement(sql, stmt, true);
        }
        return 0;
    }

    /**
     * 查询一条数据返回指定类的对象
     * 
     * @param sql
     * @return
     * @throws InvocationTargetException 
     * @throws RemoteException 
     */
    public static Object queryForObject(String sql, Class clazz) throws RemoteException, InvocationTargetException {
        return queryForObject(sql, new Object[0], clazz);
    }

    /**
     * 查询一条数据返回指定类的对象
     * 
     * @param sql
     * @return
     * @throws InvocationTargetException 
     * @throws RemoteException 
     */
    public static Object queryForObject(String sql, Object[] parameters, Class clazz) throws RemoteException, InvocationTargetException {
        WTConnection conn = getWtConnection();
        PreparedStatement stmt = null;
        try {
            stmt = prepareStatement(sql, parameters, conn);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Object obj = rs.getObject(1);
                if(clazz.isInstance(obj)) return obj;
            }

        } catch (SQLException e) {
            logger.error("执行sql语句错误：" + sql, e);
            return null;
        } finally {
            if (stmt != null)
                conn.freeStatement(sql, stmt, true);
        }
        return null;
    }

    /**
     * 查询数据返回List<Map>
     *
     * @param sql
     * @return
     * @throws InvocationTargetException 
     * @throws RemoteException 
     */
    public static List queryForObjectList(String sql) throws RemoteException, InvocationTargetException {
        WTConnection conn = getWtConnection();
        if (conn != null) {
            return queryForObjectList(conn, sql);
        }
        return new ArrayList();
    }

    /** 
     * query for object list.
     * @param sql 
     * @param parameters 
     * @return 
     * @throws InvocationTargetException 
     * @throws RemoteException 
     */
    public static List queryForObjectList(String sql,Object[] parameters) throws RemoteException, InvocationTargetException {
        WTConnection conn = getWtConnection();
        if (conn != null) {
            return queryForObjectList(conn, sql,parameters);
        }
        return new ArrayList();
    }

    /**
     * 查询数据返回List<Map>
     *
     * @param sql
     * @return
     */
    public static List queryForObjectList(WTConnection conn,String sql,Object[] parameters) {
            PreparedStatement stmt = null;
            List ResultList = new ArrayList();
            try {
                    if (parameters == null)
                            stmt = conn.prepareStatement(sql);
                    else
                            stmt = prepareStatement(sql,parameters,conn);
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                            ResultList.add(rs.getObject(1));
                    }
                    rs.close();
            } catch (SQLException e) {
                    logger.error("执行sql语句错误：" + sql, e);
            } finally {
                    if (stmt != null)
                            conn.freeStatement(sql, stmt, true);
            }
            return ResultList;
    }

    /**
     * 查询数据返回List<Map>
     *
     * @param sql
     * @return
     */
    public static List queryForObjectList(WTConnection conn, String sql) {
       return queryForObjectList(conn,sql,null); 
    }

    /**
     * 获取序列的下一个值
     *
     * @param sequence
     * @return
     * @throws InvocationTargetException 
     * @throws RemoteException 
     */
    public static int getSequenceNextValue(String sequence) throws RemoteException, InvocationTargetException{
        return queryForInt("select " + sequence + ".nextval seq from dual");
    }

    /**
     * 强制序列存在
     *
     * @param sequence
     * @throws Exception 
     */
    public static void forceSequenceExist(String sequence) throws Exception{
        if (queryForInt("select count(*) from user_sequences where sequence_name = '" + sequence.toUpperCase() + "'") <= 0) {
        	executeUpdateWithTransaction("create sequence " + sequence);
        }
    }

    /**
     * 获取分页SQL
     *
     * @param sql
     * @param begin
     * @param end
     * @return
     */
    public static String getPagingSql(String sql, int begin, int end){
        return "select * from (select rownum row_,t.* from (" + sql + ") t) where row_>" + begin
                + " and row_<=" + (end + 1);
    }

    /**
     * 按照分页起始、结束序号获取分页对象
     *
     * @param sql
     * @param begin
     * @param end
     * @return
     * @throws InvocationTargetException 
     * @throws RemoteException 
     */
    public static List getPagingList(String sql, int begin, int end) throws RemoteException, InvocationTargetException{
        int count = queryForInt("select count(*) from (" + sql + ")");
        if (count < begin)
            begin = count - count % (end - begin);
        // 负数则取最后一页
        if (begin < 0) {
            int size = end - begin;
            begin = count - count % (end - begin);
            end = begin + size;
        }
        return queryForList(getPagingSql(sql, begin, end));
    }
}
