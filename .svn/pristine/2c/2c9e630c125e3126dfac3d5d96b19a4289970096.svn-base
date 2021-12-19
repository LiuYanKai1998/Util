package ext.wisplm.demo.part;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ext.wisplm.util.PromotionNoticeUtil;
import ext.wisplm.util.WTUtil;
import wt.fc.WTObject;
import wt.maturity.Promotable;
import wt.maturity.PromotionNotice;
import wt.org.WTUser;
import wt.part.WTPart;
import wt.pom.Transaction;

/**
 * @description
 * @author      ZhongBinpeng
 * @date        2020年5月11日
 */
public class RefreshEBOMPackage {

	/**
	 * EBOM被驳回修改后再次提交审阅
	 * 刷新签审包中的部件及数模到最新版,同时从签审包移除EBOM上已不存在的部件及数模并设置其状态为"工作中"
	 * 对于借用件及其数模不改变状态
	 * @throws Exception 
	 */
	public  void refreshEBOMApprovalPackage(WTObject primaryBusinessObject) throws Exception{
		Transaction tx = null;
		try{
			tx = new Transaction();
			tx.start();
			//获取所有签审对象
			PromotionNotice pn 			 = (PromotionNotice) primaryBusinessObject;
			WTUser creator = (WTUser) ((PromotionNotice)primaryBusinessObject).getCreator().getObject();
			Set allSignObjects = new HashSet();
			//定义最新版EBOM结构对象的masterOid集合
			Set masterOids     = new HashSet();
			//获取所有顶层节点,此处为多个EBOM结构同时签审
			List<Object> topParts = null; 
			for(Object obj : topParts){
			    if(!(obj instanceof WTPart)){
			        continue;
			    }
			    WTPart topPart = (WTPart)obj;
			    //获取当前顶层节点下最新的EBOM结构
				Set<WTPart> ebomParts= null;
				
				ebomParts.add(topPart);
				allSignObjects.addAll(ebomParts);
				//通过ebomParts部件重新获取所有的EPMDocument
				allSignObjects.addAll(null);
			}			
			for(Object obj : allSignObjects){
				String masterOid = WTUtil.getObjectMasterOid((WTObject) obj);
				masterOids.add(masterOid);
			}
			
			//获取当前签审包里的对象
			ArrayList<Promotable> promotables = PromotionNoticeUtil.getPromotionNoticeItems(pn);
			
			for(Promotable promotable : promotables){
				if(!masterOids.contains(WTUtil.getObjectMasterOid((WTObject)promotable))){
					//非借用件,非ds件,获取最新版本设置状态为工作中
					if(promotable instanceof WTPart){
						//找到被移除的部件并重置状态为工作中
						WTPart part = (WTPart) promotable;
						//重置部件状态为工作中
					}
				}
				//WTDocument和EPMDocument处理方式相同
			}
			//清空签审包
			pn = PromotionNoticeUtil.removeAllPromotionNoticeItems(pn);
			//将最新版对象添加进去
			pn = PromotionNoticeUtil.addPromotionNoticeItem(pn, allSignObjects);
			
			tx.commit();
			tx = null;
		}finally{
			if(tx != null){
				tx.rollback();
			}
		}
	}
	
}
