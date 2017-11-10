package com.hooooong.pholar.view.list.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.Log;
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
import com.hooooong.pholar.model.Const;
import com.hooooong.pholar.model.Photo;
import com.hooooong.pholar.model.Post;
import com.hooooong.pholar.model.User;
import com.hooooong.pholar.util.DateUtil;
import com.hooooong.pholar.view.comment.CommentActivity;
import com.hooooong.pholar.view.custom.MoreTextView;
import com.hooooong.pholar.view.home.DetailActivity;
import com.matthewtamlin.sliding_intro_screen_library.DotIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Heepie on 2017. 11. 7..
 */

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private Context context;
    private List<Post> postList;

    public PostAdapter(Context context) {
        this.context = context;
    }

    public void setDataAndRefresh(List<Post> data) {
        this.postList = data;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.each_write, parent, false);
            //inflate your layout and pass it to view holder
            return new VHItem(view);
        } else if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.each_new_post, parent, false);
            return new VHHeader(view);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof VHItem) {
            Post post = getItem(position);
            ((VHItem)viewHolder).setPostId(post.post_id);
            ((VHItem)viewHolder).setTextContent(post.post_content);
            ((VHItem)viewHolder).setTextPostDate(post.date);
            ((VHItem)viewHolder).setUser(post.user);
            ((VHItem)viewHolder).setLike(post.like);
            ((VHItem)viewHolder).setComment(post.getComment());
            ((VHItem)viewHolder).setViewPager(post.getPhoto());
            //cast holder to VHItem and set data
        } else if (viewHolder instanceof VHHeader) {
            //cast holder to VHHeader and set data for header.
            ((VHHeader)viewHolder).setNewPostAdapter(postList);
        }
    }

    @Override
    public int getItemCount() {
        if (postList == null)
            return 0;
        return postList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private Post getItem(int position) {
        return postList.get(position - 1);
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    class VHHeader extends RecyclerView.ViewHolder{

        private ViewPager newPostViewPager;
        private NewPostAdapter newPostAdapter;

        public VHHeader(View itemView) {
            super(itemView);
            newPostViewPager = itemView.findViewById(R.id.newWriteViewPager);
            newPostViewPager.setClipToPadding(false);
            newPostViewPager.setPadding(200, 40, 200, 0);
            newPostViewPager.setPageMargin(100);

        }

        public void setNewPostAdapter(List<Post> data) {
            newPostAdapter = new NewPostAdapter(context, data);
            newPostViewPager.setAdapter(newPostAdapter);
            newPostViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    if (position < Const.NEW_PHOTO_COUNT)        //1번째 아이템에서 마지막 아이템으로 이동하면
                        newPostViewPager.setCurrentItem(position + Const.NEW_PHOTO_COUNT, false); //이동 애니메이션을 제거 해야 한다
                    else if (position >= Const.NEW_PHOTO_COUNT * 2)     //마지막 아이템에서 1번째 아이템으로 이동하면
                        newPostViewPager.setCurrentItem(position - Const.NEW_PHOTO_COUNT, false);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            newPostViewPager.setCurrentItem(Const.NEW_PHOTO_COUNT);
        }
    }

    class VHItem extends RecyclerView.ViewHolder implements View.OnClickListener {

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
        private List<Comment> commentList;

        private ViewPager viewPager;
        private DotIndicator indicator;


        public VHItem(final View itemView) {
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
            viewPager.setOnClickListener(this);
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
            this.commentList = commentList;
            if (commentList == null || commentList.size() == 0) {
                textCommentCount.setVisibility(View.INVISIBLE);
            } else {
                textCommentCount.setVisibility(View.VISIBLE);
                textCommentCount.setText(commentList.size()+"");
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
                indicator.setSelectedItem(0 ,true);
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

                    Intent commentIntent = new Intent(context, CommentActivity.class);
                    commentIntent.putParcelableArrayListExtra("commentList", new ArrayList<Comment>(commentList));
                    commentIntent.putExtra("post_id", post_id);
                    context.startActivity(commentIntent);

                    break;
                case R.id.imgShare:
                    Intent shareIntent = new Intent(context, DetailActivity.class);
                    shareIntent.putExtra("post_id", post_id);
                    context.startActivity(shareIntent);
                    break;
            }
        }
    }

}

