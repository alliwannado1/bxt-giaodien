package vn.toancauxanh.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.MapUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.model.LoaiDiTich;
import vn.toancauxanh.model.LoaiLeHoi;
import vn.toancauxanh.model.QLoaiDiTich;
import vn.toancauxanh.model.QLoaiLeHoi;

public class LoaiLeHoiService extends BasicService<LoaiLeHoi>{
	
	private String img = "/backend/assets/img/edit.png";
	private String hoverImg = "/backend/assets/img/edit_hover.png";
	private boolean checkChangeStt = false;
	
	public boolean isCheckChangeStt() {
		return checkChangeStt;
	}

	public void setCheckChangeStt(boolean checkChangeStt) {
		this.checkChangeStt = checkChangeStt;
	}

	@Command
	public void submitChangeSoThuTu(@BindingParam("list") List<LoaiLeHoi> listLoaiLeHoi) {
		if ("/backend/assets/img/edit.png".equals(getImg())) {
			setImg("/backend/assets/img/save.png");
			setHoverImg("/backend/assets/img/save_hover.png");
			setCheckChangeStt(true);
		}
		else {
			// Khi bấm save
			for (LoaiLeHoi loaiLeHoi : listLoaiLeHoi) {
				loaiLeHoi.saveNotShowNotification();
			}
			showNotification( "Đã cập nhật số thứ tự thành công", "Thông báo", "success");
			setImg("/backend/assets/img/edit.png");
			setHoverImg("/backend/assets/img/edit_hover.png");
			setCheckChangeStt(false);
		}
		BindUtils.postNotifyChange(null, null, this, "img");
		BindUtils.postNotifyChange(null, null, this, "hoverImg");
		BindUtils.postNotifyChange(null, null, this, "checkChangeStt");
		BindUtils.postNotifyChange(null, null, this, "targetQuery");
	}
	
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getHoverImg() {
		return hoverImg;
	}

	public void setHoverImg(String hoverImg) {
		this.hoverImg = hoverImg;
	}
	
	public JPAQuery<LoaiLeHoi> getTargetQuery() {
		String tuKhoa = MapUtils.getString(argDeco(), Labels.getLabel("param.tukhoa"), "").trim();
		
		JPAQuery<LoaiLeHoi> q = find(LoaiLeHoi.class).where(QLoaiLeHoi.loaiLeHoi.daXoa.isFalse())
				.where(QLoaiLeHoi.loaiLeHoi.trangThai.ne(core().TT_DA_XOA));
		
		if (!tuKhoa.isEmpty()) {
			q.where(QLoaiLeHoi.loaiLeHoi.name.like("%" + tuKhoa +"%").
					or(QLoaiLeHoi.loaiLeHoi.description.like("%" + tuKhoa +"%")));
		}
		
		return q.orderBy(QLoaiLeHoi.loaiLeHoi.soThuTu.asc()).orderBy(QLoaiLeHoi.loaiLeHoi.ngaySua.desc());
	}
	
	public List<LoaiLeHoi> getLoaiLeHoiAndNull() {
		JPAQuery<LoaiLeHoi> q = find(LoaiLeHoi.class).where(QLoaiLeHoi.loaiLeHoi.daXoa.isFalse())
				.where(QLoaiLeHoi.loaiLeHoi.trangThai.ne(core().TT_DA_XOA));
		List<LoaiLeHoi> list = new ArrayList<>();
		list.add(null);
		list.addAll(q.fetch());
		return list;
	}
	
	@Init
	public void deFaultData() {
		if (getTargetQuery().fetchCount() == 0 ) {
			boosTrapLoaiLeHoi();
		}
	}
	
	public void boosTrapLoaiLeHoi() {
		LoaiLeHoi baoTangTinh = new LoaiLeHoi();
		baoTangTinh.setName("Bảo tàng tỉnh");
		baoTangTinh.saveNotShowNotification();
		
		LoaiLeHoi baoTangHoiAn = new LoaiLeHoi();
		baoTangHoiAn.setName("Bảo tàng Hội An");
		baoTangHoiAn.saveNotShowNotification();
		
		LoaiLeHoi baoTangMySon = new LoaiLeHoi();
		baoTangMySon.setName("Bảo tàng Mỹ Sơn");
		baoTangMySon.saveNotShowNotification();
		
		LoaiLeHoi baoTangSaHuynh = new LoaiLeHoi();
		baoTangSaHuynh.setName("Bảo tàng Sa Huỳnh");
		baoTangSaHuynh.saveNotShowNotification();
		
		LoaiLeHoi baoTangDienBan = new LoaiLeHoi();
		baoTangDienBan.setName("Bảo tàng Điện Bàn");
		baoTangDienBan.saveNotShowNotification();
	}
}
