package vn.toancauxanh.gg.model.enums;

public enum TaiKhoanEmailEnum {
	
	TAIKHOAN("thithudh@gmail.com"),
	MATKHAU("BaO23081997");
	
	String text;

	private TaiKhoanEmailEnum(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
	
}
