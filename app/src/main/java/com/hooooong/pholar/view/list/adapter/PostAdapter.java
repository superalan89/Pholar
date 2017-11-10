package com.hooooong.pholar.view.list.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.hooooong.pholar.R;
import com.hooooong.pholar.dao.PostDAO;
import com.hooooong.pholar.model.Comment;
import com.hooooong.pholar.model.Photo;
import com.hooooong.pholar.model.Post;
import com.hooooong.pholar.model.User;
import com.hooooong.pholar.util.DateUtil;
import com.hooooong.pholar.view.custom.MoreTextView;
import com.matthewtamlin.sliding_intro_screen_library.DotIndicator;

import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Heepie on 2017. 11. 7..
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private final String TAG = getClass().getSimpleName();

    private Context context;
    private List<Post> postList;

    public void setDataAndRefresh(List<Post> data) {
        this.postList = data;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.each_write, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Post post = postList.get(position);
        viewHolder.setPostId(post.post_id);
        viewHolder.setTextContent(post.post_content);
        viewHolder.setTextPostDate(post.date);
        viewHolder.setUser(post.user);
        viewHolder.setLike(post.like);
        viewHolder.setComment(post.getComment());
        viewHolder.setViewPager(post.getPhoto());
    }

    @Override
    public int getItemCount() {
        if (postList == null)
            return 0;
        return postList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private String post_id;
        private MoreTextView textContent;
        private TextView textPostWriter;
        private TextView textPostDate;
        private CircleImageView imgProfile;

        private LinearLayout locationLayout;
        private TextView textLocation;

        private ImageView imgFollow;
        private ImageView imgOption;
        private ImageView imgLike;
        private TextView textLikeCount;
        private ImageView imgComment;
        private TextView textCommentCount;
        private ImageView imgShare;
        private PostPhotoAdapter postPhotoAdapter;


        private ViewPager viewPager;
        private DotIndicator indicator;


        public ViewHolder(final View itemView) {
            super(itemView);
            textPostWriter = itemView.findViewById(R.id.textPostWriter);
            textPostDate = itemView.findViewById(R.id.textPostDate);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            textContent = itemView.findViewById(R.id.textContent);
            locationLayout = itemView.findViewById(R.id.locationLayout);
            textLocation = itemView.findViewById(R.id.textLocation);

            imgFollow = itemView.findViewById(R.id.imgFollow);
            imgOption = itemView.findViewById(R.id.imgOption);
            imgLike = itemView.findViewById(R.id.imgLike);
            textLikeCount = itemView.findViewById(R.id.textLikeCount);
            imgComment = itemView.findViewById(R.id.imgComment);
            textCommentCount = itemView.findViewById(R.id.textCommentCount);
            imgShare = itemView.findViewById(R.id.imgShare);

            viewPager = itemView.findViewById(R.id.viewPager);
            indicator = itemView.findViewById(R.id.indicator);

            imgFollow.setOnClickListener(this);
            imgOption.setOnClickListener(this);
            imgLike.setOnClickListener(this);
            imgComment.setOnClickListener(this);
            imgShare.setOnClickListener(this);

        }

        void setPostId(String post_id) {
            this.post_id = post_id;
        }

        void setTextPostDate(String date) {
            textPostDate.setText(DateUtil.calculateTime(date));
        }

        void setTextContent(String content) {
            if (content.length() == 0) {
                textContent.setVisibility(View.GONE);
            } else {

                textContent.setMovementMethod(LinkMovementMethod.getInstance());
                textContent.setOnTextMoreClickListener(new MoreTextView.TextMoreClickListener() {
                    @Override
                    public void onTextMoreClick(View textView, String clickedString) {
                        textContent.setIsEndEllipsable(false);
                    }
                });
                textContent.setText(content);
            }
        }

        void setUser(User user) {
            Glide.with(context).load(user.profile_path).into(imgProfile);
            textPostWriter.setText(user.nickname);
        }

        void setComment(List<Comment> commentList) {
            if (commentList == null || commentList.size() == 0) {
                textCommentCount.setVisibility(View.INVISIBLE);
            } else {
                textCommentCount.setVisibility(View.VISIBLE);
                textCommentCount.setText(commentList.size() + "");
            }
        }

        void setLike(Map<String, Boolean> likeList) {
            if (likeList == null || likeList.size() == 0) {
                // Like 가 아예 없으면

                textLikeCount.setVisibility(View.INVISIBLE);
                imgLike.setImageResource(R.drawable.ic_favorite_border);
            } else {
                // Like 가 있으면
                textLikeCount.setVisibility(View.VISIBLE);
                textLikeCount.setText(likeList.size()+ "");

                // 현재 Login 한 사람의 ID
                String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                if(likeList.containsKey(user_id)){
                    imgLike.setImageResource(R.drawable.ic_favorite);
                }else{
                    imgLike.setImageResource(R.drawable.ic_favorite_border);
                }
            }
        }

        void setViewPager(List<Photo> photoList) {
            postPhotoAdapter = new PostPhotoAdapter(context, photoList);
            viewPager.setAdapter(postPhotoAdapter);
            if (photoList.size() == 1) {
                indicator.setVisibility(View.GONE);
            } else {
                indicator.setVisibility(View.VISIBLE);
                indicator.setNumberOfItems(photoList.size());
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        indicator.setSelectedItem(viewPager.getCurrentItem(), true);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            }
        }


        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.imgFollow:
                    break;
                case R.id.imgOption:
                    break;
                case R.id.imgLike:
                    PostDAO.getInstance().onLikeClick(post_id, FirebaseAuth.getInstance().getCurrentUser());
                    break;
                case R.id.imgComment:
                    break;
                case R.id.imgShare:
                    break;
            }

        }


    }
}
