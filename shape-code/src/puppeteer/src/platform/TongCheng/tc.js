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
}

async function post(taskOne) {
    try {
        const session = await new Session(taskOne, config.tc, true); // true 为不打开浏览器
        await CommSession.commExecutor(session, false);
    } catch (e) {
        await CommSession.sendException(taskOne.taskId);
    }
}

async function grabOnShelf(taskOne) {
    try {
        const session = await new Session(taskOne, config.tc, true); // true 为不打开浏览器
        try {
            let loginRes = await session.login();
            if (util.isFalse(loginRes)) {
                await session.close();
                return false;
            }
            let res = await session.grab();
            await session.close();
            return res;
        } catch (e) {
            await session.close();
            return false;
        }
    } catch (e) {
        util.logTOPath(e, 'grabLog.txt');
        return false;
    }
}

module.exports = {
    post,
    executor,
    grabOnShelf,
};