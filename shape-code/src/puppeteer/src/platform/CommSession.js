const util = require('../base/utils');
const enums = require('../base/enum');
const _static = require('../base/static');
const Result = require('../models/Result');
const fs = require('fs')
const Api = require('../base/api');

async function cookieLogin(_this) {
    if (util._obj.isEmpty(_this.cookies)) {
        return false;
    }
    const page = await _this.browser.newPage();
    await page.goto(_this.postCf, {waitUntil: 'networkidle0'});
    try {
        await page.waitFor(_static.oneT);
        // 本地测试使用
        // let loginCookie = JSON.parse(fs.readFileSync(_this.cookieFile));
        let loginCookie = JSON.parse(_this.cookies);
        if (0 > loginCookie.length) {
            await page.close();
            return false;
        }
        await page.waitFor(_static.oneT);
        let loginRes = await tryCookieLogin(loginCookie, page, _this);
        if (loginRes) {
            return true;
        }
        // 失败尝试一次
        await page.waitFor(_static.oneT);
        return await tryCookieLogin(loginCookie, page, _this);
    } catch (e) {
        return false;
    }
}

async function tryCookieLogin(loginCookie, page, _this) {
    let loginRes = false;
    try {
        loginRes = await tryLogin(loginCookie, page, _this);
    } catch (e) {
        util.writeLog('login error...' + e)
    }
    return loginRes
}

async function tryLogin(loginCookie, page, _this) {
    loginCookie.forEach(await function (cookie) {
        cookie.expirationDate = Number(cookie.expirationDate);
        page.setCookie(cookie);
    });
    await page.waitFor(_static.threeT);
    await page.goto(_this.postCf, {waitUntil: 'networkidle0'});
    await page.waitFor(_static.threeT);
    if (page.url() !== _this.postCf) {
        await page.close();
        return false;
    }
    util.tcLog('Cookie login success');
    return true;
}

function getPageCount(total, pageSize = 10) {
    if (total % pageSize > 0) {
        return (total / pageSize) + 1;
    }
    return total / pageSize;
}

function init(_this, task, config) {
    // 账号管理人
    _this.brokerName = task.brokerName;
    _this.username = task.businessPhone;
    _this.password = task.password;
    _this.cookies = task.cookie;
    _this.projectId = task.projectId;
    _this.taskId = task.taskId;
    // 账号平台关联ID
    _this.configPlatformId = task.configPlatformId;
    // 自定义标题
    _this.title = task.title;
    // 自定义描述
    _this.description = task.description;
    // 自定义类型
    _this.useType = task.useType;
    // 自定义区域
    _this.location = {
        province: task.province,
        city: task.city,
        district: task.district,
        market: task.market
    };
    // 自定义面积
    _this.area = task.area;
    // 自定义图片
    _this.photoUrls = task.photoUrls;
    // 刷新类型
    _this.refreshType = task.refreshType;
    // 刷新时间
    _this.refreshTime = task.refreshTime;
    // 发布厂房链接
    _this.postCf = config.urls.postCf;
    // 发布仓库链接
    _this.postCk = config.urls.postCk;
    // 登录成功的界面
    _this.loginSuccess = config.urls.loginSuccess;
    // 平台登录链接
    _this.loginUrl = config.urls.login;
    // 厂房成功界面
    _this.cfSuccessUrl = config.urls.cfSuccessUrl;
    // 仓库成功界面
    _this.ckSuccessUrl = config.urls.ckSuccessUrl;
    // 仓库列表页
    _this.ckListUrl = config.urls.ckListUrl;
    // 厂房列表页
    _this.cfListUrl = config.urls.cfListUrl;
    // cookie文件
    _this.cookieFile = `cookies/${_this.username}.json`;
    // 验证码
    _this.codeFile = `cookies/${_this.username}-code.png`;
    // 日志
    _this.logPath = `log/${_this.username}.txt`;
    // 登录页选择器
    _this.loginSel = config.loginSel;
    // 城市选择器 - cfw
    _this.citySel = config.citySel;
}

