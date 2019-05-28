package com.company.connector;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Connector  implements Closeable {
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;

    public Connector (Socket socket) {
        try {
            this.socket = socket;
            this.reader = createReader(this.socket.getInputStream());
            this.writer = createWriter(this.socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Connector (String ip, int port) {
        try {
            this.socket = new Socket(ip, port);
            this.reader = createReader(this.socket.getInputStream());
            this.writer = createWriter(this.socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Connector (ServerSocket server) {
        try {
            this.socket = server.accept();
            this.reader = createReader(this.socket.getInputStream());
            this.writer = createWriter(this.socket.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeLine (String message) {
        try {
            writer.write(message);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public String readLine(String message) {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException();
        }

    }

    public String readLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException();
        }

    }


    private BufferedReader createReader(InputStream input) {
        return new BufferedReader(new InputStreamReader(input));
    }

    private BufferedWriter createWriter(OutputStream output) {
        return new BufferedWriter(new OutputStreamWriter(output));
    }

    @Override
    public void close() throws IOException {
        reader.close();
        writer.close();
        socket.close();
    }


}
