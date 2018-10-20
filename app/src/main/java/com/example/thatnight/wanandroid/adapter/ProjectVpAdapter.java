package com.example.thatnight.wanandroid.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.thatnight.wanandroid.R;
import com.example.thatnight.wanandroid.entity.ProjectItem;

import java.util.List;
import java.util.regex.Pattern;

/**
 * date: 2018/8/2
 * author: thatnight
 */
public class ProjectVpAdapter extends PagerAdapter {

    private List<ProjectItem> mProjects;
    private OnItemClickListener mOnIbtnClickListener;

    private SparseBooleanArray mSelectArray = new SparseBooleanArray();

    public ProjectVpAdapter(List<ProjectItem> projects) {
        mProjects = projects;
    }

    @Override
    public int getCount() {
        return mProjects.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View itemView = LayoutInflater.from(container.getContext()).inflate(R.layout.item_project_viewpager, null);
        TextView title = itemView.findViewById(R.id.item_tv_title);
        TextView desc= itemView.findViewById(R.id.item_tv_desc);

        TextView author = itemView.findViewById(R.id.item_tv_author);
        TextView time = itemView.findViewById(R.id.item_tv_time);
        ImageButton ibLike = itemView.findViewById(R.id.item_ib_like);
        RelativeLayout item = itemView.findViewById(R.id.rl_item_news);
        ImageView imageView = itemView.findViewById(R.id.iv_project);

        ProjectItem projectItem = mProjects.get(position);
        boolean isHtml = Pattern.matches(".*<em.+?>(.+?)</em>.*", projectItem.getTitle());
        if (isHtml) {
            String textColor = "#FF4081";
            String newTitle = projectItem.getTitle().replaceAll("<em.+?>", "<font color=\"" + textColor + "\">").replaceAll("</em>", "</font>");
            title.setText(Html.fromHtml(newTitle));
        } else {
            title.setText(projectItem.getTitle());
        }

        //项目图
        Glide.with(imageView).load(projectItem.getEnvelopePic()).into(imageView);

        desc.setText(projectItem.getDesc());
        author.setText(projectItem.getAuthor());
        time.setText(projectItem.getNiceDate());
        ibLike.setTag(position);
        author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnIbtnClickListener != null) {
                    mOnIbtnClickListener.onAuthorClick(v, position);
                }
            }
        });

        if (projectItem.isCollect()) {
            ibLike.setSelected(true);
            mSelectArray.put(position, true);
        } else {
            ibLike.setSelected(false);
            mSelectArray.put(position, false);
        }

        ibLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    mSelectArray.put(position, false);
                } else {
                    mSelectArray.put(position, true);
                }
                if (mOnIbtnClickListener != null) {
                    mOnIbtnClickListener.onIbtnClick(v, position);
                }
            }
        });
        if(mOnIbtnClickListener!=null){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnIbtnClickListener.onItemClick(v,position);
                }
            });
        }
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public void setOnIbtnClickListener(ProjectVpAdapter.OnItemClickListener onIbtnClickListener) {
        mOnIbtnClickListener = onIbtnClickListener;
    }

    public interface OnItemClickListener {
        void onIbtnClick(View v, int position);

        void onAuthorClick(View v, int position);

        void onItemClick(View v , int position);
    }

    public void setProjects(List<ProjectItem> projects) {
        mProjects = projects;
    }
}
