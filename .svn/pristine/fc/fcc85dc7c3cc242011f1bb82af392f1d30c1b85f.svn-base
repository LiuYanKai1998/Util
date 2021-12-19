package ext.wis.constants;

import java.io.File;

import ext.wisplm.util.WTUtil;
import wt.httpgw.URLFactory;
import wt.method.MethodContext;
import wt.part.WTPartConfigSpec;
import wt.part.WTPartStandardConfigSpec;
import wt.util.WTProperties;
import wt.vc.views.View;
import wt.vc.views.ViewHelper;

/**
 *
 *Zhong Binpeng Jun 23, 2021
 */
public class BusinessConstants {
	
	public static View             designView       = null;
	
	public static WTPartConfigSpec designViewConfig = null;
	//域名逆序
	public static String           domain   		= "";
	//Windchill系统根路径D:\ptc\Windchill_10.2\Windchill
	public static String           wthome   = "";
	//D:\ptc\Windchill_10.2\Windchill\codebase
	public static String           codebase = "";
	//D:\ptc\Windchill_10.2\Windchill\codebase\temp
	public static String           codebase_temp = "";
	//D:\ptc\Windchill_10.2\Windchill\codebase\temp
	public static String           wttemp   = "";
	
	//设计视图
	public static String VIEW_DESIGN = "Design";
	
	//系统web根路径
	public static String infoPageBasicURL = null;
	//http://xxx.com/Windchill/
	public static String baseURL          = null;
	public static String homePageURL      = null;
	
	
	static{
		try{
	        MethodContext mc = MethodContext.getContext(Thread.currentThread());
	        if (mc == null){
	        	mc = new MethodContext(null, null);
	        }
			designView = ViewHelper.service.getView("Design");
			designViewConfig= WTPartConfigSpec.newWTPartConfigSpec(WTPartStandardConfigSpec.newWTPartStandardConfigSpec(designView, null));
			WTProperties properties = WTProperties.getLocalProperties();
			wthome	 = properties.getProperty("wt.home");
			codebase = properties.getProperty("wt.codebase.location");
			codebase_temp = codebase + File.separator + "temp";
			wttemp   = properties.getProperty("wt.temp");
			domain   = WTUtil.getExchangeDomain();
			baseURL          = new URLFactory().getBaseURL().toString();
			infoPageBasicURL = baseURL + "app/#ptc1/tcomp/infoPage?oid=";
			homePageURL      = new URLFactory().getURL("#ptc1/homepage").toString();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static class IBA{
		//签审信息
		public static final String SIGNINFO = "SIGNINFO";
		//根节点部件
		public static final String ROOTPART = "ROOTPART";
		//材料牌号
		public static final String CLPH     = "CLPH";
		//热处理
		public static final String RCL      = "RCL";
		//关重件标识
		public static final String GZJBS    = "GZJBS";
	}
	
	public static class State{
		//正在工作
		public static final String INWORK              = "INWORK";
		
		public static final String WISTIJIAOSHENYUE    = "WISTIJIAOSHENYUE";
		//WIS正在审阅
		public static final String WISZHENGZAISHENYUE  = "WISZHENGZAISHENYUE";
		//WIS已发布
		public static final String WISYIFABU           = "WISYIFABU";
		public static final String WISYICHUANYUE       = "WISYICHUANYUE";
		public static final String WISXIUGAIZHONG      = "WISXIUGAIZHONG";
		public static final String WISYIQUXIAO         = "WISYIQUXIAO";
	}
	
	public static class Type{
		//EBOM签审包
		public static final String EBOMPACKAGE  = "wt.maturity.PromotionNotice|"+domain+".EBOMPackage";
	}
}
