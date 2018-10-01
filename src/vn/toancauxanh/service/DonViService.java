package vn.toancauxanh.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.zkoss.util.resource.Labels;
import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.model.DonVi;
import vn.toancauxanh.model.QDonVi;

public class DonViService extends BasicService<DonVi> {
	public Map<String, String> getCapDonVi() {
		HashMap<String, String> map = new HashMap<>();
		map.put("xa-phuong", "Xã/Phường");
		map.put("tp-huyen", "Thành phố/Huyện");
		map.put("tinh", "Tỉnh");
		return map;
	}
	
	public Map<String, String> getCapDonViMoi() {
		HashMap<String, String> map = new HashMap<>();
		map.put("xa-phuong", "Xã/Phường");
		map.put("tp-huyen", "Thành phố/Huyện");
		return map;
	}
	
	public JPAQuery<DonVi> getTargetQuery() {
		String tuKhoa = MapUtils.getString(argDeco(), Labels.getLabel("param.tukhoa"), "").trim();
		JPAQuery<DonVi> q = find(DonVi.class);
		if (!tuKhoa.isEmpty()) {
			q.where(QDonVi.donVi.name.like("%"+tuKhoa+"%"));
		}
		q.orderBy(QDonVi.donVi.id.desc());
		return q;
	}
	
	public List<DonVi> getListDonViAndNull() {
		JPAQuery<DonVi> q = find(DonVi.class);
		List<DonVi> arr = q.fetch();
//		arr.add(null);
		return arr;
	}
	
}
