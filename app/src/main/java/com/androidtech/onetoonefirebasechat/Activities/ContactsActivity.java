package com.androidtech.onetoonefirebasechat.Activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.androidtech.instar.Adapters.InboxAdapter;
import com.androidtech.instar.Models.User;
import com.androidtech.instar.R;
import com.androidtech.instar.Utils.FirebaseUtil;
import com.androidtech.instar.Utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,SearchView.OnQueryTextListener{

    //views section
    @BindView(android.R.id.content)
    View mRootView;
    @BindView(R.id.contacts_recycler_view)
    RecyclerView mContactsRecycler;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    //variables section
    public static String TAG = "MessageContacts";
    private InboxAdapter mInboxAdapter;
    private List<User> feedResponse;
    private Dialog mProgressDialog;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        ButterKnife.bind(this);
        //change toolbar style
        customizeToolbar();

        //intaite Custom dialog
        customDialog();

        //implement pull to refresh
        pullToRefresh();

        //setup feed
        setupFeed();

        getFeedsFromServer();
    }

    private void customDialog() {
        mProgressDialog = new Dialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mProgressDialog.setContentView(R.layout.custom_progress_dialog);
    }

    @Override
    public void onRefresh() {
        getFeedsFromServer();
    }

    /**
     * implement pull to refresh functionality
     */
    private void pullToRefresh(){
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void getFeedsFromServer(){

        mRefreshLayout.setRefreshing(true);
        //check internet first
        if(Utils.checkInternet(this)){
            //load data from firebase of districts
            FirebaseUtil.getUsersRef().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot!=null){
                        mRefreshLayout.setRefreshing(false);
                        feedResponse = new ArrayList<User>();
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            User user = postSnapshot.getValue(User.class); // get the user
                            if(!user.getUser_id().equals(FirebaseUtil.getCurrentUserId()))
                            feedResponse.add(user);
                        }
                        mInboxAdapter.addAll(feedResponse);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                    //stop progress and show error
                    mRefreshLayout.setRefreshing(false);

                    showSnackbar(R.string.error_occured);
                }
            });
        }else{
            //stop progress and show error
            mRefreshLayout.setRefreshing(false);

            showSnackbar(R.string.error_occured);
        }
    }

    private void setupFeed() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        mContactsRecycler.setLayoutManager(linearLayoutManager);

        feedResponse = new ArrayList<>();
        mInboxAdapter = new InboxAdapter(this,feedResponse);
        mContactsRecycler.setAdapter(mInboxAdapter);
    }

    /**
     * change toolbar style
     */
    private void customizeToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar supportedToolbar = getSupportActionBar();
        supportedToolbar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contacts, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        final List<User> filteredModelList = filter(query);
        mInboxAdapter.addAll(filteredModelList);
        mContactsRecycler.scrollToPosition(0);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        final List<User> filteredModelList = filter(query);
        mInboxAdapter.addAll(filteredModelList);
        mContactsRecycler.scrollToPosition(0);
        return false;
    }

    private List<User> filter(String query) {
        query = query.toLowerCase();
        final List<User> filteredModelList = new ArrayList<>();
        for (User model : feedResponse) {
            final String text = model.getFull_name().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }


    @MainThread
    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }
}
