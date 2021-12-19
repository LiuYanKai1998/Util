package ext.wis.ebom;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

import ext.wis.constants.BusinessConstants;
import ext.wisplm.util.ContentUtil;
import ext.wisplm.util.IBAUtil;
import ext.wisplm.util.PartUtil;
import ext.wisplm.util.PromotionNoticeUtil;
import ext.wisplm.util.WTUtil;
import ext.wisplm.util.WfUtil;
import ext.wisplm.util.third.ZipFileUtil;
import wt.content.ApplicationData;
import wt.epm.EPMDocument;
import wt.fc.ObjectIdentifier;
import wt.fc.ObjectReference;
import wt.fc.ObjectToObjectLink;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.fc.WTObject;
import wt.maturity.Promotable;
import wt.maturity.PromotionNotice;
import wt.method.RemoteAccess;
import wt.part.Quantity;
import wt.part.QuantityUnit;
import wt.part.WTPart;
import wt.part.WTPartMaster;
import wt.part.WTPartUsageLink;
import wt.pds.StatementSpec;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;

public class EBOMHelper implements RemoteAccess{
	
	private static final String CLASSNAME = EBOMHelper.class.getName();
	
	private static final Logger logger    = Logger.getLogger(CLASSNAME);
	
	/**
	 *  导出EBOM清册
		1、部件操作菜单添加action,
		2、点击"导出EBOM清册"弹出Excel文件供用户下载
		3、Excel清单生成逻辑:
		   3.1以当前操作的部件为顶层件递归遍历获取所有层级的子件信息记录到excel并推送到前台供用户下载
		   3.2excel各列定义如下：
		   		序号：流水号,从1开始
		   		层级：BOM层级,action对应的根部件为0级,逐层递增
		   		编号：部件编号
		   		名称：部件名称
		   		版本：部件大版本
		   		父件编号：上一级部件编号,根节点部件上一级为空
		   		单装数量：部件在当前父件下的装配数量
				材料牌号：部件软属性,自行通过类型属性管理器为部件添加，获取值输出到excel
				热处理：    部件软属性,自行通过类型属性管理器为部件添加,获取值输出到excel
				关重件标识：部件软属性,自行通过类型属性管理器为部件添加,获取值输出到excel
		4、服务器不允许保留临时文件。
	 */
	public static List<String[]> getEbomList(WTPart currentPart) throws WTException{
		
		List<String[]> finalResult = getEbomChildList(currentPart,0);
		
		String version = currentPart.getVersionIdentifier().getValue() + "." + currentPart.getIterationIdentifier().getValue();
		//根节点部件单独添加
		finalResult.add(0, new String[]{"","0",currentPart.getNumber(),currentPart.getName(),version,"",""});
		int index = 1;
		for(String[] oneRow : finalResult){
			oneRow[0] = index + "";
			index ++;
		}
		return finalResult;
	}
	
