module.exports = {
    lianCang: {
        urls: {
            login: 'https://center.warehousing.com',
            loginSuccess: 'https://center.warehousing.com/mainframe.html',
            postCf: 'https://center.warehousing.com/platform-service/warehouse/addWarehouse.html?language=zh_CN&type=1',
            postCk: 'https://center.warehousing.com/platform-service/warehouse/addWarehouse.html?language=zh_CN&type=1',
            cfSuccessUrl: 'https://center.warehousing.com/platform-service/warehouse/warehouseList.html',
            ckSuccessUrl: 'https://center.warehousing.com/platform-service/warehouse/warehouseList.html',
            ckListUrl: 'https://center.warehousing.com/platform-service/warehouse/warehouseList.html',
            cfListUrl: 'https://center.warehousing.com/platform-service/warehouse/warehouseList.html',
        },
        loginSel: {
            username: '#userName',
            password: '#password',
            captcha: '#yzm',
            yzmPhoto: 'imgCode',
            submit: '#loginform > div > button',
        },
        citySel: {}
    },
    zgzs: {
        urls: {
            login: 'http://www.zhaoshang800.com/index.php?c=login&a=index',
            loginSuccess: 'http://agentadmin.zhaoshang800.com/index/home',
            postCf: 'http://agentadmin.zhaoshang800.com/info/new/plant',
            postCk: 'http://agentadmin.zhaoshang800.com/info/new/plant',
            cfSuccessUrl: 'http://agentadmin.zhaoshang800.com/info/new/success/plant',
            ckSuccessUrl: 'http://agentadmin.zhaoshang800.com/info/new/success/warehouse',
            ckListUrl: 'http://agentadmin.zhaoshang800.com/index/info/warehouse',
            cfListUrl: 'http://agentadmin.zhaoshang800.com/index/info/',
        },
        loginSel: {
            username: '#user_names',
            password: '#user_pass',
            captcha: '#code',
            yzmPhoto: 'codeimage',
            submit: '.lgbut'
        },
        citySel: {}
    },
    cfw: {
        urls: {
            login: 'http://user.99cfw.com/login/',
            loginSuccess: 'http://user.99cfw.com/',
            postCf: 'http://user.99cfw.com/fabu/fb_cf.asp?p=1&g=1',
            postCk: 'http://user.99cfw.com/fabu/fb_cc.asp?p=3&g=1',
            cfSuccessUrl: 'http://user.99cfw.com/fabu/fabuEnd.asp',
            ckSuccessUrl: 'http://user.99cfw.com/fabu/fabuEnd.asp',
            ckListUrl: '',
            cfListUrl: '',
        },
        loginSel: {
            username: '#loginName',
            password: '#password',
            captcha: '',
            yzmPhoto: '',
            submit: '#userlogin > table > tbody > tr:nth-child(7) > td > button'
        },
        citySel: {
            shanghai: '#content > dl > dd:nth-child(34) > a:nth-child(1)',
            zhengzhou: '#content > dl > dd:nth-child(44) > a:nth-child(1)',
            wuxi: '#content > dl > dd:nth-child(38) > a:nth-child(1)',
            suzhou: '#content > dl > dd:nth-child(34) > a:nth-child(2)',
            nanjing: '#content > dl > dd:nth-child(26) > a:nth-child(2)',
        }
    },
    tc: {
        urls: {
            login: 'https://vip.anjuke.com/login/',
            loginSuccess: 'https://vip.anjuke.com/broker/transfernotice',
            postCf: 'http://vip.anjuke.com/house/publish/factory/?jpChooseType=3',
            postCk: 'http://vip.anjuke.com/house/publish/factory/?jpChooseType=3',
            cfSuccessUrl: '',
            ckSuccessUrl: '',
            ckListUrl: 'https://vip.anjuke.com/sydchug/list/sydc',
            cfListUrl: 'https://vip.anjuke.com/sydchug/list/sydc',
        },
        loginSel: {
            username: '#loginName',
            password: '#loginPwd',
            captcha: '#seccodetxt',
            yzmPhoto: '#seccode',
            submit: '#loginSubmit'
        },
        citySel: {}
    },
}
