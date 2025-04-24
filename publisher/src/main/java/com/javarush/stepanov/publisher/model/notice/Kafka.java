package com.javarush.stepanov.publisher.model.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public  class Kafka implements Serializable {
    Long id;
    Long storyId;
    String content;
    String method;
    String state;

    @Override
    public String toString() {
        return "Kafka{" +
                "id=" + id +
                ", storyId=" + storyId +
                ", content='" + content + '\'' +
                ", method=" + method +
                ", state=" + state +
                '}';
    }
}