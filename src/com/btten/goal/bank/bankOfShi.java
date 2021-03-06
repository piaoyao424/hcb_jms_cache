package com.btten.goal.bank;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.btten.Jms.BaseActivity;
import com.btten.Jms.R;

public class bankOfShi extends Activity {
	// 控件
	private TextView tv_bankshi_title = null;
	private ListView lv_bankshi_banklist = null;
	//线程
	private shengThread updthread = null;
	//省的qyid
	private String getShengQyid = null;
	//异常日志
	private final static String TAG = "Log_bankshi";
	// 省市的数组
	private String[][] str_shengname = { { "0", "中国", "-1" },
			{ "1", "北京市", "0" }, { "2", "天津市", "0" }, { "3", "河北省", "0" },
			{ "4", "山西省", "0" }, { "5", "内蒙古自治区", "0" }, { "6", "辽宁省", "0" },
			{ "7", "吉林省", "0" }, { "8", "黑龙江省", "0" }, { "9", "上海市", "0" },
			{ "10", "江苏省", "0" }, { "11", "浙江省", "0" }, { "12", "安徽省", "0" },
			{ "13", "福建省", "0" }, { "14", "江西省", "0" }, { "15", "山东省", "0" },
			{ "16", "河南省", "0" }, { "17", "湖北省", "0" }, { "18", "湖南省", "0" },
			{ "19", "广东省", "0" }, { "20", "广西壮族自治区", "0" },
			{ "21", "海南省", "0" }, { "22", "重庆市", "0" }, { "23", "四川省", "0" },
			{ "24", "贵州省", "0" }, { "25", "云南省", "0" }, { "26", "西藏自治区", "0" },
			{ "27", "陕西省", "0" }, { "28", "甘肃省", "0" }, { "29", "青海省", "0" },
			{ "30", "宁夏回族自治区", "0" }, { "31", "新疆维吾尔自治区", "0" },
			{ "32", "台湾省", "0" }, { "33", "香港特别行政区", "0" },
			{ "34", "澳门特别行政区", "0" }, { "35", "海外", "0" }, { "36", "其他", "0" },
			{ "37", "东城区", "1" }, { "38", "西城区", "1" }, { "39", "崇文区", "1" },
			{ "40", "宣武区", "1" }, { "41", "朝阳区", "1" }, { "42", "丰台区", "1" },
			{ "43", "石景山区", "1" }, { "44", "海淀区", "1" }, { "45", "门头沟区", "1" },
			{ "46", "房山区", "1" }, { "47", "通州区", "1" }, { "48", "顺义区", "1" },
			{ "49", "昌平区", "1" }, { "50", "大兴区", "1" }, { "51", "怀柔区", "1" },
			{ "52", "平谷区", "1" }, { "53", "密云县", "1" }, { "54", "延庆县", "1" },
			{ "55", "和平区", "2" }, { "56", "河东区", "2" }, { "57", "河西区", "2" },
			{ "58", "南开区", "2" }, { "59", "河北区", "2" }, { "60", "红桥区", "2" },
			{ "61", "塘沽区", "2" }, { "62", "汉沽区", "2" }, { "63", "大港区", "2" },
			{ "64", "东丽区", "2" }, { "65", "西青区", "2" }, { "66", "津南区", "2" },
			{ "67", "北辰区", "2" }, { "68", "武清区", "2" }, { "69", "宝坻区", "2" },
			{ "70", "宁河县", "2" }, { "71", "静海县", "2" }, { "72", "蓟县", "2" },
			{ "73", "石家庄市", "3" }, { "74", "唐山市", "3" }, { "75", "秦皇岛市", "3" },
			{ "76", "邯郸市", "3" }, { "77", "邢台市", "3" }, { "78", "保定市", "3" },
			{ "79", "张家口市", "3" }, { "80", "承德市", "3" }, { "81", "衡水市", "3" },
			{ "82", "廊坊市", "3" }, { "83", "沧州市", "3" }, { "84", "太原市", "4" },
			{ "85", "大同市", "4" }, { "86", "阳泉市", "4" }, { "87", "长治市", "4" },
			{ "88", "晋城市", "4" }, { "89", "朔州市", "4" }, { "90", "晋中市", "4" },
			{ "91", "运城市", "4" }, { "92", "忻州市", "4" }, { "93", "临汾市", "4" },
			{ "94", "吕梁市", "4" }, { "95", "呼和浩特市", "5" }, { "96", "包头市", "5" },
			{ "97", "乌海市", "5" }, { "98", "赤峰市", "5" }, { "99", "通辽市", "5" },
			{ "100", "鄂尔多斯市", "5" }, { "101", "呼伦贝尔市", "5" },
			{ "102", "巴彦淖尔市", "5" }, { "103", "乌兰察布市", "5" },
			{ "104", "兴安盟", "5" }, { "105", "锡林郭勒盟", "5" },
			{ "106", "阿拉善盟", "5" }, { "107", "沈阳市", "6" },
			{ "108", "大连市", "6" }, { "109", "鞍山市", "6" },
			{ "110", "抚顺市", "6" }, { "111", "本溪市", "6" },
			{ "112", "丹东市", "6" }, { "113", "锦州市", "6" },
			{ "114", "营口市", "6" }, { "115", "阜新市", "6" },
			{ "116", "辽阳市", "6" }, { "117", "盘锦市", "6" },
			{ "118", "铁岭市", "6" }, { "119", "朝阳市", "6" },
			{ "120", "葫芦岛市", "6" }, { "121", "长春市", "7" },
			{ "122", "吉林市", "7" }, { "123", "四平市", "7" },
			{ "124", "辽源市", "7" }, { "125", "通化市", "7" },
			{ "126", "白山市", "7" }, { "127", "松原市", "7" },
			{ "128", "白城市", "7" }, { "129", "延边朝鲜族自治州", "7" },
			{ "130", "哈尔滨市", "8" }, { "131", "齐齐哈尔市", "8" },
			{ "132", "鸡西市", "8" }, { "133", "鹤岗市", "8" },
			{ "134", "双鸭山市", "8" }, { "135", "大庆市", "8" },
			{ "136", "伊春市", "8" }, { "137", "佳木斯市", "8" },
			{ "138", "七台河市", "8" }, { "139", "牡丹江市", "8" },
			{ "140", "黑河市", "8" }, { "141", "绥化市", "8" },
			{ "142", "大兴安岭地区", "8" }, { "143", "黄浦区", "9" },
			{ "144", "卢湾区", "9" }, { "145", "徐汇区", "9" },
			{ "146", "长宁区", "9" }, { "147", "静安区", "9" },
			{ "148", "普陀区", "9" }, { "149", "闸北区", "9" },
			{ "150", "虹口区", "9" }, { "151", "杨浦区", "9" },
			{ "152", "闵行区", "9" }, { "153", "宝山区", "9" },
			{ "154", "嘉定区", "9" }, { "155", "浦东新区", "9" },
			{ "156", "金山区", "9" }, { "157", "松江区", "9" },
			{ "158", "青浦区", "9" }, { "159", "南汇区", "9" },
			{ "160", "奉贤区", "9" }, { "161", "崇明县", "9" },
			{ "162", "南京市", "10" }, { "163", "无锡市", "10" },
			{ "164", "徐州市", "10" }, { "165", "常州市", "10" },
			{ "166", "苏州市", "10" }, { "167", "南通市", "10" },
			{ "168", "连云港市", "10" }, { "169", "淮安市", "10" },
			{ "170", "盐城市", "10" }, { "171", "扬州市", "10" },
			{ "172", "镇江市", "10" }, { "173", "泰州市", "10" },
			{ "174", "宿迁市", "10" }, { "175", "杭州市", "11" },
			{ "176", "宁波市", "11" }, { "177", "温州市", "11" },
			{ "178", "嘉兴市", "11" }, { "179", "湖州市", "11" },
			{ "180", "绍兴市", "11" }, { "181", "舟山市", "11" },
			{ "182", "衢州市", "11" }, { "183", "金华市", "11" },
			{ "184", "台州市", "11" }, { "185", "丽水市", "11" },
			{ "186", "合肥市", "12" }, { "187", "芜湖市", "12" },
			{ "188", "蚌埠市", "12" }, { "189", "淮南市", "12" },
			{ "190", "马鞍山市", "12" }, { "191", "淮北市", "12" },
			{ "192", "铜陵市", "12" }, { "193", "安庆市", "12" },
			{ "194", "黄山市", "12" }, { "195", "滁州市", "12" },
			{ "196", "阜阳市", "12" }, { "197", "宿州市", "12" },
			{ "198", "巢湖市", "12" }, { "199", "六安市", "12" },
			{ "200", "亳州市", "12" }, { "201", "池州市", "12" },
			{ "202", "宣城市", "12" }, { "203", "福州市", "13" },
			{ "204", "厦门市", "13" }, { "205", "莆田市", "13" },
			{ "206", "三明市", "13" }, { "207", "泉州市", "13" },
			{ "208", "漳州市", "13" }, { "209", "南平市", "13" },
			{ "210", "龙岩市", "13" }, { "211", "宁德市", "13" },
			{ "212", "南昌市", "14" }, { "213", "景德镇市", "14" },
			{ "214", "萍乡市", "14" }, { "215", "九江市", "14" },
			{ "216", "新余市", "14" }, { "217", "鹰潭市", "14" },
			{ "218", "赣州市", "14" }, { "219", "吉安市", "14" },
			{ "220", "宜春市", "14" }, { "221", "抚州市", "14" },
			{ "222", "上饶市", "14" }, { "223", "济南市", "15" },
			{ "224", "青岛市", "15" }, { "225", "淄博市", "15" },
			{ "226", "枣庄市", "15" }, { "227", "东营市", "15" },
			{ "228", "烟台市", "15" }, { "229", "潍坊市", "15" },
			{ "230", "济宁市", "15" }, { "231", "泰安市", "15" },
			{ "232", "威海市", "15" }, { "233", "日照市", "15" },
			{ "234", "莱芜市", "15" }, { "235", "临沂市", "15" },
			{ "236", "德州市", "15" }, { "237", "聊城市", "15" },
			{ "238", "滨州市", "15" }, { "239", "菏泽市", "15" },
			{ "240", "郑州市", "16" }, { "241", "开封市", "16" },
			{ "242", "洛阳市", "16" }, { "243", "平顶山市", "16" },
			{ "244", "安阳市", "16" }, { "245", "鹤壁市", "16" },
			{ "246", "新乡市", "16" }, { "247", "焦作市", "16" },
			{ "248", "濮阳市", "16" }, { "249", "许昌市", "16" },
			{ "250", "漯河市", "16" }, { "251", "三门峡市", "16" },
			{ "252", "南阳市", "16" }, { "253", "商丘市", "16" },
			{ "254", "信阳市", "16" }, { "255", "周口市", "16" },
			{ "256", "驻马店市", "16" }, { "257", "济源市", "16" },
			{ "258", "武汉市", "17" }, { "259", "黄石市", "17" },
			{ "260", "十堰市", "17" }, { "261", "宜昌市", "17" },
			{ "262", "襄樊市", "17" }, { "263", "鄂州市", "17" },
			{ "264", "荆门市", "17" }, { "265", "孝感市", "17" },
			{ "266", "荆州市", "17" }, { "267", "黄冈市", "17" },
			{ "268", "咸宁市", "17" }, { "269", "随州市", "17" },
			{ "270", "恩施土家族苗族自治州", "17" }, { "271", "仙桃市", "17" },
			{ "272", "潜江市", "17" }, { "273", "天门市", "17" },
			{ "274", "神农架林区", "17" }, { "275", "长沙市", "18" },
			{ "276", "株洲市", "18" }, { "277", "湘潭市", "18" },
			{ "278", "衡阳市", "18" }, { "279", "邵阳市", "18" },
			{ "280", "岳阳市", "18" }, { "281", "常德市", "18" },
			{ "282", "张家界市", "18" }, { "283", "益阳市", "18" },
			{ "284", "郴州市", "18" }, { "285", "永州市", "18" },
			{ "286", "怀化市", "18" }, { "287", "娄底市", "18" },
			{ "288", "湘西土家族苗族自治州", "18" }, { "289", "广州市", "19" },
			{ "290", "韶关市", "19" }, { "291", "深圳市", "19" },
			{ "292", "珠海市", "19" }, { "293", "汕头市", "19" },
			{ "294", "佛山市", "19" }, { "295", "江门市", "19" },
			{ "296", "湛江市", "19" }, { "297", "茂名市", "19" },
			{ "298", "肇庆市", "19" }, { "299", "惠州市", "19" },
			{ "300", "梅州市", "19" }, { "301", "汕尾市", "19" },
			{ "302", "河源市", "19" }, { "303", "阳江市", "19" },
			{ "304", "清远市", "19" }, { "305", "东莞市", "19" },
			{ "306", "中山市", "19" }, { "307", "潮州市", "19" },
			{ "308", "揭阳市", "19" }, { "309", "云浮市", "19" },
			{ "310", "南宁市", "20" }, { "311", "柳州市", "20" },
			{ "312", "桂林市", "20" }, { "313", "梧州市", "20" },
			{ "314", "北海市", "20" }, { "315", "防城港市", "20" },
			{ "316", "钦州市", "20" }, { "317", "贵港市", "20" },
			{ "318", "玉林市", "20" }, { "319", "百色市", "20" },
			{ "320", "贺州市", "20" }, { "321", "河池市", "20" },
			{ "322", "来宾市", "20" }, { "323", "崇左市", "20" },
			{ "324", "海口市", "21" }, { "325", "三亚市", "21" },
			{ "326", "五指山市", "21" }, { "327", "琼海市", "21" },
			{ "328", "儋州市", "21" }, { "329", "文昌市", "21" },
			{ "330", "万宁市", "21" }, { "331", "东方市", "21" },
			{ "332", "定安县", "21" }, { "333", "屯昌县", "21" },
			{ "334", "澄迈县", "21" }, { "335", "临高县", "21" },
			{ "336", "白沙黎族自治县", "21" }, { "337", "昌江黎族自治县", "21" },
			{ "338", "乐东黎族自治县", "21" }, { "339", "陵水黎族自治县", "21" },
			{ "340", "保亭黎族苗族自治县", "21" }, { "341", "琼中黎族苗族自治县", "21" },
			{ "342", "西沙群岛", "21" }, { "343", "南沙群岛", "21" },
			{ "344", "中沙群岛的岛礁及其海", "21" }, { "345", "万州区", "22" },
			{ "346", "涪陵区", "22" }, { "347", "渝中区", "22" },
			{ "348", "大渡口区", "22" }, { "349", "江北区", "22" },
			{ "350", "沙坪坝区", "22" }, { "351", "九龙坡区", "22" },
			{ "352", "南岸区", "22" }, { "353", "北碚区", "22" },
			{ "354", "双桥区", "22" }, { "355", "万盛区", "22" },
			{ "356", "渝北区", "22" }, { "357", "巴南区", "22" },
			{ "358", "黔江区", "22" }, { "359", "长寿区", "22" },
			{ "360", "綦江县", "22" }, { "361", "潼南县", "22" },
			{ "362", "铜梁县", "22" }, { "363", "大足县", "22" },
			{ "364", "荣昌县", "22" }, { "365", "璧山县", "22" },
			{ "366", "梁平县", "22" }, { "367", "城口县", "22" },
			{ "368", "丰都县", "22" }, { "369", "垫江县", "22" },
			{ "370", "武隆县", "22" }, { "371", "忠县", "22" },
			{ "372", "开县", "22" }, { "373", "云阳县", "22" },
			{ "374", "奉节县", "22" }, { "375", "巫山县", "22" },
			{ "376", "巫溪县", "22" }, { "377", "石柱土家族自治县", "22" },
			{ "378", "秀山土家族苗族自治县", "22" }, { "379", "酉阳土家族苗族自治县", "22" },
			{ "380", "彭水苗族土家族自治县", "22" }, { "381", "江津市", "22" },
			{ "382", "合川市", "22" }, { "383", "永川市", "22" },
			{ "384", "南川市", "22" }, { "385", "成都市", "23" },
			{ "386", "自贡市", "23" }, { "387", "攀枝花市", "23" },
			{ "388", "泸州市", "23" }, { "389", "德阳市", "23" },
			{ "390", "绵阳市", "23" }, { "391", "广元市", "23" },
			{ "392", "遂宁市", "23" }, { "393", "内江市", "23" },
			{ "394", "乐山市", "23" }, { "395", "南充市", "23" },
			{ "396", "眉山市", "23" }, { "397", "宜宾市", "23" },
			{ "398", "广安市", "23" }, { "399", "达州市", "23" },
			{ "400", "雅安市", "23" }, { "401", "巴中市", "23" },
			{ "402", "资阳市", "23" }, { "403", "阿坝藏族羌族自治州", "23" },
			{ "404", "甘孜藏族自治州", "23" }, { "405", "凉山彝族自治州", "23" },
			{ "406", "贵阳市", "24" }, { "407", "六盘水市", "24" },
			{ "408", "遵义市", "24" }, { "409", "安顺市", "24" },
			{ "410", "铜仁地区", "24" }, { "411", "黔西南布依族苗族自治", "24" },
			{ "412", "毕节地区", "24" }, { "413", "黔东南苗族侗族自治州", "24" },
			{ "414", "黔南布依族苗族自治州", "24" }, { "415", "昆明市", "25" },
			{ "416", "曲靖市", "25" }, { "417", "玉溪市", "25" },
			{ "418", "保山市", "25" }, { "419", "昭通市", "25" },
			{ "420", "丽江市", "25" }, { "421", "思茅市", "25" },
			{ "422", "临沧市", "25" }, { "423", "楚雄彝族自治州", "25" },
			{ "424", "红河哈尼族彝族自治州", "25" }, { "425", "文山壮族苗族自治州", "25" },
			{ "426", "西双版纳傣族自治州", "25" }, { "427", "大理白族自治州", "25" },
			{ "428", "德宏傣族景颇族自治州", "25" }, { "429", "怒江傈僳族自治州", "25" },
			{ "430", "迪庆藏族自治州", "25" }, { "431", "拉萨市", "26" },
			{ "432", "昌都地区", "26" }, { "433", "山南地区", "26" },
			{ "434", "日喀则地区", "26" }, { "435", "那曲地区", "26" },
			{ "436", "阿里地区", "26" }, { "437", "林芝地区", "26" },
			{ "438", "西安市", "27" }, { "439", "铜川市", "27" },
			{ "440", "宝鸡市", "27" }, { "441", "咸阳市", "27" },
			{ "442", "渭南市", "27" }, { "443", "延安市", "27" },
			{ "444", "汉中市", "27" }, { "445", "榆林市", "27" },
			{ "446", "安康市", "27" }, { "447", "商洛市", "27" },
			{ "448", "兰州市", "28" }, { "449", "嘉峪关市", "28" },
			{ "450", "金昌市", "28" }, { "451", "白银市", "28" },
			{ "452", "天水市", "28" }, { "453", "武威市", "28" },
			{ "454", "张掖市", "28" }, { "455", "平凉市", "28" },
			{ "456", "酒泉市", "28" }, { "457", "庆阳市", "28" },
			{ "458", "定西市", "28" }, { "459", "陇南市", "28" },
			{ "460", "临夏回族自治州", "28" }, { "461", "甘南藏族自治州", "28" },
			{ "462", "西宁市", "29" }, { "463", "海东地区", "29" },
			{ "464", "海北藏族自治州", "29" }, { "465", "黄南藏族自治州", "29" },
			{ "466", "海南藏族自治州", "29" }, { "467", "果洛藏族自治州", "29" },
			{ "468", "玉树藏族自治州", "29" }, { "469", "海西蒙古族藏族自治州", "29" },
			{ "470", "银川市", "30" }, { "471", "石嘴山市", "30" },
			{ "472", "吴忠市", "30" }, { "473", "固原市", "30" },
			{ "474", "中卫市", "30" }, { "475", "乌鲁木齐市", "31" },
			{ "476", "克拉玛依市", "31" }, { "477", "吐鲁番地区", "31" },
			{ "478", "哈密地区", "31" }, { "479", "昌吉回族自治州", "31" },
			{ "480", "博尔塔拉蒙古自治州", "31" }, { "481", "巴音郭楞蒙古自治州", "31" },
			{ "482", "阿克苏地区", "31" }, { "483", "克孜勒苏柯尔克孜自治", "31" },
			{ "484", "喀什地区", "31" }, { "485", "和田地区", "31" },
			{ "486", "伊犁哈萨克自治州", "31" }, { "487", "塔城地区", "31" },
			{ "488", "阿勒泰地区", "31" }, { "489", "石河子市", "31" },
			{ "490", "阿拉尔市", "31" }, { "491", "图木舒克市", "31" },
			{ "492", "五家渠市", "31" }, { "493", "台北市", "32" },
			{ "494", "高雄市", "32" }, { "495", "基隆市", "32" },
			{ "496", "台中市", "32" }, { "497", "台南市", "32" },
			{ "498", "新竹市", "32" }, { "499", "嘉义市", "32" },
			{ "500", "台北县", "32" }, { "501", "宜兰县", "32" },
			{ "502", "桃园县", "32" }, { "503", "新竹县", "32" },
			{ "504", "苗栗县", "32" }, { "505", "台中县", "32" },
			{ "506", "彰化县", "32" }, { "507", "南投县", "32" },
			{ "508", "云林县", "32" }, { "509", "嘉义县", "32" },
			{ "510", "台南县", "32" }, { "511", "高雄县", "32" },
			{ "512", "屏东县", "32" }, { "513", "澎湖县", "32" },
			{ "514", "台东县", "32" }, { "515", "花莲县", "32" },
			{ "516", "中西区", "33" }, { "517", "东区", "33" },
			{ "518", "九龙城区", "33" }, { "519", "观塘区", "33" },
			{ "520", "南区", "33" }, { "521", "深水埗区", "33" },
			{ "522", "黄大仙区", "33" }, { "523", "湾仔区", "33" },
			{ "524", "油尖旺区", "33" }, { "525", "离岛区", "33" },
			{ "526", "葵青区", "33" }, { "527", "北区", "33" },
			{ "528", "西贡区", "33" }, { "529", "沙田区", "33" },
			{ "530", "屯门区", "33" }, { "531", "大埔区", "33" },
			{ "532", "荃湾区", "33" }, { "533", "元朗区", "33" },
			{ "534", "澳门特别行政区", "34" } };
	// 线程
	private MyUIHandler uiHandler;
	// msghandle通信
	private final static int BANK_UI = 1;
	//省市的字符串
	private String shengshi = null;
	private String fengefu = "XX";
	// list
	private ArrayList<HashMap<String, Object>> shiList = new ArrayList<HashMap<String, Object>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		setContentView(com.btten.calltaxi.R.layout.bankofsheng);
		setContentView(R.layout.bankofshi);
//		setBackKeyListner(backListener);
		// start

