//添加 修改 交付技术状态页面
Ext.ns('App.hl.util');
var formPanel="";
Ext.override(Ext.form.Field, {
    //针对form中的基本组件 　　
    initComponent: function () {
        if (this.allowBlank !== undefined && !this.allowBlank) {
            if (this.fieldLabel) {
                this.fieldLabel = '<font color=red>*</font>' + this.fieldLabel;
            }
        }
        Ext.form.Field.superclass.initComponent.call(this);
    }
});
var servletUrl = 'http://' + window.location.host + '/Windchill/ptc1/DeliveryTechnicalStatusController?action=';
//下拉列表 型号查询
var fieldtype = [
            {name: 'name', mapping: 'name'},
  	        {name: 'value', mapping: 'value'}
          ];
var planeTypeStore = new Ext.data.JsonStore({
    proxy: new Ext.data.HttpProxy({url: servletUrl+'InitDataPlaneType'}),
    fields :fieldtype
});
planeTypeStore.load();

//下拉列表 状态名称查询
var fieldState = [
             {name: 'name', mapping: 'name'},
  	        {name: 'value', mapping: 'value'}
          ];
var jiaoFuStateStore = new Ext.data.JsonStore({
    proxy: new Ext.data.HttpProxy({url: servletUrl+'InitDataJiaoFuState'}),
    fields :fieldState
});
jiaoFuStateStore.load();

//下拉列表 下游单位查询
var fieldxiayou = [
             {name: 'name', mapping: 'name'},
  	        {name: 'value', mapping: 'value'}
          ];
var xiaYouUnitStore = new Ext.data.JsonStore({
    proxy: new Ext.data.HttpProxy({url: servletUrl+'InitDataXiaYouUnit'}),
    fields :fieldxiayou
});
xiaYouUnitStore.load();
//新增交付技术状态弹出页面form内容
  var addDeliveryTechnicalStatusForm = new Ext.form.FormPanel({
  id:'addDeliveryTechnicalStatusFormId',
  url: servletUrl + "insertOrUpdataDeliveryTechnicalStatus",
  fieldDefaults : {labelAlign : 'right',msgTarget : 'side'},
  height: 200,
  bodyStyle:'margin:40',
  frame:true,
  items: [{
			xtype : 'combo',
			editable : false,
			name : 'planeType',
			algin : 'center',
			fieldLabel : "型号",
			displayField : 'name',
			valueField : 'value',
			queryMode: 'local',
			store:planeTypeStore,
			disableKeyFilter : true
  },{
      xtype : 'combo',
      editable : false,
		name : 'jiaoFuState',
		algin : 'center',
		fieldLabel : "状态名称",
		displayField : 'name',
		valueField : 'value',
		queryMode: 'local',
		store:jiaoFuStateStore,
		disableKeyFilter : true
	},{
      xtype : 'combo',
      editable : false,
		name : 'xiaYouUnit',
		algin : 'center',
		fieldLabel : "下游单位",
		displayField : 'name',
		valueField : 'value',
		queryMode: 'local',
		store:xiaYouUnitStore,
		disableKeyFilter : true
	},{
		 fieldLabel: '有效性',
		 algin : 'center',
	     xtype: 'textfield',
	   // blankText:'计量单位的值不能为空',
	     name: 'jiaoFuEff'
  }],
  buttons: [{
      text: '确认',
      handler: function () {
          //表单提交
          var params = addDeliveryTechnicalStatusForm.getForm().getFieldValues();
          Ext.Ajax.request({
              url: servletUrl + "insertOrUpdataDeliveryTechnicalStatus",
              params: {
              	planeType:params.planeType,
              	jiaoFuState:params.jiaoFuState,
              	xiaYouUnit:params.xiaYouUnit,
              	jiaoFuEff:params.jiaoFuEff
              },
              success: function (response) {
                  var res = Ext.decode(response.responseText);
                  if(res[0].result=='success'){
                  	  addDeliveryTechnicalStatusForm.getForm().reset();
                      formPanel.getForm().reset();
                      Ext.getCmp("addDeliveryTechnicalStatusId").hide();
                      Ext.getCmp("deliveryTechnicalStatusTableId").store.load();
                  }else{
                  	   alert(res[0].messgae);
                   }
              },
              failure: function (response) {
            	  Ext.Msg.alert( "添加失败");
              },
              scope: this
          });
      },
      scope: this
  }, {
      text: '取消',
      handler: function () {
          Ext.getCmp("addDeliveryTechnicalStatusId").hide()
      },
      scope: this
  }]
});


//新增交付技术状态弹出页面
App.hl.util.addDeliveryTechnicalStatus = Ext.extend(Ext.Window, {
    id:'addDeliveryTechnicalStatusId',
    layout: 'fit',
    title: '交付技术状态创建',
    width: 400,
    height: 320,
    border : false,
    constrain: true,
    closeAction: 'hide',
    autoScroll: false,
    buttonAlign: 'center',
    initComponent: function () {
        this.items = [addDeliveryTechnicalStatusForm];
        App.hl.util.addDeliveryTechnicalStatus.superclass.initComponent.call(this)
    }
});