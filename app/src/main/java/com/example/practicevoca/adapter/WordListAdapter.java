package com.example.practicevoca.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practicevoca.R;
import com.example.practicevoca.object.Word;
import com.example.practicevoca.ui.DeleteAlertPage;
import com.example.practicevoca.ui.MainPage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.CustomViewHolder> {
    private boolean isEngVisible = true;
    private boolean isKorVisible = true;
    private final MainPage mainPage;
    private List<Word> filteredWordList;
    public static boolean isFiltered;

    public WordListAdapter(Context context) {
        this.mainPage = (MainPage) context;
        isFiltered = false;
        filteredWordList = new ArrayList<>(MainPage.wordList);
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv_eng;
        private final TextView tv_kor;

        public CustomViewHolder(View view) {
            super(view);
            tv_eng = view.findViewById(R.id.tv_eng_word);
            tv_kor = view.findViewById(R.id.tv_kor_word);
        }
    }

    @NonNull
    @Override
    public WordListAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_list_view, parent, false);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordListAdapter.CustomViewHolder holder, int position) {

        if(isFiltered) {
            Word word = filteredWordList.get(position);
            holder.tv_eng.setText(word.getEng());
            holder.tv_kor.setText(word.getKor());
        }
        else {
            holder.tv_eng.setText(MainPage.wordList.get(position).getEng());
            holder.tv_kor.setText(MainPage.wordList.get(position).getKor());
        }

        holder.tv_eng.setVisibility(isEngVisible ? View.VISIBLE : View.GONE);
        holder.tv_kor.setVisibility(isKorVisible ? View.VISIBLE : View.GONE);

        holder.itemView.setTag(position);


        if(!isFiltered) {
            holder.itemView.setOnLongClickListener(view -> {
                DeleteAlertPage deleteAlertPage = new DeleteAlertPage(mainPage, holder);
                Objects.requireNonNull(deleteAlertPage.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                deleteAlertPage.setCancelable(false);
                deleteAlertPage.show();

                return true;
            });
        } else {
            holder.itemView.setOnLongClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        if(isFiltered) {
            return (filteredWordList != null ? filteredWordList.size() : 0);
        }
        return (null != MainPage.wordList ? MainPage.wordList.size() : 0);
    }

    public void setEngVisible(boolean isVisible) {
        this.isEngVisible = isVisible;
        notifyItemRangeChanged(0, getItemCount());
    }

    public void setKorVisible(boolean isVisible) {
        this.isKorVisible = isVisible;
        notifyItemRangeChanged(0, getItemCount());
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filter(String text) {
        if (text.isEmpty()) {
            filteredWordList = new ArrayList<>(MainPage.wordList);
            isFiltered = false;
        } else {
            filteredWordList = MainPage.wordList.stream()
                    .filter(word -> word.getEng().toLowerCase().contains(text.toLowerCase()) ||
                            word.getKor().contains(text))
                    .collect(Collectors.toList());
            isFiltered = true;
        }
        notifyDataSetChanged();
    }
}