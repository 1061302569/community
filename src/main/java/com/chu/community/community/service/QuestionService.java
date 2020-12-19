package com.chu.community.community.service;

import com.chu.community.community.dto.PaginlationDTO;
import com.chu.community.community.dto.QuestionDTO;
import com.chu.community.community.mapper.QuestionMapper;
import com.chu.community.community.mapper.UserMapper;
import com.chu.community.community.model.Question;
import com.chu.community.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    public PaginlationDTO list(Integer page, Integer size) {
        //分页 size*(page-1)
        Integer offset = size * (page - 1);

        List<Question> questions = questionMapper.list(offset,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>(  );

        PaginlationDTO paginlationDTO = new PaginlationDTO();
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //把question对象copy到questionDTO
            BeanUtils.copyProperties( question,questionDTO );
            questionDTO.setUser(user);
            questionDTOList.add( questionDTO );
        }
        paginlationDTO.setQuestion( questionDTOList );
        Integer totalCount = questionMapper.count();
        paginlationDTO.setPagination(totalCount,page,size);
        return paginlationDTO;
    }

    public PaginlationDTO list(Integer userId, Integer page, Integer size) {
        //分页 size*(page-1)
        Integer offset = size * (page - 1);

        List<Question> questions = questionMapper.listByUserId(userId,offset,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>(  );

        PaginlationDTO paginlationDTO = new PaginlationDTO();
        for (Question question : questions) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //把question对象copy到questionDTO
            BeanUtils.copyProperties( question,questionDTO );
            questionDTO.setUser(user);
            questionDTOList.add( questionDTO );
        }
        paginlationDTO.setQuestion( questionDTOList );
        Integer totalCount = questionMapper.countByUserId(userId);
        paginlationDTO.setPagination(totalCount,page,size);
        return paginlationDTO;
    }

    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.getById(id);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties( question,questionDTO );
        User user = userMapper.findById( question.getCreator() );
        questionDTO.setUser( user );
        return questionDTO;
    }

    //创建问题或者删除问题
    public void createOrUpdate(Question question) {
        if(question.getId() == null){
            //创建
            question.setGmtCreate( System.currentTimeMillis() );
            question.setGmtModified( question.getGmtCreate() );
            questionMapper.create( question );
        }else{
            //修改
            question.setGmtModified( question.getGmtCreate() );
            questionMapper.update(question);
        }
    }
}
