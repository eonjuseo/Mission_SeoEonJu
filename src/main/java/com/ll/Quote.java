package com.ll;

public class Quote {

    private int quoteNum;
    private String quote;
    private String writer;
    //private LocalDateTime createdDate; // 작성 날짜
    //private LocalDateTime updatedDate;
    public Quote() {} // 기본 생성자

    public Quote(int quoteNum, String quote, String writer/*,  LocalDateTime createdDate, LocalDateTime updatedDate*/) {
        this.quoteNum = quoteNum;
        this.quote = quote;
        this.writer = writer;
        //this.createdDate = createdDate; this.updatedDate = updatedDate;
    }

    public int getQuoteNum() {
        return quoteNum;
    }

    public String getQuote() {
        return quote;
    }

    public String getWriter() {
        return writer;
    }

    //public LocalDateTime getCreatedDate() { return createdDate; }
    //public LocalDateTime getUpdatedDate() { return updatedDate; }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    //public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    //public void setUpdatedDate(LocalDateTime updatedDate) { this.updatedDate = updatedDate; }
    @Override
    public String toString() {
        return "Quote{" + "quoteNum=" + quoteNum +
                ", Quote='" + quote + '\'' +
                ", writer='" + writer + '\'' +
                /*", updatedDate='" + updatedDate + '\'' +*/
                '}';
    }
}
