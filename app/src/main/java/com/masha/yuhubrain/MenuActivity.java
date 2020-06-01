package com.masha.yuhubrain;

import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.util.Function;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import com.masha.yuhubrain.data.GameLevel;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final GridLayoutManager lManager = new GridLayoutManager(this, 2);
        //Устанавливаем, что если нечетное количество элементов, то последний элемент растягиваем на 2 колонки
        lManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(position == lManager.getItemCount()-1 && position%2==0){
                    return 2;
                }else{
                    return 1;
                }
            }
        });
        recyclerView.setLayoutManager(lManager);
        recyclerView.setAdapter(new RecyclerAdapter(new ArrayList<GameLevel>(),this));
        //Подключаем viewModel
        MenuViewModel viewModel = new ViewModelProvider(this).get(MenuViewModel.class);
        viewModel.setLevels();
        //Выполняем наблюдение за уровнями и обновляем recyclerView
        viewModel.getLevels().observe(this, new Observer<List<GameLevel>>() {
            @Override
            public void onChanged(List<GameLevel> gameLevels) {
                if(gameLevels != null){
                    recyclerView.setAdapter(new RecyclerAdapter(gameLevels,MenuActivity.this));
                }
            }
        });
    }
}
