package com.example.practicevoca.data;

import android.content.Context;
import android.util.Log;

import com.example.practicevoca.object.Word;
import com.example.practicevoca.ui.MainPage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class InnerStorageManager {
    private static final String fileName = "words.txt";

    public InnerStorageManager() {}

    public static void saveFile(Context context) {
        BufferedWriter writer = null;
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));
            for (Word word : MainPage.wordList) {
                writer.write(word.getEng() + ":" + word.getKor());
                writer.newLine();
            }
        } catch (IOException e) {
            Log.w("saveFile", "Error saving file", e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    Log.w("saveFile", "Error closing writer", e);
                }
            }
        }
    }

    public static void loadFile(Context context) {
        BufferedReader reader = null;
        try {
            FileInputStream fis = context.openFileInput(fileName);
            reader = new BufferedReader(new InputStreamReader(fis, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split(":");
                if (words.length == 2) {
                    MainPage.wordList.add(new Word(words[0], words[1]));
                }
            }
        } catch (FileNotFoundException e) {
            createFile(context);
        } catch (IOException e) {
            Log.w("loadFile", "Error loading file", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.w("loadFile", "Error closing reader", e);
                }
            }
        }
    }

    private static void createFile(Context context) {
        BufferedWriter writer = null;
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));
            writer.write("");  // 빈 파일 생성
        } catch (IOException e) {
            Log.w("createFile", "Error creating file", e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    Log.w("createFile", "Error closing writer", e);
                }
            }
        }
    }
}
