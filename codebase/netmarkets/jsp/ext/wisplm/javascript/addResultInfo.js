/**
 * @version V1.0
 * @Describe:贯彻结果信息创建界面
 * @author: zxh
 * @Date：Created in  2020/6/29 9:51
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
	
	 //加载下拉列表
	 exchangeTypeStore.load();
	 gongYiGuanCheStateStore.load();
	 shiWuGuanCheStateStore.load();
	//*************************************************Form**************************************************
	var addResultInfoPanel=Ext.create('Ext.form.Panel',{
		layout:'vbox',
		defaultType : 'combo',
		fieldDefaults :{labelAlign : 'right',labelWidth:150,labelStyle:'font-weight:bold'},
		bodyPadding:'10 50 10 0',
		items:
			[
			 	{
			 		xtype:'combo',
	 		    	name : 'exchangeType',
					displayField : 'name',
					valueField : 'value',
					queryMode : 'local',
					store:exchangeTypeStore,
					forceSelection:true,
					editable : false,
	 		    	fieldLabel : "类别"
			 	},
			 	{
			 		xtype:'combo',
	 		    	name : 'gongYiGuanCheState',
					displayField : 'name',
					valueField : 'value',
					queryMode : 'local',
					store:gongYiGuanCheStateStore,
					forceSelection:true,
					editable : false,
	 		    	fieldLabel : "工艺贯彻状态"
			 	},	{
			 		xtype:'combo',
	 		    	name : 'shiWuGuanCheState',
					displayField : 'name',
					editable : false,
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
			 		xtype:'textfield',
	 		    	name : 'ggdOid',
	 		    	hidden: true,
	 		    	fieldLabel : "更改单据oid"	
			 	}
			],
			buttons:[
			         {
			        	 text:'确定',
			        	 handler:function(){
			        		 var formParams = addResultInfoPanel.getForm().getValues();
							 //提交数据
							 Ext.Ajax.request({
					                url: servletUrl+'UpdateDataController',
					                method:'GET',
					                //提交表单参数
					                params: {
					                	ggdOid: formParams.ggdOid,
					                	exchangeType:formParams.exchangeType,
					                	gongYiGuanCheState: formParams.gongYiGuanCheState,
					                	shiWuGuanCheState: formParams.shiWuGuanCheState,
					                	shiWuWeiGuanCheEff: formParams.shiWuWeiGuanCheEff,
					                	exchangeStartTime: formParams.exchangeStartTime
					                },
					                success: function (response) {
//					                    let text = 
					                    var infoObj=Ext.decode(response.responseText);
					                    if(infoObj.result=='success'){
					                    	Ext.getCmp('addResultInfoWinId').hide();
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
			        	  Ext.getCmp('addResultInfoWinId').hide();
						}	
			         }
			        ]
	});
	//*************************************************Window**************************************************
	App.hl.util.addResultInfo = Ext.extend(Ext.Window, {
		id:'addResultInfoWinId',
	    layout: 'fit',
	    title: '贯彻结果信息创建',
	    items : [addResultInfoPanel],
	    closeAction: 'hide',
	    autoScroll: false,
	    buttonAlign: 'center',
	    initComponent: function () {
	        this.items = [addResultInfoPanel];
	        App.hl.util.addResultInfo.superclass.initComponent.call(this)
	    }
	});