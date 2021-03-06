package ext.wis.webservice;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.lang.StringEscapeUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * apache http访问Windchill soap接口-info*engine发布的webservice
 *
 *Zhong Binpeng Jul 16, 2021
 */
public class HttpClientForWindchill {
	
    public static String invokeWebservice() throws Exception {
        HttpClient httpclient = new HttpClient();
        httpclient.getState().setCredentials(AuthScope.ANY,new UsernamePasswordCredentials("soapuser", "soapuser"));
        PostMethod post = new PostMethod("http://plmtest.zhengfei.com/Windchill/servlet/RPC");
        /*类型标识,方法名*/
        post.setRequestHeader("soapAction", "urn:ie-soap-rpc:com.infoengine.soap!createDesignTask");
        StringBuffer soapResqustData = new StringBuffer();
        /**
		 封装的xml内容与pdm服务页面对应关系：
		1、xmlns:mes对应<definitions>节点里的
		 targetNamespace="http://www.ptc.com/infoengine/soap/rpc/message/"
		  和
		 xmlns:wc="http://www.ptc.com/infoengine/soap/rpc/message/"
		2、xmlns:xsd对应对应<definitions>节点里的
		 xmlns:xsd="http://www.w3.org/2001/XMLSchema"
		访问时封装的xml内容示例:
		 <soapenv:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
		 				   xmlns:xsd="http://www.w3.org/2001/XMLSchema" 
		 				   xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
		 				   xmlns:mes="http://www.ptc.com/infoengine/soap/rpc/message/">
		   <soapenv:Header/>
		   <soapenv:Body>
		   	<!--<mes:createDesignTask节点里为方法所需参数及值,createDesignTask为方法名,encodingStyle为该方法的encodingStyle-->
		      <mes:createDesignTask soapenv:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
		         <USERNAME xsi:type="xsd:string">1</USERNAME>
		         <TASKNAME xsi:type="xsd:string">2</TASKNAME>
		         <TASKID xsi:type="xsd:string">3</TASKID>
		         <PROJECTNAME xsi:type="xsd:string">4</PROJECTNAME>
		         <PROJECTID xsi:type="xsd:string">5</PROJECTID>
		         <DOCOID xsi:type="xsd:string">6</DOCOID>
		      </mes:createDesignTask>
		   </soapenv:Body>
		</soapenv:Envelope>
         */
        /**
                       返回结果示例:
         <SOAP-ENV:Envelope xmlns:SOAP-ENC="http://schemas.xmlsoap.org/soap/encoding/" xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:wc="http://www.ptc.com/infoengine/soap/rpc/message/" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" SOAP-ENV:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
			<SOAP-ENV:Header/>
			<SOAP-ENV:Body>
			<wc:createDesignTaskResponse>
			<Collection xsi:type="xsd:string">
			<!-- <Collection节点里才是windchill返回的xml内容,转义为纯字符串返回 -->
			&lt;wc:COLLECTION xmlns:wc=&quot;http://www.ptc.com/infoengine/1.0&quot;&gt;
			&lt;WINDCHILL NAME=&quot;output&quot; TYPE=&quot;Object&quot; STATUS=&quot;0&quot;&gt;
			  &lt;wc:INSTANCE&gt;
			    &lt;RESULT&gt;FAILURE&lt;/RESULT&gt;
			    &lt;INFO&gt;创建设计任务出错,请联系PDM系统管理员排查,null&lt;/INFO&gt;
			  &lt;/wc:INSTANCE&gt;
			&lt;/WINDCHILL&gt;
			&lt;/wc:COLLECTION&gt;
			</Collection></wc:createDesignTaskResponse></SOAP-ENV:Body></SOAP-ENV:Envelope>
         */
        soapResqustData.append(
                "<soapenv:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:mes=\"http://www.ptc.com/infoengine/soap/rpc/message/\">");
        soapResqustData.append("<soapenv:Header/>");
        soapResqustData.append("<soapenv:Body>");
        soapResqustData.append("<mes:createDesignTask soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">");
        /*以下内容为task所需参数名及值*/
        soapResqustData.append("<TASKNAME xsi:type=\"xsd:string\">122</TASKNAME>");
        soapResqustData.append("<USERNAME xsi:type=\"xsd:string\">1</USERNAME>");
        soapResqustData.append("<TASKID xsi:type=\"xsd:string\">133</TASKID>");
        soapResqustData.append("<PROJECTNAME xsi:type=\"xsd:string\">14</PROJECTNAME>");
        soapResqustData.append("<PROJECTID xsi:type=\"xsd:string\">14</PROJECTID>");
        soapResqustData.append("<DOCOID xsi:type=\"xsd:string\">5561</DOCOID>");
        /*参数封装结束*/
        soapResqustData.append("</mes:createDesignTask>");
        soapResqustData.append("</soapenv:Body>");
        soapResqustData.append("</soapenv:Envelope>");
        String soapResult = null;
        try {
            RequestEntity entity = new StringRequestEntity(soapResqustData.toString(), "text/xml", "utf-8");
            post.setRequestEntity(entity);
            httpclient.executeMethod(post);
            int code = post.getStatusCode();
            System.out.println(code);
            if (code == 200) { //200=HttpStatus.SC_OK
            	soapResult = new String(post.getResponseBodyAsString());
            }else{
            	throw new Exception("访问出错,错误代码:"+code);
            }
        } finally {
            post.releaseConnection();
        }
        return soapResult;
    }
    
    //解析http访问返回的xml内容,获取windchill返回的xml
    public static void parseSoapResult(String soapResult,String methodName) throws DocumentException{
        Document doc  = DocumentHelper.parseText(soapResult);
        Element root  = doc.getRootElement();
        Element body  = root.element("Body");
        Element eleResponse = body.element(methodName+"Response");//此处为方法名+Reponse
        Element eleWindchillResult= eleResponse.element("Collection");
		doc = DocumentHelper.parseText(eleWindchillResult.getTextTrim());
		root = doc.getRootElement();
		Element eleWindchill  = root.element("WINDCHILL");
		Element eleWCInstance = (Element) eleWindchill.elements().get(0);
		String result = eleWCInstance.element("RESULT").getTextTrim();
		String info   = eleWCInstance.element("INFO").getTextTrim();
		System.out.println("执行结果:result-->" + result + ",info-->" + info);
    }
    
    
	public static void main(String[] args) throws Exception {
		String soapResult = invokeWebservice();
		parseSoapResult(soapResult,"createDesignTask");
	}
}
