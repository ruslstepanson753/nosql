package com.javrush.stepanov.discussion.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public  class Kafka implements Serializable {
    Long id;
    String method;
    String state;

    Notice.Out notice;
    List<Notice.Out> notices;

    @Override
    public String toString() {
        return "Kafka{" +
                "id=" + id +
                ", method=" + method +
                ", state=" + state +
                '}';
    }
}