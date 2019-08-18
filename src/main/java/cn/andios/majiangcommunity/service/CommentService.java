package cn.andios.majiangcommunity.service;

import cn.andios.majiangcommunity.dto.CommentDTO;
import cn.andios.majiangcommunity.enums.CommentTypeEnum;
import cn.andios.majiangcommunity.enums.NotificationEnum;
import cn.andios.majiangcommunity.enums.NotificationStatusEnum;
import cn.andios.majiangcommunity.exception.CustomizeErrorCode;
import cn.andios.majiangcommunity.exception.CustomizeException;
import cn.andios.majiangcommunity.mapper.*;
import cn.andios.majiangcommunity.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @description:
 * @author:LSD
 * @when:2019/7/22/20:04
 */
@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Transactional
    public void insert(Comment comment, User commentator) {
        if(comment.getParentId() == null || comment.getParentId() == 0){
                throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if(comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())){
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if(comment.getType().equals(CommentTypeEnum.COMMENT.getType()) ){
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if(dbComment == null){
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }

            Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
            if(question == null){
                throw  new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            //增加评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);

            commentExtMapper.increaseCommentCount(parentComment);

            //创建通知
            createNotify(comment,dbComment.getCommentator(), NotificationEnum.REPLY_COMMENT, commentator.getName(), question.getTitle(), question.getId());

        }else{
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if(question == null){
                throw  new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            //如果不设为0,就是null，就不能做到增加评论条数
            comment.setCommentCount(0);
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.increaseCommentCount(question);

            //创建通知
            createNotify(comment,question.getCreator(),NotificationEnum.REPLY_QUESTION, commentator.getName(), question.getTitle(), question.getId());
        }
    }

    private void createNotify(Comment comment, Long receiver, NotificationEnum notificationEnum, String notificatorName, String outerTitle, Long outerid) {
        //回复自己的问题或评论
        if(receiver.intValue()==comment.getCommentator()){
            return;
        }
        //发送通知消息
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationEnum.getType());
        notification.setOuterid(outerid);
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setNofifier(comment.getCommentator());
        notification.setReceiver(receiver);
        notification.setNotifierName(notificatorName);
        notification.setOuterTitle(outerTitle);

        notificationMapper.insert(notification);
    }

    public List<CommentDTO> getCommentListByTargetId(Long id, CommentTypeEnum commentTypeEnum) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(commentTypeEnum.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if(comments.size() == 0){
            return new ArrayList<>();
        }
        //获取评论者（用set存，一个人多次评论时，只算一个人,去重）
        Set<Long> commentators = comments.stream().map(Comment::getCommentator).collect(Collectors.toSet());
        //将set放到list里
        List<Long> userIds = new ArrayList<>(commentators);
        //获取评论人为map
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);

        Map<Long,User> userMap = users.stream().collect(Collectors.toMap(user->user.getId(),user->user));

        //转换comment为commentDTO
        List<CommentDTO> commentDTOS =  comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;
    }
}
