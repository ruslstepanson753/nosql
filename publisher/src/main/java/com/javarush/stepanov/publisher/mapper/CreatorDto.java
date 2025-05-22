package com.javarush.stepanov.publisher.mapper;

import com.javarush.stepanov.publisher.model.creator.Creator;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreatorDto {
    Creator.Out out(Creator entity);
    Creator in(Creator.In inputDto);
}
