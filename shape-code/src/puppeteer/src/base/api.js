const rp = require('request-promise-native');
const _path = require('./path');
const util = require('./utils');
const fs = require('fs');
const request = require('request');

const api = {
    catPostTask: _path.promotion + '/promotion/autoPost/catPostTask.do',
    monitor: _path.promotion + '/promotion/tcPost/cat58Post.do',
    postTask: _path.promotion + '/promotion/autoPost/seekingRemainderGetTask.do',
    taskOne: _path.promotion + '/promotion/autoPost/onePostTask.do',
    isLock: _path.park + 'auto/post/isLock?postNo=',
    updatePromote: _path.park + 'auto/post/updatePromote',
    savePromoteRecord: _path.park + 'auto/post/savePromoteRecord',
    queryByPlatform: _path.park + 'auto/post/queryByPlatform?platform=',
    drop: _path.park + 'auto/post/drop?configPlatformId=',
    updateTask: _path.park + 'auto/post/updateTask',
    updateCookie: _path.park + 'auto/post/updateCookie',
    invalidLogin: _path.park + 'auto/post/invalidLogin?taskId=',
    postAccounts: _path.park + 'auto/post/postAccounts',
    onePostAccount: _path.park + 'auto/post/onePostAccount?id=',
    yzmCode: _path.park + 'auto/post/yzmCode',
    project: _path.site + 'project/detailContent?projectId=',
    image: _path.pms + 'resource/getOriginalUrl.do?url=',
    description: _path.promotion + 'post/58/detail.do?projectId=',
    newProject: _path.promotion + '/promotion/autoPost/project.do?projectId=',
    storageTitle: _path.promotion + 'post/58/cangku/title.do?projectId=',
    productTitle: _path.promotion + 'post/58/changfang/title.do?projectId=',
    imageKnow: _path.promotion + 'post/recognize.do',
};

/**
 * 发帖配置数据
 * @returns {Promise<null|*>}
 */
const taskOne = async function () {
    let res = await rp.get({uri: api.taskOne, json: true});
    if (util._obj.isEmpty(res) || res.code !== '0') {
        return null
    }
    return res.data
}

const queryTask = async function (remainder, mod) {
    const param = {remainder, mod};
    let res = await rp.post({uri: api.postTask, qs: param, json: true});
    if (util._obj.isEmpty(res) || res.code !== '0') {
        return null
    }
    return res.data
}

const catPostTask = async function () {
    try {
        await rp.get({uri: api.catPostTask, json: true});
    } catch (e) {

    }
}

const monitor = async function () {
    try {
        await rp.get({uri: api.monitor, json: true});
    } catch (e) {

    }
}

const isLock = async function (postNo) {
    let res = await rp.get({uri: api.isLock + postNo, json: true});
    if (util._obj.isEmpty(res) || res.code !== '0') {
        return true;
    }
    return res.data
}

const updatePromote = async function (postNo, onlineStatus) {
    const param = {postNo, onlineStatus};
    let res = await rp.post({uri: api.updatePromote, qs: param, json: true});
    if (util._obj.isEmpty(res) || res.code !== '0') {
        return null;
    }
    return res.data
}

const savePromoteRecord = async function (taskId, configPlatformId, postNo, onlineStatus, title = null) {
    const param = {taskId, configPlatformId, postNo, onlineStatus, title};
    let res = await rp.post({uri: api.savePromoteRecord, qs: param, json: true});
    if (util._obj.isEmpty(res) || res.code !== '0') {
        return null;
    }
    return res.data
}


const drop = async function (configPlatformId) {
    let res = await rp.get({uri: api.drop + configPlatformId, json: true});
    if (util._obj.isEmpty(res) || res.code !== '0') {
        return null;
    }
    return res;
}

const queryByPlatform = async function (platform) {
    let res = await rp.get({uri: api.queryByPlatform + platform, json: true});
    if (util._obj.isEmpty(res) || res.code !== '0') {
        return null;
    }
    return res.data
}

