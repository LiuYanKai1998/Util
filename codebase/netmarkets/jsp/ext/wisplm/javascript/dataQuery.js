/**
 * @version V1.0
 * @Describe:设计更改贯彻跟踪查询界面
 * @author: zxh
 * @Date：Created in 2020/6/22 14:59
 * @Modified By:
 */
Ext.ns('App.hl.util');
//改单详细信息窗口变量
var newdataInfo = "";
Ext.onReady(function() {
	Ext.QuickTips.init();
	var servletUrl='http://' + window.location.host+ '/Windchill/ptc1/';
	
	// *************************************Model*******************************************************************
	
	// 下拉列表字段对应关系
	var typeFields = [{name:'name',mapping:'name'},{name:'value',mapping:'value'}];
	//表格model
	Ext.define('tableField', {
	    extend: 'Ext.data.Model',
	    fields: [
 	             {name : 'ggdNumber',mapping : 'ggdNumber'}, 
 	             {name : 'ggdName',mapping : 'ggdName'}, 
 	             {name : 'ggdVersion',mapping : 'ggdVersion'}, 
 	             {name : 'ggdEff',mapping : 'ggdEff'},
 	             {name : 'xiaYouUnit',mapping : 'xiaYouUnit'}, 
 	             {name : 'ggdKind',mapping : 'ggdKind'}, 
 	             {name : 'oid',mapping : 'oid'}, 
 	             {name : 'exchangeState',mapping : 'exchangeState'}
 	            ]
	});
	
	// *************************************Store*******************************************************************

	//表格store
	var tableStore = new Ext.data.Store({
			model: 'tableField',
			pageSize : 20,
			proxy : new Ext.data.HttpProxy({
				type: 'ajax',
				url : servletUrl+'searchDataController?',
				reader : new Ext.data.JsonReader({
					totalProperty : 'totalProperty',
					root : 'root'
				})
			})
	});
	//加载更改单类型列表
	var ggdTypeStore = new Ext.data.JsonStore({
		proxy : new Ext.data.HttpProxy({url : servletUrl+'InitDataController?action=ggdType'}),
		fields:typeFields
	});
	ggdTypeStore.load();
	
	// 加载工艺贯彻状态列表
	var gongYiGuanCheStateStore = new Ext.data.JsonStore({
		proxy : new Ext.data.HttpProxy({url : servletUrl+'InitDataController?action=gongYiGuanCheState'}),
		fields:typeFields
	});
	gongYiGuanCheStateStore.load();
	
	// 加载下游单位列表
	var xiaYouUnitStore = new Ext.data.JsonStore({
		proxy : new Ext.data.HttpProxy({url : servletUrl+'InitDataController?action=xiaYouUnit'}),
		fields:typeFields
	});
	xiaYouUnitStore.load();
	
	// 加载实物贯彻状态列表
	var shiWuGuanCheStateStore = new Ext.data.JsonStore({
		proxy : new Ext.data.HttpProxy({url : servletUrl+'InitDataController?action=shiWuGuanCheState'}),
		fields:typeFields
	});
	shiWuGuanCheStateStore.load();
	
	// 加载更改单据种类列表
	var changeTypeStore = new Ext.data.JsonStore({
		proxy : new Ext.data.HttpProxy({url : servletUrl+'InitDataController?action=changeType'}),
		fields:typeFields
	});
	changeTypeStore.load();
	
	// 加载有效状态列表
	var youXiaoStateStore = new Ext.data.JsonStore({
		proxy : new Ext.data.HttpProxy({url : servletUrl+'InitDataController?action=youXiaoState'}),
		fields:typeFields
	});
	youXiaoStateStore.load();
	
	// *************************************formPanel*******************************************************************
	
	var form = Ext.create('Ext.form.Panel', {
		id:'formPanel',
		region : 'north',
		bodyPadding : '5 5 5 150 ',
		layout : 'column',
		margin:'0 100 20 100',
		frame : true,
		title : '设计更改贯彻跟踪查询',
		fieldDefaults : {labelAlign : 'right',msgTarget : 'side',labelStyle:'font-weight:bold'},
		items : [ 
		           //第一列
		           {
					  columnWidth : .2,
					  xtype : 'container',
					  algin : 'center',
					  layout : 'form',
				      defaultType : 'combo',
			          items : [ 
			            {
							xtype : 'textfield',
							name : 'ggdNumber',
							fieldLabel : "更改单据编号",
							disableKeyFilter : true
						}, 
						{
							name : 'ggdType',
							fieldLabel : "更改单据类型",
							displayField : 'name',
							valueField : 'value',
							mode : 'local',
							editable : false,
							store : ggdTypeStore,
							forceSelection:true,
							disableKeyFilter : true
						},
						{
							name : 'xiaYouUnit',
							fieldLabel : "下游单位",
							displayField : 'name',
							editable : false,
							valueField : 'value',
							queryMode : 'local',
							store:xiaYouUnitStore,
							forceSelection:true,
							disableKeyFilter : true
						}
						 ]
		           	},
		            //第二列
			          { 
			        	  columnWidth : .2,
						  xtype : 'container',
						  algin : 'center',
						  layout : 'form',
						  defaultType : 'combo',
				          items : [ {
								xtype : 'textfield',
								name : 'ggdName',
								fieldLabel : "更改单据名称",
								disableKeyFilter : true
				          	},
				          	{
								name : 'gongYiGuanCheState',
								fieldLabel : "工艺贯彻状态",
								displayField : 'name',
								editable : false,
								valueField : 'value',
								queryMode : 'local',
								 store:gongYiGuanCheStateStore,
								 forceSelection:true,
								disableKeyFilter : true
							}
				          	]
			           }, 
			         //第三 列
			          { 
			        	  columnWidth : .2,
						  xtype : 'container',
						  algin : 'center',
						  layout : 'form',
						  defaultType : 'combo',
				          items : [{
								name : 'youXiaoState',
								fieldLabel : "有效状态",
								displayField : 'name',
								valueField : 'value',
								queryMode : 'local',
								editable : false,
								store:youXiaoStateStore,
								forceSelection:true,
								disableKeyFilter : true
				          	},
				          	{
								name : 'shiWuGuanCheState',
								fieldLabel : "实物贯彻状态",
								displayField : 'name',
								editable : false,
								valueField : 'value',
								queryMode : 'local',
								store:shiWuGuanCheStateStore,
								forceSelection:true,
								disableKeyFilter : true
							}]
			           }, 
		           	//第四列
		           	{
						columnWidth : .25,
						xtype : 'container',
						layout : 'form',
						defaultType : 'combo',
						items : [ 
						          	{
										name : 'changeType',
										fieldLabel : "更改单据种类",
										displayField : 'name',
										valueField : 'value',
										queryMode : 'local',
										editable : false,
										store:changeTypeStore,
										forceSelection:true,
										disableKeyFilter : true
						          	}, 
						          	{
										xtype : 'fieldcontainer',
										fieldLabel : '发放日期',
										layout : 'column',
										items : [ 
										          {
														columnWidth : .5,
														xtype : 'datefield',
														name : 'faFangStartDate',
														editable : false,
														format : 'Y-m-d',
														submitFormat:'Y-m-d',
														disableKeyFilter : true
										          }, 
										          {
														xtype : 'displayfield',
														value : '<b>~</b>'
										          }, 
										          {
														columnWidth : .5,
														xtype : 'datefield',
														editable : false,
														name : 'faFangEndDate',
														format : 'Y-m-d',
														submitFormat:'Y-m-d',
														disableKeyFilter : true
										          	} ]
					          				} ]
           					} ],
           						buttonAlign : 'center',
								buttons : [ {
									text : '搜索',
									handler:function(){
										//获取表单参数
										var formParams = Ext.getCmp('formPanel').getForm().getValues();
										//非空校验
										if(formParams.ggdNumber == "" && formParams.ggdName == "" && formParams.ggdName == "" && formParams.ggdType == "" && formParams.changeType== "" && formParams.gongYiGuanCheState == "" && formParams.youXiaoState== "" && formParams.xiaYouUnit=="" && formParams.faFangStartDate=="" &&  formParams.faFangEndDate== "" &&  formParams.shiWuGuanCheState=="" )
										{
											Ext.Msg.alert("提示","请填写查询条件");
											return
										}
										else{ 
												tableStore.load({
												 //传参
							                    params: {
							                    	ggdNumber: formParams.ggdNumber,
													ggdName: formParams.ggdName,
													ggdType: formParams.ggdType,
													changeType: formParams.changeType,
													gongYiGuanCheState: formParams.gongYiGuanCheState,
													youXiaoState: formParams.youXiaoState,
													xiaYouUnit: formParams.xiaYouUnit,
													faFangStartDate: formParams.faFangStartDate,
													faFangEndDate: formParams.faFangEndDate,
													shiWuGuanCheState: formParams.shiWuGuanCheState
						                    }
						                });}
									}
								}, {
									text : '重置',
									handler:function(){
										this.up('form').getForm().reset();
									}
								} ]
	});
	// *************************************gridPanel*******************************************************************
	
	//分页bar
	var bar = Ext.create('Ext.PagingToolbar', {
		store: tableStore,
		displayInfo : true,
		displayMsg:'显示第{0}-{1}条，共{2}条',
		emptyMsg:'没有记录'
	});
	//查询结果表格
	var table = Ext.create('Ext.grid.Panel', {
		store:tableStore,
		margin:'5 100 20 100 ',
		viewConfig:{enableTextSelection:true},
		region : 'center',
		columnLines : true,
		forceFit:false,
		title : '查询结果',
		frame : true,
		//分页
		bbar : bar,
		columns : [ {
			name:'ggdNumber',
//			style: { marginTop: '5px'},
			header : "更改单据编号",
			flex:1,
//			width:150,
//			padding:5,
			style:'text-align:center',
			align:'center',
			sortable : true,
			dataIndex : 'ggdNumber',
			//显示超链接
			renderer : function(data, metadata, record,rowIndex, columnIndex, store) {
			var value = store.getAt(rowIndex).get('ggdNumber');
			var oid = store.getAt(rowIndex).get('oid');
			return '<a href="javascript:void(0);" onclick="openWin('+oid+')">'+ value+ '</a>';
		}
		}, {
			name:"ggdName",
			header : "更改单据名称",
//			width:150,
			flex:3,
//			style:'text-align:center',
			align:'center',
			sortable : true,
			dataIndex : 'ggdName'
		}, {
			name:"ggdVersion",
			header : "更改单据版本",
//			width:150,
			flex:.6,
//			style:'text-align:center',
			align:'center',
			sortable : false,
			dataIndex : 'ggdVersion'
		}, {
			name:"ggdEff",
			header : "单据有效性",
//			style:'text-align:center',
			align:'center',
//			width:150,
			flex:1,
			sortable : false,
			dataIndex : 'ggdEff'
		}, {
			name:"xiaYouUnit",
			header : "下游单位",
//			width:150,
			flex:.5,
//			style:'text-align:center',
			align:'center',
			sortable : true,
			dataIndex : 'xiaYouUnit'
		}, {
			name:"ggdKind",
			header : "单据种类",
//			width:150,
			flex:.5,
//			style:'text-align:center',
			align:'center',
			sortable : false,
			dataIndex : 'ggdKind'
		}, {
			name:"exchangeState",
			header : "贯彻状态",
//			width:150,
			flex:1,
//			style:'text-align:center',
			align:'center',			
			sortable : false,
			dataIndex : 'exchangeState'
		},{
			name:"oid",
			header : "oid",
			width : 160,
			hidden:true,
			dataIndex : 'oid'
		} ]
	});
	
	// *************************************viewport*******************************************************************
	
	var view = Ext.create('Ext.Viewport', {
		layout : 'border',
		items : [ form, table ]
	});
});

//****************************************************************************************************

//超链接点击事件
function openWin(oid){
//	alert(oid);
        	 if (!newdataInfo) {
        	    	newdataInfo = new App.hl.util.newdataInfo({oid:oid});
        	    }
        	 newdataInfo.show();
     }

   


