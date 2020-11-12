package com.biji.puppeteer.shapecode.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * 参数验空工具类
 *
 * @author caoyong
 * @since 2018年1月29日 上午11:41:10
 */
public class CheckParamsUtil {

    public static String check(Object o, Class<?> cz, String... arg) {
        if (o == null) {
            return "当前对象为空";
        }
        Field[] fields = cz.getDeclaredFields();
        for (Field field : fields){
            //开启修改访问权限
            field.setAccessible(true);
            for (String attribute : arg) {
                //验证需要校验的参数
                if (attribute.equals(field.getName())) {
                    String type = "";
                    Object value = null;
                    try {
                        //获取字段类型名称
                        type = field.getType().getName();
                        //获取字段的值
                        value = field.get(o);
                    } catch (Exception e) {
                    }
                    //所有对象类型为空时
                    if (null == value) {
                        return attribute + "为空";
                    }
                    //string类型数据
                    if ("java.lang.String".equals(type)) {
                        if (StringUtils.isBlank(value.toString())) {
                            return attribute + "为空";
                        }
                    } else if ("java.util.List".equals(type) && CollectionUtils.isEmpty((Collection<?>) value)) {
                        return attribute + "为空";
                    }
                }
            }
        }
        return null;
    }
}
