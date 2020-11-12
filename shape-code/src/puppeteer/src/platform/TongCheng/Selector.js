class Selector {
    constructor() {
        // 类型
        this.useType = {
            click: '#select_plantType > div > div > span',
            option: '#select_plantType > div > ul > li' //   :nth-child(3)
        };
        // 租赁方式
        this.leaseWay = {
            all: '#all-tenancy',
            some: '#some-tenancy',
        };
        // 房源编号
        this.projectId = '#publish_form > div:nth-child(6) > input';
        // 区域
        this.district = {
            click: '#select_zone > div > div > span',
            option: '#select_zone > div > ul > li', // :nth-child(3)
        };
        // 板块 - 子市场
        this.market = {
            click: '#select_block > div > div > span',
            option: '#select_block > div > ul > li'
        };
        // 地址
        this.address = {
            click: '#detail_address',
            option: {
                prefix: '#publish_form > div.ui-form.detail-address-box > div > ul > li',
                suffix: ' > span',
            },
        };
        // 面积
        this.area = '#publish_form > div:nth-child(15) > input';
        // 楼层
        this.floor = {
            click: '#select_floor_type > div > div > span',
            option: '#select_floor_type > div > ul > li'
        };
        // 层高
        this.floorH = '#first_floor_box > input';
        // 配套设施
        this.facility = '#fitment-services > label'; // :nth-child(2) > input
        // 房源标签
        this.tag = '#houseLabel-services > label';  // :nth-child(2) > input
        // 租金
        this.price = '#rent_price_input';
        // 起租期
        this.leaseTerm = '#renttime_mode_box > input';
        // 支付方式
        this.payWay = {
            pay: '#pay_way_box > input:nth-child(3)', // 1~99
            bet: '#pay_way_box > input:nth-child(6)' // 0~99
        };
        // 标题
        this.title = '#publish_form > div:nth-child(36) > input';
        // 描述
        this.desc = {
            iframe: '.ke-edit-iframe',
            text: '#txt_1',
        };
        // 图片
        this.photo = '#pic-fileupload';
        // 提交
        this.submit = '#publish-jpoffice-add > span.submit-text';

        // 端口
        this.port = {
            click: '#port-select > div > div',
            option: '#port-select > div > ul > li'
        };
        // 查询
        this.query = 'body > div.content > div.layout-right > div.page > div > div.form-search > div.search-content.operating > button.ui-button.ui-button-small.search-btn';
        // 顺序
        this.sort = 'body > div.content > div.layout-right > div.page > div > div.sydclist-head > div.sort > span.edit-sort.desc.select';
        // 勾选项
        this.checkbox = {
            prefix: 'body > div.content > div.layout-right > div.page > div > table > tbody > tr',
            suffix: '> td.table-input-detailed > input'
        };
        // 下架btn
        this.batchOff = '#batch-off';
        // 上架btn
        this.batchOn = '#batch-on';
        // 编号
        this.postNo = {
            prefix: 'body > div.content > div.layout-right > div.page > div > table > tbody > tr',
            suffix: '> td:nth-child(2) > div > div.text-box > p:nth-child(4) > span:nth-child(1)',
        };
        // 下架确认
        this.off = {
            success: '#off_shelf_result > div.result-area > p.result-title > i',
        };
        // 上架确认
        this.on = {
            box: '#on_shelf_process_operate > table > tbody > tr > td.check-op > input',
            submit: '#on_shelf_process_operate > div.choice-detail > input',
            success: '#on_shelf_process_result > div.result-area > p.result-title > i',
        };
        // 推广数量
        this.promote = {
            // 可推广
            canPromote: 'body > div.content > div.layout-right > div.page > div > div.top-info > div > div > span.to-grounding > span',
            // 推广中
            promoting: 'body > div.content > div.layout-right > div.page > div > div.top-info > div > div > span.left.have-grounding.in-promotion > span',
        };
        this.page = '#pager > a';
        // 列表页标题
        this.trTitle = {
            prefix: 'body > div.content > div.layout-right > div.page > div > table > tbody > tr',
            suffix: ' > td:nth-child(2) > div > div.text-box > p.title > a',
        };
        this.prompt = {
            dialog: '#no-balance-dialog > div.ui-dialog-title',
            remove: '#no-balance-dialog > div.ui-dialog-title > a'
        };
        this.photoError = {
            dialog: '#alert',
            remove: '#alert > div.ui-dialog-content > a'
        }
    }
}

module.exports = Selector;