package com.javrush.stepanov.discussion.mapper;

import com.javrush.stepanov.discussion.model.Notice;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NoticeDto {
    Notice.Out out(Notice entity);

    Notice in(Notice.In inputDto);



}
