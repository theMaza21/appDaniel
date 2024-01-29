package com.daniel.appdaniel.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.daniel.appdaniel.R;
import com.daniel.appdaniel.activites.PostDetailActivity;
import com.daniel.appdaniel.models.Comment;
import com.daniel.appdaniel.models.Post;
import com.daniel.appdaniel.providers.UsersProvider;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends FirestoreRecyclerAdapter<Comment, CommentAdapter.ViewHolder>
{
    private Context context;
    UsersProvider mUsersProvider;
    public CommentAdapter(@NonNull FirestoreRecyclerOptions<Comment> options, Context context)
    {
        super(options);
        this.context = context;
        mUsersProvider = new UsersProvider();

    }


    private void getUserInfo(String idUser, final ViewHolder holder)
    {
        mUsersProvider.getUser(idUser).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
           if (documentSnapshot.exists())
           {
                if(documentSnapshot.contains("username"))
                {
                    String username = documentSnapshot.getString("username");
                    holder.textViewUsername.setText(username.toUpperCase());
                }
                if (documentSnapshot.contains("image_profile"))
                {
                    String imageProfile = documentSnapshot.getString("image_profile");
                    if (imageProfile != null)
                    {
                        if (!imageProfile.isEmpty())
                        {
                            Picasso.get().load(imageProfile).into(holder.circuleImageComment);
                        }
                    }
                }
           }
            }
        });
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Comment comment) {
        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        final String commentId = document.getId();
        String idUser = document.getString("idUser");

        holder.textViewComment.setText(comment.getComment());
        getUserInfo(idUser, holder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUsername;
        TextView textViewComment;
        CircleImageView circuleImageComment;
        View viewHolder;

        public ViewHolder(View view) {
            super(view);
            textViewUsername = view.findViewById(R.id.textViewUserName);
            textViewComment = view.findViewById(R.id.textViewComment);
            circuleImageComment = view.findViewById(R.id.circuleImageComment);
            viewHolder =view;
        }
    }
}