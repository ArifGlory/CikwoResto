package irwan.lampungresto;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

import irwan.lampungresto.Kelas.FoodMenu;
import irwan.lampungresto.Kelas.Resep;
import irwan.lampungresto.Kelas.SharedVariable;

public class TambahResepActivity extends AppCompatActivity {

    ImageView imgBrowse;
    EditText etNama,etDeskripsi,etResepnya,etAlatBahan;
    Button btnUpload;
    public static ProgressBar progressBar;
    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;

    static final int RC_PERMISSION_READ_EXTERNAL_STORAGE = 1;
    static final int RC_IMAGE_GALLERY = 2;
    FirebaseUser fbUser;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_resep);

        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(TambahResepActivity.this);
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fbUser == null) {
            finish();
        }
        ref = FirebaseDatabase.getInstance().getReference();

        imgBrowse = (ImageView) findViewById(R.id.img_browse);
        etNama = (EditText) findViewById(R.id.userEmailId);
        etDeskripsi = (EditText) findViewById(R.id.etHargaSayur);
        etResepnya = (EditText) findViewById(R.id.etResepnya);
        btnUpload = (Button) findViewById(R.id.signUpBtn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        etAlatBahan = (EditText) findViewById(R.id.etAlatDanBahan);

        imgBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*  if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, RC_PERMISSION_READ_EXTERNAL_STORAGE);
                } else {*/
                // Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //intent.setType("image/*");
                //startActivityForResult(intent, RC_IMAGE_GALLERY);
                // }

                if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(TambahResepActivity.this, new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE}, RC_PERMISSION_READ_EXTERNAL_STORAGE);
                } else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, RC_IMAGE_GALLERY);
                }
            }
        });
        etResepnya.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_UP:
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });
        etAlatBahan.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_UP:
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });
    }

    private void matikanKomponen(){
        progressBar.setVisibility(View.VISIBLE);
        etDeskripsi.setEnabled(false);
        etResepnya.setEnabled(false);
        etNama.setEnabled(false);
        imgBrowse.setEnabled(false);
        etAlatBahan.setEnabled(false);
    }

    private void hidupkanKomponen(){
        progressBar.setVisibility(View.GONE);
        etDeskripsi.setEnabled(true);
        etResepnya.setEnabled(true);
        etNama.setEnabled(true);
        imgBrowse.setEnabled(true);
        etAlatBahan.setEnabled(true);
    }

    private void checkValidation(){
        String getNama = etNama.getText().toString();
        String getHarga = etDeskripsi.getText().toString();
        String getResepnya = etResepnya.getText().toString();
        String getAlatBahan = etAlatBahan.getText().toString();
        matikanKomponen();

        if (getNama.equals("") || getNama.length() == 0
                || getHarga.equals("") || getHarga.length() == 0
                || getResepnya.equals("") || getResepnya.length() == 0
                || getAlatBahan.equals("") || getAlatBahan.length() == 0
                ) {

            customToast("Semua Field harus diisi");
            hidupkanKomponen();
        }else if (uri == null){
            customToast("Pilih gambar resep dahulu");
            hidupkanKomponen();
        }else {
            matikanKomponen();
            uploadGambar(uri);
            hidupkanKomponen();
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
            final String tipe = GetMimeType(TambahResepActivity.this,uri);
            //Toast.makeText(TambahMenuActivity.this, "Tipe : !\n" + tipe, Toast.LENGTH_LONG).show();

            imgBrowse.setImageURI(uri);
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
                Toast.makeText(TambahResepActivity.this, "Upload failed!\n" + exception.getMessage(), Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Toast.makeText(TambahResepActivity.this, "Upload finished!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                // save image to database
                String key = ref.child("resto").child(SharedVariable.userID).child("resepList").push().getKey();
                Resep resep = new Resep(etNama.getText().toString(),
                        etDeskripsi.getText().toString(),
                        key,
                        downloadUrl.toString(),
                        etResepnya.getText().toString(),
                        etAlatBahan.getText().toString());
                ref.child("resto").child(SharedVariable.userID).child("resepList").child(key).setValue(resep);

                etDeskripsi.setText("");
                etResepnya.setText("");
                etNama.setText("");
                etAlatBahan.setText("");
                imgBrowse.setImageResource(R.drawable.ic_browse);
            }
        });
    }
}
