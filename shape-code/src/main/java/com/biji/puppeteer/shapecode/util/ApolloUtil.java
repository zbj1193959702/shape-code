package com.biji.puppeteer.shapecode.util;

import com.ctrip.framework.apollo.ConfigService;

/**
 * create by biji.zhao on 2020/11/10
 */
public class ApolloUtil {

    private static final String application = "application";

    public static Integer getLimitCount() {
        return ConfigService.getConfig(application).getIntProperty("text.search.light.limit.count", 100);
    }

    public static String yzmUsername() {
        return ConfigService.getConfig(application).getProperty("yzm code username", "hainiugen");
    }

    public static String yzmPassword() {
        return ConfigService.getConfig(application).getProperty("yzm code password", "104127");
    }
}
