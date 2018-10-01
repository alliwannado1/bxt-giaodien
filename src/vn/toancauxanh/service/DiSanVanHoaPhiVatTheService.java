package vn.toancauxanh.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import com.querydsl.jpa.impl.JPAQuery;
import vn.toancauxanh.model.DiSanVanHoaPhiVatThe;
import vn.toancauxanh.model.LoaiDiSan;
import vn.toancauxanh.model.QDiSanVanHoaPhiVatThe;
import vn.toancauxanh.model.QLoaiDiSan;

public class DiSanVanHoaPhiVatTheService extends BasicService<DiSanVanHoaPhiVatThe> {
	
	public JPAQuery<DiSanVanHoaPhiVatThe> getTargetQuery() {
		String tukhoa =  MapUtils.getString(argDeco(), "tukhoa","").trim();
		long loai =  MapUtils.getLongValue(argDeco(), "loai");
		JPAQuery<DiSanVanHoaPhiVatThe> q = find(DiSanVanHoaPhiVatThe.class);
		if (!tukhoa.isEmpty()) {
			q.where(QDiSanVanHoaPhiVatThe.diSanVanHoaPhiVatThe.name.like("%"+tukhoa+"%"));
		}
		if (loai > 0) {
			q.where(QDiSanVanHoaPhiVatThe.diSanVanHoaPhiVatThe.loai.id.eq(loai));
		}
		q.orderBy(QDiSanVanHoaPhiVatThe.diSanVanHoaPhiVatThe.ngaySua.desc());
		removeArgSessionPage();
		return q;
	}

	public DiSanVanHoaPhiVatThe getDiSanVanHoaPhiVatTheById(String id) {
		DiSanVanHoaPhiVatThe diSanVanHoaPhiVatThe  = new DiSanVanHoaPhiVatThe();;
		try {
			Long idDiSanVanHoaPhiVatThe = Long.parseLong(id);
			diSanVanHoaPhiVatThe = find(DiSanVanHoaPhiVatThe.class).where(QDiSanVanHoaPhiVatThe.diSanVanHoaPhiVatThe.id.eq(idDiSanVanHoaPhiVatThe)).fetchOne();
		} catch (Exception e) {
			diSanVanHoaPhiVatThe = new DiSanVanHoaPhiVatThe();
		}
		return diSanVanHoaPhiVatThe;
	}
	
	public List<LoaiDiSan> getLoaiDiSanAndNull() {
		JPAQuery<LoaiDiSan> q = find(LoaiDiSan.class).where(QLoaiDiSan.loaiDiSan.daXoa.isFalse())
				.where(QLoaiDiSan.loaiDiSan.trangThai.ne(core().TT_DA_XOA));
		List<LoaiDiSan> list = new ArrayList<>();
		list.add(null);
		list.addAll(q.fetch());
		return list;
	}
	
	public void removeArgSessionPage() {
		Session session = Sessions.getCurrent();
		if (session.hasAttribute("diTichSessionVM")) {
			session.removeAttribute("diTichSessionVM");
		}
	}
	
	public void putArgSessionPage() {
		Session session = Sessions.getCurrent();
		session.setAttribute("diTichSessionVM", getArg());
	}
	
	@SuppressWarnings("unchecked")
	public void getArgChangeSessionPage() {
		Session session = Sessions.getCurrent();
		session.getAttribute("diTichSessionVM");
		Map<Object, Object> map = (Map<Object, Object>) session.getAttribute("diTichSessionVM");
		if (map != null) {
			getArg().put("loai", map.get("loai"));
			getArg().put("tukhoa", map.get("tukhoa"));
			getArg().put("page", map.get("page"));
		}
	}

	public void sendRedirectPageUrl(String uri) {
		putArgSessionPage();
		Executions.sendRedirect(uri);
	}
	
}
