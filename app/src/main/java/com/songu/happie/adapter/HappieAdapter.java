package com.songu.happie.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.songu.happie.R;
import com.songu.happie.databinding.HappyCellBinding;
import com.songu.happie.model.HappieModel;

import java.util.List;

/**
 * Created by sudeep on 21-Jul-17.
 */
public class HappieAdapter extends RecyclerView.Adapter<HappieAdapter.ViewHolder> {


    private final Context mContext;
    private List<HappieModel> mHappieModelList;
    private OnItemClickListener onItemClickListener;

    public HappieAdapter(Context mContext, List<HappieModel> mHappieModelList) {
        this.mHappieModelList = mHappieModelList;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final HappyCellBinding binding = DataBindingUtil.inflate(inflater, R.layout.happy_cell, parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final HappieModel mHappieModel = this.mHappieModelList.get(position);
        holder.bind(mHappieModel);
    }

    @Override
    public int getItemCount() {
        return mHappieModelList.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public List<HappieModel> getHappieModel() {
        return mHappieModelList;
    }

    public void addChatMassgeModel(HappieModel mHappieModel) {
        try {
            this.mHappieModelList.add(mHappieModel);
            notifyItemInserted(getItemCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setHappieModelList(List<HappieModel> mHappieModelList) {
        this.mHappieModelList = mHappieModelList;
        notifyDataSetChanged();
    }

    private void dataSetChanged() {
        this.notifyDataSetChanged();

    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final HappyCellBinding binding;

        public ViewHolder(final View view, final HappyCellBinding binding) {
            super(view);
            this.binding = binding;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, getAdapterPosition());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @UiThread
        public void bind(final HappieModel mHappieModel) {
            this.binding.setHappie(mHappieModel);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
}