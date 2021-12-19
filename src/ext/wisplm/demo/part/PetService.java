package ext.wisplm.demo.part;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ext.wisplm.demo.part.model.Pet;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.fc.ReferenceFactory;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.org.WTUser;
import wt.pom.Transaction;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.session.SessionHelper;
import wt.session.SessionServerHelper;
import wt.util.WTException;
import wt.util.WTPropertyVetoException;

/**
 * @description
 * @author      ZhongBinpeng
 * @date        2020年4月18日
 */
public class PetService implements RemoteAccess,java.io.Serializable{
	
	private static final String CLASSNAME     = PetService.class.getName();
	private static final PetService instance  = new PetService();
	private static final Logger     logger    = Logger.getLogger(CLASSNAME);
	
	public static void main(String args[]) throws WTPropertyVetoException, WTException, RemoteException, InvocationTargetException{
/*		for(int i = 0 ; i < 10; i ++){
			createPet(i+"Wisplm",i+"-2017" ,i);
		}
		System.out.println("创建成功");*/
		
		createMutiplePet();
		
		List<Pet> result = instance.findPet("*CoCO",null,0);
		for(Pet pet : result){
			String petOid = new ReferenceFactory().getReferenceString(pet);
			System.out.println("petOid:"+petOid);
			Pet temp      =  (Pet) new ReferenceFactory().getReference(pet).getObject();
			System.out.println("姓名:"+temp.getName());
			System.out.println("年龄:"+temp.getAge());
			System.out.println("---------------------------------------");
		}
		
		/*List<Pet> result = findPet();
		for(Pet pet : result){
			System.out.println("删除对象oid->" + new ReferenceFactory().getReferenceString(pet)+ ","+pet.getDisplayIdentity());
			delete(pet);
		}
		System.out.println("删除完毕");*/
	}
	
	
	/**
	 * 事务控制
	 * @description
	 * @throws WTPropertyVetoException
	 * @throws WTException
	 * @throws InvocationTargetException 
	 * @throws RemoteException 
	 */
	public static void createMutiplePet() throws WTPropertyVetoException, WTException, RemoteException, InvocationTargetException{
		if(!RemoteMethodServer.ServerFlag){
			RemoteMethodServer.getDefault().invoke("createMutiplePet", 
									CLASSNAME, null,
									null, 
									null);
			return;
		}
		
		Transaction tx = null;
		try{
			tx = new Transaction();
			tx.start();
			createPet("王五","20210414",18);
			int i = 2/0;
			createPet("李四","20210414",18);
			
			
			tx.commit();
			tx = null;
		}finally{
			if(tx != null){
				tx.rollback();
			}
		}

	}
	
	/**
	 * 创建
	 * @description
	 * @author      ZhongBinpeng
	 * @param name
	 * @param birthday
	 * @param age
	 * @return
	 * @throws WTException
	 * @throws WTPropertyVetoException
	 */
	public static Pet createPet(String name,String birthday,int age) throws WTException, WTPropertyVetoException{
		Pet pet = Pet.newPet();
		pet.setAge(age);
		pet.setName(name);
		pet.setBirthday(birthday);
		pet = (Pet) PersistenceHelper.manager.save(pet);
		return pet;
	}

	
	public  List<Pet> findPet(String name,String birthday,Integer age) throws WTException, RemoteException, InvocationTargetException{
		if(!RemoteMethodServer.ServerFlag){
			return (List<Pet>) RemoteMethodServer.getDefault().invoke("findPet", 
									CLASSNAME, instance,
									new Class[]{String.class,String.class,Integer.class}, 
									new Object[]{name,birthday,age});
		}
		//false表示关闭权限
		boolean accessEnforced = false;
		WTUser current = null;
		try{
			//获取当前登录的用户
			current = (WTUser) SessionHelper.manager.getPrincipal();
			//SessionHelper.manager.setAdministrator();
			//切换用户为user1执行该段代码
			//SessionHelper.manager.setPrincipal("user1");
			//该方法返回值为当前上下文的权限开关
			accessEnforced = SessionServerHelper.manager.setAccessEnforced(accessEnforced);
			List<Pet> petList = new ArrayList<Pet>();
			//1.定义要搜索的对象类型 select * from Wispet
			QuerySpec qs 	= new QuerySpec(Pet.class);
	        //where name='zhangsan'
	        //参数1,对象类型
	        //参数2,字段名
	        //参数3,连接符
	        //参数4,条件的值
	        //参数5,是否区分大小写,false为不区分大小写
			//姓名不区分大小写&支持模糊查询
			//界面输入时*和?为通配符,需替换为oracle对应符号
	        if(StringUtils.isNotEmpty(name)){
	        	name = name.replace("*","%").replace('?', '_');
	            SearchCondition sc = new SearchCondition(Pet.class,Pet.NAME,SearchCondition.LIKE,name,false);
	          //如果条件大于0,增加and连接符
	            if (qs.getConditionCount() > 0){
	                qs.appendAnd();
	            }
	            qs.appendWhere(sc);
	        }
	        
	        if(StringUtils.isNotEmpty(birthday)){
	            SearchCondition sc = new SearchCondition(Pet.class,Pet.BIRTHDAY, SearchCondition.EQUAL,birthday,true);
	            if (qs.getConditionCount() > 0){
	                qs.appendAnd();
	            }
	            qs.appendWhere(sc);
	        }
	        if(age > 0 ){
	            SearchCondition sc = new SearchCondition(Pet.class,Pet.AGE, SearchCondition.EQUAL,age);
	            if (qs.getConditionCount() > 0){
	                qs.appendAnd();
	            }
	            qs.appendWhere(sc);
	        }
			//2.如何遍历QueryResult
	        logger.debug("查询的sql:" + qs.toString());
			QueryResult qr  = PersistenceHelper.manager.find(qs);
			while(qr.hasMoreElements()){
				Pet pet = (Pet) qr.nextElement();
				petList.add(pet);
			}
			//可直接将QueryResult转换为Vector:qr.getObjectVector().getVector();
			logger.debug("当前执行代码的用户为:" + SessionHelper.manager.getPrincipal().getName());
			return petList;
		}finally{
			SessionServerHelper.manager.setAccessEnforced(accessEnforced);
			/*if(current != null){
				SessionHelper.manager.setPrincipal(current.getName());
			}*/
		}
	}
	
	
	public static Pet update(Pet pet,String name,String birthday,int age) throws WTPropertyVetoException, WTException{
		pet.setAge(age);
		pet.setName(name);
		pet.setBirthday(birthday);
		pet = (Pet) PersistenceHelper.manager.save(pet);
		return pet;
	}
	
	public static void delete(Pet pet) throws WTException{
		PersistenceHelper.manager.delete(pet);
	}
}
