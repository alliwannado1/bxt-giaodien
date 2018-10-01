package vn.toancauxanh.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.MapUtils;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.DefaultTreeModel;
import org.zkoss.zul.TreeNode;
import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.model.DonViHanhChinh;
import vn.toancauxanh.model.QDonViHanhChinh;

public class DonViHanhChinhService extends BasicService<DonViHanhChinh> {
	public JPAQuery<DonViHanhChinh> getTargetQuery() {
		JPAQuery<DonViHanhChinh> q = find(DonViHanhChinh.class);
		return q;
	}

	public JPAQuery<DonViHanhChinh> getDonViCapHuyen() {
		JPAQuery<DonViHanhChinh> q = find(DonViHanhChinh.class);
		DonViHanhChinh tinhQuangNam = find(DonViHanhChinh.class).where(QDonViHanhChinh.donViHanhChinh.donViHanhChinhCha.isNull()).fetchOne();
		q.where(QDonViHanhChinh.donViHanhChinh.donViHanhChinhCha.eq(tinhQuangNam));
		return q;
	}
	
	public List<DonViHanhChinh> getDonViCha() {
		JPAQuery<DonViHanhChinh> q = find(DonViHanhChinh.class);
		DonViHanhChinh tinhQuangNam = find(DonViHanhChinh.class).where(QDonViHanhChinh.donViHanhChinh.donViHanhChinhCha.isNull()).fetchOne();
		q.where(QDonViHanhChinh.donViHanhChinh.donViHanhChinhCha.eq(tinhQuangNam));
		List<DonViHanhChinh> arr = new ArrayList<>();
		arr.add(tinhQuangNam);
		arr.addAll(q.fetch());
		return arr;
	}
	
	public JPAQuery<DonViHanhChinh> getDonViCapTinh() {
		JPAQuery<DonViHanhChinh> q = find(DonViHanhChinh.class);
		q.where(QDonViHanhChinh.donViHanhChinh.donViHanhChinhCha.isNull());
		return q;
	}
	
	public List<DonViHanhChinh> getListCapQuanLi(String capQuanLi) {
		JPAQuery<DonViHanhChinh> q = find(DonViHanhChinh.class);
		List<DonViHanhChinh> arrTemp = q.fetch();
		List<DonViHanhChinh> arr = new ArrayList<>();
		DonViHanhChinh tinhQuangNam = new DonViHanhChinh();
		for (DonViHanhChinh dvhct : arrTemp) {
			if (dvhct.getDonViHanhChinhCha() == null) {
				tinhQuangNam = dvhct;
				break;
			}
		}
		if ("Xã - Phường".equals(capQuanLi)) {
			for (DonViHanhChinh dvhc : arrTemp) {
				if (dvhc.getDonViHanhChinhCha() != tinhQuangNam && dvhc.getDonViHanhChinhCha() != null) {
					arr.add(dvhc);
				}
			}
		}
		if ("Thành phố - Huyện".equals(capQuanLi)) {
			for (DonViHanhChinh dvhc : arrTemp) {
				if (dvhc.getDonViHanhChinhCha() == tinhQuangNam) {
					arr.add(dvhc);
				}
			}
		}
		if ("Tỉnh".equals(capQuanLi)) {
			arr.add(tinhQuangNam);
		}
		if ("".equals(capQuanLi)) {
			arr.addAll(arrTemp);
		}
		return arr;
	}

	public List<DonViHanhChinh> getListDonViCapHuyen() {
		JPAQuery<DonViHanhChinh> q = find(DonViHanhChinh.class);
		List<DonViHanhChinh> arrTemp = q.fetch();
		List<DonViHanhChinh> arr = new ArrayList<>();
		for (DonViHanhChinh dvhc : arrTemp) {
			if (dvhc.getDonViHanhChinhCha() == null) {
				arr.add(dvhc);
			}
		}
		return arr;
	}

	@Init
	public void deFaultData() {
		if (getTargetQuery().fetchCount() == 0) {
			boosTrapDonViHanhChinh();
		}
	}

