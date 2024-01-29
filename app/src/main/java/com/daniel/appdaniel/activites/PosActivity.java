package com.daniel.appdaniel.activites;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.TextView;
import android.widget.Toast;
import com.daniel.appdaniel.R;
import com.daniel.appdaniel.models.Post;
import com.daniel.appdaniel.providers.AuthProvider;
import com.daniel.appdaniel.providers.ImageProvider;
import com.daniel.appdaniel.providers.PostProvider;
import com.daniel.appdaniel.utils.FileUtil;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class PosActivity extends AppCompatActivity {
        ImageView mImageViewPost1;
        ImageView mImageViewPost2;
        File mImagenFile;
        File mImagenFile2;
        Button mButtonPost;
        ImageProvider mImageProvider;
        PostProvider mPostProvider;
        AuthProvider mAuthProvider;
        TextInputEditText mTextInputTitle;
       TextInputEditText mTextInputDescripcion;
       ImageView mImageViewPC;
       ImageView mImageViewPS;
       ImageView mImageViewNintendo;
       ImageView mImageViewXbox;
       CircleImageView mCircleImageBack;
       TextView mTextViewCategory;
       String mCategory = "";
       String mTitle = "";
       String mDescription = "";
       AlertDialog mDialog;

       AlertDialog.Builder mBuilderSelecor;
       CharSequence options[];
    private final int GALLERY_REQUEST_CODE = 1;
    private final int GALLERY_REQUEST_CODE_2 = 2;
    private final int PHOTO_REQUEST_CODE = 3;
    private final int PHOTO_REQUEST_CODE_2 = 4;


        // Foto 1
        File  mPhotoFile;
        String mPhotoPath;
        String mAbsoulutePhotoPath;

        // Foto 2
        File  mPhotoFile2;
        String mPhotoPath2;
         String mAbsoulutePhotoPath2;
    ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedImageUri = data.getData();
                            handleGalleryResult(selectedImageUri, GALLERY_REQUEST_CODE);
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
                            handleGalleryResult(selectedImageUri, GALLERY_REQUEST_CODE_2);
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
                        Picasso.get().load(mPhotoPath).into(mImageViewPost1);
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        // El usuario canceló la captura de fotos
                        Toast.makeText(PosActivity.this, "Captura de fotos cancelada", Toast.LENGTH_SHORT).show();
                    } else {
                        // Algo salió mal
                        Toast.makeText(PosActivity.this, "Error al capturar la foto", Toast.LENGTH_SHORT).show();
                    }

                }
            }
    );

    // Definir otro launcher para la segunda casilla
    ActivityResultLauncher<Intent> galleryLauncher4 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        mPhotoFile2 = null;
                        // La segunda foto fue tomada con éxito
                        mPhotoFile2 = new File(mAbsoulutePhotoPath2);
                        Picasso.get().load(mPhotoPath2).into(mImageViewPost2);
                    } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                        // El usuario canceló la captura de la segunda foto
                        Toast.makeText(PosActivity.this, "Captura de segunda foto cancelada", Toast.LENGTH_SHORT).show();
                    } else {
                        // Algo salió mal
                        Toast.makeText(PosActivity.this, "Error al capturar la segunda foto", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos);
        mImageViewPost1 = findViewById(R.id.imageViewPost1);
        mImageViewPost2 = findViewById(R.id.imageViewPost2);
        mButtonPost =findViewById(R.id.btnPost);
        mTextInputTitle = findViewById(R.id.textInputVideoGame);
        mTextInputDescripcion = findViewById(R.id.textInputDescpripcion);
        mImageViewPC = findViewById(R.id.imageViewPC);
        mImageViewPS = findViewById(R.id.imageViewPS4);
        mImageViewNintendo = findViewById(R.id.imageViewNintendo);
        mImageViewXbox = findViewById(R.id.imageViewXbox);
        mTextViewCategory = findViewById(R.id.textViweCateory);
        mCircleImageBack = findViewById(R.id.circuleImageBack);
        mPostProvider = new PostProvider();
        mImageProvider = new ImageProvider();
        mAuthProvider  = new AuthProvider();
        mDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Espera un momento")
                .setCancelable(false).build();
        mBuilderSelecor = new AlertDialog.Builder(this);
        mBuilderSelecor.setTitle("Seleciona una opcion");
        options = new CharSequence[]{"Imagen de galeria","Tomar foto"};

        mButtonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                clickPos();
            }
        });

        mCircleImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        mImageViewPost1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOptionImage(1);

            }
        });

        mImageViewPost2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectOptionImage(2);
            }
        });

        mImageViewPC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
             mCategory = "PC";
             mTextViewCategory.setText(mCategory);
            }
        });
        mImageViewPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mCategory = "PS4";
                mTextViewCategory.setText(mCategory);
            }
        });
        mImageViewNintendo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mCategory = "Nintendo";
                mTextViewCategory.setText(mCategory);
            }
        });
        mImageViewXbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                mCategory = "Xbox";
                mTextViewCategory.setText(mCategory);
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
                        operGallery(GALLERY_REQUEST_CODE);
                    }
                    else if (numberImage == 2)
                    {
                        operGallery(GALLERY_REQUEST_CODE_2);
                    }

                }
                else if (i == 1)
                {
                    if (numberImage == 1 )
                    {
                        takePhoto(PHOTO_REQUEST_CODE);
                    }
                    else if (numberImage == 2)
                    {
                        takePhoto(PHOTO_REQUEST_CODE_2);
                    }
                }
            }
        });
        mBuilderSelecor.show();
    }

    private void takePhoto(int requestCode)
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            File photoFile = null;
            try
            {
                photoFile = createPhotoFile(requestCode);
            }
            catch (Exception e )
            {
             Toast.makeText(this, "Hubo un error con el archivo"+ e.getMessage(),Toast.LENGTH_LONG).show();

            }
            if(photoFile != null)
            {

                Uri photoUri = FileProvider.getUriForFile(PosActivity.this,"com.daniel.appdaniel",photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                if (requestCode == PHOTO_REQUEST_CODE)
                {
                    galleryLauncher3.launch(takePictureIntent);
                }
                else if (requestCode == PHOTO_REQUEST_CODE_2)
                {
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
        if (requestCode ==PHOTO_REQUEST_CODE )
        {
            mPhotoPath = "file:" + photoFile.getAbsolutePath();
            mAbsoulutePhotoPath = photoFile.getAbsolutePath();
        }
        else if (requestCode ==PHOTO_REQUEST_CODE_2)
        {
            mPhotoPath2 = "file:" + photoFile.getAbsolutePath();
            mAbsoulutePhotoPath2 = photoFile.getAbsolutePath();
        }

        return photoFile;
    }

    private void clickPos()
    {
        mTitle = mTextInputTitle.getText().toString();
        mDescription = mTextInputDescripcion.getText().toString();

        if(!mTitle.isEmpty() && !mDescription.isEmpty() && !mCategory.isEmpty())
        {
            if (mImagenFile != null && mImagenFile2 != null)
            {
                saveImage(mImagenFile, mImagenFile2);
            }
            else if (mPhotoFile != null && mPhotoFile2 != null)
            {
                saveImage(mPhotoFile,mPhotoFile2);
            }
            else if (mImagenFile != null && mPhotoFile2 != null)
            {
                saveImage(mImagenFile,mPhotoFile2);
            }
            else if (mPhotoFile != null && mImagenFile2 != null)
            {
                saveImage(mPhotoFile,mImagenFile2);
            }
            else
            {
                Toast.makeText(this, "Debes de selecionar una Imagen ", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, "Completa los campos ", Toast.LENGTH_SHORT).show();
        }

        }

    private void saveImage(File imagenFile1 , File imagenFile2) {
        mDialog.show();
        final String imageName1 = UUID.randomUUID().toString();
        final String imageName2 = UUID.randomUUID().toString();

        mImageProvider.save(PosActivity.this, imagenFile1, imageName1).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful())
                {
                    mImageProvider.getmStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                    {
                        @Override
                        public void onSuccess(Uri uri)
                        {
                            final String url = uri.toString();
                            mImageProvider.save(PosActivity.this, imagenFile2, imageName2).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> taskImage2) {
                                    if (taskImage2.isSuccessful())
                                    {
                                        mImageProvider.getmStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri2)
                                            {
                                                String url2 = uri2.toString();
                                                Post post = new Post();
                                                post.setImage1(url);
                                                post.setImage2(url2);
                                                post.setTitle(mTitle);
                                                post.setDescription(mDescription);
                                                post.setCategory(mCategory);
                                                post.setIdUser(mAuthProvider.getUid());
                                                post.setTimestamp(new Date().getTime());
                                                mPostProvider.save(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> taskSave) {
                                                        mDialog.dismiss();
                                                        if (taskSave.isSuccessful()) {
                                                            clearForm();
                                                            Toast.makeText(PosActivity.this, "La informacion se almaceno correctamente", Toast.LENGTH_SHORT).show();
                                                        }
                                                        else {
                                                            Toast.makeText(PosActivity.this, "No se pudo almacenar la informacion", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            }
                                        });
                                    }
                                    else {
                                        mDialog.dismiss();
                                        Toast.makeText(PosActivity.this, "La imagen numero 2 no se pudo guardar", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
                else {
                    mDialog.dismiss();
                    Toast.makeText(PosActivity.this, "Hubo error al almacenar la imagen", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void clearForm() {
        mTextInputTitle.setText("");
        mTextInputDescripcion.setText("");
        mTextViewCategory.setText("CATEGORIAS");
        mImageViewPost1.setImageResource(R.drawable.upload_image);
        mImageViewPost2.setImageResource(R.drawable.upload_image);
        mTitle = "";
        mDescription ="";
        mCategory = "";
        mImagenFile = null;
        mImagenFile2 = null;

    }
    private void operGallery(int galleryRequestCode) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        if (galleryRequestCode == GALLERY_REQUEST_CODE) {
            galleryLauncher.launch(galleryIntent);
        } else if (galleryRequestCode == GALLERY_REQUEST_CODE_2) {
            galleryLauncher2.launch(galleryIntent);
        }
    }




    private void handleGalleryResult(Uri selectedImageUri, int requestCode)
    {
        try
        {
            if (selectedImageUri != null)
            {
                if (requestCode == GALLERY_REQUEST_CODE)
                {
                    mImagenFile = FileUtil.from(PosActivity.this, selectedImageUri);

                    if (mImagenFile != null)
                    {
                        mImageViewPost1.setImageBitmap(BitmapFactory.decodeFile(mImagenFile.getAbsolutePath()));
                    }
                    else
                    {
                        Toast.makeText(PosActivity.this, "Error al obtener el archivo de imagen", Toast.LENGTH_LONG).show();
                    }
                }
                else if (requestCode == GALLERY_REQUEST_CODE_2)
                {
                    mImagenFile2 = FileUtil.from(PosActivity.this, selectedImageUri);
                    if (mImagenFile2 !=null)
                    {
                        mImageViewPost2.setImageBitmap(BitmapFactory.decodeFile(mImagenFile2.getAbsolutePath()));
                    }
                    else
                    {
                        Toast.makeText(PosActivity.this, "Error al obtener el archivo de imagen", Toast.LENGTH_LONG).show();
                    }
                }
            }
        } catch (Exception e)
        {
            Log.d("ERROR", "Se produjo un error " + e.getMessage());
            Toast.makeText(PosActivity.this, "Se produjo un error " + e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }
}