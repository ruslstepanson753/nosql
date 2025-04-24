package com.javarush.stepanov.publisher.mapper;

import com.javarush.stepanov.publisher.model.story.Story;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface StoryDto {

    @Mapping(target = "marks", ignore = true)
    Story.Out out(Story entity);

    @Mapping(target = "marks", ignore = true)
    Story in(Story.In inputDto);



}
