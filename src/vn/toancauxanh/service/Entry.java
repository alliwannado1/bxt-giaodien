
package vn.toancauxanh.service;

import java.io.File;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Object;
import vn.toancauxanh.cms.service.HomeService;
import vn.toancauxanh.cms.service.LanguageService;
import vn.toancauxanh.cms.service.ThamSoService;
import vn.toancauxanh.model.VaiTro;

@SuppressWarnings({ "deprecation", "unused" })
@Configuration
@Controller
public class Entry extends BaseObject<Object> {
	static Entry instance;

	@Value("${trangthai.apdung}")
	public String TT_AP_DUNG = "";
	@Value("${trangthai.daxoa}")
	public String TT_DA_XOA = "";
	@Value("${trangthai.khongapdung}")
	public String TT_KHONG_AP_DUNG = "";

	@Value("${filestore.root}")
	public String FOLDER_ROOT = "";
	@Value("${filestore.files}")
	public String FOLDER_FILES = "";
	@Value("${filestore.folder}")
	public String FOLDER_FILEFOLDER = "";

	// No image url
	public String URL_M_NOIMAGE = "/assetsfe/images/lg_noimage.png";
	public String URL_S_NOIMAGE = "/assetsfe/images/sm_noimage.png";

	@Value("${action.xem}")
	public String XEM = ""; // duoc xem bat ky
	@Value("${action.list}")
	public String LIST = ""; // duoc vao trang list search url
	@Value("${action.sua}")
	public String SUA = ""; // duoc sua bat ky
	@Value("${action.xoa}")
	public String XOA = ""; // duoc xoa bat ky
	@Value("${action.them}")
	public String THEM = ""; // duoc them
	@Value("${action.xuatban}")
	public String XUATBAN = ""; // duoc duyet va xuat ban
	@Value("${action.lichsucapnhat}")
	public String LICHSUCAPNHAT = ""; // duoc them
	@Value("${action.lichsu}")
	public String LICHSU = "";
	@Value("${action.tim}")
	public String TIMKIEM = "";
	@Value("${action.thongke}")
	public String THONGKE = "";

	@Value("${url.quanlyduan}")
	public String QUANLYDUAN = "";
	@Value("${url.nguoidung}")
	public String NGUOIDUNG = "";
	@Value("${url.phanquyen}")
	public String PHANQUYEN = "";
	@Value("${url.donvihanhchinh}")
	public String donvihanhchinh = "";
	@Value("${url.thuctrangditich}")
	public String THUCTRANGDITICH = "";
	@Value("${url.loaiditich}")
	public String LOAIDITICH = "";
	@Value("${url.loaidisan}")
	public String LOAIDISAN = "";
	@Value("${url.loailehoi}")
	public String LOAILEHOI = "";

	@Value("${url.thongtinlehoi}")
	public String THONGTINLEHOI = "";
	
	@Value("${url.thongtindisan}")
	public String THONGTINDISAN = "";
	@Value("${url.thongtinditich}")
	public String THONGTINDITICH = "";

	@Value("${url.thongkeditich}")
	public String THONGKEDITICH = "";
	@Value("${url.thongkebieumaubaocao}")
	public String THONGKEBIEUMAUBAOCAO = "";

	@Value("${url.donvi}")
	public String DONVI = "";
	
	@Value("${url.donvihanhchinh}")
	public String DONVIHANHCHINH = "";
	
	@Value("${url.ditich}")
	public String DITICH = "";
	
	@Value("${url.lehoi}")
	public String LEHOI = "";
	
	@Value("${url.dsvhpvt}")
	public String DISANVANHOA = "";
	
	@Value("${url.vaitro}")
	public String VAITRO = "";
	
	// uend
	public char CHAR_CACH = ':';
	public String CACH = CHAR_CACH + "";

	// Thêm các tùy chọn vai trò của chức năng tương ứng
	public String QUANLYDUANLIST = QUANLYDUAN + CHAR_CACH + LIST;
	public String QUANLYDUANXEM = QUANLYDUAN + CHAR_CACH + XEM;
	public String QUANLYDUANTHEM = QUANLYDUAN + CHAR_CACH + THEM;
	public String QUANLYDUANSUA = QUANLYDUAN + CHAR_CACH + SUA;
	public String QUANLYDUANXOA = QUANLYDUAN + CHAR_CACH + XOA;
	
