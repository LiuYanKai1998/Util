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
 * @Description TODO(工艺文件汇总查询)
 * @author wenhui yu
 * @Date 2020年6月29日 上午9:16:14
 * @version 1.0.0
 */
@Controller
@RequestMapping({ "/technologyFileSummaryQueryController" })
public class TechnologyFileSummaryQueryController {
	private static final String CLASSNAME = TechnologyFileSummaryQueryController.class.getName();

	@RequestMapping(method = { RequestMethod.POST, RequestMethod.GET })
	public void precessRequest(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		String action = request.getParameter("action");
		JSONArray result = new JSONArray();
		try {
			if ("technologyFileSummaryQuery".equals(action)) {// 表格查
				String json = "[{ggdNumber:\"123\",ggdName:\"123\",ggdVersion:\"123\",technologyFileName:\"123\",oid:\"123\"}]";
				response.getWriter().write(json);
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
