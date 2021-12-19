package ext.wisplm.util;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.ptc.core.meta.common.TypeIdentifier;
import com.ptc.core.meta.common.TypeIdentifierHelper;
import com.ptc.windchill.enterprise.copy.server.CoreMetaUtility;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.fc.collections.WTValuedHashMap;
import wt.folder.Folder;
import wt.folder.FolderHelper;
import wt.inf.container.WTContainerRef;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.session.SessionServerHelper;
import wt.util.WTException;
import wt.vc.baseline.BaselineHelper;
import wt.vc.baseline.Baselineable;
import wt.vc.baseline.ManagedBaseline;


/**
 * 该工具类封装了对于ManagedBaseline的一系列底层操作
 * @author mliu
 *
 */
public class BaselineUtil implements RemoteAccess, Serializable{
	private static final Logger logger = Logger.getLogger(BaselineUtil.class);		
	private static final String CLASSNAME = BaselineUtil.class.getName();
	public static String FOLDER = "FOLDER";	//创建文件夹 (默认：/Default)
	public static String DESCRIPTION = "DESCRIPTION"; //描述
	public static String TYPE = "TYPE";	//类型 (默认：wt.vc.baseline.ManagedBaseline)
	
	private static final long serialVersionUID = -8423672460519840395L;

	/**
	 * 通过编号查找基线
	 * @param number	查询基线编号条件
	 * @param accessControlled	是否受到权限制约
	 * @return	返回结果基线
	 */
	public static ManagedBaseline getBaseline(String number, boolean accessControlled) {
		try {
			number = number.toUpperCase();
			
			if (!RemoteMethodServer.ServerFlag) {
				return (ManagedBaseline) RemoteMethodServer.getDefault().invoke("getBaseline",CLASSNAME, null,
						new Class[] {String.class, boolean.class},
						new Object[] {number, accessControlled});
			} else {
				ManagedBaseline baseline = null;
				
				boolean enforce = wt.session.SessionServerHelper.manager.setAccessEnforced(accessControlled);
				try {
					QuerySpec spec = new QuerySpec(ManagedBaseline.class);
					spec.appendWhere(
							new SearchCondition(ManagedBaseline.class,
									ManagedBaseline.NUMBER, SearchCondition.EQUAL, number), new int[] { 0 });
					
					QueryResult qr = PersistenceHelper.manager.find(spec);
					if (qr.hasMoreElements()){
						baseline = (ManagedBaseline)qr.nextElement();
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				} finally {
					SessionServerHelper.manager.setAccessEnforced(enforce);
				}

				return baseline;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}	
	

	/**
	 * 通过基线内容对象查找基线
	 * @param baselineable	查询内容对象
	 * @return	返回结果基线集
	 */
	public static ArrayList<ManagedBaseline> getBaselineByBaselineable(Baselineable baselineable) {
		ArrayList<ManagedBaseline> results = new ArrayList<ManagedBaseline>();
		
		try {
			if (!RemoteMethodServer.ServerFlag) {
				return (ArrayList<ManagedBaseline>) RemoteMethodServer.getDefault().invoke("getBaselineByBaselineable",CLASSNAME, null,
						new Class[] {Baselineable.class},
						new Object[] {baselineable});
			} else {
				boolean enforce = wt.session.SessionServerHelper.manager.setAccessEnforced(false);
				try {
					QueryResult qr = BaselineHelper.service.getBaselines(baselineable);
					while (qr.hasMoreElements()){				
						ManagedBaseline baseline = (ManagedBaseline)qr.nextElement();	
						if(!results.contains(baseline)){
							results.add(baseline);
						}						
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				} finally {
					SessionServerHelper.manager.setAccessEnforced(enforce);
				}

				return results;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 得到默认的基线序列号
	 * @return 10位的序列号字符串
	 */
    public static String getDefaultBaselineSeqNumber(){
    	String bitFormat = "";
        
    	try{
        	for (int i = 0; i < 10; i++) {
        		bitFormat = bitFormat + "0";
    		}
    		
			int seq = Integer.parseInt(PersistenceHelper.manager.getNextSequence(ManagedBaseline.class));
			DecimalFormat format = new DecimalFormat(bitFormat);
			return format.format(seq);
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
		return null;
    }	
	
	
	/**
	 * 创建基线
	 * @param number	编号 （默认为系统的Baseline Sequence）	
	 * @param name		名称 （必须）
	 * @param attributes	固定属性对应表 （必须）
	 * @param containerRef	上下文
	 * @return	如果指定编号的基线存在，则返回该基线
	 */	
	public static ManagedBaseline createBaseline(String number, String name, HashMap attributes, WTContainerRef containerRef){
		try {
			if (!RemoteMethodServer.ServerFlag) {
				return (ManagedBaseline) RemoteMethodServer.getDefault().invoke("createBaseline",CLASSNAME, null,
						new Class[] {String.class, String.class, HashMap.class, WTContainerRef.class},
						new Object[] {number, name, attributes, containerRef});
			} else {
				ManagedBaseline baseline = null;

				try {
					String baselineDesc = "";
					String baselineType = "";
					String baselineFolder = "";					
					
					if(attributes != null){
						baselineDesc = (String)attributes.get(DESCRIPTION);
						baselineType = (String)attributes.get(TYPE);
						baselineFolder = (String)attributes.get(FOLDER);
					}
										
					if(containerRef == null){
						return null;
					}
										
					//设置编号默认值 （默认为系统的Baseline Sequence）					
					if(number == null || number.equalsIgnoreCase("")){
						number = getDefaultBaselineSeqNumber();
					}else{
						//如果此编号基线已存在，则返回该基线
						ManagedBaseline existBaseline = getBaseline(number, false);
						if(existBaseline != null){
							return existBaseline;
						}
					}			
										
					if(name == null || name.equalsIgnoreCase("")){
						return null;
					}
					
					if(baselineDesc == null){
						baselineDesc = "";
					}
					
					//设置默认类型  (默认：wt.vc.baseline.ManagedBaseline)	
				 	if(baselineType == null || baselineType.equalsIgnoreCase("")){
				 		baselineType = "wt.vc.baseline.ManagedBaseline";
					}
				 	
					//设置默认文件夹  (默认：/Default)
					if(baselineFolder == null || baselineFolder.equalsIgnoreCase("")){
						baselineFolder = "/Default";
					}else{
						 if(!baselineFolder.startsWith("/Default")){
							 baselineFolder = "/Default/" + baselineFolder;
						 }
					}				
					
					baseline = ManagedBaseline.newManagedBaseline();	
					baseline.setNumber(number);
					baseline.setName(name);
					
					//设置基线描述
					baseline.setDescription(baselineDesc);
					
					//设置基线类型
					if(baselineType != null){
						TypeIdentifier id = TypeIdentifierHelper.getTypeIdentifier(baselineType);
						baseline = (ManagedBaseline) CoreMetaUtility.setType(baseline, id);
					}
					
					//设置上下文
					baseline.setContainerReference(containerRef);
					
					//设置文件夹
					Folder location = null;	
						//查询文件夹是否存在
					try {
						location = FolderHelper.service.getFolder(baselineFolder,containerRef);
					} catch (Exception e) {
						location = null;
					}
						//若文件夹不存在，则创建该文件夹
					if(location == null)
						location = FolderHelper.service.saveFolderPath(baselineFolder, containerRef);
						//设置文件夹到基线对象
					if (location != null) {
						WTValuedHashMap map = new WTValuedHashMap();
						map.put(baseline, location);
						FolderHelper.assignLocations(map);
					}					
					
					baseline = (ManagedBaseline) PersistenceHelper.manager.save(baseline);
					baseline = (ManagedBaseline) PersistenceHelper.manager.refresh(baseline);											
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				return baseline;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 通过基线得到基线内容
	 * @param baseline	条件基线
	 * @return	基线内容对象集
	 */
	public static ArrayList<Baselineable> getBaselineItems(ManagedBaseline baseline){
		ArrayList<Baselineable> results = new ArrayList<Baselineable>();
		
		try {
			if (!RemoteMethodServer.ServerFlag) {
				return (ArrayList<Baselineable>) RemoteMethodServer.getDefault().invoke("getBaselineItems",CLASSNAME, null,
						new Class[] {ManagedBaseline.class},
						new Object[] {baseline});
			} else {					
				boolean enforce = wt.session.SessionServerHelper.manager.setAccessEnforced(false);
				try {
					QueryResult qr = BaselineHelper.service.getBaselineItems(baseline);
					while(qr.hasMoreElements()){
						Object obj = qr.nextElement();
						if(obj instanceof Baselineable){
							Baselineable baselineable = (Baselineable)obj;
							if(!results.contains(baselineable)){
								results.add(baselineable);
							}
						}						
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				} finally {
					SessionServerHelper.manager.setAccessEnforced(enforce);
				}
				return results;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return results;
	}	
	
	
	/**
	 * 为基线添加内容对象
	 * @param baseline	目标基线
	 * @param baselineable	基线内容对象
	 * @return	添加好的基线对象
	 */
	public static ManagedBaseline addBaselineable(ManagedBaseline baseline, Baselineable baselineable){		
		try {
			if (!RemoteMethodServer.ServerFlag) {
				return (ManagedBaseline) RemoteMethodServer.getDefault().invoke("addBaselineable",CLASSNAME, null,
						new Class[] {ManagedBaseline.class, Baselineable.class},
						new Object[] {baseline, baselineable});
			} else {					
				boolean enforce = wt.session.SessionServerHelper.manager.setAccessEnforced(false);
				try {
					baseline = (ManagedBaseline) BaselineHelper.service.addToBaseline(baselineable, baseline);
					baseline = (ManagedBaseline) PersistenceHelper.manager.refresh(baseline);	
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				} finally {
					SessionServerHelper.manager.setAccessEnforced(enforce);
				}
				return baseline;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return baseline;
	}	
	
	
	/**
	 * 为基线移除内容对象
	 * @param baseline	目标基线
	 * @param baselineable	待移除的基线内容对象
	 * @return	移除好的基线对象
	 */
	public static ManagedBaseline removeBaselineable(ManagedBaseline baseline, Baselineable baselineable){		
		try {
			if (!RemoteMethodServer.ServerFlag) {
				return (ManagedBaseline) RemoteMethodServer.getDefault().invoke("removeBaselineable",CLASSNAME, null,
						new Class[] {ManagedBaseline.class, Baselineable.class},
						new Object[] {baseline, baselineable});
			} else {					
				boolean enforce = wt.session.SessionServerHelper.manager.setAccessEnforced(false);
				try {
					baseline = (ManagedBaseline) BaselineHelper.service.removeFromBaseline(baselineable, baseline);
					baseline = (ManagedBaseline) PersistenceHelper.manager.refresh(baseline);	
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				} finally {
					SessionServerHelper.manager.setAccessEnforced(enforce);
				}
				return baseline;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return baseline;
	}
	
	
	public static void main(String[] args) throws WTException {
		//************************ Test of getBaseline ********************************
/*		ManagedBaseline baseline = CSCBaseline.getBaseline(args[0], Boolean.parseBoolean(args[1]));
		System.out.println("Result Baseline = " + baseline);*/
		
		//************************ Test of createBaseline ********************************
/*		PDMLinkProduct product = CSCContainer.getPDMLinkProduct(args[2], false);
		ReferenceFactory rf = new ReferenceFactory();
		WTContainerRef containerRef = (WTContainerRef) rf.getReference(product);
		
		HashMap<String, String> attributes = new HashMap<String, String>();
		attributes.put(CSCBaseline.FOLDER, "/Default/TestCreateBaselineFolder");
		attributes.put(CSCBaseline.TYPE, "TestBaselineType");
		
		ManagedBaseline baseline = CSCBaseline.createBaseline(args[0], args[1], attributes, containerRef);
		System.out.println("Result Baseline = " + baseline);*/
		
		//************************ Test of getBaselineItems ********************************
/*		ManagedBaseline baseline = CSCBaseline.getBaseline(args[0], false);
		ArrayList<Baselineable> baselineables = CSCBaseline.getBaselineItems(baseline);
		for (int i = 0; i < baselineables.size(); i++) {
			System.out.println("Result Baselineables = " + baselineables.get(i));
		}*/
		
		//************************ Test of getBaselineByBaselineable ********************************
/*		WTPart wtpart = CSCPart.getPart(args[0], false);
		ArrayList<ManagedBaseline> baselines = CSCBaseline.getBaselineByBaselineable(wtpart);
		for (int i = 0; i < baselines.size(); i++) {
			System.out.println("Result Baseline = " + baselines.get(i));
		}*/
		
		//************************ Test of addBaselineable ********************************
/*		WTPart wtpart = CSCPart.getPart(args[0], false);
		ManagedBaseline baseline = CSCBaseline.getBaseline(args[1], false);
		baseline = CSCBaseline.addBaselineable(baseline, wtpart);
		System.out.println("Result Baseline = " + baseline);*/
		
		//************************ Test of removeBaselineable ********************************
/*		WTPart wtpart = CSCPart.getPart(args[0], false);
		ManagedBaseline baseline = CSCBaseline.getBaseline(args[1], false);
		baseline = CSCBaseline.removeBaselineable(baseline, wtpart);
		System.out.println("Result Baseline = " + baseline);*/		
	}
}
