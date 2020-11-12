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

const enums = require('./base/enum');
const Api = require('./base/api');
const util = require('./base/utils');
const CronJob = require('cron').CronJob;
const TC = require('./platform/TongCheng/tc');

async function main() {
    let accounts = await Api.method.queryByPlatform(enums.PLATFORM.TongCheng);
    if (util._coll.isEmpty(accounts)) {
        return;
    }
    for (let account of accounts) {
        // 先下架
        let dropRes = await Api.method.drop(account.configPlatformId);
        if (util._obj.isEmpty(dropRes)) {
            continue;
        }
        // 再爬取上架数据
        let result = await TC.grabOnShelf(account);
        if (util.isFalse(result)) {
            await TC.grabOnShelf(account)
        }
    }
}

new CronJob('0 0 1 * * *', () => main(), null, true, 'Asia/Shanghai');