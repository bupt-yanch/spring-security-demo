package org.example.demo.portal.controller;

import org.dssc.demo.common.api.CommonResult;
import org.dssc.demo.common.constant.Auth;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class TestController {

    @GetMapping("/test")
    public CommonResult<String> test(@RequestParam String message,
                                     HttpServletRequest httpServletRequest) {
        String userId = httpServletRequest.getHeader(Auth.JWT_TOKEN_USER);
        String str = userId + ":" + message;
        return CommonResult.success(str);
    }
}
