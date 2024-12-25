package com.example.noteapp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp.R;
import com.example.noteapp.adapters.UserAdapter;
import com.example.noteapp.database.NoteDatabase;
import com.example.noteapp.entities.User;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserManagementActivity extends AppCompatActivity {

    private EditText editTextUserName, editTextUserEmail;
    private Button buttonAddUser;
    private RecyclerView recyclerViewUsers;
    private UserAdapter userAdapter;
    private NoteDatabase noteDatabase;

    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        editTextUserName = findViewById(R.id.editTextUserName);
        editTextUserEmail = findViewById(R.id.editTextUserEmail);
        buttonAddUser = findViewById(R.id.buttonAddUser);
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);

        noteDatabase = NoteDatabase.getNoteDatabase(this);

        buttonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });

        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        loadUsers();
    }

//    private void addUser() {
//        String name = editTextUserName.getText().toString().trim();
//        String email = editTextUserEmail.getText().toString().trim();
//
//        if (name.isEmpty() || email.isEmpty()) {
//            Toast.makeText(this, "Please enter both name and email", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        User user = new User();
//        user.setName(name);
//        user.setEmail(email);
//
//        noteDatabase.userDao().insertUser(user);
//        Toast.makeText(this, "User added", Toast.LENGTH_SHORT).show();
//        loadUsers();
//    }
//
//    private void loadUsers() {
//        List<User> users = noteDatabase.userDao().getAllUsers();
//        userAdapter = new UserAdapter(users);
//        recyclerViewUsers.setAdapter(userAdapter);
//    }

    // 使用Executor来执行数据库操作，避免主线程上访问数据库会导致UI线程长时间锁定，从而引发IllegalStateException。
    private void addUser() {
        String name = editTextUserName.getText().toString().trim();
        String email = editTextUserEmail.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please enter both name and email", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = new User();
        user.setName(name);
        user.setEmail(email);

        executor.execute(new Runnable() {
            @Override
            public void run() {
                noteDatabase.userDao().insertUser(user);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UserManagementActivity.this, "User added", Toast.LENGTH_SHORT).show();
                        loadUsers();
                    }
                });
            }
        });
    }

    private void loadUsers() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                List<User> users = noteDatabase.userDao().getAllUsers();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        userAdapter = new UserAdapter(users);
                        recyclerViewUsers.setAdapter(userAdapter);
                    }
                });
            }
        });
    }

    // 通过点击用户列表项，切换用户
    private void switchUser(User user) {
        Intent intent = new Intent(UserManagementActivity.this, MainActivity.class);
        intent.putExtra("userId", user.getId());
        startActivity(intent);
    }
}
