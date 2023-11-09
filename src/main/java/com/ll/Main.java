package com.ll;

import com.ll.domain.Quote;
import com.ll.simpledb.Sql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    private static List<Quote> quoteList;
    private static int quoteNum;

    public static void main(String[] args) throws IOException, SQLException {
        // 데이터베이스 연결 설정
        String url = "jdbc:mysql://localhost:3306/simpleDb";
        String username = "root";
        String password = "";

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Sql sql = new Sql(connection);
            System.out.println("== 명언 앱 ==");

            quoteList = sql.selectAllQuotes();
            quoteNum = nextQuoteNum(quoteList);
            BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                System.out.print("명령) ");
                String command = sc.readLine().trim();

                switch (command) {
                    case "등록":
                        createQuote(sql, sc);
                        break;

                    case "목록":
                        readQuotes(sql);
                        break;

                    case "수정":
                        updateQuote(sql, sc);
                        break;

                    case "삭제":
                        deleteQuote(sql, sc);
                        break;

                    case "종료":
                        return;
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    private static void createQuote(Sql sql, BufferedReader sc) throws IOException, SQLException{
        System.out.print("명언 : ");
        String quote = sc.readLine();
        System.out.print("작가 : ");
        String writer = sc.readLine();
        LocalDateTime now = LocalDateTime.now();

        sql.insertQuote(quote, writer);
        Quote quoteCreate = new Quote(quoteNum, quote, writer, now, now);
        quoteList.add(quoteCreate);
        quoteNum++;
        System.out.println(quoteNum - 1 + "번 명언이 등록되었습니다.");
    }

    private static void readQuotes(Sql sql) throws SQLException {
        quoteList = sql.selectAllQuotes();

        System.out.println("번호 / 작가 / 명언 / 작성일 / 수정일");
        System.out.println("-----------------------------------");
        for (Quote quoteRead : quoteList) {
            System.out.println(quoteRead.getQuoteNum() + " / " + quoteRead.getWriter() + " / " +
                    quoteRead.getQuote() + " / " + quoteRead.getCreatedDate() + " / " + quoteRead.getUpdatedDate());
        }
    }

    private static void updateQuote(Sql sql, BufferedReader sc)
            throws IOException, SQLException {
        System.out.print("?id=");
        int modNum = Integer.parseInt(sc.readLine());
        boolean update = false;
        LocalDateTime updateDate = LocalDateTime.now();
        for (Quote quote : quoteList) {
            if (quote.getQuoteNum() == modNum) {
                System.out.println("명언(기존) : " + quote.getQuote());
                System.out.print("명언 : ");
                String newQuote = sc.readLine();
                System.out.println("작가(기존) : " + quote.getWriter());
                System.out.print("작가 : ");
                String newWriter = sc.readLine();
                sql.updateQuote(modNum, newQuote, newWriter);
                quote.setQuote(newQuote);
                quote.setWriter(newWriter);
                quote.setUpdatedDate(updateDate);
                update = true;
                System.out.println(modNum + "번 명언이 수정되었습니다.");
                break;
            }
        } if (!update) {
            System.out.println(modNum + "번 명언은 존재하지 않습니다.");
        }
    }

    private static void deleteQuote(Sql sql, BufferedReader sc)
            throws IOException, SQLException {
        System.out.print("?id=");
        int delNum = Integer.parseInt(sc.readLine());
        boolean delete = false;
        sql.deleteQuote(delNum);
        for (Quote quote : quoteList) {
            if (quote.getQuoteNum() == delNum) {
                quoteList.remove(quote);
                delete = true;
                System.out.println(delNum + "번 명언이 삭제되었습니다.");
                break;
            }
        }
        if (!delete) {
            System.out.println(delNum + "번 명언은 존재하지 않습니다.");
        }
    }

    private static int nextQuoteNum(List <Quote> quoteList) {
        int max = 0;
        for (Quote quoteNext : quoteList) {
            if (quoteNext.getQuoteNum() > max) {
                max = quoteNext.getQuoteNum();
            }
        }
        return max + 1;
    }

}