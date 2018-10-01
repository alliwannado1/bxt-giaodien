/* Khai báo kích thước in */
const dims = {
	a0: [1189, 841],
	a1: [841, 594],
	a2: [594, 420],
	a3: [420, 297],
	a4: [297, 210],
	a5: [210, 148]
};

let loading = 0;
let loaded = 0;
let format = 'a4';
let resolution = 300;
let dim = dims[format];
let width = Math.round(dim[0] * resolution / 25.4);
let height = Math.round(dim[1] * resolution / 25.4);

function processBtn(olMap, view) {
	var size = [olMap.getSize()[0], olMap.getSize()[1]];
	$("#btnCapture").bind("click", function(){
		tooltipInfo("");
		olMap.once('postcompose', function(event) {
			var canvas = event.context.canvas;
			if (navigator.msSaveBlob) {
				navigator.msSaveBlob(canvas.msToBlob(), 'quyhoach.png');
			} else {
				canvas.toBlob(function(blob) {
					saveAs(blob, 'quyhoach.png');
				});
			}
		});
		olMap.renderSync();
		btnName = 'btnCapture';
		document.getElementById("btnCapture").classList.toggle("show-capture");
	});

	$("#btnExportPDF").bind("click", function(){
		btnName = 'btnExportPDF';
		tooltipInfo("");
		document.body.style.cursor = 'progress';
		var extent = olMap.getView().calculateExtent(size);

		var source = gm.getSource();

		var tileLoadStart = function() {
			++loading;
		};

		var tileLoadEnd = function() {
			++loaded;
			if (loading === loaded) {
				var canvas = this;
				window.setTimeout(function() {
					loading = 0;
					loaded = 0;
					const data = canvas.toDataURL('image/png');
					const pdf = new jsPDF('landscape', undefined, format);
					pdf.addImage(data, 'JPEG', 0, 0, dim[0], dim[1]);
					pdf.save('quyhoach.pdf');

					source.un('tileloadstart', tileLoadStart);
					source.un('tileloadend', tileLoadEnd, canvas);
					source.un('tileloaderror', tileLoadEnd, canvas);
					const printSize = [width, height];
					olMap.setSize(printSize);
					olMap.getView().fit(extent, {size: printSize});
					olMap.renderSync();
					document.body.style.cursor = 'auto';
				}, 2000);
			}
		};

		olMap.once('postcompose', function(event) {
			source.on('tileloadstart', tileLoadStart);
			source.on('tileloadend', tileLoadEnd, event.context.canvas);
			source.on('tileloaderror', tileLoadEnd, event.context.canvas);
			tileLoadEnd();
		});

		const printSize = [width, height];
		olMap.setSize(printSize);
		olMap.getView().fit(extent, {size: printSize});
		olMap.renderSync();
	});

	$("#btnPrint").bind("click", function(){
		btnName = 'btnPrint';
		tooltipInfo("");
		document.body.style.cursor = 'progress';
		var extent = olMap.getView().calculateExtent(size);

		var source = gm.getSource();

		var tileLoadStart = function() {
			++loading;
		};

		var tileLoadEnd = function() {
			++loaded;
			if (loading === loaded) {
				var canvas = this;
				window.setTimeout(function() {
					loading = 0;
					loaded = 0;
					const data = canvas.toDataURL('image/png');

					source.un('tileloadstart', tileLoadStart);
					source.un('tileloadend', tileLoadEnd, canvas);
					source.un('tileloaderror', tileLoadEnd, canvas);
					const printSize = [width, height-height*0.1];
					olMap.setSize(printSize);
					olMap.getView().fit(extent, {size: printSize});
					olMap.renderSync();
					document.body.style.cursor = 'auto';
					printJS({printable: canvas.toDataURL('image/png'), type: 'image', header: 'Thông tin quy hoạch'});
				}, 500);
			}
		};

		olMap.once('postcompose', function(event) {
			source.on('tileloadstart', tileLoadStart);
			source.on('tileloadend', tileLoadEnd, event.context.canvas);
			source.on('tileloaderror', tileLoadEnd, event.context.canvas);
			tileLoadEnd();
		});

		const printSize = [width, height];
		olMap.setSize(printSize);
		olMap.getView().fit(extent, {size: printSize});
		olMap.renderSync();
	});

	$("#btnProperties").bind("click", function(){
		tooltipInfo("Thuộc tính");
		document.body.style.cursor = "help";
		olMap.removeInteraction(interaction);
		interactionSelect.getFeatures().clear();
		olMap.removeInteraction(interactionSelect);

		btnName = 'btnProperties';
	});

	$("#btnSelectOne").bind("click", function(){
		tooltipInfo("Chọn vùng");
		olMap.removeInteraction(interaction);
		interactionSelect.getFeatures().clear();
		olMap.removeInteraction(interactionSelect);
		interaction = new ol.interaction.Select();
		olMap.addInteraction(interaction);
		document.body.style.cursor = "default";
		btnName = 'btnSelectOne';
	});

	$("#btnCancel").bind("click", function(){
		tooltipInfo("");
		olMap.removeInteraction(interaction);
		interactionSelect.getFeatures().clear();
		olMap.removeInteraction(interactionSelect);
		document.body.style.cursor = "default";
		btnName = 'btnCancel';
	});
	
	let checkFS = false;
	$(".ol-full-screen-false").bind("click", function(){
		tooltipInfo("");
		if(!checkFS) {
			olMap.removeInteraction(interaction);
			interactionSelect.getFeatures().clear();
			olMap.removeInteraction(interactionSelect);
			document.body.style.cursor = "default";
			checkFS = true;
		} else {
			$("#" + btnName).click();
			checkFS = false;
		}
	});

	//Chỉnh sửa vùng và thuộc tính
	$("#btnEditArea").bind("click", function(){
		document.body.style.cursor = "copy";
		tooltipInfo("Sửa vùng");
		olMap.removeInteraction(interaction);
		interactionSelect.getFeatures().clear();
		olMap.removeInteraction(interactionSelect);

		olMap.addInteraction(interactionSelect);
		interaction = new ol.interaction.Modify({
			features: interactionSelect.getFeatures()
		});
		olMap.addInteraction(interaction);
		for(var i=0; i<listAll.length;i++) {
			olMap.addInteraction(interactionSnap[i]);
		}

		interactionSelect.getFeatures().on('add', function (e) {
			e.element.on('change', function (e) {
				dirty[e.target.getId()] = true;
			});
			var f = e.element;
			zAu.send(new zk.Event(zk.Widget.$('$chinhSua'), "getChangeUpdate", null, {toServer:true}));
			$("#btnUpdate").bind("click", function(e){
				//open popup
				e.preventDefault();
				$('.cd-popup').addClass('is-visible');
				$("#txtConfirm").text('Bạn có muốn cập nhật thay đổi ?');
				$("#confirm-yes").bind("click", function(e){
					var featureProperties = f.getProperties();
					delete featureProperties.boundedBy;
					var clone = new ol.Feature(featureProperties);
					clone.setId(f.getId());

					updateFeature(clone);
					transactWFS('update', clone);
					//Thông báo trạng thái update thành công
					toast('Cập nhật thành công !', 'alert-success');
					//close popup
					$('.cd-popup').removeClass('is-visible');
					$("#btnEditArea").click();
				});
				$("#confirm-no").bind("click", function(e){
					//close popup
					$('.cd-popup').removeClass('is-visible');
					$("#btnEditArea").click();
				});
				//close popup
				if( $(e.target).is('.cd-popup-close') || $(e.target).is('.cd-popup') ) {
					e.preventDefault();
					$(this).removeClass('is-visible');
					$("#btnEditArea").click();
				}

				btnName = 'btnUpdate';
			});
		});
		interactionSelect.getFeatures().on('remove', function (e) {
			var f = e.element;

			if (dirty[f.getId()] && btnName != "btnUpdate") {
				delete dirty[f.getId()];
				var featureProperties = f.getProperties();
				delete featureProperties.boundedBy;
				//open popup
				e.preventDefault();
				$('.cd-popup').addClass('is-visible');
				$("#txtConfirm").text('Bạn có muốn cập nhật thay đổi ?');
				$("#confirm-yes").bind("click", function(e){
					var clone = new ol.Feature(featureProperties);
					clone.setId(f.getId());

					var coordinates = (clone.getGeometry().transform('EPSG:3857', 'EPSG:4326')).getCoordinates()[0][0];
					var polygon_feature = new ol.Feature({
						geometry: new ol.geom.MultiPolygon(
							[coordinates]
						)
					});

					//Tính diện tích
					var sphere = new ol.Sphere(6378137);
					var area_m = sphere.geodesicArea(coordinates);
					if(area_m < 0)
						area_m = area_m * (-1);
					var area_ha = area_m/10000;
					console.log('area: ', area_ha, 'ha');
					clone.set('dien_tich', area_ha.toFixed(3));
					clone.getGeometry().transform('EPSG:4326', 'EPSG:3857');
					transactWFS('update', clone);

					//Thông báo trạng thái update thành công
					toast('Cập nhật thành công !', 'alert-success');
					//close popup
					$('.cd-popup').removeClass('is-visible');
					$("#btnEditArea").click();
				});

				$("#confirm-no").bind("click", function(e){
					//close popup
					$('.cd-popup').removeClass('is-visible');
					$("#btnEditArea").click();
				});
				//close popup
				if( $(e.target).is('.cd-popup-close') || $(e.target).is('.cd-popup') ) {
					e.preventDefault();
					$(this).removeClass('is-visible');
					$("#btnEditArea").click();
				}
			};
		});

		btnName = 'btnEditArea';
	});

	//Xoá vùng
	$("#btnDeleteArea").bind("click", function(){
		tooltipInfo("Xoá vùng");
		olMap.removeInteraction(interaction);
		interactionSelect.getFeatures().clear();
		olMap.removeInteraction(interactionSelect);
		interaction = new ol.interaction.Select();
		interaction.getFeatures().on('add', function (e) {
			//open popup
			e.preventDefault();
			$('.cd-popup').addClass('is-visible');
			$("#txtConfirm").text('Bạn có muốn xoá vùng đã chọn ?');
			$("#confirm-yes").bind("click", function(event){
				transactWFS('delete', e.target.item(0));

				//Thông báo trạng thái xoá thành công
				toast('Xoá thành công !', 'alert-success');
				
				//close popup
				$('.cd-popup').removeClass('is-visible');
				$("#btnDeleteArea").click();
			});
			$("#confirm-no").bind("click", function(e){
				//close popup
				$('.cd-popup').removeClass('is-visible');
				$("#btnDeleteArea").click();
			});
			//close popup
			if( $(e.target).is('.cd-popup-close') || $(e.target).is('.cd-popup') ) {
				e.preventDefault();
				$(this).removeClass('is-visible');
				$("#btnDeleteArea").click();
			}
		});
		olMap.addInteraction(interaction);

		btnName = 'btnDeleteArea';
	});

	//Thêm vùng
	$("#btnAddArea").bind("click", function(){
		document.body.style.cursor = "copy";

		if(featureNameAdd != "") {
			tooltipInfo(list_ten_xa[listAll.indexOf(featureNameAdd)] + " - " + list_ten_dat_qh[listAll.indexOf(featureNameAdd)]);
			olMap.removeInteraction(interaction);
			interactionSelect.getFeatures().clear();
			olMap.removeInteraction(interactionSelect);
			interaction = new ol.interaction.Draw({
				type: 'MultiPolygon',
				source: layerWFS[listAll.indexOf(featureNameAdd)].getSource()
			});
			
			var featuresDrawNumber = 0;
			var area_m = [];
			interaction.on('drawend', function (e) {
				olMap.removeInteraction(interaction);
				//open popup
				e.preventDefault();
				$('.cd-popup').addClass('is-visible');
				$("#txtConfirm").text('Bạn có muốn thêm vùng đã tạo ?');
				$("#confirm-yes").bind("click", function(event){
					var myFeature = e.feature;
					var coordinates = (myFeature.getGeometry().transform('EPSG:3857', 'EPSG:4326')).getCoordinates()[0][0];
					var polygon_feature = new ol.Feature({
						geometry: new ol.geom.MultiPolygon(
							[coordinates]
						)
					});

					//Tính diện tích
					var sphere = new ol.Sphere(6378137);
					area_m.push(sphere.geodesicArea(coordinates) * (-1));
					
					if(area_m.length <= 1) {
						var area_ha = area_m[featuresDrawNumber]/10000;
						myFeature.set('ten_dat_qh', list_ten_dat_qh[listAll.indexOf(featureNameAdd)]);
						myFeature.set('ma_dat_qh', list_ma_dat_qh[listAll.indexOf(featureNameAdd)]);
						myFeature.set('ten_xa', list_ten_xa[listAll.indexOf(featureNameAdd)]);
						myFeature.set('xa_id', list_xa_id[listAll.indexOf(featureNameAdd)]);
						myFeature.set('ten_huyen', list_ten_huyen[listAll.indexOf(featureNameAdd)]);
						myFeature.set('huyen_id', list_huyen_id[listAll.indexOf(featureNameAdd)]);
						myFeature.set('dien_tich', area_ha.toFixed(3));
						myFeature.set('ty_le', list_ty_le[listAll.indexOf(featureNameAdd)]);
						myFeature.getGeometry().transform('EPSG:4326', 'EPSG:3857');
						console.log(area_ha);
						transactWFS('insert', myFeature);
					}
					
					
					//Thông báo trạng thái thêm thành công
					toast('Thêm thành công !', 'alert-success');
					//close popup
					$('.cd-popup').removeClass('is-visible');
					featuresDrawNumber++;
					$("#btnAddArea").click();
				});
				$("#confirm-no").bind("click", function(e){
					//close popup
					$('.cd-popup').removeClass('is-visible');
					sourceWFS[listAll.indexOf(featureNameAdd)].clear();
					featuresDrawNumber++;
					$("#btnAddArea").click();
				});
				//close popup
				if( $(e.target).is('.cd-popup-close') || $(e.target).is('.cd-popup') ) {
					e.preventDefault();
					$(this).removeClass('is-visible');
					$("#btnAddArea").click();
				}
				featuresDrawNumber = 0;
			});
			
			olMap.addInteraction(interaction);
		} else {
			tooltipInfo("");
			toast('Hãy chọn lớp cần thêm !', 'alert-warning');
		}

		btnName = 'btnAddArea';
	});

	$('.toolbar').bind('click', function() {
	    //    Remove .active class from all .tab class elements
	    $('.toolbar').removeClass('selected');
		// remove current feature
		if (hasClass($(this),'selected')) {
			console.log("clicktab")
    		olMap.removeInteraction(interaction);
    		interactionSelect.getFeatures().clear();
    		olMap.removeInteraction(interactionSelect);
    		document.body.style.cursor = "default";
    	};
	    //    Add .active class to currently clicked element
	    if ($(this).attr('id') == "btnCapture" || $(this).attr('id') == "btnExportPDF" ||
	    		$(this).attr('id') == "btnPrint" || $(this).attr('id') == "btnPrint" || $(this).attr('id') == "btnCancel" || $(this).attr('id') == "btnSearch"
	    			|| $(this).attr('id') == "btn-menu") {
	    } else {
	    	$(this).addClass('selected');
	    }
	});
};

