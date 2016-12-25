package com.androidtech.onetoonefirebasechat.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidtech.instar.Activities.ProfileActivity;
import com.androidtech.instar.Models.User;
import com.androidtech.instar.R;
import com.androidtech.instar.Utils.GlideUtil;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.androidtech.instar.Activities.ConversionActivity.USER_EXTRA;

public class UsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG = "UsersAdapter";
    private Context context;
    private List<User> userItems;

    public UsersAdapter(Context context , List<User> userItems) {
        this.context = context;
        this.userItems = userItems;
    }

    public List<User> getUserItems() {
        return userItems;
    }

    class CardViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.user_feed_item_image)
        ImageView userImage;

        @BindView(R.id.user_feed_item_name)
        TextView userFullName;

        @BindView(R.id.user_feed_item_location)
        TextView userLocation;

        @BindView(R.id.user_item)
        CardView userItem;

        public CardViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.user_feed_item,parent, false);
        RecyclerView.ViewHolder holder = null;
            holder = new CardViewHolder(view);
            final CardViewHolder cardHolder = (CardViewHolder)holder;

            //set click listener on card profile
            cardHolder.userItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    User user =userItems.get(cardHolder.getAdapterPosition());
                    Parcelable wrapped = Parcels.wrap(user);
                    Intent intent= new Intent(context, ProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(USER_EXTRA, wrapped);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });


        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous
        return 1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final CardViewHolder cardViewHolder = (CardViewHolder) holder;

        User user = userItems.get(cardViewHolder.getAdapterPosition());

            if(user.getProfile_picture_thumbnail()!=null)
                GlideUtil.loadImage(user.getProfile_picture_thumbnail(),cardViewHolder.userImage);

            if(user.getFull_name()!=null)
                cardViewHolder.userFullName.setText(user.getFull_name());
            if(user.getLocation()!=null)
                cardViewHolder.userLocation.setText(user.getLocation());
             else
                cardViewHolder.userLocation.setVisibility(View.INVISIBLE);

    }

    @Override
    public int getItemCount() {
            return userItems.size();
    }

    public void addAll(List<User> itemList){
        userItems.clear();
        userItems.addAll(itemList);
        notifyDataSetChanged();
    }

    public void addUser(User user){
        userItems.add(user);
        notifyDataSetChanged();
    }
}