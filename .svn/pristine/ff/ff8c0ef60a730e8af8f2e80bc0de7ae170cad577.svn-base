package ext.wis.ebom.processor;

import java.io.File;
import java.net.URL;
import java.util.List;

import com.ptc.core.components.forms.FormProcessingStatus;
import com.ptc.core.components.forms.FormResult;
import com.ptc.core.components.forms.FormResultAction;
import com.ptc.core.components.util.FeedbackMessage;
import com.ptc.netmarkets.model.NmObjectHelper;
import com.ptc.netmarkets.model.NmOid;
import com.ptc.netmarkets.util.beans.NmCommandBean;

import ext.wis.ebom.EBOMHelper;
import ext.wisplm.util.WISPLMConstants;
import ext.wisplm.util.WTMessageUtil;
import ext.wisplm.util.WfUtil;
import ext.wisplm.util.third.ExcelUtil;
import wt.part.WTPart;
import wt.util.WTException;

/**
 * 文件下载
 * @description
 * @author      ZhongBinpeng
 * @date        2020年4月19日
 */
public class DownloadEBOMListProcessor {
	
	public static FormResult download(NmCommandBean commandBean) throws WTException{
        FormResult formResult = new FormResult(FormProcessingStatus.SUCCESS);
        FeedbackMessage feedbackmessage;
        try{
    		NmOid nmOid  = commandBean.getActionOid();
    		WTPart part  = (WTPart) nmOid.getRef();
        	
        	List<String[]> finalResult = EBOMHelper.getEbomList(part);
        	
        	String fileName    = "EBOM_"+WfUtil.getCurrentTimeStr("yyyyMMddHHmmssSSS") + ".xls";
        	String xlsFilePath = WISPLMConstants.CODEBASE + "temp" + File.separator + fileName;
        	String reportHead = "EBOM清册";
        	String[] titles = {"序号","层级","编号","名称","版本","父件编号","单装数量"};
        	File xlsFile    = ExcelUtil.createExcel("EBOM清册", titles,1, finalResult, 2, xlsFilePath);
    		//1,构建文件下载地址
    		URL url = NmObjectHelper.constructOutputURL(xlsFile, xlsFile.getName());
    		//2,定义下一步执行js代码
    		formResult.setNextAction(FormResultAction.JAVASCRIPT);
    		//3,定义要执行的js代码内容
    		formResult.setJavascript("window.PTC.util.downloadUrl(\"" + url.toExternalForm() + "\");");
    		//WTMessageUtil.addSuccessFeedbackMessage(formResult, "ext.wisplm.demo.resources.CustomOperationRB", "DowloadFileSuccess");
    		return formResult;
        }catch(Exception e){
        	e.printStackTrace();
        	formResult = new FormResult(FormProcessingStatus.FAILURE);
        	WTMessageUtil.addFailureFeedbackMessage(formResult,"下载EBOM清册失败:" + e.getLocalizedMessage());
        	return formResult;
        }

	}



}
