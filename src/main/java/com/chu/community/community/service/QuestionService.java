package com.chu.community.community.service;

import com.chu.community.community.dto.PaginlationDTO;
import com.chu.community.community.dto.QuestionDTO;
import com.chu.community.community.exception.CustomizeErrorCode;
import com.chu.community.community.exception.CustomizeException;
import com.chu.community.community.mapper.QuestionExtMapper;
import com.chu.community.community.mapper.QuestionMapper;
import com.chu.community.community.mapper.UserMapper;
import com.chu.community.community.model.Question;
import com.chu.community.community.model.QuestionExample;
import com.chu.community.community.model.User;
import org.apache.ibatis.session.RowBounds;
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

    @Autowired
    private QuestionExtMapper questionExtMapper;

    public PaginlationDTO list(Integer page, Integer size) {
        //分页 size*(page-1)
        Integer offset = size * (page - 1);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds( new QuestionExample(), new RowBounds( offset, size ) );
        List<QuestionDTO> questionDTOList = new ArrayList<>(  );

        PaginlationDTO paginlationDTO = new PaginlationDTO();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //把question对象copy到questionDTO
            BeanUtils.copyProperties( question,questionDTO );
            questionDTO.setUser(user);
            questionDTOList.add( questionDTO );
        }
        paginlationDTO.setQuestion( questionDTOList );
        Integer totalCount = (int) questionMapper.countByExample( new QuestionExample() );
        paginlationDTO.setPagination(totalCount,page,size);
        return paginlationDTO;
    }

    public PaginlationDTO list(Long userId, Integer page, Integer size) {
        //分页 size*(page-1)
        Integer offset = size * (page - 1);

        QuestionExample example = new QuestionExample();
        example.createCriteria()
                .andCreatorEqualTo( userId );
        List<Question> questions = questionMapper.selectByExampleWithRowbounds( new QuestionExample(), new RowBounds( offset, size ) );
        List<QuestionDTO> questionDTOList = new ArrayList<>(  );

        PaginlationDTO paginlationDTO = new PaginlationDTO();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey( question.getCreator() );
            QuestionDTO questionDTO = new QuestionDTO();
            //把question对象copy到questionDTO
            BeanUtils.copyProperties( question,questionDTO );
            questionDTO.setUser(user);
            questionDTOList.add( questionDTO );
        }
        paginlationDTO.setQuestion( questionDTOList );
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria()
                .andCreatorEqualTo( userId );
        Integer totalCount = (int)questionMapper.countByExample( questionExample );
        paginlationDTO.setPagination(totalCount,page,size);
        return paginlationDTO;
    }

    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey( id );
        if(question == null){
            throw new CustomizeException( CustomizeErrorCode.QUESTION_NOT_FOUND );
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties( question,questionDTO );
        User user = userMapper.selectByPrimaryKey( question.getCreator() );
        questionDTO.setUser( user );
        return questionDTO;
    }

    //创建问题或者删除问题
    public void createOrUpdate(Question question) {
        if(question.getId() == null){
            //创建
            question.setGmtCreate( System.currentTimeMillis() );
            question.setGmtModified( question.getGmtCreate() );
            question.setViewCount( 0 );
            question.setLikeCount( 0 );
            question.setCommentCount( 0 );
            questionMapper.insert( question );
        }else{
            //修改
            question.setGmtModified( question.getGmtCreate() );
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified( System.currentTimeMillis() );
            updateQuestion.setTitle( question.getTitle() );
            updateQuestion.setDescription( question.getDescription() );
            updateQuestion.setTag( question.getTag() );
            QuestionExample example = new QuestionExample();
            example.createCriteria()
                    .andIdEqualTo( question.getId() );
            int updated = questionMapper.updateByExampleSelective( updateQuestion, example );
            if(updated != 1){
               throw new CustomizeException( CustomizeErrorCode.QUESTION_NOT_FOUND );
            }
        }
    }

    public void incView(Long id) {
        Question question = new Question();
        question.setId( id );
        question.setViewCount( 1 );
        questionExtMapper.incView( question );
    }
}
