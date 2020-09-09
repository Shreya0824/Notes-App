package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    CircleImageView profileImageView;
    TextInputEditText displayNameEditText;
    Button updateProfileButton,button8;
    ProgressBar progressBar;

    String DISPLAY_NAME = null;
    String PROFILE_IMAGE_URL = null;
    //int TAKE_IMAGE_CODE = 10001;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

         profileImageView = findViewById(R.id.profileImageView);
         displayNameEditText = findViewById(R.id.displayNameEditText);
        updateProfileButton = findViewById(R.id.updateProfileButton);
        progressBar = findViewById(R.id.progressBar);
        button8 = findViewById(R.id.button8);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            Log.d(TAG, "onCreate: " + user.getDisplayName());
            if (user.getDisplayName() != null) {
                displayNameEditText.setText(user.getDisplayName());
                displayNameEditText.setSelection(user.getDisplayName().length());
            }
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl())
                        .into(profileImageView);
            }
        }
        
        progressBar.setVisibility(View.GONE);
        
    }

    public void updateProfile(final View view) {

        view.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        
        DISPLAY_NAME = displayNameEditText.getText().toString();
        
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                .setDisplayName(DISPLAY_NAME)
                .build();

        firebaseUser.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        view.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ProfileActivity.this, "Succesfully updated profile", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        view.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                        Log.e(TAG, "onFailure: ", e.getCause());
                    }
                });
        
    }

    FirebaseStorage firebaseStorage= FirebaseStorage.getInstance();
    private Uri fileUri;
    Uri filePath=null;
    public void handleImageClick(View view) {
        final FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
        final FirebaseFirestore firebaseFirestore =FirebaseFirestore.getInstance();
        firebaseStorage.getReference().child(firebaseAuth.getUid().toString()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if(task.isSuccessful()){
                    Glide.with(ProfileActivity.this).load(task.getResult()).into(profileImageView);
                }
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK , MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();


        }

            updateProfileButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!(filePath==null)){
                        final FirebaseAuth firebaseAuth= FirebaseAuth.getInstance();
                        final FirebaseFirestore firebaseFirestore =FirebaseFirestore.getInstance();
                    FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
                    firebaseStorage.getReference().child(firebaseAuth.getCurrentUser().getUid()).putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            Glide.with(ProfileActivity.this).load(task.getResult()).fitCenter().into(profileImageView);

                        }
                    }

                    );

                }
            }

        });

//    private void handleUpload(Bitmap bitmap) {
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//
//        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        final StorageReference reference = FirebaseStorage.getInstance().getReference()
//                .child("profileImages")
//                .child(uid + ".jpeg");
//
//        reference.putBytes(baos.toByteArray())
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        getDownloadUrl(reference);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e(TAG, "onFailure: ",e.getCause() );
//                    }
//                });
//    }

//    private void getDownloadUrl(StorageReference reference) {
//        reference.getDownloadUrl()
//                .addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        Log.d(TAG, "onSuccess: " + uri);
//                        setUserProfileUrl(uri);
//                    }
//                });
//    }
//
//    private void setUserProfileUrl(Uri uri) {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
//                .setPhotoUri(uri)
//                .build();

//        user.updateProfile(request)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Toast.makeText(ProfileActivity.this, "Updated succesfully", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(ProfileActivity.this, "Profile image failed...", Toast.LENGTH_SHORT).show();
//                    }
//                });
  }
}



















