const fs = require('fs');
const sleep = require('sleep');
const _static = require('./static');

const notEmpty = function (_obj) {
    return _obj !== undefined && _obj !== null && _obj !== '';
}

const isEmpty = function (_obj) {
    return _obj === undefined || _obj === null || _obj === '';
}

const arrayNotEmpty = function (_array) {
    return _array !== undefined && _array !== null && _array.length > 0;
}

const arrayIsEmpty = function (_array) {
    return _array === undefined || _array === null || _array.length === 0;
}

const tcLog = function (txt) {
    console.log("========>" + txt);
}

function writeLog(text) {
    try {
        sleep.sleep(_static.one);
        fs.writeFile("log.txt", new Date().Format("MM-dd hh:mm:ss") + ' ======> ' + text + "\n", {flag: "a"}, function (err) {
            if (err) {
                console.log(err);
            }
        });
    } catch (e) {
    }
}

function logTOPath(text, path) {
    try {
        sleep.sleep(_static.one);
        fs.writeFile(path, new Date().Format("MM-dd hh:mm:ss") + text + "\n", {flag: "a"}, function (err) {
            if (err) {
                console.log(err);
            }
        });
    } catch (e) {
    }
}

const isTrue = function (_boolean) {
    return _boolean !== undefined && _boolean != null && _boolean === true;
}

const isFalse = function (_boolean) {
    return _boolean === undefined || _boolean == null || _boolean === false
}

/**
 * 返回默认值
 * @param original  原本的值 存在的话 会返回一个字符串
 * @param _defaultV 默认值
 * @returns {string}
 * @private
 */
const _default = function (original, _defaultV = '') {
    return isEmpty(original) ? _defaultV : original;
}

/**
 * 加减日期
 * @param original  日期
 * @param day       增减天数
 */
const addOrSubDay = function (original, day) {
    return new Date(original.getTime() + day * 24 * 60 * 60 * 1000);
}

module.exports = {
    isTrue: isTrue,
    isFalse: isFalse,
    tcLog: tcLog,
    writeLog: writeLog,
    _default: _default,
    _obj: {
        notEmpty: notEmpty,
        isEmpty: isEmpty,
    },
    _coll: {
        notEmpty: arrayNotEmpty,
        isEmpty: arrayIsEmpty,
    },
    addOrSubDay: addOrSubDay,
    logTOPath: logTOPath,
};

