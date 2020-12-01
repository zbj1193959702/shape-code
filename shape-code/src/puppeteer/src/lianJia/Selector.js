class Selector {
    constructor() {
        this.pageList =  'https://sh.lianjia.com/ershoufang/';
        this.pageItem = '#content > div.leftContent > ul > li';
        this.itemOne = {
            prefix: '#content > div.leftContent > ul > li',
            titleSuffix: ' > div.info.clear > div.title > a',
            distinctSuffix: '> div.info.clear > div.flood > div > a:nth-child(2)',
            addressSuffix: '> div.info.clear > div.flood > div > a:nth-child(3)',
            imageSuffix: '> a > img.lj-lazy',
            normsSuffix: '> div.info.clear > div.address > div',
            priceSuffix: '> div.info.clear > div.priceInfo > div.unitPrice > span'
        }
    }
}

module.exports = Selector;