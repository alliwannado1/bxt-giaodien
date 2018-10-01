package vn.toancauxanh.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.io.FilenameUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.util.SystemPropertyUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.sys.ValidationMessages;
import org.zkoss.image.AImage;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Window;

import vn.toancauxanh.cms.service.ThamSoService;
import vn.toancauxanh.service.SendMailTLS;


@Entity
@Table(name = "gopyphanmem")
public class GopYPhanMem extends Model<GopYPhanMem> {

	private String hoTen;
	private String soDienThoai;
	private String email;
	private String noiDung;
	@Column(columnDefinition = "TEXT")
	private String fileName = "";
	private int sizeImg;
	private List<Image> images = new ArrayList<Image>();
	private List<Image> listImages = new ArrayList<Image>();
	private List<Media> listFiles = new ArrayList<Media>();
	private List<String> fileNameNotImg = new ArrayList<>();
	private List<String> listFileNameSendMail = new ArrayList<>();
	private List<String> listFileNameAttach = new ArrayList<>();
	
	private String style = "";
	private String style_v1 = "";
	
	@Transient
	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
	@Transient
	public String getStyle_v1() {
		return style_v1;
	}

	public void setStyle_v1(String style_v1) {
		this.style_v1 = style_v1;
	}

	@Transient
	public List<Image> getListImages() {
		return listImages;
	}

	public void setListImages(List<Image> listImages) {
		this.listImages = listImages;
	}

	@Transient
	public List<Media> getListFiles() {
		return listFiles;
	}

	public void setListFiles(List<Media> listFiles) {
		this.listFiles = listFiles;
	}

	@Transient
	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	@Transient
	public int getSizeImg() {
		this.sizeImg = getImages().size();
		return sizeImg;
	}

	public String getHoTen() {
		return hoTen;
	}

	public void setHoTen(String hoTen) {
		this.hoTen = hoTen;
	}

	public String getSoDienThoai() {
		return soDienThoai;
	}

