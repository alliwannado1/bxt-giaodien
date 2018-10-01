package vn.toancauxanh.cms.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import org.apache.commons.collections.MapUtils;
import org.springframework.util.SystemPropertyUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;

import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.gg.model.enums.HomeEnum;
import vn.toancauxanh.gg.model.enums.LatLongQuanHuyenQuangNamEnum;
import vn.toancauxanh.model.DiSanVanHoaPhiVatThe;
import vn.toancauxanh.model.DiTich;
import vn.toancauxanh.model.LeHoi;
import vn.toancauxanh.model.LoaiDiSan;
import vn.toancauxanh.model.LoaiDiTich;
import vn.toancauxanh.model.LoaiLeHoi;
import vn.toancauxanh.model.QDiSanVanHoaPhiVatThe;
import vn.toancauxanh.model.QDiTich;
import vn.toancauxanh.model.QLeHoi;
import vn.toancauxanh.model.QLoaiDiSan;
import vn.toancauxanh.model.QLoaiDiTich;
import vn.toancauxanh.model.QLoaiLeHoi;
import vn.toancauxanh.service.BasicService;

public class HomeService extends BasicService<Object> {

	public HomeService() {
		super();
		// init();
		// login();

		String key = getClass() + "." + "accessed";
		if (!Sessions.getCurrent().hasAttribute(key)) {
			Sessions.getCurrent().setAttribute(key, "1");
			getSetting().addCounter();
		}
	}

	public String formatDate(Date d) {
		return new SimpleDateFormat("dd/MM/yyyy").format(d);
	}

	public String formatDayOfCalendar(Date d) {
		return new SimpleDateFormat("dd").format(d);
	}

	public String formatMonthYearOfCalendar(Date d) {
		return new SimpleDateFormat("MM/yyyy").format(d);
	}

	@Override
	public Date getTuNgay() {
		return date(Labels.getLabel("param.tungay"));
	}

	@Override
	public Date getDenNgay() {
		return date(Labels.getLabel("param.denngay"));
	}

	public boolean isTimChinhXac() {
		boolean exactly = MapUtils.getBooleanValue(getArg(), "chinhxac");
		return exactly;
	}

	public long getCid() {
		return MapUtils.getLongValue(argDeco(), "id");
	}

	public int getPageSize() {
		int pagesize = MapUtils.getIntValue(getArg(), SystemPropertyUtils.resolvePlaceholders(PH_KEYPAGESIZE),
				defaultPageSize());
		if (pagesize == 0) {
			pagesize = 10;
		}
		return pagesize;
	}
	
	public int getPageSizeImage() {
		int pagesize = MapUtils.getIntValue(getArg(), SystemPropertyUtils.resolvePlaceholders(PH_KEYPAGESIZE),
				defaultPageSize());
		if (pagesize == 0) {
			pagesize = 9;
		}
		return pagesize;
	}

	public int getPage() {
		int trang = MapUtils.getIntValue(getArg(), "page");
		return trang;
	}

	private int numberResultBegin = 0;

	private int numberResultEnd = 0;

	private int numberSizeList = 0;

	private long idDetail = -1;

	private Object detail = new Object();

	private String searchByKey = "";
	
	private double lngPlace;
	
	private double latPlace;
	
	private String placeSelected = "";
	
	public boolean isCheckSubmitSearch() {
		return checkSubmitSearch;
	}

	public void setCheckSubmitSearch(boolean checkSubmitSearch) {
		this.checkSubmitSearch = checkSubmitSearch;
	}
	
	private boolean checkSubmitSearch = false;

	public String getPlaceSelected() {
		return placeSelected;
	}

	public void setPlaceSelected(String placeSelected) {
		this.placeSelected = placeSelected;
	}

	public double getLngPlace() {
		return lngPlace;
	}

	public void setLngPlace(double lngPlace) {
		this.lngPlace = lngPlace;
	}

	public double getLatPlace() {
		return latPlace;
	}

	public void setLatPlace(double latPlace) {
		this.latPlace = latPlace;
	}

	public String getSearchByKey() {
		return searchByKey;
	}

