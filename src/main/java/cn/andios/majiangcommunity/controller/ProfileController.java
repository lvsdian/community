package cn.andios.majiangcommunity.controller;

import cn.andios.majiangcommunity.dto.PaginationDTO;
import cn.andios.majiangcommunity.mapper.UserMapper;
import cn.andios.majiangcommunity.model.User;
import cn.andios.majiangcommunity.service.NotificationService;
import cn.andios.majiangcommunity.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @description:
 * @author:LSD
 * @when:2019/7/9/23:39
 */
@Controller
public class ProfileController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action")String action,
                          Model model, HttpServletRequest request,
                          @RequestParam(name="page",defaultValue = "1")Integer page,
                          @RequestParam(name="size",defaultValue = "4")Integer size){

        User user = (User)request.getSession().getAttribute("user");

        if(user == null){
            return  "redirect:/";
        }
        if("questions".equals(action)){
            PaginationDTO paginationDTO = questionService.getQuestionListPageByUser(user.getId(), page, size);

            model.addAttribute("section","question");
            model.addAttribute("sectionName","我的提问");
            model.addAttribute("paginationDTO",paginationDTO);
        }else if("replies".equals(action)){

            PaginationDTO paginationDTO = notificationService.getQuestionListPageByUser(user.getId(), page, size);

            model.addAttribute("paginationDTO",paginationDTO);
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
        }

        return "profile";
    }
}
