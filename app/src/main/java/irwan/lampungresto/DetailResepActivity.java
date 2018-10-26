package irwan.lampungresto;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

import irwan.lampungresto.Kelas.SharedVariable;

public class DetailResepActivity extends AppCompatActivity {

    ImageView imgBrowse;
    EditText etNama,etDeskripsi,etResepnya,etAlatDanBahan;
    Button btnUpload;
    public static ProgressBar progressBar;
    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;

    static final int RC_PERMISSION_READ_EXTERNAL_STORAGE = 1;
    static final int RC_IMAGE_GALLERY = 2;
    FirebaseUser fbUser;
    Uri uri;
    private Boolean isFabOpen = false;
    private String namaResep,deskripsiResep,detailResep,downloadURL,keyResep,alatBahan;
    Intent i;
    DialogInterface.OnClickListener listener;

    FloatingActionButton fabSetting,fabEdit,fabDelete;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_resep);


        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(DetailResepActivity.this);
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fbUser == null) {
            finish();
        }

        i = getIntent();
        namaResep = i.getStringExtra("nama");
        deskripsiResep = i.getStringExtra("deskripsi");
        downloadURL = i.getStringExtra("url");
        keyResep = i.getStringExtra("key");
        detailResep = i.getStringExtra("detail");
        alatBahan = i.getStringExtra("alatBahan");

        ref = FirebaseDatabase.getInstance().getReference();

        imgBrowse = (ImageView) findViewById(R.id.img_browse);
        etNama = (EditText) findViewById(R.id.userEmailId);
        etDeskripsi = (EditText) findViewById(R.id.etHargaSayur);
        btnUpload = (Button) findViewById(R.id.signUpBtn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        etResepnya = (EditText) findViewById(R.id.etResepnya);
        etAlatDanBahan = (EditText) findViewById(R.id.etAlatDanBahan);

        etDeskripsi.setEnabled(false);
        etNama.setEnabled(false);
        btnUpload.setEnabled(false);
        etResepnya.setEnabled(false);
        etAlatDanBahan.setEnabled(false);

        fabDelete = (FloatingActionButton) findViewById(R.id.fabDelete);
        fabEdit = (FloatingActionButton) findViewById(R.id.fabEdit);
        fabSetting = (FloatingActionButton) findViewById(R.id.fabSetting);
        fabSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFB();
            }
        });
        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etNama.setEnabled(true);
                etDeskripsi.setEnabled(true);
                etResepnya.setEnabled(true);
                etAlatDanBahan.setEnabled(true);
                btnUpload.setText("Ubah");
                btnUpload.setEnabled(true);
                imgBrowse.setEnabled(true);
                animateFB();
            }
        });

        fab_open = AnimationUtils.loadAnimation(this,R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(this,R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(this,R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(this,R.anim.rotate_backward);

        etNama.setText(namaResep);
        etDeskripsi.setText(deskripsiResep);
        etResepnya.setText(detailResep);
        etAlatDanBahan.setText(alatBahan);
        Glide.with(getApplicationContext())
                .load(downloadURL)
                .asBitmap().fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgBrowse);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });
        imgBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DetailResepActivity.this, new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE}, RC_PERMISSION_READ_EXTERNAL_STORAGE);
                } else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, RC_IMAGE_GALLERY);
                }
            }
        });

        fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailResepActivity.this);
                builder.setMessage("Anda yakin ingin menghapus resep ini ?");
                builder.setCancelable(false);

                listener = new DialogInterface.OnClickListener()
                {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == DialogInterface.BUTTON_POSITIVE){
                            ref.child("resto").child(SharedVariable.userID).child("resepList").child(keyResep).setValue(null);
                            i = new Intent(getApplicationContext(),ListResepActivity.class);
                            startActivity(i);
                        }

                        if(which == DialogInterface.BUTTON_NEGATIVE){
                            dialog.cancel();
                        }
                    }
                };
                builder.setPositiveButton("Ya",listener);
                builder.setNegativeButton("Tidak", listener);
                builder.show();
            }
        });
    }

    public void animateFB(){

        if(isFabOpen){

            fabSetting.startAnimation(rotate_backward);
            fabEdit.startAnimation(fab_close);
            fabDelete.startAnimation(fab_close);
            fabEdit.setClickable(false);
            fabDelete.setClickable(false);
            isFabOpen = false;
            Log.d("fab", "close");

        } else {

            fabSetting.startAnimation(rotate_forward);
            fabEdit.startAnimation(fab_open);
            fabDelete.startAnimation(fab_open);
            fabEdit.setClickable(true);
            fabDelete.setClickable(true);
            isFabOpen = true;
            Log.d("fab","open");

        }
    }

    @Override
    public void onBackPressed() {
        if (uri != null || !namaResep.equals(etNama.getText().toString()) || !deskripsiResep.equals(etDeskripsi.getText().toString())
                || !detailResep.equals(etResepnya.getText().toString())  ){
            i = new Intent(getApplicationContext(),ListResepActivity.class);
            startActivity(i);
        }
        super.onBackPressed();
    }

    private void matikanKomponen(){
        progressBar.setVisibility(View.VISIBLE);
        etDeskripsi.setEnabled(false);
        etResepnya.setEnabled(false);
        etNama.setEnabled(false);
        imgBrowse.setEnabled(false);
        fabSetting.setEnabled(false);
        fabEdit.setEnabled(false);
        fabDelete.setEnabled(false);
        etAlatDanBahan.setEnabled(false);
    }

    private void hidupkanKomponen(){
        progressBar.setVisibility(View.GONE);
        etResepnya.setEnabled(true);
        etDeskripsi.setEnabled(true);
        etNama.setEnabled(true);
        imgBrowse.setEnabled(true);
        fabSetting.setEnabled(true);
        fabEdit.setEnabled(true);
        fabDelete.setEnabled(true);
        etAlatDanBahan.setEnabled(true);
    }

    private void checkValidation(){
        String getNama = etNama.getText().toString();
        String getDeskripsi = etDeskripsi.getText().toString();
        String getDetail = etResepnya.getText().toString();
        String getAlatbahan = etAlatDanBahan.getText().toString();
        // matikanKomponen();

        if (getNama.equals("") || getNama.length() == 0
                || getDeskripsi.equals("") || getDeskripsi.length() == 0
                || getDetail.equals("") || getDetail.length() == 0
                || getAlatbahan.equals("") || getAlatbahan.length() == 0) {

            customToast("Semua Field harus diisi");
            hidupkanKomponen();
        }else if (uri == null){
            //ke proses untuk update tapi tanpa ganti URl Gambar
            updateWithoutChangeURI(etNama.getText().toString(),etDeskripsi.getText().toString(),etResepnya.getText().toString(),
                    etAlatDanBahan.getText().toString());
        }else {

            uploadGambar(uri);

        }
    }

    private void uploadGambar(final Uri uri){

        progressBar.setVisibility(View.VISIBLE);
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child("images");
        StorageReference userRef = imagesRef.child(fbUser.getUid());
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = fbUser.getUid() + "_" + timeStamp;
        StorageReference fileRef = userRef.child(filename);

        UploadTask uploadTask = fileRef.putFile(uri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(DetailResepActivity.this, "Upload failed!\n" + exception.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Toast.makeText(DetailResepActivity.this, "Upload finished!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                // save image to database

                String nm = etNama.getText().toString();
                String desk = etDeskripsi.getText().toString();
                String detail = etResepnya.getText().toString();

                ref.child("resto").child(SharedVariable.userID).child("resepList").child(keyResep).child("namaResep").setValue(etNama.getText().toString());
                ref.child("resto").child(SharedVariable.userID).child("resepList").child(keyResep).child("deskripsi").setValue(etDeskripsi.getText().toString());
                ref.child("resto").child(SharedVariable.userID).child("resepList").child(keyResep).child("downloadUrl").setValue(downloadUrl.toString());
                ref.child("resto").child(SharedVariable.userID).child("resepList").child(keyResep).child("detailResep").setValue(etResepnya.getText().toString());
                ref.child("resto").child(SharedVariable.userID).child("resepList").child(keyResep).child("alatBahan").setValue(etAlatDanBahan.getText().toString());

                customToast("Berhasil Diubah");
                progressBar.setVisibility(View.GONE);
                etDeskripsi.setText(desk);
                etResepnya.setText(detail);
                etNama.setText(nm);

                etNama.setEnabled(false);
                etDeskripsi.setEnabled(false);
                etResepnya.setEnabled(false);
                btnUpload.setEnabled(false);
                btnUpload.setText("......");
                etAlatDanBahan.setEnabled(false);
            }
        });
    }

    private void updateWithoutChangeURI(String nama,String deskripsiRes,String detailRes,String alatdanbahan){
        progressBar.setVisibility(View.VISIBLE);
        ref.child("resto").child(SharedVariable.userID).child("resepList").child(keyResep).child("namaResep").setValue(nama);
        ref.child("resto").child(SharedVariable.userID).child("resepList").child(keyResep).child("deskripsi").setValue(deskripsiRes);
        ref.child("resto").child(SharedVariable.userID).child("resepList").child(keyResep).child("detailResep").setValue(detailRes);
        ref.child("resto").child(SharedVariable.userID).child("resepList").child(keyResep).child("alatBahan").setValue(alatdanbahan);

        customToast("Berhasil Diubah");
        progressBar.setVisibility(View.GONE);
        etDeskripsi.setText(deskripsiRes);
        etResepnya.setText(detailRes);
        etNama.setText(nama);
        etAlatDanBahan.setText(alatdanbahan);

        etNama.setEnabled(false);
        etDeskripsi.setEnabled(false);
        etResepnya.setEnabled(false);
        btnUpload.setEnabled(false);
        btnUpload.setText("......");
        etAlatDanBahan.setEnabled(false);
    }


    public static String GetMimeType(Context context, Uri uriImage)
    {
        String strMimeType = null;

        Cursor cursor = context.getContentResolver().query(uriImage,
                new String[] { MediaStore.MediaColumns.MIME_TYPE },
                null, null, null);

        if (cursor != null && cursor.moveToNext())
        {
            strMimeType = cursor.getString(0);
        }

        return strMimeType;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RC_PERMISSION_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, RC_IMAGE_GALLERY);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_IMAGE_GALLERY && resultCode == RESULT_OK) {
            uri = data.getData();
            final String tipe = GetMimeType(DetailResepActivity.this,uri);
            //Toast.makeText(TambahMenuActivity.this, "Tipe : !\n" + tipe, Toast.LENGTH_LONG).show();

            imgBrowse.setImageURI(uri);
        }
    }

    public  void customToast(String s){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.toast_root));

        TextView text = (TextView) layout.findViewById(R.id.toast_error);
        text.setText(s);
        Toast toast = new Toast(getApplicationContext());// Get Toast Context
        toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);// Set
        toast.setDuration(Toast.LENGTH_SHORT);// Set Duration
        toast.setView(layout); // Set Custom View over toast
        toast.show();// Finally show toast
    }
}
