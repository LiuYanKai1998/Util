package ext.wis.webservice.javaxjws;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.log4j.Logger;

import wt.log4j.LogR;

/**
 * Axis1访问Windchill sun-jax发布的webservice接口测试
 * 
 */
public class JWSClient{
	private static final Logger logger = LogR.getLogger(JWSClient.class.getName());
	/**
	 * http://wisplm.com/Windchill/servlet/WISService?wsdl
	 * Webservice访问地址,不带?wsdl
	 */
	private static final String WEBSERVICEURL  ="http://wisplm.com/Windchill/servlet/WISService";
	/**
	 * Webservice的命名空间
	 */
	private static final String TARGETNAMESPACE="http://service.wisplm.com/";
	
	/**
	 * 用于webservice通信的用户名和密码,上线后会创建一个没有任何权限的用户提供给第三方用于webservice通信,故服务端开发要考虑权限和用户切换问题
	 */
	private static final String USERNAME       = "wcadmin";
	private static final String PASSWORD       = "wcadmin";

	// 测试
	public static void main(String[] args) throws Exception {
		String result = invoke("test","test");
		System.out.println(result);
	}
	
	/**
	 * 调用查询文档(getWISDocument)的webservice接口
	 */
	public static String invoke(String docNumber,String docName) throws Exception {
		// 预定义失败的默认返回值
		String result = "call failed!";
		// 步骤1 构建 org.apache.axis.client.Service 对象
		Service service = new Service();
		Call call;
		try {
			// 步骤2：通过org.apache.axis.client.Service对象创建一个Call,需要强转为
			// org.apache.axis.client.Call类型
			call = (Call) service.createCall();
			call.setUsername(USERNAME);
			call.setPassword(PASSWORD);
			// 步骤3：设置目标地址，即需要访问的webservice地址
			call.setTargetEndpointAddress(WEBSERVICEURL);
			// 步骤4：设置要调用的方法operation
			call.setOperationName(new QName(TARGETNAMESPACE, "getWISPart"));
			// 步骤5： 设置参数名和参数类型
			call.addParameter("docNumber", // 参数名
					XMLType.XSD_STRING, // 参数类型:String
					ParameterMode.IN); // 参数模式：'IN' or 'OUT'
			
			call.addParameter("docName",XMLType.XSD_STRING,ParameterMode.IN);
			// 步骤6：设置返回值类型,String
			call.setReturnType(XMLType.XSD_STRING);
			// 步骤7 ：传递参数调用call.invoke(Object[] obj)方法,远程调用
			result = (String) call.invoke(new Object[] {docNumber,docName});
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}
}

