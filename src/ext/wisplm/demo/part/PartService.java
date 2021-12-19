package ext.wisplm.demo.part;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.fc.ReferenceFactory;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.part.WTPart;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.util.WTException;

/**
 * @description
 * @author ZhongBinpeng
 * @date 2020年4月6日
 */
public class PartService implements RemoteAccess {

	private static final String CLASSNAME = PartService.class.getName();
	private static final Logger logger    = Logger.getLogger(CLASSNAME);

	public static void main(String args[]) throws WTException, RemoteException, InvocationTargetException {
		String number = args[0];
		String name   = args[1];
		List<WTPart> result = queryPart(number,name);
		for (WTPart part : result) {
			String oid = new ReferenceFactory().getReferenceString(part);
			System.out.println("oid:" + oid + "编号:" + part.getNumber() + "名称:" + part.getName());

		}
	}
	
	/**
	 * 通过编号(模糊查询,不区分大小写),名称(模糊查询,区分大小写)查询部件
	 * @description
	 * @author      ZhongBinpeng
	 * @param number
	 * @return
	 * @throws WTException
	 * @throws RemoteException
	 * @throws InvocationTargetException
	 */
	public static List<WTPart> queryPart(String number,String name) throws WTException, RemoteException, InvocationTargetException{
		if(!RemoteMethodServer.ServerFlag){
			RemoteMethodServer rms = RemoteMethodServer.getDefault();
			Class cla[]  = {String.class,String.class};
			Object obj[] = {number,name};
			return  (List<WTPart>)rms.invoke("queryPart", CLASSNAME, null, cla, obj);
		}
		logger.debug("queryPart");
		//List<WTPart> partList = new ArrayList<WTPart>();
		//1、构建要查询的sql
		QuerySpec qs       = new QuerySpec(WTPart.class);
		//1-1增加编号查询条件
		/**
		 * 参数1:要查询的对象类型
		 * 参数2:要查的字段
		 * 参数3:查询条件,SearchCondition的常量
		 * 参数4:条件值
		 * 参数5:false表示不区分大小,true表示区分大小写
		 */
		//Windchill中模糊查询用*和?作为作为通配符
		if(StringUtils.isNotEmpty(number)){
			number = number.toUpperCase().replace("*", "%").replace("?", "_");
			logger.debug("增加编号搜索条件:" + number);
			//构建查询条件对象
			SearchCondition numberSC = new SearchCondition(WTPart.class,WTPart.NUMBER,SearchCondition.LIKE,number,false);
	        //判断是否需要增加and连接符,如果已存在查询条件则需要
			if (qs.getConditionCount() > 0) {
	            qs.appendAnd();
	        }
			//添加到查询sql中
			qs.appendWhere(numberSC);
		}
		if(StringUtils.isNotEmpty(name)){
			name = name.replace("*", "%").replace("?", "_");
        	logger.debug("增加名称搜索条件:" + name);
            if (qs.getConditionCount() > 0){
                qs.appendAnd();
            }
            SearchCondition nameSC = new SearchCondition(WTPart.class,WTPart.NAME,SearchCondition.LIKE, name);
            qs.appendWhere(nameSC);
        }
        
		//启用高级查询支持
		qs.setAdvancedQueryEnabled(true);
		//2,执行sql
		QueryResult qr = PersistenceHelper.manager.find(qs);
		/**
		 * 通过以下代码获取QueryResult中的对象
		 * while(qr.hasMoreElements()){
			WTPart part = (WTPart) qr.nextElement();
		   }
		 */
		/**
		 * 也可转换为Vector
		 */
		Vector partVector = qr.getObjectVector().getVector();
		return partVector;
	}
}
