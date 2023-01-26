package com.eurya.studio;

import androidx.appcompat.app.AppCompatActivity;
import android.os.*;
import android.util.Log;
import android.content.Context;
import android.widget.Toast;
import com.eurya.studio.databinding.ActivityMainBinding;
import com.itsaky.androidide.logsender.LogSender;


/**
 * @autuor Eurya QiuZhu
 * @time 2023.1.16
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
		binding.fab.setOnClickListener(v -> Toast.makeText(MainActivity.this, "Replace with your action", Toast.LENGTH_SHORT).show());
            
    }
}
