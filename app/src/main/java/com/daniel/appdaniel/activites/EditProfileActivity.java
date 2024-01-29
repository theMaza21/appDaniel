package com.daniel.appdaniel.activites;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.daniel.appdaniel.R;
import com.daniel.appdaniel.models.User;
import com.daniel.appdaniel.providers.AuthProvider;
import com.daniel.appdaniel.providers.ImageProvider;
import com.daniel.appdaniel.providers.UsersProvider;
import com.daniel.appdaniel.utils.FileUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class EditProfileActivity extends AppCompatActivity {
CircleImageView mCircleImageViewBack;
CircleImageView mCircleImageViewProfile;
ImageView mImageViewCover;
TextInputEditText mTextInputUsername;
TextInputEditText mTextInputPhone;
    AlertDialog.Builder mBuilderSelecor;
    CharSequence options[];
    private final int GALLERY_REQUEST_CODE_PROFILE = 1;
    private final int GALLERY_REQUEST_CODE_COVER = 2;
    private final int PHOTO_REQUEST_CODE_PROFILE = 3;
    private final int PHOTO_REQUEST_CODE_COVER = 4;

    Button mButtonProfile;


    // Foto 1
    File mPhotoFile;
    String mPhotoPath;
    String mAbsoulutePhotoPath;

    // Foto 2
    File  mPhotoFile2;
    String mPhotoPath2;
    String mAbsoulutePhotoPath2;

    File mImagenFile;
    File mImagenFile2;
    String mUsername = "";
    String mPhone = "";
    String mImageProfile = "";
    String mImageCover = "";
    AlertDialog mDialog;
    ImageProvider mImageProvider;

    UsersProvider mUsersProvider;
    AuthProvider mAuthProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mCircleImageViewBack = findViewById(R.id.circuleImageBack);
        mCircleImageViewProfile = findViewById(R.id.circuleImageProfile);
        mImageViewCover = findViewById(R.id.imageViewCover);
        mTextInputPhone = findViewById(R.id.textInputPhone);
        mTextInputUsername = findViewById(R.id.NombreDeUsuario);
        mBuilderSelecor = new AlertDialog.Builder(this);
        mButtonProfile = findViewById(R.id.btnEditProfile);
        mBuilderSelecor.setTitle("Seleciona una opcion");
        mImageProvider = new ImageProvider();
        mUsersProvider = new UsersProvider();
        mAuthProvider = new AuthProvider();
        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espera un momento")
                .setCancelable(false).build();
        mBuilderSelecor = new AlertDialog.Builder(this);
        options = new CharSequence[]{"Imagen de galeria","Tomar foto"};
        mButtonProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                clickEditProfile();
            }
        });
        mCircleImageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
             selectOptionImage(1);
            }
        });
        mImageViewCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionImage(2);
            }
        });
        mCircleImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getUser();
    }
    private void getUser()
    {
        mUsersProvider.getUser(mAuthProvider.getUid()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists())
                {
                    if (documentSnapshot.contains("username"))
                    {
                        mUsername = documentSnapshot.getString("username");
                        mTextInputUsername.setText(mUsername);
                    }
                   if (documentSnapshot.contains("phone"))
                   {
                       mPhone = documentSnapshot.getString("phone");
                       mTextInputPhone.setText(mPhone);
                   }
                   if (documentSnapshot.contains("image_profile"))
                   {
                        mImageProfile = documentSnapshot.getString("image_profile");
                        if (mImageProfile != null)
                        {
                            if(!mImageProfile.isEmpty())
                            {
                                Picasso.get().load(mImageProfile).into(mCircleImageViewProfile);
                            }
                        }
                   }
                   if(documentSnapshot.contains("image_cover"))
                   {
                       mImageCover = documentSnapshot.getString("image_cover");
                       if (mImageCover != null )
                       {
                           if (!mImageCover.isEmpty())
                           {
                               Picasso.get().load(mImageCover).into(mImageViewCover);
                           }
                       }
                   }

                }
            }
        });
    }

    private void clickEditProfile()
    {
        mUsername =mTextInputUsername.getText().toString();
        mPhone = mTextInputPhone.getText().toString();
        if (!mUsername.isEmpty() && !mPhone.isEmpty())
        {
            if (mImagenFile != null && mImagenFile2 != null)
            {
                saveImageCoverAndProfile(mImagenFile, mImagenFile2);
            }
            else if (mPhotoFile != null && mPhotoFile2 != null)
            {
                saveImageCoverAndProfile(mPhotoFile,mPhotoFile2);
            }
            else if (mImagenFile != null && mPhotoFile2 != null)
            {
                saveImageCoverAndProfile(mImagenFile,mPhotoFile2);
            }
            else if (mPhotoFile != null && mImagenFile2 != null)
            {
                saveImageCoverAndProfile(mPhotoFile,mImagenFile2);
            }
            else if (mPhotoFile != null)
            {
                 saveImage(mPhotoFile, true);
            }
            else if (mPhotoFile2 != null)
            {
                saveImage(mPhotoFile2, false);
            }
            else if (mImagenFile != null)
            {
                saveImage(mImagenFile, true);
            }
            else if (mImagenFile2 != null)
            {
                saveImage(mImagenFile2,false);
            }
            else
            {
                User user = new User();
                user.setUsername(mUsername);
                user.setPhone(mPhone);
                user.setId(mAuthProvider.getUid());
                updateInfo(user);
            }
        }
        else
        {
            Toast.makeText(this,"Ingresa el Nombre del Usuario y el Telfono ",Toast.LENGTH_SHORT).show();
        }
    }
    private void saveImageCoverAndProfile(File imagenFile1 , File imagenFile2) {
        mDialog.show();
        final String imageName1 = UUID.randomUUID().toString();
        final String imageName2 = UUID.randomUUID().toString();

        mImageProvider.save(EditProfileActivity.this, imagenFile1, imageName1).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful())
                {
                    mImageProvider.getmStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                    {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                            final String urlProfile = uri.toString();
                            mImageProvider.save(EditProfileActivity.this, imagenFile2, imageName2).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> taskImage2) {
                                    if (taskImage2.isSuccessful())
                                    {
                                        mImageProvider.getmStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri2)
                                            {
                                                String urlCover = uri2.toString();
                                                User user = new User();
                                                user.setUsername(mUsername);
                                                user.setPhone(mPhone);
                                                user.setImageCover(urlProfile);
                                                user.setImageProfile(urlCover);
                                                user.setId(mAuthProvider.getUid());
                                              updateInfo(user);

                                            }
                                        });
                                    }
                                    else {
                                        mDialog.dismiss();
                                        Toast.makeText(EditProfileActivity.this, "La imagen numero 2 no se pudo guardar", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
                else {
                    mDialog.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Hubo error al almacenar la imagen", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void saveImage(File image,boolean isProfileImage)
    {
        final String imageName0 = UUID.randomUUID().toString();
        mDialog.show();
        mImageProvider.save(EditProfileActivity.this, image,imageName0).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful())
                {
                    mImageProvider.getmStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String url = uri.toString();
                            User user = new User();
                            user.setUsername(mUsername);
                            user.setPhone(mPhone);
                            if (isProfileImage)
                            {

                                user.setImageProfile(url);
                                user.setImageCover(mImageCover);
                            }
                            else
                            {
                                user.setImageCover(url);
                                user.setImageProfile(mImageProfile);
                            }

                            user.setId(mAuthProvider.getUid());
                            updateInfo(user);

                        }
                    });
                }
            }
        });
    }


    private void updateInfo(User user)
    {
        if (mDialog.isShowing())
        {
            mDialog.show();

        }
        mDialog.show();
        mUsersProvider.update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mDialog.dismiss();
                if (task.isSuccessful())
                {
                    Toast.makeText(EditProfileActivity.this,"la informacion se actualizo Correctamente ",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(EditProfileActivity.this,"la informacion no se pudo actualizar ",Toast.LENGTH_SHORT).show();
                }

            }

        });
    }
    private void selectOptionImage(int numberImage)
    {
        mBuilderSelecor.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0)
                {
                    if (numberImage == 1 )
                    {
                        operGallery(GALLERY_REQUEST_CODE_PROFILE);
                    }
                    else if (numberImage == 2)
                    {
                        operGallery(GALLERY_REQUEST_CODE_COVER);
                    }

                }
                else if (i == 1)
                {
                    if (numberImage == 1 )
                    {
                        takePhoto(PHOTO_REQUEST_CODE_PROFILE);
                    }
                    else if (numberImage == 2)
                    {
                        takePhoto(PHOTO_REQUEST_CODE_COVER);
                    }
                }
            }
        });
        mBuilderSelecor.show();
    }

    private void takePhoto(int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createPhotoFile(requestCode);
            } catch (Exception e) {
                Toast.makeText(this, "Hubo un error con el archivo" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(EditProfileActivity.this, "com.daniel.appdaniel", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                if (requestCode == PHOTO_REQUEST_CODE_PROFILE) {
                    galleryLauncher3.launch(takePictureIntent);
                } else if (requestCode == PHOTO_REQUEST_CODE_COVER) {
                    galleryLauncher4.launch(takePictureIntent);
                }
            }
        }
    }

    private File createPhotoFile(int requestCode) throws IOException
    {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File photoFile = File.createTempFile(
                new Date()+ "_photo",".jpg",storageDir
        );
        if (requestCode == PHOTO_REQUEST_CODE_PROFILE)
        {
            mPhotoPath = "file:" + photoFile.getAbsolutePath();
            mAbsoulutePhotoPath = photoFile.getAbsolutePath();
        }
        else if (requestCode == PHOTO_REQUEST_CODE_COVER)
        {
            mPhotoPath2 = "file:" + photoFile.getAbsolutePath();
            mAbsoulutePhotoPath2 = photoFile.getAbsolutePath();
        }

        return photoFile;
    }
    private void operGallery(int galleryRequestCode) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        if (galleryRequestCode == GALLERY_REQUEST_CODE_PROFILE) {
            galleryLauncher.launch(galleryIntent);
        } else if (galleryRequestCode == GALLERY_REQUEST_CODE_COVER) {
            galleryLauncher2.launch(galleryIntent);
        }
    }




    private void handleGalleryResult(Uri selectedImageUri, int requestCode)
    {
        try
        {
            if (selectedImageUri != null)
            {
                if (requestCode == GALLERY_REQUEST_CODE_PROFILE)
                {
                    mImagenFile = FileUtil.from(EditProfileActivity.this, selectedImageUri);

                    if (mImagenFile != null)
                    {
                        mCircleImageViewProfile.setImageBitmap(BitmapFactory.decodeFile(mImagenFile.getAbsolutePath()));
                    }
                    else
                    {
                        Toast.makeText(EditProfileActivity.this, "Error al obtener el archivo de imagen", Toast.LENGTH_LONG).show();
                    }
                }
                else if (requestCode == GALLERY_REQUEST_CODE_COVER)
                {
                    mImagenFile2 = FileUtil.from(EditProfileActivity.this, selectedImageUri);
                    if (mImagenFile2 !=null)
                    {
                        mImageViewCover.setImageBitmap(BitmapFactory.decodeFile(mImagenFile2.getAbsolutePath()));
                    }
                    else
                    {
                        Toast.makeText(EditProfileActivity.this, "Error al obtener el archivo de imagen", Toast.LENGTH_LONG).show();
                    }
                }
            }
        } catch (Exception e)
        {
            Log.d("ERROR", "Se produjo un error " + e.getMessage());
            Toast.makeText(EditProfileActivity.this, "Se produjo un error " + e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }
    ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedImageUri = data.getData();
                            handleGalleryResult(selectedImageUri, GALLERY_REQUEST_CODE_PROFILE);
                        }
                    }
                }
            }
    );

    ActivityResultLauncher<Intent> galleryLauncher2 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedImageUri = data.getData();
                            handleGalleryResult(selectedImageUri, GALLERY_REQUEST_CODE_COVER);
                        }
                    }
                }
            }
    );
    ActivityResultLauncher<Intent> galleryLauncher3 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // La foto fue tomada con éxito
                        mPhotoFile = new File(mAbsoulutePhotoPath);
                        Picasso.get().load(mPhotoPath).into(mCircleImageViewProfile);
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        // El usuario canceló la captura de fotos
                        Toast.makeText(EditProfileActivity.this, "Captura de fotos cancelada", Toast.LENGTH_SHORT).show();
                    } else {
                        // Algo salió mal
                        Toast.makeText(EditProfileActivity.this, "Error al capturar la foto", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    ActivityResultLauncher<Intent> galleryLauncher4 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        mPhotoFile2 = null;
                        // La segunda foto fue tomada con éxito
                        mPhotoFile2 = new File(mAbsoulutePhotoPath2);
                        Picasso.get().load(mPhotoPath2).into(mImageViewCover);
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        // El usuario canceló la captura de la segunda foto
                        Toast.makeText(EditProfileActivity.this, "Captura de segunda foto cancelada", Toast.LENGTH_SHORT).show();
                    } else {
                        // Algo salió mal
                        Toast.makeText(EditProfileActivity.this, "Error al capturar la segunda foto", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );


}