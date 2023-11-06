package com.ll;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {



    public static void main(String[] args) throws IOException {

        System.out.println("== 명언 앱 ==");
        List<Quote> quoteList = new ArrayList<>();
        int quoteNum = 1;
        BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.print("명령) ");
            String command = sc.readLine();

            switch (command) {

                case "등록":
                    System.out.print("명언 : ");
                    String quote = sc.readLine();
                    System.out.print("작가 : ");
                    String writer = sc.readLine();
                    Quote quoteCreate = new Quote(quoteNum, quote, writer);
                    quoteList.add(0, quoteCreate);
                    quoteNum++;
                    System.out.println(quoteNum -1 + "번 명언이 등록되었습니다.");
                    break;

                case "수정":
                    System.out.print("?id=");
                    int modNum = Integer.parseInt(sc.readLine());
                    boolean update = false;
                    for (int i = 0; i < quoteList.size(); i++) {
                        if (quoteList.get(i).getQuoteNum() == modNum) {
                            System.out.println("명언(기존) : " + quoteList.get(i).getQuote());
                            System.out.print("명언 : ");
                            String newQuote = sc.readLine();
                            if (!newQuote.isEmpty()) {
                                quoteList.get(i).setQuote(newQuote);
                            }
                            System.out.println("작가(기존) : " + quoteList.get(i).getWriter());
                            System.out.print("작가 : ");
                            String newWriter = sc.readLine();
                            if (!newWriter.isEmpty()) {
                                quoteList.get(i).setWriter(newWriter);
                            }
                            update = true;
                            System.out.println(modNum + "번 명언이 수정되었습니다.");
                            break;
                        }
                    }
                    if (!update) {
                        System.out.println(modNum + "번 명언은 존재하지 않습니다.");
                    }
                    break;

                case "삭제":
                    System.out.print("?id=");
                    int delNum = Integer.parseInt(sc.readLine());
                    boolean delete = false;
                    for (int i = 0; i < quoteList.size(); i++) {
                        if (quoteList.get(i).getQuoteNum() == delNum) {
                            quoteList.remove(i);
                            delete = true;
                            System.out.println(delNum + "번 명언이 삭제되었습니다.");
                            break;
                        }
                    }
                    if (!delete) {
                        System.out.println(delNum + "번 명언은 존재하지 않습니다.");
                    }
                    break;

                case "목록":
                    System.out.println("번호 / 작가 / 명언");
                    System.out.println("----------------------");
                    for (Quote quoteRead : quoteList) {
                        System.out.println(quoteRead.getQuoteNum() + " / " + quoteRead.getWriter() + " / " + quoteRead.getQuote());
                    }
                    break;

                case "종료":
                    return;


            }


        }


    }
}