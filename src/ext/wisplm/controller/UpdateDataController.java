package ext.wisplm.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping({ "/UpdateDataController" })
public class UpdateDataController {
	private static final String CLASSNAME = UpdateDataController.class
			.getName();

	@RequestMapping(method = { RequestMethod.POST, RequestMethod.GET })
	public void precessRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html;charset=utf-8");
		response.setContentType("application/json");
		String oid = request.getParameter("ggdOid");
		String ggdVersion = request.getParameter("ggdVersion");
		String exchangeType = request.getParameter("exchangeType");
		String exchangeStartTime = request.getParameter("exchangeStartTime");
		String technologyFileName = request.getParameter("technologyFileName");
		if ("1".equals(oid)) {
			JSONObject obj = new JSONObject();
			obj.put("result", "success");
			obj.put("messgae", "成功");
			response.getWriter().write(obj.toJSONString());
		} else if ("33".equals(ggdVersion)) {
			JSONObject obj = new JSONObject();
			obj.put("result", "success");
			obj.put("messgae", "");
			response.getWriter().write(obj.toJSONString());
		} else if ("exchangeType".equals(exchangeType)) {
			JSONObject obj = new JSONObject();
			obj.put("result", "success");
			obj.put("messgae", "");
			response.getWriter().write(obj.toJSONString());
		} else if ("2020-06-29".equals(exchangeStartTime)) {
			JSONObject obj = new JSONObject();
			obj.put("result", "success");
			obj.put("messgae", "");
			response.getWriter().write(obj.toJSONString());
		} else if ("工艺文件".equals(technologyFileName)) {
			JSONObject obj = new JSONObject();
			obj.put("result", "success");
			obj.put("messgae", "");
			response.getWriter().write(obj.toJSONString());
		} else {
			JSONObject obj = new JSONObject();
			obj.put("result", "fail");
			obj.put("messgae", "失败原因");
			response.getWriter().write(obj.toJSONString());
		}

	}

}
