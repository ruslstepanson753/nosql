package com.javarush.stepanov.mvc.mapper;

import com.javarush.stepanov.mvc.model.creator.Creator;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreatorDto {
    Creator.Out out(Creator entity);
    Creator in(Creator.In inputDto);
}
