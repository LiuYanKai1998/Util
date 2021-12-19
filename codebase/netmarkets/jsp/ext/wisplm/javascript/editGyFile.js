/**
 * @version V1.0
 * @Describe:工艺文件信息编辑界面
 * @author: zxh
 * @Date：Created in  2020/6/29 13:32
 * @Modified By:
 */
	Ext.ns('App.hl.util');
	//*************************************************Form**************************************************
	
	var editGyFilePanel=Ext.create('Ext.form.Panel',{
		layout:'vbox',
		defaultType : 'textfield',
		fieldDefaults :{labelAlign : 'right',labelWidth:150,labelStyle:'font-weight:bold'},
		bodyPadding:'10 50 10 0',
		items:
			[
			 	{
	 		    	name : 'technologyFileName',
	 		    	fieldLabel : "工艺文件名称"
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
	 		    	fieldLabel : "工艺文件信息对象oid"	
			 	}
			],
			buttons:[
			         {
			        	 text:'确定',
			        	 handler:function(){
			        		 var formParams = editGyFilePanel.getForm().getValues();
							 //提交数据
							 Ext.Ajax.request({
					                url: servletUrl+'UpdateDataController',
					                method:'POST',
					                //提交表单参数
					                params: {
					                	ggdOid: formParams.ggdOid,
					                	technologyFileName:formParams.technologyFileName,
					                	oid: formParams.oid
					                },
					                success: function (response) {
//					                    let text = ;
					                    var infoObj=Ext.decode(response.responseText);
					                    if(infoObj.result=='success'){
					                    	Ext.getCmp('editGyFileWinId').hide();
					                    	gyFileStore.reload();
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
			        	  Ext.getCmp('editGyFileWinId').hide();
						}	
			         }
			        ]
	});
	//*************************************************Window**************************************************
	
	App.hl.util.editGyFile = Ext.extend(Ext.Window, {
		id:'editGyFileWinId',
	    layout: 'fit',
	    title: '工艺文件信息编辑',
	    items : [editGyFilePanel],
	    closeAction: 'hide',
	    autoScroll: false,
	    buttonAlign: 'center',
	    initComponent: function () {
	        this.items = [editGyFilePanel];
	        App.hl.util.editGyFile.superclass.initComponent.call(this)
	    }
	});