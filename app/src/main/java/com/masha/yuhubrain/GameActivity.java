package com.masha.yuhubrain;

import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.masha.yuhubrain.data.GameAwnser;
import com.masha.yuhubrain.data.GameLevel;
import com.masha.yuhubrain.data.GameQuestion;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private GameViewModel viewModel;
    private GameAwnser awnser1;
    private GameAwnser awnser2;
    private GameAwnser correctAwnser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        final ImageButton anwserButton1 = findViewById(R.id.anwserButton1);
        final ImageButton anwserButton2 = findViewById(R.id.awnserButton2);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        final TextView questionText = findViewById(R.id.questionText);
        final FirestoreRepository repository = new FirestoreRepository();
        anwserButton1.setOnClickListener(this);
        anwserButton2.setOnClickListener(this);
        //Получаем ViewModel
        viewModel = new ViewModelProvider(this).get(GameViewModel.class);
        //Устанавливаем уровень во ViewModel
        viewModel.setLevel(getIntent().getStringExtra("levelName"));
        //Наблюдаем за уровнем в ViewModel
        viewModel.getLevel().observe(this, new Observer<GameLevel>() {
            @Override
            public void onChanged(GameLevel gameLevel) {
                if(gameLevel != null){
                    //Наблюдаем за текущим вопросом
                    viewModel.getQuestion().observe(GameActivity.this, new Observer<GameQuestion>() {
                        @Override
                        public void onChanged(GameQuestion gameQuestion) {
                            //Загружаем первый ответ
                            gameQuestion.getAnwser1().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    awnser1 = task.getResult().toObject(GameAwnser.class);
                                    repository.getImageIntoView(awnser1.getImageLink(),GameActivity.this, anwserButton1);
                                }
                            });
                            //Загружаем второй ответ
                            gameQuestion.getAnwser2().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    awnser2 = task.getResult().toObject(GameAwnser.class);
                                    repository.getImageIntoView(awnser2.getImageLink(),GameActivity.this, anwserButton2);
                                }
                            });
                            //Загружаем правильный ответ
                            gameQuestion.getCorrectAnwser().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    correctAwnser = task.getResult().toObject(GameAwnser.class);
                                }
                            });
                            //Устанавливаем текст вопроса
                            questionText.setText(gameQuestion.getName());
                        }
                    });
                    //Выполняем наблюдение за выбранным ответом
                    viewModel.getSelectedAwnser().observe(GameActivity.this, new Observer<GameAwnser>() {
                        @Override
                        public void onChanged(GameAwnser gameAwnser) {
                            //При выборе ответа проверяем, правильный ли он, после чего изменяем значение процесса
                            if (gameAwnser != null) {
                                if (gameAwnser.getValue().equals(awnser1.getValue()) && gameAwnser.getValue().equals(correctAwnser.getValue()) || gameAwnser.getValue().equals(awnser2.getValue()) && !gameAwnser.getValue().equals(correctAwnser.getValue())) {
                                    if(gameAwnser.getValue().equals(correctAwnser.getValue())) anwserButton1.setImageResource(R.drawable.correct_anwser);
                                    else anwserButton2.setImageResource(R.drawable.wrong_awnser);
                                } else {
                                    if(!gameAwnser.getValue().equals(correctAwnser.getValue())) anwserButton1.setImageResource(R.drawable.wrong_awnser);
                                    else anwserButton2.setImageResource(R.drawable.correct_anwser);
                                }
                                if (gameAwnser.getValue().equals(correctAwnser.getValue())) {
                                    viewModel.setProgress(viewModel.getProgress().getValue() + 1);
                                } else {
                                    viewModel.setProgress(viewModel.getProgress().getValue() - 1);
                                }
                            }
                        }
                    });
                    viewModel.getProgress().observe(GameActivity.this, new Observer<Integer>() {
                        @Override
                        public void onChanged(Integer integer) {
                            //По достижению 20 очков завершаем уровень
                            progressBar.setProgress(integer);
                            if(integer>=20){
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.anwserButton1:
                viewModel.setSelectedAwnser(awnser1);
                break;
            case R.id.awnserButton2:
                viewModel.setSelectedAwnser(awnser2);
                break;
            case R.id.gameBackButton:
                finish();
                break;
        }
    }
}
