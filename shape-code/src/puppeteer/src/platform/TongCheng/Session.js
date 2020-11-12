const puppeteer = require('puppeteer');
const Api = require('../../base/api');
const util = require('../../base/utils');
const enums = require('../../base/enum');
const _static = require('../../base/static');
const Customize = require('../../models/Customize');
const Project = require('../../models/Project');
const Result = require('../../models/Result');
const CommSession = require('../CommSession');
const Selector = require('./Selector');
const fs = require('fs');

class Session {

    constructor(acconut, config, headless = false) {
        return new Promise(async (resolve, reject) => {
            CommSession.init(this, acconut, config);
            this.browser = await puppeteer.launch({
                args: ['--no-sandbox', '--disable-setuid-sandbox',
                    '--disable-gpu', '--disable-dev-shm-usage',
                    '--no-first-run', '--no-zygote'],
                headless: headless,
                slowMo: 20,
                defaultViewport: {
                    width: 1466,
                    height: 800
                }
            }).catch(reason => reject(reason));
            return resolve(this);
        })
    }

    async close() {
        await this.browser.close()
    }

    async login() {
        for (let count = 0; count < 5; count++) {
            let loginRes = await this.tryLogin();
            if (util.isTrue(loginRes)) {
                return true;
            }
        }
        return false;
    }

    async tryLogin() {
        try {
            let page = await this.browser.newPage();
            if (fs.existsSync(this.cookieFile)) {
                await this.setCookies(page);
            }

            await page.waitFor(_static.oneT);
            // domContentLoaded 一般表示 DOM 和 CSSOM 均准备就绪的时间点
            await page.goto(this.ckListUrl, {waitUntil: 'domcontentloaded'});
            await page.waitFor(_static.threeT);
            if (page.url() === this.ckListUrl) {
                return true;
            }
            await this.loginByPassword(page);
            await page.waitFor(_static.twoT);

            await page.goto(this.ckListUrl, {waitUntil: 'networkidle2'});
            await page.waitFor(_static.twoT);
            if (page.url() === this.ckListUrl) {
                let cookies = await page.cookies();
                fs.writeFileSync(this.cookieFile, JSON.stringify(cookies));
                return true;
            }
            return false;
        } catch (e) {
            return false;
        }
    }

    async loginByPassword(page) {
        await page.goto(this.loginUrl, {waitUntil: 'domcontentloaded'});
        await CommSession.oneStepC(page, 'span.login-switch.bar-code.iconfont');
        await CommSession.oneStepT(page, this.loginSel.username, this.username, _static.oneT);
        await CommSession.oneStepT(page, this.loginSel.password, this.password);
        await CommSession.oneStepC(page, this.loginSel.submit);
    }

    async setCookies(page) {
        let loginCookie;
        try {
            loginCookie = JSON.parse(fs.readFileSync(this.cookieFile));
            if (loginCookie.length > 0) {
                for (let i = 0; i < loginCookie.length; i++) {
                    await page.setCookie(loginCookie[i])
                }
                util.tcLog('Cookie loaded')
            }
        } catch (e) {
            util.tcLog('load cookie failed');
        }
    }

    async post() {
        try {
            const page = await this.browser.newPage();
            // 自定义属性
            const customize = await new Customize(this);
            // 项目信息
            const project = await new Project(this.projectId, customize);
            // 检查
            let checkResult = CommSession.checkProject(project);
            if (checkResult != null) {
                return checkResult;
            }
            let selector = new Selector();
            let houseId = await this.postTask(page, selector, project);
            if (util._obj.isEmpty(houseId)) {
                return new Result(enums.POST_STATUS.done, _static.submitError);
            }
            return await this.toPromote(page, selector, houseId);
        } catch (e) {
            util.logTOPath(e, this.logPath);
            return new Result(enums.POST_STATUS.undone, _static.sendException);
        }
    }

