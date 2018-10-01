package vn.toancauxanh.cms.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.gg.model.QThamSo;
//import vn.toancauxanh.gg.model.QThamSo;
import vn.toancauxanh.gg.model.ThamSo;
import vn.toancauxanh.gg.model.enums.ThamSoEnum;
import vn.toancauxanh.model.ThamSoConfig;
import vn.toancauxanh.service.BasicService;

public class ThamSoService extends BasicService<ThamSo> {
	@Init
	public void init() {
		emailLienHe = getQueryEmailLienHe().getValue();
		phoneLienHe = getQueryPhoneLienHe().getValue();
		donViHoTroKyThuat = getQueryDonViHoTroKyThuat().getValue();
		donViHoTro = getQueryDonViHoTro().getValue();
		phoneHoTro = getQueryPhoneHoTro().getValue();
		taiLieuHoTro = getQueryTaiLieuHoTro().getValue();
		taiLieuHoTroFrontEnd = getQueryTaiLieuHoTroFrontEnd().getValue();
		apiKeyGoogle = getQueryApiKeyGoogle().getValue();
		
	}
	private String emailLienHe;
	private String phoneLienHe;
	private String donViHoTroKyThuat;
	private String donViHoTro;
	private String phoneHoTro;
	private String taiLieuHoTro = "";
	private String taiLieuHoTroFrontEnd = "";
	
	public String getTaiLieuHoTroFrontEnd() {
		return taiLieuHoTroFrontEnd;
	}

	public void setTaiLieuHoTroFrontEnd(String taiLieuHoTroFrontEnd) {
		this.taiLieuHoTroFrontEnd = taiLieuHoTroFrontEnd;
	}
	private Media media;
	private String apiKeyGoogle = "";

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

	public String getTaiLieuHoTro() {
		return taiLieuHoTro;
	}

	public String getApiKeyGoogle() {
		return apiKeyGoogle;
	}

	public void setApiKeyGoogle(String apiKeyGoogle) {
		this.apiKeyGoogle = apiKeyGoogle;
	}
	
	public void setTaiLieuHoTro(String taiLieuHoTro) {
		this.taiLieuHoTro = taiLieuHoTro;
	}

	public String getPhoneHoTro() {
		return phoneHoTro;
	}

	public void setPhoneHoTro(String phoneHoTro) {
		this.phoneHoTro = phoneHoTro;
	}

	public String getDonViHoTroKyThuat() {
		return donViHoTroKyThuat;
	}

	public String getDonViHoTro() {
		return donViHoTro;
	}

	public void setDonViHoTro(String donViHoTro) {
		this.donViHoTro = donViHoTro;
	}

	public void setDonViHoTroKyThuat(String donViHoTroKyThuat) {
		this.donViHoTroKyThuat = donViHoTroKyThuat;
	}

	public String getPhoneLienHe() {
		return phoneLienHe;
	}

	public void setPhoneLienHe(String phoneLienHe) {
		this.phoneLienHe = phoneLienHe;
	}

	public String getEmailLienHe() {
		return emailLienHe;
	}

	public void setEmailLienHe(String emailLienHe) {
		this.emailLienHe = emailLienHe;
	}

	public JPAQuery<ThamSo> getThamSoByKey(ThamSoEnum key) {
		JPAQuery<ThamSo> q = find(ThamSo.class)
				.where(QThamSo.thamSo.trangThai.ne(core().TT_DA_XOA).and(QThamSo.thamSo.ma.eq(key)));
		return q.orderBy(QThamSo.thamSo.ngaySua.desc());
	}

	@Command
	public void saveThamSo() throws IOException {
		ThamSo email = getQueryEmailLienHe();
		email.setValue(emailLienHe);
		email.saveNotShowNotification();

		ThamSo phone = getQueryPhoneLienHe();
		phone.setValue(phoneLienHe);
		phone.saveNotShowNotification();

		ThamSo donViHoTroKT = getQueryDonViHoTroKyThuat();
		donViHoTroKT.setValue(donViHoTroKyThuat);
		donViHoTroKT.saveNotShowNotification();

		ThamSo donViHT = getQueryPhoneHoTro();
		donViHT.setValue(phoneHoTro);
		donViHT.saveNotShowNotification();

		ThamSo phoneHT = getQueryDonViHoTro();
		phoneHT.setValue(donViHoTro);
		phoneHT.saveNotShowNotification();

		ThamSo taiLieu = getQueryTaiLieuHoTro();
		taiLieu.setValue(taiLieuHoTro);
		taiLieu.saveNotShowNotification();
		
		ThamSo taiLieuFE = getQueryTaiLieuHoTroFrontEnd();
		taiLieuFE.setValue(taiLieuHoTroFrontEnd);
		taiLieuFE.saveNotShowNotification();
		
		ThamSo api = getQueryApiKeyGoogle();
		api.setValue(apiKeyGoogle);
		api.saveNotShowNotification();

		showNotification("Đã lưu thành công!", "", "success");
	}

