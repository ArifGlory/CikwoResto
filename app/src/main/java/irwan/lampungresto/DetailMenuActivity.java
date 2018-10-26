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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import irwan.lampungresto.Kelas.FoodMenu;
import irwan.lampungresto.Kelas.SharedVariable;

public class DetailMenuActivity extends AppCompatActivity {

    ImageView imgBrowse;
    EditText etNama,etHarga;
    TextView txtRating;
    Button btnUpload,btnKeKomentar;
    public static ProgressBar progressBar;
    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    public static List<String> listRating = new ArrayList();

    static final int RC_PERMISSION_READ_EXTERNAL_STORAGE = 1;
    static final int RC_IMAGE_GALLERY = 2;
    FirebaseUser fbUser;
    Uri uri;
    private Boolean isFabOpen = false;
    private String namaMenu,hargaMenu,downloadURL,keyMenu;
    Intent i;
    DialogInterface.OnClickListener listener;
    Double ratingMenu;

    FloatingActionButton fabSetting,fabEdit,fabDelete;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_menu);

        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(DetailMenuActivity.this);
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fbUser == null) {
            finish();
        }

        i = getIntent();
        namaMenu = i.getStringExtra("nama");
        hargaMenu = i.getStringExtra("harga");
        downloadURL = i.getStringExtra("url");
        keyMenu = i.getStringExtra("key");

        ref = FirebaseDatabase.getInstance().getReference();

        imgBrowse = (ImageView) findViewById(R.id.img_browse);
        etNama = (EditText) findViewById(R.id.userEmailId);
        etHarga = (EditText) findViewById(R.id.etHargaSayur);
        btnUpload = (Button) findViewById(R.id.signUpBtn);
        btnKeKomentar = (Button) findViewById(R.id.btnKeKomentar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtRating = (TextView) findViewById(R.id.txtRating);

        etHarga.setEnabled(false);
        etNama.setEnabled(false);
        btnUpload.setEnabled(false);

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
                etHarga.setEnabled(true);
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

        etNama.setText(namaMenu);
        etHarga.setText(hargaMenu);
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
                    ActivityCompat.requestPermissions(DetailMenuActivity.this, new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE}, RC_PERMISSION_READ_EXTERNAL_STORAGE);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailMenuActivity.this);
                builder.setMessage("Anda yakin ingin menghapus menu ini ?");
                builder.setCancelable(false);

                listener = new DialogInterface.OnClickListener()
                {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == DialogInterface.BUTTON_POSITIVE){
                            ref.child("resto").child(SharedVariable.userID).child("menuList").child(keyMenu).setValue(null);
                            i = new Intent(getApplicationContext(),HomeRestoActivity.class);
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
        btnKeKomentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(getApplicationContext(),KomentarActivity.class);
                i.putExtra("key",keyMenu);
                startActivity(i);
            }
        });

        ref.child("resto").child(SharedVariable.userID).child("menuList").child(keyMenu).child("rating").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    progressBar.setVisibility(View.VISIBLE);
                    Double ratingGabungan = 0.0;

                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        String nilai = child.child("nilai").getValue().toString();
                        listRating.add(nilai);
                    }


                    if (!listRating.isEmpty()){
                        for (int c=0;c<listRating.size();c++){
                            Double rate = Double.parseDouble(listRating.get(c).toString());
                            ratingGabungan = ratingGabungan + rate;
                        }
                        ratingMenu = ratingGabungan / Double.parseDouble(String.valueOf(listRating.size()));
                        txtRating.setText(String.valueOf(ratingMenu));
                    }else {
                      ratingMenu =  ratingGabungan;
                        txtRating.setText(String.valueOf(ratingMenu));
                    }
                    progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        if (uri != null || !namaMenu.equals(etNama.getText().toString()) || !hargaMenu.equals(etHarga.getText().toString()) ){
            i = new Intent(getApplicationContext(),HomeRestoActivity.class);
            startActivity(i);
        }
        super.onBackPressed();
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

    private void matikanKomponen(){
        progressBar.setVisibility(View.VISIBLE);
        etHarga.setEnabled(false);
        etNama.setEnabled(false);
        imgBrowse.setEnabled(false);
        fabSetting.setEnabled(false);
        fabEdit.setEnabled(false);
        fabDelete.setEnabled(false);
    }

    private void hidupkanKomponen(){
        progressBar.setVisibility(View.GONE);
        etHarga.setEnabled(true);
        etNama.setEnabled(true);
        imgBrowse.setEnabled(true);
        fabSetting.setEnabled(true);
        fabEdit.setEnabled(true);
        fabDelete.setEnabled(true);
    }

    private void checkValidation(){
        String getNama = etNama.getText().toString();
        String getHarga = etHarga.getText().toString();
       // matikanKomponen();

        if (getNama.equals("") || getNama.length() == 0
                || getHarga.equals("") || getHarga.length() == 0) {

            customToast("Harga dan Nama menu harus diisi");
            hidupkanKomponen();
        }else if (uri == null){
           //ke proses untuk update tapi tanpa ganti URl Gambar
            updateWithoutChangeURI(etNama.getText().toString(),etHarga.getText().toString());
        }else {

            uploadGambar(uri);

        }
    }

    private void updateWithoutChangeURI(String nama,String harga){
        progressBar.setVisibility(View.VISIBLE);
        ref.child("resto").child(SharedVariable.userID).child("menuList").child(keyMenu).child("namaMenu").setValue(nama);
        ref.child("resto").child(SharedVariable.userID).child("menuList").child(keyMenu).child("harga").setValue(harga);

        customToast("Berhasil Diubah");
        progressBar.setVisibility(View.GONE);
        etHarga.setText(harga);
        etNama.setText(nama);

        etNama.setEnabled(false);
        etHarga.setEnabled(false);
        btnUpload.setEnabled(false);
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
                Toast.makeText(DetailMenuActivity.this, "Upload failed!\n" + exception.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Toast.makeText(DetailMenuActivity.this, "Upload finished!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                // save image to database

                String nm = etNama.getText().toString();
                String harga = etHarga.getText().toString();

                ref.child("resto").child(SharedVariable.userID).child("menuList").child(keyMenu).child("namaMenu").setValue(etNama.getText().toString());
                ref.child("resto").child(SharedVariable.userID).child("menuList").child(keyMenu).child("harga").setValue(etHarga.getText().toString());
                ref.child("resto").child(SharedVariable.userID).child("menuList").child(keyMenu).child("downloadUrl").setValue(downloadUrl.toString());

                customToast("Berhasil Diubah");
                progressBar.setVisibility(View.GONE);
                etHarga.setText(harga);
                etNama.setText(nm);

                etNama.setEnabled(false);
                etHarga.setEnabled(false);
                btnUpload.setEnabled(false);
                btnUpload.setText("......");
            }
        });
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
            final String tipe = GetMimeType(DetailMenuActivity.this,uri);
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
