const Api = require('../base/api');
const util = require('../base/utils');
const enums = require('../base/enum');

class Project {

    constructor(id, customize) {
        return new Promise(async (resolve, reject) => {
            // site 项目提高的一些 标题 描述 区域 图片
            const project = await Api.method.queryProject(id).catch(error => util.tcLog(error));
            // promotion 提高的发帖归集信息
            const newProject = await Api.method.newProject(id).catch(error => util.tcLog(error));
            if (util._obj.isEmpty(project) || util._obj.isEmpty(newProject)) {
                reject('获取项目失败');
                return null;
            }
            // 项目编号
            this.id = project.id;
            // 标题
            this.title = await getTitle(this, customize);
            // 描述
            this.desc = await getDesc(project.id, customize);
            // 区域
            setRegion(this, project.locationVo, customize);
            // 面积
            this.area = getArea(project, customize);
            // 价格
            this.price = project.minPrice;
            // 第一个在租单元 原因判断是否满租
            this.oneRental = getOneRental(project);
            // 图片
            this.photoUrls = util._coll.notEmpty(customize.photoUrls) ? customize.photoUrls : project.photoUrls;
            // 仓库或者厂房
            this.useType = util._obj.notEmpty(customize.useType) ? customize.useType : newProject.useType;
            // 是否含税含物业
            this.taxOrManagementFee = util._default(newProject.taxOrManagementFee, 1);
            // 存储类型
            this.storageTypeList = newProject.storageTypeList;
            // 库型
            this.whTypeList = newProject.whTypeList;
            // 起租期限
            this.leaseTerm = getLeaseTerm(newProject.leaseTerm);
            // 是否保税区 0 :否  1：是
            this.bondedRegion = util._default(newProject.bondedRegion, 0);
            // 建设状态 1 : 建设中  2：已建成
            this.buildStatus = util._default(newProject.buildStatus, 1);
            // 仓库优势
            this.whAdvantageList = newProject.whAdvantageList;
            // 总楼层
            this.maxFloor = util._default(newProject.maxFloor, 1);
            // 消防资质
            this.fireQualification = util._default(newProject.fireQualification, 2);
            // 地坪材质
            this.floorMaterialList = newProject.floorMaterialList;
            // 建筑结构
            this.buildStructureList = newProject.buildStructureList;
            // 仓库规格 按楼层分布
            this.floorList = newProject.floorList;
            // 仓库设施
            this.whFacilityList = newProject.whFacilityList;
            // 库内设施
            this.equipmentList = newProject.equipmentList;
            // 园区安保
            this.securityList = newProject.securityList;
            // 配电量
            this.kva = util._default(newProject.kva, 0);
            //---------------------------cfw--------------------------
            // 在租总面积
            this.vacantArea = newProject.vacantArea;
            // 电量剩余量
            this.kvaLeft = newProject.kvaLeft;
            // 适合行业
            this.suitableForIndustry = newProject.suitableForIndustry;
            // 路名
            this.address = newProject.address;
            // 合成地址
            this.fullAddress = getFullAddress(this);
            // 偏移地址
            this.mockAddress = getMockAddress(newProject.mockAddress);
            // 是否有消防
            this.hasFire = newProject.hasFire;
            // ------------------------ zgzs--------------------------
            // 厂房类型
            this.plantType = newProject.plantType;
            // 厂房结构
            this.oneStructure = newProject.oneStructure;
            // 厂房楼层
            this.floorType = newProject.floorType;
            // 食堂
            this.canteen = newProject.canteen;
            // 特色卖点
            this.featuredPointList = newProject.featuredPointList;
            // 底层层高
            this.maxFloorH = util._default(newProject.maxFloorH, 5);
            // 仓库类型
            this.warehouseType = newProject.warehouseType;
            // 仓库结构
            this.ckStructure = newProject.ckStructure;
            // 地坪功能
            this.floorFunction = newProject.floorFunction;
            // 地坪材质
            this.floorMaterial = newProject.floorMaterial;
            // 仓库卖点
            this.ckPointList = newProject.ckPointList;
            return resolve(this);
        });
    }
}

