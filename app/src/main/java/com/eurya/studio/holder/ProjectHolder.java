package com.eurya.studio.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.eurya.studio.R;

/*
* @autuor Eurya QiuZhu
* @date 2023.1.26
* @emali 2644635373@qq.com
**/

public class ProjectHolder extends RecyclerView.ViewHolder {
    
    public ImageView item_image;
    public TextView item_name;
    public TextView item_type;
    
    public ProjectHolder(@Nullable View itemView){
        super(itemView);
        item_image = itemView.findViewById(R.id.mProject_image);
        item_name = itemView.findViewById(R.id.mProject_name);
        item_type = itemView.findViewById(R.id.mProject_type);        
    }
    
}
