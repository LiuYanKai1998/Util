  /**
 * @version V1.0
 * @Describe:工艺文件汇总查询
 * @author:wenhui yu
 * @Date：Created in 2020-06-28
 * @Modified By:
 */

var servletUrl = 'http://' + window.location.host + '/Windchill/ptc1/technologyFileSummaryQueryController?action=';
Ext.onReady(function() {
Ext.QuickTips.init();
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
            {header: "更改单据编号", width: 180, sortable: true, dataIndex: 'ggdNumber',align: 'center'},
            {header: "更改单据名称", width: 260, sortable: true, dataIndex: 'ggdName',align: 'center'},
            {header: "更改单据版本", width: 180, sortable: true, dataIndex: 'ggdVersion',align: 'center'},
            {header: "工艺文件名称", width: 260, sortable: true, dataIndex: 'technologyFileName',align: 'center'},
            {header: "oid",  sortable: true, dataIndex: 'oid', hidden: true}
        ];
var field = [ 
            {name: 'ggdNumber', mapping: 'ggdNumber'},
 	        {name: 'ggdName', mapping: 'ggdName'},
 	        {name: 'ggdVersion', mapping: 'ggdVersion'},
 	        {name: 'technologyFileName', mapping: 'technologyFileName'},
 	        {name: 'oid', mapping: 'oid'}
         ];
var tableStore = new Ext.data.JsonStore({
    proxy: new Ext.data.HttpProxy({url: servletUrl+'technologyFileSummaryQuery'}),
    root   : 'records',
    totalProperty : 'total',
    fields : field
});
 var formPanel = new Ext.form.FormPanel({
	renderTo : Ext.getBody(),
	region : 'north',
	margin:'10,50,10,50',
	bodyPadding : '5 5 5 5 ',
	fieldDefaults : {labelAlign : 'right',msgTarget : 'side',labelStyle:'font-weight:bold'},
	title : '工艺文件信息汇总',
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
				}]
		    },{
				columnWidth : .2,
				border : false,
				algin : 'center',
				layout : 'form',
				items : [
					{
	                    xtype : 'textfield',
						fieldLabel : '工艺文件名称',
						name : 'technologyFileName'
					},{
	                    xtype : 'combo',
	                    editable : false,
						name : 'planeType',
						fieldLabel : "机型",
						displayField : 'name',
						valueField : 'value',
						queryMode: 'local',
						store:planeTypeStore,
						disableKeyFilter : true
					}]},{
				columnWidth : .2,
				border : false,
				algin : 'center',
				layout : 'form',
				items : [{
	                    xtype : 'combo',
	                    editable : false,
						name : 'xiaYouUnit',
						fieldLabel : "下游单位",
						displayField : 'name',
						valueField : 'value',
						queryMode: 'local',
						store:xiaYouUnitStore,
						disableKeyFilter : true
					},{
						xtype : 'fieldcontainer',
						fieldLabel : '开始时间',
						layout:'column',
					    items : [{
									     columnWidth : .5,
									     editable : false,
									     xtype : 'datefield',
									     name : 'startTime',
									     format : 'Y-m-d',
									     submitFormat:'Y-m-d',
									     disableKeyFilter : true
									    }, {
									     xtype : 'displayfield',
									     value : '~'
									    }, {
									     columnWidth : .5,
									     editable : false,
									     xtype : 'datefield',
									     name : 'endTime',
									     format : 'Y-m-d',
									     submitFormat:'Y-m-d',
									     disableKeyFilter : true
						 }]
					    }
					]
			    },{columnWidth : .3,border : false}]
		    }],buttonAlign : 'center',
			buttons : [{
				text : '搜索',
			    handler:function () {
			    	var params = formPanel.getForm().getFieldValues();
			    	var ggdNumber = params.ggdNumber;
			    	var ggdName = params.ggdName;
			    	var technologyFileName = params.technologyFileName;
			    	var planeType = params.planeType;
			    	var xiaYouUnit = params.xiaYouUnit;
			    	var startTime = params.startTime;
			    	var endTime = params.endTime;
			    	if (ggdNumber == "" && ggdName == ""&&technologyFileName == "" &&startTime == null &&endTime==null&& xiaYouUnit == null&&planeType == null ) {
			    		 Ext.Msg.alert('提示',"请添加查询条件");
		                    return;
		                }
			    	 tableStore.load({
		                    params: {
		                    	 ggdNumber: ggdNumber,
		                    	 ggdName:ggdName,
		                    	 technologyFileName:technologyFileName,
		                    	 planeType:planeType,
		                    	 xiaYouUnit:xiaYouUnit,
		                    	 startTime:startTime,
		                    	 endTime:endTime
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
	pageSize : 20,
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


	