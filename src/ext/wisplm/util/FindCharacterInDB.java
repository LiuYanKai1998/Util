package ext.wisplm.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import wt.admin.AdministrativeDomainHelper;
import wt.method.MethodContext;
import wt.method.RemoteMethodServer;
import wt.pom.WTConnection;
import wt.session.SessionAuthenticator;

/**
 * 通过穷举的方式查找数据库所有表所有字段中包含指定字符串的表名和字段名
 * 穷举会导致Oracle游标超限，因此先调整游标数alter system set open_cursors=60000,最大值65000多。
 * @description
 * @author ZhongBinpeng
 * @date 2020年3月19日
 */
public class FindCharacterInDB {

	public static void exec() throws Exception {
		if (!RemoteMethodServer.ServerFlag) {
			RemoteMethodServer.getDefault().invoke("exec", FindCharacterInDB.class.getName(), null, null, null);
			return;
		}
		MethodContext mc = MethodContext.getContext(Thread.currentThread());
		if (mc == null)
			mc = new MethodContext(null, null);
		if (mc.getAuthentication() == null) {
			SessionAuthenticator sa = new SessionAuthenticator();
			mc.setAuthentication(sa.setUserName(AdministrativeDomainHelper.ADMINISTRATOR_NAME));
		}
		Connection conn = ((WTConnection) mc.getConnection()).getConnection();
		String sql = "select DATA_TYPE,OWNER, TABLE_NAME, COLUMN_NAME from dba_tab_columns where OWNER = 'PDM10'";
		Statement state = conn.createStatement();
		ResultSet rs = state.executeQuery(sql);
		while (rs.next()) {
			String TABLE_NAME  = rs.getString("TABLE_NAME");
			String COLUMN_NAME = rs.getString("COLUMN_NAME");
			String DATA_TYPE = rs.getString("DATA_TYPE");
			if (!DATA_TYPE.equalsIgnoreCase("number") && !DATA_TYPE.equalsIgnoreCase("VARCHAR2")) {
				continue;
			}
			String sql1 = "select count(*) as A from " + TABLE_NAME + " where " + COLUMN_NAME + " like '%.AEGLink%' or "
					+ COLUMN_NAME + " like '%1776501%'";
			try {
				Connection conn2 = ((WTConnection) mc.getConnection()).getConnection();
				Statement state1 = conn.createStatement();
				ResultSet rs1 = state1.executeQuery(sql1);
				if (rs1.next()) {
					long num = rs1.getLong("A");
					// System.out.println("数量:" + num);
					if (num > 0) {
						System.out.println("找到包含AEGLink或1776501的数据库表:" + TABLE_NAME + "," + COLUMN_NAME);
						String deletesql = "delete from " + TABLE_NAME + " where " + COLUMN_NAME
								+ " like '%.AEGLink%' or " + COLUMN_NAME + " like '%1776501%'";
						Statement state3 = conn.createStatement();
						state3.execute(deletesql);
						state3.close();
					}
				}
				rs1.close();
				state1.close();
				// conn2.close();
			} catch (Exception e) {

			}
		}
	}
}