const getOneAccount = async function (id) {
    let res = await rp.get({uri: api.onePostAccount + id, json: true});
    if (util._obj.isEmpty(res) || res.code !== '0') {
        return null
    }
    return res.data;
}

const collectCookieAccount = async function () {
    let res = await rp.get({uri: api.postAccounts, json: true})
    if (util._obj.isEmpty(res) || res.code !== '0') {
        return [];
    }
    return res.data;
}

const updateCookie = async function (id, cookie) {
    const param = {id, cookie};
    return rp.post({uri: api.updateCookie, qs: param, json: true});
}

const invalidLogin = async function (taskId) {
    return rp.post({uri: api.invalidLogin + taskId, json: true});
}

const updateTask = async function (id, postStatus, lastPostResult) {
    const param = {id, postStatus, lastPostResult};
    return rp.get({uri: api.updateTask, qs: param, json: true})
}

const description = async function (projectId) {
    let res = await rp.get({uri: api.description + projectId, json: true});
    if (util._obj.isEmpty(res) || res.code !== '0') {
        return null
    }
    return res.data.detail;
}

const queryProject = async function (projectId) {
    let res = await rp.get({uri: api.project + projectId, json: true})
    if (util._obj.isEmpty(res) || res.code !== '0') {
        return null
    }
    return res.data.projectVo;
}

const newProject = async function (projectId) {
    let res = await rp.get({uri: api.newProject + projectId, json: true});
    if (util._obj.isEmpty(res) || res.code !== '0') {
        return null
    }
    return res.data;
}

const storageTitle = async function (projectId) {
    let res = await rp.get({uri: api.storageTitle + projectId, json: true})
    if (util._obj.isEmpty(res) || res.code !== '0') {
        return ''
    }
    return res.data;
}

const productTitle = async function (projectId) {
    let res = await rp.get({uri: api.productTitle + projectId, json: true});
    if (util._obj.isEmpty(res) || res.code !== '0') {
        return ''
    }
    return res.data;
}

const fetchPhoto = async function (urls, projectId, originalPicture = null) {
    if (!fs.existsSync('temp/' + projectId)) {
        fs.mkdirSync('temp/' + projectId);
    }
    if (!urls) {
        return []
    }
    let realUrls = urls.map(url => {
        return {
            filename: 'temp/' + projectId + '/' + url.replace('https://image.toodc.cn/', ''),
            url: originalPicture == null ? (url + '-vipSearch.big4') : url,
        }
    });

    let files = []
    for (let i = 0; i < realUrls.length; i++) {
        if (fs.existsSync(realUrls[i].filename)) {
            files.push(realUrls[i].filename);
            continue
        }

        let imageUrl = await rp.get({uri: api.image + realUrls[i].url, json: true})
            .catch(reason => util.tcLog(reason));

        if (!imageUrl || imageUrl.code !== '0') {
            continue
        }

        let file = null;
        try {
            file = await new Promise((resolve, reject) => {
                request(imageUrl.data)
                    .pipe(fs.createWriteStream(realUrls[i].filename))
                    .on('close', () => resolve(realUrls[i].filename))
            }).catch(reason => util.tcLog(reason));
        } catch (e) {
            util.tcLog(e)
        }

        if (file) {
            files.push(file)
        }
    }
    return files
};

const yzmCode = async function (filepath) {
    if (!filepath) {
        return null;
    }
    const formData = {
        multipartFile: fs.createReadStream(filepath),
    };
    const result = await rp({
        method: 'POST',
        uri: api.yzmCode,
        formData: formData,
        json: true
    });
    if (!result || result.code !== '0') {
        util.writeLog('yzm get error');
        return '1234';
    }
    return result.data
}

module.exports = {
    method: {
        taskOne,
        updateTask,
        updateCookie,
        invalidLogin,
        description,
        queryProject,
        storageTitle,
        productTitle,
        fetchPhoto,
        getOneAccount,
        collectCookieAccount,
        newProject,
        yzmCode,
        isLock,
        updatePromote,
        savePromoteRecord,
        queryByPlatform,
        drop,
        queryTask,
        monitor,
        catPostTask
    },
};