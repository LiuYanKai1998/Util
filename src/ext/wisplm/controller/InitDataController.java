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
 * @version V1.0
 * @Describe:
 * @author: zxh
 * @Date：Created in 2020/06/23 00:23
 * @Modified By:
 */
@Controller
@RequestMapping({ "/InitDataController" })
public class InitDataController {
	private static final String CLASSNAME = InitDataController.class.getName();

	@RequestMapping(method = { RequestMethod.POST, RequestMethod.GET })
	public void precessRequest(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		String action = request.getParameter("action");
		try {
			if ("ggdType".equals(action)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", "类型");
				map.put("value", "001");
				JSONArray result = new JSONArray();
				result.add(map);
				response.getWriter().write(result.toJSONString());
			} else if ("gongYiGuanCheState".equals(action)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", "工艺贯彻状态");
				map.put("value", "001");
				JSONArray result = new JSONArray();
				result.add(map);
				response.getWriter().write(result.toJSONString());
			} else if ("xiaYouUnit".equals(action)) {
				Map<String, String> map1 = new HashMap<String, String>();
				map1.put("name", "下游");
				map1.put("value", "001");
				Map<String, String> map2 = new HashMap<String, String>();
				map2.put("name", "下游2");
				map2.put("value", "002");
				JSONArray result = new JSONArray();
				result.add(map1);
				result.add(map2);
				response.getWriter().write(result.toJSONString());
			} else if ("shiWuGuanCheState".equals(action)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", "实物贯彻状态");
				map.put("value", "001");
				JSONArray result = new JSONArray();
				result.add(map);
				response.getWriter().write(result.toJSONString());
			} else if ("changeType".equals(action)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", "更改单据种类");
				map.put("value", "001");
				JSONArray result = new JSONArray();
				result.add(map);
				response.getWriter().write(result.toJSONString());
			} else if ("youXiaoState".equals(action)) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("name", "有效状态");
				map.put("value", "001");
				JSONArray result = new JSONArray();
				result.add(map);
				response.getWriter().write(result.toJSONString());
			} else if ("exchangeState".equals(action)) {
				Map<String, String> map1 = new HashMap<String, String>();
				map1.put("name", "下游111");
				map1.put("value", "001");
				Map<String, String> map2 = new HashMap<String, String>();
				map2.put("name", "下游2222");
				map2.put("value", "002");
				JSONArray result = new JSONArray();
				result.add(map1);
				result.add(map2);
				response.getWriter().write(result.toJSONString());
			} else if ("ggdKind".equals(action)) {
				Map<String, String> map1 = new HashMap<String, String>();
				map1.put("name", "种类1");
				map1.put("value", "001");
				Map<String, String> map2 = new HashMap<String, String>();
				map2.put("name", "种类2");
				map2.put("value", "002");
				JSONArray result = new JSONArray();
				result.add(map1);
				result.add(map2);
				response.getWriter().write(result.toJSONString());
			}

			else if ("objType".equals(action)) {
				Map<String, String> map1 = new HashMap<String, String>();
				map1.put("name", "种类1");
				map1.put("value", "001");
				Map<String, String> map2 = new HashMap<String, String>();
				map2.put("name", "种类2");
				map2.put("value", "002");
				JSONArray result = new JSONArray();
				result.add(map1);
				result.add(map2);
				response.getWriter().write(result.toJSONString());
			}

			else if ("objState".equals(action)) {
				Map<String, String> map1 = new HashMap<String, String>();
				map1.put("name", "状态1");
				map1.put("value", "001");
				Map<String, String> map2 = new HashMap<String, String>();
				map2.put("name", "状态2");
				map2.put("value", "002");
				JSONArray result = new JSONArray();
				result.add(map1);
				result.add(map2);
				response.getWriter().write(result.toJSONString());
			}
			// 加载类别列表
			else if ("exchangeType".equals(action)) {
				Map<String, String> map1 = new HashMap<String, String>();
				map1.put("name", "类别3");
				map1.put("value", "003");
				Map<String, String> map2 = new HashMap<String, String>();
				map2.put("name", "类别4");
				map2.put("value", "004");
				JSONArray result = new JSONArray();
				result.add(map1);
				result.add(map2);
				response.getWriter().write(result.toJSONString());
			}
			// 加载工艺贯彻状态列表
			else if ("gongYiGuanCheState".equals(action)) {
				Map<String, String> map1 = new HashMap<String, String>();
				map1.put("name", "工作中1");
				map1.put("value", "001");
				Map<String, String> map2 = new HashMap<String, String>();
				map2.put("name", "工作中2");
				map2.put("value", "002");
				JSONArray result = new JSONArray();
				result.add(map1);
				result.add(map2);
				response.getWriter().write(result.toJSONString());
			}
			// 加载实物贯彻状态列表
			else if ("shiWuGuanCheState".equals(action)) {
				Map<String, String> map1 = new HashMap<String, String>();
				map1.put("name", "审签中1");
				map1.put("value", "001");
				Map<String, String> map2 = new HashMap<String, String>();
				map2.put("name", "审签2");
				map2.put("value", "002");
				JSONArray result = new JSONArray();
				result.add(map1);
				result.add(map2);
				response.getWriter().write(result.toJSONString());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