		// 准备工作
		findViews();
		init();
	}

	private void findViews() {
		tv_bankshi_title = (TextView) findViewById(R.id.tvId_bankshi_title);
		lv_bankshi_banklist = (ListView) findViewById(R.id.lvId_bankshi_shilist);
	}

	private void init() {
		//取得主页面发送来的数据
      	Bundle bundle = this.getIntent().getExtras();
      	getShengQyid = bundle.getString("KEY_SHENGQYID");
      	System.out.println("来源省的qyid为>>>>>>>>>>>>> "+ getShengQyid);
		
		uiHandler = new MyUIHandler() ;
		updthread = new shengThread();
		updthread.start();

	}


	private void setBankList() {
		SimpleAdapter adapter = new SimpleAdapter(this, shiList,
				R.layout.bankitem, new String[] { "shiname" }, 
				new int[] { R.id.tvId_bankitem_item });

		lv_bankshi_banklist.setAdapter(adapter);

		lv_bankshi_banklist
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						// 点中市
						System.out.println("点中市或区id为 :" + shiList.get(arg2).get("shiqyid").toString());
						Intent intent = new Intent();
						Bundle bundle = new Bundle();
						intent.setClass(bankOfShi.this, bankOfFenlei.class);
						// send Mes to other view
						//省市信息的字符串，中间是||符号间隔
						shengshi = getShengQyid+fengefu+shiList.get(arg2).get("shiqyid").toString();
						System.out.println("省市信息为>>>>>>>>>>" + shengshi);
						bundle.putString("KEY_SHENGSHIINFO", shengshi);
						// intent.setClass(ShowDirectory.this, ReadBook.class);
						// bundle.putString("KEY_BOOKPATH",
						// dirList.get(arg2).get("dirpath").toString());
						// convertCodeAndGetText(dirList.get(arg2).get("dirpath").toString());
						intent.putExtras(bundle);
						startActivity(intent);
						bankOfShi.this.finish();
					}
				});

	}

	class MyUIHandler extends Handler {
		public MyUIHandler() {
		}

		public MyUIHandler(Looper L) {
			super(L);
		}

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case BANK_UI:
				System.out.println("打开市的列表!");

				setBankList();
				break;

			default:
				break;
			}

			super.handleMessage(msg);
		}
	}

	// 定义一个读取省的名字的线程。
	class shengThread extends Thread {
		public shengThread() {
			super();
		}

		public void run() {
			try {
				System.out.println("打开市目录线程执行！");
				// 取得本地的txt文件中的字符案串
				// String newReadStr = readTxtFile(bookFiles.toString());
				// split函数返回的数组以实际得到的元素个数为准。而不是定义的时候的255个。
				// arrayNewRead = newReadStr.split("\\|\\|");
				// getBookNameFromPath(arrayNewRead);
				// 取得数据库中的记录保存到listview中去
				getAllShi();
				Message msg = uiHandler.obtainMessage(BANK_UI, 0, 0, 0);
				uiHandler.sendMessage(msg);

			} catch (Exception e) {
				Log.e(TAG, "Error in shiThread -> run() " + e.toString());
			}

		}
	}

	private void getAllShi() {
		//String[][] list = new String[50][3];
		//所有省市的个数
		int allCount = str_shengname.length;
//		System.out.println("所有省市的个数为："+ allCount);
		
		for (int i = 0; i < allCount; i++) {
			if(str_shengname[i][2].equals(getShengQyid))
			{
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("shiqyid", str_shengname[i][0]);
				map.put("shiname", str_shengname[i][1]);
				map.put("upid", str_shengname[i][2]);
				shiList.add(map);
			}
		}
	}
	
	OnClickListener backListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			onBackPressed();
		}
	};
	
}
