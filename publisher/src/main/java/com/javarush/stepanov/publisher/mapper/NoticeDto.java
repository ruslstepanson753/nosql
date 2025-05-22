package com.javarush.stepanov.publisher.mapper;

import com.javarush.stepanov.publisher.model.notice.Kafka;
import com.javarush.stepanov.publisher.model.notice.Notice;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NoticeDto {
    Notice.Out out(Notice entity);

    Notice in(Notice.In inputDto);

    Notice.Out getOutFromIn (Notice.In inputDto);

}
