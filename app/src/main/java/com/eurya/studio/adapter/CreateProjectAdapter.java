package com.eurya.studio.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.view.LayoutInflaterCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.eurya.studio.R;
import com.eurya.studio.holder.CreateProjectHolder;
import java.util.List;

public class CreateProjectAdapter extends RecyclerView.Adapter<CreateProjectHolder> {
    
    private Context mContext;
    private List<String> mList;
    private CreateProjectHolder mHolder;
    
    public CreateProjectAdapter(Context context, List<String> list){
        mContext = context;
        mList = list;
    }
    
  @Override
  public CreateProjectHolder onCreateViewHolder(ViewGroup group, int postion) {
    View view = LayoutInflater.from(mContext).inflate(R.layout.layout_create_project_holder, group, false);
    mHolder = new CreateProjectHolder(view);
    return mHolder;
  }

  @Override
  public void onBindViewHolder(CreateProjectHolder holder, int postion) {
      String type = mList.get(postion);
      switch(type){
          case "Maven"-> drawable(R.drawable.ic_language_java);
          case "Android"-> drawable(R.drawable.ic_language_android);
          case "Gradle"-> drawable(R.drawable.ic_language_gradle);
      }
      holder.text.setText(type);
  }

  @Override
  public int getItemCount() {
    return mList.size();
  }
  
  private void drawable(int id){
      Drawable drawable = mContext.getDrawable(id);
      mHolder.image.setImageDrawable(drawable);
  }
}
