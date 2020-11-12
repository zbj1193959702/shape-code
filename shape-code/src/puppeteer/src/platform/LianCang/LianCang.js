const Session = require('./Session');
const CommSession = require('../CommSession');
const config = require('../../base/config');
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
        const session = await new Session(taskOne, config.lianCang, true); // true 为不打开浏览器
        await CommSession.commExecutor(session);
    } catch (e) {
        await CommSession.sendException(taskOne.taskId);
    }
}

async function refresh(taskOne) {

}

module.exports = {
    post,
    refresh,
    executor,
};