    async postTask(page, selector, project) {
        await page.goto(this.postCk, {waitUntil: 'networkidle0'});
        await page.waitFor(_static.threeT);
        // 类型
        if (project.useType === enums.USE_TYPE.cangku) {
            await this.clickOption(page, selector.useType, '仓库')
        }
        // 租赁方式
        await CommSession.oneStepC(page, selector.leaseWay.some);
        // 编号
        await CommSession.oneStepT(page, selector.projectId, project.id);
        // 板块
        await this.selectRegion(page, selector.district, project.district);
        await this.selectRegion(page, selector.market, project.market);
        // 地址
        await this.selectAddress(page, selector.address, project);
        // 面积
        await CommSession.oneStepT(page, selector.area, project.area);
        // 楼层
        await this.clickOption(page, selector.floor, (project.maxFloor > 1 ? '多层' : '单层'));
        // 层高
        await CommSession.oneStepT(page, selector.floorH, project.maxFloorH);
        // 配套
        await CommSession.oneStepC(page, CommSession.childNode(selector.facility, 4, '> input'));
        await CommSession.oneStepC(page, CommSession.childNode(selector.facility, 5, '> input'));
        // 房源标签
        await this.selectTag(page, selector.tag);
        // 租金
        await CommSession.oneStepT(page, selector.price, util._default(project.price, 1.0));
        // 起租期
        await CommSession.oneStepT(page, selector.leaseTerm, project.leaseTerm);
        // 支付方式
        await CommSession.oneStepT(page, selector.payWay.pay, 1);
        await CommSession.oneStepT(page, selector.payWay.bet, 1);
        // 标题
        await CommSession.oneStepT(page, selector.title, project.title);
        // 描述
        await this.entryDesc(page, selector, project.desc);
        // 图片
        let urls = CommSession.getPhotoUrls(project.photoUrls);
        await CommSession.upPhoto(page, urls, project.id, selector.photo, selector.photoError);
        // 提交
        await CommSession.oneStepC(page, selector.submit);

        return this.submit(page);
    }

    async entryDesc(page, selector, desc) {
        if (await page.$(selector.desc.iframe)) {
            await CommSession.oneStepT(page, selector.desc.iframe, desc)
        } else {
            await CommSession.oneStepT(page, selector.desc.text, desc)
        }
    }

    async selectTag(page, tagSelector) {
        await CommSession.oneStepC(page, CommSession.childNode(tagSelector, 2, '> input'));
        await CommSession.oneStepC(page, CommSession.childNode(tagSelector, 3, '> input'));
        await CommSession.oneStepC(page, CommSession.childNode(tagSelector, 5, '> input'));
        await CommSession.oneStepC(page, CommSession.childNode(tagSelector, 6, '> input'));
    }

    // 推广
    async toPromote(page, selector, houseId) {
        try {
            await page.waitFor(_static.oneT);
            await page.goto(this.ckListUrl, {waitUntil: 'networkidle0'});
            await page.waitFor(_static.twoT);

            await this.removePrompt(page, selector);
            let canPromoteCount = await CommSession.getText(page, selector.promote.canPromote);
            if (canPromoteCount > 0) {
                return await this.goOnShelf(page, selector, houseId);
            }
            return await this.doOnShelf(page, selector, houseId);
        } catch (e) {
            util.logTOPath(_static.promoteException + e, this.logPath);
            return new Result(enums.POST_STATUS.undone, _static.promoteException);
        }
    }

    async doOnShelf(page, selector, houseId) {
        let result = await this.goOffShelf(page, selector);
        if (result.msg !== _static.sendSuccess) {
            return result;
        }
        await page.goto(this.ckListUrl, {waitUntil: 'networkidle0'});
        await this.removePrompt(page, selector);
        await page.waitFor(_static.twoT);
        return await this.goOnShelf(page, selector, houseId);
    }

    // 上架
    async goOnShelf(page, selector, houseId) {
        await this.queryNoPromote(page, selector);

        let pageList = await page.$$(selector.page);
        let pageCount = pageList.length - 1 > 3 ? 3 : pageList.length - 1;

        for (let pageNo = 1; pageNo <= pageCount; pageNo++) {
            let pageSize = await this.getPageSize(page, selector, pageNo);
            for (let tr = 1; tr <= pageSize; tr++) {
                let trHouseId = await this.getHouseId(page, selector, tr);
                if (trHouseId.toString() === houseId.toString()) {
                    await this.pitchOnRecord(page, selector, tr);
                    return await this.onShelfConfirm(page, selector.on.success, trHouseId);
                }
            }
        }
        await Api.method.savePromoteRecord(this.taskId, this.configPlatformId, houseId, ONLINE_STATUS.OFFLINE);
        return new Result(enums.POST_STATUS.done, _static.onShelfError);
    }

