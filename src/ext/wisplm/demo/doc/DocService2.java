package ext.wisplm.demo.doc;

import java.io.File;

import wt.doc.WTDocument;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import ext.wisplm.util.DocUtil;

public class DocService2 implements RemoteAccess{
	private static final String CLASS_NAME = DocService2.class.getName();

	public static void main(String[] args) throws Exception {
//		try {
//			RemoteMethodServer.getDefault().setUserName("wcadmin");
//			RemoteMethodServer.getDefault().setPassword("wcadmin");
//			createDoc();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		RemoteMethodServer.getDefault().setUserName("wcadmin");
		RemoteMethodServer.getDefault().setPassword("wcadmin");
		RemoteMethodServer.getDefault().invoke(
                "createDoc", CLASS_NAME, null,
                new Class[] {},
                new Object[] {});
	}
	
	
	public static void createDoc() throws Exception{
//		if (!RemoteMethodServer.ServerFlag) {
//            RemoteMethodServer.getDefault().invoke(
//                    "createDoc", CLASS_NAME, null,
//                    new Class[] {},
//                    new Object[] {});
//            return;
//        }
		WTDocument doc = DocUtil.createDoc("JSWJ-002", "技术文件2", "测试", "C919", 
				"wt.doc.WTDocument|com.wisplm.wisplmdoc", 
				"", "", "/Default/技术文件/自定义技术文件", null);
		System.out.println("创建的文件对象=====》"+doc);
	}
	
}