	public void setSoDienThoai(String soDienThoai) {
		this.soDienThoai = soDienThoai;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Length(max = 5000)
	public String getNoiDung() {
		return noiDung;
	}

	public void setNoiDung(String noiDung) {
		this.noiDung = noiDung;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean checkSizeFile(int maximum, int sizeFile) {
		if (sizeFile > maximum) {
			return false;
		}
		return true;
	}

	public boolean isFileWord(String tenFile) {
		if ("docx".equals(getFileEx(tenFile)) || "doc".equals(getFileEx(tenFile)) || "xlsx".equals(getFileEx(tenFile))
				|| "xls".equals(getFileEx(tenFile)) || "pdf".equals(getFileEx(tenFile))) {
			return true;
		}
		return false;
	}
	
	@Transient
	public void changeStyle() {
		System.out.println("images : "+ images.size());
		if(images.size() == 2) {
			setStyle("float: left; height: 100px; width: "+(float)100/2+"%; position: relative");
		} else if(images.size() == 1) {
			setStyle("float: left; height: 100px; width: 100%; position: relative");
		} else {
			setStyle("float: left; height: 100px; width: "+(float)100/3+"%; position: relative");			
		}
		setStyle_v1("position: absolute; top: -15px; right: 0px;");
	}

	@Command
	public void attachImages(@BindingParam("media") final Media[] medias, @BindingParam("sizeImgOld") int sizeImgOld,
			@BindingParam("vmsgs") final ValidationMessages vmsgs) {
		if ((getImages().size() + 1) > 3 || medias.length > 3 || (sizeImgOld + medias.length) > 3) {
			showNotification("Chỉ được chọn tối đa 3 file","", "error");
			return;
		}
		int isFileUpload = 0;
		int vt = 0;
		for (Media media : medias) {

			int countByte = media.getByteData().length;
			int maxSizeFile = 10 * 1024 * 1024;

			if (!checkSizeFile(maxSizeFile, countByte)) {
				showNotification("Chỉ được chọn tối đa file có dung lượng 10 Mb","", "error");
				return;
			}

			String tenFile = media.getName();
			if (checkFileExtension(getFileEx(tenFile))) {
				tenFile = tenFile.replace(" ", "");
				tenFile = tenFile.substring(0, tenFile.lastIndexOf(".")) + "_"
						+ System.nanoTime() + tenFile.substring(tenFile.lastIndexOf("."));
				if (vmsgs != null) {
					vmsgs.clearKeyMessages("errLabel");
				}
				Image image = new Image();
				try {
					if ("docx".equals(getFileEx(tenFile)) || "doc".equals(getFileEx(tenFile))) {
						image.setImageContent(
								new AImage(new File(Executions.getCurrent().getDesktop().getWebApp().getRealPath("")
										+ "/backend/assets/images/word.svg")));
						System.out.println(Executions.getCurrent().getDesktop().getWebApp().getRealPath(""));
						image.setName(media.getName());
						listFiles.add(media);
					} else if ("xlsx".equals(getFileEx(tenFile)) || "xls".equals(getFileEx(tenFile))) {
						image.setImageContent(
								new AImage(new File(Executions.getCurrent().getDesktop().getWebApp().getRealPath("")
										+ "/backend/assets/images/excel.svg")));
						System.out.println(Executions.getCurrent().getDesktop().getWebApp().getRealPath(""));
						image.setName(media.getName());
						listFiles.add(media);
					} else if ("pdf".equals(getFileEx(tenFile))) {
						image.setImageContent(
								new AImage(new File(Executions.getCurrent().getDesktop().getWebApp().getRealPath("")
										+ "/backend/assets/images/pdf.svg")));
						System.out.println(Executions.getCurrent().getDesktop().getWebApp().getRealPath(""));
						image.setName(media.getName());
						listFiles.add(media);
					} else {
						image.setImageContent((org.zkoss.image.Image) media);
						image.setName(tenFile);
						listImages.add(image);
					}
					this.getImages().add(image);
					listFileNameAttach.add(String.valueOf(vt));
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			} else {
				isFileUpload++;
			}
			vt++;
		}

		if (isFileUpload > 0) {
			showNotification(
					"Chọn tập tin theo đúng định dạng (*.png, *.jpg, *.jpeg, *.docx, *.doc, *.xlsx, *.xls, *.pdf)",
					SystemPropertyUtils.resolvePlaceholders(PH_DEFNOTIFYPOS), "error");
		} else {
			BindUtils.postNotifyChange(null, null, this, "images");
			BindUtils.postNotifyChange(null, null, this, "listFiles");
			BindUtils.postNotifyChange(null, null, this, "sizeImg");
			BindUtils.postNotifyChange(null, null, this, "listImages");
		}
		changeStyle();
		BindUtils.postNotifyChange(null, null, this, "listFileNameAttach");
		BindUtils.postNotifyChange(null, null, this, "checkIsEmptyNameFile");
	}

	@Command
	public void saveGopYPhanMem(@BindingParam("list") final Object listObject, @BindingParam("attr") final String attr,
			@BindingParam("wdn") final Window wdn, @BindingParam("fileError") String fileNameError) {
		System.out.println("fileError : " + fileNameError);
		
		try {
			listFileNameSendMail = new ArrayList<>();
			listFileNameAttach = new ArrayList<>();
			if (fileNameError != null) {
				this.fileName = fileNameError;
				final String DIR_UPLOAD = "uploads/gopy";
				String dirPath = Executions.getCurrent().getDesktop().getWebApp().getRealPath("") + File.separator
						+ DIR_UPLOAD;
				listFileNameSendMail.add(dirPath + File.separator + fileNameError);
			} else {
				this.fileName = concatFileName();
			}
			saveImages();
			saveFiles();
			
			saveNotShowNotification();
			showNotification("Gửi góp ý thành công !", "", "success");
			wdn.detach();
			
			SendMailTLS.sendEmailGmail(new ThamSoService().getQueryEmailLienHe().getValue(), "Gop y phan mem quan ly VHTTDL Quang Nam", noiDung, listFileNameSendMail);
			
			BindUtils.postNotifyChange(null, null, listObject, attr);
		} catch (IOException e) {
			showNotification("Gửi góp ý không thành công !", "", "error");
		}
	}

	@Transient
	public String getFileEx(String fileName) {
		return FilenameUtils.getExtension(fileName).toLowerCase();
	}

	@Transient
	public List<String> getExtensions() {
		List<String> extensions = new ArrayList<>();
		extensions.add("docx");
		extensions.add("doc");
		extensions.add("jpeg");
		extensions.add("png");
		extensions.add("jpg");
		extensions.add("xlsx");
		extensions.add("xls");
		extensions.add("pdf");
		return extensions;
	}

	public boolean checkIsEmptyNameFile(Image img) {
		if (img.getName().isEmpty()) {
			return true;
		}
		return false;
	}

	public boolean checkFileExtension(String extensionCheck) {
		for (String extension : getExtensions()) {
			if (extensionCheck.equalsIgnoreCase(extension)) {
				return true;
			}
		}
		return false;
	}

	public String concatFileName() {
		StringBuilder sb = new StringBuilder();
		fileNameNotImg = getFileNameWord();
		for (int i = 0; i < listImages.size(); i++) {
			if (i == (listImages.size() - 1)) {
				sb.append(listImages.get(i).getName());
			} else {
				sb.append(listImages.get(i).getName()).append("|");
			}
		}
		if (listImages.size() > 0) {
			for (int i = 0; i < listFiles.size(); i++) {
				if (i == (listFiles.size() - 1)) {
					if (listFiles.size() == 1) {
						sb.append("|").append(fileNameNotImg.get(i));
					} else {
						sb.append(fileNameNotImg.get(i));
					}

				} else {
					sb.append("|").append(fileNameNotImg.get(i)).append("|");
				}
			}
		} else {
			for (int i = 0; i < listFiles.size(); i++) {
				if (i == (listFiles.size() - 1)) {
					sb.append(fileNameNotImg.get(i));
				} else {
					sb.append(fileNameNotImg.get(i)).append("|");
				}
			}
		}
		return sb.toString();
	}

	@Transient
	public List<String> getShowListImgs() {
		List<String> imgs = new ArrayList<>();
		String dirPathUpload = "/uploads/gopy/";
		for (String img : fileName.split("\\|")) {
			if (!"".equals(img)) {
				img = dirPathUpload + img;
				imgs.add(img);
			}
		}
		return imgs;
	}

	@Transient
	public List<String> getFileNameWord() {
		List<String> fileNameWords = new ArrayList<>();
		for (Media word : getListFiles()) {
			String fileName = "isFileTCX-" + System.nanoTime() + "." + getFileEx(word.getName());
			fileNameWords.add(fileName);
		}
		this.fileNameNotImg = fileNameWords;
		BindUtils.postNotifyChange(null, null, this, "fileNameNotImg");
		return fileNameWords;
	}

	@Transient
	public boolean isFile(String fileName) {
		String firstFileName = fileName.split("\\_")[0];
		if ("".equals(firstFileName)) {
			return true;
		}
		return false;
	}
	
	@Transient
	public String srcImageFile(String fileName) {
		String src = "";
		if ("docx".equals(getFileEx(fileName)) || "doc".equals(getFileEx(fileName)))
			src = "/backend/assets/images/word.svg";
		else if ("xlsx".equals(getFileEx(fileName)) || "xls".equals(getFileEx(fileName)))
			src = "/backend/assets/images/excel.svg";
		else if ("pdf".equals(getFileEx(fileName)))
			src = "/backend/assets/images/pdf.svg";
		return src;
	}

	@Command
	public void delImg(@BindingParam("each") final Object ob, @BindingParam("attr") final String attr,
			@BindingParam("indexFile") int indexFile) {
		Image obj = (Image) ob;
		getImages().remove(obj);
		getListImages().remove(obj);
		listFileNameAttach.remove(String.valueOf(indexFile));
		for(int i=0; i<getListFiles().size(); i++) {
			if(getListFiles().get(i).getName().equals(obj.getName())) {
				getListFiles().remove(i);
			}
		}
		
		changeStyle();
		BindUtils.postNotifyChange(null, null, this, attr);
		BindUtils.postNotifyChange(null, null, this, "sizeImg");
		BindUtils.postNotifyChange(null, null, this, "listFiles");
		BindUtils.postNotifyChange(null, null, this, "images");
		BindUtils.postNotifyChange(null, null, this, "listImages");
		BindUtils.postNotifyChange(null, null, this, "listFileNameAttach");
		BindUtils.postNotifyChange(null, null, this, "style");
		BindUtils.postNotifyChange(null, null, this, "style_v1");
	}

	protected void saveImages() throws IOException {
		for (Image img : getListImages()) {
			if (img != null) {
				if (!isFile(img.getName())) {
					org.zkoss.image.Image imageContent = img.getImageContent();
					if (imageContent != null) {
						// luu hinh
						img.setImageUrl(folderImageUrl().concat(img.getName()));
//						img.setMedium(folderImageUrl().concat("m_" + img.getName()));
//						img.setSmall(folderImageUrl().concat("s_" + img.getName()));
						img.setExtension(
								img.getName().substring(img.getName().indexOf(".", img.getName().lastIndexOf(".")) + 1)
										.toUpperCase());
						final String DIR_UPLOAD = "uploads/gopy";
						String dirPath = Executions.getCurrent().getDesktop().getWebApp().getRealPath("")
								+ File.separator + DIR_UPLOAD;
						File createDirPath = new File(dirPath);
						if (!createDirPath.exists()) {
							createDirPath.mkdirs();
						}
						System.out.println(dirPath);
						File filePath = new File(dirPath + File.separator + img.getName());
						Files.copy(filePath, imageContent.getStreamData());
						listFileNameSendMail.add(dirPath + File.separator + img.getName());
					}
				}
			}
		}
	}

	protected void saveFiles() throws IOException {
		for (int i = 0; i < getListFiles().size(); i++) {
			final String DIR_UPLOAD = "uploads/gopy";
			String dirPath = Executions.getCurrent().getDesktop().getWebApp().getRealPath("") + File.separator
					+ DIR_UPLOAD;
			File createDirPath = new File(dirPath);
			if (!createDirPath.exists()) {
				createDirPath.mkdirs();
			}
			File filePath = new File(dirPath + File.separator + fileNameNotImg.get(i));
			Files.copy(filePath, getListFiles().get(i).getStreamData());
			listFileNameSendMail.add(dirPath + File.separator + fileNameNotImg.get(i));
			System.out.println(dirPath);
		}
	}

	@Transient
	public String folderImageUrl() {
		return "/" + Labels.getLabel("filestore.folder") + "/image/";
	}

	@Command
	public void downloadFile(@BindingParam("filePath") String filePath) {
		try {
			Filedownload.save(filePath, null);
		} catch (FileNotFoundException e) {
			showNotification("Không thể tải file!", "", "error");
		}
	}
	
	@Transient
	public List<String> getListEmail() {
		List<String> listEmail = new ArrayList<>();
		/*for(Emails email : new EmailService().getListEmail()) {
			listEmail.add(email.getEmail());
		}*/
		return listEmail;
	}

	@Command
	public void close(@BindingParam("detach") Window detach, @BindingParam("fileName") String fileName) {
		if (fileName != null) {
			final String DIR_UPLOAD = "uploads/gopy";
			String filePath = Executions.getCurrent().getDesktop().getWebApp().getRealPath("") + File.separator
					+ DIR_UPLOAD + File.separator + fileName;
			System.out.println(filePath);
			try {
				File file = new File(filePath);
				file.delete();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		Clients.confirmClose(null);
		detach.detach();
	}
}