    async pitchOnRecord(page, selector, tr) {
        await CommSession.oneStepC(page,
            CommSession.childNode(selector.checkbox.prefix, tr, selector.checkbox.suffix));
        await CommSession.oneStepC(page, selector.batchOn);
        await CommSession.oneStepC(page, selector.on.box, _static.twoT);
        await CommSession.oneStepC(page, selector.on.submit);
    }

    // 下架
    async goOffShelf(page, selector) {
        await this.queryPromote(page, selector);
        let promotingCount = await CommSession.getText(page, selector.promote.promoting);
        let pageCount = CommSession.getPageCount(promotingCount, 20);

        for (let pageNo = 1; pageNo <= pageCount; pageNo++) {
            let pageSize = await this.getPageSize(page, selector, pageNo);
            for (let tr = 1; tr <= pageSize; tr++) {
                let houseId = await this.getHouseId(page, selector, tr);
                if (util.isFalse(await this.isLock(houseId))) {
                    await CommSession.oneStepC(page,
                        CommSession.childNode(selector.checkbox.prefix, tr, selector.checkbox.suffix));
                    await CommSession.oneStepC(page, selector.batchOff);
                    return await this.OffShelfConfirm(page, selector.off.success, houseId);
                }
            }
        }
        return new Result(enums.POST_STATUS.done, _static.offShelfEmpty);
    }

    // 下架确认
    async OffShelfConfirm(page, success, houseId) {
        await page.waitFor(_static.oneT);
        if (await page.$(success)) {
            await Api.method.updatePromote(houseId, 0);
            return new Result(enums.POST_STATUS.done, _static.sendSuccess);
        }
        await page.waitFor(_static.twoT);
        if (await page.$(success)) {
            await Api.method.updatePromote(houseId, 0);
            return new Result(enums.POST_STATUS.done, _static.sendSuccess);
        }
        return new Result(enums.POST_STATUS.done, _static.offShelfError);
    }

    // 上架确认
    async onShelfConfirm(page, success, houseId) {
        await page.waitFor(_static.oneT);
        if (await page.$(success)) {
            await Api.method.savePromoteRecord(this.taskId, this.configPlatformId, houseId, enums.ONLINE_STATUS.ONLINE);
            return new Result(enums.POST_STATUS.done, _static.sendSuccess);
        }
        await page.waitFor(_static.twoT);
        if (await page.$(success)) {
            await Api.method.savePromoteRecord(this.taskId, this.configPlatformId, houseId, enums.ONLINE_STATUS.ONLINE);
            return new Result(enums.POST_STATUS.done, _static.sendSuccess);
        }
        await Api.method.savePromoteRecord(this.taskId, this.configPlatformId, houseId, enums.ONLINE_STATUS.OFFLINE);
        return new Result(enums.POST_STATUS.done, _static.onShelfError);
    }

    async queryPromote(page, selector) {
        await this.clickOption(page, selector.port, '58推广');
        await CommSession.oneStepC(page, selector.query);
        await CommSession.oneStepC(page, selector.sort, _static.twoT);
        await page.waitFor(_static.twoT);
    }

    async queryNoPromote(page, selector) {
        await this.clickOption(page, selector.port, '58未推广');
        await CommSession.oneStepC(page, selector.query);
        await page.waitFor(_static.twoT);
    }

    async isLock(houseId) {
        return await Api.method.isLock(houseId)
    }

    async getPageSize(page, selector, pageNo) {
        await CommSession.oneStepC(page, CommSession.childNode(selector.page, (pageNo === 1 ? 1 : pageNo + 1)));
        await page.waitFor(_static.twoT);
        let trList = await page.$$(selector.postNo.prefix);
        return trList.length;
    }

    async getHouseId(page, selector, tr) {
        let houseId = await CommSession.getText(page,
            CommSession.childNode(selector.postNo.prefix, tr, selector.postNo.suffix));
        await page.waitFor(_static.oneT);
        return houseId.replace('编号：', '').trim();
    }

    async submit(page) {
        await page.waitFor(_static.twoT);
        let url = await page.url();
        if (url.includes('houseId')) {
            return this.parseHouseId(url);
        }
        await page.waitFor(_static.twoT);
        url = await page.url();
        if (url.includes('houseId')) {
            return this.parseHouseId(url);
        }
        return null;
    }

    async clickOption(page, selector, type) {
        await CommSession.oneStepC(page, selector.click);
        await page.waitFor(_static.oneT);
        let options = await page.$$(selector.option);
        for (let pIx = 1; pIx <= options.length; pIx++) {
            let text = await CommSession.getText(page, CommSession.childNode(selector.option, pIx));
            if (text === type) {
                await CommSession.oneStepC(page, CommSession.childNode(selector.option, pIx));
                return;
            }
        }
    }

