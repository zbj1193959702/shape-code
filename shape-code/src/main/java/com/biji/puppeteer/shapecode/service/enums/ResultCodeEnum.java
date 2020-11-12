package com.biji.puppeteer.shapecode.service.enums;

/**
 * By zhenyu.yue
 */
public enum ResultCodeEnum {

    /**
     * 错误码规范定义
     * 6位错误码
     * ABCDEF
     * AB代表系统      00-系统预留 01-CMS系统  02-SITE
     * CD代表大功能    CMS系统中01-发布功能 02-微信工作号
     * EF代表大功能下
     */

    SUCCESS("000000","成功!"),

    SYSTEM_ERROR_UN_KNOW("000101","系统错误!"),
    UN_EXPECT_PARAMS("000102","参数错误!"),



    /** 项目发布错误码 0101XX */

    NO_MARKET("010101","项目子市场不合法"),
    NO_PRO_CITY_AREA("010102","项目所属区域不合法"),
    NO_LEND_DATE_AREA_PRICE("010103","项目在未来6个月内无可租面积或出租价格"),
    NO_AVAILABLE_BUILDINGS("010104","项目无可用物业"),
    NO_AVAILABLE_FLOOR("010105","物业无楼层信息"),
    NO_AVAILABLE_FLOOR_PRICE("010106","楼层无价格信息"),
    YUE_TAI_TYPE_NULL("010107","物业高台库无月台信息"),
    YUE_TAI_HEIGHT_WIDTH_NULL("010108","物业月台高度或宽度为空"),

    NO_BUILDING_TYPE("010109","无物业类型"),
    NO_BUILDING_USAGE_TYPE("010110","物业无使用类型"),
    NO_BUILDING_UNLOAD_TYPE("010111","物业无卸货类型"),
    FLOOR_SIZE_NULL("010112","物业楼层尺寸载重信息有误"),

    BUILDING_UNLOAD_DOOR_DETAIL_ERR("010114","物业卸货门详情错误"),

    BUILDING_FIRE_RATING_ERR("010115","物业消防等级错误"),
    BUILDING_FLOORING_ERR("010116","物业地坪错误"),


    BUILDING_FIRE_FACILITIES_ERR("010117", "物业消防设施错误"),
    BUILDING_UNLOAD_AREA_WIDTH_ERR("010118", "物业周转场地宽度错误"),
    BUILDING_LICENSE_ERR("010119", "物业无证件信息"),
    PROJECT_HEAD_PHOTO_ERR("010120", "项目篇首图不正确"),
    PROJECT_MAP_PHOTO_ERR("010121", "项目地理位置图不正确"),
    PROJECT_PLAN_PHOTO_ERR("010122", "项目总平图图不正确"),
    BUILDING_OUTER_FRONT_PHOTO_ERR("010123", "物业外部正面图必须不少于2张"),
    BUILDING_INNER_PHOTO_ERR("010124", "物业内部图必须大于三张"),

    PROJECT_NO_SLOGAN_ERR("010125", "项目Slogan未设置"),
    PROJECT_NO_ADVANTANGE_ERR("010126", "项目优势未设置"),

    BUILDING_NO_LEASE("010127","物业无租约信息"),
    BUILDING_NO_LEND_TOTAL_AREA("010128", "物业无可租赁总面积"),

    PROJECT_NO_GATE_PHOTO_ERR("010129", "项目无大门图"),
    PROJECT_NO_OUTER_ROAD_ERR("010130", "项目无外部道路图"),

    BUILDING_NO_COMMERCIAL_TYPE("010131", "物业经营类型未设置"),
    BUILDING_NO_LAT_LNG_TYPE("010132", "项目经纬度信息错误"),
    BUILDING_NO_LENDING_BUILDING_TYPE("010133", "项目无在租仓库"),
    BUILDING_NO_ENOUGH_PHOTO_TYPE("010134", "物业照片不全"),

    LEADS_NOT_QA_APPROVED("010135", "LEADS审核未通过"),
    PROJECTA_BUILDING_AREA_NULL("010136","甲A类建筑面积未填写"),
    NOT_PROJECTA_BUILDING_AREA_NULL("010137","非甲A类建筑面积未填写"),

    PROJECT_NO_SATELLITE_PLAN_ERR("010138", "项目无卫星平面图"),
    PROJECT_NO_DRAWING_OR_PLAN_ERR("010139", "甲A项目无效果或总平面图"),

    BUILDING_FIRSTA_OUTER_FRONT_PHOTO_ERR("010140", "物业外部图不足"),
    BUILDING_FIRSTA_INNER_FRONT_PHOTO_ERR("010141", "物业内部图不足"),
    BUILDING_ON_CONSTRUCT_PHOTO_ERR("010142", "物业在建工程图不足"),

    BUILDING_NOT_FIRSTA_OUTER_FRONT_PHOTO_ERR("010143", "非甲A物业外部正面图不足"),
    BUILDING_NOT_FIRSTA_INNER_FRONT_PHOTO_ERR("010144", "非甲A物业内部正面图不足"),
    BUILDING_TYPE_NULL("010145","物业类型未设置"),
    PROJECT_NO_CONTACT("010146","项目至少得有一个联系人"),
    BUILDING_NO_BANDING("010147","在建项目未描边"),
    UNOCCUPIED_AREA_OVERFLOW("010148", "空置面积不能大于所选物业的建筑面积"),




    NO_OPEN_ID("010201","系统内未查询到微信openId"),
    NO_ACCESS_TOKEN("010202","未获得accessToken"),


    ERROR_POST_MSG("010203", "请求微信发送接口异常"),
    ERROR_RECEIVE_MSG("010204", "请求微信发送接口返回异常"),
    GET_MINI_QRCODE_ERROR("010205", "获取小程序二维码失败");

    public String code;
    public String defaultMsg;

    ResultCodeEnum(String code, String defaultMsg){
        this.code = code;
        this.defaultMsg = defaultMsg;
    }


    @Override
    public String toString() {
        return defaultMsg;
    }
}