	public void setSearchByKey(String searchByKey) {
		this.searchByKey = searchByKey;
	}

	public Object getDetail() {
		Object detailObject = new Object();
		if (HomeEnum.DITICH.getText().equals(searchCategory)) {
			detailObject = find(DiTich.class).where(QDiTich.diTich.id.eq(idDetail)).fetchOne();
		} else if (HomeEnum.LEHOI.getText().equals(searchCategory)) {
			detailObject = find(LeHoi.class).where(QLeHoi.leHoi.id.eq(idDetail)).fetchOne();
		} else if (HomeEnum.DISAN.getText().equals(searchCategory)) {
			detailObject = find(DiSanVanHoaPhiVatThe.class)
					.where(QDiSanVanHoaPhiVatThe.diSanVanHoaPhiVatThe.id.eq(idDetail)).fetchOne();
		}
		setDetail(detailObject);
		if (detailObject != null) {
			setHiddenSearch(true);
			BindUtils.postNotifyChange(null, null, this, "hiddenSearch");
		}
		Clients.evalJavaScript("hiddenLoadingDetail()");
		return detail;
	}

	public Object getLimit3ImagesDetail() {
		Object detailObject = new Object();
		if (HomeEnum.DITICH.getText().equals(searchCategory)) {
			detailObject = find(DiTich.class).where(QDiTich.diTich.id.eq(idDetail)).fetchOne();
		} else if (HomeEnum.LEHOI.getText().equals(searchCategory)) {
			detailObject = find(LeHoi.class).where(QLeHoi.leHoi.id.eq(idDetail)).fetchOne();
		} else if (HomeEnum.DISAN.getText().equals(searchCategory)) {
			detailObject = find(DiSanVanHoaPhiVatThe.class)
					.where(QDiSanVanHoaPhiVatThe.diSanVanHoaPhiVatThe.id.eq(idDetail)).fetchOne();
		}
		setDetail(detailObject);
		if (detailObject != null) {
			setHiddenSearch(true);
			BindUtils.postNotifyChange(null, null, this, "hiddenSearch");
		}
		return detail;
	}

	public void setDetail(Object detail) {
		this.detail = detail;
	}

	public long getIdDetail() {
		/*Clients.evalJavaScript("history.pushState(null, null, '"+Executions.getCurrent().getContextPath()+"');");*/
		setCheckSubmitSearch(false);
		BindUtils.postNotifyChange(null, null, this, "detail");
		resetOffsetResultLienQuans();
		BindUtils.postNotifyChange(null, null, this, "listLienQuanByLoai");
		setCheckShow(HomeEnum.SHOWMAP.getText());
		BindUtils.postNotifyChange(null, null, this, "checkShow");
		if(idDetail != -1) {
			if (HomeEnum.DITICH.getText().equals(searchCategory)) {
				Clients.evalJavaScript("pushStateHistory('"+ HomeEnum.NAMEDITICH.getText() +"', '" + idDetail +"')");
			} else if (HomeEnum.LEHOI.getText().equals(searchCategory)) {
				Clients.evalJavaScript("pushStateHistory('"+ HomeEnum.NAMEBAOTANG.getText() +"', '" + idDetail +"')");
			} else if (HomeEnum.DISAN.getText().equals(searchCategory)) {
				Clients.evalJavaScript("pushStateHistory('"+ HomeEnum.NAMEDISAN.getText() +"', '" + idDetail +"')");
			}
		}
		
		Clients.evalJavaScript("showDetailByIdUrl()");
		return idDetail;
	}

	public void setIdDetail(long idDetail) {
		this.idDetail = idDetail;
	}

	private String checkShow = HomeEnum.SHOWMAP.getText();

	public String getCheckShow() {
		return checkShow;
	}

	public void setCheckShow(String checkShow) {
		this.checkShow = checkShow;
	}

