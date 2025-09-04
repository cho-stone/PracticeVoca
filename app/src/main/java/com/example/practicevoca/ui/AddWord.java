package com.example.practicevoca.ui;


import android.app.Dialog;
import android.content.Context;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.Toast;


import com.example.practicevoca.R;
import com.example.practicevoca.data.InnerStorageManager;
import com.example.practicevoca.object.Word;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.regex.Pattern;

public class AddWord extends Dialog {

    private TextInputEditText kor_input, eng_input;
    private TextInputLayout kor_input_layout, eng_input_layout;
    private final String eng_validation = "^[\\p{IsHangul}\\p{IsHiragana}\\p{IsKatakana}\\p{IsHan}a-zA-Z0-9\\s',/]+$";
    private final String kor_validation = "^[\\p{IsHangul}\\p{IsHiragana}\\p{IsKatakana}\\p{IsHan}a-zA-Z0-9\\s',/]+$";
    private boolean isEngValidate = false;
    private boolean isKorValidate = false;
    private final Context context;

    public AddWord(Context context) {
        super(context);
        this.context = context;
        setContentView(R.layout.activity_add_word);
        init();
    }

    private void init() {
        eng_input_layout = findViewById(R.id.eng_input_layout);
        kor_input_layout = findViewById(R.id.kor_input_layout);
        eng_input = findViewById(R.id.eng_input);
        kor_input = findViewById(R.id.kor_input);

        eng_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String engCheck = Objects.requireNonNull(eng_input.getText()).toString();
                boolean validation = Pattern.matches(eng_validation, engCheck);
                if (validation) {
                    eng_input_layout.setError("");
                    isEngValidate = true;
                }
                else if (engCheck.isEmpty()) {
                    eng_input_layout.setError("단어를 입력해 주세요.");
                    isEngValidate = false;
                }
                else {
                    eng_input.setError("형식이 올바르지 않습니다.");
                    isEngValidate = false;
                }
            }
        });

        kor_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String korCheck = Objects.requireNonNull(kor_input.getText()).toString();
                boolean validation = Pattern.matches(kor_validation, korCheck);
                if (validation) {
                    kor_input_layout.setError("");
                    isKorValidate = true;
                }
                else if (korCheck.isEmpty()) {
                    kor_input_layout.setError("단어를 입력해 주세요.");
                    isKorValidate = false;
                }
                else {
                    kor_input.setError("형식이 올바르지 않습니다.");
                    isKorValidate = false;
                }
            }
        });

        Button confirm = findViewById(R.id.btn_add_word_confirm);
        confirm.setOnClickListener(view -> {

            if(isEngValidate && isKorValidate) {
                MainPage.wordList.add(new Word(Objects.requireNonNull(eng_input.getText()).toString().trim(), Objects.requireNonNull(kor_input.getText()).toString().trim()));
                InnerStorageManager.saveFile(context);
                Toast.makeText(context, "추가되었습니다.", Toast.LENGTH_SHORT).show();
                dismiss();
            }
            else {
                Toast.makeText(context, "단어를 정확히 입력해 주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        Button close = findViewById(R.id.btn_add_word_close);
        close.setOnClickListener(view -> dismiss());
    }
}