function checkProject(project) {
    if (util._obj.isEmpty(project)) {
        return new Result(enums.POST_STATUS.done, _static.noProject);
    }
    if (util._coll.isEmpty(project.oneRental)) {
        return new Result(enums.POST_STATUS.done, _static.fullProject);
    }
    if (util._coll.isEmpty(project.floorList)) {
        return new Result(enums.POST_STATUS.done, _static.floorEmpty);
    }
    if (util._coll.isEmpty(project.useType)) {
        return new Result(enums.POST_STATUS.done, _static.useTypeEmpty);
    }
    return null;
}

function getNames(types) {
    if (util._coll.isEmpty(types)) {
        return [];
    }
    let names = [];
    types.forEach(function (type) {
        names.push(type.name);
    });
    return names;
}

function getPhotoUrls(photoUrls) {
    if (util._coll.isEmpty(photoUrls)) {
        return [];
    }
    if (photoUrls.length <= 8) {
        return photoUrls;
    }
    let urls = [];
    for (let i = 0; i < 8; i++) {
        urls.push(photoUrls[i]);
    }
    return urls;
}

async function yzmLogin(_this) {
    try {
        let page = await _this.browser.newPage();
        await page.goto(_this.loginUrl, {waitUntil: 'networkidle0'});
        await oneStepT(page, _this.loginSel.username, _this.username);
        await oneStepT(page, _this.loginSel.password, _this.password);
        let yzm = await obtainYZM(page, _this);
        if (yzm == null) {
            return false;
        }
        await oneStepT(page, _this.loginSel.captcha, yzm);
        await oneStepC(page, _this.loginSel.submit);

        return await getLoginResult(page, _this);
    } catch (e) {
        return false;
    }
}

async function getLoginResult(page, _this) {
    await page.waitFor(_static.threeT);
    if (page.url() === _this.loginSuccess) {
        return true;
    }
    await page.waitFor(_static.threeT);
    return page.url() === _this.loginSuccess;
}

async function collectYzmPhoto(page, _this) {
    //调用evaluate 方法返回id 为form元素的位置信息
    let clip = await page.evaluate((yzmId) => {
        let {
            x, y, width, height
        } = document.getElementById(yzmId).getBoundingClientRect();
        return {x, y, width, height};
    }, _this.loginSel.yzmPhoto);
    // 官方说明 ：截屏 这个过程可能需要1~6秒
    await page.screenshot({
        path: _this.codeFile,
        type: 'jpeg',
        quality: 80,
        clip: clip,
    });
}

async function obtainYZM(page, _this) {
    await collectYzmPhoto(page, _this);
    await page.waitFor(_static.threeT);
    return await Api.method.yzmCode(_this.codeFile);
}

async function testLogin(_this) {
    let page = await _this.browser.newPage();
    await page.goto(_this.loginUrl, {waitUntil: 'networkidle0'});
    await oneStepT(page, _this.loginSel.username, _this.username);
    await oneStepT(page, _this.loginSel.password, _this.password);
    await page.waitFor(_static.nineT);
}

async function login(_this) {
    let page = await _this.browser.newPage();
    await page.goto(_this.loginUrl, {waitUntil: 'networkidle0'});
    await oneStepT(page, _this.loginSel.username, _this.username);
    await oneStepT(page, _this.loginSel.password, _this.password);
    await oneStepC(page, _this.loginSel.submit);
    await page.waitFor(_static.threeT);
    return page.url() === _this.loginSuccess;
}

function collectCookie() {
    // 当浏览器的 页面链接发生变化时
    // this.browser.on('targetchanged', async ()=>{
    //     let cookies = await page.cookies();
    //     fs.writeFileSync(_this.cookieFile, JSON.stringify(cookies));
    //     await Api.method.updateCookie(_this.configPlatformId, JSON.stringify(cookies));
    //     await _this.close();
    // });
}

