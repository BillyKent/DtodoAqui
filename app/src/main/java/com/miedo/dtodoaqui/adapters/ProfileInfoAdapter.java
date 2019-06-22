package com.miedo.dtodoaqui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.miedo.dtodoaqui.R;

import java.util.ArrayList;

public class ProfileInfoAdapter extends ArrayAdapter<ProfileInfoAdapter.ProfileItem> {

    private final Context mContext;
    ArrayList<ProfileInfoAdapter.ProfileItem> mData;

    public ProfileInfoAdapter(Context mContext, ArrayList<ProfileInfoAdapter.ProfileItem> mData) {
        super(mContext, R.layout.profile_item, mData);
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ProfileInfoAdapter.ProfileItem item = getItem(position);

        ProfileInfoAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ProfileInfoAdapter.ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.profile_item, parent, false);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.profileItemImageView);
            viewHolder.content = (TextView) convertView.findViewById(R.id.contentItemTextView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ProfileInfoAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.content.setText(item.getContent());
        viewHolder.icon.setImageResource(item.getResIcon());
        viewHolder.icon.setTag(position);

        return convertView;
    }

    private static final class ViewHolder {
        ImageView icon;
        TextView content;

    }

    public static class ProfileItem {
        private int resIcon;
        private String content;

        public ProfileItem(int resIcon, String content) {
            this.resIcon = resIcon;
            this.content = content;
        }

        public int getResIcon() {
            return resIcon;
        }

        public void setResIcon(int resIcon) {
            this.resIcon = resIcon;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
