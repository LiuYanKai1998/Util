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
public class DownloadEBOMModelProcessor {
	
	public static FormResult download(NmCommandBean commandBean) throws WTException{
        FormResult formResult = null;
        try{
    		NmOid nmOid  = commandBean.getActionOid();
    		WTPart rootPart  = (WTPart) nmOid.getRef();
    		File zipFile     = EBOMHelper.downloadEbomModel(rootPart);
    		if(zipFile.exists()){
    			formResult = new FormResult(FormProcessingStatus.SUCCESS);
        		//1,构建文件下载地址
        		URL url = NmObjectHelper.constructOutputURL(zipFile, zipFile.getName());
        		//2,定义下一步执行js代码
        		formResult.setNextAction(FormResultAction.JAVASCRIPT);
        		//3,推送下载
        		formResult.setJavascript("window.PTC.util.downloadUrl(\"" + url.toExternalForm() + "\");");
    		}else{
    			formResult = new FormResult(FormProcessingStatus.FAILURE);
    			WTMessageUtil.addFailureFeedbackMessage(formResult,"下载EBOM模型失败,请联系管理员");
    		}
    		//WTMessageUtil.addSuccessFeedbackMessage(formResult, "ext.wisplm.demo.resources.CustomOperationRB", "DowloadFileSuccess");
    		return formResult;
        }catch(Exception e){
        	e.printStackTrace();
        	formResult = new FormResult(FormProcessingStatus.FAILURE);
        	WTMessageUtil.addFailureFeedbackMessage(formResult,"下载EBOM模型失败:" + e.getLocalizedMessage());
        	return formResult;
        }

	}



}