    async selectAddress(page, selector, project) {
        let addressList = await this.getAddressNodes(page, selector, project);

        for (let aIdx = 1; aIdx <= addressList.length; aIdx++) {
            let addressText = await CommSession.getText(page,
                CommSession.childNode(selector.option.prefix, aIdx, selector.option.suffix));
            if (!addressText.includes('地铁站')) {
                await CommSession.oneStepC(page,
                    CommSession.childNode(selector.option.prefix, aIdx, selector.option.suffix));
                return;
            }
        }
        await CommSession.oneStepC(page,
            CommSession.childNode(selector.option.prefix, 1, selector.option.suffix));
    }

    async getAddressNodes(page, selector, project) {
        let address = util._obj.isEmpty(project.mockAddress) ? project.fullAddress : project.mockAddress;
        await CommSession.oneStepT(page, selector.click, address, _static.oneT);
        let addressList;
        await CommSession.keyboardCopy(page, selector.click);
        addressList = await page.$$(selector.option.prefix);
        if (addressList.length < 1) {
            addressList = await this.copyAgain(page, selector, address, project);
        }
        return addressList;
    }

    async copyAgain(page, selector, address, project) {
        await CommSession.oneStepC(page, selector.click);
        await CommSession.mouseExecutor(page, 'Backspace', address.length + 1);
        await CommSession.oneStepT(page, selector.click, project.fullAddress);
        await CommSession.keyboardCopy(page, selector.click);
        return await page.$$(selector.option.prefix);
    }

    async selectRegion(page, selector, region) {
        let regions = await this.getRegionNodes(page, selector);
        if (util._coll.isEmpty(regions)) {
            return;
        }
        for (let pIx = 1; pIx <= regions.length; pIx++) {
            let optionText = await CommSession.getText(page, CommSession.childNode(selector.option, pIx));
            if (optionText.includes(region) || region.includes(optionText)) {
                await CommSession.oneStepC(page, CommSession.childNode(selector.option, pIx));
                return;
            }
        }
        try {
            await CommSession.oneStepC(page, CommSession.childNode(selector.option, 2));
        } catch (e) {
            await CommSession.oneStepC(page, CommSession.childNode(selector.option, 1));
        }
    }

    async getRegionNodes(page, selector) {
        await CommSession.oneStepC(page, selector.click);
        let regions = await page.$$(selector.option);
        await page.waitFor(_static.oneT);
        if (util._coll.isEmpty(regions)) {
            await CommSession.clickOther(page);
            await CommSession.oneStepC(page, selector.click);
            regions = await page.$$(selector.option);
        }
        return regions;
    }

    async grab() {
        try {
            const page = await this.browser.newPage();
            await page.goto(this.ckListUrl, {waitUntil: 'networkidle0'});
            await page.waitFor(_static.twoT);

            let selector = new Selector();
            await this.removePrompt(page, selector);
            await this.queryPromote(page, selector);

            return await this.grabHistoryRecord(page, selector);
        } catch (e) {
            util.logTOPath(e, 'grabLog.txt');
            return false;
        }
    }

    async grabHistoryRecord(page, selector) {
        let promotingCount = await CommSession.getText(page, selector.promote.promoting);
        let pageCount = CommSession.getPageCount(promotingCount, 20);
        for (let pageNo = 1; pageNo <= pageCount; pageNo++) {
            let pageSize = await this.getPageSize(page, selector, pageNo);
            for (let tr = 1; tr <= pageSize; tr++) {
                let houseId = await this.getHouseId(page, selector, tr);
                let title = await CommSession.getText(page,
                    CommSession.childNode(selector.trTitle.prefix, tr, selector.trTitle.suffix));
                await Api.method.savePromoteRecord(null, this.configPlatformId, houseId, enums.ONLINE_STATUS.ONLINE, title);
            }
        }
        return true;
    }

    // 移除列表页提示
    async removePrompt(page, prompt) {
        if (await page.$(prompt.dialog)) {
            await CommSession.oneStepC(page, prompt.remove);
            await page.waitFor(_static.twoT);
        }
    }

    parseHouseId(url) {
        return url.substring(url.lastIndexOf('houseId='), url.length)
            .replace('houseId=', '');
    }
}

module.exports = Session;