package ext.wis.webservice;

import java.rmi.RemoteException;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.log4j.Logger;

import wt.log4j.LogR;

/**
 * Axis1访问Windchill sun-jax发布的webservice接口测试
 * 
 */
public class Axis1ClientForWindchillSunjaxws {
	private static final Logger LOG = LogR.getLogger(Axis1ClientForWindchillSunjaxws.class.getName());
	private String endpoint="http://cpm.sacc.com/Windchill/servlet/MPMInterfaceService";
	private String namespace="http://service.catia.integration.mpm.sacc.ext/";
	private String methodName="getModelByWorkingPosition";
	

	// 测试
	public static void main(String[] args) {
		Axis1ClientForWindchillSunjaxws test = new Axis1ClientForWindchillSunjaxws();
		String result = test.invokeRemoteFuc("C919","站位","0000000067","10001");//机型、类型（工作包/站位/工位）、编号、单架次有效性
		System.out.println(result);
	}
	
	/**
	 * 调用webservice 传递一个参数
	 * 
	 * @param endpoint
	 *            wsdl地址
	 * @param namespace
	 *            命名空间
	 * @param methodName
	 *            方法名
	 * @param zipFileName
	 *            zip地址
	 * @return
	 */
	public String invokeRemoteFuc(String planType,String srType,String number,String eff) {
		// 预定义失败的默认返回值
		String result = "call failed!";
		// 步骤1 构建 org.apache.axis.client.Service 对象
		Service service = new Service();
		Call call;
		try {
			// 步骤2：通过org.apache.axis.client.Service对象创建一个Call,需要强转为
			// org.apache.axis.client.Call类型
			call = (Call) service.createCall();
			call.setUsername("wcadmin");
			call.setPassword("wcadmin");
			// 步骤3：设置目标地址，即需要访问的webservice地址
			call.setTargetEndpointAddress(endpoint);
			call.setOperationName(new QName(namespace, methodName));
			// 步骤5： 设置参数名
			call.addParameter("planType", // 参数名
					XMLType.XSD_STRING, // 参数类型:String
					ParameterMode.IN); // 参数模式：'IN' or 'OUT'
			
			call.addParameter("srType", // 参数名
					XMLType.XSD_STRING, // 参数类型:String
					ParameterMode.IN); // 参数模式：'IN' or 'OUT'
			
			call.addParameter("number", // 参数名
					XMLType.XSD_STRING, // 参数类型:String
					ParameterMode.IN); // 参数模式：'IN' or 'OUT'
			
			call.addParameter("eff", // 参数名
					XMLType.XSD_STRING, // 参数类型:String
					ParameterMode.IN); // 参数模式：'IN' or 'OUT'

			// 步骤6：设置返回值类型
			call.setReturnType(XMLType.XSD_STRING); // 返回值类型：String
			// 步骤7 ：调用call.invoke(Object[] obj)方法
			result = (String) call.invoke(new Object[] {planType,srType,number,eff});// 远程调用
			LOG.info("invokeRemoteFuc=====>>>" + result);
		} catch (ServiceException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return result;
	}
}
