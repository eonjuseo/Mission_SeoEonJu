package com.ll;

public class Quote {

    private int quoteNum;
    private String quote;
    private String writer;

    public Quote() {} // 기본 생성자

    public Quote(int quoteNum, String quote, String writer) {
        this.quoteNum = quoteNum;
        this.quote = quote;
        this.writer = writer;
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

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    @Override
    public String toString() {
        return "Quote{" + "quoteNum=" + quoteNum +
                ", Quote='" + quote + '\'' +
                ", writer='" + writer + '\'' +
                '}';
    }
}
