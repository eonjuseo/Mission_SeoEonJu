package com.ll.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class Quote {

    private int quoteNum;
    private String quote;
    private String writer;
    private LocalDateTime createdDate; // 작성 날짜
    private LocalDateTime updatedDate;
    public Quote() {} // 기본 생성자

    public Quote(int quoteNum, String quote, String writer, LocalDateTime createdDate, LocalDateTime updatedDate) {
        this.quoteNum = quoteNum;
        this.quote = quote;
        this.writer = writer;
        this.createdDate = createdDate; this.updatedDate = updatedDate;
    }


    @Override
    public String toString() {
        return "Quote{" + "quoteNum=" + quoteNum +
                ", Quote='" + quote + '\'' +
                ", writer='" + writer + '\'' +
                /*", updatedDate='" + updatedDate + '\'' +*/
                '}';
    }
}
