package com.example.practicevoca.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.practicevoca.R;
import com.example.practicevoca.adapter.WordListAdapter;
import com.example.practicevoca.data.InnerStorageManager;
import com.example.practicevoca.object.Word;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Objects;

public class MainPage extends AppCompatActivity {

    public static ArrayList<Word> wordList;
    public static WordListAdapter wordListAdapter;
    private Button engButton, korButton;
    private TextView tv_main_text;
    private boolean isEngButton, isKorButton;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        signInAnonymously();

        Toolbar toolbar = findViewById(R.id.main_topic);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        SearchView searchView = findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                wordListAdapter.filter(newText);
                return true;
            }
        });
    }

    private void setClickListener() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            if(WordListAdapter.isFiltered) {
                Toast.makeText(MainPage.this, "검색 기능을 종료해 주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            AddWord addWord = new AddWord(MainPage.this);
            Objects.requireNonNull(addWord.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            addWord.setCancelable(false);
            addWord.show();
        });

        isEngButton = true;
        isKorButton = true;

        engButton = findViewById(R.id.btn_Eng_invisible);
        engButton.setOnClickListener(view -> {
            if(isEngButton) {
                engButton.setText(getResources().getString(R.string.eng_visible_txt));
                isEngButton = false;
                wordListAdapter.setEngVisible(false);
            }
            else {
                engButton.setText(getResources().getString(R.string.eng_invisible_txt));
                isEngButton = true;
                wordListAdapter.setEngVisible(true);
            }
        });

        korButton = findViewById(R.id.btn_Kor_invisible);
        korButton.setOnClickListener(view -> {
            if(isKorButton) {
                korButton.setText(getResources().getString(R.string.kor_visible_txt));
                isKorButton = false;
                wordListAdapter.setKorVisible(false);
            }
            else {
                korButton.setText(getResources().getString(R.string.kor_invisible_txt));
                isKorButton = true;
                wordListAdapter.setKorVisible(true);
            }
        });

        tv_main_text = findViewById(R.id.mainTextView);
        tv_main_text.setOnClickListener(view -> {
            Toast.makeText(MainPage.this, "터치", Toast.LENGTH_SHORT).show();
        });
    }

    private void view_words() {
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        wordListAdapter = new WordListAdapter(MainPage.this);
        recyclerView.setAdapter(wordListAdapter);
        wordListAdapter.notifyItemRangeChanged(0, wordListAdapter.getItemCount());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if(WordListAdapter.isFiltered) {
            Toast.makeText(MainPage.this, "검색 기능을 종료해 주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (itemId == R.id.menu_backupData) {
            SavePage savePage = new SavePage(MainPage.this);
            Objects.requireNonNull(savePage.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            savePage.setCancelable(false);
            savePage.show();
        }
        else if (itemId == R.id.menu_loadData) {
            LoadPage loadPage = new LoadPage(MainPage.this);
            Objects.requireNonNull(loadPage.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loadPage.setCancelable(false);
            loadPage.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI(currentUser);
        }
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Toast.makeText(MainPage.this, "로그인 인증 실패", Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            wordList = new ArrayList<>();
            InnerStorageManager.loadFile(this);

            view_words();
            setClickListener();
        } else {
            Toast.makeText(MainPage.this, "앱을 다시 실행해 주세요", Toast.LENGTH_SHORT).show();
        }
    }
}