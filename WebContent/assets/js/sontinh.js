Ext.require([
    'GeoExt.component.Map',
    'GeoExt.data.store.LayersTree'
]);

/**
	* A plugin for Ext.grid.column.Column s that overwrites the internal cellTpl to
	* support legends.
*/
Ext.define('BasicTreeColumnLegends', {
    xtype: 'treepanel',
	extend: 'Ext.AbstractPlugin',
    alias: 'plugin.basic_tree_column_legend',
	
    /**
		* @private
	*/
    originalCellTpl: Ext.clone(Ext.tree.Column.prototype.cellTpl).join(''),
	
    /**
		* The Xtemplate strings that will be used instead of the plain {value}
		* when rendering
	*/
    valueReplacementTpl: [
        '{value}',
        '<tpl if="this.hasLegend(values.record)"><br />',
        '<tpl for="lines">',
        '<img src="{parent.blankUrl}"',
        ' class="{parent.childCls} {parent.elbowCls}-img ',
        '{parent.elbowCls}-<tpl if=".">line<tpl else>empty</tpl>"',
        ' role="presentation"/>',
        '</tpl>',
        '</tpl>'
	],
	
    /**
		* The context for methods available in the template
	*/
    valueReplacementContext: {
        hasLegend: function(rec) {
            var isChecked = rec.get('checked');
            var layer = rec.data;
            return isChecked && !(layer instanceof ol.layer.Group);
		}
	},
	
    init: function(column) {
        var me = this;
        if (!(column instanceof Ext.grid.column.Column)) {
            Ext.log.warn('Plugin shall only be applied to instances of' +
			' Ext.grid.column.Column');
            return;
		}
        var valuePlaceHolderRegExp = /\{value\}/g;
        var replacementTpl = me.valueReplacementTpl.join('');
        var newCellTpl = me.originalCellTpl.replace(
		valuePlaceHolderRegExp, replacementTpl
        );
		
        column.cellTpl = [
            newCellTpl,
            me.valueReplacementContext
		];
	}
});

var mapComponent;
var mapPanel;
var treePanel;
var treePanel2;

