const puppeteer = require('puppeteer');
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
        });
    }

    async selectRegion(page, region, regionSel, elementSel) {
        let elements = await this.getRegionNodes(page, elementSel, regionSel);
        if (util._coll.isEmpty(elements)) {
            return;
        }
        let mousePCount = 2;
        for (let pIx = 1; pIx <= elements.length; pIx++) {
            let optionText = await page.$eval(elementSel + ':nth-child(' + pIx + ')', el => el.innerText);
            if (optionText.includes(region) || region.includes(optionText)) {
                mousePCount = pIx;
                break;
            }
        }
        await page.waitFor(_static.oneT);
        for (let pc = 1; pc <= mousePCount; pc++) {
            await page.keyboard.press('ArrowDown');
        }
        await page.keyboard.press('Enter');
    }

    async getRegionNodes(page, elementSel, regionSel) {
        await CommSession.oneStepC(page, regionSel);
        await page.waitFor(_static.threeT);
        let elements = await page.$$(elementSel);
        if (util._coll.isEmpty(elements)) {
            await CommSession.oneStepC(page, regionSel);
            await CommSession.oneStepC(page, regionSel);
            await page.waitFor(_static.threeT);
            elements = await page.$$(elementSel);
        }
        await page.waitFor(_static.oneT);
        return elements;
    }

    async checkOption(page, type, elementSel) {
        await page.waitFor(_static.oneT);
        let elements = await page.$$(elementSel);
        for (let eIndex = 1; eIndex <= elements.length; eIndex++) {
            let text = elementSel + ':nth-child(' + eIndex + ') > span';
            let label = await CommSession.getText(page, text);
            if (type.name == label) {
                await CommSession.oneStepC(page, text, _static.twoT);
                return;
            }
        }
        await CommSession.oneStepC(page, elementSel + ':nth-child(1) > span');
    }

    // 厂房类型
    async checkPlantType(page, plantType) {
        await page.waitFor(_static.oneT);
        if (util._obj.isEmpty(plantType)) {
            plantType = {index: 1, name: '标准厂房'};
        }
        await CommSession.oneStepC(page, '#section2 > div > div:nth-child(2) > div:nth-child(1) > div > div > div > div > input');
        await page.waitFor(_static.oneT);
        let elementSel = 'body > div:nth-child(9) > div.el-scrollbar > div > ul > li';
        await this.checkOption(page, plantType, elementSel);
    }

    // 仓库类型
    async checkWarehouseType(page, warehouseType) {
        if (util._obj.isEmpty(warehouseType)) {
            warehouseType = {index: 1, name: '普通仓库'};
        }
        await CommSession.oneStepC(page, '#section2 > div > div:nth-child(2) > div:nth-child(2) > div > div > div > div > input');
        await page.waitFor(_static.twoT);
        let elementSel = 'body > div:nth-child(9) > div.el-scrollbar > div.el-select-dropdown__wrap.el-scrollbar__wrap > ul > li';
        try {
            await this.checkOption(page, warehouseType, elementSel);
        } catch (e) {
            await this.checkOption(page, warehouseType, elementSel);
        }
    }

    // 厂房结构
    async checkStructure(page, oneStructure) {
        await page.waitFor(_static.oneT);
        if (util._obj.isEmpty(oneStructure)) {
            oneStructure = {index: 1, name: '钢结构'};
        }
        await CommSession.oneStepC(page, '#section2 > div > div:nth-child(2) > div:nth-child(2) > div > div > div > div > input');
        await page.waitFor(_static.oneT);
        let elementSel = 'body > div:nth-child(10) > div.el-scrollbar > div > ul > li';
        await this.checkOption(page, oneStructure, elementSel);
    }

    // 厂房新旧
    async checkDegree(page, degree) {
        await page.waitFor(_static.oneT);
        if (util._obj.isEmpty(degree)) {
            degree = {index: 2, name: '九成新'};
        }
        await CommSession.oneStepC(page, '#section2 > div > div:nth-child(3) > div:nth-child(1) > div > div > div > div > input');
        await page.waitFor(_static.oneT);
        let elementSel = 'body > div:nth-child(11) > div.el-scrollbar > div > ul > li';
        await this.checkOption(page, degree, elementSel);
    }

    // 地坪功能
    async checkFloorFunction(page, floorFunction) {
        await page.waitFor(_static.oneT);
        if (util._obj.isEmpty(floorFunction)) {
            floorFunction = {index: 2, name: '防尘'};
        }
        await CommSession.oneStepC(page, '#section2 > div > div:nth-child(3) > div:nth-child(2) > div > div > div > div > input');
        let elementSel = 'body > div:nth-child(11) > div.el-scrollbar > div.el-select-dropdown__wrap.el-scrollbar__wrap > ul > li';
        await this.checkOption(page, floorFunction, elementSel);
    }

    // 地坪材质
    async checkFloorMaterial(page, floorMaterial) {
        await page.waitFor(_static.oneT);
        if (util._obj.isEmpty(floorMaterial)) {
            floorMaterial = {index: 2, name: '地砖'};
        }
        await CommSession.oneStepC(page, '#section2 > div > div:nth-child(4) > div > div > div > div > div.el-input.el-input--small.el-input--suffix > input');
        let elementSel = 'body > div:nth-child(12) > div.el-scrollbar > div.el-select-dropdown__wrap.el-scrollbar__wrap > ul > li';
        await this.checkOption(page, floorMaterial, elementSel);
    }

    // 仓库结构
    async checkCKStructure(page, ckStructure) {
        await page.waitFor(_static.oneT);
        if (util._obj.isEmpty(ckStructure)) {
            ckStructure = {index: 1, name: '钢架结构'};
        }
        await CommSession.oneStepC(page, '#section2 > div > div:nth-child(3) > div:nth-child(1) > div > div > div > div > input');
        let elementSel = 'body > div:nth-child(10) > div.el-scrollbar > div.el-select-dropdown__wrap.el-scrollbar__wrap > ul > li';
        await this.checkOption(page, ckStructure, elementSel);
    }

    // 厂房楼层
    async checkFloorType(page, floorType) {
        await page.waitFor(_static.oneT);
        if (util._obj.isEmpty(floorType)) {
            floorType = {index: 1, name: '一楼'};
        }
        await CommSession.oneStepC(page, '#section2 > div > div:nth-child(3) > div:nth-child(2) > div > div > div > div > input');
        let elementSel = 'body > div:nth-child(12) > div.el-scrollbar > div > ul > li';
        await this.checkOption(page, floorType, elementSel);
    }

    // 特色卖点
    async checkFeaturedPoint(page, pointList) {
        if (util._coll.isEmpty(pointList)) {
            pointList = [{index: 3, name: '可办公'}, {index: 12, name: '可办环评'}];
        }
        let elements = await page.$$('#section4 > div > div:nth-child(1) > div > div > div > div > label');
        let elementSel = '#section4 > div > div:nth-child(1) > div > div > div > div > label';
        let names = CommSession.getNames(pointList);
        for (let eIndex = 1; eIndex <= elements.length; eIndex++) {
            let text = elementSel + ':nth-child(' + eIndex + ') > span.el-checkbox__label';
            let label = await CommSession.getText(page, text);
            if (names.indexOf(label + '') > -1) {
                await CommSession.oneStepC(page, text);
            }
        }
    }

    // 仓库特色卖点
    async checkCkPoint(page, pointList) {
        if (util._coll.isEmpty(pointList)) {
            pointList = [{index: 3, name: '物流仓库'}, {index: 4, name: '大型楼库'}];
        }
        let elements = await page.$$('#section4 > div > div:nth-child(1) > div > div > div > div > label');
        let elementSel = '#section4 > div > div:nth-child(1) > div > div > div > div > label';
        let names = CommSession.getNames(pointList);
        for (let eIndex = 1; eIndex <= elements.length; eIndex++) {
            let text = elementSel + ':nth-child(' + eIndex + ') > span.el-checkbox__label';
            let label = await CommSession.getText(page, text);
            if (names.indexOf(label + '') > -1) {
                await CommSession.oneStepC(page, text);
            }
        }
    }

    //厂房
    async postWorkshop(page, project) {
        await this.checkedCFRegion(page, project);
        // 面积
        await CommSession.oneStepT(page, '#section2 > div > div:nth-child(1) > div:nth-child(1) > div > div > div > input', project.area);
        // 租金
        await CommSession.oneStepT(page, '#section2 > div > div:nth-child(1) > div:nth-child(2) > div > div > div > input', this.getPrice(project.price));
        // 类型
        await this.checkPlantType(page, project.plantType);
        // 结构
        await this.checkStructure(page, project.oneStructure);
        // 新旧 默认
        await this.checkDegree(page, null);
        // 厂房楼层
        await this.checkFloorType(page, project.floorType);
        // 参数信息
        // 面积
        await CommSession.oneStepT(page, '#section3 > div > div:nth-child(2) > div:nth-child(1) > div > div > div > input', project.area);
        // 厂房配电
        await this.fillInKva(project, page);
        // 共有消防
        await CommSession.oneStepC(page, '#section3 > div > div:nth-child(4) > div > div > div > div > label:nth-child(1) > span > input');
        // 食堂
        await this.fillInCanteen(project, page);
        // 特色卖点
        await this.checkFeaturedPoint(page, project.featuredPointList);
        // 标题
        await CommSession.oneStepT(page, '#section4 > div > div:nth-child(2) > div > div > div > div > input', this.getTitle(project));
        // 描述
        await this.fillInDesc(page, project);
        // 提交房源
        await CommSession.oneStepC(page, '#infoFooterBar > button', _static.threeT);

        return await this.getSubmitRes(page, this.cfSuccessUrl);
    }

    async checkedCFRegion(page, project) {
        await page.waitFor(_static.threeT);
        await this.selectRegion(page, project.province,
            '#section1 > div > div:nth-child(2) > div > div > div > div > div:nth-child(1) > div > input',
            'body > div.el-select-dropdown.el-popper > div.el-scrollbar > div > ul > li'
        );
        await this.selectRegion(page, project.city,
            '#section1 > div > div:nth-child(2) > div > div > div > div > div:nth-child(2) > div > input',
            'body > div:nth-child(6) > div.el-scrollbar > div > ul > li'
        );
        if (project.district === '工业园区') {
            project.district = '苏州工业区园';
        }
        await this.selectRegion(page, project.district,
            '#section1 > div > div:nth-child(2) > div > div > div > div > div:nth-child(3) > div > input',
            'body > div:nth-child(7) > div.el-scrollbar > div > ul > li'
        );
        await this.selectRegion(page, project.market,
            '#section1 > div > div:nth-child(2) > div > div > div > div > div:nth-child(4) > div > input',
            'body > div:nth-child(8) > div.el-scrollbar > div > ul > li'
        );
    }

    async fillInDesc(page, project) {
        await page.type('#section4 > div > div:nth-child(3) > div > div > div > div > textarea', project.desc);
        let urls = CommSession.getPhotoUrls(project.photoUrls);
        await CommSession.upPhoto(page, urls, project.id, '.el-upload__input');
    }

    async fillInCanteen(project, page) {
        if (util._obj.notEmpty(project.canteen) && project.canteen == 1) {
            await CommSession.oneStepC(page, '#section3 > div > div:nth-child(3) > div:nth-child(2) > div > div > div > label:nth-child(1) > span > input');
        } else {
            await CommSession.oneStepC(page, '#section3 > div > div:nth-child(3) > div:nth-child(2) > div > div > div > label:nth-child(3) > span > input');
        }
    }

    async fillInKva(project, page) {
        if (util._obj.isEmpty(project.kva)) {
            // 没有kva
            await CommSession.oneStepC(page, '#section3 > div > div:nth-child(3) > div:nth-child(1) > div > div > div.el-radio-group > label:nth-child(2) > span > input');
        } else {
            await CommSession.oneStepC(page, '#section3 > div > div:nth-child(3) > div:nth-child(1) > div > div > div.el-radio-group > label:nth-child(1) > span > input');
            // 电量
            await CommSession.oneStepT(page, '#section3 > div > div:nth-child(3) > div:nth-child(1) > div > div > div > input', project.kva);
        }
    }

    // 仓库
    async postWarehouse(page, project) {
        await page.waitFor(_static.fiveT);
        await CommSession.oneStepC(page, 'body > div > div:nth-child(3) > div > nav > a:nth-child(4)');
        await this.checkedCKRegion(page, project);
        // 面积
        await CommSession.oneStepT(page, ' #section2 > div > div:nth-child(1) > div:nth-child(1) > div > div > div > input', project.area);
        // 租金
        await CommSession.oneStepT(page, '#section2 > div > div:nth-child(1) > div:nth-child(2) > div > div > div > input', this.getPrice(project.price));
        // 层高
        await CommSession.oneStepT(page, '#section2 > div > div:nth-child(2) > div:nth-child(1) > div > div > div > input', project.maxFloorH);
        // 仓库类型
        await this.checkWarehouseType(page, project.warehouseType);
        // 仓库结构
        await this.checkCKStructure(page, project.ckStructure);
        // 地坪功能
        await this.checkFloorFunction(page, project.floorFunction);
        // 地坪材质
        await this.checkFloorMaterial(page, project.floorMaterial);
        // 电量
        await CommSession.oneStepT(page, '#section3 > div > div:nth-child(1) > div:nth-child(1) > div > div > div > input', project.kva);
        // 保税区
        await this.checkBondedRegion(project, page);
        // 分割
        await CommSession.oneStepC(page, '#section3 > div > div:nth-child(2) > div:nth-child(1) > div > div > div > label:nth-child(2) > span.el-radio__input > input');
        //改建 ：否
        await CommSession.oneStepC(page, '#section3 > div > div:nth-child(2) > div:nth-child(2) > div > div > div > label:nth-child(2) > span.el-radio__input > input');
        // 仓库特色卖点
        await this.checkCkPoint(page, project.ckPointList);
        // 标题
        await CommSession.oneStepT(page, '#section4 > div > div:nth-child(2) > div > div > div > div > input', this.getTitle(project));
        // 描述
        await page.type('#section4 > div > div:nth-child(3) > div > div > div > div > textarea', project.desc);
        // 图片
        let urls = CommSession.getPhotoUrls(project.photoUrls);
        await CommSession.upPhoto(page, urls, project.id, '.el-upload__input');
        // 提交房源
        await CommSession.oneStepC(page, '#infoFooterBar > button > span', _static.threeT);
        await page.waitFor(_static.threeT);

        return await this.getSubmitRes(page, this.ckSuccessUrl);
    }

    async checkedCKRegion(page, project) {
        await page.waitFor(_static.threeT);
        await this.selectRegion(page, project.province,
            '#section1 > div > div > div > div > div > div:nth-child(1) > div > input',
            'body > div.el-select-dropdown.el-popper > div.el-scrollbar > div > ul > li'
        );
        await this.selectRegion(page, project.city,
            '#section1 > div > div > div > div > div > div:nth-child(2) > div > input',
            'body > div:nth-child(6) > div.el-scrollbar > div > ul > li'
        );
        if (project.district === '工业园区') {
            project.district = '苏州工业区园';
        }
        await this.selectRegion(page, project.district,
            '#section1 > div > div > div > div > div > div:nth-child(3) > div > input',
            'body > div:nth-child(7) > div.el-scrollbar > div > ul > li'
        );
        await this.selectRegion(page, project.market,
            '#section1 > div > div > div > div > div > div:nth-child(4) > div > input',
            'body > div:nth-child(8) > div.el-scrollbar > div > ul > li'
        );
    }

    async checkBondedRegion(project, page) {
        if (project.bondedRegion === 1) {
            await CommSession.oneStepC(page, '#section3 > div > div:nth-child(1) > div:nth-child(2) > div > div > div > label:nth-child(1) > span.el-radio__input > input')
        } else {
            await CommSession.oneStepC(page, '#section3 > div > div:nth-child(1) > div:nth-child(2) > div > div > div > label:nth-child(2) > span.el-radio__input > input');
        }
    }

    async getSubmitRes(page, successUrl) {
        if (page.url() === successUrl) {
            return new Result(enums.POST_STATUS.done, _static.sendSuccess);
        }
        await page.waitFor(_static.twoT);
        if (page.url() === successUrl) {
            return new Result(enums.POST_STATUS.done, _static.sendSuccess);
        }
        return new Result(enums.POST_STATUS.done, _static.submitError);
    }

    async getCanPostCount() {
        let page;
        try {
            page = await this.browser.newPage();
            await page.goto(this.ckListUrl, {waitUntil: 'networkidle0'});
            let canPostSelector = 'body > div > div:nth-child(4) > div > div.index-main-right > div > div.ms-list-hanlder > div.ms-list-hanlder-right > span:nth-child(1) > strong';
            for (let i = 0; i < 3; i++) {
                await page.waitFor(_static.twoT);
                if (await page.$(canPostSelector)) {
                    let canPostCount = await CommSession.getText(page, canPostSelector);
                    await page.close();
                    return canPostCount.replace('条', '');
                }
            }
            await page.close();
            return 1;
        } catch (e) {
            if (page) {
                await page.close();
            }
            return 1;
        }
    }

    getPrice(price) {
        if (util._obj.isEmpty(price)) {
            return 30;
        }
        if (Number(price) < 1) {
            return 30;
        }
        return (Number(price) * 30.4).toFixed(1);
    }

    getTitle(project) {
        if (util._obj.isEmpty(project.title)) {
            return project.address + project.area + '平';
        }
        if (project.title.length > 30) {
            return project.title.substring(0, 29);
        }
        return project.title;
    }

    async post() {
        try {
            const page = await this.browser.newPage();
            await page.goto(this.postCf, {waitUntil: 'networkidle0'});
            // 自定义属性
            const customize = await new Customize(this);
            // 项目信息
            const project = await new Project(this.projectId, customize);
            let checkRes = CommSession.checkProject(project);
            if (checkRes != null) {
                return checkRes;
            }
            let canPostCount = await this.getCanPostCount();
            if (canPostCount < 1) {
                return new Result(enums.POST_STATUS.done, _static.postOutOf);
            }
            if (project.useType === enums.USE_TYPE.cangku) {
                return await this.postWarehouse(page, project);
            }
            return await this.postWorkshop(page, project);
        } catch (e) {
            util.writeLog(e);
            return new Result(enums.POST_STATUS.undone, _static.sendException);
        }
    }

    async close() {
        await this.browser.close()
    }

    async cookieLogin() {
        return await CommSession.cookieLogin(this);
    }

    async getRefreshPage() {
        let page = await this.browser.newPage();
        let url = this.refreshType === enums.USE_TYPE.cangku ? this.ckListUrl : this.cfListUrl;
        await page.goto(url, {waitUntil: 'networkidle0'});
        await page.waitFor(_static.threeT);
        return page;
    }

    // 得到刷新剩余次数
    async getSurplusCount(page) {
        let surplusCount = await CommSession.getText(page, 'body > div > div:nth-child(4) > div > div.index-main-right > div > div.ms-list-hanlder > div.ms-list-hanlder-right > span:nth-child(2) > strong');
        util.logTOPath('\n当前账号剩余刷新次数：' + surplusCount, this.logPath);
        return surplusCount.toString().replace('次', '');
    }

    async getPageCount(page) {
        let ckCount = await this.projectCount(page);
        util.logTOPath('\n总条数：' + ckCount, this.logPath);
        return CommSession.getPageCount(Number(ckCount));
    }

    async dealWithPost(page, prefix) {
        let title = await CommSession.getText(page, prefix + '> td.el-table_1_column_2.is-center > div > div > div > a');
        let number = await CommSession.getText(page, prefix + '> td.el-table_1_column_2.is-center > div > div > div > p > span:nth-child(1)');
        number = number.toString().replace('编号：', '');
        util.logTOPath('\n标题：' + title + '\n编号：' + number, this.logPath);
        await CommSession.oneStepC(page, prefix + ' > td.el-table_1_column_6.is-center > div > span:nth-child(1)');
        await CommSession.oneStepC(page, 'body > div.el-message-box__wrapper > div > div.el-message-box__btns > button.el-button.el-button--default.el-button--small.el-button--primary > span', _static.threeT);
    }

    // 刷新
    async refresh() {
        try {
            if (CommSession.checkRefreshTask(this)) {
                return new Result(enums.POST_STATUS.done, _static.refreshTaskError);
            }
            let page = await this.getRefreshPage();
            let surplusCount = await this.getSurplusCount(page);
            if (surplusCount < 1) {
                return new Result(enums.POST_STATUS.done, _static.refreshOutOf);
            }
            let pageCount = await this.getPageCount(page);
            if (pageCount < 1) {
                return new Result(enums.POST_STATUS.done, _static.refreshNo);
            }
            return await this.doRefresh(pageCount, page);
        } catch (e) {
            return new Result(enums.POST_STATUS.undone, _static.exeException);
        }
    }

    async doRefresh(pageCount, page) {
        let pageSelector = 'body > div > div:nth-child(4) > div > div.index-main-right > div > div.ms-list-footer > div > div > ul > li';
        for (let pageNumber = 1; pageNumber <= pageCount; pageNumber++) {
            await CommSession.oneStepC(page, pageSelector + ':nth-child(' + pageNumber + ')');
            await page.waitFor(_static.threeT);
            let trSelector = 'body > div > div:nth-child(4) > div > div.index-main-right > div > div.ms-list-body > div > div.el-table__body-wrapper.is-scrolling-none > table > tbody > tr';
            let elements = await page.$$(trSelector);
            for (let eIdx = 1; eIdx <= elements.length; eIdx++) {
                let prefix = trSelector + ':nth-child(' + eIdx + ')';
                let time = await CommSession.getText(page, prefix + '> td.el-table_1_column_5.is-center > div > p');
                let refreshTime = time.toString().replace('\n', ' ');
                if (refreshTime > this.refreshTime) {
                    continue;
                }
                await this.dealWithPost(page, prefix);
                return new Result(enums.POST_STATUS.done, _static.refreshSuccess);
            }
        }
        return new Result(enums.POST_STATUS.done, _static.refreshNoTime);
    }

    async projectCount(page) {
        let totalSelector = 'body > div.index > div:nth-child(4) > div > div.index-main-right > div > div.ms-list-footer > div > div > span.el-pagination__total';
        let total = await CommSession.getText(page, totalSelector);
        return total.toString()
            .replace('共', '')
            .replace('条', '')
            .replace(' ', '');
    }

    async yzmLogin() {
        // 尝试三次 失败不再登录
        for (let count = 0; count < 5; count++) {
            let yzmRes = await CommSession.yzmLogin(this);
            if (yzmRes === true) {
                return true;
            }
        }
        return false;
    }

    /**
     * 测试登录使用 为公司省钱
     * @returns {Promise<void>}
     */
    async testLogin() {
        await CommSession.testLogin(this);
    }
}

module.exports = Session;
