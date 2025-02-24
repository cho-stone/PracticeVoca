package com.example.practicevoca.data;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.practicevoca.object.Word;
import com.example.practicevoca.ui.MainPage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class FireBaseManager {
    private DatabaseReference mDatabase;

    public void saveToCloud(String uuid, Context context) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(uuid).child("Words").setValue(MainPage.wordList);
        Toast.makeText(context, "업로드가 완료되었습니다.", Toast.LENGTH_SHORT).show();
    }

    public void loadToCloud(String uuid, Context context) {
        mDatabase = FirebaseDatabase.getInstance().getReference(uuid).child("Words");

        ValueEventListener loadWordListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ArrayList<Word> newWordList = extractWordList(snapshot);

                    int oldSize = MainPage.wordList.size();
                    MainPage.wordList.clear();
                    MainPage.wordList.addAll(newWordList);
                    int newSize = MainPage.wordList.size();

                    if (newSize > oldSize) {
                        MainPage.wordListAdapter.notifyItemRangeInserted(0, newSize - oldSize);
                    } else if (newSize < oldSize) {
                        MainPage.wordListAdapter.notifyItemRangeRemoved(0, oldSize - newSize);
                    } else {
                        MainPage.wordListAdapter.notifyItemRangeChanged(0, newSize);
                    }

                    InnerStorageManager.saveFile(context);
                    Toast.makeText(context, "로딩 완료되었습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(context, "존재하지 않는 코드입니다.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("loadToCloudFailed", "loadPost:onCancelled", error.toException());
            }
        };
        mDatabase.addListenerForSingleValueEvent(loadWordListener);
    }

    /** @noinspection unchecked*/
    private ArrayList<Word> extractWordList(DataSnapshot snapshot) {
        ArrayList<Word> newWordList = new ArrayList<>();

        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
            HashMap<String, Object> wordMap = (HashMap<String, Object>) dataSnapshot.getValue();

            if (wordMap != null) {
                String eng = (String) wordMap.get("eng");
                String kor = (String) wordMap.get("kor");

                Word word = new Word(eng, kor);
                newWordList.add(word);
            }
        }

        return newWordList;
    }
}