	/**
	 * 获取所有层级部件,序号,层级,编号,名称,版本,父件编号,单装数量
	 * @description 
	 * @param rootPart 根节点部件对象
	 * @return
	 * @throws WTException 
	 */
	public static List<String[]> getEbomChildList(WTPart parentPart,int level) throws WTException{
		List<String[]> result = new ArrayList<String[]>();
		//1、查询以当前部件为父件的WTPartusageLink
		String columnName   = ObjectToObjectLink.ROLE_AOBJECT_REF + "." + ObjectReference.KEY+ "." + ObjectIdentifier.ID;
        QuerySpec qs = new QuerySpec(WTPartUsageLink.class);
        SearchCondition parentSC = new SearchCondition(WTPartUsageLink.class,
        		columnName,
                SearchCondition.EQUAL,
                parentPart.getPersistInfo().getObjectIdentifier().getId());
        qs.appendWhere(parentSC,new int[] { 0 });
        QueryResult qr = PersistenceHelper.manager.find((StatementSpec) qs);
        //2、循环获取link对象
        while (qr.hasMoreElements()) {
            WTPartUsageLink link     = (WTPartUsageLink) qr.nextElement();
            //3、获取子件Master
            WTPartMaster childMaster = link.getUses();
            //4、通过子件Master查找设计视图最新版部件
            WTPart childPart 		 = PartUtil.getLatestPart(childMaster, "Design");
            //5、如果不存在设计视图部件则结束该分支
            if(childPart == null || !"Design".equalsIgnoreCase(childPart.getViewName())){
            	continue;
            }
            //封装子件信息
            //6.子件单装数量
            double amount		     = link.getQuantity().getAmount();
            String[] partInfo        = new String[10];
            //序号
            //层级
            partInfo[1] = (level+1) + "";
            //编号
            partInfo[2] = childPart.getNumber();
            //名称
            partInfo[3] = childPart.getName();
            //大小版本A.3
            partInfo[4] = childPart.getVersionIdentifier().getValue()+"." + childPart.getIterationIdentifier().getValue();
            //父件编号
            partInfo[5] = parentPart.getNumber();
            //单装数量
            partInfo[6] = amount+"";
            //材料牌号
            partInfo[7] = IBAUtil.getIBAValue(childPart, BusinessConstants.IBA.CLPH);
            //热处理
            partInfo[8] = IBAUtil.getIBAValue(childPart, BusinessConstants.IBA.RCL);
            //关重件标识
            partInfo[9] = IBAUtil.getIBAValue(childPart, BusinessConstants.IBA.GZJBS);
            result.add(partInfo);
            result.addAll(getEbomChildList(childPart,level+1));
        }
        return result;
	}
	
	/**
	 * 创建EBOM签审包
	 * 名称=根节点部件名称
	 * 编号=EBOM签审包_部件编号_流水
	 * @description
	 * @param rootPart
	 * @throws WTException 
	 * @throws WTPropertyVetoException 
	 * @throws InvocationTargetException 
	 * @throws RemoteException 
	 * @throws ParseException 
	 */
	public static PromotionNotice createEBOMPackage(WTPart rootPart) throws WTException, RemoteException, InvocationTargetException, WTPropertyVetoException, ParseException{
		
		String packageName   = rootPart.getName();
		String packageNumber = "EBOM_" + rootPart.getNumber() + "_" + WTUtil.formatTime(new Timestamp(System.currentTimeMillis()), "yyyyMMddHHmmss");
		//1、创建签审包
		PromotionNotice ebomPackage = PromotionNoticeUtil.createPromotionNotice(
									  packageName, packageNumber, BusinessConstants.Type.EBOMPACKAGE, 
									  "/Default/EBOM签审包", "", rootPart.getContainer());
		//设置根节点部件oid
		IBAUtil.setIBAValue(ebomPackage, BusinessConstants.IBA.ROOTPART,WTUtil.getVROid(rootPart));
		//2、设置状态为提交审阅激活审批流程
		WfUtil.changeLifeCycle(ebomPackage, BusinessConstants.State.WISTIJIAOSHENYUE);
		//3、获取当前部件为根节点的整个BOM部件和EPM文档
		Set<WTObject> ebomObjects = getEBOMPartEpms(rootPart);
		for(WTObject obj : ebomObjects){
			//4、添加部件和EPM文档到签审包,建立关联关系
			ebomPackage = PromotionNoticeUtil.addPromotionNoticeItem(ebomPackage,(Promotable)obj);
			//5、修改部件和EPM文档生命周期状态为正在审阅
			WfUtil.changeLifeCycle(obj, BusinessConstants.State.WISTIJIAOSHENYUE);
		}
		logger.debug("EBOM签审包创建成功:" + ebomPackage.getDisplayIdentity());
		return ebomPackage;
	}
	
	
	
