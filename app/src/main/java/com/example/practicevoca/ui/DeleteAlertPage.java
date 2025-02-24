package com.example.practicevoca.ui;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.example.practicevoca.R;
import com.example.practicevoca.adapter.WordListAdapter;
import com.example.practicevoca.data.InnerStorageManager;


public class DeleteAlertPage extends Dialog {

    private final Context context;

    public DeleteAlertPage(Context context, WordListAdapter.CustomViewHolder holder) {
        super(context);
        this.context = context;
        setContentView(R.layout.dialog_alert);
        init(holder);
    }

    private void init(WordListAdapter.CustomViewHolder holder) {
        Button btn_confirm = findViewById(R.id.btn_delete_confirm);
        Button btn_close = findViewById(R.id.btn_delete_close);

        btn_confirm.setOnClickListener(view -> {
            int adapterPosition = holder.getBindingAdapterPosition();
            if(adapterPosition != RecyclerView.NO_POSITION) {
                MainPage.wordList.remove(adapterPosition);

                MainPage.wordListAdapter.notifyItemRemoved(adapterPosition);
                MainPage.wordListAdapter.notifyItemRangeChanged(adapterPosition, MainPage.wordList.size());

                InnerStorageManager.saveFile(context);
                dismiss();
            }
        });

        btn_close.setOnClickListener(view -> dismiss());
    }
}