Ext.application({
    name: 'Quy Hoạch Quảng Ngãi',
    launch: function() {
        
		var source1;
        var layer1;
		var source2;
        var layer2;
        var group;
        var olMap;
        var treeStore;
        var treeStore2;
		var interaction;
		var interactionSelect;
		var interactionSnap;
		var transactWFS;
		var interactionSelectPointerMove;
		var isClickButton = false;
		
		var binhSon = ol.proj.fromLonLat([108.7507, 15.2643]);
		var sonTinh = ol.proj.fromLonLat([108.7352, 15.1643]);
		
		/**
			* Elements that make up the popup.
		*/
		var container = document.getElementById('popup2');
		var content = document.getElementById('popup-content');
		var closer = document.getElementById('popup-closer');
		
		/**
			* Create an overlay to anchor the popup to the map.
		*/
		var overlay = new ol.Overlay(/** @type {olx.OverlayOptions} */({
			element: container,
			autoPan: true,
			autoPanAnimation: {
				duration: 250
			}
		}));
		
		
		var formatWFS = new ol.format.WFS();

		var formatGML = new ol.format.GML({
			featureNS: 'http://localhost:8080/geoserver/sontinh',
			featureType: 'hdatonongthon_region_region',
			extractAttributes: true,
			srsName: 'EPSG:3857'
		});

		var xs = new XMLSerializer();
		
		var sourceWFS = new ol.source.Vector({
			loader: function (extent) {
				$.ajax('http://localhost:8080/geoserver/sontinh/ows', {
					type: 'GET',
					data: {
						service: 'WFS',
						version: '1.1.0',
						request: 'GetFeature',
						typename: 'hdatonongthon_region_region',
						srsname: 'EPSG:3857',
						bbox: extent.join(',') + ',EPSG:3857'
					}
				}).done(function (response) {
					sourceWFS.addFeatures(formatWFS.readFeatures(response));
				});
			},
			//strategy: ol.loadingstrategy.tile(ol.tilegrid.createXYZ()),
			strategy: ol.loadingstrategy.bbox,
			projection: 'EPSG:3857'
		});
		
		var layerWFS = new ol.layer.Vector({
			source: sourceWFS,
			style: styleFunction,
			name: 'Đất ở nông thôn'
		});
		
		var osm = new ol.layer.Tile({
            source: new ol.source.OSM(),
			name: 'OpenStreetMap'
		});
		osm.setVisible(false);
		
		var gm = new ol.layer.Tile({
            source: new ol.source.TileImage({url: 'http://maps.google.com/maps/vt?pb=!1m5!1m4!1i{z}!2i{x}!3i{y}!4i256!2m3!1e0!2sm!3i375060738!3m9!2spl!3sUS!5e18!12m1!1e47!12m3!1e37!2m1!1ssmartmaps!4e0'}),
			name: 'Google Map'
		});
		
		interactionSelectPointerMove = new ol.interaction.Select({
			condition: ol.events.condition.pointerMove
		});
		
		interactionSelect = new ol.interaction.Select({
			wrapX: false
		});
		
		interactionSnap = new ol.interaction.Snap({
			source: layerWFS.getSource()
		});
		
		var modify = new ol.interaction.Modify({
			features: interactionSelect.getFeatures()
		});
		
		var translate = new ol.interaction.Translate({
			features: interactionSelect.getFeatures()
		});
		
        source1 = new ol.source.TileWMS({
            url: 'http://localhost:8080/geoserver/sontinh/wms',
            params: {'LAYERS': 'sontinh:hdatonongthon_region_region', 'TILED': true}
		});
        layer1 = new ol.layer.Tile({
            legendUrl: 'https://stamen-tiles-b.a.ssl.fastly.net/' +
			'terrain-labels/4/4/6.png',
            source: source1,
            name: 'Đất ở nông thôn 2'
		});
		source2 = new ol.source.TileWMS({
            url: 'http://localhost:8080/geoserver/sontinh/wms',
            params: {'LAYERS': 'sontinh:QuyHoachSonTinh', 'TILED': true}
		});
        layer2 = new ol.layer.Tile({
            legendUrl: 'https://stamen-tiles-b.a.ssl.fastly.net/' +
			'terrain-labels/4/4/6.png',
            source: source2,
            name: 'Đất quy hoạch'
		});
		
		var styles = {
			'MultiPolygon': new ol.style.Style({
				stroke: new ol.style.Stroke({
					color: 'yellow',
					width: 3
				})
			})
		};
		
		var styleFunction = function (feature) {
			return styles[feature.getGeometry().getType()];
		};
		
        olMap = new ol.Map({
			controls: ol.control.defaults().extend([
				new ol.control.FullScreen(), new ol.control.ZoomSlider()
			]),
			interactions: ol.interaction.defaults().extend([
				translate, modify, new ol.interaction.DragRotateAndZoom(), new ol.interaction.MouseWheelZoom()
			]),
            layers: [osm, gm, layer2, layerWFS],
			target: 'map',
			overlays: [overlay],
            view: new ol.View({
                center: sonTinh,
                zoom: 14
			})
		});

		
		//wfs-t
		var dirty = {};
		transactWFS = function (mode, f) {
			var node;
			switch (mode) {
				case 'insert':
					node = formatWFS.writeTransaction([f], null, null, formatGML);
					break;
				case 'update':
					node = formatWFS.writeTransaction(null, [f], null, formatGML);
					break;
				case 'delete':
					node = formatWFS.writeTransaction(null, null, [f], formatGML);
					break;
			}
			var payload = xs.serializeToString(node);
			$.ajax('http://localhost:8080/geoserver/sontinh/ows', {
				type: 'POST',
				dataType: 'xml',
				processData: false,
				contentType: 'text/xml',
				data: payload
			}).done(function() {
				sourceWFS.clear();
			});
		};

		olMap.on('singleclick', function(evt) {		
			document.getElementById('map').innerHTML = "Loading... please wait...";
			var view = olMap.getView();
			var viewResolution = view.getResolution();
			var source5 = layer1.getSource();
			var url = source5.getGetFeatureInfoUrl(
			evt.coordinate, viewResolution, view.getProjection(),
			{ 'INFO_FORMAT': 'application/json', 'FEATURE_COUNT': 50 });
			if (url) {
				$.ajax({
					type: "POST",
					url: url,
					contentType: "application/json; charset=utf-8",
					dataType: 'json',
					success: function (n) {
						var content = "<table>";
						var isDataFound = false;
						for (var i = 0; i < n.features.length; i++) {
							var feature = n.features[i];
							var featureAttr = feature.properties;
							isDataFound = true;
							content += "<tr><td><b>Chức năng sử dụng: </b>" + featureAttr["chucnangsd"]
							+ "</td></tr><tr><td><b>Tỉnh/TP: </b>" + featureAttr["tinh_tp"]
							+ "</td></tr><tr><td><b>Quận/Huyện: </b>" + featureAttr["quan_huyen"]
							+"</td></tr>"
							$("#input1").val(featureAttr["chucnangsd"]);
							$("#input2").val(featureAttr["tinh_tp"]);
							$("#input3").val(featureAttr["quan_huyen"]);
						}
						content += "</table>";
						$("#map").html(content);
					}
				});
			}
		});
	
		/* Xu ly su kien Button */
		$('button').click(function () {
			$(this).siblings().removeClass('btn-active');
			$(this).addClass('btn-active');
			olMap.removeInteraction(interaction);
			interactionSelect.getFeatures().clear();
			olMap.removeInteraction(interactionSelect);

			switch ($(this).attr('id')) {

				case 'btnEdit':
					olMap.addInteraction(interactionSelect);
					interaction = new ol.interaction.Modify({
						features: interactionSelect.getFeatures()
					});
					olMap.addInteraction(interaction);
					olMap.addInteraction(interactionSnap);
					dirty = {};
					interactionSelect.getFeatures().on('add', function (e) {
						e.element.on('change', function (e) {
							dirty[e.target.getId()] = true;
						});
					});
					interactionSelect.getFeatures().on('remove', function (e) {
						var f = e.element;
						if (dirty[f.getId()]) {
							delete dirty[f.getId()];
							var featureProperties = f.getProperties();
							delete featureProperties.boundedBy;
							var clone = new ol.Feature(featureProperties);
							clone.setId(f.getId());
							transactWFS('update', clone);
						}
					});
					isClickButton = true;
					break;
					
				case 'btnUpdate':
					interactionSelect.getFeatures().on('add', function (e) {
						e.element.on('change', function (e) {
							var chucnangsd = $("#input1").val();
							var tinh_tp = $("#input2").val();
							var quan_huyen = $("#input3").val();
							var myFeature = e.feature;
							if(chucnangsd != null && tinh_tp != null && quan_huyen != null) {
								myFeature.set('chucnangsd', chucnangsd);
								myFeature.set('tinh_tp', tinh_tp);
								myFeature.set('quan_huyen', quan_huyen);
								transactWFS('insert', myFeature);
							}
						});
					});
					break;

				case 'btnPoint':
					interaction = new ol.interaction.Draw({
						type: 'Point',
						source: layerWFS.getSource()
					});
					olMap.addInteraction(interaction);
					interaction.on('drawend', function (e) {
						var chucnangsd = prompt("Chức năng sử dụng: ", "");
						var tinh_tp = prompt("Tỉnh/TP: ", "");
						var quan_huyen = prompt("Quận/Huyện: ", "");
						var myFeature= e.feature;
						if(chucnangsd != null && tinh_tp != null && quan_huyen != null) {
							myFeature.set('chucnangsd', chucnangsd);
							myFeature.set('tinh_tp', tinh_tp);
							myFeature.set('quan_huyen', quan_huyen);
							transactWFS('insert', e.feature);
						}
					});
					isClickButton = true;
					break;

				case 'btnLine':
					interaction = new ol.interaction.Draw({
						type: 'LineString',
						source: layerWFS.getSource()
					});
					olMap.addInteraction(interaction);
					interaction.on('drawend', function (e) {
						var chucnangsd = prompt("Chức năng sử dụng: ", "");
						var tinh_tp = prompt("Tỉnh/TP: ", "");
						var quan_huyen = prompt("Quận/Huyện: ", "");
						var myFeature= e.feature;
						if(chucnangsd != null || tinh_tp != null || quan_huyen != null) {
							myFeature.set('chucnangsd', chucnangsd);
							myFeature.set('tinh_tp', tinh_tp);
							myFeature.set('quan_huyen', quan_huyen);
							transactWFS('insert', e.feature);
						}
					});
					isClickButton = false;
					break;

				case 'btnArea':
					interaction = new ol.interaction.Draw({
						type: 'Polygon',
						source: layerWFS.getSource()
					});
					interaction.on('drawend', function (e) {
						var chucnangsd = prompt("Chức năng sử dụng: ", "");
						var tinh_tp = prompt("Tỉnh/TP: ", "");
						var quan_huyen = prompt("Quận/Huyện: ", "");
						var myFeature= e.feature;
						if(chucnangsd != null || tinh_tp != null || quan_huyen != null) {
							myFeature.set('chucnangsd', chucnangsd);
							myFeature.set('tinh_tp', tinh_tp);
							myFeature.set('quan_huyen', quan_huyen);
							transactWFS('insert', e.feature);
						}
					});
					olMap.addInteraction(interaction);
					isClickButton = true;
					break;

				case 'btnDelete':
					interaction = new ol.interaction.Select();
					interaction.getFeatures().on('add', function (e) {
						transactWFS('delete', e.target.item(0));
						interactionSelectPointerMove.getFeatures().clear();
						interaction.getFeatures().clear();
					});
					olMap.addInteraction(interaction);
					isClickButton = true;
					break;

				default:
					break;
			}
		});
		
		
        mapComponent = Ext.create('GeoExt.component.Map', {
            map: olMap
		});
		
        mapPanel = Ext.create('Ext.panel.Panel', {
            region: 'center',
            layout: 'fit',
            border: false,
            items: [mapComponent]
		});
		
        treeStore = Ext.create('GeoExt.data.store.LayersTree', {
            layerGroup: olMap.getLayerGroup()
		});
		
		var homeStore = Ext.create('Ext.data.TreeStore', {
			root: {
				text: 'Root',
				expanded: true,
				children: [
					{
						text: 'Huyện Sơn Tịnh',
						leaf: true
					},
					{
						text: 'Huyện Bình Sơn',
						leaf: true
					}
				]
			}
		});
		
		home = Ext.create('Ext.tree.Panel', {
            title: 'Home',
            store: homeStore,
            border: false,
            rootVisible: false,
            hideHeaders: true,
            lines: false,
            flex: 1,
            columns: {
                header: false,
                items: [
                    {
                        xtype: 'treecolumn',
                        dataIndex: 'text',
                        flex: 1,
                        plugins: [
                            {
                                ptype: 'basic_tree_column_legend'
							}
						]
					}
				]
			}
		});
		
        classMap = Ext.create('Ext.tree.Panel', {
            title: 'Lớp bản đồ',
            store: treeStore,
            border: false,
            rootVisible: false,
            hideHeaders: true,
            lines: false,
            flex: 1,
            columns: {
                header: false,
                items: [
                    {
                        xtype: 'treecolumn',
                        dataIndex: 'text',
                        flex: 1,
                        plugins: [
                            {
                                ptype: 'basic_tree_column_legend'
							}
						]
					}
				]
			}
		});
		
		
        var description = Ext.create('Ext.panel.Panel', {
            contentEl: 'map',
            title: 'Thông tin',
            height: 300,
            border: false,
            bodyPadding: 5
		});
		
        Ext.create('Ext.Viewport', {
            layout: 'border',
            items: [
                mapPanel,
                {
                    xtype: 'tabpanel',
                    region: 'west',
                    width: 300,
                    layout: {
                        type: 'vbox',
                        align: 'stretch'
					},
                    items: [
						home,
                        classMap,
                        description
					]
				}
			]
		});
	}
});