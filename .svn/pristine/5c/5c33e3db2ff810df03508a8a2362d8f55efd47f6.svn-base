  /**
	 * @version V1.0
	 * @Describe:交付技术状态查询界面
	 * @author:wenhui yu
	 * @Date：Created in 2020-06-23 *
	 * @Modified By:
	 */
var servletUrl = 'http://' + window.location.host + '/Windchill/ptc1/DeliveryTechnicalStatusController?action=';
Ext.ns('App.hl.util');
Ext.onReady(function() {
Ext.QuickTips.init();
var addDeliveryTechnicalStatus="";
var editDeliveryTechnicalStatus="";
// 下拉列表 下游单位查询
var fieldxiayou = [
            {name: 'name', mapping: 'name'},
  	        {name: 'value', mapping: 'value'}
          ];
var xiaYouUnitStore = new Ext.data.JsonStore({
    proxy: new Ext.data.HttpProxy({url: servletUrl+'InitDataXiaYouUnit'}),
    fields :fieldxiayou
});
xiaYouUnitStore.load();
// 表格数据获取
var cols = [{header: "",  width: 10, sortable: true, dataIndex: '',align: 'center'},
            {header: "型号", width: 240, sortable: true, dataIndex: 'planeType',align: 'center'},
            {header: "状态名称", width: 240, sortable: true, dataIndex: 'jiaoFuState',align: 'center'},
            {header: "有效性", width: 240, sortable: true, dataIndex: 'jiaoFuEff',align: 'center'},
            {header: "下游单位", width: 240, sortable: true, dataIndex: 'xiaYouUnit',align: 'center'},
            {header: "oid", width: 240, sortable: true, dataIndex: 'oid', hidden: true}
        ];
var field = [
             {name: '', mapping: ''},
            {name: 'planeType', mapping: 'planeType'},
 	        {name: 'jiaoFuState', mapping: 'jiaoFuState'},
 	        {name: 'jiaoFuEff', mapping: 'jiaoFuEff'},
 	        {name: 'xiaYouUnit', mapping: 'xiaYouUnit'},
 	        {name: 'oid', mapping: 'oid'}
         ];
var tableStore = new Ext.data.JsonStore({
    proxy: new Ext.data.HttpProxy({url: servletUrl+'SearchDeliveryTechnicalStatus'}),
    root   : 'records',
    totalProperty : 'total',
    fields : field
});
  formPanel = new Ext.form.Panel({
	id:'formPanelId',
	margin:'10,50,10,50',
	region : 'north',
	//frame:true,
	bodyPadding : '5 5 5 5 ',
	fieldDefaults : {labelAlign : 'right',msgTarget : 'side',labelStyle:'font-weight:bold'},
	title : '交付技术状态查询',
	layout : 'column',
	items : [{
			columnWidth : .2,
			border : false,
			layout : 'column',
			items : [{
				xtype : 'textfield',
				fieldLabel : '型号',
				name : 'planeType'
			}]
		    },{
		    	columnWidth : .2,
		    	border : false,
		    	algin : 'left',
				layout : 'column',
				items : [{
                    xtype : 'combo',
					name : 'xiaYouUnit',
					fieldLabel : "下游单位",
					editable : false,
					displayField : 'name',
					valueField : 'value',
					queryMode: 'local',
					store:xiaYouUnitStore,
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
						margin:3,
					    handler:function () {
					    	var params = formPanel.getForm().getFieldValues();
					    	var planeType = params.planeType;
					    	var xiaYouUnit = params.xiaYouUnit;
					    	 if (planeType == ""&&xiaYouUnit==null ) {
					    		 Ext.Msg.alert('提示',"请添加查询条件");
				                    return;
				                }
					    	 tableStore.load({
				                    params: {
				                    	xiaYouUnit: xiaYouUnit,
				                    	planeType:planeType
				                    }
				                });
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
							margin:3,
						    handler:function () {formPanel.getForm().reset();}
			    		}]		    	
		    	}]
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
		id:'deliveryTechnicalStatusTableId',
		margin:'10,50,10,50',
		columnLines : true,
		region : 'center',
		title : '技术状态详情',
		selModel: new Ext.selection.CheckboxModel(),
		tbar: [{
			  id:"btnAddDeliveryTechnicalStatus",
			  text : "新增交付技术状态",
			  handler:function(){
				  if (!addDeliveryTechnicalStatus) {
					  addDeliveryTechnicalStatus = new App.hl.util.addDeliveryTechnicalStatus({
			            })
			        }
				addDeliveryTechnicalStatus.show();
		        Ext.getCmp("addDeliveryTechnicalStatusFormId").getForm().reset();
		   }
		}, {
			  id:"btnEditDeliveryTechnicalStatus",
			  text : "修改",
			  handler:function(){
				  // 获取选中行回填表格
		            var selection = table.selModel.getSelection();
		            if (selection == "" || selection == null ||selection.length>1 ) {
		            	Ext.Msg.alert('提示',"请选中一条数据");
		                return
		            }
		            var oid =selection[0].data.oid;
				    if (!editDeliveryTechnicalStatus) {
					   editDeliveryTechnicalStatus = new App.hl.util.editDeliveryTechnicalStatus({
			            })
			        }
				 editDeliveryTechnicalStatus.show();
				 Ext.Ajax.request({
		              url: servletUrl + "SearchDeliveryTechnicalStatusByOid",
		              params: {
		            	  oid:oid
		              },
		              success: function (response) {
		                  var res = Ext.decode(response.responseText);
		                  Ext.getCmp("editDeliveryTechnicalStatusFormId").getForm().setValues(selection[0].data);
		              },
		              failure: function (response) {
		            	  Ext.Msg.alert( '系统提示',"编辑失败");
		              },
		              scope: this
		          });
			    }
			  }, {
			 	  id:"btnDeleteDeliveryTechnicalStatus",
				  text : "删除",
				  handler:function(){
			        	// var
						// grid=Utils.getCmp('deliveryTechnicalStatusTableId');
			         	var records=table.selModel.selected.items;
			        	if(records.length <= 0){
			        		Ext.Msg.alert('提示', "请选择想要删除的行！");
			        		return;
			        	}
			        	Ext.Msg.confirm('系统提示','确定要删除这些记录吗？',
	        		      function(btn){
	        		        if(btn=='yes'){
	        		        	for(var i=records.length -1;i >=0;i--){
	        		        		table.store.remove(records[i]);
					        	}							
	        		        }
	        		      },this);
			        }
				}],
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


	