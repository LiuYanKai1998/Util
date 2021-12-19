package ext.wisplm.common;

import java.io.File;

import ext.wisplm.util.WTUtil;
import wt.httpgw.URLFactory;
import wt.method.MethodContext;
import wt.part.WTPartConfigSpec;
import wt.part.WTPartStandardConfigSpec;
import wt.util.WTProperties;
import wt.vc.views.View;
import wt.vc.views.ViewHelper;

public class WISConstants {
		
	public static View             designView       = null;
	
	public static WTPartConfigSpec designViewConfig = null;
	//域名逆序
	public static String           domain   		= "";
	
	public static String           wthome   = "";
	
	public static String           codebase = "";
	public static String           codebase_temp = "";
	
	public static String           wttemp   = "";
	
	public static String           codebase_tempDownload = "";
	public static String           codebase_tempUpload = "";
	public static String           TEMP_DOWNLOAD = "tempDownload";
	public static String           TEMP_UPLOAD = "tempUpload";
	
	//设计视图
	public static String VIEW_DESIGN = "Design";
	
	//系统web根路径
	public static String webBaseURL = "";
	
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
			webBaseURL  = new URLFactory().getBaseURL().toString();
			
			codebase_tempDownload = codebase + File.separator + TEMP_DOWNLOAD;
			File temoDownload = new File(codebase_tempDownload);
			if(!temoDownload.exists()) {
			    temoDownload.mkdirs();
			}

			codebase_tempUpload = codebase + File.separator + TEMP_UPLOAD;
			File temoUpload = new File(codebase_tempUpload);
			if(!temoUpload.exists()) {
			    temoUpload.mkdirs();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	


		//顶层结构
		public  String TYPE_UPPERLEVEL 	 = "wt.part.WTPart|" + WISConstants.domain + ".UpperLevel";
		//技术附注
		public  String TYPE_TECHANNOTAION  = "wt.doc.WTDocument" + "|" + WISConstants.domain + ".TechAnnotation";

	

	    //IBA Key Definition
	    public static String IBA_MODEL = "model"; //型号

	

		//工作中
		public static  String STATE_INWORK  = "INWORK";
		//待审阅
		public static  String STATE_DSY  = "WIS_DSY";
		//修改中
		public static  String STATE_XGZ  = "WIS_XGZ";
		//审阅中
		public static  String STATE_SYZ  = "UNDERREVIEW";
	/*	//已批准待适航审定
		public static  String STATE_YPZDSHSD = "WIS_YPZDSHSD";*/
		//已发布
		public static  String STATE_YFB = "RELEASED";
	/*	//已冻结
		public static  String STATE_YDJ = "WIS_YDJ";*/
		//初始化构型层
		public static  String STATE_CSHGXC = "WIS_CSHGXC";
		//已取消
		public static  String STATE_CANCELLED = "CANCELLED";	
		//已停用
		public static  String STATE_YTY = "WIS_YTY";
		//已作废
		public static  String STATE_YZF = "WIS_YZF";
		
		//发放中
		public static  String STATE_FFZ = "WIS_FFZ";
		
		//已发放
		public static  String STATE_YFF = "WIS_YFF";
	
	
	public static  String CONNECTSTR = "-";
    public static  String UNDERLINE = "_";
    public static  String ADD = "+";
    public static  String COMMA = ",";
    public static  String LEFT_BRACKETS = "(";
    public static  String RIGHT_BRACKETS = ")";
    public static  String SPOT = ".";
    public static  String Y = "Y";
    public static  String N = "N";
    public static  String SPRIT = "/";
    public static  String DISPLAY_NAME = "displayName";
    public static  String TILDE = "~";
    public static  String OR_CHAR = "|";
    public static  String OID_OR_PREFIX = "OR:";
    public static  String VID_VR_PREFIX = "VR:";
    public static  String COLON = ":";
    public static  String GMT_8 = "GMT+8:00";
    public static  String TIME_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static  String DAY_DATE_FORMAT = "yyyy-MM-dd";
    public static  String A = "A";
    public static  String E = "E";
    public static  String F = "F";
    
    
    public static String STRING_SPLIT_STR=";;;qqq";
    

    
}