	@Command
	public void showDetail() {
		setCheckShow(HomeEnum.HIDDENMAP.getText());
		setUrlPageDetail("/frontend/home/thongtin.zhtml");
		Clients.evalJavaScript("setHereNowTrue()");
		/*Clients.evalJavaScript("	.pushState(null, null, '"+Executions.getCurrent().getContextPath()+"');");*/
		BindUtils.postNotifyChange(null, null, this, "checkShow");
		BindUtils.postNotifyChange(null, null, this, "urlPageDetail");
		BindUtils.postNotifyChange(null, null, this, "offsetReSultLienQuans");
		BindUtils.postNotifyChange(null, null, this, "nameDoiTuongLienQuan");
		Clients.evalJavaScript("showGioiThieu()");
	}

	public void resetPage() {
		Map<Object, Object> arg = super.getArg();
		arg.put("page", 0);
		BindUtils.postNotifyChange(null, null, this, "page");
	}

	@Command
	public void goBack() {
		setCheckShow(HomeEnum.SHOWMAP.getText());
		setCheckSubmitSearch(false);
		setHiddenSearch(false);
		resetOffsetResultLienQuans();
		BindUtils.postNotifyChange(null, null, this, "checkShow");
		BindUtils.postNotifyChange(null, null, this, "hiddenSearch");
		resetPage();
		Clients.evalJavaScript(
				"markers[thutuTemp].setIcon('"+ Executions.getCurrent().getContextPath() +"/assets/frontend/image/pinred.png');");
		Clients.evalJavaScript("resetInputId()");
		
		Clients.evalJavaScript("clickActive()");
		Clients.evalJavaScript("setHeightSearchBack()");
		Clients.evalJavaScript("setHereNowFalse()");
		Clients.evalJavaScript("hiddenLoading()");
		Clients.evalJavaScript("resetInputId()");
		Clients.evalJavaScript("pushStateHistoryHome()");
		
	}

	private String urlPageDetail;

	public String getUrlPageDetail() {
		return urlPageDetail;
	}

	public void setUrlPageDetail(String urlPageDetail) {
		this.urlPageDetail = urlPageDetail;
	}

	@Command
	public void showMoreDetail(@BindingParam("url") String url) {
		resetPage();
		setUrlPageDetail(url);
		BindUtils.postNotifyChange(null, null, this, "urlPageDetail");
		Clients.evalJavaScript("scrollLocation()");
	}

	private static final int limitResult = 10;
	
	private int offset = 0;
	private long sizeLienQuan = 0;
	public long getSizeLienQuan() {
		return sizeLienQuan;
	}

	public void setSizeLienQuan(long sizeLienQuan) {
		this.sizeLienQuan = sizeLienQuan;
	}

	public JPAQuery<DiTich> getTargetQueryDiTichWso2(boolean checkLimit) {
		long loaiDiTich = MapUtils.getLongValue(argDeco(), "loai");
		JPAQuery<DiTich> q = find(DiTich.class);
		if(isCheckSubmitSearch()) {
			if (!searchByKey.isEmpty()) {
				q.where(QDiTich.diTich.name.like("%" + searchByKey + "%"));
			}
		}
		if (checkLimit) {
			q.limit(limitResult).offset(offset);
		}
		q.orderBy(QDiTich.diTich.ngaySua.desc());
		return q;
	}

	public JPAQuery<DiSanVanHoaPhiVatThe> getTargetQueryDiSanVanHoaPhiVatTheWso2(boolean checkLimit) {
		long loaiDiSan = MapUtils.getLongValue(argDeco(), "loai");
		JPAQuery<DiSanVanHoaPhiVatThe> q = find(DiSanVanHoaPhiVatThe.class);
		if (loaiDiSan > 0) {
			q.where(QDiSanVanHoaPhiVatThe.diSanVanHoaPhiVatThe.loai.id.eq(loaiDiSan));
		}
		if (!searchByKey.isEmpty()) {
			q.where(QDiSanVanHoaPhiVatThe.diSanVanHoaPhiVatThe.name.like("%" + searchByKey + "%"));
		}
		if (checkLimit) {
			q.limit(limitResult).offset(offset);
		}
		q.orderBy(QDiSanVanHoaPhiVatThe.diSanVanHoaPhiVatThe.ngaySua.desc());
		return q;
	}

