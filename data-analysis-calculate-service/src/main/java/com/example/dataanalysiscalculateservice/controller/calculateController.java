package com.example.dataanalysiscalculateservice.controller;

import com.example.core.entity.ResponseModel;
import com.example.dataanalysiscalculateservice.config.BigModelNew;
import com.example.dataanalysiscalculateservice.config.XFConfig;
import com.example.dataanalysiscalculateservice.service.CalculateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calculate")
public class calculateController {

    @Autowired
    CalculateService calculateService;
    @Autowired
    XFConfig xfConfig;

    @GetMapping("/projectUser")
    public ResponseModel<?> calcuate(@RequestParam String projectUser) {
        try {
            return ResponseModel.success( calculateService.calculate(projectUser));
        } catch (Exception e) {
            return ResponseModel.failure("计算失败" +  e.getMessage());
        }
    }

//    @GetMapping("/ai")
//    public ResponseModel<?> ai(String text){
//        try {
//            SecurityContextHolder ctx   = new SecurityContextHolder();
//            BigModelNew bigModelNew = new BigModelNew(ctx.toString(),true);
//            return ResponseModel.success( bigModelNew.requestModel("通过以下数据分析此github开发者的所在的地区并给出置信度,login并不是无用信息你可以通过他是哪种语言来判断开发者所在地区:"+"login=Pointedstick,location=null,blog=Husband, father of two, independent OSS dev. @vuejs, @vitejs, @rolldown, and more. Connoisseur of sushi.,company=上海景荣公司，twitterUsername=youyuxi"));
//        } catch (Exception e) {
//            return ResponseModel.failure("计算失败" +  e);
//        }
//    }

}
