package cn.andios.majiangcommunity.controller;

import cn.andios.majiangcommunity.dto.NotificationDTO;
import cn.andios.majiangcommunity.dto.PaginationDTO;
import cn.andios.majiangcommunity.enums.NotificationEnum;
import cn.andios.majiangcommunity.model.User;
import cn.andios.majiangcommunity.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @description:
 * @author:LSD
 * @when:2019/7/30/15:44
 */
@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    /**
     * 用户点击通知的信息时，传过来消息的id
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/notification/{id}")
    public String profile(@PathVariable(name = "id")Long id,HttpServletRequest request){

        User user = (User)request.getSession().getAttribute("user");

        if(user == null){
            return  "redirect:/";
        }
        NotificationDTO notificationDTO = notificationService.readNotification(id,user);
        if(NotificationEnum.REPLY_COMMENT.getType() == notificationDTO.getType() ||
                NotificationEnum.REPLY_QUESTION.getType() == notificationDTO.getType()){
            System.out.println(notificationDTO.getOuterid()+"==============================");
            return "redirect:/question/"+notificationDTO.getOuterid();
        }else {
            return "profile";
        }
    }

}
