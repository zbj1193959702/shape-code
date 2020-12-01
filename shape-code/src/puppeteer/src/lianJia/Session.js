const puppeteer = require('puppeteer');
const CommSession = require('../platform/CommSession');
const Util = require('../base/utils');
const _static = require('../base/static');
const Api = require('../base/api');

class Session {

    constructor(selector, headless = true) {
        return new Promise(async (resolve, reject) => {
            if (selector == null) {
                reject('empty selector')
            }
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
        await page.setViewport({
            width: 1400,
            height: 1024
        });

        for (let i = 3; i <= 100; i++) {
            try {
                try {
                    await page.goto(selector.pageList+"pg" + i + "/", {waitUntil: 'networkidle0'});
                    await page.waitFor(_static.twoT);
                }catch (e) {
                    await page.goto(selector.pageList+"pg" + i + "/", {waitUntil: 'networkidle0'});
                    await page.waitFor(_static.twoT);
                }
                let items = await page.$$(selector.pageItem);

                await page.waitFor(items.length);
                if (items.length < 1) {
                    continue;
                }
                for (let liIdx = 1; liIdx <= items.length; liIdx++) {
                    // 标题
                    let title = await CommSession.getText(page, CommSession.childNode(selector.itemOne.prefix, liIdx, selector.itemOne.titleSuffix));
                    // 详情页地址
                    let detailUrl = await page.evaluate((selector) => {
                        if (document.querySelector(selector) != null) {
                            return document.querySelector(selector).href;
                        }
                        return null;
                    }, CommSession.childNode(selector.itemOne.prefix, liIdx, selector.itemOne.titleSuffix));
                    // 地址
                    let address = await CommSession.getText(page, CommSession.childNode(selector.itemOne.prefix, liIdx, selector.itemOne.distinctSuffix));
                    address += await CommSession.getText(page, CommSession.childNode(selector.itemOne.prefix, liIdx, selector.itemOne.addressSuffix));
                    // 图片
                    let firstImage = await page.evaluate((idx) => {
                        window.scroll(0, 500 * idx);
                        let sel = '.sellListContent > li:nth-child('+idx+') > a > img.lj-lazy';
                        let src = '';
                        if (document.querySelector(sel) != null) {
                            src =  document.querySelector(sel).getAttribute("src")
                        }
                        if (src === '' || src == null) {
                            return null;
                        }
                        // 没有加载出来
                        if (src !== 'https://s1.ljcdn.com/feroot/pc/asset/img/blank.gif?_v=20201125232054') {
                            return src;
                        }
                        window.scroll(0, 400 * idx);
                        return document.querySelector(sel).getAttribute("src");
                    }, liIdx);
                    // 规格
                    let norms = await CommSession.getText(page, CommSession.childNode(selector.itemOne.prefix, liIdx, selector.itemOne.normsSuffix));
                    // 价格
                    let price = await CommSession.getText(page, CommSession.childNode(selector.itemOne.prefix, liIdx, selector.itemOne.priceSuffix));
                    if (firstImage == null || firstImage === 'https://s1.ljcdn.com/feroot/pc/asset/img/blank.gif?_v=20201125232054') {
                        continue;
                    }
                    await Api.method.saveHouse(title, detailUrl, address, firstImage, norms, price);
                }
            }catch (e) {

            }
        }
    }
}


module.exports = Session;