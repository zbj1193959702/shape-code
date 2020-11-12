class Result {
    constructor(code, msg) {
        return new Promise(async (resolve, reject) => {
            if (code === null || msg === null) {
                reject('构造Result失败');
            }
            this.code = code;
            this.msg = msg;
            return resolve(this);
        });
    }
}

module.exports = Result;