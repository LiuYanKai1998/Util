package ext.wis.webservice;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

/**
 * Axis1访问Windchill soap接口-info*engine发布的webservice
 *
 *Zhong Binpeng Jul 16, 2021
 */
public class Axis1ClientForWindchill {
	public static void main(String[] args) {
		String serviceURL ="http://plmtest.zhengfei.com/Windchill/servlet/RPC";
		String nameSpace = "http://www.ptc.com/infoengine/soap/rpc/message/";
		String methodName = "createDesignTask";
		String soapActionURI = "urn:ie-soap-rpc:com.infoengine.soap!"+methodName;
		String pdmUserName = "soapuser";
		String pdmUserPass = "soapuser";
		String result = "";
		Service service = new Service();
		Call call;
		try {
			call = (Call) service.createCall();
			call.setUsername(pdmUserName);
			call.setPassword(pdmUserPass);
			call.setTargetEndpointAddress(new java.net.URL(serviceURL));
			//设置调用的方法名
			call.setOperationName(new QName(nameSpace,methodName));
			//设置参数名
			call.addParameter("TASKNAME",org.apache.axis.encoding.XMLType.XSD_STRING,javax.xml.rpc.ParameterMode.IN);
			call.addParameter("USERNAME",org.apache.axis.encoding.XMLType.XSD_STRING,javax.xml.rpc.ParameterMode.IN);
			call.addParameter("TASKID",org.apache.axis.encoding.XMLType.XSD_STRING,javax.xml.rpc.ParameterMode.IN);
			call.addParameter("PROJECTNAME",org.apache.axis.encoding.XMLType.XSD_STRING,javax.xml.rpc.ParameterMode.IN);
			call.addParameter("PROJECTID",org.apache.axis.encoding.XMLType.XSD_STRING,javax.xml.rpc.ParameterMode.IN);
			call.addParameter("DOCOID",org.apache.axis.encoding.XMLType.XSD_STRING,javax.xml.rpc.ParameterMode.IN);
			
			//参数值,顺序与addParameter相同
			Object[] parmmeter = {"用户名1","任务名1","任务id1","项目名称1","项目id1","文档id1"};
			//设置返回类型
			call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING); //返回String
			call.setUseSOAPAction(true);
			call.setSOAPActionURI(soapActionURI);

			result = (String) call.invoke(parmmeter);
			System.out.println(result.trim());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
}
