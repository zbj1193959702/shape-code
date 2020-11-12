const puppeteer = require('puppeteer');
const util = require('../../base/utils');
const enums = require('../../base/enum');
const _static = require('../../base/static');
const Customize = require('../../models/Customize');
const Project = require('../../models/Project');
const Result = require('../../models/Result');
const CommSession = require('../CommSession');

class Session {

    constructor(account, lianCang, headless = false) {

        return new Promise(async (resolve, reject) => {
            CommSession.init(this, account, lianCang);
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

    async cookieLogin() {
        return CommSession.cookieLogin(this);
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

    async post() {
        const page = await this.browser.newPage();
        await page.goto(this.postCf, {waitUntil: 'networkidle0'});
        // 自定义属性
        const customize = await new Customize(this);
        // 项目信息
        const project = await new Project(this.projectId, customize);
        let checkRes = CommSession.checkProject(project)
        if (checkRes != null) {
            return checkRes;
        }
        try {
            await this.postBase(page, project);
            await this.postDetail(page, project);
            await this.postSpec(page, project);
            await this.checkFacility(page, project);
            await this.checkPark(page, project);
            await this.postContactInfo(page);
            return await this.commit(page);
        } catch (e) {
            util.writeLog(e);
            return new Result(enums.POST_STATUS.undone, _static.sendException);
        }
    }

    /**
     * 园区信息
     */
    async checkPark(page, project) {
        await CommSession.oneStepC(page, '#select_tab > span:nth-child(5)');
        await page.waitFor(_static.threeT);
        // 园区安保
        await this.checkSecurity(page, project.securityList);
        // 园区最大供电率
        await CommSession.oneStepT(page, '#basediv5 > div.row.lc-formMain > div > div:nth-child(4) > div > input', project.kva);
    }

    async checkSecurity(page, security) {
        await page.waitFor(_static.oneT);
        if (util._coll.isEmpty(security)) {
            security = [{index: 2, name: '园区保安'}];
        }
        await CommSession.oneStepC(page, '#basediv5 > div.row.lc-formMain > div > div:nth-child(1) > div > div > button');
        let names = CommSession.getNames(security);
        let elementSel = '#basediv5 > div.row.lc-formMain > div > div:nth-child(1) > div > div > div > ul > li';
        await this.checkOption(page, names, elementSel);
    }

    /**
     * 设施信息
     */
    async checkFacility(page, project) {
        await CommSession.oneStepC(page, '#select_tab > span:nth-child(4)');
        await page.waitFor(_static.threeT);
        // 仓内水电网
        await CommSession.oneStepC(page, '#basediv4 > div.row.lc-formMain > div > div:nth-child(1) > div > div > button');
        await CommSession.oneStepC(page, '#basediv4 > div.row.lc-formMain > div > div:nth-child(1) > div > div > div > ul > li:nth-child(3) > a > span.text');
        await CommSession.oneStepC(page, '#basediv4 > div.row.lc-formMain > div > div:nth-child(1) > div > div > div > ul > li:nth-child(4) > a > span.text');
        await CommSession.clickOther(page);

        // 仓库设施
        await this.checkWhFacility(page, project.whFacilityList);
        // 库内设备
        await this.checkEquipment(page, project.equipmentList);
    }

    async checkEquipment(page, equipment) {
        await page.waitFor(_static.oneT);
        if (util._coll.isEmpty(equipment)) {
            equipment = [{index: 1, name: '行吊'}];
        }
        await CommSession.oneStepC(page, '#basediv4 > div.row.lc-formMain > div > div:nth-child(3) > div > div > button', _static.oneT);
        let names = CommSession.getNames(equipment);
        let elementSel = '#basediv4 > div.row.lc-formMain > div > div:nth-child(3) > div > div.show-tick > div > ul > li';
        await this.checkOption(page, names, elementSel);
    }

    async checkOption(page, names, elementSel) {
        let elements = await page.$$(elementSel);
        for (let eIndex = 1; eIndex <= elements.length; eIndex++) {
            let text = elementSel + ':nth-child(' + eIndex + ') > a > span.text';
            let label = await CommSession.getText(page, text);
            if (names.indexOf(label + '') > -1) {
                await CommSession.oneStepC(page, text);
            }
        }
        await page.waitFor(_static.oneT);
        await CommSession.clickOther(page);
    }

    async checkWhFacility(page, whFacility) {
        await page.waitFor(_static.oneT);
        if (util._coll.isEmpty(whFacility)) {
            whFacility = [{index: 1, name: '电梯'}];
        }
        await CommSession.oneStepC(page, '#basediv4 > div.row.lc-formMain > div > div:nth-child(2) > div > div > button', _static.oneT);
        let names = CommSession.getNames(whFacility);
        let elementSel = '#basediv4 > div.row.lc-formMain > div > div:nth-child(2) > div > div > div > ul > li';
        await this.checkOption(page, names, elementSel);
    }

    async commit(page) {
        await CommSession.oneStepC(page, '#basediv6 > div.row.cl > button:nth-child(4)');
        await CommSession.oneStepC(page, '#layui-layer1 > div.layui-layer-btn > a.layui-layer-btn0', _static.threeT);
        await page.waitFor(_static.fiveT);
        return page.url() === this.ckSuccessUrl ?
            new Result(enums.POST_STATUS.done, _static.sendSuccess) : new Result(enums.POST_STATUS.done, _static.submitError);
    }

    // 仓库详情
    async postDetail(page, project) {
        // 总楼层
        await CommSession.oneStepC(page, '#select_tab > span:nth-child(2)', _static.threeT);
        await page.waitFor(_static.threeT);
        await CommSession.oneStepT(page, '#basediv2 > div.row.lc-formMain > div > div:nth-child(1) > div > input', util._default(project.maxFloor, '2'));
        // 消防资质
        await this.checkFire(page, project.fireQualification);
        // 消防检验
        await CommSession.oneStepC(page, '#xfjy1');
        // 地坪材质
        await this.checkFloorMaterial(page, project.floorMaterialList);
        // 建筑结构
        await this.checkBuildStructure(page, project.buildStructureList);
    }

    async checkBuildStructure(page, buildStructure) {
        await page.waitFor(_static.oneT);
        if (util._coll.isEmpty(buildStructure)) {
            buildStructure = [{index: 1, name: '钢架结构'}];
        }
        await CommSession.oneStepC(page, '#basediv2 > div.row.lc-formMain > div > div:nth-child(5) > div > div > button');
        let names = CommSession.getNames(buildStructure);
        let elementSel = '#basediv2 > div.row.lc-formMain > div > div:nth-child(5) > div > div > div > ul > li';
        await this.checkOption(page, names, elementSel);
    }

    /**
     * 地坪材质
     */
    async checkFloorMaterial(page, floorMaterial) {
        await page.waitFor(_static.oneT);
        if (util._coll.isEmpty(floorMaterial)) {
            floorMaterial = [{index: 3, name: '防尘'}];
        }
        await CommSession.oneStepC(page, '#basediv2 > div.row.lc-formMain > div > div:nth-child(4) > div > div > button');
        await page.waitFor(_static.oneT);
        let names = CommSession.getNames(floorMaterial);
        let elementSel = '#basediv2 > div.row.lc-formMain > div > div:nth-child(4) > div > div > div > ul > li';
        await this.checkOption(page, names, elementSel);
    }

    async checkFire(page, fireIndex) {
        await CommSession.oneStepC(page, '#xfxt');
        await page.waitFor(_static.oneT);
        for (let i = 0; i < fireIndex - 1; i++) {
            await page.keyboard.press('ArrowDown');
        }
        await page.waitFor(_static.oneT);
        await page.keyboard.press('Enter');
    }

    // 规格
    async postSpec(page, project) {
        await CommSession.oneStepC(page, '#select_tab > span:nth-child(3)', _static.threeT);
        await page.waitFor(_static.threeT);

        for (let index = 0; index < project.floorList.length; index++) {
            let fOne = project.floorList[index];
            let elePrefix = '#basediv3 > div.lc-formMain > div > div > div > table > tbody > tr:nth-child(' + (index + 1) + ')';
            // 出租楼层
            await CommSession.oneStepT(page, elePrefix + ' > td.lc-atl-level > input', util._default(fOne.floorNumber));
            // 是否可分租
            await CommSession.oneStepC(page, elePrefix + ' > td.lc-atl-rentType > div > button');
            if (fOne.isSublet == 1) {
                await CommSession.oneStepC(page, elePrefix + ' > td.lc-atl-rentType > div > div > ul > li:nth-child(1) > a');
            } else {
                await CommSession.oneStepC(page, elePrefix + ' > td.lc-atl-rentType > div > div > ul > li:nth-child(2) > a');
            }
            await CommSession.clickOther(page);
            // 起租面积(㎡)
            await CommSession.oneStepT(page, elePrefix + ' > td:nth-child(3) > input', util._default(fOne.minimumLeaseArea));
            // 可租面积
            await CommSession.oneStepT(page, elePrefix + ' > td:nth-child(4) > input', util._default(fOne.rentableArea));
            // 租赁起价
            await CommSession.oneStepT(page, elePrefix + ' > td:nth-child(5) > input', util._default(fOne.minPrice));
            // 地坪承重(t/㎡)
            await CommSession.oneStepT(page, elePrefix + ' > td:nth-child(6) > input', util._default(fOne.floorBearing));
            // 层高(m)
            await CommSession.oneStepT(page, elePrefix + ' > td:nth-child(7) > input', util._default(fOne.floorHeight));
            // 添加楼层
            if (index < project.floorList.length - 1) {
                await CommSession.oneStepC(page, elePrefix + ' > td.lc-atl-operate > div > div.lc-atl-add');
                await page.waitFor(_static.oneT);
            }
        }
    }

    // 联系信息
    async postContactInfo(page) {
        await CommSession.oneStepC(page, '#select_tab > span:nth-child(6)', _static.threeT);
        await CommSession.oneStepT(page, '#basediv6 > div.row.lc-formMain > div > div:nth-child(1) > div > input', '上海熙邻网络技术有限公司');
        await CommSession.oneStepT(page, '#basediv6 > div.row.lc-formMain > div > div:nth-child(2) > div > input', this.brokerName);
        await CommSession.oneStepT(page, '#basediv6 > div.row.lc-formMain > div > div:nth-child(3) > div > input', this.username);
    }

    // 基本信息
    async postBase(page, project) {
        // 标题
        await CommSession.oneStepT(page, '#warehouseName', project.title);
        // 区域 直辖市另处理
        await this.checkedRegion(page, project);
        // 地图位置
        await this.checkedMap(page, project);
        // 是否含税含物业
        await this.checkTaxOrFee(page, project.taxOrManagementFee);
        // 总建筑面积
        await CommSession.oneStepT(page, '#basediv > div.row.lc-formMain > div > div:nth-child(6) > div > input', project.area);
        // 存储类型
        await this.checkStorageType(page, project.storageTypeList);
        // 库型选择
        await this.checkWhType(page, project.whTypeList);
        // 起租期限
        await CommSession.oneStepT(page, '#basediv > div.row.lc-formMain > div > div:nth-child(9) > div > input', util._default(project.leaseTerm, 12));
        // 是否保税区
        await this.checkBondedRegion(page, project.bondedRegion);
        // 建设状态
        await this.checkBuildStatus(page, project.buildStatus);
        // 发票
        await CommSession.oneStepC(page, '#fpkj1');
        // 服务方式
        await this.checkServiceMode(page);
        // 仓库优势
        await this.checkWhAdvantage(page, project.whAdvantageList);
        // 描述
        await CommSession.oneStepT(page, '#introduce', project.desc);
        // 上传图片
        let urls = CommSession.getPhotoUrls(project.photoUrls);
        await CommSession.upPhoto(page, urls, project.id, "#lcfilePicker input[name='file']");
    }

    async checkServiceMode(page) {
        await CommSession.oneStepC(page, '#basediv > div.row.lc-formMain > div > div:nth-child(13) > div > div > button > span.filter-option.pull-left');
        await CommSession.oneStepC(page, '#basediv > div.row.lc-formMain > div > div:nth-child(13) > div > div > div > ul > li:nth-child(2) > a');
        await CommSession.clickOther(page);
    }

    async checkedMap(page, project) {
        await CommSession.oneStepT(page, '#address', project.address);
        await CommSession.oneStepC(page, '#basediv > div.row.lc-formMain > div > div:nth-child(4) > button');
        await CommSession.oneStepC(page, '#lc-map-save', _static.fiveT);
    }

    async checkedRegion(page, project) {
        await page.waitFor(_static.threeT);
        await CommSession.oneStepC(page, '#address1');
        await page.waitFor(_static.oneT);
        await page.keyboard.press('ArrowDown');
        await page.keyboard.press('Enter');

        let municipality = project.city.includes(_static.shanghai);
        await selectRegion(page, project.province, '#address2');
        await selectRegion(page, municipality ? project.district : project.city, '#address3');
        await selectRegion(page, municipality ? project.market : project.district, '#address4');
    }

    async checkWhAdvantage(page, whAdvantage) {
        if (util._coll.isEmpty(whAdvantage)) {
            whAdvantage = [{index: 1, name: '可以分割'}];
        }
        await CommSession.oneStepC(page, '#basediv > div.row.lc-formMain > div > div:nth-child(15) > div > div > button');
        let names = CommSession.getNames(whAdvantage);
        let elementSel = '#basediv > div.row.lc-formMain > div > div:nth-child(15) > div > div > div > ul > li';
        await this.checkOption(page, names, elementSel);
    }

    async checkBuildStatus(page, buildStatus) {
        if (util._coll.isEmpty(buildStatus)) {
            await CommSession.oneStepC(page, '#jszt1');
        }
        if (buildStatus == 1) {
            await CommSession.oneStepC(page, '#jszt2');
            return;
        }
        await CommSession.oneStepC(page, '#jszt1');
    }

    async checkStorageType(page, storageType) {
        await CommSession.oneStepC(page, '#basediv > div.row.lc-formMain > div > div:nth-child(7) > div > div > button > span.filter-option.pull-left');
        if (util._coll.isEmpty(storageType)) {
            storageType = [{index: 1, name: '其他'}];
        }
        let names = CommSession.getNames(storageType);
        let elementSel = '#basediv > div.row.lc-formMain > div > div:nth-child(7) > div > div > div > ul > li';
        await this.checkOption(page, names, elementSel);
    }

    /**
     * 库型选择
     */
    async checkWhType(page, whType) {
        if (util._coll.isEmpty(whType)) {
            whType = [{index: 7, name: '其他'}];
        }
        let names = CommSession.getNames(whType);
        await CommSession.oneStepC(page, '#basediv > div.row.lc-formMain > div > div:nth-child(8) > div > div > button > span.filter-option.pull-left');
        let elementSel = '#basediv > div.row.lc-formMain > div > div:nth-child(8) > div > div > div > ul > li';
        await this.checkOption(page, names, elementSel);
    }

    async checkBondedRegion(page, bondedRegion) {
        if (util._obj.isEmpty(bondedRegion) || bondedRegion == 0) {
            await CommSession.oneStepC(page, '#sfbsq2');
            return;
        }
        await CommSession.oneStepC(page, '#sfbsq1');
    }

    async checkTaxOrFee(page, taxOrManagementFee) {
        if (taxOrManagementFee == 1) {
            await CommSession.oneStepC(page, '#sfhshwy2');
            return;
        }
        if (taxOrManagementFee == 2) {
            await CommSession.oneStepC(page, '#sfhshwy3');
            return;
        }
        if (taxOrManagementFee == -1) {
            await CommSession.oneStepC(page, '#sfhshwy1');
            return;
        }
        await CommSession.oneStepC(page, '#sfhshwy1');
    }
}

async function gerRegionNodes(page, regionSelector) {
    await page.click(regionSelector);
    await page.waitFor(_static.threeT);
    let regions = await page.$$(regionSelector + ' > option');

    if (util._coll.isEmpty(regions)) {
        await page.click(regionSelector);
        await page.waitFor(_static.fiveT);
        regions = await page.$$(regionSelector + ' > option');
    }
    return regions;
}

async function selectRegion(page, region, regionSelector) {
    let regions = await gerRegionNodes(page, regionSelector);
    if (util._coll.isEmpty(regions)) {
        return;
    }
    let mousePCount = 0;
    for (let pIx = 1; pIx <= regions.length; pIx++) {
        let optionText = await page.$eval(regionSelector + ' > option:nth-child(' + pIx + ')', el => el.innerText);
        if (optionText.includes(region) || region.includes(optionText)) {
            mousePCount = pIx;
            break;
        }
    }
    for (let pc = 1; pc < mousePCount; pc++) {
        await page.keyboard.press('ArrowDown');
    }
    await page.keyboard.press('Enter');
}

module.exports = Session;