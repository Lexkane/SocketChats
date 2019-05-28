package com.company.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.stream.IntStream;

//ALgorithms reverse input Array of Strings
public class Controller {

    private static String reverse(String test) {

        return IntStream.range(0, test.length())
                .map(i -> test.charAt(test.length() - i - 1))
                .collect(StringBuilder::new, (sb, c) -> sb.append((char) c), StringBuilder::append)
                .toString();
    }

    private static Long unique(String in) {
        String input = in.trim();
        char match=' ';
        return IntStream.range(0, input.length())
                .map(input::charAt)
                .filter(e -> e!=match)
                .distinct()
                .count();
    }

    /*
    public static String byteStringReverse(String string) {
        String resultString="";
        byte[] stringAsByteArray = string.getBytes();
        byte[] result = new byte[stringAsByteArray.length];
        for (int i = 0; i < stringAsByteArray.length; i++) {
            result[i] = stringAsByteArray[stringAsByteArray.length - 1];
        }
        try {
            resultString = new String(result, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            System.out.println("Something went wrong");
        }
        return resultString;
    }
    */

    public static void main(String[] args) {
        List<String> inputList = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        StringTokenizer st = new StringTokenizer(str);
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            try {
                inputList.add(token);
            } catch (Exception e) {
                System.out.println("Something went wrong!");
            }
        }

        for (var s : inputList) {
            System.out.print(s + " ");
        }
        System.out.println();


        /*inputList.replaceAll(s->s.chars()
                    .collect(StringBuilder::new, (b, c) -> b.insert(0, (char) c), (b1, b2) -> b1.insert(0, b2))
                    .toString());
        */

         /* inputList.replaceAll(srt->srt.chars()
                    .mapToObj(c->(char)(c))
                    .reduce("",(s,c)->c+s,(s1,s2)->s2+s1));

*/

        for (int i = 0; i < inputList.size(); ++i) {
            inputList.set(i, reverse(inputList.get(i)));
        }


        for (var s : inputList) {
            System.out.print(s + " ");
        }


    }
}

