package cn.andios.majiangcommunity.mapper;

import cn.andios.majiangcommunity.model.Comment;
import cn.andios.majiangcommunity.model.CommentExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface CommentExtMapper {
    int increaseCommentCount(Comment comment);
}