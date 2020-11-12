const puppeteer = require('puppeteer');
const Api = require('../../base/api');
const util = require('../../base/utils');
const enums = require('../../base/enum');
const _static = require('../../base/static');
const Customize = require('../../models/Customize');
const Project = require('../../models/Project');
const Result = require('../../models/Result');
const CommSession = require('../CommSession');

class Session {

    constructor(acconut, config, headless = false) {
        return new Promise(async (resolve, reject) => {
            CommSession.init(this, acconut, config);
            this.browser = await puppeteer.launch({
                args: ['--no-sandbox', '--disable-setuid-sandbox'],
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
        let loginRes = await CommSession.login(this);
        if (util.isTrue(loginRes)) {
            return true;
        }
        return await CommSession.login(this);
    }

    async post() {
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
        try {
            if (project.useType === enums.USE_TYPE.cangku) {
                return await this.postToCK(page, project);
            }
            return await this.postToCF(page, project);
        } catch (e) {
            util.logTOPath(e, this.logPath);
            return new Result(enums.POST_STATUS.undone, _static.sendException);
        }
    }

    async postToCK(page, project) {
        await page.goto(this.postCk, {waitUntil: 'networkidle0'});
        let selector = ' #form1 > table > tbody > tr:nth-child(3) > td:nth-child(2) > a';
        await this.selectCity(page, project.city, selector);
        await page.goto(this.postCk, {waitUntil: 'networkidle0'});
        await page.waitFor(_static.twoT);
        // 面积
        await CommSession.oneStepT(page, '#mj', project.vacantArea);
        // 层高
        await CommSession.oneStepT(page, '#chengao', project.maxFloorH);
        // 层数
        await CommSession.oneStepT(page, '#chenshu', project.maxFloor);
        // 价格
        await CommSession.oneStepC(page, '#M');
        await page.keyboard.press('Backspace');
        await CommSession.oneStepT(page, '#M', project.price);
        // 厂房仓库公共信息
        await this.commInfo(page, project);
        // 判断结果
        await page.waitFor(_static.twoT);
        if (page.url().includes(this.ckSuccessUrl)) {
            return new Result(enums.POST_STATUS.done, _static.sendSuccess);
        }
        return new Result(enums.POST_STATUS.done, _static.submitError);
    }

    async postToCF(page, project) {
        await page.goto(this.postCf, {waitUntil: 'networkidle0'});
        let selector = '#form1 > table > tbody > tr:nth-child(3) > td > table > tbody > tr:nth-child(1) > td:nth-child(2) > a';
        await this.selectCity(page, project.city, selector);
        await page.goto(this.postCf, {waitUntil: 'networkidle0'});
        await page.waitFor(_static.twoT);
        // 价格
        await CommSession.oneStepC(page, '#M');
        await page.keyboard.press('Backspace');
        await CommSession.oneStepT(page, '#M', project.price);
        // 面积
        await CommSession.oneStepT(page, '#mj', project.vacantArea);
        // 建筑结构
        await this.checkBuildStructure(page, project.buildStructureList);
        // 独院
        await this.independentHouse(page, project.whAdvantageList);
        // 行车
        await page.click('#hangche1');
        // 电梯
        await this.elevator(page, project.whFacilityList);
        // 层高
        await CommSession.oneStepT(page, '#chengao', project.maxFloorH);
        // 层数
        await CommSession.oneStepT(page, '#chenshu', project.maxFloor);
        // 现配电
        await CommSession.oneStepT(page, '#peidian', util._default(project.kvaLeft, project.kva));
        // 可配电
        await CommSession.oneStepT(page, '#peidian2', this.getKva(project.kva));
        // 消防
        await this.selectFire(page, project.hasFire);
        // 适合行业
        await CommSession.oneStepT(page, '#hangye', util._default(project.suitableForIndustry, '仓储'));
        // 新旧程度
        await this.oldOrNew(page);
        // 产权
        await CommSession.oneStepC(page, '#chanquan1');
        // 厂房仓库公共信息
        await this.commInfo(page, project);
        // 判断结果
        await page.waitFor(_static.twoT);
        if (page.url().includes(this.cfSuccessUrl)) {
            return new Result(enums.POST_STATUS.done, _static.sendSuccess);
        }
        return new Result(enums.POST_STATUS.done, _static.submitError);
    }

    async commInfo(page, project) {
        // 区域
        await this.selectRegion(page, project.district,
            '#quyuSelect > select.prov', '#quyuSelect > .prov > option');
        await this.selectRegion(page, project.market,
            '#quyuSelect > select.city', '#quyuSelect > .city > option');
        let address = this.getAddress(project);
        // 具体地址
        await CommSession.oneStepT(page, '#add2', address.substring(0, 25));
        // 标题
        await CommSession.oneStepT(page, '#title', project.title);
        // 描述
        await CommSession.oneStepT(page, '#info', project.desc);
        // 联系人
        await CommSession.oneStepT(page, '#lxr', this.brokerName);
        // 提交
        await CommSession.oneStepC(page, '#submittj', _static.twoT);
    }

    async selectCity(page, city, selector) {
        await page.waitFor(_static.twoT);
        await CommSession.oneStepC(page, selector);
        await page.waitFor(_static.twoT);
        if (city.includes('上海')) {
            await CommSession.oneStepC(page, this.citySel.shanghai);
        }
        if (city.includes('无锡')) {
            await CommSession.oneStepC(page, this.citySel.wuxi);
        }
        if (city.includes('苏州')) {
            await CommSession.oneStepC(page, this.citySel.suzhou);
        }
        if (city.includes('南京')) {
            await CommSession.oneStepC(page, this.citySel.nanjing);
        }
        if (city.includes('郑州')) {
            await CommSession.oneStepC(page, this.citySel.zhengzhou);
        }
    }

    async checkBuildStructure(page, buildStructure) {
        await page.waitFor(_static.oneT);
        if (util._coll.isEmpty(buildStructure)) {
            buildStructure = [{index: 4, name: '钢架结构'}];
        }
        await CommSession.oneStepC(page, '#jiegou');
        let names = CommSession.getNames(buildStructure);
        let elementSel = '#jiegou > option';
        let eIndex = await this.getOptionIndex(page, names, elementSel);
        for (let pc = 1; pc < eIndex; pc++) {
            await page.keyboard.press('ArrowDown');
        }
        await page.keyboard.press('Enter');
    }

    async getOptionIndex(page, names, elementSel) {
        let elements = await page.$$(elementSel);
        for (let eIndex = 1; eIndex <= elements.length; eIndex++) {
            let label = await CommSession.getText(page, elementSel + ':nth-child(' + eIndex + ')');
            if (names.indexOf(label) > -1) {
                return eIndex;
            }
        }
        return 2;
    }

    async independentHouse(page, whAdvantageList) {
        if (util._coll.isEmpty(whAdvantageList)) {
            await CommSession.oneStepC(page, '#duyuan0');
            return;
        }
        let labels = CommSession.getNames(whAdvantageList);
        if (labels.indexOf('独门独院') > -1) {
            await CommSession.oneStepC(page, '#duyuan1');
            return;
        }
        await CommSession.oneStepC(page, '#duyuan0');
    }

    async elevator(page, whFacilityList) {
        if (util._coll.isEmpty(whFacilityList)) {
            await CommSession.oneStepC(page, '#dianti0');
            return;
        }
        let labels = CommSession.getNames(whFacilityList);
        if (labels.indexOf('电梯') > -1) {
            await CommSession.oneStepC(page, '#dianti1');
            return;
        }
        await CommSession.oneStepC(page, '#dianti0');
    }

    async selectFire(page, hasFire) {
        if (util._obj.isEmpty(hasFire)) {
            await CommSession.oneStepC(page, '#xiaofang0');
            return;
        }
        if (hasFire == 1) {
            await CommSession.oneStepC(page, '#xiaofang1');
            return;
        }
        await CommSession.oneStepC(page, '#xiaofang0')
    }

    async oldOrNew(page) {
        await CommSession.oneStepC(page, '#xinjiu');
        for (let i = 0; i < 2; i++) {
            await page.keyboard.press('ArrowDown');
        }
        await page.keyboard.press('Enter');
    }

    async selectRegion(page, region, regionSel, optionSel) {
        await CommSession.oneStepC(page, regionSel);
        let regions = await page.$$(optionSel);
        if (util._coll.isEmpty(regions)) {
            await CommSession.clickOther(page);
            await CommSession.oneStepC(page, regionSel);
            regions = await page.$$(optionSel);
        }
        if (util._coll.isEmpty(regions)) {
            return;
        }
        let mousePCount = 2;
        for (let pIx = 1; pIx <= regions.length; pIx++) {
            let optionText = await page.$eval(optionSel + ':nth-child(' + pIx + ')', el => el.innerText);
            if (optionText.includes(region) || region.includes(optionText)) {
                mousePCount = pIx;
                break;
            }
        }
        for (let pc = 1; pc < mousePCount; pc++) {
            await page.waitFor(_static.oneT);
            await page.keyboard.press('ArrowDown');
        }
        await page.keyboard.press('Enter');
    }

    getKva(kva) {
        if (util._obj.isEmpty(kva)) {
            return 200;
        }
        if (kva < 200 || kva > 300) {
            return 200;
        }
        return kva;
    }

    getAddress(project) {
        let address = project.city + project.district;
        if (util._obj.notEmpty(project.market)) {
            address += project.market;
        }
        if (util._obj.notEmpty(project.mockAddress)) {
            address += project.mockAddress;
        }
        return address;
    }
}

module.exports = Session;