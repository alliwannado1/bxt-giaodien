var baseMapStore = Ext.create('Ext.data.TreeStore', {
	model: 'MyApp.model.m_Menuitem',
	proxy: {
		type: 'ajax',
		url: urlBase,
		reader: {
			type: 'json',
			root: function(o) {
				return o.data || o.children
			}
		}
	}
});

var classMapStore = Ext.create('Ext.data.TreeStore', {
	model: 'MyApp.model.m_Menuitem',
	proxy: {
		type: 'ajax',
		url: urlClass,
		reader: {
			type: 'json',
			root: function(o) {
				return o.data || o.children
			}
		}
	}
});