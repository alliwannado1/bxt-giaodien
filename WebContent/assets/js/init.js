Ext.require(['GeoExt.component.Map', 'GeoExt.data.store.LayersTree',
		'Ext.container.Viewport', 'Ext.layout.container.Border', 'Ext.tab.Panel']);

Ext.application({
	name : 'Quy Hoạch Quảng Ngãi',
	launch : function(){

		var binhSon = ol.proj.fromLonLat([108.7507, 15.2643]);
		var sonTinh = ol.proj.fromLonLat([108.7352, 15.1643]);
		var ducPho = ol.proj.fromLonLat([108.9827, 14.7873]);
		var ttbato = ol.proj.fromLonLat([108.74001502990723, 14.754838840276236]);
		var ttbavi = ol.proj.fromLonLat([108.55238914489743, 14.70395366826115]);
		var xs = new XMLSerializer();

		/**
		 * Thiet lap cac lop vector
		 */

		var i = 0;
		var j = 0;

		function mauSac(element, colors){
			styleLayer = function(feature){
				return new ol.style.Style({
					stroke : new ol.style.Stroke({
						color : 'blue',
						width : 1
					}),
					fill : new ol.style.Fill({
						color : colors
					}),
					text : new ol.style.Text({
						font : '13px Calibri,sans-serif',
						fill : new ol.style.Fill({
							color : '#000'
						}),
						stroke : new ol.style.Stroke({
							color : '#fff',
							width : 2
						}),
						text : element
					})
				})
			};
		}

		var formatWFS = new ol.format.WFS();
		getFeatures();
		listAll.forEach(function(element){
			getStyle(element);
			mauSac(list_ten_dat_qh[i], colors);

			sourceWFS[i] = new ol.source.Vector({
				loader : function(extent){
					$.ajax(wfs_service, {
						type : 'GET',
						data : {
							service : 'WFS',
							version : '1.1.0',
							request : 'GetFeature',
							typename : element,
							srsname : 'EPSG:3857',
							bbox : extent.join(',') + ', EPSG:3857'
						}
					}).done(
							function(response){
								if(formatWFS.readFeatures(response).length > 0) {
									j = listAll.indexOf(formatWFS.readFeatures(response)[0].c
											.split('.')[0]);
									sourceWFS[j].addFeatures(formatWFS.readFeatures(response));
								}
							});
				},
				// strategy:
				// ol.loadingstrategy.tile(ol.tilegrid.createXYZ()),
				strategy : ol.loadingstrategy.bbox,
				projection : 'EPSG:3857'
			});

			layerWFS[i] = new ol.layer.Vector({
				source : sourceWFS[i],
				style : styleLayer,
				name : element
			});
			layerWFS[i].setVisible(false);

			interactionSnap[i] = new ol.interaction.Snap({
				source : layerWFS[i].getSource()
			});

			i++;
		});

		/**
		 * Ket thuc thiet lap cac lop vector
		 */

		$('#content').show();

		veTinh = new ol.layer.Tile({
			source : new ol.source.TileImage({
				url : 'http://mt1.google.com/vt/lyrs=y&x={x}&y={y}&z={z}',
				crossOrigin : "Anonymous"
			}),
			name : 'Vệ tinh'
		});
		veTinh.setVisible(false);

		gm = new ol.layer.Tile({
			source : new ol.source.TileImage({
				url : 'http://mt1.google.com/vt/lyrs=m&x={x}&y={y}&z={z}',
				crossOrigin : "Anonymous"
			}),
			name : 'Google Map'
		});

		interactionSelect = new ol.interaction.Select({
			wrapX : false
		});

		interactionSelectPointerMove = new ol.interaction.Select({
			condition : ol.events.condition.pointerMove
		});

		var modify = new ol.interaction.Modify({
			features : interactionSelect.getFeatures()
		});

		var translate = new ol.interaction.Translate({
			features : interactionSelect.getFeatures()
		});

		var sourcePolygon = new ol.source.TileWMS({
			url : wms_service,
			params : {
				'LAYERS' : layers_polygon,
				'TILED' : true
			}
		});
		var layerPolygon = new ol.layer.Tile({
			legendUrl : 'https://stamen-tiles-b.a.ssl.fastly.net/'
					+ 'terrain-labels/4/4/6.png',
			source : sourcePolygon,
			name : 'Vùng quy hoạch'
		});

		lopNenGroup = new ol.layer.Group({
			layers : [veTinh, gm],
			name : 'Lớp nền'
		});

		var danhSachHuyenGroup = new ol.layer.Group({
			layers : layerWFS,
			name : 'Danh sách huyện'
		});

		view = new ol.View({
			center : ol.proj.transform(listCenter[0], 'EPSG:3405', 'EPSG:3857'),
			zoom : 11,
			minZoom : 11,
			maxZoom : 20
		});

		olMap = new ol.Map({
			controls : ol.control.defaults({
				attributionOptions : {
					collapsible : false
				}
			}).extend(
					[new ol.control.ZoomSlider(), new ol.control.ScaleLine(),
							new ol.control.FullScreen()]),
			interactions : ol.interaction.defaults().extend([translate, modify,
			// new ol.interaction.DragRotateAndZoom(),
			new ol.interaction.MouseWheelZoom()]),
			layers : [lopNenGroup, danhSachHuyenGroup],
			target : 'map',
			view : view
		});

		olMap.getView().on('change:resolution', function(evt){
			if(btnName == "btnSelectOne") {
				$("#btnSelectOne").click();
			}
			if(btnName != "btnPrint") {
//				btnName = '';
				if(olMap.getView().getZoom() >= 13) {
					for (var i = 0; i < arrayChecked.length; i++)
						layerWFS[arrayChecked[i]].setVisible(true);
				}
				if(olMap.getView().getZoom() < 13) {
					for (var i = 0; i < arrayChecked.length; i++)
						layerWFS[arrayChecked[i]].setVisible(false);
				}
			}
		});

		var highlight;
		var featureName;
		var features2;
		var isFeatures = false;
		var displayFeatureInfo = function(pixel){
			var features = olMap.forEachFeatureAtPixel(pixel, function(feature){
				return feature;
			});

			if(features !== highlight) {
				if(highlight) {
					isFeatures = false;
					getStyle(featureName);
					mauSac(list_ten_dat_qh[listAll.indexOf(featureName)], colors);
					
					layerWFS[listAll.indexOf(featureName)].getSource().forEachFeature(
							function(feature){
								if(features2 == feature.c) {
									style = new ol.style.Style({
										stroke : new ol.style.Stroke({
											color : 'blue',
											width : 1
										}),
										fill : new ol.style.Fill({
											color : colors
										}),
										text : new ol.style.Text({
											font : '13px Calibri,sans-serif',
											fill : new ol.style.Fill({
												color : '#000'
											}),
											stroke : new ol.style.Stroke({
												color : '#fff',
												width : 2
											}),
											text : list_ten_dat_qh[listAll.indexOf(feature.c.split(".")[0])]
										})
									});
									feature.setStyle(style);
								}
							});
				}
				if(features) {
					isFeatures = true;
					if(features.c !== undefined) {
						featureName = features.c.split(".")[0];
						layerWFS[listAll.indexOf(featureName)].getSource().forEachFeature(
								function(feature){
									if(features.c == feature.c) {
										features2 = feature.c;
										style = new ol.style.Style({
											stroke : new ol.style.Stroke({
												color : 'red',
												width : 2
											}),
											fill : new ol.style.Fill({
												color : "#FFB90F"
											}),
											text : new ol.style.Text({
												font : '13px Calibri,sans-serif',
												fill : new ol.style.Fill({
													color : '#000'
												}),
												stroke : new ol.style.Stroke({
													color : '#fff',
													width : 2
												}),
												text : list_ten_dat_qh[listAll.indexOf(feature.c.split(".")[0])]
											})
										});
										feature.setStyle(style);
									}
								});
					}
				}

				highlight = features;
			}
		};

		/* Lấy độ phân giải bản đồ */
		widthW = $(window).width();
		heightW = $(window).height() - 64;
		console.log(widthW, heightW);

		olMap.on('pointermove', function(evt){
			var pixel = olMap.getEventPixel(evt.originalEvent);
			if(evt.dragging) {
				$("#flag").hide();
				// document.body.style.cursor = "move";
				return;
			}
			
			if(btnName != "btnSelectOne" && btnName != "btnEditArea" && btnName != "btnAddArea")
				displayFeatureInfo(pixel);
			// document.body.style.cursor = "auto";
		});

		transactWFS = function(mode, f){
			var node;
			var formatGML = new ol.format.GML({
				featureNS : featureNS_service,
				featureType : featureNameAdd,
				extractAttributes : true,
				srsName : 'EPSG:3857'
			});

			switch (mode) {
				case 'insert' :
					node = formatWFS.writeTransaction([f], null, null, formatGML);
					break;
				case 'update' :
					node = formatWFS.writeTransaction(null, [f], null, formatGML);
					break;
				case 'delete' :
					node = formatWFS.writeTransaction(null, null, [f], formatGML);
					break;
			}
			var payload = xs.serializeToString(node);
			$.ajax(wfs_service, {
				type : 'POST',
				dataType : 'xml',
				processData : false,
				contentType : 'text/xml',
				data : payload
			}).done(function(){
				sourceWFS[listAll.indexOf(featureNameAdd)].clear();
				olMap.getView().setZoom(olMap.getView().getZoom() - 0.001);
				olMap.getView().setZoom(olMap.getView().getZoom() + 0.002);
			});
		};

		olMap.on('click', function(evt){
			displayFeatureInfo(evt.pixel);
		});

		var position;
		let isClickHistory = false;
		olMap.on('singleclick', function(evt){
			var view = olMap.getView();
			var viewResolution = view.getResolution();
			var source = layerPolygon.getSource();
			position = evt.coordinate;
			console.log('position: '
					+ ol.proj.transform(position, 'EPSG:3857', 'EPSG:3405'));
			var url = source.getGetFeatureInfoUrl(evt.coordinate, viewResolution,
					view.getProjection(), {
						'INFO_FORMAT' : 'application/json',
						'FEATURE_COUNT' : 50
					});
			if(url) {
				$.ajax({
					type : "POST",
					url : url,
					contentType : "application/json; charset=utf-8",
					dataType : 'json',
					success : function(n){
						var isDataFound = false;
						for (var i = 0; i < n.features.length; i++) {
							featureSelect = n.features[i];
							typeNameClick = featureSelect.id;
							featureNameAdd = featureSelect.id.split(".")[0];
							console.log(typeNameClick);
							var featureAttr = featureSelect.properties;
							isDataFound = true;

							var x = document.getElementById("tbProperties");
							if(featureAttr["ten_dat_qh"] != null)
								x.rows[0].cells[1].innerHTML = featureAttr["ten_dat_qh"];
							else
								x.rows[0].cells[1].innerHTML = '--';
							if(featureAttr["ma_dat_qh"] != null)
								x.rows[1].cells[1].innerHTML = featureAttr["ma_dat_qh"];
							else
								x.rows[1].cells[1].innerHTML = '--';
							if(featureAttr["ten_xa"] != null)
								x.rows[2].cells[1].innerHTML = featureAttr["ten_xa"];
							else
								x.rows[2].cells[1].innerHTML = '--';
							if(featureAttr["ten_huyen"] != null)
								x.rows[3].cells[1].innerHTML = featureAttr["ten_huyen"];
							else
								x.rows[3].cells[1].innerHTML = '--';
							if(featureAttr["dien_tich"])
								x.rows[4].cells[1].innerHTML = featureAttr["dien_tich"];
							else
								x.rows[4].cells[1].innerHTML = '--';
							if(featureAttr["dia_danh"])
								x.rows[5].cells[1].innerHTML = featureAttr["dia_danh"];
							else
								x.rows[5].cells[1].innerHTML = '--';
							if(featureAttr["ghi_chu"] != null)
								x.rows[6].cells[1].innerHTML = featureAttr["ghi_chu"];
							else
								x.rows[6].cells[1].innerHTML = '--';
							if(featureAttr["ty_le"] != null)
								x.rows[7].cells[1].innerHTML = featureAttr["ty_le"];
							else
								x.rows[7].cells[1].innerHTML = '--';

							$('#ten_dat_qh').val(featureAttr["ten_dat_qh"]);
							$('#ma_dat_qh').val(featureAttr["ma_dat_qh"]);
							$('#ten_xa').val(featureAttr["ten_xa"]);
							$('#ten_huyen').val(featureAttr["ten_huyen"]);
							$('#dien_tich').val(featureAttr["dien_tich"]);
							$('#dia_danh').val(featureAttr["dia_danh"]);
							$('#ghi_chu').val(featureAttr["ghi_chu"]);
							$('#ty_le').val(featureAttr["ty_le"]);
						}

						if(isDataFound && isFeatures) {
							if(btnName == 'btnEditArea') {
								/*get list thuoc tinh gui ve java*/
								var list = "";
							   	list = contentThuocTinh();
							   	list += typeNameClick;
							  	zAu.send(new zk.Event(zk.Widget.$('$cs'), "onGetList", list, {toServer:true}));

								$('#btn-history').click(function(){
									if(!isClickHistory) {
										zAu.send(new zk.Event(zk.Widget.$('$cs'), "onGetItemHistory", typeNameClick, {toServer:true}));
										isClickHistory = true;
									}
							  	});
							  	$('#btn-history').show
								$('#tbPropertiesEdit').show();
								$('#rowPropertiesEdit').show();
								$('#tbProperties').hide();
								$('#win').hide();
								$("#map_property").click();
								$("#map_lengend").removeClass("active");
								$("#map_layer").removeClass("active");
								$("#map_property").addClass("active");
								$("#map_search").removeClass("active");
							} else if(btnName == 'btnProperties') {
								$('#tbPropertiesEdit').hide();
								$('#rowPropertiesEdit').hide();
								$('#tbProperties').show();
								$('#win').show();
								$("#map_property").click();
								$("#map_lengend").removeClass("active");
								$("#map_layer").removeClass("active");
								$("#map_property").addClass("active");
								$("#map_search").removeClass("active");
							} else {
								$('#tbPropertiesEdit').hide();
								$('#rowPropertiesEdit').hide();
								$('#tbProperties').hide();
								$('#win').hide();
							}
						} else {
							$('#tbPropertiesEdit').hide();
							$('#rowPropertiesEdit').hide();
							$('#tbProperties').hide();
							$('#win').hide();
							$("#map_layer").click();
						}
					}
				});
			}
		});

		// Xu ly su kien Button
		processBtn(olMap, view);

		mapComponent = Ext.create('GeoExt.component.Map', {
			map : olMap
		});

		mapPanel = Ext.create('Ext.panel.Panel', {
			region : 'center',
			layout : 'fit',
			border : false,
			items : [mapComponent]
		});

		treeStore = Ext.create('GeoExt.data.store.LayersTree', {
			layerGroup : olMap.getLayerGroup()
		});

		var baseMapPanel = Ext.create('Ext.tree.Panel', {
			store : baseMapStore,
			border : false,
			rootVisible : false,
			hideHeaders : true,
			lines : false,
			scrollable : true,
			width : 293,
			flex : 1,
			columns : {
				header : false,
				items : [{
					xtype : 'treecolumn',
					dataIndex : 'text',
					flex : 1,
					plugins : [{
						ptype : 'basic_tree_column_legend'
					}]
				}]
			},
			listeners : {
				'itemclick' : onItemClickRadioButton
			},
			renderTo : Ext.get('base_map')
		});

		var baseMapDropDownPanel = Ext.create('Ext.tree.Panel', {
			store : baseMapStore,
			border : false,
			rootVisible : false,
			hideHeaders : true,
			lines : false,
			scrollable : true,
			width : 150,
			flex : 1,
			columns : {
				header : false,
				items : [{
					xtype : 'treecolumn',
					dataIndex : 'text',
					flex : 1,
					plugins : [{
						ptype : 'basic_tree_column_legend'
					}]
				}]
			},
			listeners : {
				'itemclick' : onItemClickRadioButton
			},
			renderTo : Ext.get('myDropdown')
		});

		var classMapPanel = Ext.create('Ext.tree.Panel', {
			store : classMapStore,
			border : false,
			rootVisible : false,
			hideHeaders : true,
			lines : false,
			scrollable : true,
			width : 292,
			height : $(window).height() - 144,
			flex : 1,
			columns : {
				header : false,
				items : [{
					xtype : 'treecolumn',
					dataIndex : 'text',
					flex : 1,
					plugins : [{
						ptype : 'basic_tree_column_legend'
					}]
				}]
			},
			listeners : {
				'checkchange' : checkChangeClassMap,
				'itemclick' : onItemClickClassMap,
				'itemdblclick' : onItemDblClickClassMap
			},
			renderTo : Ext.get('layers')
		});

		var properties = Ext.create('Ext.panel.Panel', {
			contentEl : 'map',
			border : false,
			bodyPadding : 5,
			renderTo : Ext.get('map_properties')
		});

		var $inner = $(".content");
		var size;
		if($inner.css("padding-left") == "292px") {
			size = [widthW, heightW];
		} else {
			size = [widthW - 292, heightW];
		}

		olMap.setSize(size);
		olMap.renderSync();
		$("#btn-menu").bind("click", function(){
			if($inner.css("padding-left") == "292px") {
				size = [widthW, heightW];
			} else {
				size = [widthW - 292, heightW];
			}

			olMap.setSize(size);
			olMap.renderSync();
		});
	}
});
