  /**
 * @version V1.0
 * @Describe:更改对象汇总查询
 * @author:wenhui yu
 * @Date：Created in 2020-06-28
 * @Modified By:
 */
var servletUrl = 'http://' + window.location.host + '/Windchill/ptc1/ChangeObjectSummaryQueryController?action=';
Ext.onReady(function() {
Ext.QuickTips.init();
//下拉列表 对象类型
var fieldObjType =[
             {name: 'name', mapping: 'name'},
  	        {name: 'value', mapping: 'value'}
          ];
var objTypeStore = new Ext.data.JsonStore({
    proxy: new Ext.data.HttpProxy({url: servletUrl+'InitDataObjType'}),
    fields :fieldObjType
});
objTypeStore.load();

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
            {header: "更改单据名称", width: 150, sortable: true, dataIndex: 'ggdName',align: 'center'},
            {header: "更改单据版本",width: 150,  sortable: true, dataIndex: 'ggdVersion',align: 'center'},
            {header: "名称", width: 200, sortable: true, dataIndex: 'objName',align: 'center'},
            {header: "编号",  sortable: true, dataIndex: 'objNumber',align: 'center'},
            {header: "版本",  sortable: true, dataIndex: 'objVersion',align: 'center'},
            {header: "类型",  sortable: true, dataIndex: 'objType',align: 'center'},
            {header: "状态",  sortable: true, dataIndex: 'objState',align: 'center'},
            {header: "父件名称",width: 200,  sortable: true, dataIndex: 'parentNumber',align: 'center'},
            {header: "父级编号", width: 130, sortable: true, dataIndex: 'parentName',align: 'center'},
            {header: "父级版本", width: 130,  sortable: true, dataIndex: 'parentVersion',align: 'center'},
            {header: "oid",  sortable: true, dataIndex: 'oid', hidden: true}
        ];
var field = [ 
            {name: 'ggdNumber', mapping: 'ggdNumber'},
 	        {name: 'ggdName', mapping: 'ggdName'},
 	        {name: 'ggdVersion', mapping: 'ggdVersion'},
 	        {name: 'objName', mapping: 'objName'},
 	        {name: 'objNumber', mapping: 'objNumber'},
 	        {name: 'objVersion', mapping: 'objVersion'},
 	        {name: 'objType', mapping: 'objType'},
 	        {name: 'objState', mapping: 'objState'},
 	        {name: 'parentNumber', mapping: 'parentNumber'},
 	        {name: 'parentName', mapping: 'parentName'},
 	        {name: 'parentVersion', mapping: 'parentVersion'},
 	        {name: 'oid', mapping: 'oid'}
         ];
var tableStore = new Ext.data.JsonStore({
    proxy: new Ext.data.HttpProxy({url: servletUrl+'ChangeObjectSummaryQuery'}),
    root   : 'records',
    totalProperty : 'total',
    fields : field
});
 var formPanel = new Ext.form.FormPanel({
	renderTo : Ext.getBody(),
	margin:'10,50,0,50',
	region : 'north',
	bodyPadding : '5 5 0 5 ',
	title : '更改对象汇总查询',
	fieldDefaults : {labelAlign : 'right',msgTarget : 'side',labelStyle:'font-weight:bold'},
	layout : 'form',
	items : [{
		layout : 'column',
		algin : 'center',
		border : false,
		items : [{columnWidth : .1,border : false},{
			columnWidth : .2,
			border : false,
			algin : 'center',
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
					fieldLabel : '创建时间',
					colspan:2,
					layout : 'column',
					items : [{
						columnWidth : .5,
						editable : false,
						xtype : 'datefield',
						name : 'ceateStartTime',
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
						name : 'ceateEndTime',
						format : 'Y-m-d',
						submitFormat : 'Y-m-d',
						disableKeyFilter : true
					}]
		    }]
		},{ columnWidth : .2,
		    	algin : 'center',
				layout : 'form',
				border : false,
				items : [
				    {
                    xtype : 'textfield',
					fieldLabel : '更改对象编号',
					name : 'objNumber'
				},{
                    xtype : 'combo',
                    editable : false,
					name : 'objType',
					fieldLabel : "对象类型",
					displayField : 'name',
					valueField : 'value',
					queryMode: 'local',
					store:objTypeStore,
					disableKeyFilter : true
		    	}]},{
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
				    	var objNumber = params.objNumber;
				    	var objType = params.objType;
				    	var planeType = params.planeType;
				    	var xiaYouUnit = params.xiaYouUnit;
				    	var ceateStartTime = params.ceateStartTime;
				    	var ceateEndTime = params.ceateEndTime;
				    	 if (ggdNumber == "" && ggdName == ""&&objNumber == "" &&ceateStartTime == null &&ceateEndTime==null&& xiaYouUnit == null&&planeType == null && objType == null ) {
				    		 Ext.Msg.alert('提示',"请添加查询条件");
			                    return;
			                }
				    	 tableStore.load({
			                    params: {
			                    	 ggdNumber: ggdNumber,
			                    	 ggdName:ggdName,
			                    	 objNumber:objNumber,
			                    	 objType:objType,
			                    	 planeType:planeType,
			                    	 xiaYouUnit:xiaYouUnit,
			                    	 ceateStartTime:ceateStartTime,
			                    	 ceateEndTime:ceateEndTime
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


	