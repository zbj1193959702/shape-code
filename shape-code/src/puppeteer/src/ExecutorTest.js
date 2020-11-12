Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};

const LC_platform = require('./platform/LianCang/LianCang');
const ZG_platform = require('./platform/zgzs/zgzs');
const CFW = require('./platform/cfw/cfw');
const TC = require('./platform/TongCheng/tc');
const util = require('./base/utils');
const Api = require('./base/api');

async function zg_post() {
    let taskOne = {};
    taskOne.businessPhone = '13142771149';
    taskOne.password = 'toudengcang123';
    taskOne.projectId = 457039;
    taskOne.title = '嘉定独门独院独栋7000平方，厂房仓库';
    taskOne.description = "【地址】：上海-嘉定-安亭-漳翔路\n" +
        "【面积】：共7000平可租，可分租，500平起租\n" +
        "【结构】：单层钢混结构\n" +
        "【地面】：环氧地坪，3吨/平米\n" +
        "【层高】：层高9米\n" +
        "【配电】：配电200KVA，配电充足\n" +
        "【消防】：消防栓系统\n" +
        "【优势】：场地大、独门独院、标准厂房、层高高、配办公、配宿舍、配食堂、可环评、单独大门、大车可进出\n" +
        "【周边】：本项目位于上海市上海市嘉定区安亭。附近有众多制造业及物流业集聚，交通便捷，距离中信泰富万达广场0公里，距离嘉定新城0公里，距离G15沈海高速1公里，距离S6沪翔高速4公里，距离G1501上海绕城高速4公里，距离S5沪嘉高速5公里，距离G2京沪高速13公里，距离S20外环15公里，距离G42沪蓉高速15公里，距离S26沪常高速18公里。本项目含有一栋单层厂房。周转场地深度5-10米。\n" +
        "【用途】：适合塑胶塑料金属设备等\n" +
        "【其他】：配套办公室宿舍食堂";
    taskOne.taskId = 5574;
    taskOne.usageType = 2;
    taskOne.province = '上海市';
    taskOne.city = '上海';
    taskOne.district = '嘉定区';
    taskOne.market = '安亭';
    await ZG_platform.post(taskOne);
}

async function test_getCount() {
    let taskOne = {};
    taskOne.businessPhone = '17153487081';
    taskOne.password = 'liuyi123';
}

async function tc_post() {
    let taskOne = {};
    taskOne.businessPhone = '17095467483';
    taskOne.password = 'Aa123456';
    taskOne.projectId = 23236;
    taskOne.taskId = 139;
    taskOne.platformId = 4;
    taskOne.configPlatformId = 14;
    taskOne.brokerName = '永红';
    taskOne.type = 1;
    taskOne.title = '奉贤青村100000平仓库厂房出租 可分租 丙二类 有月台 ';
    taskOne.description = null
    await TC.post(taskOne);
}

/*
{
	"id": 280,
	"name": "青浦",
	"title": "青浦全单层2500平出租 5吨行车、交通便利",
	"status": 1,
	"createUser": 156,
	"updateUser": 156,
	"createTime": "2020-09-15 14:01:53",
	"updateTime": "2020-09-15 14:01:53",
	"projectId": 92863,
	"usageType": 2,
	"provinceCode": "310000",
	"cityCode": "310100",
	"districtCode": "310118",
	"market": 16780,
	"configPlatformId": 15
}
 */

async function cfw_post() {
    let taskOne = {};
    taskOne.businessPhone = '17172189005';
    taskOne.password = 'toudengcang123';
    taskOne.projectId = 92863;
    taskOne.taskId = 3422;
    taskOne.platformId = 3;
    taskOne.configPlatformId = 15;
    taskOne.brokerName = '军林';
    taskOne.type = 1;
    taskOne.title = '青浦全单层2500平出租 5吨行车、交通便利';
    taskOne.description = null;
    await CFW.post(taskOne);
}

async function test_task_one_api() {
    let res = await Api.method.taskOne(3, 0).catch(reason => util.writeLog(reason));
    console.log(res);
    res = await Api.method.taskOne(3, 1).catch(reason => util.writeLog(reason));
    console.log(res);
    res = await Api.method.taskOne(3, 2).catch(reason => util.writeLog(reason));
    console.log(res);
}

function test_() {
    let a = "靠近春林路与民益路交叉口\t";
}

tc_post().then(e => console.log(e));
