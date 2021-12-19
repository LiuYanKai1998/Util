package ext.wisplm.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONArray;

/**
 * 
 * @author wenhui yu
 *
 */
@Controller
@RequestMapping({ "/DataMaintainHistoryController" })
public class SearchDataMaintainHistoryController {
	private static final String CLASSNAME = SearchDataMaintainHistoryController.class.getName();

	@RequestMapping(method = { RequestMethod.POST, RequestMethod.GET })
	public void precessRequest(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		String action = request.getParameter("action");
		JSONArray result = new JSONArray();
		try {
			if ("SearchDataMaintainHistory".equals(action)) {
				// 返回更改单编辑页面 结果写固定值
				String json = "[{ggdNumber:\"123\",updateType:\"123\",updateReason:\"123\",mender:\"123\",updateTime:\"123\"},{ggdNumber:\"123\",updateType:\"123\",updateReason:\"123\",mender:\"123\",updateTime:\"123\"},{ggdNumber:\"123\",updateType:\"123\",updateReason:\"123\",mender:\"123\",updateTime:\"123\"}]";
				response.getWriter().write(json);
			}
			if ("InitDataMaintainHistory".equals(action)) {
				// 返回更改单编辑页面 结果写固定值
				Map map = new HashMap<>();
				map.put("name", "类别");
				map.put("value", "0");
				result.add(map);
				response.getWriter().write(result.toJSONString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
