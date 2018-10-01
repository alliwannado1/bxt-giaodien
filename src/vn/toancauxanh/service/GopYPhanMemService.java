package vn.toancauxanh.service;

import org.apache.commons.collections.MapUtils;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.model.GopYPhanMem;
import vn.toancauxanh.model.QGopYPhanMem;

public class GopYPhanMemService extends BasicService<GopYPhanMem>{

	public JPAQuery<GopYPhanMem> getTargetQuery() {
		String tuKhoa = MapUtils.getString(argDeco(), Labels.getLabel("param.tukhoa"),"").trim();
		String trangThai = MapUtils.getString(argDeco(), Labels.getLabel("param.trangthai"), "");

		JPAQuery<GopYPhanMem> q = find(GopYPhanMem.class)
				.where(QGopYPhanMem.gopYPhanMem.trangThai.ne(core().TT_DA_XOA));

		if (tuKhoa != null && !tuKhoa.isEmpty()) {
			String tukhoa = "%" + tuKhoa + "%";
			q.where(QGopYPhanMem.gopYPhanMem.noiDung.like(tukhoa)
					.or(QGopYPhanMem.gopYPhanMem.hoTen.like(tuKhoa)
							.or(QGopYPhanMem.gopYPhanMem.soDienThoai.like(tuKhoa)
									.or(QGopYPhanMem.gopYPhanMem.email.like(tuKhoa)))));	
		}
		if (!trangThai.isEmpty()) {
			q.where(QGopYPhanMem.gopYPhanMem.trangThai.eq(trangThai));
		}
		q.orderBy(QGopYPhanMem.gopYPhanMem.ngaySua.desc());
		return q;
	}
	
	public JPAQuery<GopYPhanMem> getGopY() {
		String tuKhoa = MapUtils.getString(argDeco(), Labels.getLabel("param.tukhoa"),"").trim();
		String trangThai = MapUtils.getString(argDeco(), Labels.getLabel("param.trangthai"), "");

		JPAQuery<GopYPhanMem> q = find(GopYPhanMem.class)
				.where(QGopYPhanMem.gopYPhanMem.trangThai.eq(core().TT_AP_DUNG));
		if (tuKhoa != null && !tuKhoa.isEmpty()) {
			String tukhoa = "%" + tuKhoa + "%";
			q.where(QGopYPhanMem.gopYPhanMem.noiDung.like(tukhoa).or(QGopYPhanMem.gopYPhanMem.hoTen.like(tuKhoa)));	
		}
		if (!trangThai.isEmpty()) {
			q.where(QGopYPhanMem.gopYPhanMem.trangThai.eq(trangThai));
		}
		q.orderBy(QGopYPhanMem.gopYPhanMem.ngaySua.desc());
		return q;
	}
	
	
}
