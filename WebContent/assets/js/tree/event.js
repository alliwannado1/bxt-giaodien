var checkChangeClassMap = function(node, checked){
	var index = node.get('index');
	var id = node.get('id');
	var parentId = node.get('parentId');
	var i = 0;
	indexBound = 0;
	
	node.eachChild(function(n){
		node.cascadeBy(function(n){
			n.set('checked', checked);
			if(n.parentNode.getData().id != "lopchuyende"
			&& n.parentNode.getData().id != "bato" && n.parentNode.getData().id != "tunghia")
			n.parentNode.set('checked', checked);
			for (var l = 0; l < arrIndexBound.length - 1; l++) {
				indexBound = arrIndexBound[l];
				if(i < arrIndexBound[l + 1] - arrIndexBound[l]
				&& (id == listId[l] || parentId == listId[l])) {
					if(checked) {
						layerWFS[i + indexBound].setVisible(true);
						if(arrayChecked.indexOf(i + indexBound) < 0)
							arrayChecked.push(i + indexBound);
						} else {
						layerWFS[i + indexBound].setVisible(false);
						arrayChecked.splice(arrayChecked.indexOf(i + indexBound), 1);
					}
				}
			}
			i++;
		});
	});
	
	for (var l = 0; l < arrIndexBound.length - 1; l++) {
		indexBound = arrIndexBound[l];
		if(parentId == listId[l]) {
			if(checked) {
				layerWFS[index + indexBound].setVisible(true);
				if(arrayChecked.indexOf(index + indexBound) < 0)
					arrayChecked.push(index + indexBound);
				} else {
				layerWFS[index + indexBound].setVisible(false);
				arrayChecked.splice(arrayChecked.indexOf(index + indexBound), 1);
			}
		}
	}
};

var onItemClickRadioButton = function(view, record, item, index, e, eOpts){
	var text = record.getData().text;
	
	var pNode = record.parentNode;
	var id = record.get('id');
	pNode.eachChild(function(childNode){
		var childRecord = this.getView().getRecord(childNode);
		if(childNode.get('id') != "lopnen") {
			if(childNode.get('id') != id) {
				childRecord.set('checked', false);
				} else {
				childRecord.set('checked', true);
			}
		}
	}, this);
	
	// all father nodes change by
	for (; pNode != null; pNode = pNode.parentNode) {
		if(id == 'google') {
			if(record.get('checked')) {
				veTinh.setVisible(false);
				gm.setVisible(true);
				} else {
				veTinh.setVisible(false);
				gm.setVisible(false);
			}
			} else if(id == 'vetinh') {
			if(record.get('checked')) {
				gm.setVisible(false);
				veTinh.setVisible(true);
				} else {
				gm.setVisible(false);
				veTinh.setVisible(false);
			}
			} else if(id == 'khongnen') {
			if(record.get('checked')) {
				gm.setVisible(false);
				veTinh.setVisible(false);
				} else {
				gm.setVisible(false);
				veTinh.setVisible(false);
			}
		}
		return true;
	}
}

var onItemClickClassMap = function(views, record, item, index, e, eOpts){
	var text = record.getData().text;
	var index = record.getData().index;
	var id = record.getData().id;
	var parentId = record.getData().parentId;

	indexBound = 0;
	for (var i = 0; i < arrIndexBound.length - 1; i++) {
		indexBound = arrIndexBound[i];
		if(parentId == listId[i]) {
//			if($(window).width() > 1400) {
//				olMap.getView().setZoom(14);
//				view.centerOn(ol.proj.transform(listCenter[i], 'EPSG:3405', 'EPSG:3857'), olMap.getSize(), [700, 500]);
//			} else {
//				olMap.getView().setZoom(13);
//				view.centerOn(ol.proj.transform(listCenter[i], 'EPSG:3405', 'EPSG:3857'), olMap.getSize(), [500, 300]);
//			}
			
			featureNameAdd = listAll[index + indexBound];
			if(btnName == "btnAddArea") {
				$("#btnAddArea").click();
			}
		}
		if(id == listId[i]) {
			if($(window).width() > 1400) {
				olMap.getView().setZoom(14);
				view.centerOn(ol.proj.transform(listCenter[i], 'EPSG:3405', 'EPSG:3857'), olMap.getSize(), [700, 500]);
			} else {
				olMap.getView().setZoom(13);
				view.centerOn(ol.proj.transform(listCenter[i], 'EPSG:3405', 'EPSG:3857'), olMap.getSize(), [500, 300]);
			}
		}
	}
}

var onItemDblClickClassMap = function(views, record, item, index, e, eOpts){
	var text = record.getData().text;
	var index = record.getData().index;
	var id = record.getData().id;
	var parentId = record.getData().parentId;
	
	indexBound = 0;
	for (var i = 0; i < arrIndexBound.length - 1; i++) {
		indexBound = arrIndexBound[i];
		if(parentId == listId[i]) {
			if($(window).width() > 1400) {
				olMap.getView().setZoom(14);
				view.centerOn(ol.proj.transform(listCenter[i], 'EPSG:3405', 'EPSG:3857'), olMap.getSize(), [700, 500]);
				} else {
				olMap.getView().setZoom(13);
				view.centerOn(ol.proj.transform(listCenter[i], 'EPSG:3405', 'EPSG:3857'), olMap.getSize(), [500, 300]);
			}
		}
	}
}
