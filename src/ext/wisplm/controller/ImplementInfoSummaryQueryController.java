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
 * @Description TODO(贯彻信息汇总 查询)
 * @author wenhui yu
 * @Date 2020年6月28日 下午6:54:24
 * @version 1.0.0
 */
@Controller
@RequestMapping({ "/implementInfoSummaryQueryController" })
public class ImplementInfoSummaryQueryController {
	private static final String CLASSNAME = ImplementInfoSummaryQueryController.class.getName();

	@RequestMapping(method = { RequestMethod.POST, RequestMethod.GET })
	public void precessRequest(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		String action = request.getParameter("action");
		JSONArray result = new JSONArray();
		try {
			if ("implementInfoSummaryQuery".equals(action)) {// 表格查
				String json = "[{ggdNumber:\"123\",ggdName:\"123\",ggdVersion:\"123\",exchangeType:\"123\",gongYiGuanCheState:\"123\",shiWuFGuanCheState:\"123\",shiWUuWeiGuanCheEff:\"123\",startTime:\"123\",remark:\"123\",oid:\"123\"}]";
				response.getWriter().write(json);
			}
			if ("InitDataExchangeType".equals(action)) {// 贯彻类型查询初始化
				Map map = new HashMap<>();
				map.put("name", "贯彻类型");
				map.put("value", "0");
				result.add(map);
				response.getWriter().write(result.toJSONString());
			}
			if ("InitDataExchangeState".equals(action)) {// 贯彻状态查询初始化
				Map map = new HashMap<>();
				map.put("name", "贯彻状态");
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
