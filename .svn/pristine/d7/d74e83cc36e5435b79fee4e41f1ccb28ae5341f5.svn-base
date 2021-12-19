package ext.wis.webservice;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.apache.soap.Constants;
import org.apache.soap.rpc.Call;
import org.apache.soap.rpc.Parameter;
import org.apache.soap.rpc.Response;
import org.apache.soap.transport.http.SOAPHTTPConnection;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * Apache soap客户端访问Windchill soap接口-info*engine发布的webservice
 *
 *Zhong Binpeng Jul 16, 2021
 */
public class SoapClientForWindchill {
	
	public static void main (String[] args) throws DocumentException, RemoteException, InvocationTargetException{
		createDesignTask("testuser","设计任务ID5","设计任务名称5","项目ID1","项目名称1","");	 //文档OID1
	}
	/**
	 * Windchill Webservice服务器连接地址
	 */
	public static final String SERVICEURL = "http://plmtest.zhengfei.com/Windchill/servlet/RPC";
	/**
	 * Windchill WebService登录用户名
	 */
	public static final String USERNAME  = "soapuser";
	/**
	 * Windchill WebService登录密码
	 */
	public static final String PASSWORD  = "soapuser";
	/**
	 * Windchill WebService方法名:创建设计任务
	 */
	public static final String CREATEDESIGNTASK = "createDesignTask";
	/**
	 * Windchill WebService类型标识符
	 */
	public static final String OBJECTURI = "urn:ie-soap-rpc:com.infoengine.soap";

	
	/**
	 * 调用PDM系统接口推送设计任务
	 *  USERNAME	String	PDM系统用户登录名
		TASKNAME	String	设计任务名称
		TASKID	    String	设计任务ID
		PROJECTNAME	String	项目名称
		PROJECTID	String	项目ID
		DOCOID	    String	设计数据对象的OID:新建设计数据任务时为空,更改设计数据任务时为设计数据在PDM系统中的OID。
		返回的xml格式
		<wc:COLLECTION xmlns:wc="http://www.ptc.com/infoengine/1.0">
		<WINDCHILL  NAME="output"  TYPE="Object"  STATUS="0">
		<wc:INSTANCE>
		<RESULT>FAILURE</RESULT> <!--FAILURE或SUCCESS-->
		<INFO>用户名在PDM系统中不存在</INFO>
		</wc:INSTANCE>
		</WINDCHILL>
		</wc:COLLECTION>	
	 */
	public static void createDesignTask(String userName,String taskID,String taskName,String projectID,String projectName,String docOid) throws DocumentException{
		Map hashMap = new HashMap();
		/**
		 * 参数名称不可更改,无顺序要求
		 */
		hashMap.put("USERNAME",userName);
		hashMap.put("TASKID", taskID);
		hashMap.put("TASKNAME",taskName);
		hashMap.put("PROJECTID", projectID);
		hashMap.put("PROJECTNAME",projectName);
		hashMap.put("DOCOID", docOid);
		
		Response response = invokeSOAPRequest(CREATEDESIGNTASK,hashMap);
		if(response == null){
			System.out.println("客户端调用WebService出错");
		}else{
			//服务器端执行成功
			if(!response.generatedFault()){
				String resultXml = response.getReturnValue().getValue().toString();
				System.out.println("PDM系统Webservice返回的xml内容:" + resultXml);
				//解析webservice返回的xml字符串
				Document doc = DocumentHelper.parseText(resultXml);
				Element root = doc.getRootElement();
				Element eleWindchill  = root.element("WINDCHILL");
				Element eleWCInstance = (Element) eleWindchill.elements().get(0);
				String result = eleWCInstance.element("RESULT").getTextTrim();
				String info   = eleWCInstance.element("INFO").getTextTrim();
				System.out.println("执行结果:result-->" + result + ",info-->" + info);
				
			}else{
				System.out.println("服务器端执行Webservice出错");
			}
		}

	}
	
	/**
	 * 执行一个WebService请求，访问Windchill系统
	 */
	public static Response invokeSOAPRequest(String methodName,Map paramsMap){
		//创建SOAP连接器，访问Windchill系统不允许匿名访问，需要用户名和密码登录
		SOAPHTTPConnection soapConnection = new SOAPHTTPConnection();
		soapConnection.setUserName(USERNAME);
		soapConnection.setPassword(PASSWORD);
		//构建执行对象
        Call call = new Call();
        call.setSOAPTransport(soapConnection);
        call.setTargetObjectURI(OBJECTURI);
        call.setMethodName(methodName);
        call.setEncodingStyleURI(Constants.NS_URI_SOAP_ENC);
		//传递参数
        if(paramsMap != null && paramsMap.size()>0){
        	Iterator it = paramsMap.keySet().iterator();
        	Vector params = new Vector();
        	while(it.hasNext()){
        		String paramName  = (String) it.next();
        		String paramValue =  (String) paramsMap.get(paramName);
        		params.addElement(new Parameter(paramName, String.class,paramValue,null));
        	}
        	call.setParams(params);
        }
        //执行
        Response response = null;
        try{
        	URL url = new URL(SERVICEURL);
        	response = call.invoke(url,OBJECTURI+"!"+methodName);
        }catch(Exception e){
        	e.printStackTrace();
        }
        return response;
	}
}
