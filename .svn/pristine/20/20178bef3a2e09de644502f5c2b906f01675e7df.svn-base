/**
 * @version V1.0
 * @Describe:更改对象维护界面
 * @author: zxh
 * @Date：Created in  2020/6/29 8:39
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
	//*************************************************Form**************************************************
	var editObjPanel=Ext.create('Ext.form.Panel',{
		layout:'vbox',
		defaultType : 'textfield',
		fieldDefaults :{labelAlign : 'right',labelStyle:'font-weight:bold'},
		bodyPadding:'10 50 10 0',
		items:
			[
			 	{
			 		xtype:'displayfield',
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
					editable : false,
					store:objStateStore,
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
			 		xtype:'textareafield',
	 		    	name : 'xiuGaiYuanYin',
	 		    	fieldLabel : "修改原因"
			 	},{
			 		xtype:'textfield',
	 		    	name : 'oid',
	 		    	value: '1',
	 		    	hidden: true,
	 		    	fieldLabel : "oid"	
			 	},
			 	{
			 		xtype:'textfield',
	 		    	name : 'ggdOid',
	 		    	hidden: true,
	 		    	fieldLabel : "ggdOid"	
			 	}
			],
			buttons:[
			         {
			        	 text:'确定',
			        	 handler:function(){
			        		 var formParams = editObjPanel.getForm().getValues();
							 //提交数据
							 Ext.Ajax.request({
					                url: servletUrl+'UpdateDataController',
					                method:'POST',
					                //提交表单参数
					                params: {
					                	ggdOid: formParams.ggdOid,
					                	oid:formParams.oid,
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
					                success: function (response) {
//					                    let text =;
					                    var infoObj=Ext.decode( response.responseText);
					                    if(infoObj.result=='success'){
					                    	Ext.getCmp('editObjId').hide();
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
			        	  Ext.getCmp('editObjId').hide();
						}	
			         }
			        ]
	});
	//*************************************************Window**************************************************
	App.hl.util.editObj = Ext.extend(Ext.Window, {
		id:'editObjId',
	    layout: 'fit',
	    title: '更改对象信息编辑',
	    items : [editObjPanel],
	    closeAction: 'hide',
	    autoScroll: false,
	    buttonAlign: 'center',
	    initComponent: function () {
	        this.items = [editObjPanel];
	        App.hl.util.editObj.superclass.initComponent.call(this)
	    }
	});