package cn.andios.majiangcommunity.mapper;

import cn.andios.majiangcommunity.dto.QuestionQueryDTO;
import cn.andios.majiangcommunity.model.Question;
import cn.andios.majiangcommunity.model.QuestionExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface QuestionExtMapper {
    /**
     * 增加阅读数
     * @param record
     * @return
     */
    int increaseView( Question record);

    /**
     * 增加评论数
     * @param record
     * @return
     */
    int increaseCommentCount( Question record);

    /**
     * 根据当前问题的tag查找相关问题
     * @param question
     * @return
     */
    List<Question> selectRelatedQuestions(Question question);

    /**
     * 根据查询条件获得相关的问题条数
     * @param questionQueryDTO
     * @return
     */
    Integer countQuestionBySearchCondition(QuestionQueryDTO questionQueryDTO);

    /**
     * 根据查询条件分页查询问题
     * @param questionQueryDTO
     * @return
     */
    List<Question> selectQuestionBySearchCondition(QuestionQueryDTO questionQueryDTO);
}