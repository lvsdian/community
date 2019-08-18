package cn.andios.majiangcommunity.service;

import cn.andios.majiangcommunity.dto.NotificationDTO;
import cn.andios.majiangcommunity.dto.PaginationDTO;
import cn.andios.majiangcommunity.dto.QuestionDto;
import cn.andios.majiangcommunity.enums.NotificationEnum;
import cn.andios.majiangcommunity.enums.NotificationStatusEnum;
import cn.andios.majiangcommunity.exception.CustomizeErrorCode;
import cn.andios.majiangcommunity.exception.CustomizeException;
import cn.andios.majiangcommunity.mapper.NotificationMapper;
import cn.andios.majiangcommunity.mapper.UserMapper;
import cn.andios.majiangcommunity.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author:LSD
 * @when:2019/7/30/9:28
 */
@Service
public class NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private UserMapper userMapper;

    public PaginationDTO getQuestionListPageByUser(Long userId, Integer page, Integer size) {
        //创建分页对象
        PaginationDTO<NotificationDTO> paginationDTO = new PaginationDTO<>();
        //获得总页数
        NotificationExample notificationExample = new NotificationExample();
        notificationExample.createCriteria()
                .andReceiverEqualTo(userId);
        Integer totalCount = (int) notificationMapper.countByExample(notificationExample);
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
        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andReceiverEqualTo(userId);
        example.setOrderByClause("gmt_create desc");
        List<Notification> notificationList = notificationMapper.selectByExampleWithRowbounds(example,new RowBounds(offset,size));

        if(notificationList.size() == 0){
            return paginationDTO;
        }

        List<NotificationDTO> notificationDTOLists = new ArrayList<>();
        for (Notification notification : notificationList) {
            NotificationDTO notificationDTO = new NotificationDTO();

            BeanUtils.copyProperties(notification,notificationDTO);

            notificationDTO.setTypeName(NotificationEnum.nameOfType(notification.getType()));

            notificationDTOLists.add(notificationDTO);
        }

        paginationDTO.setData(notificationDTOLists);

        return paginationDTO;
    }

    public Long getUnReadCountByUserId(Long userId) {
        NotificationExample example = new NotificationExample();
        example.createCriteria()
                .andReceiverEqualTo(userId)
                .andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(example);
    }

    public NotificationDTO readNotification(Long id, User user) {
        //根据id查出NotificationDTO
        Notification notification = notificationMapper.selectByPrimaryKey(id);
        if(notification == null){
            throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
        }
        if(!Objects.equals(notification.getReceiver(),user.getId())){
            throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
        }

        NotificationDTO notificationDTO = new NotificationDTO();

        BeanUtils.copyProperties(notification,notificationDTO);
        notificationDTO.setTypeName(NotificationEnum.nameOfType(notification.getType()));

        //标为已读
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
        notificationMapper.updateByPrimaryKey(notification);

        return notificationDTO;
    }
}