	@Command
	public void uploadTaiLieuHoTro(@BindingParam("media") Media media, @BindingParam("vm") Object vm)
			throws IOException {
		if (media.getName().toLowerCase().endsWith(".pdf") || media.getName().toLowerCase().endsWith(".doc")
				|| media.getName().toLowerCase().endsWith(".docx")) {
			if (media.getByteData().length <= 50000000) {
				setTaiLieuHoTro(media.getName());
				setMedia(media);
				final File baseDir = new File(folderStore().concat(getTaiLieuHoTro()));
				Files.copy(baseDir, getMedia().getStreamData());
				BindUtils.postNotifyChange(null, null, vm, "taiLieuHoTro");
				showNotification("Lưu file thành công", "", "success");
			} else {
				showNotification("Tệp tin quá 50 MB", "Tệp tin quá lớn", "error");
			}

		} else {
			showNotification("Chỉ chấp nhận các tệp nằm trong các định dạng sau : pdf, doc, docx",
					"Có tệp không đúng định dạng", "danger");
		}

	}

	@Command
	public void deleteTaiLieuHoTro(@BindingParam("vm") Object vm) {
		Messagebox.show("Bạn muốn xóa hình ảnh này không?", "Xác nhận", Messagebox.CANCEL | Messagebox.OK,
				Messagebox.QUESTION, new EventListener<Event>() {
					@Override
					public void onEvent(final Event event) throws IOException {
						if (Messagebox.ON_OK.equals(event.getName())) {
							setTaiLieuHoTro("");
							BindUtils.postNotifyChange(null, null, vm, "taiLieuHoTro");
						}
					}
				});
	}
	
	
	@Command
	public void uploadTaiLieuHoTroFE(@BindingParam("media") Media media, @BindingParam("vm") Object vm)
			throws IOException {
		if (media.getName().toLowerCase().endsWith(".pdf") || media.getName().toLowerCase().endsWith(".doc")
				|| media.getName().toLowerCase().endsWith(".docx")) {
			if (media.getByteData().length <= 50000000) {
				setTaiLieuHoTroFrontEnd(media.getName());
				setMedia(media);
				final File baseDir = new File(folderStore().concat(getTaiLieuHoTroFrontEnd()));
				Files.copy(baseDir, getMedia().getStreamData());
				BindUtils.postNotifyChange(null, null, vm, "taiLieuHoTroFrontEnd");
				showNotification("Lưu file thành công", "", "success");
			} else {
				showNotification("Tệp tin quá 50 MB", "Tệp tin quá lớn", "error");
			}

		} else {
			showNotification("Chỉ chấp nhận các tệp nằm trong các định dạng sau : pdf, doc, docx",
					"Có tệp không đúng định dạng", "danger");
		}

	}

	@Command
	public void deleteTaiLieuHoTroFE(@BindingParam("vm") Object vm) {
		Messagebox.show("Bạn muốn xóa hình ảnh này không?", "Xác nhận", Messagebox.CANCEL | Messagebox.OK,
				Messagebox.QUESTION, new EventListener<Event>() {
					@Override
					public void onEvent(final Event event) throws IOException {
						if (Messagebox.ON_OK.equals(event.getName())) {
							setTaiLieuHoTroFrontEnd("");
							BindUtils.postNotifyChange(null, null, vm, "taiLieuHoTroFrontEnd");
						}
					}
				});
	}

	public ThamSo getQueryEmailLienHe() {
		ThamSo thamSo;
		JPAQuery<ThamSo> q = find(ThamSo.class).where(QThamSo.thamSo.ma.eq(ThamSoEnum.EMAIL_LIENHE));
		thamSo = q.fetchOne();
		if (thamSo == null) {
			thamSo = new ThamSo();
			thamSo.setMa(ThamSoEnum.EMAIL_LIENHE);
			thamSo.setValue(ThamSoConfig.EMAIL_LIENHE);
			thamSo.saveNotShowNotification();
		}
		return thamSo;
	}

	public ThamSo getQueryPhoneLienHe() {
		ThamSo thamSo;
		JPAQuery<ThamSo> q = find(ThamSo.class).where(QThamSo.thamSo.ma.eq(ThamSoEnum.PHONE_LIENHE));
		thamSo = q.fetchOne();
		if (thamSo == null) {
			thamSo = new ThamSo();
			thamSo.setMa(ThamSoEnum.PHONE_LIENHE);
			thamSo.setValue(ThamSoConfig.PHONE_LIENHE);
			thamSo.saveNotShowNotification();
		}
		return thamSo;
	}

