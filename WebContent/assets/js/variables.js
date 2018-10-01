const wfs_service = 'http://192.168.1.157:8080/geoserver/demogisqn/ows';
const wms_service = 'http://192.168.1.157:8080/geoserver/demogisqn/wms';
const featureNS_service = 'http://192.168.1.157:8080/geoserver/demogisqn';
const layers_polygon = 'demogisqn:demogisqn-polygon';
const urlBase = 'assets/json/base.json';
const urlClass = 'assets/json/class.json';
const urlFeatures = 'assets/json/features.json';
var mapComponent;
var mapPanel;
var olMap;
var veTinh;
var gm;
var view;
var btnName;
var featureSelect;
var lopNenGroup;
var treeStore;
var interaction;
var interactionSelect;
var transactWFS;
var interactionSelectPointerMove = [];
var dirty = {}; // wfs-t
var colors;
var cssChuGiai;
var typeNameClick = "";
var featureNameAdd = ""; //Tên lớp cần thêm vùng
var indexBound = 0; //Vị trí cuối cùng của mỗi lớp
var arrIndexBound = [0]; // Khởi tạo phần tử đầu tiên là 0
var interactionSnap = [];
var sourceWFS = [];
var layerWFS = [];
var styleFunction;
var arrayChecked = [];
var lopDucPho = [];
var listFeatures;
var listAll = [];
var list_ten_xa = [];
var list_xa_id = [];
var list_ten_huyen = [];
var list_huyen_id = [];
var list_ty_le = [];
var list_ten_dat_qh = [];
var list_ma_dat_qh = [];
var listNameHuyen = []; //Lấy tên huyện cho từng lớp
var listId = []; //Lấy id các huyện
var listLength = [];
var listCenter = []; //Lấy toạ độ trung tâm các huyện
var widthW;
var heightW;
var huyenStore = [];