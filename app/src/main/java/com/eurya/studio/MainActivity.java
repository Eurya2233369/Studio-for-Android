package com.eurya.studio;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.*;
import android.util.Log;
import android.content.Context;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.eurya.studio.activity.NewProject;
import com.eurya.studio.adapter.ProjectAdapter;
import com.eurya.studio.databinding.ActivityMainBinding;
import com.eurya.studio.utils.StudioUtil;

import com.itsaky.androidide.logsender.LogSender;

import java.io.File;


/**
 * @autuor Eurya QiuZhu
 * @date 2023.1.16
 * @emali 2644635373@qq.com
 */
 
public class MainActivity extends AppCompatActivity {
	
	private ActivityMainBinding binding;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
		LogSender.startLogging(this);
        super.onCreate(savedInstanceState);
		binding = ActivityMainBinding.inflate(getLayoutInflater());
		setSupportActionBar(binding.toolbar);
        setContentView(binding.getRoot());
		binding.fab.setOnClickListener(v -> startActivity(new Intent(this, NewProject.class)));
        initProject();
    }
    
    private void initProject(){
        var project_adapter = new ProjectAdapter(this, StudioUtil.getProjectHome());
        var layout_manager = new LinearLayoutManager(this);
        binding.recycler.setLayoutManager(layout_manager);    
        binding.recycler.setAdapter(project_adapter);
    }
}
