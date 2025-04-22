package com.example.discussion.mapper;

import com.example.discussion.model.Notice;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-22T11:20:39+0300",
    comments = "version: 1.6.0.Beta1, compiler: javac, environment: Java 21.0.4 (Oracle Corporation)"
)
@Component
public class NoticeDtoImpl implements NoticeDto {

    @Override
    public Notice.Out out(Notice entity) {
        if ( entity == null ) {
            return null;
        }

        Notice.Out.OutBuilder out = Notice.Out.builder();

        out.id( entity.getId() );
        out.storyId( entity.getStoryId() );
        out.content( entity.getContent() );

        return out.build();
    }

    @Override
    public Notice in(Notice.In inputDto) {
        if ( inputDto == null ) {
            return null;
        }

        Notice.NoticeBuilder notice = Notice.builder();

        notice.content( inputDto.getContent() );

        return notice.build();
    }
}
