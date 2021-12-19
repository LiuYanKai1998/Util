/**
 * @version V1.0
 * @Describe:更改单详细信息界面
 * @author: zxh
 * @Date：Created in  2020/6/23 22:10
 * @Modified By:
 */
	Ext.ns('App.hl.util');

	//更改单维护窗口变量
	var editDataInfo="";
	//更改对象创建窗口变量
	var addObject=""
	//更改对象维护窗口变量
	var editObj="";
	//贯彻结果信息创建窗口变量
	var addResultInfo="";
	//贯彻结果信息编辑窗口变量
	var editResultInfo="";
	//工艺文件信息创建窗口变量
	var addGyFile="";
	//工艺文件信息编辑窗口变量
	var editGyFile="";

//*************************************************Model**************************************************

	var servletUrl='http://' + window.location.host+ '/Windchill/ptc1/';
	//贯彻对象Model
	Ext.define('infoOneFields', {
	    extend: 'Ext.data.Model',
	    fields: [
					{name:'objName',mapping:'objName'},
					{name:'objNumber',mapping:'objNumber'},
					{name:'objVersion',mapping:'objVersion'},
					{name:'objType',mapping:'objType'},
					{name:'objState',mapping:'objState'},
					{name:'parentNumber',mapping:'parentNumber'},
					{name:'parentName',mapping:'parentName'},
					{name:'oid',mapping:'oid'},
					{name:'parentVersion',mapping:'parentVersion'}
		            ]
	});
	//贯彻信息
	Ext.define('infoTwoFields', {
	    extend: 'Ext.data.Model',
	    fields: [
					{name:'exchangeType',mapping:'exchangeType'},
					{name:'gongYiGuanCheState',mapping:'gongYiGuanCheState'},
					{name:'shiWuGuanCheState',mapping:'shiWuGuanCheState'},
					{name:'shiWuWeiGuanCheEff',mapping:'shiWuWeiGuanCheEff'},
					{name:'exchangeStartTime',mapping:'exchangeStartTime'},
					{name:'remark',mapping:'remark'},
					{name:'oid',mapping:'oid'}
		            ]
	});
	//工艺文件
	Ext.define('gyFileFields', {
	    extend: 'Ext.data.Model',
	    fields: [
					{name:'technologyFileName',mapping:'technologyFileName'},
					{name:'oid',mapping:'oid'}
		            ]
	});
//***************************************Store**********************************************************
	//贯彻对象store
	var infoOneStore = new Ext.data.JsonStore({
			model: 'infoOneFields',
			proxy : new Ext.data.HttpProxy({
				type: 'ajax',
				url : servletUrl+'searchDataController'
	//			reader : new Ext.data.JsonReader({
	//				totalProperty : 'totalProperty',
	//				root : 'root'
	//			})
			})
	});
	//贯彻信息store
	var infoTwoStore = new Ext.data.JsonStore({
		model: 'infoTwoFields',
		proxy : new Ext.data.HttpProxy({
			type: 'ajax',
			url : servletUrl+'searchDataController'
		})
	});
	//工艺文件store
	var gyFileStore = new Ext.data.JsonStore({
		model: 'gyFileFields',
		proxy : new Ext.data.HttpProxy({
			type: 'ajax',
			url : servletUrl+'searchDataController'
		})
	});