	public JPAQuery<LeHoi> getTargetQueryLeHoiWso2(boolean checkLimit) {
		long loaiLeHoi = MapUtils.getLongValue(argDeco(), "loai");
		JPAQuery<LeHoi> q = find(LeHoi.class);
		
		if (!searchByKey.isEmpty()) {
			q.where(QLeHoi.leHoi.name.like("%" + searchByKey + "%"));
		}
		if (checkLimit) {
			q.limit(limitResult).offset(offset);
		}
		q.orderBy(QLeHoi.leHoi.ngaySua.desc());
		return q;
	}

	private String searchCategory = HomeEnum.DITICH.getText();
	private String searchCategoryTemp = HomeEnum.DITICH.getText();

	public String getSearchCategoryTemp() {
		return searchCategoryTemp;
	}

	public void setSearchCategoryTemp(String searchCategoryTemp) {
		this.searchCategoryTemp = searchCategoryTemp;
	}

	private ListModelList<Object> listObject = new ListModelList<>();

	public ListModelList<Object> getListObject() {
		listObject.clear();
		offset = 0;
		if (HomeEnum.DITICH.getText().equals(searchCategory)) {
			for (Object diTich : getTargetQueryDiTichWso2(true).fetch()) {
				listObject.add(diTich);
			}
		} else if (HomeEnum.LEHOI.getText().equals(searchCategory)) {
			for (Object leHoi : getTargetQueryLeHoiWso2(true).fetch()) {
				listObject.add(leHoi);
			}
		} else if (HomeEnum.DISAN.getText().equals(searchCategory)) {
			for (Object diSan : getTargetQueryDiSanVanHoaPhiVatTheWso2(true).fetch()) {
				listObject.add(diSan);
			}
		}
		return listObject;
	}

	@Command
	public void nextMore() {
		// Nếu là show more bình thường thì xóa clear đi
		listObject.clear();
		offset += limitResult;
		if (HomeEnum.DITICH.getText().equals(searchCategory)) {
			for (Object diTich : getTargetQueryDiTichWso2(true).fetch()) {
				listObject.add(diTich);
			}
		} else if (HomeEnum.LEHOI.getText().equals(searchCategory)) {
			for (Object leHoi : getTargetQueryLeHoiWso2(true).fetch()) {
				listObject.add(leHoi);
			}
		} else if (HomeEnum.DISAN.getText().equals(searchCategory)) {
			for (Object diSan : getTargetQueryDiSanVanHoaPhiVatTheWso2(true).fetch()) {
				listObject.add(diSan);
			}
		}
		setNumberResultEnd(listObject.size());
		BindUtils.postNotifyChange(null, null, this, "numberResultEnd");
		BindUtils.postNotifyChange(null, null, this, "numberResultBegin");
		Clients.evalJavaScript("addingMarker()");
		Clients.evalJavaScript("setHeightSearch()");
	}

	@Command
	public void prevMore() {
		listObject.clear();
		offset -= limitResult;
		if (HomeEnum.DITICH.getText().equals(searchCategory)) {
			for (Object diTich : getTargetQueryDiTichWso2(true).fetch()) {
				listObject.add(diTich);
			}
		} else if (HomeEnum.LEHOI.getText().equals(searchCategory)) {
			for (Object leHoi : getTargetQueryLeHoiWso2(true).fetch()) {
				listObject.add(leHoi);
			}
		} else if (HomeEnum.DISAN.getText().equals(searchCategory)) {
			for (Object diSan : getTargetQueryDiSanVanHoaPhiVatTheWso2(true).fetch()) {
				listObject.add(diSan);
			}
		}
		setNumberResultEnd(listObject.size());
		BindUtils.postNotifyChange(null, null, this, "numberResultEnd");
		BindUtils.postNotifyChange(null, null, this, "numberResultBegin");
		Clients.evalJavaScript("addingMarker()");
		Clients.evalJavaScript("setHeightSearch()");
	}

	public void setListObject(ListModelList<Object> listObject) {
		this.listObject = listObject;
	}

	public String getSearchCategory() {
		return searchCategory;
	}

