function getFeatures() {
	var xmlhttp = new XMLHttpRequest();
	xmlhttp.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
					listFeatures = JSON.parse(this.responseText);
					var length = 0;
					for(var i=0; i<listFeatures.huyen.length; i++) {
						length += listFeatures.huyen[i].layer.length;
						arrIndexBound.push(length);
						listLength.push(listFeatures.huyen[i].layer.length);
						listId.push(listFeatures.huyen[i].id);
						listCenter.push(listFeatures.huyen[i].center)
						var columns = [];
						for(var j=0; j<listFeatures.huyen[i].layer.length; j++) {
							getStyle(listFeatures.huyen[i].layer[j].feature);
							columns[j] = setListHuyenStore(listFeatures.huyen[i].layer[j].ten_dat_qh);
							listAll.push(listFeatures.huyen[i].layer[j].feature);
							list_ten_xa.push(listFeatures.huyen[i].ten_xa);
							list_xa_id.push(listFeatures.huyen[i].xa_id);
							list_ten_huyen.push(listFeatures.huyen[i].ten_huyen);
							list_huyen_id.push(listFeatures.huyen[i].huyen_id);
							list_ty_le.push(listFeatures.huyen[i].ty_le);
							list_ten_dat_qh.push(listFeatures.huyen[i].layer[j].ten_dat_qh);
							list_ma_dat_qh.push(listFeatures.huyen[i].layer[j].ma_dat_qh);
						}
						huyenStore[i] = columns;
					}
			}
	};
	xmlhttp.open("GET", urlFeatures, false);
	xmlhttp.send();
}

function setListHuyenStore(ten_dat_qh) {
	return {text : ten_dat_qh, checked : false, leaf : true, iconCls :  cssChuGiai};
}

