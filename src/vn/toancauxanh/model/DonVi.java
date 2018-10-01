package vn.toancauxanh.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zul.Window;

@Entity
@Table(name = "donvi")
public class DonVi extends Model<DonVi> {
	private String name;
	private String capTinh="49";
	private String capHuyen;
	private String capXa;
	private String capDonVi;
	private DonViHanhChinh donViCapTinh;
	private DonViHanhChinh donViCapHuyen;
	private DonViHanhChinh donViCapXa;
	private List<DonViHanhChinh> listDonViCapTinh;
	private List<DonViHanhChinh> listDonViCapHuyen;
	private List<DonViHanhChinh> listDonViCapXa;
	
	public String getCapTinh() {
		return capTinh;
	}

	public void setCapTinh(String capTinh) {
		this.capTinh = capTinh;
	}

	public String getCapHuyen() {
		return capHuyen;
	}

	public void setCapHuyen(String capHuyen) {
		this.capHuyen = capHuyen;
	}

	public String getCapXa() {
		return capXa;
	}

	public void setCapXa(String capXa) {
		this.capXa = capXa;
	}

	@Transient
	public DonViHanhChinh getDonViCapTinh() {
		return donViCapTinh;
	}

	public void setDonViCapTinh(DonViHanhChinh donViCapTinh) {
		this.donViCapTinh = donViCapTinh;
	}

	@Transient
	public DonViHanhChinh getDonViCapHuyen() {
		return donViCapHuyen;
	}

	public void setDonViCapHuyen(DonViHanhChinh donViCapHuyen) {
		this.donViCapHuyen = donViCapHuyen;
	}

	@Transient
	public DonViHanhChinh getDonViCapXa() {
		return donViCapXa;
	}

	public void setDonViCapXa(DonViHanhChinh donViCapXa) {
		this.donViCapXa = donViCapXa;
	}

	@Transient
	public List<DonViHanhChinh> getListDonViCapTinh() {
		return listDonViCapTinh;
	}

	public void setListDonViCapTinh(List<DonViHanhChinh> listDonViCapTinh) {
		this.listDonViCapTinh = listDonViCapTinh;
	}

	@Transient
	public List<DonViHanhChinh> getListDonViCapHuyen() {
		return listDonViCapHuyen;
	}

	public void setListDonViCapHuyen(List<DonViHanhChinh> listDonViCapHuyen) {
		this.listDonViCapHuyen = listDonViCapHuyen;
	}

	@Transient
	public List<DonViHanhChinh> getListDonViCapXa() {
		return listDonViCapXa;
	}

	public void setListDonViCapXa(List<DonViHanhChinh> listDonViCapXa) {
		this.listDonViCapXa = listDonViCapXa;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Transient
	public String getCapDonVi() {
		return capDonVi;
	}

	public void setCapDonVi(String capDonVi) {
		this.capDonVi = capDonVi;
	}

	@Command
	public void saveDonVi(@BindingParam("wdn") Window wdn, @BindingParam("list") Object object,
			@BindingParam("attr") String query) {
		save();
		wdn.detach();
		BindUtils.postNotifyChange(null, null, object, query);
	}
	
	
}
