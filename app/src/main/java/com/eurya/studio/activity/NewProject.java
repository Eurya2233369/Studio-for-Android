package com.eurya.studio.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eurya.studio.R;
import com.eurya.studio.adapter.CreateProjectAdapter;

import com.eurya.studio.databinding.ActivityProjectBinding;
import java.util.Arrays;

public class NewProject extends AppCompatActivity {

  private ActivityProjectBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityProjectBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
    setSupportActionBar(binding.toolbar);
    binding.createProjectList.setLayoutManager(new GridLayoutManager(this, 2));
    CreateProjectAdapter adapter =
        new CreateProjectAdapter(
            this, Arrays.asList(getResources().getStringArray(R.array.create_project_item)));
    binding.createProjectList.setAdapter(adapter);
  }
}
