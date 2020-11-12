const util = require('../base/utils');

class Customize {

    constructor(session) {
        return new Promise(async (resolve, reject) => {
            if (util._obj.isEmpty(session)) {
                reject('empty param')
            }
            this.title = session.title;
            this.desc = session.description;
            // 自定义类型
            this.useType = session.useType;
            // 自定义区域
            this.location = session.location;
            // 面积
            this.area = session.area;
            // 图片
            this.photoUrls = session.photoUrls;
            return resolve(this);
        });
    }
}

module.exports = Customize;