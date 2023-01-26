package com.eurya.studio.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class ProjectHolder extends RecyclerView.ViewHolder {
    
    private ImageView item_image;
    private TextView item_name;
    private TextView item_type;
    
    public ProjectHolder(@Nullable final View itemView){
        super(itemView);
        
    }
}
