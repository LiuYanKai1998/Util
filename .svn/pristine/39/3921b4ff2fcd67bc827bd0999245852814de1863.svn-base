/**
 * @version V1.0
 * @Describe:更改单据维护界面
 * @author: zxh
 * @Date：Created in  2020/6/28 8:20
 * @Modified By:
 */
	Ext.ns('App.hl.util');
	//*************************************************Store**************************************************
	var servletUrl='http://' + window.location.host+ '/Windchill/ptc1/';
	//加载状态列表
	var exchangeStateStore = new Ext.data.JsonStore({
		proxy : new Ext.data.HttpProxy({url : servletUrl+'InitDataController?action=exchangeState'}),
		fields:[{name:'name'},{name:'value'}]
	});
	//加载种类列表
	var ggdKindStore = new Ext.data.JsonStore({
		proxy : new Ext.data.HttpProxy({url : servletUrl+'InitDataController?action=ggdKind'}),
		fields:[{name:'name'},{name:'value'}]
	});
//加载类型列表
var ggdTypeStore = new Ext.data.JsonStore({
	proxy : new Ext.data.HttpProxy({url : servletUrl+'InitDataController?action=ggdType'}),
	fields:[{name:'name'},{name:'value'}]
});
//加载状态列表
var xiaYouUnitStore = new Ext.data.JsonStore({
	proxy : new Ext.data.HttpProxy({url : servletUrl+'InitDataController?action=xiaYouUnit'}),
	fields:[{name:'name'},{name:'value'}]
});
//*************************************************Form**************************************************
var topForm=Ext.create('Ext.form.Panel',{
	id:'topFormId',
	layout:'column',
//	frame:true,
	fieldDefaults :{labelAlign : 'right',labelStyle:'font-weight:bold'},
	bodyBorder:false,
	border:false,
	bodyPadding:'0 0 0 0',
	width:'100%',
	items:[
	       //第一列
	       {
		    	xtype : 'container',
		    	columnWidth : .3,
		   		width:'100%',
		   		items:[
		   		    {
		   		    	xtype:'displayfield',
		   		    	name:'ggdNumber',
		   		    	fieldLabel : "更改单据编号"
	   		       },
		   		      
		   		       {
			   		    	xtype:'textfield',
			   		    	name:'ggdVersion',
			   		    	fieldLabel : "更改单据版本"
		   		       },
		   		       {
			   		    	xtype:'combo',
			   		    	fieldLabel : "状态",
			   		    	name:'exchangeState',
			   		    	editable : false,
							displayField : 'name',
							valueField : 'value',
							queryMode : 'local',
							store:exchangeStateStore,
							forceSelection:true
		   		       },
		   		       {
			   		    	xtype:'combo',
			   		    	fieldLabel : "类型",
			   		    	editable : false,
				   		 	name : 'ggdType',
							displayField : 'name',
							valueField : 'value',
							queryMode : 'local',
							store:ggdTypeStore,
							forceSelection:true
		   		       },
		   		       {
			   		        xtype: 'textfield',
			                hidden: true,
			                name: 'oid' 
		   		       }
		   		      ]
	       },
	       //第二列
	       {
		    	xtype : 'container',
		    	columnWidth : .3,
		   		width:'100%',
		   		frame:false,
		   		items:[
		   		    {
		   		    	xtype:'displayfield',
		   		    	name:'ggdName',
		   		    	fieldLabel : "更改单据名称"
	   		       },
		   		       
		   		       {
			   		    	xtype:'combo',
			   		    	fieldLabel : "种类",
				   		 	name : 'ggdKind',
				   		 	editable : false,
							displayField : 'name',
							valueField : 'value',
							queryMode : 'local',
							store:ggdKindStore,
							forceSelection:true
		   		       },
		   		       {
			   		    	xtype:'combo',
			   		    	fieldLabel : "下游单位",
				   		 	name : 'xiaYouUnit',
				   		 	editable : false,
							displayField : 'name',
							valueField : 'value',
							queryMode : 'local',
							store:xiaYouUnitStore,
							forceSelection:true
		   		       },
		   		    {
			   		    	xtype:'textfield',
			   		    	fieldLabel : "专业",
				   		 	name : 'professional'
		   		       }
		   		      ]
	       },
	       {
		    	xtype : 'container',
		    	columnWidth : .3,
		   		width:'100%',
		   		frame:false,
		   		items:[
		   		       	{
				   	xtype:'textfield',
				   	name:'ggdEff',
				   	fieldLabel : "有效性"
		   		       	},
		   		     {
			   		    	xtype:'textfield',
			   		    	name : 'designer',
			   		    	fieldLabel : "设计员"
		   		       },
		   		       {
			   		    	xtype:'datefield',
			   		    	name : 'releaseDate',
			   		    	format : 'Y-m-d',
				   		    submitFormat:'Y-m-d',
				   		    editable : false,
			   		    	fieldLabel : "发放时间"
		   		       }
		   		      ]
	       }
	      ]
});
	//文本域form
	var buttomForm=Ext.create('Ext.form.Panel',{
		layout:'vbox',
		border:false,
		fieldDefaults :{labelAlign : 'right',labelStyle:'font-weight:bold'},
		bodyPadding:'0 0 0 0',
		width:'100%',
		defaultType : 'textareafield',
		items:[
		       {
					width:600,
					overflowY:'scroll',
					name:'ggdSketch',
					fieldLabel: '更改简述'
		       },
		       {
					width:600,
					overflowY:'scroll',
					name:'ggdReason',
					fieldLabel: '更改原因'
		       },
		       {
					width:600,
					overflowY:'scroll',
					name:'remark',
					fieldLabel: '备注'
		       },
		       {
					width:600,
					overflowY:'scroll',
					name:'xiuGaiYuanYin',
					fieldLabel: '修改原因'
		       }
		      ]
	});
	//*************************************************Panel**************************************************
	var mainPanel=Ext.create('Ext.form.Panel',{
		layout:'vbox',
		bodyPadding:'30 0 0 0',
	    closeAction:'hide',
		 frame: false,
		 width:'100%',
		items:[topForm,buttomForm],
		buttonAlign : 'center',
		buttons:
			[
			 {
				 text:'确定',
				 handler:function(){
					 var formParams = mainPanel.getForm().getValues();
					 //提交数据
					 Ext.Ajax.request({
			                url: servletUrl+'UpdateDataController',
			                method:'GET',
			                params: {
			                	ggdNumber: formParams.ggdNumber,
								ggdName: formParams.ggdName,
								ggdVersion: formParams.ggdVersion,
								ggdEff: formParams.ggdEff,
								exchangeState: formParams.exchangeState,
								ggdKind: formParams.ggdKind,
								ggdType: formParams.ggdType,
								xiaYouUnit: formParams.xiaYouUnit,
								designer: formParams.designer,
								releaseDate: formParams.releaseDate,
								xiaYouUnit: formParams.xiaYouUnit,
								ggdSketch: formParams.ggdSketch,
								ggdReason: formParams.ggdReason,
								remark: formParams.remark,
								xiuGaiYuanYin: formParams.xiuGaiYuanYin,
								oid: formParams.oid
			                },
			                //回调函数
			                success: function (response) {
//			                	var text=
//			                	 var res = Ext.util.JSON.decode();
//			                    let text = response.responseText;
			                    var infoObj=Ext.decode(response.responseText);
			                    if(infoObj.result=='success'){
			                    	//关闭窗口
			                    	Ext.getCmp('editWinId').hide();
			                    	//刷新页面
			                		ggdInfo.getForm().load({
			                		    url: servletUrl+'searchDataController?',
			                		    params: { oid:1 }
			                		});
			                    }
			                    if(infoObj.result=='fail'){
			                    	Ext.Msg.alert('系统提示',infoObj.messgae);
			                    }	
			                }
			            });
				
				 }
			},{
				text:'取消',
				handler:function(){
					Ext.getCmp('editWinId').hide();
//					editDataInfo="";
				},
				scope:this
			}
			]
	});
	//*************************************************Window**************************************************
	App.hl.util.editDataInfo = Ext.extend(Ext.Window, {
		id:'editWinId',
	    layout: 'fit',
	    title: '编辑原有装配部件',
	    width: 800,
	    height: 500,
	    items : [mainPanel],
	    closeAction: 'hide',
	    autoScroll: false,
	    buttonAlign: 'center',
	    initComponent: function () {
	        this.items = [mainPanel];
	        App.hl.util.editDataInfo.superclass.initComponent.call(this)
	    }
	});