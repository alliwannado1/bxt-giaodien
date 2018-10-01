package vn.toancauxanh.gg.model;

import java.util.Date;

import vn.toancauxanh.gg.model.enums.MucDoUuTien;
import vn.toancauxanh.model.Model;
import vn.toancauxanh.model.NhanVien;

public class ThongTinDuAn extends Model<ThongTinDuAn> {
	
	private String nhaDauTu = "";
	private NhanVien nguoiPhuTrach;
	private LinhVuc linhVuc;
	private Date thoiGianBatDauXucTien;
	private String nganhNgheChiTiet = "";
	private String mucDichCuaDuAn = "";
	private String quiMoCuaDuAn = "";
	private String tongVonDauTu = "";
	private DiaDiemDuAn diaDiemDuAn;
	private String dienTichSuDungDat = "";
	private String congNgheSuDung = "";
	private String danhGiaSoBoHieuQuaKinhTe = "";
	private String tinhHinhCanhTranhCacDiaDiemThayThe = "";
	private MucDoUuTien mucDoUuTien;
	private DonViPhoiHop donViPhoiHop;
	
}
