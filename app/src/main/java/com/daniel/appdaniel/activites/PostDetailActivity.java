package com.daniel.appdaniel.activites;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daniel.appdaniel.R;
import com.daniel.appdaniel.adapters.CommentAdapter;
import com.daniel.appdaniel.adapters.PostsAdapter;
import com.daniel.appdaniel.adapters.SliderAdapter;
import com.daniel.appdaniel.models.Comment;
import com.daniel.appdaniel.models.FCMBody;
import com.daniel.appdaniel.models.FCMResponse;
import com.daniel.appdaniel.models.Post;
import com.daniel.appdaniel.models.SliderItem;
import com.daniel.appdaniel.providers.AuthProvider;
import com.daniel.appdaniel.providers.CommentProvider;
import com.daniel.appdaniel.providers.LikesProvider;
import com.daniel.appdaniel.providers.NotificationProviders;
import com.daniel.appdaniel.providers.PostProvider;
import com.daniel.appdaniel.providers.TokenProvider;
import com.daniel.appdaniel.providers.UsersProvider;
import com.daniel.appdaniel.utils.RelativeTime;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetailActivity extends AppCompatActivity {
    SliderView msliderView;
    SliderAdapter mSliderAdapter;
    List<SliderItem> mSliderItem = new ArrayList<>();
    String mExtraPostId;
    PostProvider mPostProvider;
    UsersProvider mUserProvider;
    CommentProvider mCommentProvider;
     TextView mTextViewRelativeTime;
     LikesProvider mLikesProvider;

    CommentAdapter mAdapter;
    AuthProvider mAuthProvider;
    TextView mTextViewTitle;
    TextView mTextViewDescription;
    TextView mTextViewUsername;
    TextView mTextViewPhone;
    TextView mTextViewNameCategory;
    ImageView mImageViewCategory;
    TextView mTextViewLikes;
    CircleImageView mCircleImageViewProfile;
    Button mButtonShowProfile;
    TokenProvider mTokenProvider;
    String mIdUser = "";
    FloatingActionButton mFabComment;
    RecyclerView mRecyclerView;
    Toolbar mToolbar;
    NotificationProviders mNotificationProviders;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        msliderView = findViewById(R.id.imageSlider);
        mTextViewTitle = findViewById(R.id.textViewUserTitle);
        mTextViewDescription = findViewById(R.id.textViewDescripcion);
        mTextViewUsername = findViewById(R.id.textViewUserName);
        mTextViewPhone = findViewById(R.id.textViewPhone);
        mTextViewRelativeTime = findViewById(R.id.textViewRelativeTime);
        mTextViewNameCategory = findViewById(R.id.textViewCategor);
        mImageViewCategory = findViewById(R.id.imageViewCategory);
        mCircleImageViewProfile = findViewById(R.id.CircolImageProfile);
        mButtonShowProfile = findViewById(R.id.btnShowProfile);

        mFabComment = findViewById(R.id.fabComment);
        mRecyclerView =findViewById(R.id.recycleViewComments);
        mTextViewLikes = findViewById(R.id.textViewLikes);
        mToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        LinearLayoutManager linearLayoutManager  = new LinearLayoutManager(PostDetailActivity.this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mPostProvider = new PostProvider();
        mUserProvider = new UsersProvider();
        mCommentProvider = new CommentProvider();
        mAuthProvider = new AuthProvider();
        mLikesProvider = new LikesProvider();
        mNotificationProviders = new NotificationProviders();
        mTokenProvider = new TokenProvider();
        mExtraPostId = getIntent().getStringExtra("id");


        mFabComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogComment();
            }
        });


        mButtonShowProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToShowProfile();
            }
        });

        getPost();
        getNumberLikes();
    }

    private void getNumberLikes()
    {
        mLikesProvider.getLikesByPost(mExtraPostId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (queryDocumentSnapshots != null)
                {
                    int numberLikes = queryDocumentSnapshots.size();
                    if (numberLikes == 1)
                    {
                        mTextViewLikes.setText(numberLikes + "  Me gusta");
                    }
                    else
                    {
                        mTextViewLikes.setText(numberLikes +"  Me gustas");
                    }
                }
                else
                {
                    Log.e("FirestoreListener", "QuerySnapshot is null. Error: " + e);
                }
            }
        });
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Query query = mCommentProvider.getCommentsByPost(mExtraPostId);
        FirestoreRecyclerOptions<Comment> options =
                new FirestoreRecyclerOptions.Builder<Comment>()
                        .setQuery(query,Comment.class)
                        .build();
        mAdapter = new CommentAdapter(options,PostDetailActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.startListening();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        mAdapter.stopListening();
    }

    private void showDialogComment()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(PostDetailActivity.this);
        alert.setTitle("COMENTARIO!");
        alert.setMessage("Ingresa tu comentario");
        EditText editText = new EditText(PostDetailActivity.this);
        editText.setHint("Text");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
          LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(36,0,36,36);
        editText.setLayoutParams(params);
        RelativeLayout container = new RelativeLayout(PostDetailActivity.this);
        ViewGroup.LayoutParams relativePrams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        container.setLayoutParams(relativePrams);
        container.addView(editText);
        alert.setView(container);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = editText.getText().toString();
                if (!value.isEmpty())
                {
                    createComment(value);
                }
                else 
                {
                    Toast.makeText(PostDetailActivity.this, "Debe ingresar  el Comentario ", Toast.LENGTH_SHORT).show();
                }

            }
        });
        alert.setNegativeButton("Caselar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alert.show();
    }

    private void createComment(final String value)
    {
        Comment comment = new Comment();
        comment.setComment(value);
        comment.setIdPost(mExtraPostId);
        comment.setIdUser(mAuthProvider.getUid());
        comment.setTimestamp(new Date().getTime());
        mCommentProvider.create(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    sendNoificaation(value);
                    Toast.makeText(PostDetailActivity.this, "El comentario se creo correcta mente ", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(PostDetailActivity.this, "No se pudo crear el comentario   ", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
     private void sendNoificaation(final String comment)
     {
        if (mIdUser == null)
        {
        return;
        }
        mTokenProvider.getToken(mIdUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                if (documentSnapshot.exists())
                {
                    if (documentSnapshot.contains("token"))
                    {
                        String token = documentSnapshot.getString("token");
                        Map<String, String> data = new HashMap<>();
                        data.put("title","NUEVO COMENTARIO ");
                        data.put("body",comment);
                        FCMBody body = new FCMBody(token,  "higt","4500s",data);
                        mNotificationProviders.sendNotification(body).enqueue(new Callback<FCMResponse>()
                        {
                            @Override
                            public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response)
                            {
                                if(response.body() != null)
                                {
                                    if (response.body().getSuccess() == 1)
                                    {
                                        Toast.makeText(PostDetailActivity.this, "La Notificacion se Envio Correcta Mente ", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(PostDetailActivity.this, "La Notificacion no se Pudo mandar Correctamente ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    Toast.makeText(PostDetailActivity.this, "La Notificacion no se Pudo mandar Correctamente ", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<FCMResponse> call, Throwable t) {

                            }
                        });
                    }
                }
                else
                {
                    Toast.makeText(PostDetailActivity.this, "El Token del Usuario no Existe  ", Toast.LENGTH_SHORT).show();

                }
            }
        });
     }
    private void goToShowProfile()
    {
        if(!mIdUser.equals(""))
        {
            Intent intent = new Intent(PostDetailActivity.this,UserProfileActivity.class);
            intent.putExtra("idUser",mIdUser);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "El id del usario no se a cargado ", Toast.LENGTH_SHORT).show();
        }
    }

    private void instanceSlider()
    {
        mSliderAdapter = new SliderAdapter(PostDetailActivity.this,mSliderItem);
        msliderView.setSliderAdapter(mSliderAdapter);
        msliderView.setIndicatorAnimation(IndicatorAnimations.THIN_WORM);
        msliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        msliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);
        msliderView.setIndicatorSelectedColor(Color.WHITE);
        msliderView.setIndicatorUnselectedColor(Color.GRAY);
        msliderView.setScrollTimeInSec(3);
        msliderView.setAutoCycle(true);
        msliderView.startAutoCycle();
    }
    private void getPost() {
        mPostProvider.getPostById(mExtraPostId).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                if (documentSnapshot.exists())
                {
                    if (documentSnapshot.contains("image1"))
                    {
                        String image1 = documentSnapshot.getString("image1");
                        SliderItem item = new SliderItem();
                        item.setImagenUrl(image1);
                        mSliderItem.add(item);
                    }
                    if (documentSnapshot.contains("image2"))
                    {
                        String image2 = documentSnapshot.getString("image2");
                        SliderItem item = new SliderItem();
                        item.setImagenUrl(image2);
                        mSliderItem.add(item);
                    }
                    if (documentSnapshot.contains("title"))
                    {
                        String title = documentSnapshot.getString("title");
                        mTextViewTitle.setText(title.toUpperCase());
                    }
                    if (documentSnapshot.contains("description"))
                    {
                        String description = documentSnapshot.getString("description");
                        mTextViewDescription.setText(description);
                    }
                    if (documentSnapshot.contains("category"))
                    {
                        String category = documentSnapshot.getString("category");
                        mTextViewNameCategory.setText(category);

                        if (category.equals("Nintendo"))
                        {
                            mImageViewCategory.setImageResource(R.drawable.icon_nintendo);
                        }
                        else if (category.equals("PS4"))
                        {
                            mImageViewCategory.setImageResource(R.drawable.icon_ps4);
                        }
                        else if (category.equals("Xbox"))
                        {
                            mImageViewCategory.setImageResource(R.drawable.icon_xbox);
                        }
                        else if (category.equals("PC"))
                        {
                            mImageViewCategory.setImageResource(R.drawable.icon_pc);
                        }
                    }
                    if (documentSnapshot.contains("idUser"))
                    {
                        mIdUser = documentSnapshot.getString("idUser");
                        getUserInfo(mIdUser);

                    }
                    if (documentSnapshot.contains("timestamp"))
                    {
                        long timestamp = documentSnapshot.getLong("timestamp");
                        String relativeTime = RelativeTime.getTimeAgo(timestamp,PostDetailActivity.this);
                        mTextViewRelativeTime.setText(relativeTime);

                    }
                    instanceSlider();

                }
            }
        });
    }

    private void getUserInfo(String idUser)
    {
        mUserProvider.getUser(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                if (documentSnapshot.exists())
                {
                    if (documentSnapshot.contains("username"))
                    {
                        String username = documentSnapshot.getString("username");
                        mTextViewUsername.setText(username);
                    }
                    if (documentSnapshot.contains("phone"))
                    {
                        String phone = documentSnapshot.getString("phone");
                        mTextViewPhone.setText(phone);
                    }
                    if (documentSnapshot.contains("image_profile"))
                    {
                        String imageProfile = documentSnapshot.getString("image_profile");
                        Picasso.get().load(imageProfile).into(mCircleImageViewProfile);
                    }
                }
            }
        });


    }
}