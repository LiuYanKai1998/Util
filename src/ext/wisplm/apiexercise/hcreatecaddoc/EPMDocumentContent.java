package ext.wisplm.apiexercise.hcreatecaddoc;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.Vector;

import wt.content.ApplicationData;
import wt.content.ContentHelper;
import wt.content.ContentHolder;
import wt.content.ContentItem;
import wt.content.ContentRoleType;
import wt.content.ContentServerHelper;
import wt.content.ContentServiceSvr;
import wt.content.FormatContentHolder;
import wt.doc.WTDocument;
import wt.epm.EPMDocument;
import wt.fc.PersistenceHelper;
import wt.fc.PersistenceManager;
import wt.fc.QueryResult;
import wt.inf.container.WTContainer;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.part.PartDocHelper;
import wt.part.WTPart;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.util.WTException;

public class EPMDocumentContent implements RemoteAccess{
		
	
	public static void main(String [] args) throws RemoteException, InvocationTargetException, WTException{
		String number 		 	 = args[0];
		String filePath 	     = args[1];
		RemoteMethodServer rms = RemoteMethodServer.getDefault();
		Class cla[] = {String.class,String.class};
		Object obj[] = {number,filePath};
		rms.invoke("uploadEPMPrimaryContent", EPMDocumentContent.class.getName(), null, cla, obj);
		return;
	}
	
	public static void uploadEPMPrimaryContent(String number,String filePath) throws WTException, PropertyVetoException, FileNotFoundException, IOException{
		EPMDocument doc = queryEPMDocumentByNumber(number);
    	File file = new File(filePath);
        if(!file.exists()){
        	System.out.println("未找到本地文件:" + filePath + ",无法上传");
        	return;
        }
        wt.content.ContentHolder ch = wt.content.ContentHelper.service.getContents(doc);
        //首先删除文档已存在的主内容对象
		ContentServiceSvr service = ContentServerHelper.service;
		ContentItem item  = ContentHelper.service.getPrimary((FormatContentHolder) ch);
		if(item != null){
			System.out.println("找到EPM文档已存在的主内容,删除...");
			ContentServerHelper.service.deleteContent(ch,item);
			System.out.println("删除EPM文档主内容成功...");
		}
		
        
        //构建ApplicationData对象,用于存储实体文件信息
        ApplicationData applicationdata = ApplicationData.newApplicationData(doc);
        //定义要上传的文件名
        applicationdata.setFileName(file.getName());
        applicationdata.setFormatName("Pro/ENGINEER UGC");
        //定义要上传的文件所在路径
        applicationdata.setUploadedFromPath(file.getParent());
        applicationdata.setRole(ContentRoleType.PRIMARY);
        ContentServerHelper.service.updateContent(ch, applicationdata,file.getPath());
        System.out.println("EPM文档主内容上传成功,:" + doc.getDisplayIdentity());
	}
	
	public static EPMDocument queryEPMDocumentByNumber(String number) throws WTException{
		QuerySpec qs = new QuerySpec(EPMDocument.class);
		number = number.toUpperCase();
		SearchCondition numberSC = new SearchCondition(EPMDocument.class,WTDocument.NUMBER,SearchCondition.EQUAL,number);
		qs.appendWhere(numberSC);
		//构建查找最新版的配置规范
		wt.vc.config.LatestConfigSpec latestconfigspec = new wt.vc.config.LatestConfigSpec();
		//添加到查询条件
        latestconfigspec.appendSearchCriteria(qs);
		PersistenceManager pm = PersistenceHelper.manager;
		QueryResult qr = pm.find(qs);
		System.out.println("找到编号为" + number+"的文档数:"  + qr.size());
		//过滤出最新版
        qr = latestconfigspec.process(qr);
		if(qr.size() > 0){
			return (EPMDocument)qr.nextElement();
		}
		return null;
	}
	
	public static void downLoadEpmDocumentContent(EPMDocument epm) throws WTException, PropertyVetoException, IOException{
		ContentHolder holder  = wt.content.ContentHelper.service.getContents(epm);
		//获取附件集
        Vector vec = wt.content.ContentHelper.getApplicationData(holder);
        //获取主内容
        if (holder instanceof FormatContentHolder) {
            ContentItem contentItem = wt.content.ContentHelper.getPrimary((FormatContentHolder) holder);
            if (contentItem != null && contentItem instanceof ApplicationData) {
                vec.addElement(contentItem);
            }
        }
        //开始全部下载
        for(int i = 0 ; i < vec.size(); i ++){
        	ApplicationData app = (ApplicationData) vec.get(i);
        	wt.content.ContentServerHelper.service.writeContentStream(app, "");
        }
	}
	
    public static void getEpms(WTPart part) throws WTException, PropertyVetoException{
        QueryResult associates = PartDocHelper.service.getAssociatedDocuments(part);
        while (associates.hasMoreElements()) {
        	Object doc = associates.nextElement();
        	System.out.println("关联文档类型:"+doc.getClass().getName());
        	if (doc instanceof EPMDocument){
        		
        	}
        }
    }

}
