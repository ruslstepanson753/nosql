package com.example.discussion.mapper;

import com.example.discussion.model.Notice;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NoticeDto {
    Notice.Out out(Notice entity);

    Notice in(Notice.In inputDto);
}
