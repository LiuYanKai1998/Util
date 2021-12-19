package ext.wisplm.demo.part.processor;

import java.io.File;
import java.net.URL;

import com.ptc.core.components.forms.FormProcessingStatus;
import com.ptc.core.components.forms.FormResult;
import com.ptc.core.components.forms.FormResultAction;
import com.ptc.core.components.util.FeedbackMessage;
import com.ptc.netmarkets.model.NmObjectHelper;
import com.ptc.netmarkets.util.beans.NmCommandBean;

import ext.wisplm.util.WTMessageUtil;
import wt.util.WTException;

/**
 * 文件下载
 * @description
 * @author      ZhongBinpeng
 * @date        2020年4月19日
 */
public class DownloadFileProcessor {
	
	public static FormResult download(NmCommandBean commandBean) throws WTException{
        FormResult formResult = new FormResult(FormProcessingStatus.SUCCESS);
        FeedbackMessage feedbackmessage;
        try{
        	String filePath = "";
    		File file = new File("D:\\temp\\iadebug.log");
    		//1,构建文件下载地址
    		URL url = NmObjectHelper.constructOutputURL(file, file.getName());
    		//2,定义下一步执行js代码
    		formResult.setNextAction(FormResultAction.JAVASCRIPT);
    		//3,定义要执行的js代码内容
    		formResult.setJavascript("window.PTC.util.downloadUrl(\"" + url.toExternalForm() + "\");");
    		WTMessageUtil.addSuccessFeedbackMessage(formResult, "ext.wisplm.demo.resources.CustomOperationRB", "DowloadFileSuccess");
    		return formResult;
        }catch(Exception e){
        	WTMessageUtil.addFailureFeedbackMessage(formResult,"下载文件失败:" + e.getLocalizedMessage());
        	return formResult;
        }

	}



}
