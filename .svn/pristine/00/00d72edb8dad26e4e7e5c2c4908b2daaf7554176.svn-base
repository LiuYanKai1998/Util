package ext.wisplm.demo.controller;

import com.alibaba.fastjson.JSONObject;
import ext.wisplm.vo.R;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * RestController示例
 * 访问路径为http://wisplm.com/Windchill/extRest/wisplm/RestControllerDemo/test?name=bzh
 * 需进行以下配置:
 * 1.codebase/WEB-INF/web_add.xml
 * 2.codebase/config/mvc/ext-rest-servlet.xml
 * 3.Apache配置如下,重启生效:
 * 1. 追加内容至{windchill.install}\HTTPServer\conf\extra\app-Windchill-AJP.conf
 * <IfModule mod_jk.c>
 * <p>
 * #Customization for extRest
 * JkMount /Windchill/extRest/* ajpWorker
 *
 * </IfModule>
 * <p>
 * 2. 追加内容至{windchill.install}\HTTPServer\conf\extra\app-Windchill-Auth.conf
 * <p>
 * # Customization for ext trusted URL
 * <LocationMatch ^/+Windchill/+extRest/+trusted(;.*)?>
 * Satisfy Any
 * Allow from all
 * </LocationMatch>
 * 3.重启Apache
 * <p>
 * <p>
 * Zhong Binpeng Jul 20, 2020
 */
@RestController
@RequestMapping({"/wisplm/RestControllerDemo"})
public class RestControllerDemo {
	private static final String CLASSNAME = ControllerDemo.class.getName();
	private static final Logger logger = Logger.getLogger(CLASSNAME);

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public Object test(HttpServletResponse response, @RequestParam(value = "name", defaultValue = "") String name) throws IOException {
		if ("bzh".equals(name)) {
			JSONObject obj = new JSONObject();
			obj.put("result", "success");
			obj.put("messgae", "成功");
			return obj;
		} else {
			JSONObject obj = new JSONObject();
			obj.put("result", "fail");
			obj.put("messgae", "失败原因");
			return obj;
		}
	}

	/**
	 * RESTful API接口设计标准及规范
	 * <p>
	 * 所有的查询请求，全部使用get请求，地址：http://wisplm.com/Windchill/extRest/wisplm/RestControllerDemo/find/bzh
	 *
	 * @param name
	 * @return
	 */
	@GetMapping(value = "/find/{name}")
	public R get(@PathVariable("name") String name) {
		//TODO 执行查询业务逻辑
		if ("bzh".equals(name)) {
			return R.ok("成功");
		} else {
			return R.failed("失败原因");
		}
	}

	/**
	 * RESTful API接口设计标准及规范
	 * <p>
	 * 所有的新增请求，全部使用post请求，地址：http://wisplm.com/Windchill/extRest/wisplm/RestControllerDemo/add/bzh
	 *
	 * @param name
	 * @return
	 */
	@PostMapping(value = "/add/{name}")
	public R post(@PathVariable("name") String name) {
		//TODO 执行添加业务逻辑
		if ("bzh".equals(name)) {
			return R.ok("成功");
		} else {
			return R.failed("失败原因");
		}
	}

	/**
	 * RESTful API接口设计标准及规范
	 * <p>
	 * 所有的修改请求，全部用put请求，地址：http://wisplm.com/Windchill/extRest/wisplm/RestControllerDemo/update/bzh
	 * put请求方式有些浏览器不支持
	 *
	 * @param name
	 * @return
	 */
	@PutMapping(value = "/update/{name}")
	public R put(@PathVariable("name") String name) {
		//TODO 执行修改业务逻辑
		if ("bzh".equals(name)) {
			return R.ok("成功");
		} else {
			return R.failed("失败原因");
		}
	}

	/**
	 * RESTful API接口设计标准及规范
	 * <p>
	 * 所有的删除请求，全部用delete请求，地址：http://wisplm.com/Windchill/extRest/wisplm/RestControllerDemo/delete/bzh
	 * delete请求方式有些浏览器不支持
	 *
	 * @param name
	 * @return
	 */
	@DeleteMapping(value = "/delete/{name}")
	public R delete(@PathVariable("name") String name) {
		//TODO 执行删除业务逻辑
		if ("bzh".equals(name)) {
			return R.ok("成功");
		} else {
			return R.failed("失败原因");
		}
	}
}
