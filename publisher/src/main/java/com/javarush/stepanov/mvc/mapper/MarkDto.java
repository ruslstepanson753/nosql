package com.javarush.stepanov.mvc.mapper;

import com.javarush.stepanov.mvc.model.mark.Mark;
import com.javarush.stepanov.mvc.model.story.Story;
import com.javarush.stepanov.mvc.model.storymark.StoryMark;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MarkDto {

    Mark.Out out(Mark entity);

    Mark in(Mark.In inputDto);


}
