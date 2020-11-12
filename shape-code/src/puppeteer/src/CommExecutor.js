const util = require('./base/utils');

function illegalTask(task) {
    return util._obj.isEmpty(task)
        || util._obj.isEmpty(task.platformId)
        || util._obj.isEmpty(task.businessPhone)
        || util._obj.isEmpty(task.password)
        || util._obj.isEmpty(task.type);
}

function postStartLog(taskOne) {
    util.writeLog("\n开始发送平台 ：" + taskOne.platformId
        + " \n账号 ：" + taskOne.businessPhone
        + "\nTaskId ：" + taskOne.taskId);
}


module.exports = {
    illegalTask,
    postStartLog,
}