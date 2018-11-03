package irwan.lampungresto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import irwan.lampungresto.Adapter.AdapterKeranjang;
import irwan.lampungresto.Kelas.SharedVariable;

public class DetailOrderActivity extends AppCompatActivity {

    String Faktur;
    Firebase firebase;
    DatabaseReference refMenu,refOrder,reGambar,refRek;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    TextView txt_faktur,txt_tanggal,txt_status,txt_NoRek;

    private ArrayList<String> NamaMenu = new ArrayList<String>();
    private ArrayList<String> HargaMenu = new ArrayList<String>();
    private ArrayList<String> UrlGambar = new ArrayList<String>();
    private ArrayList<String> Jumlah = new ArrayList<String>();
    private ArrayList<String> keykunci = new ArrayList<String>();
    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    TextView txt_total;
    int subtotal,total;
    Button btn_order;

    //a constant to track the file chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;
    private StorageReference mStorageRef;
    //ImageView
    private ImageView imageView;

    //a Uri object to store file path
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        txt_faktur = (TextView) findViewById(R.id.txt_faktur);
        txt_tanggal = (TextView) findViewById(R.id.txt_tanggal);
        txt_status = (TextView) findViewById(R.id.txt_status);
        txt_total = (TextView) findViewById(R.id.txt_total);
        txt_NoRek = (TextView) findViewById(R.id.txt_NoRek);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        Faktur = getIntent().getExtras().getString("faktur");
        Firebase.setAndroidContext(DetailOrderActivity.this);
        refOrder = FirebaseDatabase.getInstance().getReference();
        refMenu = FirebaseDatabase.getInstance().getReference();
        reGambar = FirebaseDatabase.getInstance().getReference();
        refRek = FirebaseDatabase.getInstance().getReference();
        refOrder.child("order").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot child : dataSnapshot.getChildren()){
                    if(child.getKey().equals(Faktur)) {
                        txt_faktur.setText("" + child.getKey());
                        txt_tanggal.setText("" + child.child("tanggal").getValue());
                        if (child.child("status").getValue().equals("1")) {
                            txt_status.setText("Menunggu Verifikasi");
                        }
                        if (child.child("status").getValue().equals("2")) {
                            txt_status.setText("Sedang diproses");
                        }
                        if (child.child("status").getValue().equals("3")) {
                            txt_status.setText("Transaksi Selesai");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        refRek.child("resto").child(SharedVariable.uIDCikwo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String noRek = dataSnapshot.child("noRekening").getValue().toString();
                txt_NoRek.setText(noRek);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        refOrder.child("order_detail").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                total = 0;
                if(!NamaMenu.isEmpty()){
                    NamaMenu.clear();
                }
                if(!HargaMenu.isEmpty()){
                    HargaMenu.clear();
                }
                if(!UrlGambar.isEmpty()){
                    UrlGambar.clear();
                }
                if(!Jumlah.isEmpty()){
                    Jumlah.clear();
                }
                for(DataSnapshot child2 : dataSnapshot.getChildren()){
                    if(child2.child("id_order").getValue().equals(Faktur)) {
                        //Toast.makeText(view.getContext(), "" + child2.child("namaMenu").getValue(), Toast.LENGTH_SHORT).show();
                        NamaMenu.add(child2.child("nama_menu").getValue().toString());
                        HargaMenu.add(child2.child("harga").getValue().toString());
                        UrlGambar.add(child2.child("url_gambar").getValue().toString());
                        Jumlah.add(child2.child("jumlah").getValue().toString());
                        subtotal = Integer.parseInt(child2.child("harga").getValue().toString()) * Integer.parseInt(child2.child("jumlah").getValue().toString());
                        total+=subtotal;
                    }
                }

                adapter.notifyDataSetChanged();
                txt_total.setText("Rp "+getMoney(""+total)+",00");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        rvView = (RecyclerView) findViewById(R.id.rv_main_keranjang);
        rvView.setHasFixedSize(true);

        rvView.setLayoutManager(new GridLayoutManager(DetailOrderActivity.this, 1));
        adapter = new AdapterKeranjang(DetailOrderActivity.this,NamaMenu,HargaMenu,UrlGambar,Jumlah);
        rvView.setAdapter(adapter);


    }

    private String getMoney(String str2) {
        StringBuilder str = new StringBuilder(str2);
        int idx = str.length() - 3;
        while (idx > 0) {
            str.insert(idx, ".");
            idx = idx - 3;
        }
        return str.toString();
    }

    public void upload(View view) {
        //Toast.makeText(DetailOrderActivity.this,""+Faktur,Toast.LENGTH_SHORT).show();
        showFileChooser();
//        uploadFile();
    }
    //this method will upload the file
    private void uploadFile() {
        //if there is a file to upload
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            Random rand = new Random();
            int n = rand.nextInt(20);
            StorageReference riversRef = mStorageRef.child("images/bukti_bayar/"+Faktur+".jpg");
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File berhasil diupload....", Toast.LENGTH_LONG).show();
                            final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                            Map mParent = new HashMap();
                            mParent.put("uid", "" + firebaseAuth.getCurrentUser().getUid());
                            mParent.put("url_bukti", "" + downloadUrl);
                            reGambar.child("bukti_bayar").child(Faktur).setValue(mParent);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            uploadFile();
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
}
