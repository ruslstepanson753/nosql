package com.javarush.stepanov.mvc.mapper;

import com.javarush.stepanov.mvc.model.mark.Mark;
import com.javarush.stepanov.mvc.model.story.Story;
import com.javarush.stepanov.mvc.model.storymark.StoryMark;
import org.mapstruct.*;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface StoryDto {

    @Mapping(target = "marks", ignore = true)
    Story.Out out(Story entity);

    @Mapping(target = "marks", ignore = true)
    Story in(Story.In inputDto);



}
