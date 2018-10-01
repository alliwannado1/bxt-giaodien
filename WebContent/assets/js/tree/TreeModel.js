Ext.define('BasicTreeColumnLegends', {
	extend : 'Ext.AbstractPlugin',
	alias : 'plugin.basic_tree_column_legend',
	
	/**
		* @private
	*/
	originalCellTpl : Ext.clone(Ext.tree.Column.prototype.cellTpl).join(''),
	
	/**
		* The Xtemplate strings that will be used instead of the plain {value} when
		* rendering
	*/
	valueReplacementTpl : ['{value}',
	'<tpl if="this.hasLegend(values.record)"><br />', '</tpl>'],
	
	/**
		* The context for methods available in the template
	*/
	valueReplacementContext : {
		hasLegend : function(rec){
			var isChecked = rec.get('checked');
			var layer = rec.data;
			return isChecked && !(layer instanceof ol.layer.Group);
		}
	},
	
	init : function(column){
		var me = this;
		if(!(column instanceof Ext.grid.column.Column)) {
			Ext.log.warn('Plugin shall only be applied to instances of'
			+ ' Ext.grid.column.Column');
			return;
		}
		var valuePlaceHolderRegExp = /\{value\}/g;
		var replacementTpl = me.valueReplacementTpl.join('');
		var newCellTpl = me.originalCellTpl.replace(valuePlaceHolderRegExp,
		replacementTpl);
		
		column.cellTpl = [newCellTpl, me.valueReplacementContext];
	}
});


Ext.define('MyApp.model.m_Menuitem', {
	extend: 'Ext.data.Model',
	fields: ['text']
});