package ext.wisplm.apiexercise.ddownloaddoc;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Vector;

import wt.content.ApplicationData;
import wt.content.ContentHelper;
import wt.content.ContentItem;
import wt.content.ContentServerHelper;
import wt.content.ContentService;
import wt.content.ContentServiceSvr;
import wt.doc.WTDocument;
import wt.fc.PersistenceHelper;
import wt.fc.PersistenceManager;
import wt.fc.QueryResult;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.util.WTException;

public class DocumentContent implements RemoteAccess{
	
	private static final String CLASSNAME = DocumentContent.class.getName();
	
	public static void main(String [] args) throws RemoteException, InvocationTargetException{
		
		String number = args[0];
		String folder = "D:\\文档主内容和附件";
		RemoteMethodServer rms = RemoteMethodServer.getDefault();
		rms.invoke(
					"downloadContent", 
					CLASSNAME,
					null,
					new Class[]{String.class,String.class},
					new Object[]{number,folder}
				  );
	}
	
	
	public static void downloadContent(String docNumber,String folderPath) throws WTException, PropertyVetoException, IOException{
		WTDocument doc = queryDocumentByNumber(docNumber);
		if(doc == null){
			System.out.println("未找到编号为"+docNumber+"的文档");
			return;
		}
		System.out.println("找到文档,开始下载主内容:" + doc.getDisplayIdentity());
		
		File folder = new File(folderPath);
		if(!folder.exists()){
			folder.mkdirs();
		}
		ContentServiceSvr service = ContentServerHelper.service;
		ContentService cs = ContentHelper.service;
		ContentItem item    = cs.getPrimary(doc);
		if(item == null || !(item instanceof ApplicationData)){
			System.out.println("未找到文档主内容:" + doc.getDisplayIdentity());
		}else{
			ApplicationData primaryContent = (ApplicationData) item;
			String fileName = primaryContent.getFileName();
			
			String filePath = folder.getPath() + File.separator + fileName;
			service.writeContentStream(primaryContent,filePath);
			if(new File(filePath).exists()){
				System.out.println("文档主内容下载成功:" + filePath);
			}else{
				System.out.println("未成功下载文档主内容..");
			}
		}
		//获取附件集
		wt.content.ContentHolder contentHolder = ContentHelper.service.getContents(doc);
		Vector apps = ContentHelper.getApplicationData(contentHolder);
		if(apps == null || apps.size() == 0){
			System.out.println("未找到文档附件");
		}else{
			for(int i = 0 ; i < apps.size(); i ++){
				ApplicationData second = (ApplicationData) apps.get(i);
				String fileName = second.getFileName();
				String filePath = folder.getPath() + File.separator + fileName;
				service.writeContentStream(second,filePath);
				if(new File(filePath).exists()){
					System.out.println("文档附件下载成功:" + filePath);
				}else{
					System.out.println("未成功下载文档附件..");
				}
			}
		}
	}
	
	
	public static WTDocument queryDocumentByNumber(String number) throws WTException{
		QuerySpec qs = new QuerySpec(WTDocument.class);
		number = number.toUpperCase();
		SearchCondition numberSC = new SearchCondition(WTDocument.class,WTDocument.NUMBER,SearchCondition.EQUAL,number);
		qs.appendWhere(numberSC);
		//构建查找最新版的配置规范
		wt.vc.config.LatestConfigSpec latestconfigspec = new wt.vc.config.LatestConfigSpec();
		//添加到查询条件
        latestconfigspec.appendSearchCriteria(qs);
;
		PersistenceManager pm = PersistenceHelper.manager;
		QueryResult qr = pm.find(qs);
		System.out.println("找到编号为" + number+"的文档数:"  + qr.size());
		//过滤出最新版
        qr = latestconfigspec.process(qr);
		if(qr.size() > 0){
			return (WTDocument)qr.nextElement();
		}
		return null;
	}
	
	
/*	*//**
	 * 下载文档的主内容到指定目录
	 * @param doc
	 * @param storageDirectory 存储目录
	 * @return 下载到的主文件,如果返回空,则表示无主内容或下载出错
	 *//*
	public static File downloadPrimary(WTDocument doc,String storageDirectory) throws WTException, IOException, PropertyVetoException, InvocationTargetException{
		if (!RemoteMethodServer.ServerFlag) {
			return (File) RemoteMethodServer.getDefault().invoke("downloadPrimary",CLASSNAME, null,new Class[]{WTDocument.class, String.class},new Object[]{doc, storageDirectory});
		}
		if (!storageDirectory.endsWith("/") || storageDirectory.endsWith("\\")) {
			storageDirectory = storageDirectory + File.separator;
		}
		createFolders(storageDirectory);
		ContentItem item = (ContentItem) ContentHelper.service.getPrimary(doc);
		if (item != null && item instanceof ApplicationData) {
			ApplicationData primaryContent = (ApplicationData) item;
			String primaryContentName      = primaryContent.getFileName(); 
			String downloadFullPath = storageDirectory + primaryContentName;
			ContentServerHelper.service.writeContentStream(primaryContent,downloadFullPath);
			return new File(downloadFullPath);
		}else{
			log.info("未发现主内容文件,WTDocument=>" + doc);
			return null;
		}
	}*/
	

}