function getFullAddress(_this) {
    let fullAddress = _this.district;
    fullAddress  = util._obj.notEmpty(_this.market) ? (fullAddress + _this.market) : fullAddress;
    fullAddress  = util._obj.notEmpty(_this.address) ? (fullAddress + _this.address) : fullAddress;
    return fullAddress;
}

function getMockAddress(originalAddress) {
    if (util._obj.isEmpty(originalAddress)) {
        return null;
    }
    let mockAddress = originalAddress;
    mockAddress = mockAddress.replace('靠近', '');
    mockAddress = mockAddress.replace('交叉口', '');
    mockAddress = mockAddress.replace('\t', '');
    mockAddress = mockAddress.replace('', '');
    return mockAddress;
}

function getLeaseTerm(leaseTerm) {
    if (util._obj.isEmpty(leaseTerm)) {
        return 12;
    }
    if (leaseTerm === enums.RENTAL_DURATION.ONE_MONTH) {
        return 1;
    }
    if (leaseTerm === enums.RENTAL_DURATION.THREE_MONTH) {
        return 3;
    }
    if (leaseTerm === enums.RENTAL_DURATION.SIX_MONTH) {
        return 6;
    }
    if (leaseTerm === enums.RENTAL_DURATION.ONE_YEAR) {
        return 12;
    }
    if (leaseTerm === enums.RENTAL_DURATION.TWO_YEAR) {
        return 24;
    }
    if (leaseTerm === enums.RENTAL_DURATION.THREE_YEAR) {
        return 36;
    }
    if (leaseTerm === enums.RENTAL_DURATION.FOUR_YEAR) {
        return 48;
    }
    if (leaseTerm === enums.RENTAL_DURATION.FIVE_YEAR) {
        return 60;
    }
    return 12;
}

function getOneRental(project) {
    if (util._coll.isEmpty(project.buildingList[0])) {
        return null;
    }
    if (util._coll.isEmpty(project.buildingList[0].rentalUnitList[0])) {
        return null;
    }
    return project.buildingList[0].rentalUnitList[0];
}

function setRegion(_this, locationVo, customize) {
    if (util._obj.notEmpty(customize) && util._obj.notEmpty(customize.province)) {
        _this.province = customize.province;
        _this.city = customize.city;
        _this.district = customize.district;
        _this.market = customize.market;
        return;
    }
    if (util._obj.isEmpty(locationVo)) {
        return;
    }
    _this.province = locationVo.provinceName;
    _this.city = locationVo.cityName;
    _this.district = locationVo.districtName;
    _this.market = locationVo.marketName;
}

function getArea(project, customize) {
    if (util._obj.notEmpty(customize.area) && customize.area > 0){
        return customize.area;
    }
    let area = util._default(project.buildingArea, 1000);
    return area.replace(',', '');
}

/**
 * 得到标题
 * @param _this   site项目信息
 * @param customize  自定义属性
 */
async function getTitle(_this, customize) {
    if (util._obj.notEmpty(customize) && util._obj.notEmpty(customize.title)) {
        return customize.title;
    }
    if (_this.useType === enums.USE_TYPE.changfang) {
        return await Api.method.productTitle(_this.id);
    }
    return await Api.method.storageTitle(_this.id);
}

/**
 * 如果未自定义 则取面积到周边
 * @param projectId
 * @param customize
 * @returns {Promise<*>}
 */
async function getDesc(projectId, customize) {
    if (util._obj.notEmpty(customize.desc)) {
        return customize.desc;
    }
    let desc = await Api.method.description(projectId);
    if (util._obj.isEmpty(desc)) {
        return '';
    }
    return desc.substring(desc.indexOf('【面积】'), desc.indexOf('【用途】'));
}

module.exports = Project;