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
const sleep = require('sleep');
const util = require('./base/utils');
const _static = require('./base/static');
const CommExecutor = require('./CommExecutor');
const LC = require('./platform/LianCang/LianCang');
const ZGZS = require('./platform/zgzs/zgzs');
const CFW = require('./platform/cfw/cfw');
const TC = require('./platform/TongCheng/tc');
const parseArgs = require('minimist');

async function main() {
    for (let i = 0; i > -1;) {
        try {
            const task = await Api.method.taskOne().catch(reason => util.writeLog(reason));
            if (CommExecutor.illegalTask(task)) {
                util.logTOPath('no task wait...', 'exeLog.txt');
                sleep.sleep(_static.sixty);
                continue;
            }
            CommExecutor.postStartLog(task);

            await matchPlatform(task);

        } catch (e) {
            util.logTOPath('match platform exception ' + e, 'exeLog.txt');
            sleep.sleep(_static.nine);
        }
    }
}

async function matchPlatform(task) {
    if (task.platformId === enums.PLATFORM.LianCang) {
        await LC.executor(task).catch(e => util.writeLog(e));
    }
    if (task.platformId === enums.PLATFORM.zgzs) {
        await ZGZS.executor(task).catch(e => util.writeLog(e));
    }
    if (task.platformId === enums.PLATFORM.cfw) {
        await CFW.executor(task).catch(e => util.writeLog(e));
    }
    if (task.platformId === enums.PLATFORM.TongCheng) {
        await TC.executor(task).catch(e => util.writeLog(e));
    }
}


async function postOne() {
    const task = await Api.method.taskOne().catch(reason => util.tcLog(reason));
    if (CommExecutor.illegalTask(task)) {
        return;
    }
    try {
        await matchPlatform(task);
    } catch (e) {
        util.writeLog(JSON.stringify(task) + ' | ' + e);
    }
}

const args = parseArgs(process.argv);

if (args['one']) {
    postOne().then(r => util.tcLog(r));
}
// main().then(r => util.writeLog(r));