function getStyle(element) {
	if(element.indexOf('baidoxe') >= 0) {
		colors = 'rgb(0,0,255)';
		cssChuGiai = 'baidoxe';
	} else if(element.indexOf('baithaixaydung') >= 0) {
		colors = 'rgb(127,159,255)';
		cssChuGiai = 'baithaixaydung';
	} else if(element.indexOf('baotonnghiemngat') >= 0) {
		colors = 'rgb(0,124,165)';
		cssChuGiai = 'baotonnghiemngat';
	} else if(element.indexOf('cang') >= 0) {
		colors = 'rgb(0,19,76)';
		cssChuGiai = 'cang';
	} else if(element.indexOf('datanninh') >= 0) {
		colors = 'rgb(127,127,0)';
		cssChuGiai = 'datanninh';
	} else if(element.indexOf('datbenhvien') >= 0) {
		colors = 'rgb(255,0,191)';
		cssChuGiai = 'datbenhvien';
	} else if(element.indexOf('datcanhquan') >= 0) {
		colors = 'rgb(31,127,0)';
		cssChuGiai = 'datcanhquan';
	} else if(element.indexOf('datcatsanhovenbien') >= 0) {
		colors = 'rgb(165,145,82)';
		cssChuGiai = 'datcatsanhovenbien';
	} else if(element.indexOf('datcaycongnghiep') >= 0) {
		colors = 'rgb(127,0,255)';
		cssChuGiai = 'datcaycongnghiep';
	} else if(element.indexOf('datcaythango') >= 0) {
		colors = 'rgb(0,255,0)';
		cssChuGiai = 'datcaythango';
	} else if(element.indexOf('datcayxanh') >= 0) {
		colors = 'rgb(82,165,0)';
		cssChuGiai = 'datcayxanh';
	} else if(element.indexOf('datcongcong') >= 0) {
		colors = 'rgb(255,0,0)';
		cssChuGiai = 'datcongcong';
	} else if(element.indexOf('datcongnghiep') >= 0) {
		colors = 'rgb(82,0,165)';
		cssChuGiai = 'datcongnghiep';
	} else if(element.indexOf('datcongtrinhcongcong') >= 0) {
		colors = 'rgb(165,0,0)';
		cssChuGiai = 'datcongtrinhcongcong';
	} else if(element.indexOf('datcongtrinh') >= 0) {
		colors = 'rgb(255,0,0)';
		cssChuGiai = 'datcongtrinh';
	} else if(element.indexOf('datcoquanhienhuu') >= 0) {
		colors = 'rgb(27,63,0)';
		cssChuGiai = 'datcoquanhienhuu';
	} else if(element.indexOf('datcoquan') >= 0) {
		colors = 'rgb(127,63,0)';
		cssChuGiai = 'datcoquan';
	} else if(element.indexOf('datdancu') >= 0) {
		colors = 'rgb(255,127,0)';
		cssChuGiai = 'datdancu';
	} else if(element.indexOf('datdaumoihientrang') >= 0) {
		colors = 'rgb(51,51,51)';
		cssChuGiai = 'datdaumoihientrang';
	} else if(element.indexOf('datdaumoi') >= 0) {
		colors = 'rgb(38,38,76)';
		cssChuGiai = 'datdaumoi';
	} else if(element.indexOf('datdichvu') >= 0) {
		colors = 'rgb(255,0,255)';
		cssChuGiai = 'datdichvu';
	} else if(element.indexOf('datdiemnghiep') >= 0) {
		colors = 'rgb(127,95,0)';
		cssChuGiai = 'datdiemnghiep';
	} else if(element.indexOf('datdoicanhquan') >= 0) {
		colors = 'rgb(31,127,0)';
		cssChuGiai = 'datdoicanhquan';
	} else if(element.indexOf('datdoinui') >= 0) {
		colors = 'rgb(27,153,21)';
		cssChuGiai = 'datdoinui';
	} else if(element.indexOf('datduan') >= 0) {
		colors = 'rgb(127,127,63)';
		cssChuGiai = 'datduan';
	} else if(element.indexOf('datdulich') >= 0) {
		colors = 'rgb(255,0,255)';
		cssChuGiai = 'datdulich';
	} else if(element.indexOf('datduphong') >= 0) {
		colors = 'rgb(165,124,82)';
		cssChuGiai = 'datduphong';
	} else if(element.indexOf('datdutru') >= 0) {
		colors = 'rgb(63,127,0)';
		cssChuGiai = 'datdutru';
	} else if(element.indexOf('datgiaoduc') >= 0) {
		colors = 'rgb(127,31,0)';
		cssChuGiai = 'datgiaoduc';
	} else if(element.indexOf('datgiaothong') >= 0) {
		colors = 'rgb(255,170,50)';
		cssChuGiai = 'datgiaothong';
	} else if(element.indexOf('dathatangkythuat') >= 0) {
		colors = 'rgb(95,63,127)';
		cssChuGiai = 'dathatangkythuat';
	} else if(element.indexOf('dathatang') >= 0) {
		colors = 'rgb(165,103,82)';
		cssChuGiai = 'dathatang';
	} else if(element.indexOf('dathonhop') >= 0) {
		colors = 'rgb(255,127,127)';
		cssChuGiai = 'dathonhop';
	} else if(element.indexOf('datkhac') >= 0) {
		colors = 'rgb(76,57,38)';
		cssChuGiai = 'datkhac';
	} else if(element.indexOf('datkhotang') >= 0) {
		colors = 'rgb(82,0,165)';
		cssChuGiai = 'datkhotang';
	} else if(element.indexOf('datlamnghiep') >= 0) {
		colors = 'rgb(0,38,38)';
		cssChuGiai = 'datlamnghiep';
	} else if(element.indexOf('datlangxom') >= 0) {
		colors = 'rgb(76,76,38)';
		cssChuGiai = 'datlangxom';
	} else if(element.indexOf('datncdt') >= 0) {
		colors = 'rgb(0,95,127)';
		cssChuGiai = 'datncdt';
	} else if(element.indexOf('datnen') >= 0) {
		colors = 'rgb(51,51,51)';
		cssChuGiai = 'datnen';
	} else if(element.indexOf('datnghiatrang') >= 0) {
		colors = 'rgb(51,51,51)';
		cssChuGiai = 'datnghiatrang';
	} else if(element.indexOf('datnghiduong') >= 0) {
		colors = 'rgb(0,255,191)';
		cssChuGiai = 'datnghiduong';
	} else if(element.indexOf('datnongnghiep') >= 0) {
		colors = 'rgb(0,255,0)';
		cssChuGiai = 'datnongnghiep';
	} else if(element.indexOf('datnuoitrongthuysan') >= 0) {
		colors = 'rgb(127,159,255)';
		cssChuGiai = 'datnuoitrongthuysan';
	} else if(element.indexOf('datodothi') >= 0) {
		colors = 'rgb(255,127,0)';
		cssChuGiai = 'datodothi';
	} else if(element.indexOf('datochinhtrang') >= 0) {
		colors = 'rgb(165,124,82)';
		cssChuGiai = 'datochinhtrang';
	} else if(element.indexOf('datomoi') >= 0) {
		colors = 'rgb(165,82,0)';
		cssChuGiai = 'datomoi';
	} else if(element.indexOf('datonongthon') >= 0) {
		colors = 'rgb(127,127,0)';
		cssChuGiai = 'datonongthon';
	} else if(element.indexOf('datoxaymoi') >= 0) {
		colors = 'rgb(255,127,0)';
		cssChuGiai = 'datoxaymoi';
	} else if(element.indexOf('dato') >= 0) {
		colors = 'rgb(55,127,0)';
		cssChuGiai = 'dato';
	} else if(element.indexOf('datquocphong') >= 0) {
		colors = 'rgb(38,76,0)';
		cssChuGiai = 'datquocphong';
	} else if(element.indexOf('datruonghoc') >= 0) {
		colors = 'rgb(63,255,0)';
		cssChuGiai = 'datruonghoc';
	} else if(element.indexOf('sanvandong') >= 0) {
		colors = 'rgb(0,255,0)';
		cssChuGiai = 'sanvandong';
	} else if(element.indexOf('datst') >= 0) {
		colors = 'rgb(0,165,82)';
		cssChuGiai = 'datst';
	} else if(element.indexOf('dattheducthethao') >= 0) {
		colors = 'rgb(0,76,0)';
		cssChuGiai = 'dattheducthethao';
	} else if(element.indexOf('datthuongmai') >= 0) {
		colors = 'rgb(255,0,0)';
		cssChuGiai = 'datthuongmai';
	} else if(element.indexOf('dattongiao') >= 0) {
		colors = 'rgb(76,0,0)';
		cssChuGiai = 'dattongiao';
	} else if(element.indexOf('dattrongcayantrai') >= 0) {
		colors = 'rgb(19,76,0)';
		cssChuGiai = 'dattrongcayantrai';
	} else if(element.indexOf('dattrongcaylaunam') >= 0) {
		colors = 'rgb(0,127,0)';
		cssChuGiai = 'dattrongcaylaunam';
	} else if(element.indexOf('dattronglua') >= 0) {
		colors = 'rgb(0,255,0)';
		cssChuGiai = 'dattronglua';
	} else if(element.indexOf('dattrusocoquan') >= 0) {
		colors = 'rgb(127,63,0)';
		cssChuGiai = 'dattrusocoquan';
	} else if(element.indexOf('datvanhoa') >= 0) {
		colors = 'rgb(255,0,0)';
		cssChuGiai = 'datvanhoa';
	} else if(element.indexOf('datyte') >= 0) {
		colors = 'rgb(255,0,191)';
		cssChuGiai = 'datyte';
	} else if(element.indexOf('khuphattrien') >= 0) {
		colors = 'rgb(127,63,63)';
		cssChuGiai = 'khuphattrien';
	} else if(element.indexOf('phuchoirongbien') >= 0) {
		colors = 'rgb(145,165,82)';
		cssChuGiai = 'phuchoirongbien';
	} else if(element.indexOf('sanhovenbien') >= 0) {
		colors = 'rgb(145,165,82)';
		cssChuGiai = 'sanhovenbien';
	} else if(element.indexOf('thamco') >= 0) {
		colors = 'rgb(31,127,0)';
		cssChuGiai = 'thamco';
	} else if(element.indexOf('thuydien') >= 0) {
		colors = 'rgb(0,0,255)';
		cssChuGiai = 'thuydien';
	} else if(element.indexOf('truongbenhvien') >= 0) {
		colors = 'rgb(255,0,63)';
		cssChuGiai = 'truongbenhvien';
	} else if(element.indexOf('vung-kcn-khotang') >= 0) {
		colors = 'rgb(82,0,165)';
		cssChuGiai = 'vung-kcn-khotang';
	} else if(element.indexOf('matnuoc') >= 0) {
		colors = 'rgb(0,82,165)';
		cssChuGiai = 'matnuoc';
	}
}
