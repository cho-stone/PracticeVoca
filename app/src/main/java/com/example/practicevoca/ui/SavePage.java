package com.example.practicevoca.ui;


import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;


import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.practicevoca.R;
import com.example.practicevoca.data.FireBaseManager;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

public class SavePage extends Dialog {

    private TextView tv_UUID;
    private FireBaseManager fireBaseManager;
    private final Context context;

    public SavePage(Context context) {
        super(context);
        this.context = context;
        setContentView(R.layout.activity_save_page);
        init();
    }

    private void init() {
        tv_UUID = findViewById(R.id.tv_UUID);
        Button btn_confirm = findViewById(R.id.btn_backup_confirm);
        Button btn_close = findViewById(R.id.btn_backup_close);
        fireBaseManager = new FireBaseManager();

        UUID uuid = UUID.randomUUID();

        // UUID 바이트 배열로 변환
        ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[16]);
        byteBuffer.putLong(uuid.getMostSignificantBits());
        byteBuffer.putLong(uuid.getLeastSignificantBits());
        byte[] uuidBytes = byteBuffer.array();

        // Base64로 인코딩
        String base64UUID = Base64.getUrlEncoder().withoutPadding().encodeToString(uuidBytes);

        tv_UUID.setText(base64UUID);
        tv_UUID.setOnClickListener(view -> {
            String textToCopy = tv_UUID.getText().toString();
            copyToClipboard(textToCopy);
        });

        btn_confirm.setOnClickListener(view -> {
            if(MainPage.wordList.size() > 0) {
                fireBaseManager.saveToCloud(tv_UUID.getText().toString(), context);
                dismiss();
            }
            else {
                Toast.makeText(context, "단어를 1개 이상 등록해 주세요.", Toast.LENGTH_SHORT).show();
            }

        });

        btn_close.setOnClickListener(view -> dismiss());
    }

    private void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
    }
}
