  /**
 * @version V1.0
 * @Describe:贯彻信息汇总查询
 * @author:wenhui yu
 * @Date：Created in 2020-06-28
 * @Modified By:
 */

var servletUrl = 'http://' + window.location.host + '/Windchill/ptc1/implementInfoSummaryQueryController?action=';
Ext.onReady(function() {
Ext.QuickTips.init();
//下拉列表 贯彻类型
var fieldExchangeType =[
             {name: 'name', mapping: 'name'},
  	        {name: 'value', mapping: 'value'}
          ];
var exchangeTypeStore = new Ext.data.JsonStore({
    proxy: new Ext.data.HttpProxy({url: servletUrl+'InitDataExchangeType'}),
    fields :fieldExchangeType
});
exchangeTypeStore.load();

//下拉列表 贯彻状态
var fieldExchangeState =[
             {name: 'name', mapping: 'name'},
  	        {name: 'value', mapping: 'value'}
          ];
var exchangeStateStore = new Ext.data.JsonStore({
    proxy: new Ext.data.HttpProxy({url: servletUrl+'InitDataExchangeState'}),
    fields :fieldExchangeState
});
exchangeStateStore.load();

//下拉列表 机型
var fieldPlaneType =[
             {name: 'name', mapping: 'name'},
  	        {name: 'value', mapping: 'value'}
          ];
var planeTypeStore = new Ext.data.JsonStore({
    proxy: new Ext.data.HttpProxy({url: servletUrl+'InitDataPlaneType'}),
    fields :fieldPlaneType
});
planeTypeStore.load();

//下拉列表 下游单位查询
var fieldxiayou = [
             {name: 'name', mapping: 'name'},
  	        {name: 'value', mapping: 'value'}
          ];
var xiaYouUnitStore = new Ext.data.JsonStore({
    proxy: new Ext.data.HttpProxy({url: servletUrl+'InitDataXiaYouUnit'}),
    fields :fieldxiayou
});
xiaYouUnitStore.load();
//表格数据获取
var cols = [
            {header: "更改单据编号", width: 150, sortable: true, dataIndex: 'ggdNumber',align: 'center'},
            {header: "更改单据名称",  width: 150,sortable: true, dataIndex: 'ggdName',align: 'center'},
            {header: "更改单据版本", width: 150, sortable: true, dataIndex: 'ggdVersion',align: 'center'},
            {header: "类型", width: 150, sortable: true, dataIndex: 'exchangeType',align: 'center'},
            {header: "工艺贯彻状态", width: 150, sortable: true, dataIndex: 'gongYiGuanCheState',align: 'center'},
            {header: "实物贯彻状态", width: 150, sortable: true, dataIndex: 'shiWuFGuanCheState',align: 'center'},
            {header: "实物未贯彻架次",width: 150,  sortable: true, dataIndex: 'shiWUuWeiGuanCheEff',align: 'center'},
            {header: "贯彻开始时间", width: 150, sortable: true, dataIndex: 'startTime',align: 'center'},
            {header: "备注", width: 300, sortable: true, dataIndex: 'remark',align: 'center'},
            {header: "oid",  sortable: true, dataIndex: 'oid', hidden: true}
        ];
var field = [
            {name: 'ggdNumber', mapping: 'ggdNumber'},
 	        {name: 'ggdName', mapping: 'ggdName'},
 	        {name: 'ggdVersion', mapping: 'ggdVersion'},
 	        {name: 'exchangeType', mapping: 'exchangeType'},
 	        {name: 'gongYiGuanCheState', mapping: 'gongYiGuanCheState'},
 	        {name: 'shiWuFGuanCheState', mapping: 'shiWuFGuanCheState'},
 	        {name: 'shiWUuWeiGuanCheEff', mapping: 'shiWUuWeiGuanCheEff'},
 	        {name: 'startTime', mapping: 'startTime'},
 	        {name: 'remark', mapping: 'remark'},
 	        {name: 'oid', mapping: 'oid'}
         ];
var tableStore = new Ext.data.JsonStore({
    proxy: new Ext.data.HttpProxy({url: servletUrl+'implementInfoSummaryQuery'}),
    root   : 'records',
    totalProperty : 'total',
    fields : field
});
 var formPanel = new Ext.form.FormPanel({
	renderTo : Ext.getBody(),
	region : 'north',
	margin:'10,50,10,50',
	bodyPadding : '5 5 5 5 ',
	title : '贯彻信息汇总查询',
	fieldDefaults : {labelAlign : 'right',msgTarget : 'side',labelStyle:'font-weight:bold'},
	layout : 'form',
	items : [{
		layout : 'column',
		algin : 'center',
		border : false,
		items : [{columnWidth : .1,border : false},{
			columnWidth : .2,
			border : false,
			layout : 'form',
			items : [{
				xtype : 'textfield',
				fieldLabel : '更改单据编号',
				name : 'ggdNumber'
			},{
                    xtype : 'textfield',
					fieldLabel : '更改单据名称',
					name : 'ggdName'
				}, {
					xtype : 'fieldcontainer',
					fieldLabel : '贯彻开始时间',
					layout : 'column',
					items : [ {
						columnWidth : .5,
						xtype : 'datefield',
						editable : false,
						name : 'exchangeStartTime',
						format : 'Y-m-d',
						submitFormat : 'Y-m-d',
						disableKeyFilter : true
					}, {
						xtype : 'displayfield',
						value : '~'
					}, {
						columnWidth : .5,
						editable : false,
						xtype : 'datefield',
						name : 'exchangeEndTime',
						format : 'Y-m-d',
						submitFormat : 'Y-m-d',
						disableKeyFilter : true
					} ]
				} ]
		    },{
		    	columnWidth : .2,
		    	border : false,
		    	layout : 'form',
				items : [{
					xtype : 'combo',
					editable : false,
					name : 'exchangeType',
					fieldLabel : "类型",
					displayField : 'name',
					valueField : 'value',
					queryMode : 'local',
					store : exchangeTypeStore,
					disableKeyFilter : true
				}, {
					xtype : 'combo',
					editable : false,
					name : 'exchangeState',
					fieldLabel : "贯彻状态",
					displayField : 'name',
					valueField : 'value',
					queryMode : 'local',
					store : exchangeStateStore,
					disableKeyFilter : true
				}]
		    },{
				columnWidth : .2,
				border : false,
				algin : 'center',
				layout : 'form',
				items : [{
	                    xtype : 'combo',
	                    editable : false,
						name : 'planeType',
						fieldLabel : "机型",
						displayField : 'name',
						valueField : 'value',
						queryMode: 'local',
						store:planeTypeStore,
						disableKeyFilter : true
					},{
	                    xtype : 'combo',
	                    editable : false,
						name : 'xiaYouUnit',
						fieldLabel : "下游单位",
						displayField : 'name',
						valueField : 'value',
						queryMode: 'local',
						store:xiaYouUnitStore,
						disableKeyFilter : true
					}]
			    },{columnWidth : .1,border : false}]
		    }],
		    buttonAlign : 'center',
			buttons : [{
				text : '搜索',
			    handler:function () {
			    	var params = formPanel.getForm().getFieldValues();
			    	var ggdNumber = params.ggdNumber;
			    	var ggdName = params.ggdName;
			    	var exchangeState = params.exchangeState;
			    	var exchangeType = params.exchangeType;
			    	var exchangeState = params.exchangeState;
			    	var planeType = params.planeType;
			    	var xiaYouUnit = params.xiaYouUnit;
			    	var exchangeStartTime = params.exchangeStartTime;
			    	var exchangeEndTime = params.exchangeEndTime;
			    	if (ggdNumber == "" && ggdName == ""&&planeType ==null &&exchangeStartTime == null&&exchangeEndTime==null&& xiaYouUnit == null&&exchangeType == null && exchangeState == null ) {
			    		 Ext.Msg.alert('提示',"请添加查询条件");
		                    return;
		                }
			    	 tableStore.load({
		                    params: {
		                    	 ggdNumber: ggdNumber,
		                    	 ggdName:ggdName,
		                    	 exchangeState:exchangeState,
		                    	 exchangeType:exchangeType,
		                    	 planeType:planeType,
		                    	 xiaYouUnit:xiaYouUnit,
		                    	 exchangeStartTime:exchangeStartTime,
		                    	 exchangeEndTime:exchangeEndTime			                    	 
		                    }
		                });
			    	 }          
			}, {
				text : '重置',
				handler: function () {
					formPanel.getForm().reset();
			       }
			}]
		});
var bar = Ext.create('Ext.PagingToolbar', {
	pageSize : 10,
	store: tableStore,
	displayInfo : true,
	displayMsg:'显示第{0}-{1}条，共{2}条',
	emptyMsg:'没有记录'
});
// *************************************gridPanel*******************************************************************
	var table = Ext.create('Ext.grid.Panel', {
		region : 'center',
		margin:'10,50,10,50',
		columnLines : true,
		title : '查询结果',
		frame : false,
		bbar : bar,
		store:tableStore,
		columns: cols,
		viewConfig: {
		forceFit: true
		 }
	});
	// *************************************viewport*******************************************************************
	var view = Ext.create('Ext.Viewport', {
		layout : 'border',
		items : [formPanel,table]
	});
});


	