function search() {
	tooltipInfo("");
	var toado;
	var toadoX = document.getElementById('toadoX').value;;
	var toadoY = document.getElementById('toadoY').value;
	var toadoX_Do = document.getElementById('toadoX-Do').value;
	var toadoX_Phut = document.getElementById('toadoX-Phut').value;
	var toadoX_Giay = document.getElementById('toadoX-Giay').value;
	var toadoY_Do = document.getElementById('toadoY-Do').value;
	var toadoY_Phut = document.getElementById('toadoY-Phut').value;
	var toadoY_Giay = document.getElementById('toadoY-Giay').value;
	var value = $("[name=radio]:checked").val();
	if(value=='thap-phan') {
		toado = ol.proj.transform([Number(toadoX), Number(toadoY)], 'EPSG:3405', 'EPSG:3857');
  } else {
  	toado = ol.proj.transform([Number(toadoX_Do) + Number(toadoX_Phut) + Number(toadoX_Giay), Number(toadoY_Do) + Number(toadoY_Phut) + Number(toadoY_Giay)], 'EPSG:3405', 'EPSG:3857');
  }


	if((toadoX != '' && toadoY != '') || (toadoX_Do != '' && toadoX_Phut != '' && toadoX_Giay != '' && toadoY_Do != '' && toadoY_Phut != '' && toadoY_Giay != '')) {
		$("#flag").show();
		olMap.getView().setZoom(17);
		view.centerOn(toado, olMap.getSize(), [500, 300]);
	}
}

function setZoomMap(zoom) {
	var zoomC = olMap.getView().getZoom();
	var properties = olMap.getView().getProperties();
	properties["maxZoom"] = zoom;
	olMap.setView(new ol.View(properties));
	if(zoomC > 17)
		olMap.getView().setZoom(17);
}

function hasClass(element, cls) {
    return (' ' + element.className + ' ').indexOf(' ' + cls + ' ') > -1;
}
