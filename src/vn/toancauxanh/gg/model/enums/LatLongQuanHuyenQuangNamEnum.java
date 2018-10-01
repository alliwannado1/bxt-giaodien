package vn.toancauxanh.gg.model.enums;

public enum LatLongQuanHuyenQuangNamEnum {
	TINH_QUANG_NAM("Tỉnh Quảng Nam", "49", 15.555079, 108.079588),
	TAM_KY("Tam kỳ", "502", 15.5638292,108.4821677),
	HOI_AN("Hội An", "503",15.8800584, 108.3380469),
	TAY_GIANG("Tây Giang", "504", 15.8852628, 107.4890302),
	DONG_GIANG("Đông Giang", "505", 15.9660249, 107.7831632),
	DAI_LOC("Đại Lộc", "506",15.8497875, 108.0668661),
	DIEN_BAN("Điện Bàn", "507", 15.8915646, 108.2477859),
	DUY_XUYEN("Duy Xuyên", "508", 15.77511, 108.1665855),
	QUE_SON("Quế Sơn", "509",15.6848385, 108.1665855),
	NAM_GIANG("Nam Giang", "510",15.6628546, 107.6215321),
	PHUOC_SON("Phước Sơn", "511", 15.3762517, 107.811),
	HIEP_DUC("Hiệp Đức", "512", 15.5590682, 108.0805694),
	THANG_BINH("Thăng Bình", "513", 15.6890346, 108.3801314),
	TIEN_PHUOC("Tiên Phước", "514", 15.4964006, 108.2614775),
	BAC_TRA_MY("Bắc Trà My", "515", 15.3202715, 108.214028),
	NAM_TRA_MY("Nam Trà My", "516" , 15.0826614 , 108.0954351),
	NUI_THANH("Núi Thành", "517", 15.4229422, 108.5938124),
	PHU_NINH("Phú Ninh", "518", 15.5743256 , 108.4038672),
	NONG_SON("Nông Sơn" , "519", 15.6557978 , 107.9768875);
	
	
	
	String text;
	String maVung;
	public String getMaVung() {
		return maVung;
	}
	public void setMaVung(String maVung) {
		this.maVung = maVung;
	}
	double lat;
	double lng;
	

	
	
	private LatLongQuanHuyenQuangNamEnum(String text, String maVung, double lat, double lng) {
		this.text = text;
		this.maVung = maVung;
		this.lat = lat;
		this.lng = lng;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	
}
