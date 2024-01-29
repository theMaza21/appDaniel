package com.daniel.appdaniel.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.daniel.appdaniel.R;
import com.daniel.appdaniel.providers.AuthProvider;
import com.daniel.appdaniel.providers.PostProvider;
import com.daniel.appdaniel.providers.UsersProvider;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity
{
    LinearLayout mLinearLayoutEditProfile;
    TextView mTextViewUsername;
    TextView mTextViewPhone;
    TextView mTextViewEmail;
    TextView mTextViewPostNumber;
    ImageView mImageViewCover;
    CircleImageView mCircleImageViewProfile;
    UsersProvider mUsersProvider;
    AuthProvider mAuthProvider;
    PostProvider mPostProvider;
    Toolbar mToolbar;
    String mExtraIdUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        mTextViewEmail = findViewById(R.id.textViewEmail);
        mTextViewPhone = findViewById(R.id.textViewPhone);
        mTextViewUsername = findViewById(R.id.textViewUserName);
        mTextViewPostNumber = findViewById(R.id.textViewPostNumber);
        mCircleImageViewProfile = findViewById(R.id.circuleImageProfile);
        mImageViewCover = findViewById(R.id.imageViewCover);
        mToolbar = findViewById(R.id.toolbar);
       setSupportActionBar(mToolbar);
       getSupportActionBar().setTitle("");
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mAuthProvider = new AuthProvider();
        mUsersProvider = new UsersProvider();
        mPostProvider = new PostProvider();
        mExtraIdUser = getIntent().getStringExtra("idUser");
        getUser();
        getPostNumber();
    }

    private void getPostNumber()
    {
        mPostProvider.getPostByUser(mExtraIdUser).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots)
            {
                int numberPost  = queryDocumentSnapshots.size();
                mTextViewPostNumber.setText(String.valueOf(numberPost));
            }
        });
    }
    private void getUser()
    {
        mUsersProvider.getUser(mExtraIdUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                if (documentSnapshot.contains("email"))
                {
                    String email = documentSnapshot.getString("email");
                    mTextViewEmail.setText(email);
                }
                if (documentSnapshot.contains("phone"))
                {
                    String phone = documentSnapshot.getString("phone");
                    mTextViewPhone.setText(phone);
                }
                if (documentSnapshot.contains("username"))
                {
                    String username = documentSnapshot.getString("username");
                    mTextViewUsername.setText(username);
                }
                if (documentSnapshot.contains("image_profile"))
                {
                    String imageProfile = documentSnapshot.getString("image_profile");
                    if (imageProfile != null)
                    {
                        if (!imageProfile.isEmpty())
                        {
                            Picasso.get().load(imageProfile).into(mCircleImageViewProfile);
                        }
                    }
                }

                if (documentSnapshot.contains("image_cover"))
                {
                    String imageCover = documentSnapshot.getString("image_cover");
                    if (imageCover != null)
                    {
                        if (!imageCover.isEmpty())
                        {
                            Picasso.get().load(imageCover).into(mImageViewCover);
                        }
                    }
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return true;
    }
}
