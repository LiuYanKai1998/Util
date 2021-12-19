package ext.wisplm.apiexercise.hcreatecaddoc;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

import wt.epm.EPMApplicationType;
import wt.epm.EPMAuthoringAppType;
import wt.epm.EPMContextHelper;
import wt.epm.EPMDocument;
import wt.epm.EPMDocumentType;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.folder.Folder;
import wt.folder.FolderEntry;
import wt.folder.FolderHelper;
import wt.inf.container.WTContainer;
import wt.inf.container.WTContainerRef;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.part.QuantityUnit;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.type.ClientTypedUtility;
import wt.type.TypeDefinitionReference;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;

public class CreateEPMDocument implements RemoteAccess{
	
	public static void main(String [] args) throws RemoteException, InvocationTargetException, WTException{
		String number 		 = args[0];
		String name 	     = args[1];
		String containerName = args[2];
		WTContainer container = getContainer(containerName);
		RemoteMethodServer rms = RemoteMethodServer.getDefault();
		Class cla[] = {String.class,String.class,String.class};
		Object obj[] = {number,name,containerName};
		rms.invoke("createEPMDocument", CreateEPMDocument.class.getName(), null, cla, obj);
		return;
	}
	
	/**
	 * 创建一个3D或2D CAD文档(EPMDocument)
	 * @throws WTPropertyVetoException
	 * @throws WTException
	 * @throws RemoteException
	 */
	public static void createEPMDocument(String number,String name,String containerName) throws WTPropertyVetoException, WTException, RemoteException{
		System.out.println("begin createEPMDocument....");
		
		//设置适用类型
		//参考src/wt/pem/EPMApplicationType_zh_CN.rbInfo
        EPMContextHelper.setApplication(EPMApplicationType.getEPMApplicationTypeDefault());
        System.out.println("001....");
        //设置文档类型:装配,零件,二维图(绘图)
        //装配:CADASSEMBLY
        //零件:CADCOMPONENT
        //绘图(二维图):CADDRAWING
        //更多类型参考src/wt/pem/EPMDocumentType_zh_CN.rbInfo
        EPMDocumentType epmType = EPMDocumentType.toEPMDocumentType("CADCOMPONENT"); //CATPart(零件)为EPMDocumentType.toEPMDocumentType("CADCOMPONENT");
        System.out.println("002....");
        //设计工具
        //参考src/wt/pem/EPMAuthoringAppTypeRB_zh_CN.rbInfo
        EPMAuthoringAppType appType = EPMAuthoringAppType.toEPMAuthoringAppType("PROE");
        System.out.println("003....");
        //创建
        EPMDocument epm = EPMDocument.newEPMDocument(number, name, appType, epmType, name,QuantityUnit.EA);
        System.out.println("004....");
       //设置容器
        WTContainer container = getContainer(containerName);
		System.out.println("containercontainer--->" + container);
        System.out.println("005....");
        WTContainerRef cref = WTContainerRef.newWTContainerRef(container);
        System.out.println("006....");
        epm.setContainerReference(cref);
        System.out.println("007....");
        epm.setDomainRef(container.getDefaultDomainReference());
        System.out.println("008....");
        //设置文件夹
        Folder folder = FolderHelper.service.getFolder("/Default/CAD文档", cref);
        System.out.println("009....");
        FolderHelper.assignLocation((FolderEntry) epm, folder);
        System.out.println("010....");
        //设置类型
        TypeDefinitionReference tdr = ClientTypedUtility.getTypeDefinitionReference("wt.epm.EPMDocument|com.pdmtest.DefaultEPMDocument");
        epm.setTypeDefinitionReference(tdr);
        System.out.println("011....");
        //持久化到数据库
        epm = (EPMDocument) PersistenceHelper.manager.save(epm);
        epm = (EPMDocument) PersistenceHelper.manager.refresh(epm);
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
