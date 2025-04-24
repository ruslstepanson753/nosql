package com.javarush.stepanov.publisher.mapper;

import com.javarush.stepanov.publisher.model.mark.Mark;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MarkDto {

    Mark.Out out(Mark entity);

    Mark in(Mark.In inputDto);


}
