package com.example.practicevoca.ui;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.Toast;

import com.example.practicevoca.R;
import com.example.practicevoca.data.FireBaseManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.regex.Pattern;

public class LoadPage extends Dialog {

    private TextInputLayout key_input_layout;
    private TextInputEditText key_input;
    private FireBaseManager fireBaseManager;
    private final String key_validation = "^[A-Za-z0-9+/=_-]{22}(==)?|^[A-Za-z0-9+/=_-]{24}$";
    private boolean isKeyValidate;
    private final Context context;

    public LoadPage(Context context) {
        super(context);
        this.context = context;
        setContentView(R.layout.activity_load_page);
        init();
    }

    private void init() {
        key_input_layout = findViewById(R.id.passKey_input_layout);
        key_input = findViewById(R.id.passKey_input);
        Button btn_confirm = findViewById(R.id.btn_load_confirm);
        Button btn_close = findViewById(R.id.btn_load_close);
        fireBaseManager = new FireBaseManager();
        isKeyValidate = false;

        key_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String keyCheck = Objects.requireNonNull(key_input.getText()).toString();
                boolean validation = Pattern.matches(key_validation, keyCheck);
                if (validation) {
                    key_input_layout.setError("");
                    isKeyValidate = true;
                }
                else if (keyCheck.isEmpty()) {
                    key_input_layout.setError("코드를 입력해 주세요.");
                    isKeyValidate = false;
                }
                else {
                    key_input.setError("형식이 올바르지 않습니다.");
                    isKeyValidate = false;
                }
            }
        });

        btn_confirm.setOnClickListener(view -> {
            if(isKeyValidate) {
                fireBaseManager.loadToCloud(Objects.requireNonNull(key_input.getText()).toString(), context);
                dismiss();
            }
            else {
                Toast.makeText(context, "키를 정확히 입력해 주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        btn_close.setOnClickListener(view -> dismiss());
    }
}
