package ext.wisplm.demo.part;

import ext.wisplm.util.PartUtil;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.part.Quantity;
import wt.part.QuantityUnit;
import wt.part.WTPart;
import wt.part.WTPartMaster;
import wt.part.WTPartUsageLink;
import wt.util.WTException;

/**
 * @description
 * @author      ZhongBinpeng
 * @date        2020年4月19日
 */
public class WTPartUsageLinkDemo {

	public static void main(String args[]) throws Exception{
		
		//名称:部件使用关系,描述父部件与子部件的结构关系
		//1、如何创建WTPartUsageLink
		//1.1、获取父件
		WTPart parentPart = null;
		//1.2、获取子件
		WTPart childPart  = null;
		//1.3、获取子件Master
		WTPartMaster childPartMaster = (WTPartMaster) childPart.getMaster();
		//1.4、创建link
		WTPartUsageLink link = WTPartUsageLink.newWTPartUsageLink(parentPart, childPartMaster);
		//创建数量对象(数量,单位,EA表示个)
		Quantity quantity = Quantity.newQuantity(10, QuantityUnit.EA);
		//设置数量
		link.setQuantity(quantity);
		//1.5 保存
		PersistenceHelper.manager.save(link);
		//2、如何查询link
		//2.1通过父件查询link QueryResult为WTPartUsageLink的集合
		QueryResult qr = PartUtil.getWTPartUsageLink(parentPart);
		//2.2通过子件查询link,返回的是Map,key为父件,value为父子件的link
		PartUtil.getParentPartsAndUsageLink(childPart, null);
		//3、如何通过link读取父子件信息
		//3.1获取父件
		WTPart parentPart1 = link.getUsedBy();
		//3.2获取子件,返回的是子件master
		WTPartMaster childPartMaster1 = link.getUses();
		//3.3-1,获取Design视图子件方式1:通过子件Master获取Design视图最新版本(EBOM)
		WTPart childPart1 = PartUtil.getPartByConfig(childPartMaster1, PartUtil.DesignConfigSpec);
		//3.3-2,获取Design视图子件方式2:通过子件编号和Design视图
		childPart1 = PartUtil.getLatestPart(childPartMaster1.getNumber(),"Design");
		//3.4 link获取
		double amount = link.getQuantity().getAmount();
		//练习1,以GC000001为根节点,编写递归代码,获取整个EBOM结果部件,返回到List<WTPart>,最后输出集合确认信息无误
		//练习2,下载EBOM清册,操作入口为部件action
		//如何生成Excel
		
	}
}
