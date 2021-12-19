package ext.wisplm.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.drools.lang.dsl.DSLMapParser.variable_definition_return;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ptc.windchill.uwgm.common.prefs.res.newCadDocPrefsResource;

/**
 * @version V1.0
 * @Describe:
 * @author: zxh
 * @Date：Created in 2020/06/23 00:23
 * @Modified By:
 */
@Controller
@RequestMapping({ "/searchDataController" })
public class SearchDataController {
	private static final String CLASSNAME = SearchDataController.class
			.getName();

	@RequestMapping(method = { RequestMethod.POST, RequestMethod.GET })
	public void precessRequest(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("application/json");
		response.setContentType("text/html;charset=utf-8");

		Enumeration<String> names = request.getParameterNames();
		// 循环获取枚举中所有的内容
		while (names.hasMoreElements()) {
			// 获取每一个文本域的name
			String name = names.nextElement();
			// 根据name获得参数的值
			// 为了保证获取到所有的值 因为表单可能是单值 也可能是多值
			String[] values = request.getParameterValues(name);
			// 输出参数名和参数值
//			System.out.print(name + ":");
			for (String val : values) {
//				System.out.print(val);
			}
			// 换行
			System.out.println();
		}
		String oid = request.getParameter("oid");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		List<Map<String, String>> result = new ArrayList<Map<String, String>>();
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if ("1".equals(oid)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("ggdName", "1234567");
			map.put("ggdName", "123544455");
			map.put("ggdType", "123545465");
			map.put("designer", "12365656");
			map.put("exchangeState", "12387");
			map.put("ggdSketch", "123676");
			map.put("ggdReason", "123676");
			map.put("oid", "1237676");
			JSONObject s = new JSONObject();
			s.put("success", true);
			s.put("data", map);
			try {
				response.getWriter().write(s.toJSONString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if ("2".equals(oid)) {
			Map<String, String> map = new HashMap<String, String>();
			JSONArray s = new JSONArray();
			for (int i = 0; i < 10; i++) {
				Map<String, String> mapi = new HashMap<String, String>();
				mapi.put("objName", "1234567");
				mapi.put("objNumber", "123544455");
				mapi.put("objVersion", "123545465");
				mapi.put("objType", "12365656");
				mapi.put("objState", "12387");
				mapi.put("parentNumber", "123676");
				mapi.put("parentName", "123676");
				mapi.put("oid", "1237676");
				mapi.put("parentVersion", "1237676");
				// result.add(mapi);
				s.add(mapi);
			}
			try {
				response.getWriter().write(s.toJSONString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		else if ("3".equals(oid)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("exchangeType", "1234567");
			map.put("gongYiGuanCheState", "123544455");
			map.put("shiWuGuanCheState", "123545465");
			map.put("shiWuWeiGuanCheEff", "12365656");
			map.put("exchangeStartTime", "12387");
			map.put("remark", "123676");
			map.put("oid", "123676");
			JSONArray s = new JSONArray();
			s.add(map);
			try {
				response.getWriter().write(s.toJSONString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if ("4".equals(oid)) {

			Map<String, String> map = new HashMap<String, String>();
			map.put("technologyFileName", "1234567");
			map.put("oid", "123544455");
			JSONArray s = new JSONArray();
			s.add(map);
			try {
				response.getWriter().write(s.toJSONString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if ("5".equals(oid)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("ggdNumber", "11");
			map.put("ggdName", "22");
			map.put("ggdVersion", "33");
			map.put("ggdEff", "55");
			map.put("exchangeState", "001");
			map.put("ggdKind", "001");
			map.put("ggdType", "001");
			map.put("xiaYouUnit", "001");

			map.put("designer", "66");
			map.put("professional", "77");
			map.put("releaseDate", "2020-06-01");
			map.put("ggdSketch", "88");
			map.put("ggdReason", "99");
			map.put("remark", "1111");
			map.put("xiuGaiYuanYin", "2222");
			JSONObject s = new JSONObject();
			s.put("success", true);
			s.put("data", map);
			try {
				response.getWriter().write(s.toJSONString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		else if ("1237676".equals(oid)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("ggdOid", "1237676");
			map.put("objName", "123");
			map.put("objNumber", "1234");
			map.put("objVersion", "A.1");
			map.put("objType", "001");
			map.put("objState", "001");
			map.put("parentNumber", "f123");
			map.put("parentName", "f对象");
			map.put("parentVersion", "A.2");
			JSONObject s = new JSONObject();
			s.put("success", true);
			s.put("data", map);
			try {
				response.getWriter().write(s.toJSONString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		else if ("123676".equals(oid)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("exchangeType", "003");
			map.put("gongYiGuanCheState", "001");
			map.put("shiWuGuanCheState", "实物贯彻状态");
			map.put("shiWuWeiGuanCheEff", "实物未贯彻有效性");
			map.put("exchangeStartTime", "2020-06-29");
			map.put("xiuGaiYuanYin", "修改原因");
			JSONObject s = new JSONObject();
			s.put("success", true);
			s.put("data", map);
			try {
				response.getWriter().write(s.toJSONString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		else if ("123544455".equals(oid)) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("technologyFileName", "123");
			JSONObject s = new JSONObject();
			s.put("success", true);
			s.put("data", map);
			try {
				response.getWriter().write(s.toJSONString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
//			Map<String,String> data=new HashMap<String,String >();
			Map[] dataArray=new  Map[500];
			for (int i = 0; i < 500; i++) {
				Map<String, String> mapi = new HashMap<String, String>();
				
				mapi.put("ggdNumber", "GGD-000" + i);
				mapi.put("ggdName", "GGD-000"  + i);
				mapi.put("ggdVersion", "A."  +i);
				mapi.put("ggdEff",i + "-9999");
				mapi.put("xiaYouUnit", i + "厂");
				mapi.put("ggdKind", i + "类");
				mapi.put("exchangeState", "贯彻中" + "");
				mapi.put("oid", i + "");
				dataArray[i]=mapi;
//				result.add(mapi);
			}
			int startIndex=Integer.parseInt(start);
			int limitNum=Integer.parseInt(limit);
			int endIndex=startIndex+limitNum;
			for(int i=startIndex;i<endIndex;i++){
				list.add(dataArray[i]);
			}
			
			int total = 500;
			JSONObject map = new JSONObject();
			map.put("totalProperty", total);
			map.put("root", list);
			try {
				response.getWriter().write(map.toJSONString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