	@Value("${url.donvihanhchinh}" + ":" + "${action.xem}")
	public String DONVIHANHCHINHXEM;
	@Value("${url.donvihanhchinh}" + ":" + "${action.them}")
	public String DONVIHANHCHINHTHEM;
	@Value("${url.donvihanhchinh}" + ":" + "${action.sua}")
	public String DONVIHANHCHINHSUA;
	@Value("${url.donvihanhchinh}" + ":" + "${action.xoa}")
	public String DONVIHANHCHINHXOA;
	@Value("${url.donvihanhchinh}" + ":" + "${action.list}")
	public String DONVIHANHCHINHLIETKE;
	@Value("${url.donvihanhchinh}" + ":" + "${action.tim}")
	public String DONVIHANHCHINHTIMKIEM;

	@Value("${url.phanquyen}" + ":" + "${action.xem}")
	public String PHANQUYENXEM;
	@Value("${url.phanquyen}" + ":" + "${action.them}")
	public String PHANQUYENTHEM;
	@Value("${url.phanquyen}" + ":" + "${action.sua}")
	public String PHANQUYENSUA;
	@Value("${url.phanquyen}" + ":" + "${action.xoa}")
	public String PHANQUYENXOA;
	@Value("${url.phanquyen}" + ":" + "${action.list}")
	public String PHANQUYENLIETKE;
	@Value("${url.phanquyen}" + ":" + "${action.tim}")
	public String PHANQUYENTIMKIEM;

	// Thêm các tùy chọn vai trò của chức năng tương ứng
	@Value("${url.thuctrangditich}" + ":" + "${action.xem}")
	public String THUCTRANGDITICHXEM;
	@Value("${url.thuctrangditich}" + ":" + "${action.them}")
	public String THUCTRANGDITICHTHEM;
	@Value("${url.thuctrangditich}" + ":" + "${action.sua}")
	public String THUCTRANGDITICHSUA;
	@Value("${url.thuctrangditich}" + ":" + "${action.xoa}")
	public String THUCTRANGDITICHXOA;
	@Value("${url.thuctrangditich}" + ":" + "${action.list}")
	public String THUCTRANGDITICHLIETKE;
	@Value("${url.thuctrangditich}" + ":" + "${action.tim}")
	public String THUCTRANGDITICHTIMKIEM;

	// Thêm các tùy chọn vai trò của chức năng tương ứng
	@Value("${url.loaiditich}" + ":" + "${action.xem}")
	public String LOAIDITICHXEM;
	@Value("${url.loaiditich}" + ":" + "${action.them}")
	public String LOAIDITICHTHEM;
	@Value("${url.loaiditich}" + ":" + "${action.sua}")
	public String LOAIDITICHSUA;
	@Value("${url.loaiditich}" + ":" + "${action.xoa}")
	public String LOAIDITICHXOA;
	@Value("${url.loaiditich}" + ":" + "${action.list}")
	public String LOAIDITICHLIETKE;
	@Value("${url.loaiditich}" + ":" + "${action.tim}")
	public String LOAIDITICHTIMKIEM;

	@Value("${url.loailehoi}" + ":" + "${action.xem}")
	public String LOAILEHOIXEM;
	@Value("${url.loailehoi}" + ":" + "${action.them}")
	public String LOAILEHOITHEM = "";
	@Value("${url.loailehoi}" + ":" + "${action.list}")
	public String LOAILEHOILIST = "";
	@Value("${url.loailehoi}" + ":" + "${action.xoa}")
	public String LOAILEHOIXOA = "";
	@Value("${url.loailehoi}" + ":" + "${action.sua}")
	public String LOAILEHOISUA = "";
	@Value("${url.loailehoi}" + ":" + "${action.tim}")
	public String LOAILEHOITIMKIEM;

	@Value("${url.loaidisan}" + ":" + "${action.xem}")
	public String LOAIDISANXEM;
	@Value("${url.loaidisan}" + ":" + "${action.them}")
	public String LOAIDISANTHEM = "";
	@Value("${url.loaidisan}" + ":" + "${action.list}")
	public String LOAIDISANLIST = "";
	@Value("${url.loaidisan}" + ":" + "${action.xoa}")
	public String LOAIDISANXOA = "";
	@Value("${url.loaidisan}" + ":" + "${action.sua}")
	public String LOAIDISANSUA = "";
	@Value("${url.loaidisan}" + ":" + "${action.tim}")
	public String LOAIDISANTIMKIEM;

