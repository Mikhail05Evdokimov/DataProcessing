package ru.nsu.evdokimov;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SimpleHttpClient {

    public static void main(String[] args) throws IOException {
        args = new String[]{"https://table.nsu.ru/group/21215"};
        if (args.length != 1) {
            System.out.println("Usage: java SimpleHttpClient <url>");
            return;
        }

        String urlStr = args[0]; // получаем URL из аргумента командной строки
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(); // открываем HTTP-соединение

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            int lineCount = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                lineCount++;

                if (lineCount == 25) {
                    System.out.println("Press space to scroll down.");
                    System.in.read(); // ожидание нажатия пробела для прокрутки
                    lineCount = 0;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect(); // закрываем HTTP-соединение
        }
    }
}