	public void boosTrapDonViHanhChinh() {
		DonViHanhChinh tinhQuangNam = new DonViHanhChinh();
		tinhQuangNam.setName("Tỉnh Quảng Nam");
		tinhQuangNam.saveNotShowNotification();
		
		DonViHanhChinh tpTamKi = new DonViHanhChinh();
		tpTamKi.setName("Thành phố Tam Kì");
		tpTamKi.setDonViHanhChinhCha(tinhQuangNam);
		tpTamKi.saveNotShowNotification();

		DonViHanhChinh phuongTanThanh = new DonViHanhChinh();
		phuongTanThanh.setName("Phường Tân Thạnh");
		phuongTanThanh.setDonViHanhChinhCha(tpTamKi);
		phuongTanThanh.saveNotShowNotification();

		DonViHanhChinh phuongPhuocHoa = new DonViHanhChinh();
		phuongPhuocHoa.setName("Phường Phước Hòa");
		phuongPhuocHoa.setDonViHanhChinhCha(tpTamKi);
		phuongPhuocHoa.saveNotShowNotification();

		DonViHanhChinh tpHoiAn = new DonViHanhChinh();
		tpHoiAn.setName("Thành phố Hội An");
		tpHoiAn.setDonViHanhChinhCha(tinhQuangNam);
		tpHoiAn.saveNotShowNotification();

		DonViHanhChinh phuongMinhAn = new DonViHanhChinh();
		phuongMinhAn.setName("Phường Minh An");
		phuongMinhAn.setDonViHanhChinhCha(tpHoiAn);
		phuongMinhAn.saveNotShowNotification();
	}

	public List<DonViHanhChinh> getList() {
		String param = MapUtils.getString(argDeco(), "tukhoa", "").trim();
		String trangThai = MapUtils.getString(argDeco(),"trangthai","");
		JPAQuery<DonViHanhChinh> q = find(DonViHanhChinh.class);
		q.where(QDonViHanhChinh.donViHanhChinh.trangThai.ne(core().TT_DA_XOA));
		q.where(QDonViHanhChinh.donViHanhChinh.donViHanhChinhCha.isNull());
		q.orderBy(QDonViHanhChinh.donViHanhChinh.ngaySua.desc());
		List<DonViHanhChinh> list = q.fetch();
		for (DonViHanhChinh donVi : list) {
			if (donVi.getName().toLowerCase().contains(param.toLowerCase())) {
				donVi.loadChildren();
			} else{
				donVi.loadChildren(param, trangThai);
			}
		}
		return list;
	}

	public List<DonViHanhChinh> getDonViHanhChinhChildren(DonViHanhChinh category) {
		List<DonViHanhChinh> list = new ArrayList<>();
		if (!category.getTrangThai().equalsIgnoreCase(core().TT_DA_XOA)) {
			for (TreeNode<DonViHanhChinh> el : category.getNode().getChildren()) {
				list.add(el.getData());
				list.addAll(getDonViHanhChinhChildren(el.getData()));
			}
		}
		return list;
	}

	public void openObject(DefaultTreeModel<DonViHanhChinh> model, TreeNode<DonViHanhChinh> node) {
		if (node.isLeaf()) {
			model.addOpenObject(node);
		} else {
			for (TreeNode<DonViHanhChinh> child : node.getChildren()) {
				model.addOpenObject(child);
				openObject(node.getModel(), child);
			}
		}
	}

	public DefaultTreeModel<DonViHanhChinh> getModel() {
		String param = MapUtils.getString(argDeco(), "tukhoa", "").trim();
		DonViHanhChinh donViGoc = new DonViHanhChinh();
		DefaultTreeModel<DonViHanhChinh> model = new DefaultTreeModel<>(donViGoc.getNode(), true);
		for (DonViHanhChinh donVi : getList()) {
			if (donVi.getName().toLowerCase().contains(param.toLowerCase()) || donVi.loadSizeChild() > 0)  {
				donViGoc.getNode().add(donVi.getNode());
			}

		}
		if (!param.isEmpty() || !param.equals("")) {
			donViGoc.getNode().getModel().setOpenObjects(donViGoc.getNode().getChildren());
		}
		donViGoc.getNode().getModel().setOpenObjects(donViGoc.getNode().getChildren());
		openObject(model, donViGoc.getNode());
		return model;
	}
}
