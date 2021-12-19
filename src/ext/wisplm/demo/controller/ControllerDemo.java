package ext.wisplm.demo.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;

/**
 * Controller示例
 * 访问路径为http://域名/Windchill/ptc1/{@RequestMapping}值
 * http://wisplm.com/Windchill/ptc1/ControllerDemo?name=bzh
 * 需在codebase/config/mvc/custom.xml中配置扫描该路径
 * <context:component-scan base-package = "ext.wisplm"/>
 * 
 *Zhong Binpeng Jul 20, 2020
 */
@Controller
@RequestMapping({ "/ControllerDemo" })
public class ControllerDemo {
	private static final String CLASSNAME = ControllerDemo.class.getName();
	private static final Logger logger    = Logger.getLogger(CLASSNAME);

	@RequestMapping(method = { RequestMethod.POST, RequestMethod.GET })
	public void precessRequest(HttpServletRequest request,HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setContentType("application/json");
		String name = request.getParameter("name");
		if ("bzh".equals(name)) {
			JSONObject obj = new JSONObject();
			obj.put("result", "success");
			obj.put("messgae", "成功");
			response.getWriter().write(obj.toJSONString());
		}else {
			JSONObject obj = new JSONObject();
			obj.put("result", "fail");
			obj.put("messgae", "失败原因");
			response.getWriter().write(obj.toJSONString());
		}

	}

}
