package cn.andios.majiangcommunity.controller;

import cn.andios.majiangcommunity.dto.PaginationDTO;
import cn.andios.majiangcommunity.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;


/**
 * @description:
 * @author:LSD
 * @when:2019/6/11/12:32
 */
@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    /**
     * 获取name为token的cookie,
     * 因为 “/callback”中，如果用户正确登陆，就会把用户token写入cookie.那么这里就可以获取
     * @param request
     * @return
     */
    @GetMapping("/")
    public String index(HttpServletRequest request,Model model,
                        @RequestParam(name="page",defaultValue = "1")Integer page,
                        @RequestParam(name="size",defaultValue = "4")Integer size,
                        @RequestParam(name="search",required = false)String search){
        PaginationDTO paginationDTO = questionService.getQuestionListByUser(search,page,size);
        model.addAttribute("paginationDTO",paginationDTO);
        model.addAttribute("search",search);
        return "index";
    }


}
