package com.example.dataanalysiscalculateservice.controller;

import com.example.core.pojo.base.ResponseModel;
import com.example.dataanalysiscalculateservice.config.XFConfig;
import com.example.dataanalysiscalculateservice.pojo.vo.TalentRankVO;
import com.example.dataanalysiscalculateservice.service.CalculateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calculate")
public class calculateController {

    @Autowired
    CalculateService calculateService;
    @Autowired
    XFConfig xfConfig;

    @GetMapping("/calculateAll")
    public ResponseModel<?> calculateAll() {
        try {
            calculateService.calculateAll();
            return ResponseModel.success("计算成功");
        } catch (Exception e) {
            return ResponseModel.failure("计算失败" +  e.getMessage());
        }
    }


    @GetMapping("/calculateByLogin")
    public ResponseModel<?> calculateByLogin(String login ) {
        try {
            calculateService.calculateByLogin(login);
            return ResponseModel.success("计算成功");
        } catch (Exception e) {
            return ResponseModel.failure("计算失败" +  e.getMessage());
        }
    }

    @GetMapping("/findAll")
    public ResponseModel<?> findAll( ) {
        try {

            return ResponseModel.success( calculateService.findAll());
        } catch (Exception e) {
            return ResponseModel.failure("计算失败" +  e.getMessage());
        }
    }
    @GetMapping("/findByLogin")
    public ResponseModel<?> findByLogin( String login ) {
        try {

            return ResponseModel.success(calculateService.findByLogin(login));
        } catch (Exception e) {
            return ResponseModel.failure("计算失败" +  e.getMessage());
        }
    }




//    @GetMapping("/ai")
//    public ResponseModel<?> ai(String text){
//        try {
//            SecurityContextHolder ctx   = new SecurityContextHolder();
//            BigModelNew bigModelNew = new BigModelNew(ctx.toString(),true);
//            return ResponseModel.success( bigModelNew.requestModel("通过以下数据分析此github开发者的所在的地区并给出置信度,login并不是无用信息你可以通过他是哪种语言来判断开发者所在地区:"+"login=Pointedstick,location=null,biog=Husband, father of two, independent OSS dev. @vuejs, @vitejs, @rolldown, and more. Connoisseur of sushi.,company=上海景荣公司，twitterUsername=youyuxi"));
//        } catch (Exception e) {
//            return ResponseModel.failure("计算失败" +  e);
//        }
//    }

}
