package ext.wisplm.demo.doc;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import wt.doc.WTDocument;
import wt.fc.ReferenceFactory;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import ext.wisplm.util.DocUtil;

public class DocService6 implements RemoteAccess{
	private static final String CLASS_NAME = DocService6.class.getName();

	public static void main(String[] args) throws Exception {
		try {
			RemoteMethodServer.getDefault().setUserName("wcadmin");
			RemoteMethodServer.getDefault().setPassword("wcadmin");
			//createDoc();
			
			createWtdocumentdependencylink();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static void createDoc() throws Exception{
		if (!RemoteMethodServer.ServerFlag) {
            RemoteMethodServer.getDefault().invoke(
                    "createDoc", CLASS_NAME, null,
                    new Class[] {},
                    new Object[] {});
            return;
        }
		WTDocument doc = DocUtil.createDoc("JSWJ-005", "技术文件5", "测试", "C919", 
				"wt.doc.WTDocument|com.wisplm.wisplmdoc", 
				"", "", "/Default/技术文件/自定义技术文件", null);
		System.out.println("创建的文件对象=====》"+doc);
		File file = new File("D:/ptc/Windchill_10.2/Windchill/temp/JSWJ-005.txt");
		DocUtil.addApplicationDataToDoc(doc,file,true);
		
		DocUtil.downloadPrimary(doc, "D:/");
		
	}
	
	
	
	public static void createWtdocumentdependencylink() throws Exception{
		if (!RemoteMethodServer.ServerFlag) {
            RemoteMethodServer.getDefault().invoke(
                    "createWtdocumentdependencylink", CLASS_NAME, null,
                    new Class[] {},
                    new Object[] {});
            return;
        }
		ReferenceFactory rf = new ReferenceFactory();
		WTDocument doc = (WTDocument)rf.getReference("OR:wt.doc.WTDocument:134836").getObject();
		Set set = new HashSet();
		WTDocument doc1 = (WTDocument)rf.getReference("OR:wt.doc.WTDocument:134902").getObject();
		set.add(doc1);
		DocUtil.createDependencyLinks(doc1, set);
		
	}
	
}
