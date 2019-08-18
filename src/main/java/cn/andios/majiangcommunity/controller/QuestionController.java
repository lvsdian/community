package cn.andios.majiangcommunity.controller;

import cn.andios.majiangcommunity.dto.CommentDTO;
import cn.andios.majiangcommunity.dto.QuestionDto;
import cn.andios.majiangcommunity.enums.CommentTypeEnum;
import cn.andios.majiangcommunity.model.Question;
import cn.andios.majiangcommunity.service.CommentService;
import cn.andios.majiangcommunity.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @description:
 * @author:LSD
 * @when:2019/7/19/0:08
 */
@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;


    @GetMapping("/question/{id}")
    public String question(@PathVariable Long id, Model model){
        QuestionDto questionDto = questionService.getQuestionById(id);
        List<QuestionDto> relatedQuestionDTOs = questionService.selectRelatedQuestions(questionDto);
        List<CommentDTO> commentDTOList = commentService.getCommentListByTargetId(id,CommentTypeEnum.QUESTION);
        //累加阅读数
        questionService.increaseView(id);
        model.addAttribute("questionDto",questionDto);
        model.addAttribute("commentDTOList",commentDTOList);
        model.addAttribute("relatedQuestionDTOs",relatedQuestionDTOs);
        return "question";
    }
}
