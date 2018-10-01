package vn.toancauxanh.gg.model.enums;

public enum ThamSoEnum {
	EMAIL_LIENHE("Email liên hệ góp ý"),
	PHONE_LIENHE("Số điện thoại liên hệ góp ý"),
	DONVI_HOTRO_KYTHUAT("Đơn vị hỗ trợ kĩ thuật"),
	DONVI_HOTRO("Đơn vị hỗ trợ"),
	PHONE_HOTRO("Số điện thoại hỗ trợ"),
	TAILIEU_HOTRO("Tài liệu hỗ trợ"),
	TAILIEU_HOTRO_FE("Tài liệu hỗ trợ Frontend"),
	API_KEY("Api key google");
	
	String text;
	ThamSoEnum(final String txt) {
		text = txt;
	}
	public String getText() {
		return text;
	}
}
