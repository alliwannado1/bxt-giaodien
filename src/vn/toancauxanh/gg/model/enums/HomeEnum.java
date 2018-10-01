package vn.toancauxanh.gg.model.enums;

public enum HomeEnum {
	NAMEDITICH("ditich"),
	NAMELEHOI("lehoi"),
	NAMEBAOTANG("baotang"),
	NAMEDISAN("disanvanhoaphivatthe"),
	DITICH("0"),
	LEHOI("1"),
	DISAN("2"),
	SHOWMAP("0"),
	HIDDENMAP("1"),
	PAGE_HOME("0"),
	PAGE_DETAIL("1"),
	PAGE_MOREDETAIL("2");
	
	
	String text;


	private HomeEnum(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	
}
