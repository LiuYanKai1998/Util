package ext.wisplm.util;

import wt.httpgw.URLFactory;
import wt.util.WTProperties;

import java.io.File;

/**
 * 生命周期，工作流，节点名称相关常量类
 */
public class WISPLMConstants {
	public static String WT_HOME   = "";
	public static String CODEBASE = "";
	public static String WT_TEMP   = "";
	public static String BASEURL   = "";
	static {
		try {
			//codebase/wt.properties
			WTProperties properties = WTProperties.getLocalProperties();
			WT_HOME	 = properties.getProperty("wt.home") + File.separator;
			CODEBASE = properties.getProperty("wt.codebase.location") + File.separator;
			WT_TEMP   = properties.getProperty("wt.temp") + File.separator;
			BASEURL   = new URLFactory().getBaseURL().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
