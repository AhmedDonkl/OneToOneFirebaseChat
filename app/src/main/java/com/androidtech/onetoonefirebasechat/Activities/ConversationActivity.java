package com.androidtech.onetoonefirebasechat.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidtech.onetoonefirebasechat.R;
import com.axisprosolutions.look.Fragments.UploadPhotoToFirebaseFragment;
import com.axisprosolutions.look.Utils.DocumentHelper;
import com.axisprosolutions.look.Utils.FirebaseUtil;
import com.axisprosolutions.look.Utils.PermissionUtil;
import com.axisprosolutions.look.Utils.Utils;
import com.axisprosolutions.look.Widgets.CircleTransformation;
import com.axisprosolutions.look.webserviceapi.API;
import com.axisprosolutions.look.webserviceapi.ServiceBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Adapters.ConversionAdapter;
import DataModels.Inbox;
import DataModels.Message;
import DataModels.Response;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;

public class ConversationActivity extends AppCompatActivity implements ServerManager.ServerResponseHandler,
        UploadPhotoToFirebaseFragment.TaskCallbacks, ActivityCompat.OnRequestPermissionsResultCallback{

    //views section
    @BindView(android.R.id.content)
    View mRootView;
    @BindView(R.id.message_recycler)
    RecyclerView mConversionRecycler;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.message_edit)
    EditText mMessageEditText;
    @BindView(R.id.message_send)
    RelativeLayout mSendMessage;
    @BindView(R.id.user_image_online)
    ImageView mOnlineImage;
    @BindView(R.id.chat_user_image)
    ImageView mToolbarUserImage;
    @BindView(R.id.user_name_toolbar)
    TextView mToolbarUserName;
    @BindView(R.id.online_text)
    TextView mOnlineText;

    //variables section
    public static String TAG = "ConversationActivity";
    public static String MESSAGE_TEXT_TYPE = "text";
    public static String MESSAGE_FILE_TYPE = "file";
    private static final int GET_CONV_REQUEST = 12;
    public static final String USER_EXTRA = "USER_EXTRA";
    private SharedPreferences preferences;
    List<Message> feedResponse;
    private Message message;

    ServiceBuilder Builder;

    private UploadPhotoToFirebaseFragment mTaskFragment;
    public static final String TAG_TASK_FRAGMENT = "UploadPhotoToFirebaseFragment";
    private static final int TC_PICK_IMAGE = 101;
    private static final int REQUEST_CAMERA = 800;
    String userChoosenTask=null;

    private String mCurrentUserId;
    private String mCurrentUserName;
    private Inbox mChattingUser;
    private String mCurrentProfileUrl;
    private String mCurrentType;
    private String currentUid;
    private String mAuthKey;
    ServerManager serverManager;

    ConversionAdapter mConversionAdapter;
    List<Message> mMessageResponse;
    private boolean mIsLastMessageSeen = false;
    private LinearLayoutManager mLinearLayoutManager;
    Handler mDelayHandler ;
    private Dialog mProgressDialog;

    private boolean userRegister = false;

    /**
     * Id to identify a contacts permission request.
     */
    private static final int REQUEST_STORAGE= 1;

    /**
     * Permissions required to read and write contacts. Used by the {@link }.
     */
    private static String[] PERMISSIONS_CONTACT = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_conversation);
        ButterKnife.bind(this);

        serverManager = new ServerManager(this);
        Builder = new ServiceBuilder();

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null)
            mChattingUser =  Parcels.unwrap(bundle.getParcelable(USER_EXTRA));

        //change toolbar style
        customizeToolbar();

        //intaite Custom dialog
        customDialog();

        preferences = getApplicationContext().getSharedPreferences("EkattorSchoolManagerPrefs", 0);

        mCurrentUserId =  preferences.getString("USERID", "");
        mCurrentUserName = preferences.getString("USERNAME", "");
        mCurrentProfileUrl =  preferences.getString(getString(R.string.image_url),"");
        mCurrentType = preferences.getString("LOGINTYPE", "");
        mAuthKey = preferences.getString("AUTHKEY", "");

        currentUid = mCurrentUserId + mCurrentType;

        //try to register user
        startChat();
        //serverManager.startChat(GET_CONV_REQUEST,mCurrentUserId,mCurrentType,mAuthKey,mChattingUser.uid,mChattingUser.type);

        //setup feed
        setupFeed();

        //check if user online
        checkUserOnline();

        mDelayHandler = new Handler();

        getFeedsFromServer();

        // find the retained fragment on activity restarts
        FragmentManager fm = getSupportFragmentManager();
        mTaskFragment = (UploadPhotoToFirebaseFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);

        // create the fragment and data the first time
        if (mTaskFragment == null) {
            // add the fragment
            mTaskFragment = new UploadPhotoToFirebaseFragment();
            fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
        }
    }

    //start chat
    private void startChat() {
        if (!userRegister){
            //serverManager.startChat(GET_CONV_REQUEST,);

            API.Service serviceApi =Builder.BuildLookApi();
            serviceApi.startChat(mCurrentUserId,mCurrentType,mAuthKey,mChattingUser.uid,mChattingUser.type, new Callback<Response>() {
                @Override
                public void success(Response feedResponse, retrofit.client.Response response) {
                    if (feedResponse.status == 200){
                        Log.d(TAG,"start chat Success");
                        userRegister = true;
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d(TAG,"start chat unSuccess");
                }
            });
        }
    }

    /**
     * check if current user is online or not
     */
    private void checkUserOnline() {
        String currenUid = mChattingUser.uid  + mChattingUser.type;
        FirebaseUtil.getOnlineUsersRef().child(currenUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){//user is online
                    mOnlineImage.setImageResource(R.drawable.online_dotted);
                    mOnlineText.setText(R.string.online);
                }else{//user is offline
                    mOnlineImage.setImageResource(R.drawable.offline_dotted);
                    mOnlineText.setText(R.string.offline);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,"Error getting user status of online");
            }
        });
    }

    //seen last message
    private void seenLastMessage(final Message message, String room_type) {
        mIsLastMessageSeen = true;
        if(currentUid.equals(message.getReceiverUid())){//if i'm the receiver of the message update it to be seen
            Map<String, Object> updateValues = new HashMap<>();
            updateValues.put("seen",true);

            FirebaseUtil.getChatRoomsRef().child(room_type).child(String.valueOf(message.getTimestamp())).updateChildren(
                    updateValues,
                    new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError firebaseError, DatabaseReference databaseReference) {

                            if (firebaseError != null) {
                                Log.e(TAG, "message "+message.getTimestamp()+" has error to updated to seen");

                            }else{
                                Log.e(TAG, "message "+message.getTimestamp()+" has been updated to seen");
                            }
                        }
                    });
        }
    }

    @MainThread
    private void showSnackbar(@StringRes int errorMessageRes) {
        Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG).show();
    }

    @OnClick(R.id.image_send)
    void showImagePicker() {
        Log.i(TAG, "Show camera button pressed. Checking permission.");
        // Check if the Storage permission is already available.
        // Verify that all required contact permissions have been granted.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // STORAGE permission has not been granted.
            requestStoragePermission();

        } else {
            galleryIntent();
        }

    }

    void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, getString(R.string.picture_chooser_title)),TC_PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TC_PICK_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getData() != null) {
                    Uri fileUri = data.getData();
                    String filePath = DocumentHelper.getPath(this, fileUri);
                    //Safety check to prevent null pointer exception
                    if (filePath == null || filePath.isEmpty()) {
                        showToast(R.string.error_message);
                        return;
                    }
                    mProgressDialog.show();
                    uploadImageFirebase(filePath);
                }
            }
        }
    }

    private void showToast(@StringRes int errorMessageRes) {
        Toast.makeText(this, errorMessageRes, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.message_send)
    void sendMessage(){

        //try to register user
        startChat();
       // serverManager.startChat(GET_CONV_REQUEST,mCurrentUserId,mCurrentType,mAuthKey,mChattingUser.uid,mChattingUser.type);

        String messageStr = mMessageEditText.getText().toString();
        if(messageStr.isEmpty()){
            mSendMessage.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake_error));
            return;
        }

        Message message = new Message(currentUid,
                mChattingUser.uid + mChattingUser.type ,
                messageStr,MESSAGE_TEXT_TYPE,System.currentTimeMillis(),
                false);

        //send message
        sendMessageToFirebaseUser(message);
    }

    private void customDialog() {
        mProgressDialog = new Dialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mProgressDialog.setContentView(R.layout.custom_progress_dialog);
    }

    private void getFeedsFromServer(){

        mProgressDialog.show();

        //check internet first
        if(Utils.checkInternet(this)){
//            if (!userRegister)
//                //try to register user
                   startChat();
//                serverManager.startChat(GET_CONV_REQUEST,mCurrentUserId,mCurrentType,mAuthKey,mChattingUser.uid,mChattingUser.type);

            //load messages from firebase
            getMessageFromFirebaseUser(currentUid,mChattingUser.uid + mChattingUser.type);
        }else{
            //stop progress and show error
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            showSnackbar(R.string.error_message);
        }
    }

    /**
     * get messages between users from firebase
     * @param senderUid
     * @param receiverUid
     */
    private void getMessageFromFirebaseUser(String senderUid, String receiverUid) {
        final String room_type_1 = senderUid + "_" + receiverUid;
        final String room_type_2 = receiverUid + "_" + senderUid;

        FirebaseUtil.getChatRoomsRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                if (dataSnapshot.hasChild(room_type_1)) {
                    Log.e(TAG, "getMessageFromFirebaseUser: " + room_type_1 + " exists");

                    FirebaseUtil.getChatRoomsRef().child(room_type_1).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot!=null){
                                mMessageResponse = new ArrayList<Message>();
                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                    Message message = postSnapshot.getValue(Message.class);
                                    mMessageResponse.add(message);
                                }
                                mConversionAdapter.addAll(mMessageResponse);

                                mDelayHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {//try to smooth scroll to end of recycler after delay
                                        try {
                                            //scroll to last position
                                            mConversionRecycler.smoothScrollBy(0, mConversionRecycler.getChildAt(0).getHeight() * mConversionAdapter.getItemCount());

                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }

                                    }
                                }, 100);

                                if(!mIsLastMessageSeen)
                                    seenLastMessage(mMessageResponse.get(mMessageResponse.size() - 1),room_type_1);

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            showSnackbar(R.string.error_message);
                        }
                    });
                } else if (dataSnapshot.hasChild(room_type_2)) {
                    Log.e(TAG, "getMessageFromFirebaseUser: " + room_type_2 + " exists");
                    FirebaseUtil.getChatRoomsRef().child(room_type_2).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot!=null){
                                mMessageResponse = new ArrayList<Message>();
                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                    Message message = postSnapshot.getValue(Message.class);
                                    mMessageResponse.add(message);
                                }
                                mConversionAdapter.addAll(mMessageResponse);

                                mDelayHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {//try to smooth scroll to end of recycler after delay
                                        try {
                                            //scroll to last position
                                            mConversionRecycler.smoothScrollBy(0, mConversionRecycler.getChildAt(0).getHeight() * mConversionAdapter.getItemCount());

                                        }catch (Exception e){
                                            e.printStackTrace();
                                        }

                                    }
                                }, 100);

                                if(!mIsLastMessageSeen)
                                    seenLastMessage(mMessageResponse.get(mMessageResponse.size() - 1),room_type_2);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            showSnackbar(R.string.error_message);
                        }
                    });
                } else {
                    Log.e(TAG, "getMessageFromFirebaseUser: no such room available");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                showSnackbar(R.string.error_message);
            }
        });
    }

    private void setupFeed() {
        mLinearLayoutManager = new LinearLayoutManager(this) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };

        mLinearLayoutManager.setStackFromEnd(true);
        mConversionRecycler.setLayoutManager(mLinearLayoutManager);

        mConversionRecycler.setOverScrollMode(View.OVER_SCROLL_NEVER);

        mMessageResponse = new ArrayList<>();
        mConversionAdapter = new ConversionAdapter(this,mMessageResponse,mChattingUser);
        mConversionRecycler.setAdapter(mConversionAdapter);
    }

    public void sendMessageToFirebaseUser(final Message message) {
        mSendMessage.setEnabled(false);
        final String room_type_1 = message.getSenderUid() + "_" + message.getReceiverUid();
        final String room_type_2 = message.getReceiverUid() + "_" + message.getSenderUid();

        FirebaseUtil.getChatRoomsRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(room_type_1)) {
                    Log.e(TAG, "sendMessageToFirebaseUser: " + room_type_1 + " exists");
                    FirebaseUtil.getChatRoomsRef().child(room_type_1).child(String.valueOf(message.getTimestamp())).setValue(message);
                } else if (dataSnapshot.hasChild(room_type_2)) {
                    Log.e(TAG, "sendMessageToFirebaseUser: " + room_type_2 + " exists");
                    FirebaseUtil.getChatRoomsRef().child(room_type_2).child(String.valueOf(message.getTimestamp())).setValue(message);
                } else {
                    Log.e(TAG, "sendMessageToFirebaseUser: success");
                    FirebaseUtil.getChatRoomsRef().child(room_type_1).child(String.valueOf(message.getTimestamp())).setValue(message);
                    getFeedsFromServer();
                }
                mSendMessage.setEnabled(true);
                mMessageEditText.getText().clear();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mSendMessage.setEnabled(true);
                showSnackbar(R.string.error_message);
            }
        });
    }

    /**
     * change toolbar style
     */
    private void customizeToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar supportedToolbar = getSupportActionBar();
        // supportedToolbar.setDisplayHomeAsUpEnabled(true);
        supportedToolbar.setDisplayShowTitleEnabled(false);
        supportedToolbar.setDisplayShowCustomEnabled(true);
        //set user name and image
        if(mChattingUser.name!=null)
            mToolbarUserName.setText(mChattingUser.name);
        if(mChattingUser.pic!=null){
            Picasso.with(this).load(mChattingUser.pic).transform(new CircleTransformation()).into(mToolbarUserImage);
        }

    }

    @Override
    public void requestFinished(String responseStr, int requestTag) {
        boolean jsonError = false;
        if (requestTag == GET_CONV_REQUEST) {
            if(responseStr!=null&&!responseStr.equals("")){
                Gson gson = new Gson();
                Response response=gson.fromJson(responseStr,Response.class);
                if (response.status == 200){
                    Log.d(TAG,"start chat Success");
                    userRegister = true;
                }
            }
        }

    }

    @Override
    public void requestFailed(String errorMessage, int requestTag) {
        if (requestTag == GET_CONV_REQUEST)
            Log.d(TAG,"start chat unSuccess");
    }

    @Override
    public void imageDownloaded(Bitmap image, int requestTag) {

    }

    /**
     * compress image and upload it to firebase
     * @param filePath
     */
    private void uploadImageFirebase(String filePath) {
        //compress image here before upload
        String compressFilePath = compressImage(filePath);
        if (compressFilePath == null || compressFilePath.isEmpty()){
            showToast(R.string.error_message);
            return;
        }
        File chosenFile = new File(compressFilePath);

        Uri file = Uri.fromFile(chosenFile);
        mTaskFragment.uploadPhotoProfile(file);
    }


    @Override
    public void onPhotoProfileUploaded(final String imageUrl) {
        if(mProgressDialog.isShowing())
            mProgressDialog.dismiss();
        if(imageUrl!=null){

            // serverManager.startChat(GET_CONV_REQUEST,mCurrentUserId,mCurrentType,mAuthKey,mChattingUser.uid,mChattingUser.type);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //try to register user
                    startChat();
                    Message message = new Message(currentUid,
                            mChattingUser.uid + mChattingUser.type ,
                            imageUrl,MESSAGE_FILE_TYPE,System.currentTimeMillis(),
                            false);

                    //send message
                    sendMessageToFirebaseUser(message);
                }
            });

        }else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showToast(R.string.error_message);
                }
            });

        }
    }


    public String compressImage(String imageUri) {

        String filePath = imageUri;
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    @Override
    public void onImageResized(String resizedImage, boolean isFull) {

    }

    @Override
    public void onPhotoUploaded(String fullUrl, String thumbnailUrl) {

    }

    /**
     * Requests the Camera permission.
     * If the permission has been denied previously, a SnackBar will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    private void requestStoragePermission() {
        Log.i(TAG, "Storage permission has NOT been granted. Requesting permission.");

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example, if the request has been denied previously.
            Log.i(TAG,
                    "Displaying contacts permission rationale to provide additional context.");

            // Display a SnackBar with an explanation and a button to trigger the request.
            Snackbar.make(mRootView, R.string.permission_storage_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat
                                    .requestPermissions(ConversationActivity.this, PERMISSIONS_CONTACT,
                                            REQUEST_STORAGE);
                        }
                    })
                    .show();
        } else {

            // Storage permissions have not been granted yet. Request them directly.
            ActivityCompat.requestPermissions(this, PERMISSIONS_CONTACT, REQUEST_STORAGE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_STORAGE) {
            Log.i(TAG, "Received response for contact permissions request.");

            // We have requested multiple permissions for contacts, so all of them need to be
            // checked.
            if (PermissionUtil.verifyPermissions(grantResults)) {
                // All required permissions have been granted, display contacts fragment.
                Snackbar.make(mRootView, R.string.permission_available_storage,
                        Snackbar.LENGTH_SHORT)
                        .show();
            } else {
                Log.i(TAG, "Storage permissions were NOT granted.");
                Snackbar.make(mRootView, R.string.permissions_not_granted,
                        Snackbar.LENGTH_SHORT)
                        .show();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
