package com.masha.yuhubrain;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.masha.yuhubrain.data.User;

import org.w3c.dom.Document;

public class SingUp extends AppCompatActivity {
    TextInputEditText regName, regUsername,regEmail,regPassword;
    Button regBtn, toLoginBtn;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sing_up);

        regName = findViewById(R.id.reg_name);
        regUsername = findViewById(R.id.reg_username);
        regEmail = findViewById(R.id.reg_email);
        regPassword = findViewById(R.id.reg_password);
        regBtn = findViewById(R.id.reg_btn);
        toLoginBtn = findViewById(R.id.to_login_btn);
        //Переходим к политике конфиденциальности
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SingUp.this, ConfActivity.class);
                intent.putExtra("user",new User(regUsername.getText().toString(), regEmail.getText().toString(), regName.getText().toString()));
                intent.putExtra("password", regPassword.getText().toString());
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    final User user1 = (User) data.getSerializableExtra("user");
                    final String password = data.getStringExtra("password");
                    Toast.makeText(this, password, Toast.LENGTH_SHORT);
                    db = FirebaseFirestore.getInstance();
                    DocumentReference docRef = db.collection("users").document(user1.getUsername());
                    //Проверяем, существует ли пользователь
                    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            //Если существует, выводим диалоговое окно
                            if(task.isSuccessful()){
                                DocumentSnapshot document = task.getResult();
                                final User user = document.toObject(User.class);
                                if(user != null){
                                    new AlertDialog.Builder(SingUp.this)
                                            .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                }
                                            })
                                            .setTitle("Ошибка регистрации")
                                            .setMessage("Пользователь уже существует")
                                            .create()
                                            .show();
                                }else{
                                    db.collection("users").document(user1.getUsername()).set(user1);
                                    mAuth = FirebaseAuth.getInstance();
                                    Log.d("SingUp",user1.getEmail());
                                    Log.d("SingUp",password);
                                    //Пытаемся создать пользователя
                                    mAuth.createUserWithEmailAndPassword(user1.getEmail(), password).addOnCompleteListener(SingUp.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            //В случае успешной регистрации переходим к меню игры
                                            if(task.isSuccessful()){
                                                Toast.makeText(SingUp.this, "Регистрация прошла успешно\n"+user.getUsername()+"\n"+user.getEmail(),Toast.LENGTH_LONG);
                                                Intent intent = new Intent(SingUp.this, MenuActivity.class);
                                                startActivity(intent);
                                                //Если адрес почты занят, пароль слишком слабый или адрес электронной почты некорректен, выводим соответствующее сообщение
                                            }else if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                                new AlertDialog.Builder(SingUp.this)
                                                        .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.cancel();
                                                            }
                                                        })
                                                        .setTitle("Ошибка регистрации")
                                                        .setMessage("Данный адрес электронной почты занят")
                                                        .create()
                                                        .show();
                                                db.collection("users").document(user1.getUsername()).delete();
                                            }else if(task.getException() instanceof FirebaseAuthWeakPasswordException){
                                                new AlertDialog.Builder(SingUp.this)
                                                        .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.cancel();
                                                            }
                                                        })
                                                        .setTitle("Ошибка регистрации")
                                                        .setMessage("Пароль слишком слабый")
                                                        .create()
                                                        .show();
                                                db.collection("users").document(user1.getUsername()).delete();
                                            }else{
                                                new AlertDialog.Builder(SingUp.this)
                                                        .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.cancel();
                                                            }
                                                        })
                                                        .setTitle("Ошибка регистрации")
                                                        .setMessage("Неправильный адрес электронной почты")
                                                        .create()
                                                        .show();
                                                db.collection("users").document(user1.getUsername()).delete();
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                }
        }
    }
}