	@Value("${url.thongtinlehoi}" + ":" + "${action.xem}")
	public String THONGTINLEHOIXEM;
	@Value("${url.thongtinlehoi}" + ":" + "${action.them}")
	public String THONGTINLEHOIHEM = "";
	@Value("${url.thongtinlehoi}" + ":" + "${action.list}")
	public String THONGTINLEHOILIST = "";
	@Value("${url.thongtinlehoi}" + ":" + "${action.xoa}")
	public String THONGTINLEHOIXOA = "";
	@Value("${url.thongtinlehoi}" + ":" + "${action.sua}")
	public String THONGTINLEHOISUA = "";
	@Value("${url.thongtinlehoi}" + ":" + "${action.tim}")
	public String THONGTINLEHOITIMKIEM;

	@Value("${url.thongtinditich}" + ":" + "${action.xem}")
	public String THONGTINDITICHXEM;
	@Value("${url.thongtinditich}" + ":" + "${action.them}")
	public String THONGTINDITICHTHEM = "";
	@Value("${url.thongtinditich}" + ":" + "${action.list}")
	public String THONGTINDITICHLIST = "";
	@Value("${url.thongtinditich}" + ":" + "${action.xoa}")
	public String THONGTINDITICHXOA = "";
	@Value("${url.thongtinditich}" + ":" + "${action.sua}")
	public String THONGTINDITICHSUA = "";
	@Value("${url.thongtinditich}" + ":" + "${action.tim}")
	public String THONGTINDITICHTIMKIEM;

	@Value("${url.thongtindisan}" + ":" + "${action.xem}")
	public String THONGTINDISANXEM;
	@Value("${url.thongtindisan}" + ":" + "${action.them}")
	public String THONGTINDISANTHEM = "";
	@Value("${url.thongtindisan}" + ":" + "${action.list}")
	public String THONGTINDISANLIST = "";
	@Value("${url.thongtindisan}" + ":" + "${action.xoa}")
	public String THONGTINDISANXOA = "";
	@Value("${url.thongtindisan}" + ":" + "${action.sua}")
	public String THONGTINDISANSUA = "";
	@Value("${url.thongtindisan}" + ":" + "${action.tim}")
	public String THONGTINDISANTIMKIEM;

	@Value("${url.thongkeditich}" + ":" + "${action.xem}")
	public String THONGKEDITICHXEM;
	@Value("${url.thongkeditich}" + ":" + "${action.them}")
	public String THONGKEDITICHTHEM = "";
	@Value("${url.thongkeditich}" + ":" + "${action.list}")
	public String THONGKEDITICHLIST = "";
	@Value("${url.thongkeditich}" + ":" + "${action.xoa}")
	public String THONGKEDITICHXOA = "";
	@Value("${url.thongkeditich}" + ":" + "${action.sua}")
	public String THONGKEDITICHSUA = "";
	@Value("${url.thongkeditich}" + ":" + "${action.tim}")
	public String THONGKEDITICHTIMKIEM;
	@Value("${url.thongkeditich}" + ":" + "${action.thongke}")
	public String THONGKEDITICHTHONGKE;

	@Value("${url.thongkebieumaubaocao}" + ":" + "${action.xem}")
	public String THONGKEBIEUMAUBAOCAOXEM;
	@Value("${url.thongkebieumaubaocao}" + ":" + "${action.tim}")
	public String THONGKEBIEUMAUBAOCAOTIMKIEM;
	@Value("${url.thongkebieumaubaocao}" + ":" + "${action.thongke}")
	public String THONGKEBIEUMAUBAOCAOTAOEXCEL;
	

	@Value("${url.nguoidung}" + ":" + "${action.xem}")
	public String NGUOIDUNGXEM = "";
	@Value("${url.nguoidung}" + ":" + "${action.them}")
	public String NGUOIDUNGTHEM = "";
	@Value("${url.nguoidung}" + ":" + "${action.list}")
	public String NGUOIDUNGLIST = "";
	@Value("${url.nguoidung}" + ":" + "${action.xoa}")
	public String NGUOIDUNGXOA = "";
	@Value("${url.nguoidung}" + ":" + "${action.sua}")
	public String NGUOIDUNGSUA = "";
	@Value("${url.nguoidung}" + ":" + "${action.tim}")
	public String NGUOIDUNGTIMKIEM;
	

	@Value("${url.ditich}" + ":" + "${action.xem}")
	public String DITICHXEM = "";
	@Value("${url.ditich}" + ":" + "${action.them}")
	public String DITICHTHEM = "";
	@Value("${url.ditich}" + ":" + "${action.list}")
	public String DITICHLIST = "";
	@Value("${url.ditich}" + ":" + "${action.xoa}")
	public String DITICHXOA = "";
	@Value("${url.ditich}" + ":" + "${action.sua}")
	public String DITICHSUA = "";
	@Value("${url.ditich}" + ":" + "${action.tim}")
	public String DITICHTIMKIEM;

