package ext.wisplm.apiexercise.abasicquery;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.List;

import wt.doc.WTDocument;
import wt.fc.PersistenceHelper;
import wt.fc.PersistenceManager;
import wt.fc.QueryResult;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.util.WTException;

public class BasicQuery implements RemoteAccess{
	
	
	public static void main(String [] args) throws WTException, RemoteException, InvocationTargetException{
		String name   = args[0];
		String number = args[1];
		System.out.println("名称:" + name);
		System.out.println("编号:" + number);
		//queryDocuments(name);
		RemoteMethodServer rms = RemoteMethodServer.getDefault();
		rms.setUserName("user1");
		rms.setPassword("123");
		rms.invoke(
					"queryDocuments", 
			 		BasicQuery.class.getName(),
					null,
					new Class[]{String.class,String.class},
					new Object[]{name,number}
				  );
		
	}
	
	public static void queryDocuments(String name,String number) throws WTException{
		//SQL写法:select * from wtdocument;
		//一、指定要查询的对象类型,传递的参数为Class对象
		QuerySpec qs = new QuerySpec(WTDocument.class);
		//1.1构建查询条件
		name = name.replace("*", "%");
		SearchCondition nameSC = new SearchCondition(WTDocument.class,WTDocument.NAME,SearchCondition.LIKE,name);
		//1.2整合到QuerySpec
		qs.appendWhere(nameSC);
		
		number = number.replace("*", "%").toUpperCase();
		//1.3构建编号模糊查询的条件
		SearchCondition numberSC = new SearchCondition(WTDocument.class,WTDocument.NUMBER,SearchCondition.LIKE,number);
		qs.appendAnd();
		qs.appendWhere(numberSC);
		
		
		System.out.println("查询所有文档的sql:");
		System.out.println(qs);
		
		//windchill中搜索相关的api存储在包wt.query
		//windchill中持久化对象处理的相关api存储在包wt.fc
		//二、初始化持久化对象管理类PersistenceManager
		PersistenceManager pm = PersistenceHelper.manager;
		//三、执行查询,接收查询结果
		QueryResult qr = pm.find(qs);
		System.out.println("共找到文档:" + qr.size());
		List resultList = qr.getObjectVectorIfc().getVector();
		
		for(int i = 0 ; i < resultList.size();i ++){
			WTDocument doc = (WTDocument) resultList.get(i);
			System.out.println(doc.getDisplayIdentity());
		}
	}
	
	
	

}
