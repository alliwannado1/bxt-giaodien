package vn.toancauxanh.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.MapUtils;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.model.QThucTrangDitich;
import vn.toancauxanh.model.ThucTrangDitich;

public class ThucTrangDitichService extends BasicService<ThucTrangDitich> {
	public JPAQuery<ThucTrangDitich> getTargetQuery() {
		String tuKhoa = MapUtils.getString(argDeco(), Labels.getLabel("param.tukhoa"), "").trim();
		
		JPAQuery<ThucTrangDitich> q = find(ThucTrangDitich.class).where(QThucTrangDitich.thucTrangDitich.daXoa.isFalse())
				.where(QThucTrangDitich.thucTrangDitich.trangThai.ne(core().TT_DA_XOA));
		
		if (!tuKhoa.isEmpty()) {
			q.where(QThucTrangDitich.thucTrangDitich.name.like("%" + tuKhoa +"%").
					or(QThucTrangDitich.thucTrangDitich.description.like("%" + tuKhoa +"%")));
		}
		
		return q.orderBy(QThucTrangDitich.thucTrangDitich.id.desc());
	}
	
	public List<ThucTrangDitich> getThucTrangDiTichAndNull() {
		JPAQuery<ThucTrangDitich> q = find(ThucTrangDitich.class).where(QThucTrangDitich.thucTrangDitich.daXoa.isFalse())
				.where(QThucTrangDitich.thucTrangDitich.trangThai.ne(core().TT_DA_XOA));
		List<ThucTrangDitich> list = new ArrayList<>();
		list.add(null);
		list.addAll(q.fetch());
		return list;
	}
}
