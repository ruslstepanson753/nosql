package com.javarush.stepanov.mvc.model.creator;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tbl_creator")
public class Creator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long id;
     String login;
     String password;
     String firstname;
     String lastname;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class In{
        @Positive
        public Long id;
        @Size(min = 2, max = 64)
        public String login;
        @Size(min = 8, max = 128)
        public String password;
        @Size(min = 2, max = 64)
        public String firstname;
        @Size(min = 2, max = 64)
        public String lastname;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Out{
        public Long id;
        public String login;
        public String password;
        public String firstname;
        public String lastname;
    }


}
