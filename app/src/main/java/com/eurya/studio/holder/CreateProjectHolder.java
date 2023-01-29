package com.eurya.studio.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.eurya.studio.R;

public class CreateProjectHolder extends RecyclerView.ViewHolder {

  public ImageView image;
  public TextView text;

  public CreateProjectHolder(View item) {
    super(item);
    image = item.findViewById(R.id.creat_image);
    text = item.findViewById(R.id.creat_text);
  }
}
