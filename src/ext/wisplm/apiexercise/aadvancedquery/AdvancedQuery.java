package ext.wisplm.apiexercise.aadvancedquery;

import wt.doc.WTDocument;
import wt.doc.WTDocumentMaster;
import wt.fc.PersistenceHelper;
import wt.fc.QueryResult;
import wt.method.RemoteAccess;
import wt.method.RemoteMethodServer;
import wt.query.AttributeRange;
import wt.query.QueryException;
import wt.query.QuerySpec;
import wt.query.SearchCondition;
import wt.util.WTAttributeNameIfc;
import wt.util.WTException;

import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;
import java.sql.Timestamp;

/**
 * 高级查询示例
 * 按以下条件查询wtdocument和wtdocumentMaster
 * 1、生命周期状态为WISYIFABU或INWORK
 * 2、更新的起止时间,日期的起止时间2021/07/16
 * 
 *
 */
public class AdvancedQuery implements RemoteAccess {

    //测试
    public static void main(String[] args) throws InvocationTargetException, RemoteException, WTException {
        if(!RemoteMethodServer.ServerFlag) { //判断当前是否在methodServer里边运行
            String strT1 = args[0];
            String strT2 = args[1];

            System.out.println("起始时间：" + strT1);
            System.out.println("结束时间：" + strT2);

            RemoteMethodServer remoteMethodServer = RemoteMethodServer.getDefault();
            remoteMethodServer.setUserName("user1");
            remoteMethodServer.setPassword("123");
            //invoke传递的第三个参数如果是对象类型的则返回
            //其他任何类型的均为null
            remoteMethodServer.invoke("getAWTDocument",
            						  AdvancedQuery.class.getName(), null,
                                      new Class[] { String.class, String.class },
                                      new Object[] { strT1, strT2 });

        }
    }

    /**
     * 该方法用来查询生命周期状态为‘WISYIFABU’或‘INWORK’的文档
     *
     * @param strT1 查询起始时间 格式:2021/07/16
     * @param strT2 查询结束时间
     * @return 返回查询结果
     * @throws WTException
     */

    public static QueryResult getAWTDocument(String strT1, String strT2) throws WTException {
        System.out.println("From:" + strT1);
        System.out.println("To:" + strT2);
        //大部分高级查询要用到
        //参数为true以便支持多表查询
        QueryResult qr = new QueryResult();
        QuerySpec qs = new QuerySpec();
        qs.setAdvancedQueryEnabled(true);

        //多表查询，查询用到的表，
        //参数是true 则表示结果集会返回
        int iIndex1 = qs.appendClassList(WTDocumentMaster.class, true);
        int iIndex2 = qs.appendClassList(WTDocument.class,       false);

        SearchCondition scName =
                new SearchCondition(WTDocument.class, WTDocument.NAME, SearchCondition.LIKE, "%" + ".txt");
        qs.appendWhere(scName, new int[] { iIndex2 });

        //返回and (document.state='INWORK' OR document.state='WISYIFABU')
        //判断是否有查询条件，如果有则增加and连接
        if (qs.getConditionCount() > 0) {
            qs.appendAnd();
        }
        
        /**
         * 状态查询,对应sql: (state = 'WISYIFABU' or state='INWORK')
         *
         */
        qs.appendOpenParen();   //左括号
        SearchCondition sc_State1 =
                new SearchCondition(WTDocument.class, WTDocument.LIFE_CYCLE_STATE, SearchCondition.EQUAL, "WISYIFABU");
        qs.appendWhere(sc_State1, new int[] { iIndex2 });
        qs.appendOr();
        SearchCondition sc_State2 =
                new SearchCondition(WTDocument.class, WTDocument.LIFE_CYCLE_STATE, SearchCondition.EQUAL, "INWORK");
        qs.appendWhere(sc_State2, new int[] { iIndex2 });
        qs.appendCloseParen();     //右括号

        //关联查询
        //返回为WTDocument.idA3masterReference=WTDocumentMaster.idA2A2
        SearchCondition scJoinMaster =
                new SearchCondition(WTDocument.class, "masterReference.key.id", WTDocumentMaster.class,
                                    WTAttributeNameIfc.ID_NAME);
        /**
         * 返回的是已经存在的查询条件数量：int count = qs.getConditionCount(); 
		   select * from wtdocument       --count = 0;
		  select * from wtdocument where name like '%.txt'--count = 1;
		  如果count数量大于0,则后面再增加条件时需要先加and连接符 qs.appendAnd();
         */
        if (qs.getConditionCount() > 0) {
            qs.appendAnd();
            qs.appendWhere(scJoinMaster, new int[] { iIndex2, iIndex1 });
        }
        
        SearchCondition scTimeRange = getUpdateTimeRangeCondition(WTDocument.class, strT1, strT2);
        if (scTimeRange != null) {
            if (qs.getConditionCount() > 0) {
                qs.appendAnd();
            }
            qs.appendWhere(scTimeRange, new int[] { iIndex2 });
        }

        System.out.println("----------------------------");
        System.out.println(qs);
        qr = PersistenceHelper.manager.find(qs);
        System.out.println("QuerySize:" + qr.size());
        while (qr.hasMoreElements()) {
        	/**
        	 *      关联查询返回的是多种对象的组合，类似于Objs[0]返回第一种对象集合，objs[1]返回第二种对象的集合，其他类似
            		这里只返WTDocumentMaster对象的集合，因为只有它返回的值为true
        	 */
            Object[] objs = (Object[]) qr.nextElement();
            WTDocumentMaster docmaster = (WTDocumentMaster) objs[0];
            System.out.println("Name:" + docmaster.getName());
            System.out.println("Number:" + docmaster.getNumber());
        }
        return qr;
    }
    private static SearchCondition getUpdateTimeRangeCondition(Class search_class,String startData,String endData) throws
                                                                                                                   QueryException{
        Timestamp beginTimestamp=null;
        Timestamp endTimestamp=null;

        /*
        要考虑输入条件的多种情况
        1）只有开始时间
        2）只有结束时间
        3）输入时间格式的问题
         */
        if(startData !=null&&!startData.equals("")){
            startData=startData.replace('\\','-');
            startData=startData.replace('/','-');
            startData=startData+" 00:00:00.000000001";
            beginTimestamp=Timestamp.valueOf(startData);
            if(endData !=null&&!endData.equals("")){
                endData=endData.replace('\\','-');
                endData=endData.replace('/','-');
                endData=endData+" 23:59:59.999999999";
                endTimestamp=Timestamp.valueOf(endData);
            }else{
                endTimestamp=new Timestamp(System.currentTimeMillis());
            }

            AttributeRange timeRange=new AttributeRange(beginTimestamp,endTimestamp);
            if(timeRange!=null){
                SearchCondition scTimeRange=new SearchCondition(search_class,WTAttributeNameIfc.MODIFY_STAMP_NAME,true,timeRange);
                return scTimeRange;
            }
        }else{
            if(endData !=null&&!endData.equals("")){
                endData=endData.replace('\\','-');
                endData=endData.replace('/','-');
                endData=endData+" 23:59:59.999999999";
                endTimestamp=Timestamp.valueOf(endData);
                SearchCondition scTimeRange=new SearchCondition(search_class,WTAttributeNameIfc.MODIFY_STAMP_NAME,
                                                                SearchCondition.LESS_THAN_OR_EQUAL,endTimestamp);
                return scTimeRange;
            }
        }
        return null;
    }
}



