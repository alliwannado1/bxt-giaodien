package vn.toancauxanh.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.zul.Window;

import com.querydsl.jpa.impl.JPAQuery;

@Entity
@Table(name = "loaidisan")
public class LoaiDiSan extends Model<LoaiDiSan> {
	private String name;
	private String description;
	private int soThuTu;
	
	public int getSoThuTu() {
		return soThuTu;
	}

	public void setSoThuTu(int soThuTu) {
		this.soThuTu = soThuTu;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public LoaiDiSan() {
		super();
	}
	
	@Command
	public void saveLoaiDiSan(@BindingParam("list") Object list, @BindingParam("wdn") Window wdn
			, @BindingParam("attr") String attr) {
		
		save();
		wdn.detach();
		BindUtils.postNotifyChange(null, null, list, attr);
		
	}
	
	@Transient
	public AbstractValidator getValidatorNameLoaiDiSan() {
		return new AbstractValidator() {
			
			@Override
			public void validate(ValidationContext ctx) {
				String nameTemp = (String) ctx.getProperty().getValue();
				if (nameTemp.isEmpty()) {
					addInvalidMessage(ctx, "Không được để trống trường này");
				}
				else {
					JPAQuery<LoaiDiSan> q = find(LoaiDiSan.class)
							.where(QLoaiDiSan.loaiDiSan.daXoa.isFalse())
							.where(QLoaiDiSan.loaiDiSan.trangThai.ne(core().TT_DA_XOA))
							.where(QLoaiDiSan.loaiDiSan.name.eq(nameTemp))
							;
					if (!LoaiDiSan.this.noId()) {
						q.where(QLoaiDiSan.loaiDiSan.id.ne(getId()));
					}
					
					if (q.fetchCount() > 0) {
						addInvalidMessage(ctx, "Đã tồn tại loại di sản này");
					}
				}
			}
		};
	}
	
}