async function oneStepC(page, select, time = _static.oneT) {
    await page.waitFor(time);
    await page.click(select);
}

async function oneStepT(page, select, text, time = 0) {
    await page.waitFor(time);
    await page.type(select, text + '');
}

async function getText(page, select, time = _static.sixty) {
    await page.waitFor(time);
    let text = await page.$$eval(select, getInnerText());
    return text.toString();
}

function childNode(selector, nth, suffix = '') {
    return selector + ':nth-child(' + nth + ')' + suffix;
}

async function mouseExecutor(page, key, count) {
    for (let j = 0; j < count; j++) {
        await page.keyboard.press(key);
    }
}

async function keyboardCopy(page, selector) {
    await oneStepC(page, selector);
    await page.keyboard.down('Control');
    await page.keyboard.press('KeyA');
    await page.keyboard.up('Control');
    await page.keyboard.down('Control');
    await page.keyboard.press('KeyC');
    await page.keyboard.up('Control');
    await page.waitFor(_static.oneT);
    await mouseExecutor(page, 'Backspace', 1);
    await page.waitFor(_static.oneT);
    await page.keyboard.down('Control');
    await page.keyboard.press('KeyV');
    await page.keyboard.up('Control');
}

function getInnerText() {
    return elements => elements.map(r => r.innerText)
}

async function upPhoto(page, urls, projectId, eleSel, photoError = null) {
    let photos = await Api.method.fetchPhoto(urls, projectId, photoError)
        .catch(reason => util.writeLog(reason));

    let upCount = photos.length > 8 ? 8 : photos.length;
    for (let pIx = 0; pIx < upCount; pIx++) {
        const input = await page.$(eleSel);
        await input.uploadFile(photos[pIx]);
        if (photoError != null) {
            try {
                await oneStepC(page, photoError.remove, _static.twoT);
            } catch (e) {
            }
        }
        await page.waitFor(_static.fiveT);
    }
}

function checkType(taskOne) {
    return util._obj.isEmpty(taskOne) || util._obj.isEmpty(taskOne.type);
}

function checkRefreshTask(_this) {
    return util._obj.isEmpty(_this)
        || util._obj.isEmpty(_this.refreshType) || util._obj.isEmpty(_this.refreshTime)
}

async function clickOther(page) {
    try {
        await page.evaluate(() => document.querySelector('#pps').click());
    } catch (e) {

    }
}

async function undone(session, taskId, text, e = null) {
    try {
        if (util._obj.notEmpty(e)) {
            util.logTOPath(e, session.logPath);
        }
        await session.close();
        await Api.method.updateTask(taskId, enums.POST_STATUS.undone, text);
    } catch (e) {
        await session.close();
    }
}

async function commExecutor(session, yzm = true, post = true) {
    try {
        let loginRes = yzm ? await session.yzmLogin() : await session.login();
        if (util.isFalse(loginRes)) {
            await undone(session, session.taskId, _static.loginError);
            return;
        }
        let res = post ? await session.post() : await session.refresh();
        if (res.code !== enums.POST_STATUS.done) {
            res = post ? await session.post() : await session.refresh();
        }
        await session.close();
        await Api.method.updateTask(session.taskId, res.code, res.msg);
    } catch (e) {
        await undone(session, session.taskId, _static.exeException, e);
    }
}

async function sendException(taskId) {
    await Api.method.updateTask(taskId, enums.POST_STATUS.undone, _static.sessionException);
}

module.exports = {
    init,
    checkProject,
    cookieLogin,
    getNames,
    getPhotoUrls,
    upPhoto,
    yzmLogin,
    obtainYZM,
    oneStepC,
    oneStepT,
    testLogin,
    getPageCount,
    getText,
    checkType,
    checkRefreshTask,
    clickOther,
    login,
    undone,
    childNode,
    mouseExecutor,
    commExecutor,
    sendException,
    keyboardCopy
};