	@Value("${url.lehoi}" + ":" + "${action.xem}")
	public String LEHOIXEM = "";
	@Value("${url.lehoi}" + ":" + "${action.them}")
	public String LEHOITHEM = "";
	@Value("${url.lehoi}" + ":" + "${action.list}")
	public String LEHOILIST = "";
	@Value("${url.lehoi}" + ":" + "${action.xoa}")
	public String LEHOIXOA = "";
	@Value("${url.lehoi}" + ":" + "${action.sua}")
	public String LEHOISUA = "";
	@Value("${url.lehoi}" + ":" + "${action.tim}")
	public String LEHOITIMKIEM;

	@Value("${url.dsvhpvt}" + ":" + "${action.xem}")
	public String DISANVANHOAXEM = "";
	@Value("${url.dsvhpvt}" + ":" + "${action.them}")
	public String DISANVANHOATHEM = "";
	@Value("${url.dsvhpvt}" + ":" + "${action.list}")
	public String DISANVANHOALIST = "";
	@Value("${url.dsvhpvt}" + ":" + "${action.xoa}")
	public String DISANVANHOAXOA = "";
	@Value("${url.dsvhpvt}" + ":" + "${action.sua}")
	public String DISANVANHOASUA = "";
	@Value("${url.dsvhpvt}" + ":" + "${action.tim}")
	public String DISANVANHOATIMKIEM;

	@Value("${url.vaitro}" + ":" + "${action.xem}")
	public String VAITROXEM = "";
	@Value("${url.vaitro}" + ":" + "${action.them}")
	public String VAITROTHEM = "";
	@Value("${url.vaitro}" + ":" + "${action.list}")
	public String VAITROLIST = "";
	@Value("${url.vaitro}" + ":" + "${action.xoa}")
	public String VAITROXOA = "";
	@Value("${url.vaitro}" + ":" + "${action.sua}")
	public String VAITROSUA = "";
	@Value("${url.vaitro}" + ":" + "${action.tim}")
	public String VAITROTIMKIEM;

	// aend
	public String[] getRESOURCES() { // Các title của vai trò
		return new String[] {DITICH, LEHOI, DISANVANHOA, THUCTRANGDITICH, LOAIDITICH, LOAILEHOI,LOAIDISAN, VAITRO,
				NGUOIDUNG, DONVIHANHCHINH, THONGKEBIEUMAUBAOCAO, QUANLYDUAN}; //
	}

	public String[] getACTIONS() {
		return new String[] { LIST, XEM, THEM, SUA, XOA, XUATBAN, LICHSUCAPNHAT, LICHSU, TIMKIEM, THONGKE };
	}

	static {
		File file = new File(Labels.getLabel("filestore.root") + File.separator + Labels.getLabel("filestore.folder"));
	}
	@Autowired
	public Environment env;

	@Autowired
	DataSource dataSource;

	public Entry() {
		super();
		setCore();
		instance = this;
	}

	@Bean
	public FilterRegistrationBean cacheFilter() {
		FilterRegistrationBean rs = new FilterRegistrationBean(new CacheFilter());
		rs.addUrlPatterns("*.css");
		rs.addUrlPatterns("*.js");
		rs.addUrlPatterns("*.wpd");
		rs.addUrlPatterns("*.wcs");
		rs.addUrlPatterns("*.jpg");
		rs.addUrlPatterns("*.jpeg");
		rs.addUrlPatterns("*.png");
		rs.addUrlPatterns("*.svg");
		rs.addUrlPatterns("*.gif");
		rs.addUrlPatterns("*.tff");
		return rs;
	}

	@RequestMapping(value = "/login")
	public String login() {
		return "forward:/WEB-INF/zul/login.zul";
	}

	@RequestMapping(value = "/upload2")
	public String upLoad() {
		return "forward:/WEB-INF/zul/upload/upload2.zul";
	}

	@RequestMapping(value = "/cp")
	public String cp2() {
		return "forward:/WEB-INF/zul/home.zul?resource=ditich&action=lietke&file=/WEB-INF/zul/ditich/list.zul&macdinh=home";
	}

	@RequestMapping(value = "/cp/{path:.+$}")
	public String cp(@PathVariable String path) {
		return "forward:/WEB-INF/zul/home.zul?resource=" + path + "&action=lietke&file=/WEB-INF/zul/" + path
				+ "/list.zul";
	}

