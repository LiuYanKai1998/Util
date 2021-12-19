package ext.wisplm.apiexercise.euploaddoccontent;

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
import wt.content.ContentRoleType;
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
import wt.util.WTPropertyVetoException;

public class DocumentContent implements RemoteAccess{
	
	private static final String CLASSNAME = DocumentContent.class.getName();
	
	public static void main(String [] args) throws RemoteException, InvocationTargetException{
		
		String number     = args[0];
		String filePath   = args[1];
		boolean isPrimary = Boolean.parseBoolean(args[2]);
		RemoteMethodServer rms = RemoteMethodServer.getDefault();
		rms.invoke(
					"uploadContent", 
					CLASSNAME,
					null,
					new Class[]{String.class,String.class,boolean.class},
					new Object[]{number,filePath,isPrimary}
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
	
	
	/**
	 * 为对象添加主内容或附件
	 */
    public static WTDocument uploadContent(String number,String filePath, boolean isPrimary) throws Exception{
    	WTDocument doc = queryDocumentByNumber(number);
    	File file = new File(filePath);
        if(!file.exists()){
        	System.out.println("未找到本地文件:" + filePath + ",无法上传");
        	return doc;
        }
        wt.content.ContentHolder ch = wt.content.ContentHelper.service.getContents(doc);
        if(isPrimary){
            //首先删除文档已存在的主内容对象
    		ContentServiceSvr service = ContentServerHelper.service;
    		ContentItem item  = ContentHelper.service.getPrimary(doc);
    		if(item != null){
    			System.out.println("找到文档已存在的主内容,删除...");
    			ContentServerHelper.service.deleteContent(ch,item);
    			System.out.println("删除文档主内容成功...");
    		}
        }
        
        //构建ApplicationData对象,用于存储实体文件信息
        ApplicationData applicationdata = ApplicationData.newApplicationData(doc);
        //定义要上传的文件名
        applicationdata.setFileName(file.getName());
        //定义要上传的文件所在路径
        applicationdata.setUploadedFromPath(file.getParent());
        if (isPrimary){
        	 applicationdata.setRole(ContentRoleType.PRIMARY);
        }
        ch = wt.content.ContentHelper.service.getContents(doc);
        ContentServerHelper.service.updateContent(ch, applicationdata,file.getPath());
        if(isPrimary){
        	System.out.println("文档主内容上传成功");
        }else{
        	System.out.println("文档附件上传成功");
        }
        return doc;
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
