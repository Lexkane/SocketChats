package com.company.server;

import com.company.connector.Connector;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.stream.IntStream;

public class Server {

    private static String reverse(String input) {

        return IntStream.range(0, input.length())
                .map(i -> input.charAt(input.length() - i - 1))
                .collect(StringBuilder::new, (sb, c) -> sb.append((char) c), StringBuilder::append)
                .toString();
    }

    private static Long unique(String input) {
        char match = ' ';
        var answer = IntStream.range(0, input.length())
                .map(input::charAt)
                .distinct()
                .collect(StringBuilder::new, (sb, c) -> sb.append((char) c), StringBuilder::append)
                .toString();
        System.out.println(answer);


        return IntStream.range(0, input.length())
                .map(input::charAt)
                .filter(e -> e != match)
                .distinct()
                .count();
    }


    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(8000)) {

            System.out.println("Server started!");
            while (true) {
                Connector connector = new Connector(server);

                new Thread(() -> {
                    String request = connector.readLine();
                    System.out.printf("Response: %s \n", request);
                    Long response = Server.unique(request);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    connector.writeLine(response.toString());
                    System.out.printf("Response: %s \n", response);
                    try {
                        connector.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();

            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
