package vn.toancauxanh.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.mp4parser.IsoFile;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.ValidationContext;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.validator.AbstractValidator;
import org.zkoss.image.AImage;
import org.zkoss.image.Image;
import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.VideoListResponse;

import bsh.ParseException;
import vn.toancauxanh.cms.service.ThamSoService;
import vn.toancauxanh.service.ResizeHinhAnh;

@Entity
@Table(name = "video")
public class Video extends Model<Video> {
	private String name;
	private String description;
	private Image avatarVideo;
	private String typeVideo;
	private Media fileVideo;
	private String urlVideo;
	private String path;
	private String pathAvatar;
	private boolean buttonAdd = false;
	private boolean buttonConfim = true;
	private String duration;
	private static final String videotumay = "Video từ máy";
	private static final String videoyoutube = "Video youtube";

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	@Transient
	public boolean isButtonConfim() {
		return buttonConfim;
	}

	public void setButtonConfim(boolean buttonConfim) {
		this.buttonConfim = buttonConfim;
	}

	@Transient
	public boolean isButtonAdd() {
		return buttonAdd;
	}

	public void setButtonAdd(boolean buttonAdd) {
		this.buttonAdd = buttonAdd;
	}

	public String getName() {
		return name;
	}

	public String getPathAvatar() {
		return pathAvatar;
	}

