package top.golvaje.me.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import top.golvaje.me.R;
import top.golvaje.me.activity.JokeCommentActivity;
import top.golvaje.me.doc.Config;
import top.golvaje.me.model.HappieModel;
import top.golvaje.me.util.Utility;
import top.golvaje.me.view.OvalImageView;

import static top.golvaje.me.R.id.jokes_list_image;


/**
 * Created by Administrator on 7/14/2017.
 */

public class AdapterHome extends RecyclerView.Adapter<AdapterHome.ViewHolder> {


    List<HappieModel> m_lstItems;
    int jokeType;
    int jokeCategory;


    public AdapterHome(List<HappieModel> tickets, int jokeCategory,int joketype) {
        m_lstItems = tickets;
        this.jokeType = joketype;
        this.jokeCategory = jokeCategory;
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView imgJoke;
        public TextView txtJokesShares;
        public TextView txtJokesView;
        public OvalImageView mUserPhoto;
        public TextView mUserName;
        public TextView mLang;
        public TextView mTime;
        public TextView mDescription;
        public ImageView mPhoto;
        public ImageView imgComment;
        public TextView txtJokesUp;
        public TextView txtJokeTitle;
        public TextView txtJokesDown;
        public TextView txtJokesComments;
        public LinearLayout descriptionLayout;
        public RelativeLayout imageVideoAdsLayout;
        public Context mContext;

        public ViewHolder(View itemView) {
            super(itemView);
            mUserPhoto = (OvalImageView) itemView.findViewById(R.id.jokes_user_profile);
            mPhoto = (ImageView) itemView.findViewById(jokes_list_image);
            mUserName = (TextView) itemView.findViewById(R.id.jokes_username);
            mLang = (TextView) itemView.findViewById(R.id.lang);
            mTime = (TextView) itemView.findViewById(R.id.time);
            txtJokesUp = (TextView) itemView.findViewById(R.id.jokes_smiles);
            txtJokesDown = (TextView) itemView.findViewById(R.id.jokes_frowns);
            txtJokesComments = (TextView) itemView.findViewById(R.id.jokes_comments);
            txtJokesView = (TextView) itemView.findViewById(R.id.jokes_view);
            txtJokesShares = (TextView) itemView.findViewById(R.id.jokes_share);
            mDescription = (TextView) itemView.findViewById(R.id.jokes_description);
            descriptionLayout=(LinearLayout)itemView.findViewById(R.id.description_block);
            imageVideoAdsLayout=(RelativeLayout)itemView.findViewById(R.id.image_ads_video_layout);
            imgComment = (ImageView) itemView.findViewById(R.id.comment_icon);
            txtJokeTitle = (TextView) itemView.findViewById(R.id.jokes_list_text);
            imgComment.setOnClickListener(this);

            mContext = itemView.getContext();
        }

        public void bindPhoto(HappieModel hModel) {


            Picasso.with(mContext)
                    .load(Config.mImageBaseUrl + hModel.user_avatar)
                    .placeholder(R.drawable.male).error(R.drawable.male).into(mUserPhoto);

            mUserName.setText(hModel.mUserName);
            txtJokesUp.setText(hModel.mUpVotes);
            txtJokesDown.setText(hModel.mDownVotes);
            txtJokesComments.setText(hModel.mComments);
            txtJokesView.setText(hModel.mViews);
            txtJokesShares.setText(hModel.mShares);
//            mPhoto.setVisibility(View.VISIBLE);
            mTime.setText(Utility.getFormatedJokeDate(hModel.mCreateAt));

            switch (jokeType) {
                case 0:
                    descriptionLayout.setVisibility(View.GONE);
                    imageVideoAdsLayout.setVisibility(View.VISIBLE);
                    txtJokeTitle.setText(hModel.mTitle);
                    Picasso.with(mContext)
                            .load(Config.mImageBaseUrl + hModel.mPhoto)
                            .placeholder(R.drawable.image_loading).error(R.drawable.promt_screens_listing).into(mPhoto);
                    break;
                case 1:
                    descriptionLayout.setVisibility(View.VISIBLE);
                    mDescription.setText(hModel.mContent);
                    imageVideoAdsLayout.setVisibility(View.GONE);
                    break;
                case 2:
                    descriptionLayout.setVisibility(View.GONE);
                    imageVideoAdsLayout.setVisibility(View.VISIBLE);
                    txtJokeTitle.setText(hModel.mTitle);
                    Picasso.with(mContext)
                            .load(Config.mImageBaseUrl + hModel.mPhoto)
                            .placeholder(R.drawable.image_loading).error(R.drawable.promt_screens_listing).into(mPhoto);


                    break;
                case 3:
                    break;
            }


            /*Picasso.with(mContext)
                    .load(url)
                    .into(mPhoto);*/

        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.comment_icon:
                    Intent m = new Intent(v.getContext(), JokeCommentActivity.class);
                    v.getContext().startActivity(m);
                    break;
            }
        }
    }


}
