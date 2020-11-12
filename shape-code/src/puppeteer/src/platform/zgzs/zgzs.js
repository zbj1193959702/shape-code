const Session = require('./Session');
const CommSession = require('../CommSession');
const config = require('../../base/config');
const util = require('../../base/utils');
const enums = require('../../base/enum');

async function executor(taskOne) {
    if (CommSession.checkType(taskOne)) {
        return;
    }
    if (taskOne.type === enums.TASK_TYPE.POST) {
        await post(taskOne);
    }
    if (taskOne.type === enums.TASK_TYPE.REFRESH) {
        await refresh(taskOne);
    }
}

async function post(taskOne) {
    try {
        const session = await new Session(taskOne, config.zgzs, true); // true 不打开浏览器
        await CommSession.commExecutor(session);
    } catch (e) {
        await CommSession.sendException(taskOne.taskId);
    }
}

async function refresh(taskOne) {
    try {
        let session = await new Session(taskOne, config.zgzs, true);
        await CommSession.commExecutor(session, true, false);
    } catch (e) {
        await util.logTOPath(e, "exeLog.txt");
        await CommSession.sendException(taskOne.taskId);
    }
}

module.exports = {
    post,
    refresh,
    executor,
};