	public ThamSo getQueryPhoneHoTro() {
		ThamSo thamSo;
		JPAQuery<ThamSo> q = find(ThamSo.class).where(QThamSo.thamSo.ma.eq(ThamSoEnum.PHONE_HOTRO));
		thamSo = q.fetchOne();
		if (thamSo == null) {
			thamSo = new ThamSo();
			thamSo.setMa(ThamSoEnum.PHONE_HOTRO);
			thamSo.setValue(ThamSoConfig.PHONE_HOTRO);
			thamSo.saveNotShowNotification();
		}
		return thamSo;
	}

	public ThamSo getQueryDonViHoTroKyThuat() {
		ThamSo thamSo;
		JPAQuery<ThamSo> q = find(ThamSo.class).where(QThamSo.thamSo.ma.eq(ThamSoEnum.DONVI_HOTRO_KYTHUAT));
		thamSo = q.fetchOne();
		if (thamSo == null) {
			thamSo = new ThamSo();
			thamSo.setMa(ThamSoEnum.DONVI_HOTRO_KYTHUAT);
			thamSo.setValue(ThamSoConfig.DONVI_HOTRO_KYTHUAT);
			thamSo.saveNotShowNotification();
		}
		return thamSo;
	}

	public ThamSo getQueryDonViHoTro() {
		ThamSo thamSo;
		JPAQuery<ThamSo> q = find(ThamSo.class).where(QThamSo.thamSo.ma.eq(ThamSoEnum.DONVI_HOTRO));
		thamSo = q.fetchOne();
		if (thamSo == null) {
			thamSo = new ThamSo();
			thamSo.setMa(ThamSoEnum.DONVI_HOTRO);
			thamSo.setValue(ThamSoConfig.DONVI_HOTRO);
			thamSo.saveNotShowNotification();
		}
		return thamSo;
	}

	public ThamSo getQueryTaiLieuHoTro() {
		ThamSo thamSo;
		JPAQuery<ThamSo> q = find(ThamSo.class).where(QThamSo.thamSo.ma.eq(ThamSoEnum.TAILIEU_HOTRO));
		thamSo = q.fetchOne();
		if (thamSo == null) {
			thamSo = new ThamSo();
			thamSo.setMa(ThamSoEnum.TAILIEU_HOTRO);
			thamSo.saveNotShowNotification();
		}
		return thamSo;
	}
	
	public ThamSo getQueryTaiLieuHoTroFrontEnd() {
		ThamSo thamSo;
		JPAQuery<ThamSo> q = find(ThamSo.class).where(QThamSo.thamSo.ma.eq(ThamSoEnum.TAILIEU_HOTRO_FE));
		thamSo = q.fetchOne();
		if (thamSo == null) {
			thamSo = new ThamSo();
			thamSo.setMa(ThamSoEnum.TAILIEU_HOTRO_FE);
			thamSo.saveNotShowNotification();
		}
		return thamSo;
	}
	
	public ThamSo getQueryApiKeyGoogle() {
		ThamSo thamSo;
		JPAQuery<ThamSo> q = find(ThamSo.class).where(QThamSo.thamSo.ma.eq(ThamSoEnum.API_KEY));
		thamSo = q.fetchOne();
		if (thamSo == null) {
			thamSo = new ThamSo();
			thamSo.setMa(ThamSoEnum.API_KEY);
			thamSo.saveNotShowNotification();
		}
		return thamSo;
	}
	
	public String getStringApiKeyGoogle() {
		ThamSo thamSo;
		JPAQuery<ThamSo> q = find(ThamSo.class).where(QThamSo.thamSo.ma.eq(ThamSoEnum.API_KEY));
		thamSo = q.fetchOne();
		if (thamSo == null) {
			thamSo = new ThamSo();
			thamSo.setMa(ThamSoEnum.API_KEY);
			thamSo.saveNotShowNotification();
		}
		return thamSo.getValue();
	}
	
	@Command
	public void downloadTepDinhKem() {
		if (!taiLieuHoTro.isEmpty()) {
			final String path = folderStore() + this.getTaiLieuHoTro() ;
			if (new java.io.File(path).exists()) {
				try {
					Filedownload.save(new URL("file://" + path).openStream(), null,
							taiLieuHoTro);
				} catch (IOException e) {
					showNotification("Không tìm thấy file", "Thông báo", "danger");
				}
			}
			else {
				showNotification("Không tìm thấy file", "Thông báo", "danger");
			}
		}
	}
	
	@Command
	public void downloadTepDinhKemFE() {
		if (!taiLieuHoTroFrontEnd.isEmpty()) {
			final String path = folderStore() + this.getTaiLieuHoTroFrontEnd() ;
			if (new java.io.File(path).exists()) {
				try {
					Filedownload.save(new URL("file://" + path).openStream(), null,
							taiLieuHoTroFrontEnd);
				} catch (IOException e) {
					showNotification("Không tìm thấy file", "Thông báo", "danger");
				}
			}
			else {
				showNotification("Không tìm thấy file", "Thông báo", "danger");
			}
		}
	}

}