	public void setSearchCategory(String searchCategory) {
		this.searchCategory = searchCategory;
	}

	@Command
	public void chooseSearch(@BindingParam("searchCategory") String searchCategory) {
		Map<Object, Object> arg = this.getArg();
		arg.put("loai", null);
		BindUtils.postNotifyChange(null, null, this, "arg");
		setSearchCategoryTemp(searchCategory);
//		setNumberResultBegin(0);
//		setNumberResultEnd(0);
		BindUtils.postNotifyChange(null, null, this, "listObjectLoai");
		// BindUtils.postNotifyChange(null, null, this, "listObject");
		BindUtils.postNotifyChange(null, null, this, "searchLoai");
		// BindUtils.postNotifyChange(null, null, this, "numberResultEnd");
		// BindUtils.postNotifyChange(null, null, this, "numberResultBegin");
		// BindUtils.postNotifyChange(null, null, this, "numberSizeList");
		// Clients.evalJavaScript("addingMarker()");
		// Clients.evalJavaScript("resetInputId()");
		// Clients.evalJavaScript("setHeightSearch()");
	}

	

	public JPAQuery<LoaiDiTich> getTargetQueryLoaiDiTich() {
		JPAQuery<LoaiDiTich> q = find(LoaiDiTich.class).where(QLoaiDiTich.loaiDiTich.daXoa.isFalse())
				.where(QLoaiDiTich.loaiDiTich.trangThai.ne(core().TT_DA_XOA));

		return q.orderBy(QLoaiDiTich.loaiDiTich.soThuTu.asc()).orderBy(QLoaiDiTich.loaiDiTich.ngaySua.desc());
	}

	public JPAQuery<LoaiLeHoi> getTargetQueryLoaiLeHoi() {
		JPAQuery<LoaiLeHoi> q = find(LoaiLeHoi.class).where(QLoaiLeHoi.loaiLeHoi.daXoa.isFalse())
				.where(QLoaiLeHoi.loaiLeHoi.trangThai.ne(core().TT_DA_XOA));

		return q.orderBy(QLoaiLeHoi.loaiLeHoi.soThuTu.asc()).orderBy(QLoaiLeHoi.loaiLeHoi.ngaySua.desc());
	}

	public JPAQuery<LoaiDiSan> getTargetQueryLoaiDiSan() {
		JPAQuery<LoaiDiSan> q = find(LoaiDiSan.class).where(QLoaiDiSan.loaiDiSan.daXoa.isFalse())
				.where(QLoaiDiSan.loaiDiSan.trangThai.ne(core().TT_DA_XOA));

		return q.orderBy(QLoaiDiSan.loaiDiSan.soThuTu.asc()).orderBy(QLoaiDiSan.loaiDiSan.ngaySua.desc());
	}

	private List<Object> listObjectLoai = new ArrayList<>();

	public List<Object> getListObjectLoai() {
		listObjectLoai.clear();
		if (HomeEnum.DITICH.getText().equals(searchCategoryTemp)) {
			DiTich diTich = new DiTich();
			diTich.setName("Tất cả");
			listObjectLoai.add(diTich);
			for (Object loaiDiTich : getTargetQueryLoaiDiTich().fetch()) {
				listObjectLoai.add(loaiDiTich);
			}
		} else if (HomeEnum.LEHOI.getText().equals(searchCategoryTemp)) {
			LeHoi leHoi = new LeHoi();
			leHoi.setName("Tất cả");
			listObjectLoai.add(leHoi);
			for (Object loaiLeHoi : getTargetQueryLoaiLeHoi().fetch()) {
				listObjectLoai.add(loaiLeHoi);
			}
		} else if (HomeEnum.DISAN.getText().equals(searchCategoryTemp)) {
			DiSanVanHoaPhiVatThe diSan = new DiSanVanHoaPhiVatThe();
			diSan.setName("Tất cả");
			listObjectLoai.add(diSan);

			for (Object loaiDiSan : getTargetQueryLoaiDiSan().fetch()) {
				listObjectLoai.add(loaiDiSan);
			}
		}
		return listObjectLoai;
	}

