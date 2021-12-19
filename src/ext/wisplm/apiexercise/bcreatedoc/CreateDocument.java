package ext.wisplm.apiexercise.bcreatedoc;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

import com.ptc.core.meta.common.TypeIdentifier;
import com.ptc.windchill.enterprise.copy.server.CoreMetaUtility;

import wt.doc.DepartmentList;
import wt.doc.DocumentType;
import wt.doc.WTDocument;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.inf.container.WTContainer;
import wt.inf.container.WTContainerRef;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;

public class CreateDocument implements RemoteAccess{
	
	public static void main(String [] args) throws RemoteException, InvocationTargetException{
		String number 		 = args[0];
		String name 	     = args[1];
		String containerName = args[2];
		
		RemoteMethodServer rms = RemoteMethodServer.getDefault();
		Class cla[] = {String.class,String.class,String.class};
		Object obj[] = {number,name,containerName};
		rms.invoke("createDoc", CreateDocument.class.getName(), null, cla, obj);
	}
	

	public static WTDocument createDoc(String number, String name,String containerName) throws Exception{
		//一、设置文档类型
		TypeIdentifier objType = CoreMetaUtility.getTypeIdentifier("wt.doc.WTDocument");
		WTDocument doc = (WTDocument) CoreMetaUtility.newInstance(objType);
		//二、设置文档编号
		doc.setNumber(number);
		//三、设置文档名称
		doc.setName(name);
		//四、设置其他默认信息
		doc.setDocType(DocumentType.getDocumentTypeDefault());
		doc.setDepartment(DepartmentList.getDepartmentListDefault());
		//五、设置存储容器
		WTContainerRef cref = WTContainerRef.newWTContainerRef(getContainer(containerName));
		doc.setContainerReference(cref);
		//
		doc.setDomainRef(cref.getReferencedContainerReadOnly()
				.getDefaultDomainReference());
		//持久化到数据库
		doc = (WTDocument) PersistenceHelper.manager.save(doc);
		//刷新缓存
		doc = (WTDocument) PersistenceHelper.manager.refresh(doc);
		if (doc != null) {
			System.out.println("文档创建成功:" + doc.getDisplayIdentity());
		} else {
			System.out.println("文档创建失败...");
		}
		return doc;
	}
	
	/**
	 * 通过容器名称查询容器对象
	 * @param containerName
	 * @return
	 * @throws WTException
	 */
	public static WTContainer getContainer(String containerName) throws WTException{
		QuerySpec qs = new QuerySpec(WTContainer.class);
		SearchCondition sc = new SearchCondition(WTContainer.class,WTContainer.NAME,SearchCondition.EQUAL,containerName);
		qs.appendWhere(sc);
		QueryResult qr = PersistenceHelper.manager.find(qs);
		return (WTContainer)qr.nextElement();
	}

}
