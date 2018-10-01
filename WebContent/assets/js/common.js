function toast(text, typeInfo) {
  $("#snackbar").text( text );
  var x = document.getElementById("snackbar");
  x.className = "show " + typeInfo;
  setTimeout(function(){ x.className = x.className.replace("show", ""); }, 5000);
}

function tooltipInfo (text) {
  $("#tooltipInfo").text(text);
}

function updateFeature(feature) {
  var ten_dat_qh = $('#ten_dat_qh').val();
  var ma_dat_qh = $('#ma_dat_qh').val();
  var ten_xa = $('#ten_xa').val();
  var ten_huyen = $('#ten_huyen').val();
  var dien_tich = $('#dien_tich').val();
  var dia_danh = $('#dia_danh').val();
  var ghi_chu = $('#ghi_chu').val();
  var ty_le = $('#ty_le').val();

  feature.set('ten_dat_qh', ten_dat_qh);
  feature.set('ma_dat_qh', ma_dat_qh);
  feature.set('ten_xa', ten_xa);
  feature.set('ten_huyen', ten_huyen);
  feature.set('dien_tich', dien_tich);
  feature.set('dia_danh', dia_danh);
  feature.set('ghi_chu', ghi_chu);
  feature.set('ty_le', ty_le);
}
