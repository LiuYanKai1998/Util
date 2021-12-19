  /**
 * @version V1.0
 * @Describe:系统数据维护历史查询界面
 * @author:wenhui yu
 * @Date：Created in 2020-06-23
 * @Modified By:
 */

var servletUrl = 'http://' + window.location.host + '/Windchill/ptc1/DataMaintainHistoryController?action=';
Ext.onReady(function() {
Ext.QuickTips.init();
//下拉列表 更改单据类型查询
var fieldtype = [
             {name: 'name', mapping: 'name'},
  	        {name: 'value', mapping: 'value'}
          ];
var ggdTypeStore = new Ext.data.JsonStore({
    proxy: new Ext.data.HttpProxy({url: servletUrl+'InitDataMaintainHistory'}),
    fields :fieldtype
});
ggdTypeStore.load();
//表格数据获取
var cols = [
            {header: "编号", width: 200, sortable: true, dataIndex: 'ggdNumber',align: 'center'},
            {header: "更改类型", width: 150, sortable: true, dataIndex: 'updateType',align: 'center'},
            {header: "备注", width: 350, sortable: true, dataIndex: 'updateReason',align: 'center'},
            {header: "更改者", width: 150, sortable: true, dataIndex: 'mender',align: 'center'},
            {header: "更改时间", width: 150, sortable: true, dataIndex: 'updateTime',align: 'center'}
        ];
var field = [
            {name: 'ggdNumber', mapping: 'ggdNumber'},
 	        {name: 'updateType', mapping: 'updateType'},
 	        {name: 'updateReason', mapping: 'updateReason'},
 	        {name: 'mender', mapping: 'mender'},
 	        {name: 'updateTime', mapping: 'updateTime'}
         ];
var tableStore = new Ext.data.JsonStore({
    proxy: new Ext.data.HttpProxy({url: servletUrl+'SearchDataMaintainHistory'}),
    root   : 'records',
    totalProperty : 'total',
    align: 'center',
	pageSize : 10,
    fields : field
});
 var formPanel = new Ext.form.FormPanel({
	renderTo : Ext.getBody(),
	region : 'north',
	bodyPadding : '5 5 5 5 ',
	margin:'10,50,10,50',
	buttonAlign:'right',
	title : '维护历史信息查询',
	layout : 'column',
	fieldDefaults : {labelAlign : 'right',msgTarget : 'side',labelStyle:'font-weight:bold'},
	items : [{
		    layout : 'form',
		    border : false,
			columnWidth : .2,
			items : [{
				xtype : 'textfield',
				fieldLabel : '更改单据编号',
				name : 'ggdNumber'
		  }]
		},{
			columnWidth : .2,
			border : false,
			layout : 'form',
			items : [{
				xtype : 'textfield',
				fieldLabel : '更改单据版本',
				name : 'ggdVersion'
			}]
		    },{ 
		    	columnWidth : .2,
		    	border : false,
		    	layout : 'form',
		    	items : [{
                    xtype : 'combo',
                    editable : false,
					name : 'ggdType',
					fieldLabel : "更改类型",
					labelAlign:'right',
					displayField : 'name',
					valueField : 'value',
					queryMode: 'local',
					store:ggdTypeStore,
					disableKeyFilter : true
		    	}]	
		    	},{
		    		columnWidth:.2,
		    		border : false,
		    		layout : 'column',
		    		items : [{
		    			columnWidth : .2,
				    	border : false,
						layout : 'column',
		    			width:70,
		    			items : [{
                        xtype:"button",
						text : '搜索',
						margin:'3 0 3 25',
					    handler:function () {
					    	var params = formPanel.getForm().getFieldValues();
					    	var ggdNumber = params.ggdNumber;
					    	var ggdVersion = params.ggdVersion;
					    	var ggdType = params.ggdType;
					    	 if (ggdNumber == "" && ggdVersion == ""&&ggdType==null ) {
					    		 Ext.Msg.alert('提示',"请添加查询条件");
				                    return;
				                }
					    	 tableStore.on("beforeload",function(){
					         tableStore.getProxy().extraParams = {ggdNumber: ggdNumber,
			                    	 ggdVersion:ggdVersion,
		                    	 ggdType:ggdType};
								});
					    	 tableStore.load();
					    	 }
		    			}]	
		    		},{
			    		columnWidth:.2,
			    		xtype:'container',
			    		layout : 'column',
			    		border : false,
			    		width:70,
			    		items : [{
	                        xtype:"button",
							text : '重置',
							margin:'3 0 3 20',
						    handler:function () {formPanel.getForm().reset();}
			    		}]		    	
		    	}]
		}]
		});
    var bar = Ext.create('Ext.PagingToolbar', {
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
		title : '技术状态详情',
		frame : false,
		bbar : bar,
		store:tableStore,
		columns: cols,
		listeners:{
		    	'select':function(_this,record,index,eOpts ){
		    	}
		    },
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


	