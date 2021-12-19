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
 * @version
 */
@Controller
@RequestMapping({ "/ChangeObjectSummaryQueryController" })
public class ChangeObjectSummaryQueryController {
	private static final String CLASSNAME = ChangeObjectSummaryQueryController.class.getName();

	@RequestMapping(method = { RequestMethod.POST, RequestMethod.GET })
	public void precessRequest(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		String action = request.getParameter("action");
		JSONArray result = new JSONArray();
		try {
			if ("ChangeObjectSummaryQuery".equals(action)) {// 表格查
				String json = "[{ggdNumber:\"123\",ggdName:\"123\",ggdVersion:\"123\",objName:\"123\",objNumber:\"123\",objVersion:\"123\",objType:\"123\",objState:\"123\",parentNumber:\"123\",parentName:\"123\",parentVersion:\"123\",oid:\"123\"}]";
				response.getWriter().write(json);
			}
			if ("InitDataObjType".equals(action)) {// 对象类型查询初始化
				Map map = new HashMap<>();
				map.put("name", "对象类型");
				map.put("value", "0");
				result.add(map);
				response.getWriter().write(result.toJSONString());
			}
			if ("InitDataPlaneType".equals(action)) {// 机型查询初始化
				Map map = new HashMap<>();
				map.put("name", "机型");
				map.put("value", "0");
				result.add(map);
				response.getWriter().write(result.toJSONString());
			}
			if ("InitDataXiaYouUnit".equals(action)) {// 机型查询初始化
				Map map = new HashMap<>();
				map.put("name", "下游单位");
				map.put("value", "0");
				result.add(map);
				response.getWriter().write(result.toJSONString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
