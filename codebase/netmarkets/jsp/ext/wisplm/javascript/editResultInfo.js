/**
 * @version V1.0
 * @Describe:贯彻结果信息编辑界面
 * @author: zxh
 * @Date：Created in  2020/6/29 13:32
 * @Modified By:
 */
	Ext.ns('App.hl.util');
	//*************************************************Store**************************************************
	
	//加载类别列表
	var exchangeTypeStore = new Ext.data.JsonStore({
		proxy : new Ext.data.HttpProxy({url : servletUrl+'InitDataController?action=exchangeType'}),
		fields:[{name:'name'},{name:'value'}]
	});
	//加载工艺贯彻状态列表
	var gongYiGuanCheStateStore = new Ext.data.JsonStore({
		proxy : new Ext.data.HttpProxy({url : servletUrl+'InitDataController?action=gongYiGuanCheState'}),
		fields:[{name:'name'},{name:'value'}]
	});
	//加载实物贯彻状态列表
	var shiWuGuanCheStateStore = new Ext.data.JsonStore({
		proxy : new Ext.data.HttpProxy({url : servletUrl+'InitDataController?action=shiWuGuanCheState'}),
		fields:[{name:'name'},{name:'value'}]
	});
	
	//*************************************************Form**************************************************
	
	var editResultInfoPanel=Ext.create('Ext.form.Panel',{
		layout:'vbox',
		defaultType : 'combo',
		fieldDefaults :{labelAlign : 'right',labelWidth:150,labelStyle:'font-weight:bold'},
		bodyPadding:'10 50 10 0',
		items:
			[
			 	{
	 		    	name : 'exchangeType',
					displayField : 'name',
					valueField : 'value',
					queryMode : 'local',
					editable : false,
					store:exchangeTypeStore,
					forceSelection:true,
	 		    	fieldLabel : "类别"
			 	},
			 	{
			 		xtype:'combo',
	 		    	name : 'gongYiGuanCheState',
	 		    	editable : false,
					displayField : 'name',
					valueField : 'value',
					queryMode : 'local',
					store:gongYiGuanCheStateStore,
					forceSelection:true,
	 		    	fieldLabel : "工艺贯彻状态"
			 	},	{
			 		xtype:'combo',
			 		editable : false,
	 		    	name : 'shiWuGuanCheState',
					displayField : 'name',
					valueField : 'value',
					queryMode : 'local',
					store:shiWuGuanCheStateStore,
					forceSelection:true,
	 		    	fieldLabel : "实物贯彻状态"
			 	},
			 	{
			 		xtype:'textfield',
	 		    	name : 'shiWuWeiGuanCheEff',
	 		    	fieldLabel : "实物未贯彻有效性"
			 	},
			 	{
			 		xtype:'datefield',
	 		    	name : 'exchangeStartTime',
	 		    	format : 'Y-m-d',
	 		    	editable : false,
					submitFormat:'Y-m-d',
	 		    	fieldLabel : "贯彻开始时间"
			 	},
			 	{
			 		xtype:'textareafield',
	 		    	name : 'xiuGaiYuanYin',
	 		    	fieldLabel : "修改原因"
			 	},
			 	{
			 		xtype:'textfield',
	 		    	name : 'ggdOid',
	 		    	hidden: true,
	 		    	fieldLabel : "更改单据对象oid"	
			 	},{
			 		xtype:'textfield',
	 		    	name : 'oid',
	 		    	hidden: true,
	 		    	fieldLabel : "更改贯彻结果对象oid"	
			 	}
			],
			buttons:[
			         {
			        	 text:'确定',
			        	 handler:function(){
			        		 var formParams = editResultInfoPanel.getForm().getValues();
							 //提交数据
							 Ext.Ajax.request({
					                url: servletUrl+'UpdateDataController',
					                method:'POST',
					                //提交表单参数
					                params: {
					                	ggdOid: formParams.ggdOid,
					                	exchangeType:formParams.exchangeType,
					                	gongYiGuanCheState: formParams.gongYiGuanCheState,
					                	shiWuGuanCheState: formParams.shiWuGuanCheState,
					                	shiWuWeiGuanCheEff: formParams.shiWuWeiGuanCheEff,
					                	exchangeStartTime: formParams.exchangeStartTime,
					                	xiuGaiYuanYin: formParams.xiuGaiYuanYin,
					                	oid: formParams.oid
					                },
					                success: function (response) {
//					                    let text = ;
					                    var infoObj=Ext.decode(response.responseText);
					                    if(infoObj.result=='success'){
					                    	Ext.getCmp('editResultInfoWinId').hide();
					                    	infoTwoStore.reload();
					                    }
					                    if(infoObj.result=='fail'){
					                    	Ext.Msg.alert('系统提示',infoObj.messgae);
					                    }	
					                }
					            });
			        	 }
			        },
			         {text:'取消',
			          handler:function(){
			        	  Ext.getCmp('editResultInfoWinId').hide();
						}	
			         }
			        ]
	});
	
	//*************************************************Window**************************************************
	
	App.hl.util.editResultInfo = Ext.extend(Ext.Window, {
		id:'editResultInfoWinId',
	    layout: 'fit',
	    title: '贯彻结果信息编辑',
	    items : [editResultInfoPanel],
	    closeAction: 'hide',
	    autoScroll: false,
	    buttonAlign: 'center',
	    initComponent: function () {
	        this.items = [editResultInfoPanel];
	        App.hl.util.editResultInfo.superclass.initComponent.call(this)
	    }
	});
