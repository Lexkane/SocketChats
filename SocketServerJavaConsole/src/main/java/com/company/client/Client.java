package com.company.client;

import com.company.connector.Connector;

import java.io.*;

public class Client {

    public static void main(String[] args) throws IOException {
        try (Connector connector = new Connector ("127.0.0.1", 8000)) {
            System.out.println("Connected to server");
            String request = "aaabbc kkkk vvv zz i p";
            System.out.printf("Request: %s \n", request);
            connector.writeLine(request);
            String response = connector.readLine();
            System.out.printf("Response: %s \n", response);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
