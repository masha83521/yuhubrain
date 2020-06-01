package com.masha.yuhubrain;

import android.app.Application;
import android.os.Debug;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.masha.yuhubrain.data.GameAwnser;
import com.masha.yuhubrain.data.GameLevel;
import com.masha.yuhubrain.data.GameQuestion;

public class GameViewModel extends ViewModel {
    public static final int CHANGE_QUESTION_DELAY = 7000;
    private MutableLiveData<GameAwnser> selectedAwnser = new MutableLiveData<>();
    private MutableLiveData<GameQuestion> question;
    private final MutableLiveData<GameLevel> level = new MutableLiveData<>();
    private MutableLiveData<Integer> progress;
    private FirestoreRepository repository = new FirestoreRepository();
    private int questionIndex = 0;

    public LiveData<GameLevel> getLevel(){
        return level;
    }
    //Получаем объект уровня из ссылки на документ по имени
    public void setLevel(String levelName){
        DocumentReference ref = repository.getLevel(levelName);
        ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                try {
                    setLevel(documentSnapshot.toObject(GameLevel.class));
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    //Устанавливаем уровень из объекта
    public void setLevel(final GameLevel level) throws InterruptedException {
        if(this.level.getValue() != null) {
            wait(CHANGE_QUESTION_DELAY);
            this.level.postValue(level);
            questionIndex = 0;
            question = null;
            Log.d("GameViewModel",level.toString());
            getQuestion();
            selectedAwnser.postValue(null);
        }else{
            this.level.postValue(level);
            questionIndex = 0;
            question = null;
            Log.d("GameViewModel",level.toString());
            getQuestion();
            selectedAwnser.postValue(null);
        }
    }

    //Получаем вопрос из документа
    public MutableLiveData<GameQuestion> getQuestion() {
        if(question == null) {
            question = new MediatorLiveData<>();
            ((MediatorLiveData<GameQuestion>)question).addSource(level, new Observer<GameLevel>() {
                @Override
                public void onChanged(GameLevel gameLevel) {
                    DocumentReference ref = gameLevel.getQuestions().get(0);
                    ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            question.setValue(documentSnapshot.toObject(GameQuestion.class));
                        }
                    });
                }
            });
        }
        return question;
    }

    public MutableLiveData<GameAwnser> getSelectedAwnser() {
        return selectedAwnser;
    }

    //Устанавливаем выбранный ответ, переходим к следующему вопросу
    public void setSelectedAwnser(GameAwnser selectedAwnser) {
        this.selectedAwnser.postValue(selectedAwnser);
        questionIndex++;
        if(questionIndex == level.getValue().getQuestions().size()) questionIndex = 0;
        DocumentReference ref = level.getValue().getQuestions().get(questionIndex);
        ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                question.postValue(documentSnapshot.toObject(GameQuestion.class));
            }
        });
    }

    public void setProgress(int progress) {
        this.progress.postValue(progress);
    }

    public MutableLiveData<Integer> getProgress() {
        if(progress == null){
            progress = new MutableLiveData<>();
            progress.postValue(0);
        }
        return progress;
    }
}