	public void setPathAvatar(String pathAvatar) {
		this.pathAvatar = pathAvatar;
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

	@Transient
	public Image getAvatarVideo() throws FileNotFoundException, IOException {
		if (avatarVideo == null) {
			loadImageIsView();
		}
		return avatarVideo;
	}

	public void setAvatarVideo(Image avatarVideo) {
		this.avatarVideo = avatarVideo;
	}

	@Transient
	public Media getFileVideo() {
		return fileVideo;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setFileVideo(Media fileVideo) {
		this.fileVideo = fileVideo;
	}

	public String getTypeVideo() {
		return typeVideo;
	}

	public void setTypeVideo(String typeVideo) {
		this.typeVideo = typeVideo;
	}

	public String getUrlVideo() {
		return urlVideo;
	}

	public void setUrlVideo(String urlVideo) {
		this.urlVideo = urlVideo;
	}

	@Command
	public void saveVideo() throws IOException, NumberFormatException, ParseException, java.text.ParseException {
		if (videotumay.equals(getTypeVideo())) {
			if (fileVideo != null) {
				saveFileVideo();
			}
		} else {
			saveVideoYoutube();
		}
		if (avatarVideo != null) {
			saveImage();
		}
		saveNotShowNotification();
	}

	@Command
	public void closePopupVideo(@BindingParam("wdn") Window wdn) {
		wdn.detach();
	}

	private boolean visibleButton;
	private boolean visibleTextbox;

	@Transient
	public boolean isVisibleButton() {
		if ("tep-tin".equals(getTypeVideo())) {
			visibleButton = true;
		}
		return visibleButton;
	}

	public void setVisibleButton(boolean visibleButton) {
		this.visibleButton = visibleButton;
	}

	@Transient
	public boolean isVisibleTextbox() {
		if ("link".equals(getTypeVideo())) {
			visibleTextbox = true;
		}
		return visibleTextbox;
	}

	public void setVisibleTextbox(boolean visibleTextbox) {
		this.visibleTextbox = visibleTextbox;
	}

	@Command
	public void chageCheck() {
		if (videotumay.equals(typeVideo)) {
			setVisibleButton(true);
			setVisibleTextbox(false);
			setUrlVideo(null);
		}
		if (videoyoutube.equals(typeVideo)) {
			setVisibleTextbox(true);
			setVisibleButton(false);
			setUrlVideo(null);
			setFileVideo(null);
		}
		BindUtils.postNotifyChange(null, null, this, "visibleTextbox");
		BindUtils.postNotifyChange(null, null, this, "visibleButton");
		BindUtils.postNotifyChange(null, null, this, "typeVideo");
	}

	@Command
	public void uploadVideo(@BindingParam("media") Media media, @BindingParam("folder") String folder) {
		if (media.getName().toLowerCase().endsWith(".mp4")) {
			String tenFile = media.getName();
			tenFile = tenFile.replace(" ", "");
			tenFile = unAccent(tenFile.substring(0, tenFile.lastIndexOf("."))) + "_"
					+ Calendar.getInstance().getTimeInMillis() + tenFile.substring(tenFile.lastIndexOf("."));
			setPath(folderStoreFilesLink() + folder + folderStoreFilesVideo());
			setUrlVideo(tenFile);
			setFileVideo(media);
			BindUtils.postNotifyChange(null, null, this, "urlVideo");
			BindUtils.postNotifyChange(null, null, this, "fileVideo");
			showNotification("Đã tải lên video thành công", "", "success");
		} else {
			showNotification("Tệp tải lên không đúng định dạng", "", "danger");
		}
	}

	@Command
	public void deleteVideo(@BindingParam("vm") Object vm) {
		Messagebox.show("Bạn muốn xóa video này?", "Xác nhận", Messagebox.CANCEL | Messagebox.OK, Messagebox.QUESTION,
				new EventListener<Event>() {
					@Override
					public void onEvent(final Event event) {
						if (Messagebox.ON_OK.equals(event.getName())) {
							setFileVideo(null);
							setUrlVideo(null);
							BindUtils.postNotifyChange(null, null, vm, "urlVideo");
							BindUtils.postNotifyChange(null, null, vm, "fileVideo");
							showNotification("Đã xóa video tải lên", "", "success");
						}
					}
				});
	}

	public void saveFileVideo() throws IOException {
		if (this.getFileVideo() != null) {
			final File baseDir = new File((folderStoreFilesHome() + getPath()).concat(getUrlVideo()));
			Files.copy(baseDir, this.getFileVideo().getStreamData());
			converTime(getDurationVideo());
			setFileVideo(null);
			BindUtils.postNotifyChange(null, null, this, "fileVideo");
		}
	}

	public void saveVideoYoutube() {
		try {
			converTime(getTimeFromUrlYoutube());
		} catch (Exception e) {
		}
	}

	@SuppressWarnings("resource")
	@Transient
	public double getDurationVideo() throws IOException {
		String path = (folderStoreFilesHome() + getPath()).concat(getUrlVideo());
		File file = new File(path);
		double le = 0;
		if (file.exists()) {
			IsoFile isoFile = new IsoFile(file);
			le = isoFile.getMovieBox().getMovieHeaderBox().getDuration()
					/ isoFile.getMovieBox().getMovieHeaderBox().getTimescale();
		}
		return le;
	}

	@Transient
	public void converTime(double time) {
		int timeint = (int) time;
		String phutString = "";
		String giayString = "";
		if (timeint / 60 < 10) {
			phutString = "0" + timeint / 60;
		} else {
			phutString = Integer.toString(timeint / 60);
		}
		if (timeint % 60 < 10) {
			giayString = "0" + timeint % 60;
		} else {
			giayString = Integer.toString(timeint % 60);
		}
		duration = phutString + ":" + giayString;
	}

	@Command
	public void uploadAvatarVideo(@BindingParam("media") Media media, @BindingParam("folder") String folder) {
		if (media instanceof org.zkoss.image.Image) {
			if (media.getName().toLowerCase().endsWith(".jpg") || media.getName().toLowerCase().endsWith(".jpeg")) {
				int length = media.getByteData().length;
				if (length < 50000) {
					showNotification("Kích thước hình ảnh quá nhỏ", "", "danger");
				} else {
					if (length < 10000000) {
						String tenFile = media.getName();
						tenFile = tenFile.replace(" ", "");
						tenFile = unAccent(tenFile.substring(0, tenFile.lastIndexOf("."))) + "_"
								+ Calendar.getInstance().getTimeInMillis()
								+ tenFile.substring(tenFile.lastIndexOf("."));
						setAvatarVideo((Image) media);
						setPath(folderStoreFilesLink() + folder + folderStoreFilesVideo());
						setPathAvatar(tenFile);
						BindUtils.postNotifyChange(null, null, this, "avatarVideo");
						BindUtils.postNotifyChange(null, null, this, "pathAvatar");
					} else {
						showNotification("Kích thước hình ảnh không quá 10 MB", "", "danger");
					}
				}
			} else {
				showNotification("Hình ảnh phải ở dạng jpg hoặc jpeg", "", "danger");
			}
		} else {
			showNotification("File không phải là hình ảnh", "", "danger");
		}
	}

	@Command
	public void deleteImg() {
		setAvatarVideo(null);
		setPathAvatar(null);
		BindUtils.postNotifyChange(null, null, this, "avatarVideo");
		BindUtils.postNotifyChange(null, null, this, "pathAvatar");
	}

	protected void saveImage() throws IOException {
		Image imageContent = getAvatarVideo();
		if (imageContent != null) {
			final File baseDir = new File((folderStoreFilesHome() + getPath()).concat(getPathAvatar()));
			Files.copy(baseDir, imageContent.getStreamData());
			ResizeHinhAnh.saveMediumAndSmallVideo(folderStoreFilesHome() + getPath(), this);
			setAvatarVideo(null);
		}
	}

	@Transient
	public AbstractValidator getValidaLinkYoutube() {
		return new AbstractValidator() {
			@Override
			public void validate(final ValidationContext ctx) {
				String idStringVideo = (String) ctx.getProperty().getValue();
				if (videoyoutube.equals(typeVideo)) {
					if (idStringVideo.isEmpty()) {
						addInvalidMessage(ctx, "error", "Không để trống link");
					} else {
						String expression = "^.*((youtu.be" + "\\/)"
								+ "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*";
						if (!idStringVideo.matches(expression)) {
							addInvalidMessage(ctx, "error", "Bạn phải chọn link youtube");
						} else {
							try {
								String id = getIdYoutubeWithUrl(idStringVideo);
								double timeVideo = getTimeFromUrlYoutubeWithId(id);
								if (timeVideo <= 0) {
									addInvalidMessage(ctx, "error", "Link bạn nhập chưa đúng");
								}
							} catch (Exception e) {
							}
						}
					}
				}
			}
		};
	}

	@Transient
	public AbstractValidator getValidaLocalVideo() {
		return new AbstractValidator() {
			@Override
			public void validate(final ValidationContext ctx) {
				String value = (String) ctx.getProperty().getValue();
				if (!videotumay.equals(typeVideo)) {
					if (value.isEmpty()) {
						addInvalidMessage(ctx, "error", "Bạn chưa upload video");
					}
				}
			}
		};
	}

	@Transient
	public String getIdYoutube() {
		String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\"
				+ "/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%"
				+ "2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";
		Pattern compiledPattern = Pattern.compile(pattern);
		if (getUrlVideo() != null) {
			Matcher matcher = compiledPattern.matcher(getUrlVideo());
			if (matcher.find()) {
				return matcher.group();
			}
		}
		return "";
	}

	@Transient
	public String getIdYoutubeWithUrl(String url) {
		String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\"
				+ "/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%"
				+ "2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";
		Pattern compiledPattern = Pattern.compile(pattern);
		if (url != null) {
			Matcher matcher = compiledPattern.matcher(url);
			if (matcher.find()) {
				return matcher.group();
			}
		}
		return "";
	}

	@Command
	public void showVideo() {
		this.idVideoYoutube = getIdYoutube();
		this.urlVideoYoutube = getUrlYoutube(idVideoYoutube);
		BindUtils.postNotifyChange(null, null, this, "idVideoYoutube");
		BindUtils.postNotifyChange(null, null, this, "urlVideoYoutube");
	}

	@Transient
	public String getUrlYoutube(String id) {
		String http = "https://www.youtube.com/embed/";
		http = http + id;
		return http;
	}

	private String urlVideoYoutube;

	@Transient
	public String getUrlVideoYoutube() {
		return urlVideoYoutube;
	}

	public void setUrlVideoYoutube(String urlVideoYoutube) {
		this.urlVideoYoutube = urlVideoYoutube;
	}

	private String idVideoYoutube;

	@Transient
	public String getIdVideoYoutube() {
		if (urlVideo != null) {
			idVideoYoutube = getIdYoutube();
		}
		return idVideoYoutube;
	}

	public void setIdVideoYoutube(String idVideoYoutube) {
		this.idVideoYoutube = idVideoYoutube;
	}

	@Command
	public void close(@BindingParam("wdn") Window wdn) {
		wdn.detach();
	}

	private List<Video> listVideo;

	@Transient
	public List<Video> getListVideo() {
		return listVideo;
	}

	public void setListVideo(List<Video> listVideo) {
		this.listVideo = listVideo;
	}

	@Transient
	public AbstractValidator getValidaVideo() {
		return new AbstractValidator() {
			@Override
			public void validate(final ValidationContext ctx) {
				String value = (String) ctx.getProperty().getValue();
				if ("".equals(value) || value == null) {
					addInvalidMessage(ctx, "error", "Bạn chưa chọn loại và tải video lên");
				}
			}
		};
	}

	@Transient
	public AbstractValidator getValidaAvatarVideo() {
		return new AbstractValidator() {
			@Override
			public void validate(final ValidationContext ctx) {
				Object value = ctx.getProperty().getValue();
				if (value == null) {
					addInvalidMessage(ctx, "error", "Bạn chưa chọn loại và tải video lên");
				}

			}
		};
	}

	private void loadImageIsView() throws FileNotFoundException, IOException {
		String path = folderStoreFilesHome() + getPath() + getPathAvatar();
		if (new File(path).exists()) {
			String pathCompare = path.substring(0, path.lastIndexOf(File.separator) + 1)
					+ path.substring(path.lastIndexOf(File.separator) + 1);
			String imgName = pathCompare.substring(path.lastIndexOf(File.separator) + 1);
			try (FileInputStream fis = new FileInputStream(new File(path));) {
				setAvatarVideo(new AImage(imgName, fis));
			}
		}
	}

	public String idYoutube() {
		String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\"
				+ "/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%"
				+ "2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";
		Pattern compiledPattern = Pattern.compile(pattern);
		Matcher matcher = compiledPattern.matcher(getUrlVideo());
		if (matcher.find()) {
			return matcher.group();
		}
		return "";
	}

	@Transient
	public String getUrlYoutube() {
		String http = "https://www.youtube.com/embed/";
		http = http + idYoutube();
		return http;
	}

	@Transient
	public AbstractValidator getValidator() {
		return new AbstractValidator() {
			@Override
			public void validate(final ValidationContext ctx) {
				try {
					if (getAvatarVideo() == null) {
						addInvalidMessage(ctx, "error2", "Bạn chưa chọn hình ảnh cho banner.");
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}

	@Transient
	public List<String> getlistTypeVideo() {
		List<String> arr = new ArrayList<String>();
		arr.add(videotumay);
		arr.add(videoyoutube);
		return arr;
	}

	@Transient
	public double getTimeFromUrlYoutube()
			throws IOException, NumberFormatException, ParseException, java.text.ParseException {
		YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(),
				new HttpRequestInitializer() {
					public void initialize(HttpRequest request) throws IOException {
					}
				}).setApplicationName("video-test").build();
		long duration = 0L;
		String videoId = getIdVideoYoutube();
		try {
			YouTube.Videos.List videoRequest = youtube.videos().list("snippet,statistics,contentDetails");
			videoRequest.setId(videoId);
			ThamSoService thamSoKey = new ThamSoService();
			videoRequest.setKey(thamSoKey.getQueryApiKeyGoogle().getValue());
			VideoListResponse listResponse = videoRequest.execute();
			List<com.google.api.services.youtube.model.Video> videoList = listResponse.getItems();
			com.google.api.services.youtube.model.Video targetVideo = videoList.iterator().next();
			String thoiGian = targetVideo.getContentDetails().getDuration();
			String time = thoiGian.substring(2);
			Object[][] indexs = new Object[][] { { "H", 3600 }, { "M", 60 }, { "S", 1 } };
			for (int i = 0; i < indexs.length; i++) {
				int index = time.indexOf((String) indexs[i][0]);
				if (index != -1) {
					String value = time.substring(0, index);
					duration += Integer.parseInt(value) * (int) indexs[i][1];
					time = time.substring(value.length() + 1);
				}
			}

		} catch (Exception e) {
			duration = 0;
		}

		return duration;
	}

	@Transient
	public double getTimeFromUrlYoutubeWithId(String id)
			throws IOException, NumberFormatException, ParseException, java.text.ParseException {
		YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(),
				new HttpRequestInitializer() {
					public void initialize(HttpRequest request) throws IOException {
					}
				}).setApplicationName("video-test").build();
		long duration = 0L;
		try {
			YouTube.Videos.List videoRequest = youtube.videos().list("snippet,statistics,contentDetails");
			videoRequest.setId(id);
			ThamSoService thamSoKey = new ThamSoService();
			videoRequest.setKey(thamSoKey.getQueryApiKeyGoogle().getValue());
			VideoListResponse listResponse = videoRequest.execute();
			List<com.google.api.services.youtube.model.Video> videoList = listResponse.getItems();
			com.google.api.services.youtube.model.Video targetVideo = videoList.iterator().next();
			String thoiGian = targetVideo.getContentDetails().getDuration();
			String time = thoiGian.substring(2);
			Object[][] indexs = new Object[][] { { "H", 3600 }, { "M", 60 }, { "S", 1 } };
			for (int i = 0; i < indexs.length; i++) {
				int index = time.indexOf((String) indexs[i][0]);
				if (index != -1) {
					String value = time.substring(0, index);
					duration += Integer.parseInt(value) * (int) indexs[i][1];
					time = time.substring(value.length() + 1);
				}
			}

		} catch (Exception e) {
			duration = -1;
		}

		return duration;
	}

	@Command
	public void geIdVideoFromLink(@BindingParam("vm") Video video) {
		// String pattern =
		// "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\"
		// + "/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%"
		// +
		// "2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";
		// Pattern compiledPattern = Pattern.compile(pattern);
		// Matcher matcher = compiledPattern.matcher(video.getUrlVideo());
		// if (matcher.find()) {
		// idVideoYoutube = matcher.group();
		// }
	}
}