package com.ll;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public String hello() {
        return "hello world";
    }

    private static final String file = "C:\\Users\\82107\\data.json";
    private static List<Quote> quoteList;
    private static int quoteNum;

    public static void main(String[] args) throws IOException {
        System.out.println("== 명언 앱 ==");

        quoteList = callData();
        quoteNum = nextQuoteNum(quoteList);
        BufferedReader sc = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.print("명령) ");
            String command = sc.readLine();

            // 등록 목록 수정 삭제 종료

            switch (command) {
                case "등록":
                    createQuote(sc);
                    break;

                case "목록":
                    readQuotes();
                    break;

                case "수정":
                    updateQuote(sc);
                    break;

                case "삭제":
                    deleteQuote(sc);
                    break;

                case "빌드":
                    saveData();
                    System.out.println("data.json 파일의 내용이 갱신되었습니다.");
                    break;

                case "종료":
                    saveData();
                    return;
            }
        }
    }

    private static void createQuote(BufferedReader sc) throws IOException {
        System.out.print("명언 : ");
        String quote = sc.readLine();
        System.out.print("작가 : ");
        String writer = sc.readLine();
        //LocalDateTime now = LocalDateTime.now();
        Quote quoteCreate = new Quote(quoteNum, quote, writer);
        //Quote quoteCreate = new Quote(quoteNum, quote, writer, now, now);
        quoteList.add(0, quoteCreate);
        quoteNum++;
        System.out.println(quoteNum - 1 + "번 명언이 등록되었습니다.");
    }
    private static void readQuotes() {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");
        for (Quote quoteRead : quoteList) {
            System.out.println(quoteRead.getQuoteNum() + " / " + quoteRead.getWriter() + " / " + quoteRead.getQuote());
        }
    }
    private static void updateQuote(BufferedReader sc) throws IOException {
        System.out.print("?id=");
        int modNum = Integer.parseInt(sc.readLine());
        boolean update = false;
        //LocalDateTime updateDate = LocalDateTime.now();
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
                // quoteList.get(i).setUpdatedDate(updateDate);
                update = true;
                System.out.println(modNum + "번 명언이 수정되었습니다.");
                break;
            }
        }
        if (!update) {
            System.out.println(modNum + "번 명언은 존재하지 않습니다.");
        }
    }

    private static void deleteQuote(BufferedReader sc) throws IOException {
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
    }

    private static int nextQuoteNum(List<Quote> quoteList) {
        int max = 0;
        for (Quote quoteNext : quoteList) {
            if (quoteNext.getQuoteNum() > max) {
                max = quoteNext.getQuoteNum();
            }
        }
        return max + 1;
    }
//quoteList를 초기화하여 명언 데이터를 보유할 목록을 생성
//ObjectMapper를 사용하여 JSON 파일을 읽기 위한 객체를 생성
//JSON 파일을 읽어오기 위해 파일 경로로부터 File 객체를 만들고, 파일이 존재하는지 확인
//JSON 파일에서 명언 데이터를 읽어온 후, quote[] quotes 배열로 저장
//읽어온 명언 데이터를 명언 목록에 추가
//명언 목록을 반환
    private static List<Quote> callData() {
        List<Quote> quoteList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File jsonFile = new File(file);
            if (jsonFile.exists()) {
                Quote[] quotes = objectMapper.readValue(jsonFile, Quote[].class);
                quoteList.addAll(List.of(quotes));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return quoteList; // JSON 파일에서 읽어온 명언 목록을 반환
    }
//ObjectMapper를 사용하여 JSON 파일에 데이터를 저장하기 위한 객체를 생성
//objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(file), quoteList)를 사용하여
//명언 목록을 JSON 파일에 저장
//저장할 때 데이터를 들여쓰기된 형식으로 저장
    private static void saveData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(file), quoteList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}