//********************************************Form****************************************************

	var ggdInfo = new Ext.FormPanel({
		id:'formId',
	    frame: true,
	    layout:'vbox',
	    bodyBorder:false,
	    margin:'10 10 20 10',
	    width:'100%',
	    fieldDefaults : {labelAlign : 'right',labelStyle:'font-weight:bold'},
	    bodyPadding:'0 0 0 50',
	    items: [
	            //第一列
	            {
			    	xtype : 'container',
					layout : 'column',
					width:'100%',
					items : [ {
					xtype : 'container',
					columnWidth : .25,
					defaultType : 'displayfield',
					items:[
							{
								fieldLabel : "更改单据编号",
								name:'ggdNumber',
								value : 'ARJ21-700'
							},
						       {
								fieldLabel : "类型",
								name:'ggdType',
								value : 'ARJ21-700'
					       },
					       {
								fieldLabel : "下游单位",
								value : 'ARJ21-700'
					       }
					       
					      ]
						}, 
				//第二列
				{
					xtype : 'container',
					columnWidth : .25,
					defaultType : 'displayfield',
					items:[
							{
								fieldLabel : "更改单名称",
								name:'ggdName',
								disableKeyFilter : true
							},
							   {
								fieldLabel : "状态",
								name:'exchangeState',
								value : 'ARJ21-700'
					       },

					       {
								fieldLabel : "设计员",
								name:'designer',
								value : 'ARJ21-700'
					       }
					      ]
				}, 
				//第三列
				{
					xtype : 'container',
					columnWidth : .25,
					defaultType : 'displayfield',
					items:[
					       {
								fieldLabel : "更改单据版本",
								value : 'ARJ21-700'
					       },
					       {
								fieldLabel : "种类",
								value : 'ARJ21-700'
					       	},
					       	{
								fieldLabel : "专业",
								value : 'ARJ21-700'
					       	}
					       ]
				} ,
				{
					xtype : 'container',
					columnWidth : .25,
					defaultType : 'displayfield',
					items:[
					       	 {
								fieldLabel : "有效性",
								name:'ggdEff',
								value : 'ARJ21-700'
					       },
					    
					       {
								fieldLabel : "发放时间",
								value : 'ARJ21-700'
					       },
					       	{
								fieldLabel : "数据来源",
								value : 'ARJ21-700'
					       	}]
				}]
			},
			//文本域
			{
				xtype : 'container',
				layout : 'vbox',
				defaultType : 'textareafield',
				items:[{
					maxHeight:'50',
					width:800,
					overflowY:'scroll',
					name:'ggdSketch',
					labelStyle:'font-weight:bold',
					fieldLabel: '更改简述'
				},{
					maxHeight:'50',
					width:800,
					name:'ggdReason',
					labelStyle:'font-weight:bold',
					fieldLabel: '更改原因'
				},{
					maxHeight:'50',
					width:800,
					name:'remark',
					labelStyle:'font-weight:bold',
					fieldLabel: '备注'
				}]
			}],
			buttonAlign : 'right',
			buttons:[
			         {
			        	 text:'编辑更改单据',
			        	 handler:function(){
			        		 var oid = Ext.getCmp('winId').oid;
			        		 if (!editDataInfo) {
			        			 editDataInfo = new App.hl.util.editDataInfo();
			        			 mainPanel.getForm().load({
			        	    		    url: servletUrl+'searchDataController?',
			        	    		    params: {oid:oid}
			        	    		});
			        			 //获取下拉列表
			        			 exchangeStateStore.load();
			        			 ggdKindStore.load();
			        			 ggdTypeStore.load();
			        			 xiaYouUnitStore.load();
			         	    } 
			        		 editDataInfo.show();
			        	 }
			         }
			         ]
	});
	
	//*******************************************grid***********************************************
	
	//var bar = Ext.create('Ext.PagingToolbar', {
	//	pageSize : 5,
	//	store: infoOneStore,
	//	displayInfo : true,
	//	displayMsg:'显示第{0}-{1}条，共{2}条',
	//	emptyMsg:'没有记录'
	//});
	var addObjTable=Ext.create('Ext.grid.Panel', {
		store:infoOneStore,
		frame:true,
		viewConfig:{enableTextSelection:true},
		 title: '更改对象',
		 tbar: [
		        { 
		        	xtype: 'button', 
		        	text: '新增贯彻对象',
		        	handler:function(){
		        		 addObjPanel.getForm().reset();
		        		 if (!addObject) {
		        			 addObject = new App.hl.util.addObject();
		        	    		};
		        	    		addObject.show();
		        	}
		        },
		        { 
		        	xtype: 'button', 
		        	text: '修改' ,
		        	handler:function(){
		        		//获取选中行表格
		                var selection = addObjTable.selModel.getSelection();
		                //判断
		                if (selection == "" || selection == null ||selection.length>1 ) {
		                    Ext.Msg.alert("提示","请选中一条数据");
		                    return
		                }
		                else if (!editObj) {
		                	var oid=selection[0].raw.oid;
		                	//获取下拉列表
		                	objTypeStore.load();
		                	objStateStore.load();
		                	//加载表单数据
		        			editObjPanel.getForm().load({
		        				 	method : 'get',
		        	    		    url: servletUrl+'searchDataController?',
		        	    		    params: {oid:oid}
		        	    		});
		        			 editObj = new App.hl.util.editObj();
		        	    		};
		        	    		editObj.show();
		        	}
		        },
		        { 
		        	xtype: 'button', 
		        	text: '删除',
		        	handler:function(){
		        		// var
		        	      // grid=Utils.getCmp('deliveryTechnicalStatusTableId');
		        	             var records=addObjTable.selModel.selected.items;
		        	            if(records.length <= 0){
		        	             Ext.Msg.alert('提示', "请选择想要删除的行！");
		        	             return;
		        	            }
		        	            Ext.Msg.confirm('系统提示','确定要删除这些记录吗？',
		        	                 function(btn){
		        	                   if(btn=='yes'){
		        	                    for(var i=records.length -1;i >=0;i--){
		        	                    	addObjTable.store.remove(records[i]);
		        	              }       
		        	                   }
		        	                 },this);
		        	}	
		        }
		      ],
	//	      bbar: bar,
		    selModel: new Ext.selection.CheckboxModel(),
		    columnLines : true,
		    columns: [
		        { text: '名称',  dataIndex: 'objName' ,flex:2,align:'center'},
		        { text: '编号', dataIndex: 'objNumber', flex: 1,align:'center' },
		        { text: '版本', dataIndex: 'objVersion',flex: .5,align:'center' },
		        { text: '类别', dataIndex: 'objType', flex: .5,align:'center'},
		        { text: '状态', dataIndex: 'objState',flex: 1,align:'center'},
		        { text: '父级编号', dataIndex: 'parentNumber', flex: 1 ,align:'center'},
		        { text: '父级名称', dataIndex: 'parentName',flex: 2,align:'center'},
		        { text: '父级版本', dataIndex: 'parentVersion',flex: .8 ,align:'center'},
		        { text: 'oid', dataIndex: 'oid', flex: 1 ,hidden:true,align:'center'}
		    ],
		    height: 300,
		    width: '100%'
	});
	
	var addInfoTable=Ext.create('Ext.grid.Panel', {
		store:infoTwoStore,
		margin:'20 0 0 0',
		frame:true,
		columnLines : true,
		viewConfig:{enableTextSelection:true},
		 title: '贯彻情况',
		 tbar: [
		        { 
		        	xtype: 'button', 
		        	text: '新增贯彻信息',
		        	handler:function(){
		        		 if (!addResultInfo) {
		        			 addResultInfo = new App.hl.util.addResultInfo();
		        	    		};
		        	    		addResultInfoPanel.getForm().reset();
		        	    		addResultInfo.show();
		        	}
		        },
		        { 
		        	xtype: 'button',
		        	text: '修改',
		        	handler:function(){
		        		//获取选中行回填表格
		                var selection = addInfoTable.selModel.getSelection();
		                //判断
		                if (selection == "" || selection == null ||selection.length>1 ) {
		                	  Ext.Msg.alert("提示","请选中一条数据");
		                    return
		                }
		                else if (!editResultInfo) {
		                	//获取选中行oid
		                	var oid=selection[0].raw.oid;
		                	//加载下拉列表
		        			exchangeTypeStore.load();
		        			gongYiGuanCheStateStore.load();
		        			shiWuGuanCheStateStore.load();
		        			 //加载表单初始值
		        			editResultInfoPanel.getForm().load({
		        				 	method : 'get',
		        	    		    url: servletUrl+'searchDataController?',
		        	    		    params: {oid:oid}
		        	    		});
		        			editResultInfo = new App.hl.util.editResultInfo();
		        	    		};
		        	    	editResultInfo.show();
		        	}
		        },
		        { 
		        	xtype: 'button',
		        	text: '删除' ,
		        		handler:function(){
			        		// var
			        	      // grid=Utils.getCmp('deliveryTechnicalStatusTableId');
			        	             var records=addInfoTable.selModel.selected.items;
			        	            if(records.length <= 0){
			        	             Ext.Msg.alert('提示', "请选择想要删除的行！");
			        	             return;
			        	            }
			        	            Ext.Msg.confirm('系统提示','确定要删除这些记录吗？',
			        	                 function(btn){
			        	                   if(btn=='yes'){
			        	                    for(var i=records.length -1;i >=0;i--){
			        	                    	addObjTable.store.remove(records[i]);
			        	              }       
			        	                   }
			        	                 },this);
			        	}		
		        }
		      ],
//		    selModel: new Ext.selection.CheckboxModel({checkOnly:true}),
		    selModel: new Ext.selection.CheckboxModel(),
		    columns: [
		        { text: '类型',  dataIndex: 'exchangeType' ,flex:1,align:'center'},
		        { text: '工艺贯彻状态', dataIndex: 'gongYiGuanCheState',flex:1.2 ,align:'center'},
		        { text: '实物贯彻状态', dataIndex: 'shiWuGuanCheState',flex:1.2,align:'center' },
		        { text: '实物未贯彻有效性', dataIndex: 'shiWuWeiGuanCheEff', flex:1.4,align:'center' },
		        { text: '贯彻开始时间', dataIndex: 'exchangeStartTime', flex:2,align:'center' },
		        { text: '备注', dataIndex: 'remark',flex:4,align:'center'},
		        { text: 'oid', dataIndex: 'oid', flex: 1 ,hidden:true,align:'center'}
		    ],
		    height: 200,
		    width: '100%'
	});
	
	var gyFileTable=Ext.create('Ext.grid.Panel', {
		store:gyFileStore,
		margin:'20 0 0 0',
		frame:true,
		columnLines : true,
		viewConfig:{enableTextSelection:true},
		 title: '工艺文件',
		 tbar: [
		        { 
		        	xtype: 'button', 
		        	text: '新增工艺文件',
		        	handler:function(){
		        		 if (!addGyFile) {
		        			 addGyFile = new App.hl.util.addGyFile();
		        	    		};
		        	    		addGyFilePanel.getForm().reset();
		        	    		addGyFile.show();
		        	}
		        },
		        { 
		        	xtype: 'button',
		        	text: '修改',
		        	handler:function(){
		        		//获取选中行回填表格
		                var selection = gyFileTable.selModel.getSelection();
		                if (selection == "" || selection == null ||selection.length>1 ) {
		                	  Ext.Msg.alert("提示","请选中一条数据");
		                    return
		                }
		                else if (!editGyFile) {
		                	var oid=selection[0].raw.oid;
		                	editGyFilePanel.getForm().load({
		        				 	method : 'get',
		        	    		    url: servletUrl+'searchDataController?',
		        	    		    params: {oid:oid}
		        	    		});
		        			 editGyFile = new App.hl.util.editGyFile();
		        	    		};
		        	    		editGyFile.show();
		        	}
		        },
		        { 
		        	xtype: 'button', 
		        	text: '删除' ,
		        		handler:function(){
			        		// var
			        	      // grid=Utils.getCmp('deliveryTechnicalStatusTableId');
			        	             var records=gyFileTable.selModel.selected.items;
			        	            if(records.length <= 0){
			        	             Ext.Msg.alert('提示', "请选择想要删除的行！");
			        	             return;
			        	            }
			        	            Ext.Msg.confirm('系统提示','确定要删除这些记录吗？',
			        	                 function(btn){
			        	                   if(btn=='yes'){
			        	                    for(var i=records.length -1;i >=0;i--){
			        	                    	addObjTable.store.remove(records[i]);
			        	              }       
			        	                   }
			        	                 },this);
			        	}		
		        }
		      ],
		    selModel: new Ext.selection.CheckboxModel(),
		    columns: [
		        { header: '序号', xtype: 'rownumberer',width:40, align: 'center', sortable: false },      
		        { text: '工艺文件名称',  dataIndex: 'technologyFileName' ,width:160,align:'center'},
		        {  flex: 1,align:'center' },
		        { text: 'oid', dataIndex: 'oid', flex: 1,hidden:true,align:'center' }
		    ],
		    height: 200,
		    width: '100%'
	});
	
	//*************************************************Panel**************************************************
	
	var bottom=Ext.create('Ext.panel.Panel', {
	    layout:'vbox',
	    width: '100%',
	    border:false,
	    bodyPadding:'0 10 0 10',
	    items:[addObjTable,addInfoTable,gyFileTable]
	});
	
	
	//*************************************************Window**************************************************
	
	App.hl.util.newdataInfo = Ext.extend(Ext.window.Window, {
		id:'winId',
		layout : 'vbox',
	    title: '更改单据详情',
	    width: 1200,
	    constrain:true,
	    height: 600,
//	    frame:true,
	    maximizable : true,
//	    minimizable : true,
//	    maximized : true,
	    closeAction: 'hide',
	    items : [ggdInfo,bottom],
	    autoScroll: true,
	    buttonAlign: 'center',
	    initComponent: function () {
	        this.items = [ggdInfo,bottom];
	        App.hl.util.newdataInfo.superclass.initComponent.call(this)
	    },
	    //监听页面渲染之后执行
	    listeners:{
	    	'afterrender':function(){
	    		ggdInfo.getForm().reset();
	    		//本页表单数据初始化
	    		ggdInfo.getForm().load({
	    		    url: servletUrl+'searchDataController',
	    		    params: { oid:1 }
	    		});
	    		//本页表格数据读取
	    		infoOneStore.load( {params: {oid:2} });
	    		infoTwoStore.load( {params: {oid:3} });
	    		gyFileStore.load( {params: {oid:4} });
	
	    	}
	    }
	});