	public void setListObjectLoai(List<Object> listObjectLoai) {
		this.listObjectLoai = listObjectLoai;
	}

	private boolean hiddenSearch;

	public boolean isHiddenSearch() {
		return hiddenSearch;
	}

	public void setHiddenSearch(boolean hiddenSearch) {
		this.hiddenSearch = hiddenSearch;
	}

	public String getSearchLoai() {
		String loai = "";
		if (HomeEnum.DITICH.getText().equals(searchCategoryTemp)) {
			loai = "Loại di tích và danh thắng";
		} else if (HomeEnum.LEHOI.getText().equals(searchCategoryTemp)) {
			loai = "Loại bảo tàng";
		} else if (HomeEnum.DISAN.getText().equals(searchCategoryTemp)) {
			loai = "Loại di sản";
		}
		return loai;
	}

	public String getNameDoiTuong() {
		String loai = "";
		if (HomeEnum.DITICH.getText().equals(searchCategory)) {
			loai = "Di tích và danh thắng";
		} else if (HomeEnum.LEHOI.getText().equals(searchCategory)) {
			loai = "Bảo tàng";
		} else if (HomeEnum.DISAN.getText().equals(searchCategory)) {
			loai = "Di sản";
		}
		return loai;
	}

	@Command
	public void showView(@BindingParam("vm") Object vm, @BindingParam("objectDetail") Object objectDetail,
			@BindingParam("url") String url,@BindingParam("index") int index,@BindingParam("list") List<Object> list) {
		Map<String, Object> map = new HashMap<>();
		map.put("vm", vm);
		map.put("objectDetail", objectDetail);
		map.put("index", index - 1);
		map.put("list", list);
		Executions.createComponents(url, null, map);
	}

	public int getNumberResultBegin() {
		numberResultBegin = (listObject.isEmpty()) ? 0 : offset + 1;
		return numberResultBegin;
	}

	public void setNumberResultBegin(int numberResultBegin) {
		this.numberResultBegin = numberResultBegin;
	}

	public int getNumberResultEnd() {
		numberResultEnd = (limitResult > listObject.size()) ? offset + listObject.size() : offset + limitResult;
		return numberResultEnd;
	}

	
	public String getNameDoiTuongLienQuan() {
		String loai = "";
		if(getDetail() != null) {
			if ("ditich".equalsIgnoreCase(getDetail().getClass().getSimpleName().toLowerCase())) {
				loai = "Di tích và danh thắng";
			} else if ("lehoi".equalsIgnoreCase(getDetail().getClass().getSimpleName().toLowerCase())) {
				loai = "Bảo tàng";
			} else if ("disanvanhoaphivatthe".equalsIgnoreCase(getDetail().getClass().getSimpleName().toLowerCase())) {
				loai = "Di sản";
			}
		}
		return loai;
	}
	
	public void setNumberResultEnd(int numberResultEnd) {
		this.numberResultEnd = numberResultEnd;
	}

	public int getNumberSizeList() {
		if (HomeEnum.DITICH.getText().equals(searchCategory)) {
			numberSizeList = (int) getTargetQueryDiTichWso2(false).fetchCount();
		} else if (HomeEnum.LEHOI.getText().equals(searchCategory)) {
			numberSizeList = (int) getTargetQueryLeHoiWso2(false).fetchCount();
		} else if (HomeEnum.DISAN.getText().equals(searchCategory)) {
			numberSizeList = (int) getTargetQueryDiSanVanHoaPhiVatTheWso2(false).fetchCount();
		}
		return numberSizeList;
	}

	public void setNumberSizeList(int numberSizeList) {
		this.numberSizeList = numberSizeList;
	}

	@Command
	public void showMoreResultLienQuan() {
		offsetReSultLienQuans += limitReSultDetailLienQuans;
		checkFirst = true;
		BindUtils.postNotifyChange(null, null, this, "listLienQuanByLoai");
		BindUtils.postNotifyChange(null, null, this, "offsetReSultLienQuans");
	}

