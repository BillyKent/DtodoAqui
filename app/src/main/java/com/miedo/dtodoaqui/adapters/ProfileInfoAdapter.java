package com.miedo.dtodoaqui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.miedo.dtodoaqui.R;

import java.util.ArrayList;

public class ProfileInfoAdapter extends ArrayAdapter<ProfileInfoAdapter.ProfileItem> {

    private final Context mContext;
    ArrayList<ProfileItem> mData;

    public ProfileInfoAdapter(Context mContext, ArrayList<ProfileItem> mData) {
        super(mContext, R.layout.cardview_profile_info, mData);
        this.mContext = mContext;
        this.mData = mData;
    }

    public void setmData(ArrayList<ProfileItem> mData) {
        this.mData = mData;
    }

    private int lastPosition = -1;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ProfileItem item = getItem(position);

        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.cardview_profile_info, parent, false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.infoTypeTextView);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.infoTypeImageView);
            viewHolder.content = (TextView) convertView.findViewById(R.id.contentInfoTextView);

            result = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        //Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        //result.startAnimation(animation);
        lastPosition = position;

        viewHolder.title.setText(item.getTitle());
        viewHolder.content.setText(item.getContent());
        viewHolder.icon.setImageResource(item.getResIcon());
        viewHolder.icon.setTag(position);

        return convertView;
    }

    private static final class ViewHolder {
        ImageView icon;
        TextView title;
        TextView content;

    }

    public static class ProfileItem {
        private int resIcon;
        private String title;
        private String content;

        public ProfileItem(int resIcon, String title, String content) {
            this.resIcon = resIcon;
            this.title = title;
            this.content = content;
        }

        public int getResIcon() {
            return resIcon;
        }

        public void setResIcon(int resIcon) {
            this.resIcon = resIcon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
