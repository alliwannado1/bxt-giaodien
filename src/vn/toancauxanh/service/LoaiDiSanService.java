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
import vn.toancauxanh.model.QLoaiDiSan;
import vn.toancauxanh.model.QLoaiDiTich;

public class LoaiDiSanService extends BasicService<LoaiDiSan>{
	
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
	public void submitChangeSoThuTu(@BindingParam("list") List<LoaiDiSan> listLoaiDiSan) {
		if ("/backend/assets/img/edit.png".equals(getImg())) {
			setImg("/backend/assets/img/save.png");
			setHoverImg("/backend/assets/img/save_hover.png");
			setCheckChangeStt(true);
		}
		else {
			// Khi bấm save
			for (LoaiDiSan loaiDiSan : listLoaiDiSan) {
				loaiDiSan.saveNotShowNotification();
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
	
	public JPAQuery<LoaiDiSan> getTargetQuery() {
		String tuKhoa = MapUtils.getString(argDeco(), Labels.getLabel("param.tukhoa"), "").trim();
		
		JPAQuery<LoaiDiSan> q = find(LoaiDiSan.class).where(QLoaiDiSan.loaiDiSan.daXoa.isFalse())
				.where(QLoaiDiSan.loaiDiSan.trangThai.ne(core().TT_DA_XOA));
		
		if (!tuKhoa.isEmpty()) {
			q.where(QLoaiDiSan.loaiDiSan.name.like("%" + tuKhoa +"%").
					or(QLoaiDiSan.loaiDiSan.description.like("%" + tuKhoa +"%")));
		}
		
		return q.orderBy(QLoaiDiSan.loaiDiSan.soThuTu.asc()).orderBy(QLoaiDiSan.loaiDiSan.ngaySua.desc());
	}
	
	public List<LoaiDiSan> getLoaiDiSanAndNull() {
		JPAQuery<LoaiDiSan> q = find(LoaiDiSan.class).where(QLoaiDiSan.loaiDiSan.daXoa.isFalse())
				.where(QLoaiDiSan.loaiDiSan.trangThai.ne(core().TT_DA_XOA));
		List<LoaiDiSan> list = new ArrayList<>();
		list.add(null);
		list.addAll(q.fetch());
		return list;
	}
	
	@Init
	public void deFaultData() {
		if (getTargetQuery().fetchCount() == 0) {
			boosTrapLoaiDiSan();
		}
	}
	
	public void boosTrapLoaiDiSan() {
		LoaiDiSan nttd = new LoaiDiSan();
		nttd.setName("Nghệ thuật trình diễn dân gian");
		nttd.saveNotShowNotification();
		
		LoaiDiSan tiengNoi = new LoaiDiSan();
		tiengNoi.setName("Ngôn ngữ");
		tiengNoi.saveNotShowNotification();
		
		LoaiDiSan leHoi = new LoaiDiSan();
		leHoi.setName("Lễ hội truyền thống");
		leHoi.saveNotShowNotification();
		
		LoaiDiSan tapQuan = new LoaiDiSan();
		tapQuan.setName("Tập quán xã hội và tín ngưỡng");
		tapQuan.saveNotShowNotification();
		
		LoaiDiSan triThuc = new LoaiDiSan();
		triThuc.setName("Tri thức dân gian");
		triThuc.saveNotShowNotification();
		
		LoaiDiSan nghe = new LoaiDiSan();
		nghe.setName("Làng nghề truyền thống");
		nghe.saveNotShowNotification();
	}
}