	private int limitReSultDetailLienQuans = 10;
	public int getLimitReSultDetailLienQuans() {
		return limitReSultDetailLienQuans;
	}

	public void setLimitReSultDetailLienQuans(int limitReSultDetailLienQuans) {
		this.limitReSultDetailLienQuans = limitReSultDetailLienQuans;
	}

	private int offsetReSultLienQuans = 0;

	ListModelList<Object> listLienQuanByLoai = new ListModelList<>();

	private boolean checkFirst = true;

	@Transient
	public ListModelList<Object> getListLienQuanByLoai() {
		if (HomeEnum.DITICH.getText().equals(searchCategory) && getDetail() != null && checkFirst) {
			JPAQuery<DiTich> q = find(DiTich.class).where(QDiTich.diTich.daXoa.isFalse())
					.where(QDiTich.diTich.trangThai.ne(core().TT_DA_XOA))
					.where(QDiTich.diTich.ne(((DiTich) getDetail())));
			if (((DiTich) getDetail()).getLoai() != null) {
				q.where(QDiTich.diTich.loai.eq(((DiTich) getDetail()).getLoai()));
			}
			setSizeLienQuan(q.fetchCount());
			q.limit(limitReSultDetailLienQuans).offset(offsetReSultLienQuans).orderBy(QDiTich.diTich.ngaySua.desc());
			listLienQuanByLoai.addAll(q.fetch());
			checkFirst = false;
		} else if (HomeEnum.LEHOI.getText().equals(searchCategory) && getDetail() != null && checkFirst) {
			JPAQuery<LeHoi> q = find(LeHoi.class).where(QLeHoi.leHoi.daXoa.isFalse())
					.where(QLeHoi.leHoi.trangThai.ne(core().TT_DA_XOA)).where(QLeHoi.leHoi.ne(((LeHoi) getDetail())));
			if (((LeHoi) getDetail()).getLoai() != null) {
				q.where(QLeHoi.leHoi.loai.eq(((LeHoi) getDetail()).getLoai()));
			}
			setSizeLienQuan(q.fetchCount());
			q.limit(limitReSultDetailLienQuans).offset(offsetReSultLienQuans).orderBy(QLeHoi.leHoi.ngaySua.desc());;
			listLienQuanByLoai.addAll(q.fetch());
			checkFirst = false;
		} else if (HomeEnum.DISAN.getText().equals(searchCategory) && getDetail() != null && checkFirst) {
			JPAQuery<DiSanVanHoaPhiVatThe> q = find(DiSanVanHoaPhiVatThe.class)
					.where(QDiSanVanHoaPhiVatThe.diSanVanHoaPhiVatThe.daXoa.isFalse())
					.where(QDiSanVanHoaPhiVatThe.diSanVanHoaPhiVatThe.trangThai.ne(core().TT_DA_XOA))
					.where(QDiSanVanHoaPhiVatThe.diSanVanHoaPhiVatThe.ne(((DiSanVanHoaPhiVatThe) getDetail())));
			if (((DiSanVanHoaPhiVatThe) getDetail()).getLoai() != null) {
				q.where(QDiSanVanHoaPhiVatThe.diSanVanHoaPhiVatThe.loai
						.eq(((DiSanVanHoaPhiVatThe) getDetail()).getLoai()));
			}
			setSizeLienQuan(q.fetchCount());
			q.limit(limitReSultDetailLienQuans).offset(offsetReSultLienQuans).orderBy(QDiSanVanHoaPhiVatThe.diSanVanHoaPhiVatThe.ngaySua.desc());;
			listLienQuanByLoai.addAll(q.fetch());
			checkFirst = false;
		}

		return listLienQuanByLoai;
	}

	public int getOffsetReSultLienQuans() {
		return offsetReSultLienQuans;
	}

	public void setOffsetReSultLienQuans(int offsetReSultLienQuans) {
		this.offsetReSultLienQuans = offsetReSultLienQuans;
	}

	public void resetOffsetResultLienQuans() {
		offsetReSultLienQuans = 0;
		listLienQuanByLoai.clear();
		checkFirst = true;
	}


}