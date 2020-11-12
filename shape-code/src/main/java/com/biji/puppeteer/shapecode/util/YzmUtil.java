package com.biji.puppeteer.shapecode.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class YzmUtil {

    public static String yzmCode(MultipartFile partFile) throws Exception {
        if (partFile == null || StringUtils.isEmpty(partFile.getOriginalFilename())) {
            return null;
        }
        // 平台 ： http://www.ttshitu.com/user/recharge.html
        String username = ApolloUtil.yzmUsername();
        String password = ApolloUtil.yzmPassword();
        //验证码类型(默认数英混合),1:纯数字, 2:纯英文，其他:数英混合：可空
        String typeid = "1";
        //备注字段: 可以不写
        String remark = "输出计算结果";
        //你需要识别的1:图片地址，2:也可以是一个文件
        //1:这是远程url的图片地址
//        URL u = new URL(url);
//        InputStream inputStream = u.openStream();
        //2:这是本地文件
        String originFileName = partFile.getOriginalFilename()
                .substring(partFile.getOriginalFilename().lastIndexOf("."));
        String filename = DateUtil.formatDate(new Date(), DateUtil.FULL_FIELD_FORMAT) + RandomUtils.nextInt() + originFileName;
        File image = new File(filename);
         InputStream inputStream = new FileInputStream(image);
        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);
        data.put("typeid", typeid);
        data.put("remark", remark);
        String resultString = Jsoup.connect("http://api.ttshitu.com/create.json")
                .data(data).data("image", "test.jpg", inputStream)
                .ignoreContentType(true)
                .post().text();
        JSONObject jsonObject = JSONObject.parseObject(resultString);
        inputStream.close();
        if (jsonObject.getBoolean("success")) {
            return jsonObject.getJSONObject("data").getString("result");
        } else {
            return null;
        }
    }
}
