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
 * \
 * 
 * @author wenhui yu
 *
 */

@Controller
@RequestMapping({ "/DeliveryTechnicalStatusController" })
public class DeliveryTechnicalStatusController {

	private static final String CLASSNAME = DeliveryTechnicalStatusController.class.getName();

	@RequestMapping(method = { RequestMethod.POST, RequestMethod.GET })
	public void precessRequest(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		String action = request.getParameter("action");
		String oid = request.getParameter("oid");
		JSONArray result = new JSONArray();
		try {
			// 查询交付技术状态
			if ("SearchDeliveryTechnicalStatus".equals(action)) {
				String json = "[{planeType:\"123\",jiaoFuState:\"123\",jiaoFuEff:\"123\",xiaYouUnit:\"123\",oid:\"123\"}]";
				response.getWriter().write(json);
			}
			// 插入或更新交付技术状态
			if ("insertOrUpdataDeliveryTechnicalStatus".equals(action)) {
				String json = "[{result:\"success\",messgae:\"成功\"}]";
				// String json = "[{result:\"fail\",messgae:\"失败时说明\"}]";
				response.getWriter().write(json);
			}
			// 下游单位下拉框初始化
			if ("InitDataXiaYouUnit".equals(action)) {
				Map map = new HashMap<>();
				map.put("name", "下游单位");
				map.put("value", "0");
				result.add(map);
				response.getWriter().write(result.toJSONString());
			}
			// 型号下拉框初始化
			if ("InitDataPlaneType".equals(action)) {
				Map map = new HashMap<>();
				map.put("name", "型号");
				map.put("value", "0");
				result.add(map);
				response.getWriter().write(result.toJSONString());
			}
			// 状态名称下拉框初始化
			if ("InitDataJiaoFuState".equals(action)) {
				Map map = new HashMap<>();
				map.put("name", "状态名称");
				map.put("value", "0");
				result.add(map);
				response.getWriter().write(result.toJSONString());
			}
			// 编辑查询根据oid
			if ("SearchDeliveryTechnicalStatusByOid".equals(action)) {
				String json = "[{planeType:\"123\",jiaoFuState:\"123\",jiaoFuEff:\"123\",xiaYouUnit:\"123\",oid:\"123\"}]";
				response.getWriter().write(json);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
