/**
 * @version V1.0
 * @Describe:工艺文件信息创建界面
 * @author: zxh
 * @Date：Created in  2020/6/29 9:51
 * @Modified By:
 */
	Ext.ns('App.hl.util');
	//*************************************************Form**************************************************
	var addGyFilePanel=Ext.create('Ext.form.Panel',{
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
	 		    	name : 'ggdOid',
	 		    	hidden:true,
	 		    	value:1,
	 		    	fieldLabel : "更改单据对象oid"
			 	}
			],
			buttons:[
			         {
			        	 text:'确定',
			        	 handler:function(){
			        		 var formParams = addGyFilePanel.getForm().getValues();
							 //提交数据
							 Ext.Ajax.request({
					                url: servletUrl+'UpdateDataController',
					                method:'POST',
					                //提交表单参数
					                params: {
					                	technologyFileName: formParams.technologyFileName,
					                	ggdOid:formParams.ggdOid
					                },
					                success: function (response) {
//					                    let text = ;
					                    var infoObj=Ext.decode(response.responseText);
					                    if(infoObj.result=='success'){
					                    	Ext.getCmp('addGyFileWinId').hide();
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
			        	  Ext.getCmp('addGyFileWinId').hide();
						}	
			         }
			        ]
	});
	//*************************************************Window**************************************************
	App.hl.util.addGyFile = Ext.extend(Ext.Window, {
		id:'addGyFileWinId',
	    layout: 'fit',
	    title: '工艺文件信息创建',
	    items : [addGyFilePanel],
	    closeAction: 'hide',
	    autoScroll: false,
	    buttonAlign: 'center',
	    initComponent: function () {
	        this.items = [addGyFilePanel];
	        App.hl.util.addGyFile.superclass.initComponent.call(this)
	    }
	});