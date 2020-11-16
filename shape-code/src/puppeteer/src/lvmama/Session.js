const puppeteer = require('puppeteer');
const CommSession = require('../platform/CommSession');
const Util = require('../base/utils');
const _static = require('../base/static');
const Api = require('../base/api');

class Session {

    constructor(account, selector, headless = true) {
        return new Promise(async (resolve, reject) => {
            if (!account.password) {
                Util.tcLog('error! ' + account.username);
                reject('empty password')
            }
            this.selector = selector;
            this.username = account.username;
            this.password = account.password;

            this.browser = await puppeteer.launch({
                args: ['--no-sandbox', '--disable-setuid-sandbox',
                    '--disable-gpu', '--disable-dev-shm-usage',
                    '--no-first-run', '--no-zygote', '--unhandled-rejections=strict'],
                headless: headless,
                slowMo: 20,
            }).catch(reason => reject(reason));
            return resolve(this);
        })
    }

    async close() {
        await this.browser.close()
    }

    async execute(selector){
        const page = await this.browser.newPage();
        page.setViewport({
            width: 1400,
            height: 1024
        });
        await page.goto(selector.customerSelect, {waitUntil: 'networkidle0'});
        await page.waitFor(_static.threeT);

        let memberCountText = await CommSession.getText(page, selector.totalCount);
        let memberCount = memberCountText.replace(" ", "")
            .replace("共", "")
            .replace("条", "");
        let pageSize = memberCount % 10 === 0 ? memberCount / 10 : (memberCount / 10) + 1;
        for (let i = 1; i <= pageSize; i++) {
            await CommSession.oneStepC(page, CommSession.childNode(selector.row.prefix, i));
            await page.waitFor(_static.threeT);
            for (let j = 1; j <= 10; j++) {
                let phoneSelect = CommSession.childNode(selector.phoneSelect.prefix, j, selector.phoneSelect.suffix);
                if (phoneSelect === null) {
                    break;
                }
                let phone = await CommSession.getText(page, phoneSelect);
                Util.tcLog(phone);
                await Api.method.saveCustomer(phone);
            }
        }
    }

    async login() {
        const page = await this.browser.newPage();
        page.setViewport({
            width: 1400,
            height: 1024
        });

        await page.goto(this.selector.loginUrl, {waitUntil: 'networkidle0'});
        await CommSession.oneStepT(page, this.selector.username, this.username);
        await CommSession.oneStepT(page, this.selector.password, this.password);
        await CommSession.oneStepC(page, this.selector.submitBtn);

        await page.waitFor(_static.twoT);
        let url = await page.url();
        if (url === this.selector.loginSuccess) {
            Util.tcLog('login success');
            return true;
        }
        return false;
    }

    async scenicSpot(selector) {
        const page = await this.browser.newPage();
        page.setViewport({
            width: 1400,
            height: 1024
        });
        await page.goto(selector.lvmama.page, {waitUntil: 'networkidle0'});
        await page.waitFor(_static.threeT);

        for (let i = 1; i < 17; i++) {
            await CommSession.oneStepT(page, selector.lvmama.pageNumber, i);
            await CommSession.oneStepC(page, selector.lvmama.pageSubmit);
            await page.waitFor(_static.twoT);
            let items = await page.$$(selector.lvmama.tableItem);
            if (items.length <= 0) {
                return ;
            }
            for (let j = 1; j <= items.length; j++) {
                let itemSelector = CommSession.childNode(selector.lvmama.tableItem, j);
                let title = await CommSession.getText(page, itemSelector + selector.lvmama.title);
                let price = await CommSession.getText(page, itemSelector + selector.lvmama.price);
                let photo = itemSelector + selector.lvmama.photo;
                let photoUrl = await page.evaluate((selector) => {
                    if (document.querySelector(selector) != null) {
                         return document.querySelector(selector).src;
                    }
                    return null;
                }, photo);
                let common = await CommSession.getText(page, itemSelector + selector.lvmama.common);
                if (photoUrl === null) {
                    continue;
                }
                await Api.method.scenicSpot(title, price, photoUrl, common);
            }
        }
        Util.tcLog("========end================")
    }
}


module.exports = Session;