	/**
	 * 获取当前部件为根节点的整个EBOM部件和关联的EPM文档
	 * @description
	 * @param rootPart
	 * @throws WTException 
	 */
	public static Set<WTObject> getEBOMPartEpms(WTPart rootPart) throws WTException{
		Set<WTObject> allEBOMPartEpms = new HashSet<WTObject>();
		//获取所有EBOM部件,包括根节点部件
		Set<WTPart> allParts = PartUtil.getAllBOMParts(rootPart, PartUtil.DesignConfigSpec);
		//获取每个部件关联的EPM文档
		for(WTPart part : allParts){
			//获取3D和2D图纸
			Vector<EPMDocument> epm3d2d = PartUtil.getAssEpmDocument(part);
			allEBOMPartEpms.addAll(epm3d2d);
			allEBOMPartEpms.add(part);
		}
		return allEBOMPartEpms;
	}
	
	/**
	 * 下载EBOM数模压缩包
	 * @description
	 * @param rootPart
	 * @return
	 * @throws Exception 
	 */
	public static File downloadEbomModel(WTPart rootPart) throws Exception{
		//codebase/temp下创建临时文件夹存储数模
		File tempFolder = null;
		try{
			String tempFolderPath = BusinessConstants.codebase_temp + File.separator + "EBOM_"+ WTUtil.formatTime("yyyyMMddHHmmss");
			tempFolder = new File(tempFolderPath);
			tempFolder.mkdirs();
			//获取所有EBOM部件,包括根节点部件
			Set<WTPart> allParts     = PartUtil.getAllBOMParts(rootPart, PartUtil.DesignConfigSpec);
			Set<EPMDocument> allEpms = new HashSet<EPMDocument>();
			//获取每个部件关联的EPM文档
			for(WTPart part : allParts){
				//获取3D和2D图纸
				Vector<EPMDocument> epm3d2d = PartUtil.getAssEpmDocument(part);
				allEpms.addAll(epm3d2d);
			}
			if(allEpms.size() == 0){
				throw new WTException("未找到BOM关联的EPM文档");
			}
			//循环下载EPM文档主内容到临时文件夹
			boolean hasAnyContent = false;
			for(EPMDocument epm : allEpms){
				ApplicationData primary = ContentUtil.getPrimary(epm);
				if(primary != null){
					File primaryFile = ContentUtil.downloadApplicationData(primary, tempFolder.getPath(), null, epm);
					if(primaryFile.exists()){
						hasAnyContent = true;
					}
				}
			}
			if(!hasAnyContent){
				throw new WTException("BOM关联的EPM文档主内容文件为空");
			}
			
			String zipFilePath = BusinessConstants.codebase_temp + 
								 File.separator + "EBOMMODEL_"   +
								 WTUtil.formatTime("yyyyMMddHHmmss") + ".zip";
			//将临时文件夹里的数模打包到codebase/temp目录下
			ZipFileUtil.zip(tempFolder, zipFilePath);
			File zipFile = new File(zipFilePath);
			return zipFile;
		}finally{
			if(tempFolder != null){
				ZipFileUtil.deleteFiles(tempFolder.getPath());
			}
		}
	}
	
	public static void main(String args[]) throws WTException{
		WTPart parentPartA1 = WTPart.newWTPart();
		
		WTPart childPartA1  = WTPart.newWTPart();
		
		WTPartMaster master = (WTPartMaster) childPartA1.getMaster();
		
		//master--part
		//1->n
		WTPart P01VA1 = null;
		WTPart P01VA2 = null;
		WTPart P01VB1 = null;
		
		WTPartUsageLink usageLink = WTPartUsageLink.newWTPartUsageLink(parentPartA1, master);
		Quantity quantity = Quantity.newQuantity();
		quantity.setAmount(2);
		quantity.setUnit(QuantityUnit.EA);
		usageLink.setQuantity(Quantity.newQuantity());
		PersistenceHelper.manager.save(usageLink);
		
		WTPart parentPart 		 =  usageLink.getUsedBy();
		WTPartMaster childMaster = usageLink.getUses();
	}

}
