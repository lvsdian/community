package cn.andios.majiangcommunity.service;

import cn.andios.majiangcommunity.dto.PaginationDTO;
import cn.andios.majiangcommunity.dto.QuestionDto;
import cn.andios.majiangcommunity.dto.QuestionQueryDTO;
import cn.andios.majiangcommunity.mapper.QuestionExtMapper;
import cn.andios.majiangcommunity.mapper.QuestionMapper;
import cn.andios.majiangcommunity.mapper.UserMapper;
import cn.andios.majiangcommunity.model.Question;
import cn.andios.majiangcommunity.model.QuestionExample;
import cn.andios.majiangcommunity.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author:LSD
 * @when:2019/7/6/21:39
 */
@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    public PaginationDTO getQuestionListByUser(String search, Integer page, Integer size){
        //拼接查询条件
        if(StringUtils.isNoneBlank(search)){
            String [] conditions= StringUtils.split(search," ");
            search = Arrays.stream(conditions).collect(Collectors.joining("|"));
        }
        //创建分页对象
        PaginationDTO paginationDTO = new PaginationDTO();

        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        //获得总条数
        Integer totalCount = questionExtMapper.countQuestionBySearchCondition(questionQueryDTO);

        //总页数
        Integer totalPages;

        //计算最大页
        if(totalCount % size == 0){
            totalPages = totalCount / size;
        }else{
            totalPages = totalCount /size + 1;
        }
        //页码容差
        if(page < 1){
            page = 1;
        }
        if(page > totalPages){
            page = totalPages;
        }

        //设置分页对象属性
        paginationDTO.setPagination(totalPages,page);

        Integer offset = size*(page - 1);

        questionQueryDTO.setPage(offset);
        questionQueryDTO.setSize(size);
        List<Question> questionList = questionExtMapper.selectQuestionBySearchCondition(questionQueryDTO);

        List<QuestionDto> questionDtoList = new ArrayList<>();

        //得到QuestionDto
        for (Question question: questionList) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question,questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        paginationDTO.setData(questionDtoList);

        return paginationDTO;

    }

    public PaginationDTO getQuestionListPageByUser(Long userId, Integer page, Integer size) {
        //创建分页对象
        PaginationDTO paginationDTO = new PaginationDTO();
        //获得总页数
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(questionExample);
        //总页数
        Integer totalPages;

        //计算最大页
        if(totalCount % size == 0){
            totalPages = totalCount / size;
        }else{
            totalPages = totalCount /size + 1;
        }
        //页码容差
        if(page < 1){
            page = 1;
        }
        if(page > totalPages){
            page = totalPages;
        }

        //设置分页对象属性
        paginationDTO.setPagination(totalPages,page);

        Integer offset = page < 1 ? 0 : size*(page - 1);
        QuestionExample questionExample1 = new QuestionExample();
        questionExample1.createCriteria()
                .andCreatorEqualTo(userId);
        List<Question> questionList = questionMapper.selectByExampleWithRowbounds(questionExample1,new RowBounds(offset,size));

        List<QuestionDto> questionDtoList = new ArrayList<>();

        //得到QuestionDto
        for (Question question: questionList) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDto questionDto = new QuestionDto();
            BeanUtils.copyProperties(question,questionDto);
            questionDto.setUser(user);
            questionDtoList.add(questionDto);
        }
        paginationDTO.setData(questionDtoList);

        return paginationDTO;
    }

    public QuestionDto getQuestionById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
//        if(question == null){
//            throw  new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
//        }
        QuestionDto questionDto = new QuestionDto();
        BeanUtils.copyProperties(question,questionDto);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDto.setUser(user);
        return questionDto;
    }

    public void createOrUpdate(Question question) {
        if(question.getId() == null){
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            question.setCommentCount(0);
            question.setLikeCount(0);
            question.setViewCount(0);
            questionMapper.insert(question);
        }else{
            //更新
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setTag(question.getTag());
            updateQuestion.setDescription(question.getDescription());

            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria()
                    .andIdEqualTo(question.getId());
            int i = questionMapper.updateByExampleSelective(updateQuestion, questionExample);
//            if(1 != i){
//                throw  new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
//            }
        }
    }

    public void increaseView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.increaseView(question);
    }

    public List<QuestionDto> selectRelatedQuestions(QuestionDto questionDto) {
        //如果问题的标签为空，直接返回
        if(StringUtils.isBlank(questionDto.getTag())){
            return new ArrayList<>();
        }
        //得到标签分割后的数组
        String[] tags = StringUtils.split(questionDto.getTag(), ",");
        //将标签数组转为用 | 分割的字符串以便在sql中使用
        String regexTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        //查找到有相同标签的list
        Question question = new Question();
        question.setId(questionDto.getId());
        question.setTag(regexTag);
        List<Question> questionList = questionExtMapper.selectRelatedQuestions(question);

        List<QuestionDto> questionDTOS = questionList.stream().map(q -> {
            QuestionDto questionDto1 = new QuestionDto();
            BeanUtils.copyProperties(q, questionDto1);
            return questionDto1;
        }).collect(Collectors.toList());
        return questionDTOS;
    }
}
