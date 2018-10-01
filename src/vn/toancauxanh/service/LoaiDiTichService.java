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

import vn.toancauxanh.model.LoaiDiSan;
import vn.toancauxanh.model.LoaiDiTich;
import vn.toancauxanh.model.QLoaiDiTich;

public class LoaiDiTichService extends BasicService<LoaiDiTich>{
	
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
	public void submitChangeSoThuTu(@BindingParam("list") List<LoaiDiTich> listLoaiDiTich) {
		if ("/backend/assets/img/edit.png".equals(getImg())) {
			setImg("/backend/assets/img/save.png");
			setHoverImg("/backend/assets/img/save_hover.png");
			setCheckChangeStt(true);
		}
		else {
			// Khi bấm save
			for (LoaiDiTich loaiDiTich : listLoaiDiTich) {
				loaiDiTich.saveNotShowNotification();
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

	public JPAQuery<LoaiDiTich> getTargetQuery() {
		String tuKhoa = MapUtils.getString(argDeco(), Labels.getLabel("param.tukhoa"), "").trim();
		
		JPAQuery<LoaiDiTich> q = find(LoaiDiTich.class).where(QLoaiDiTich.loaiDiTich.daXoa.isFalse())
				.where(QLoaiDiTich.loaiDiTich.trangThai.ne(core().TT_DA_XOA));
		
		if (!tuKhoa.isEmpty()) {
			q.where(QLoaiDiTich.loaiDiTich.name.like("%" + tuKhoa +"%").
					or(QLoaiDiTich.loaiDiTich.description.like("%" + tuKhoa +"%")));
		}
		
		return q.orderBy(QLoaiDiTich.loaiDiTich.soThuTu.asc()).orderBy(QLoaiDiTich.loaiDiTich.ngaySua.desc());
	}
	
	public List<LoaiDiTich> getLoaiDiTichAndNull() {
		JPAQuery<LoaiDiTich> q = find(LoaiDiTich.class).where(QLoaiDiTich.loaiDiTich.daXoa.isFalse())
				.where(QLoaiDiTich.loaiDiTich.trangThai.ne(core().TT_DA_XOA));
		List<LoaiDiTich> list = new ArrayList<>();
		list.add(null);
		list.addAll(q.fetch());
		return list;
	}
	
	@Init
	public void deFaultData() {
		if (getTargetQuery().fetchCount() == 0) {
			boosTrapLoaiDiTich();
		}
	}
	
	public void boosTrapLoaiDiTich() {
		LoaiDiTich dtLichSu = new LoaiDiTich();
		dtLichSu.setName("Di tích lịch sử - Văn hóa");
		dtLichSu.setSoThuTu(0);
		dtLichSu.saveNotShowNotification();
		
		LoaiDiTich dtKienTruc = new LoaiDiTich();
		dtKienTruc.setName("Di tích kiến trúc nghệ thuật");
		dtKienTruc.setSoThuTu(0);
		dtKienTruc.saveNotShowNotification();
		
		LoaiDiTich dtKhaoCo = new LoaiDiTich();
		dtKhaoCo.setName("Di tích khảo cổ");
		dtKhaoCo.setSoThuTu(0);
		dtKhaoCo.saveNotShowNotification();
		
		LoaiDiTich dtThangCanh = new LoaiDiTich();
		dtThangCanh.setName("Di tích thắng cảnh");
		dtThangCanh.setSoThuTu(0);
		dtThangCanh.saveNotShowNotification();
		
		LoaiDiTich dtLichSuCM = new LoaiDiTich();
		dtLichSuCM.setName("Di tích lịch sử Cách mạng");
		dtLichSuCM.setSoThuTu(0);
		dtLichSuCM.saveNotShowNotification();
	}
	
}
