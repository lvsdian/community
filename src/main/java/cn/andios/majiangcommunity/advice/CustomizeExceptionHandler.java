package cn.andios.majiangcommunity.advice;

import cn.andios.majiangcommunity.dto.ResultDto;
import cn.andios.majiangcommunity.exception.CustomizeErrorCode;
import cn.andios.majiangcommunity.exception.CustomizeException;
import com.alibaba.fastjson.JSON;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @description:自定义异常处理
 * @author:LSD
 * @when:2019/7/21/11:02
 */
@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response,
                        Throwable throwable, Model model){
        String contentType = request.getContentType();
        System.out.println("cn.andios.majiangcommunity.advice.CustomizeExceptionHandler--throwable.getMessage()======================" + throwable.getMessage());
        if("application/json".equals(contentType)){
            //返回json形式的错误信息
            ResultDto resultDto;
            if(throwable instanceof  CustomizeException){
                resultDto =  ResultDto.error((CustomizeException) throwable);
            }else {
                resultDto =  ResultDto.error(CustomizeErrorCode.SYSTEM_ERROR);
            }
            try {
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                PrintWriter printWriter = response.getWriter();
                System.out.println("````````````````````````````"+JSON.toJSONString(resultDto));
                printWriter.write(JSON.toJSONString(resultDto));

                printWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }else{
            //返回错误页面(html形式的错误信息)
            if(throwable instanceof CustomizeException){
                model.addAttribute("message",throwable.getMessage());
            }else{
                model.addAttribute("message",CustomizeErrorCode.SYSTEM_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }

    }
}
