package com.firrael.psychology.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.firrael.psychology.R;
import com.firrael.psychology.model.Sign;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by railag on 21.05.2018.
 */
public class SignsAdapter extends RecyclerView.Adapter<SignsAdapter.ViewHolder> {

    public interface OnSignClickListener {
        void onSignSelected(Sign sign);
    }

    private List<Sign> signs = new ArrayList<>();
    private OnSignClickListener listener;

    public void setSigns(List<Sign> signs, OnSignClickListener listener) {
        this.signs = signs;
        this.listener = listener;
        notifyDataSetChanged();
    }

    @Override
    public SignsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_sign, parent, false);
        return new SignsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SignsAdapter.ViewHolder holder, int position) {
        Sign sign = signs.get(position);

        holder.sign.setImageResource(sign.getDrawableId());
        holder.sign.setOnClickListener(v -> listener.onSignSelected(sign));

        if (holder.sign.isSelected()) {
            holder.sign.setBackgroundResource(R.drawable.outline);
        } else {
            holder.sign.setBackground(null);
        }
    }

    @Override
    public int getItemCount() {
        return signs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.sign)
        ImageView sign;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}