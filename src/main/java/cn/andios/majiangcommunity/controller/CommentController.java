package cn.andios.majiangcommunity.controller;

import cn.andios.majiangcommunity.dto.CommentCreateDTO;
import cn.andios.majiangcommunity.dto.CommentDTO;
import cn.andios.majiangcommunity.dto.ResultDto;
import cn.andios.majiangcommunity.enums.CommentTypeEnum;
import cn.andios.majiangcommunity.exception.CustomizeErrorCode;
import cn.andios.majiangcommunity.model.Comment;
import cn.andios.majiangcommunity.model.User;
import cn.andios.majiangcommunity.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @description:评论
 * @author:LSD
 * @when:2019/7/22/17:08
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentCreateDTO,
                       HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        if(user == null){
            return ResultDto.error(CustomizeErrorCode.NOT_LOGIN);
        }
        if(commentCreateDTO == null || StringUtils.isBlank(commentCreateDTO.getContent())){
            return ResultDto.error(CustomizeErrorCode.CONTENT_IS_EMPTY);
        }

        Comment comment = new Comment();
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);

        commentService.insert(comment,user);

        return ResultDto.success();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    public ResultDto comments(@PathVariable("id")Long id){
         List<CommentDTO> commentDTOList = commentService.getCommentListByTargetId(id,CommentTypeEnum.COMMENT);

         return ResultDto.successOf(commentDTOList);
    }
}
