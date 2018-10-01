package vn.toancauxanh.gg.model;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import com.google.common.base.Strings;

import vn.toancauxanh.model.Model;

@MappedSuperclass
public class Asset<T extends Asset<T>> extends Model<T> {
	public String trangThaiSoan = "";
	private boolean ckEditorPopup;

	@Override
	// @SuppressWarnings("deprecation")
	public String getTrangThaiSoan() {
		return trangThaiSoan;
	}

	public void setTrangThaiSoan(final String _trangThaiSoan) {
		this.trangThaiSoan = Strings.nullToEmpty(_trangThaiSoan);
	}

	public boolean isCkEditorPopup() {
		return ckEditorPopup;
	}

	@Command
	@NotifyChange("ckEditorPopup")
	public void setCkEditorPopup(@BindingParam("visible") final boolean value) {
		this.ckEditorPopup = value;
	}

	
	@Transient
	public String setStyleNoiBat(boolean strStyle) {
		if (strStyle) {
			return "label label-success width-90px";
		} else {
			return "label label-default width-90px";
		}
	}
	
	
}
