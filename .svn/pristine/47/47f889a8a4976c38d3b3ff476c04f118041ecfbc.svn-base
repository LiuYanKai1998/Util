/**
 * @version V1.0
 * @Describe:更改对象创建界面
 * @author: zxh
 * @Date：Created in  2020/6/28 18:51
 * @Modified By:
 */

	Ext.ns('App.hl.util');
	//*************************************************Store**************************************************
	//加载类型列表
	var objTypeStore = new Ext.data.JsonStore({
		proxy : new Ext.data.HttpProxy({url : servletUrl+'InitDataController?action=objType'}),
		fields:[{name:'name'},{name:'value'}]
	});

	//加载状态列表
	var objStateStore = new Ext.data.JsonStore({
		proxy : new Ext.data.HttpProxy({url : servletUrl+'InitDataController?action=objState'}),
		fields:[{name:'name'},{name:'value'}]
	});
	
	//获取下拉列表
	 objTypeStore.load();
	 objStateStore.load();
	//*************************************************Form**************************************************
	var addObjPanel=Ext.create('Ext.form.Panel',{
		layout:'vbox',
		defaultType : 'textfield',
		fieldDefaults :{labelAlign : 'right',labelStyle:'font-weight:bold'},
		bodyPadding:'10 50 10 0',
		items:
			[
			 	{
	   		    	name : 'objNumber',
	   		    	fieldLabel : "编号"
			 	},
			 	{
	   		    	name : 'objName',
	   		    	fieldLabel : "名称"
			 	},
			 	{
	   		    	name : 'objVersion',
	   		    	fieldLabel : "版本"
			 	},
			 	{
			 		xtype:'combo',
	   		    	name : 'objType',
					displayField : 'name',
					valueField : 'value',
					queryMode : 'local',
					store:objTypeStore,
					editable : false,
					forceSelection:true,
	   		    	fieldLabel : "类别"
			 	},
			 	{
			 		xtype:'combo',
	   		    	name : 'objState',
					displayField : 'name',
					valueField : 'value',
					queryMode : 'local',
					store:objStateStore,
					editable : false,
					forceSelection:true,
	   		    	fieldLabel : "状态"
			 	},
			 	{
	   		    	name : 'parentNumber',
	   		    	fieldLabel : "父级编号"
			 	},
			 	{
	   		    	name : 'parentName',
	   		    	fieldLabel : "父级名称"
			 	},
			 	{
	   		    	name : 'parentVersion',
	   		    	fieldLabel : "父级版本"
			 	},
			 	{
			 		xtype:'textfield',
	   		    	name : 'ggdOid',
	   		    	hidden: true,
	   		    	value:1,
	   		    	fieldLabel : "oid"	
			 	}
			],
			buttonAlign:'center',
			buttons:[
			         {
			        	 text:'确定',
			        	 handler:function(){
			        		 var formParams = addObjPanel.getForm().getValues();
							 //提交数据
							 Ext.Ajax.request({
					                url: servletUrl+'UpdateDataController',
					                method:'POST',
					                //提交表单参数
					                params: {
					                	ggdOid: formParams.ggdOid,
					                	objName: formParams.objName,
					                	objNumber: formParams.objNumber,
					                	objVersion: formParams.objVersion,
					                	objType: formParams.objType,
					                	objState: formParams.objState,
					                	parentNumber: formParams.parentNumber,
					                	parentName: formParams.parentName,
					                	parentVersion: formParams.parentVersion,
					                	xiuGaiYuanYin: formParams.xiuGaiYuanYin
					                },
					                //回调
					                success: function (response) {
					                    var infoObj=Ext.decode(response.responseText);
					                    if(infoObj.result=='success'){
					                    	Ext.getCmp('addObjWinId').hide();
					                    	infoOneStore.reload();
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
			        	  Ext.getCmp('addObjWinId').hide();
						}	
			         }
			        ]
	});
	//*************************************************Window**************************************************
	
	App.hl.util.addObject = Ext.extend(Ext.Window, {
		id:'addObjWinId',
	    layout: 'fit',
	    title: '更改对象信息创建',
	    items : [addObjPanel],
	    closeAction: 'hide',
	    autoScroll: false,
	    buttonAlign: 'center',
	    initComponent: function () {
	        this.items = [addObjPanel];
	        App.hl.util.addObject.superclass.initComponent.call(this)
	    }
	});