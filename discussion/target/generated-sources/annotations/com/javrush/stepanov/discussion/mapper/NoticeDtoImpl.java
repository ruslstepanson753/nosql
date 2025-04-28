package com.javrush.stepanov.discussion.mapper;

import com.javrush.stepanov.discussion.model.Notice;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-28T02:54:16+0300",
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

    @Override
    public Notice toNoticeFromOut(Notice.Out outDto) {
        if ( outDto == null ) {
            return null;
        }

        Notice.NoticeBuilder notice = Notice.builder();

        notice.content( outDto.getContent() );

        return notice.build();
    }

    @Override
    public Notice.In toInFromOut(Notice.Out outDto) {
        if ( outDto == null ) {
            return null;
        }

        Notice.In.InBuilder in = Notice.In.builder();

        in.id( outDto.getId() );
        in.storyId( outDto.getStoryId() );
        in.content( outDto.getContent() );

        return in.build();
    }
}
