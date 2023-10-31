package ru.nsu.evdokimov;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Main {

    private final static int lineLimit = 25;
    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.out.println("Usage: java SimpleHttpClient <url>");
            return;
        }

        String urlStr = args[0]; // получаем URL из аргумента командной строки
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(); // открываем HTTP-соединение

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {

            printBody(reader);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect(); // закрываем HTTP-соединение
        }
    }

    private static void printBody(BufferedReader reader) throws IOException {
        int lineCount = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
            lineCount++;

            if (lineCount == lineLimit) {
                System.out.println("Press space to scroll down.");
                char c = (char) System.in.read(); // ожидание нажатия пробела для прокрутки
                while (c != ' ') {
                    c = (char) System.in.read();
                }
                lineCount = 0;
            }
        }
    }

}

