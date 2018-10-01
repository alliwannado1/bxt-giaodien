package vn.toancauxanh.model;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.zul.Filedownload;

@Entity
@Table(name = "teptin")
public class TepTin extends Model<TepTin> {

	private String tenFile;
	private String pathFile;
	private Media media;
	private String typeFile;
	private String nameHash;
	
	public String getNameHash() {
		return nameHash;
	}

	public void setNameHash(String nameHash) {
		this.nameHash = nameHash;
	}

	public String getTypeFile() {
		return typeFile;
	}

	public void setTypeFile(String typeFile) {
		this.typeFile = typeFile;
	}

	public String getTenFile() {
		return tenFile;
	}

	public void setTenFile(String tenFile) {
		this.tenFile = tenFile;
	}

	public String getPathFile() {
		return pathFile;
	}

	public void setPathFile(String pathFile) {
		this.pathFile = pathFile;
	}

	@Transient
	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}
	
	public void saveFileTepTin() throws IOException {
		if (media != null) {
			final File baseDir = new File((folderStoreFilesHome() + getPathFile()).concat(getNameHash()));
			Files.copy(baseDir, media.getStreamData());
			setMedia(null);
			BindUtils.postNotifyChange(null, null, this, "media");
		}
	}

	public void saveTepTin() throws IOException {
		saveFileTepTin();
		saveNotShowNotification();
	}

	@Transient
	public AbstractValidator getValidaTenFile() {
		return new AbstractValidator() {
			@Override
			public void validate(final ValidationContext ctx) {
				String value = (String) ctx.getProperty().getValue();
				if ("".equals(value)) {
					addInvalidMessage(ctx, "error", "Bạn chưa upload video");
					showNotification("Tên tệp tin không để trống", "", "danger");
				}
			}
		};
	}
	
	@Command
	public void downLoadTepTin() throws MalformedURLException {
		if (!this.getPathFile().isEmpty()) {
			final String path = folderRoot() + this.getPathFile();
			if (new java.io.File(path).exists()) {
				try {
					Filedownload.save(new URL("file://" + folderRoot() + this.getPathFile() + this.getNameHash()).openStream(), null,
							this.getTenFile().concat(this.getTypeFile()));
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
