package ext.wisplm.demo.part.processor;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.ptc.core.components.forms.FormProcessingStatus;
import com.ptc.core.components.forms.FormResult;
import com.ptc.core.components.util.FeedbackMessage;
import com.ptc.core.ui.resources.FeedbackType;
import com.ptc.netmarkets.util.beans.NmCommandBean;
import com.ptc.netmarkets.util.misc.NmContext;

import wt.fc.ReferenceFactory;
import wt.fc.WTObject;
import wt.session.SessionHelper;
import wt.util.WTException;

/**
	自定义action processor示例(不继承DefaultObjectFormProcessor也可以)
 * action中class使用此类名
 * method使用此方法名(自定义即可),参数NmCommandBean为前端传过来的固定格式
 * 示例:
 * 		<action name="xxxxxx">
			<command class="ext.wisplm.demo.part.processor.TableToolBarActionProcessor" method="actionMethod" windowType="normal"/>
			<includeFilter name="SetStateFilter" />
		</action>
 */
/**
 * 表格工具栏菜单对应的processor类-获取表格选中对象示例
 */
public class MVCTableSelectedProcessor {

	private static final Logger logger = Logger.getLogger(MVCTableSelectedProcessor.class);
	
    public static FormResult actionMethod(NmCommandBean nmcommandbean) throws WTException {

        FormResult formResult = new FormResult();
        formResult.setStatus(FormProcessingStatus.SUCCESS);
        FeedbackMessage feedbackmessage;
        //获取mvc表格选中的对象oid
        ArrayList<NmContext> addItemOids = nmcommandbean.getSelectedContextsForPopup();
        logger.debug("选中的对象数量:" + addItemOids.size());
        ReferenceFactory rf = new ReferenceFactory();
        if (addItemOids != null && addItemOids.size() > 0) {
	       for (int i = 0; i < addItemOids.size(); i++) {
                NmContext ctext = (NmContext) addItemOids.get(i);
                //获取选中对象,转换为对应的持久化类即可
                WTObject obj = (WTObject) ctext.getTargetOid().getWtRef().getObject();
                logger.debug("TableToolBarActionProcessor获取到的对象:" + obj.getDisplayIdentity());   
	        }      
			feedbackmessage = new FeedbackMessage(FeedbackType.SUCCESS,
					SessionHelper.getLocale(), "", null, "操作成功");
			formResult.addFeedbackMessage(feedbackmessage);
        }
        return formResult;
    }
}
