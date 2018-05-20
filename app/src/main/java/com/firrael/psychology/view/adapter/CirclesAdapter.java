package com.firrael.psychology.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.firrael.psychology.R;
import com.firrael.psychology.model.Circle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Railag on 17.03.2017.
 */

public class CirclesAdapter extends RecyclerView.Adapter<CirclesAdapter.ViewHolder> {

    private List<Circle> circles = new ArrayList<>();

    public void setCircles(List<Circle> circles) {
        this.circles = circles;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_circle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Circle circle = circles.get(position);

        switch (circle) {

            case TOP_RIGHT:
                holder.circle.setRotation(180);
                break;
            case TOP_LEFT:
                holder.circle.setRotation(90);
                break;
            case DOWN_RIGHT:
                holder.circle.setRotation(270);
                break;
            case DOWN_LEFT:
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return circles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.circle)
        ImageView circle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
