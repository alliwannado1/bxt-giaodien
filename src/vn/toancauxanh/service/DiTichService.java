package vn.toancauxanh.service;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import com.querydsl.jpa.impl.JPAQuery;
import vn.toancauxanh.model.DiTich;
import vn.toancauxanh.model.QDiTich;

public class DiTichService extends BasicService<DiTich> {
	public JPAQuery<DiTich> getTargetQuery() {
		String tukhoa = MapUtils.getString(argDeco(), "tukhoa", "").trim();
		long loai = MapUtils.getLongValue(argDeco(), "loai");
		long thuctrangditich = MapUtils.getLongValue(argDeco(), "thuctrangditich");
		JPAQuery<DiTich> q = find(DiTich.class);
		if (!tukhoa.isEmpty()) {
			q.where(QDiTich.diTich.name.like("%" + tukhoa + "%"));
		}
		if (loai > 0) {
			q.where(QDiTich.diTich.loai.id.eq(loai));
		}
		if (thuctrangditich > 0) {
			q.where(QDiTich.diTich.thucTrangDiTich.id.eq(thuctrangditich));
		}
		q.orderBy(QDiTich.diTich.ngaySua.desc());
		removeArgSessionPage();
		return q;
	}

	public JPAQuery<DiTich> getTargetQueryTK(long loai, String capTinh, String capHuyen, String capXa) {
		JPAQuery<DiTich> q = find(DiTich.class); 
		if (loai > 0) {
			q.where(QDiTich.diTich.loai.id.eq(loai));
		}
		if (capXa != null) {
			q.where(QDiTich.diTich.capXa.eq(capXa));
		}
		if (capHuyen != null && capXa == null) {
			q.where(QDiTich.diTich.capXa.isNull());
			q.where(QDiTich.diTich.capHuyen.eq(capHuyen));
		}
		if (capTinh != null && capHuyen == null && capXa == null) {
			q.where(QDiTich.diTich.capXa.isNull());
			q.where(QDiTich.diTich.capHuyen.isNull());
			q.where(QDiTich.diTich.capTinh.eq(capTinh));
		}
		return q;
	}

	public DiTich getDitichById(String id) {
		DiTich ditich = new DiTich();
		;
		try {
			Long idDitich = Long.parseLong(id);
			ditich = find(DiTich.class).where(QDiTich.diTich.id.eq(idDitich)).fetchOne();
		} catch (Exception e) {
			ditich = new DiTich();
		}

		return ditich;
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
			getArg().put("loaiditich", map.get("loaiditich"));
			getArg().put("thuctrangditich", map.get("thuctrangditich"));
		}
	}

	public void sendRedirectPageUrl(String uri) {
		putArgSessionPage();
		Executions.sendRedirect(uri);
	}
	
}
