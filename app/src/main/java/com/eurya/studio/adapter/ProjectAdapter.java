package com.eurya.studio.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eurya.studio.adapter.ProjectAdapter;
//import com.eurya.studio.drawable.StudioDrawable;
import com.eurya.studio.holder.ProjectHolder;
import com.eurya.studio.R;

import com.eurya.studio.utils.StudioUtil;
import java.io.File;
import java.util.List;

/**
 * @autuor Eurya QiuZhu
 * @date 2023.1.16
 * @emali 2644635373@qq.com
 */
public class ProjectAdapter extends RecyclerView.Adapter<ProjectHolder> {

  private Context mContext;
  private List<File> mList;

  public ProjectAdapter(Context context, List<File> list) {
    mContext = context;
    mList = list;
  }

  @NonNull
  @Override
  public ProjectHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
    View view =
        LayoutInflater.from(mContext).inflate(R.layout.layout_project_holder, viewGroup, false);
    return new ProjectHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ProjectHolder holder, int position) {
    File file = mList.get(position);
    String file_name = file.getName();
    holder.itemView.setOnClickListener(
        v -> Toast.makeText(mContext, String.valueOf(position), Toast.LENGTH_SHORT).show());
    holder.item_image.setImageResource(R.drawable.ic_language_java);
    holder.item_name.setText(file_name);
    holder.item_type.setText("null");
  }

  @Override
  public int getItemCount() {
    return mList.size();
  }

}
