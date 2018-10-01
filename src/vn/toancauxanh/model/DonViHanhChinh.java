package vn.toancauxanh.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.collections.MapUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Default;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.DefaultTreeNode;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.TreeNode;
import org.zkoss.zul.Window;

import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.service.DonViHanhChinhService;

@Entity
@Table(name = "donvihanhchinh")
public class DonViHanhChinh extends Model<DonViHanhChinh> {
	private String name;
	private DonViHanhChinh donViHanhChinhCha;
	private String capDonVi;
	
	@Transient
	public String getCapDonVi() {
		return capDonVi;
	}

	public void setCapDonVi(String capDonVi) {
		this.capDonVi = capDonVi;
	}

	private transient final TreeNode<DonViHanhChinh> node = new DefaultTreeNode<DonViHanhChinh>(this,
			new ArrayList<>());

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne
	public DonViHanhChinh getDonViHanhChinhCha() {
		return donViHanhChinhCha;
	}

	public void setDonViHanhChinhCha(DonViHanhChinh donViHanhChinhCha) {
		this.donViHanhChinhCha = donViHanhChinhCha;
	}

	@Transient
	public TreeNode<DonViHanhChinh> getNode() {
		return node;
	}

	public void loadChildren() {
		for (final DonViHanhChinh donViCon : find(DonViHanhChinh.class)
				.where(QDonViHanhChinh.donViHanhChinh.donViHanhChinhCha.eq(this))
				.where(QDonViHanhChinh.donViHanhChinh.trangThai.ne(core().TT_DA_XOA))
				.orderBy(QDonViHanhChinh.donViHanhChinh.ngaySua.desc()).fetch()) {
			donViCon.loadChildren();
			node.add(donViCon.getNode());
		}
	}

	public void loadChildren(String param, String trangThai) {
		for (final DonViHanhChinh con : find(DonViHanhChinh.class)
				.where(QDonViHanhChinh.donViHanhChinh.donViHanhChinhCha.eq(this))
				.where(QDonViHanhChinh.donViHanhChinh.trangThai.ne(core().TT_DA_XOA))
				.orderBy(QDonViHanhChinh.donViHanhChinh.ngaySua.desc()).fetch()) {
			if (con.getName().toLowerCase().contains(param.toLowerCase())) {
				con.loadChildren();
				node.add(con.getNode());
			} else {
				con.loadChildren(param, trangThai);
				if (con.loadSizeChild() > 0) {
					node.add(con.getNode());
				}
			}
		}
	}

	public int loadSizeChild() {
		int size = core().getDonViHanhChinhs().getDonViHanhChinhChildren(this).size();
		return size;
	}

	@Transient
	public String getTypeTrangThai() {
		if ("ap_dung".equals(getTrangThai())) {
			return "Áp dụng";
		} else {
			return "";
		}
	}

	private boolean themDonViCapHuyen;
	private boolean themDonViCapXa;

	@Transient
	public boolean isThemDonViCapHuyen() {
		return themDonViCapHuyen;
	}

	public void setThemDonViCapHuyen(boolean themDonViCapHuyen) {
		this.themDonViCapHuyen = themDonViCapHuyen;
	}

	@Transient
	public boolean isThemDonViCapXa() {
		return themDonViCapXa;
	}

	public void setThemDonViCapXa(boolean themDonViCapXa) {
		this.themDonViCapXa = themDonViCapXa;
	}

	@Command
	public void selectDonViMoi() {
		String capDonVi = MapUtils.getString(argDeco(), Labels.getLabel("param.capdonvi"), "").trim();
		if ("tp-huyen".equals(capDonVi)) {
			setThemDonViCapHuyen(true);
			setThemDonViCapXa(false);
		}
		if ("xa-phuong".equals(capDonVi)) {
			setThemDonViCapHuyen(false);
			setThemDonViCapXa(true);
		}
		BindUtils.postNotifyChange(null, null, this, "themDonViCapHuyen");
		BindUtils.postNotifyChange(null, null, this, "themDonViCapXa");
	}

