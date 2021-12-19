package ext.wis.webservice.javaxjws;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.ptc.jws.servlet.InfoEngineWebService;

/**
 *基于web Servlet发布的webservice
 *Zhong Binpeng Jul 16, 2021
 *访问地址:http://wisplm.com/Windchill/servlet/WISService?wsdl
 *codebase/web-inf/sun-jaxws.xml中增加以下内容发布webservice
 *implementation：webservice服务类的完整类名
 *name          : webservice的名称,自定义
 *url-pattern   : webservice的访问地址(相对路径,访问时前面增加web项目访问地址:http://wisplm.com/Windchill),必须以/servlet开头
 *
	<endpoint implementation="ext.wis.webservice.javaxjws.JWSServer"
             name="WISService"
             url-pattern="/servlet/WISService">
		    <jws:handler-chains>
	    	   <jws:handler-chain>
	         	<jws:handler>
					 <jws:handler-class>com.ptc.jws.security.impl.WebServerAuthenticated</jws:handler-class>
	            </jws:handler>
	         </jws:handler-chain>
      </jws:handler-chains>
	  </endpoint>
	  
 */

/**
 * targetNamespace = wsdl根节点中的targetNamespace,如果不定义则默认为包名逆序:http://javaxjws.webservice.wis.ext/
 * serviceName     = wsdl根节点中的name、service节点的 name
 * portName        = wsdl service节点下port节点的name
 *Zhong Binpeng Jul 16, 2021
 */
@WebService(targetNamespace = "http://service.wisplm.com/", portName = "WISServicePort", serviceName = "WISService")
public class WISService  extends InfoEngineWebService {

    public WISService() {
        super("ext.wis.webservice.javaxjws");
    }
    
	protected WISService(String arg0) {
		super(arg0);
	}
	
	/**
	 * action		 = wsdl中的soapAction,如果不定义则wsdl中的soapAction=""
	 * operationName = wsdl中的operation name
	 * @description
	 * @param docNumber
	 * @param docName
	 * @return
	 */
    @WebMethod(operationName = "getWISDocument",action="getWISDocument")
    public String getWISDocument(@WebParam(name = "docNumber")String docNumber,@WebParam(name = "docName")String docName){
    	return "返回文档信息";
    }
    
    @WebMethod(operationName = "getWISPart")
    public String getWISPart(@WebParam(name = "partNumber")String partNumber,@WebParam(name = "partName")String docName){
    	return "返回部件信息";
    }
}
