package com.ll;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println("== 명언 앱 ==");

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
                    break;


                case "종료":
                    return;


            }


        }


    }
}