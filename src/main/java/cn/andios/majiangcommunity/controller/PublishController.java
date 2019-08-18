package cn.andios.majiangcommunity.controller;

import cn.andios.majiangcommunity.cache.TagCache;
import cn.andios.majiangcommunity.dto.QuestionDto;
import cn.andios.majiangcommunity.model.Question;
import cn.andios.majiangcommunity.model.User;
import cn.andios.majiangcommunity.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @description:发起问题
 * @author:LSD
 * @when:2019/6/12/21:05
 */
@Controller
public class PublishController {
    @Autowired
    private QuestionService questionService;

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable (name = "id")Long id,Model model){
        QuestionDto question = questionService.getQuestionById(id);

        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id",question.getId());
        model.addAttribute("tags",TagCache.get());
        return "publish";
    }

    @GetMapping("/publish")
    public String publish(Model model){
        model.addAttribute("tags",TagCache.get());
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(@RequestParam(value = "title",required = false) String title,
                            @RequestParam(value = "description",required = false) String description,
                            @RequestParam(value = "tag",required = false) String tag,
                            @RequestParam(value = "id",required = false) Long id,
                            Model model,
                            HttpServletRequest request){
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        model.addAttribute("tags",TagCache.get());

        if("".equals(title) || title == null){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if("".equals(description) || description == null){
            model.addAttribute("error","问题详细描述不能为空");
            return "publish";
        }
        if("".equals(tag) || tag == null){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }
        String invalidTags = TagCache.filterInvalid(tag);
        if(StringUtils.isNoneBlank(invalidTags)){
            model.addAttribute("error","标签非法");
            return "publish";
        }

        //获取user
        User user = (User)request.getSession().getAttribute("user");

        if(user == null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }
        //封装Question
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setId(id);
        //持久化到数据库
        questionService.createOrUpdate(question);

        return "redirect:/";
    }
}
