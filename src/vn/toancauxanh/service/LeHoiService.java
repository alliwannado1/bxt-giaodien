package vn.toancauxanh.service;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Session;
import org.zkoss.zk.ui.Sessions;

import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.model.LeHoi;
import vn.toancauxanh.model.QLeHoi;

public class LeHoiService extends BasicService<LeHoi>{
	
	
	public JPAQuery<LeHoi> getTargetQuery() {
		String tukhoa =  MapUtils.getString(argDeco(), "tukhoa","").trim();
		long loai =  MapUtils.getLongValue(argDeco(), "loai");
		JPAQuery<LeHoi> q = find(LeHoi.class);
		if (!tukhoa.isEmpty()) {
			q.where(QLeHoi.leHoi.name.like("%"+tukhoa+"%"));
		}
		if (loai > 0) {
			q.where(QLeHoi.leHoi.loai.id.eq(loai));
		}
		q.orderBy(QLeHoi.leHoi.ngaySua.desc());
		removeArgSessionPage();
		return q;
	}
	
	public LeHoi getLeHoiById(String id) {
		LeHoi leHoi  = new LeHoi();;
		try {
			Long idLeHoi = Long.parseLong(id);
			leHoi = find(LeHoi.class).where(QLeHoi.leHoi.id.eq(idLeHoi)).fetchOne();
		} catch (Exception e) {
			leHoi = new LeHoi();
		}
		return leHoi;
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
			getArg().put("tukhoa", map.get("tukhoa"));
			getArg().put("page", map.get("page"));
			getArg().put("loai", map.get("loai"));
		}
	}

	public void sendRedirectPageUrl(String uri) {
		putArgSessionPage();
		Executions.sendRedirect(uri);
	}
}
