package com.biji.puppeteer.web.controller;

import com.biji.puppeteer.service.UserService;
import com.biji.puppeteer.util.YzmUtil;
import com.biji.puppeteer.web.vo.JsonReturn;
import com.biji.puppeteer.web.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * create by biji.zhao on 2020/11/11
 */
@RequestMapping("/login")
@RestController
public class LoginController {

    @Autowired
    UserService userService;

    @RequestMapping("/loginIn")
    public JsonReturn login(UserVO userVO, HttpServletRequest request) {
        return JsonReturn.successInstance();
    }

    @RequestMapping("/yzmCode")
    public JsonReturn yzmCode(MultipartFile multipartFile) {
        String code;
        try {
            code = YzmUtil.yzmCode(multipartFile);
        }catch (Exception e) {
            return JsonReturn.errorInstance("yzm get error");
        }
        return JsonReturn.successInstance(code);
    }

}
