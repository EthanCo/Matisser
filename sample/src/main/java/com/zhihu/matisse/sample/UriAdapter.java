package com.zhihu.matisse.sample;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * UriAdapter
 */
public class UriAdapter extends RecyclerView.Adapter<UriAdapter.UriViewHolder> {

    private List<Uri> mUris;
    private List<String> mPaths;

    void setData(List<Uri> uris, List<String> paths) {
        mUris = uris;
        mPaths = paths;
        notifyDataSetChanged();
    }

    @Override
    public UriViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UriViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.uri_item, parent, false));
    }

    @Override
    public void onBindViewHolder(UriViewHolder holder, int position) {
        holder.mUri.setText(mUris.get(position).toString());
        holder.mPath.setText(mPaths.get(position));

        holder.mUri.setAlpha(position % 2 == 0 ? 1.0f : 0.54f);
        holder.mPath.setAlpha(position % 2 == 0 ? 1.0f : 0.54f);
    }

    @Override
    public int getItemCount() {
        return mUris == null ? 0 : mUris.size();
    }

    static class UriViewHolder extends RecyclerView.ViewHolder {

        private TextView mUri;
        private TextView mPath;

        UriViewHolder(View contentView) {
            super(contentView);
            mUri = (TextView) contentView.findViewById(R.id.uri);
            mPath = (TextView) contentView.findViewById(R.id.path);
        }
    }
}
