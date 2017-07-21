package com.songu.happie.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.songu.happie.R;
import com.songu.happie.activity.JokeCommentActivity;
import com.songu.happie.doc.Config;
import com.songu.happie.model.HappieModel;
import com.songu.happie.view.OvalImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 7/14/2017.
 */

public class AdapterHome extends RecyclerView.Adapter<AdapterHome.ViewHolder> {


    List<HappieModel> m_lstItems;


    public AdapterHome(List<HappieModel> tickets) {
        m_lstItems = tickets;
    }
    @Override
    public AdapterHome.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_page_adapter_recy, parent, false);
        return new ViewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HappieModel hModel = m_lstItems.get(position);
        holder.bindPhoto(hModel);
    }
        @Override
    public int getItemCount() {
        return m_lstItems.size();
    }
    public void update(List<HappieModel> tickets) {
        this.m_lstItems = tickets;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        public OvalImageView mUserPhoto;
        public TextView mUserName;
        public TextView mLang;
        public TextView mTime;
        public TextView mDescription;
        public ImageView mPhoto;
        public ImageView imgComment;
        public TextView txtJokesUp;
        public TextView txtJokesDown;
        public TextView txtJokesComments;
        public Context mContext;

        public ViewHolder(View itemView) {
            super(itemView);
            mUserPhoto = (OvalImageView) itemView.findViewById(R.id.jokes_user_profile);
            mPhoto = (ImageView) itemView.findViewById(R.id.jokes_list_image);
            mUserName = (TextView) itemView.findViewById(R.id.jokes_username);
            mLang = (TextView) itemView.findViewById(R.id.lang);
            mTime = (TextView) itemView.findViewById(R.id.time);
            txtJokesUp = (TextView) itemView.findViewById(R.id.jokes_smiles);
            txtJokesDown = (TextView) itemView.findViewById(R.id.jokes_frowns);
            txtJokesComments = (TextView) itemView.findViewById(R.id.jokes_comments);
            mDescription = (TextView) itemView.findViewById(R.id.jokes_description);

            imgComment = (ImageView)itemView.findViewById(R.id.comment_icon);
            imgComment.setOnClickListener(this);

            mContext = itemView.getContext();
        }

        public void bindPhoto(HappieModel hModel) {
            mDescription.setText(hModel.mContent);
            mUserName.setText(hModel.mUserName);
            txtJokesUp.setText(hModel.mUpVotes);
            txtJokesDown.setText(hModel.mDownVotes);
            txtJokesComments.setText(hModel.mComments);

            String url = Config.mImageBaseUrl + hModel.mPhoto;
            mPhoto.setVisibility(View.VISIBLE);
//            mTime.setText(Utils.getElapsedTime(hModel.mCreateAt));

            Picasso.with(mContext)
                    .load(url)
                    .into(mPhoto);

        }


        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.comment_icon:
                    Intent m = new Intent(v.getContext(), JokeCommentActivity.class);
                    v.getContext().startActivity(m);
                    break;
            }
        }
    }
}
