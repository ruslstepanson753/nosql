package com.javarush.stepanov.mvc.mapper;

import com.javarush.stepanov.mvc.model.notice.Notice;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NoticeDto {
    Notice.Out out(Notice entity);

    Notice in(Notice.In inputDto);
}
