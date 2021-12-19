package ext.wisplm.apiexercise.fcreatepart;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.ptc.core.meta.common.TypeIdentifier;
import com.ptc.core.meta.common.impl.WCTypeIdentifier;
import com.ptc.windchill.enterprise.copy.server.CoreMetaUtility;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.folder.Folder;
import wt.folder.FolderHelper;
import wt.inf.container.WTContainer;
import wt.inf.container.WTContainerRef;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.part.QuantityUnit;
import wt.part.Source;
import wt.part.WTPart;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.type.TypeDefinitionReference;
import wt.type.TypedUtility;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;
import wt.vc.views.View;
import wt.vc.views.ViewHelper;
import wt.vc.views.ViewReference;

public class CreatePart implements RemoteAccess{
	
	
	public static void main(String [] args) throws RemoteException, InvocationTargetException{
		String number 		 = args[0];
		String name 	     = args[1];
		String containerName = args[2];
		String viewName      = args[3];
		String folder        = args[4];
		
		RemoteMethodServer rms = RemoteMethodServer.getDefault();
		Class cla[] = {String.class,String.class,String.class,String.class,String.class};
		Object obj[] = {number,name,containerName,viewName,folder};
		rms.invoke("createPart", CreatePart.class.getName(), null, cla, obj);
	}
	
	/**
	 * 创建部件
	 * @param number 编号
	 * @param name   名称
	 * @param containerName 容器名称
	 * @param viewName 视图名称
	 * @param isEndItem 是否为成品
	 * @param folderPath 存储的文件夹路径
	 * @return
	 * @throws WTException 
	 * @throws WTPropertyVetoException 
	 */
	public static WTPart createPart(
			String number,
			String name,
			String containerName,
			String viewName,
			String folderPath) throws WTException, WTPropertyVetoException{
		//创建WTPart(部件)对象
		WTPart part = WTPart.newWTPart();
		//设置部件类型
		part.setTypeDefinitionReference(getTypeDefinitionReference("wt.part.WTPart"));
		//设置名称
		part.setName(name.trim());
		//设置编号
		part.setNumber(number.trim().toUpperCase());
		//设置部件来源
		part.setSource(Source.getSourceDefault());
		//设置默认单位
		part.setDefaultUnit(QuantityUnit.getQuantityUnitDefault());
		//设置视图
		ViewReference vr = getPartViewReference("Planning");
		part.setView(vr);
		//设置是否为成品
		part.setEndItem(false);
		//设置存储容器
		WTContainer container = getContainer(containerName);
		WTContainerRef cref = WTContainerRef.newWTContainerRef(container);
		part.setContainerReference(cref);
		folderPath = folderPath == null ? "" : folderPath.trim();
		if (folderPath.length() > 0) {
			Folder folder = FolderHelper.service.getFolder(folderPath, cref);
			part.setContainerReference(cref);
			FolderHelper.assignLocation(part, folder);
		}
		WTContainer cont = cref.getReferencedContainerReadOnly();
		part.setDomainRef(cont.getDefaultDomainReference());
		//持久化到数据库
		part = (WTPart) PersistenceHelper.manager.save(part);
		part = (WTPart) PersistenceHelper.manager.refresh(part);
		return part;
	}

	public static TypeDefinitionReference getTypeDefinitionReference(
			String fullType) {
		TypeIdentifier typeidentifier = CoreMetaUtility
				.getTypeIdentifier(fullType);
		WCTypeIdentifier wctypeidentifier = (WCTypeIdentifier) typeidentifier;
		TypeDefinitionReference tdr = TypedUtility
				.getTypeDefinitionReference(wctypeidentifier.getTypename());
		return tdr;
	}

	/**
	 * 获取视图对象
	 */
	public static ViewReference getPartViewReference(String viewStr) {
		ViewReference vrs = null;
		try {
			View aview = ViewHelper.service.getView(viewStr);
			vrs = ViewReference.newViewReference(aview);
		} catch (WTException ex) {
			ex.printStackTrace();
		}
		return vrs;
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