	@RequestMapping(value = "/cp/quanlyduan/{path:.+$}")
	public String add2(@PathVariable String path) {
		return "forward:/WEB-INF/zul/home.zul?resource=quanlyduan&action=lietke&file=/WEB-INF/zul/quanlyduan/" + path
				+ ".zul";
	}
	
	@RequestMapping(value = "/cp/{path:.+$}/view/{id:\\d+}")
	public String view(@PathVariable String path, @PathVariable Long id) {
		return "forward:/WEB-INF/zul/home.zul?resource=" + path + "&action=lietke&file=/WEB-INF/zul/" + path
				+ "/view-page.zul&id="+id;
	}
	
	@RequestMapping(value = "/cp/thongke/{path:.+$}/{id:\\d+}")
	public String viewThongKe(@PathVariable String path, @PathVariable Long id) {
		return "forward:/WEB-INF/zul/home.zul?resource=thongke&action=lietke&file=/WEB-INF/zul/thongke/" + path
				+ ".zul&id="+id;
	}

	@RequestMapping(value = "/cp/{path:.+$}/edit/{id:\\d+}")
	public String edit2(@PathVariable String path, @PathVariable Long id) {
		return "forward:/WEB-INF/zul/home.zul?resource=" + path + "&action=lietke&file=/WEB-INF/zul/" + path
				+ "/add-page.zul&id=" + id;
	}

	public final NhanVienService getNhanViens() {
		return new NhanVienService();
	}

	public final DiTichService getDitichs() {
		return new DiTichService();
	}

	public final VaiTroService getVaiTros() {
		return new VaiTroService();
	}

	public final Quyen getQuyen() {
		return getNhanVien().getTatCaQuyen();
	}

	public final ThamSoService getThamSos() {
		return new ThamSoService();
	}

	public final VideoService getVideos() {
		return new VideoService();
	}

	public final ThucTrangDitichService getThucTrangDitichs() {
		return new ThucTrangDitichService();
	}

	public final LoaiDiTichService getLoaiDiTichs() {
		return new LoaiDiTichService();
	}

	public final LoaiLeHoiService getLoaiLeHois() {
		return new LoaiLeHoiService();
	}

	public final LoaiDiSanService getLoaiDiSans() {
		return new LoaiDiSanService();
	}

	public final HomeService getHomes() {
		return new HomeService();
	}
	
	public final GopYPhanMemService getGopYPhanMem() {
		return new GopYPhanMemService();
	}
	@RequestMapping(value = "/{type:.+$}/{id:\\d+}")
	public String showDetail( @PathVariable("type") String type, @PathVariable("id") String id) {
		return "forward:/frontend/home/home.zhtml?type=" + type + "&id="+id;
	}
	@RequestMapping(value = "/")
	public String showHome() {
		return "forward:/frontend/home/home.zhtml";
	}
	@RequestMapping(value = "/search")
	public String showHomeSearch() {
		return "forward:/frontend/home/search.zhtml";
	}
	
	public final DonViService getDonVis() {
		return new DonViService();
	}
	
	public final DonViHanhChinhService getDonViHanhChinhs() {
		return new DonViHanhChinhService();
	}

	public final LanguageService getLanguages() {
		return new LanguageService();
	}

	public boolean checkVaiTro(String vaiTro) {
		if (vaiTro == null || vaiTro.isEmpty()) {
			return false;
		}
		boolean rs = false;
		for (VaiTro vt : getNhanVien().getVaiTros()) {
			if (vaiTro.equals(vt.getAlias())) {
				rs = true;
				break;
			}
		}
		return rs;// || getQuyen().get(vaiTro);
	}

	@Configuration
	@EnableWebMvc
	public static class MvcConfig extends WebMvcConfigurerAdapter {
		@Override
		public void addResourceHandlers(ResourceHandlerRegistry registry) {
			registry.addResourceHandler("/files/**").addResourceLocations("file:/home/hdndhoavangdata/hdndfiles/");
			registry.addResourceHandler("/assets/**").addResourceLocations("/assets/");
			registry.addResourceHandler("/backend/**").addResourceLocations("/backend/");
			registry.addResourceHandler("/img/**").addResourceLocations("/img/");
			registry.addResourceHandler("/login/**").addResourceLocations("/login/");
		}

		@Override
		public void configureViewResolvers(final ViewResolverRegistry registry) {
			registry.jsp("/WEB-INF/", "*");
		}

		@ExceptionHandler(ResourceNotFoundException.class)
		@ResponseStatus(HttpStatus.NOT_FOUND)
		public String handleResourceNotFoundException() {
			return "forward:/WEB-INF/zul/notfound.zul";
		}
	}

}