const util = require('./base/utils');
const Session = require('./lvmama/Session');
const Selector = require('./lvmama/Selector');

async function main() {
    let account = {
        username : 'zhaobiji',
        password : 'zhaobiji'
    };
    let selector = new Selector();
    const session = await new Session(account, selector, false);
    // let loginRes = await session.login();
    // if (loginRes === false) {
    //     util.tcLog('登陆失败');
    //     return ;
    // }
    // await session.execute(selector);
    await session.scenicSpot(selector);
}

main().then(r => util.writeLog(r));






