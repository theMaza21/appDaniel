package com.daniel.appdaniel.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.daniel.appdaniel.R;
import com.daniel.appdaniel.activites.PostDetailActivity;
import com.daniel.appdaniel.models.Like;
import com.daniel.appdaniel.models.Post;
import com.daniel.appdaniel.providers.AuthProvider;
import com.daniel.appdaniel.providers.LikesProvider;
import com.daniel.appdaniel.providers.PostProvider;
import com.daniel.appdaniel.providers.UsersProvider;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class PostsAdapter extends FirestoreRecyclerAdapter<Post, PostsAdapter.ViewHolder>
{
    private Context context;
    PostProvider mPostProvider;
    UsersProvider mUsersProvider;
    LikesProvider mLikesProvider;
    AuthProvider mAuthProvider;
    TextView mTextViewNumberFilter;
    public PostsAdapter(@NonNull FirestoreRecyclerOptions<Post> options, Context context)
    {
        super(options);
        this.context = context;
        mPostProvider = new PostProvider();
        mUsersProvider = new UsersProvider();
        mLikesProvider = new LikesProvider();
        mAuthProvider = new AuthProvider();
    }
    public PostsAdapter(@NonNull FirestoreRecyclerOptions<Post> options, Context context,TextView textView)
    {
        super(options);
        this.context = context;
        mPostProvider = new PostProvider();
        mUsersProvider = new UsersProvider();
        mLikesProvider = new LikesProvider();
        mAuthProvider = new AuthProvider();
        mTextViewNumberFilter =  textView;
    }

    @Override
    protected void onBindViewHolder(@NonNull final  ViewHolder holder, int position, @NonNull final Post post)
    {
        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        final String postId = document.getId();

        if (mTextViewNumberFilter != null)
        {
            int nuberFilter = getSnapshots().size();
            mTextViewNumberFilter.setText(String.valueOf(nuberFilter));
        }


        holder.textViewTitle.setText(post.getTitle().toUpperCase());
        holder.textViewDescripcion.setText(post.getDescription());
        if (post.getImage1() != null)
        {
            if (!post.getImage1().isEmpty())
            {
                Picasso.get().load(post.getImage1()).into(holder.imageViewPost);
            }
        }
        holder.viewHolder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, PostDetailActivity.class);
                intent.putExtra("id",postId);
            context.startActivity(intent);
            }
        });

  holder.imageViewLikes.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

          Like like = new Like();
          like.setIdUser(mAuthProvider.getUid());
          like.setIdPost(postId);
          like.setTimestamp(new Date().getTime());
          like(like,holder);

      }
  });
        getUserInfo(post.getIdUser(),holder);
        getNumberLikesByPost(postId,holder);

        checkIfExistlike(postId,mAuthProvider.getUid(),holder);
    }
    private void getNumberLikesByPost(String idPost, final ViewHolder holder) {
        mLikesProvider.getLikesByPost(idPost).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {
                    int numberLikes = queryDocumentSnapshots.size();
                    holder.textViewLikes.setText(String.valueOf(numberLikes) + " Me gustas");
                } else {
                   
                    Log.e("FirestoreListener", "QuerySnapshot is null. Error: " + e);
                }
            }
        });
    }
    private void like(final Like like, final ViewHolder holder)
    {
        mLikesProvider.getLikeByPostAndUser(like.getIdPost(),mAuthProvider.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots)
            {
                int numberDocuments = queryDocumentSnapshots.size();
                if (numberDocuments > 0 )
                {
                    String idLike = queryDocumentSnapshots.getDocuments().get(0).getId();
                    holder.imageViewLikes.setImageResource(R.drawable.icon_like_grey);
                    mLikesProvider.delete(idLike);
                }
                else
                {
                    holder.imageViewLikes.setImageResource(R.drawable.icon_like_blue);
                    mLikesProvider.create(like);
                }
            }
        });

    }

    private void checkIfExistlike(String idPost, String idUser, ViewHolder holder)
    {
        mLikesProvider.getLikeByPostAndUser(idPost,idUser).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots)
            {
                int numberDocuments = queryDocumentSnapshots.size();
                if (numberDocuments > 0 )
                {

                    holder.imageViewLikes.setImageResource(R.drawable.icon_like_blue);

                }
                else
                {
                    holder.imageViewLikes.setImageResource(R.drawable.icon_like_grey);


                }
            }
        });

    }
    private void getUserInfo(String idUser, final ViewHolder holder)
    {
        mUsersProvider.getUser(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
        {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
           if (documentSnapshot.exists())
           {
              if (documentSnapshot.contains("username"))
              {
                  String username = documentSnapshot.getString("username");
                  holder.textViewUserName.setText("BY: "+ username.toUpperCase());

              }
           }
            }
        });

    }

    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_post, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewDescripcion;
        TextView textViewUserName;
        TextView textViewLikes;
        ImageView imageViewPost;
        ImageView imageViewLikes;


        View viewHolder;

        public ViewHolder(View view)
        {
            super(view);
            textViewTitle = view.findViewById(R.id.textViewTitlePostCad);
            textViewDescripcion = view.findViewById(R.id.textViewDescripcionPostCard);
            textViewUserName = view.findViewById(R.id.textViewUserNamePostCad);
            imageViewPost = view.findViewById(R.id.imageViewPostCard);
            textViewLikes = view.findViewById(R.id.textViewLikes);
            imageViewLikes = view.findViewById(R.id.imageViewLikes);

            viewHolder =view;
        }
    }
}