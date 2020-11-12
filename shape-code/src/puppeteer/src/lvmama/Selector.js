class Selector {
    constructor() {
        this.loginUrl = 'http://hubeimiddle.joyuai.com/#/login';
        this.loginSuccess = "http://hubeimiddle.joyuai.com/#/dashboard";
        this.username = '#app > div > form > div:nth-child(2) > div > div > input';
        this.password = '#app > div > form > div:nth-child(3) > div > div > input';
        this.submitBtn = '#app > div > form > button';

        this.customerSelect = 'http://hubeimiddle.joyuai.com/#/customerManage/customerInfo/customerSelect';
        this.totalCount = '#app > div > div.main-container > section > div > div > div.el-pagination > span.el-pagination__total';
        this.row = {
           prefix : '#app > div > div.main-container > section > div > div > div.el-pagination > ul > li'
        };
        this.phoneSelect = {
            prefix : '#app > div > div.main-container > section > div > div > div.el-table.el-table--fit.el-table--border.el-table--fluid-height.el-table--enable-row-hover.el-table--enable-row-transition.el-table--medium > div.el-table__body-wrapper.is-scrolling-none > table > tbody > tr',
            suffix : '> td.el-table_1_column_3.is-center > div'
        }

    }
}

module.exports = Selector;