	@Transient
	public AbstractValidator getValidatorNameDonViHanhChinhCapHuyen() {
		return new AbstractValidator() {
			@Override
			public void validate(ValidationContext ctx) {
				String nameTpHuyen = (String) ctx.getProperty().getValue();
				if (themDonViCapHuyen) {
					if (nameTpHuyen.isEmpty()) {
						addInvalidMessage(ctx, "Không được để trống trường này");
					}
				}
			}
		};
	}

	@Transient
	public AbstractValidator getValidatorNameDonViHanhChinhCapXa() {
		return new AbstractValidator() {
			@Override
			public void validate(ValidationContext ctx) {
				String nameXaPhuong = (String) ctx.getProperty().getValue();
				if (themDonViCapXa) {
					if (nameXaPhuong.isEmpty()) {
						addInvalidMessage(ctx, "Không được để trống trường này");
					}
				}
			}
		};
	}

	@Command
	public void saveDonViHanhChinh(@BindingParam("list") Object args, @BindingParam("wdn") Window wdn) {
		if (this.getDonViHanhChinhCha() == null) {
			DonViHanhChinhService dvhcsv = new DonViHanhChinhService();
			this.setDonViHanhChinhCha(dvhcsv.getDonViCapTinh().fetchOne());
		}
		save();
		BindUtils.postNotifyChange(null, null, args, "model");
		wdn.detach();
	}

	@Command
	public void deleteDonViHanhChinh(final @BindingParam("notify") DonViHanhChinhService donViHanhChinhService,
			@BindingParam("item") DonViHanhChinh donViHanhChinh,
			final @BindingParam("attr") @Default(value = "*") String attr) {
		if (donViHanhChinh.getDonViHanhChinhCha() == null) {
			Messagebox.show("Các phường, xã trực thuộc sẽ bị xóa?", "Xác nhận", Messagebox.CANCEL | Messagebox.OK,
					Messagebox.QUESTION, new EventListener<Event>() {
						@Override
						public void onEvent(final Event event) {
							if (Messagebox.ON_OK.equals(event.getName())) {
								List<DonViHanhChinh> arr = new ArrayList<>();
								arr.add(donViHanhChinh);
								JPAQuery<DonViHanhChinh> q = find(DonViHanhChinh.class)
										.where(QDonViHanhChinh.donViHanhChinh.donViHanhChinhCha.eq(donViHanhChinh));
								arr.addAll(q.fetch());
								for (DonViHanhChinh dvhc : arr) {
									dvhc.doDelete(true);
								}
								if (donViHanhChinhService != null) {
									BindUtils.postNotifyChange(null, null, donViHanhChinhService, attr);
								}
							}
						}
					});
		} else {
			Messagebox.show("Bạn muốn xóa mục này?", "Xác nhận", Messagebox.CANCEL | Messagebox.OK, Messagebox.QUESTION,
					new EventListener<Event>() {
						@Override
						public void onEvent(final Event event) {
							if (Messagebox.ON_OK.equals(event.getName())) {
								doDelete(true);
								showNotification("Xóa thành công!", "", "success");
								if (donViHanhChinhService != null) {
									BindUtils.postNotifyChange(null, null, donViHanhChinhService, attr);
								}
							}
						}
					});
		}
	}

	@Transient
	public List<DonViHanhChinh> getListHuyen() {
		List<DonViHanhChinh> arr = new ArrayList<>();
		DonViHanhChinhService dvhcsv = new DonViHanhChinhService();
		arr = dvhcsv.getDonViCapHuyen().fetch();
		return arr;
	}
	
	@Transient
	public List<DonViHanhChinh> getListTinh() {
		List<DonViHanhChinh> arr = new ArrayList<>();
		DonViHanhChinhService dvhcsv = new DonViHanhChinhService();
		arr = dvhcsv.getDonViCapTinh().fetch();
		return arr;
	}
}
