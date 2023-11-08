package com.example.aunae_chat.service.impl;

import com.example.aunae_chat.data.documents.MessageSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Auto Increment 기능을 담당하는 SequenceGeneratorService 클래스
 * 해당 collection에 대한 idx값이 없으면 1부터 idx를 시작하고
 * 해당 collection에 대한 idx값이 있으면 다음 숫자로 업데이트하여 자동 증가 기능을 담당해주는 서비스 클래스
 * 출처: https://min-nine.tistory.com/252
 */
@Service
@RequiredArgsConstructor
public class SequenceGeneratorService {
    private final MongoOperations mongoOperations;

    public long generateSequence(String seqName) {
        MessageSequence counter = mongoOperations.findAndModify(
                Query.query(Criteria.where("_id").is(seqName)),
                new Update().inc("seq", 1), FindAndModifyOptions.options().returnNew(true).upsert(true),
                MessageSequence.class
        );
        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }
}
