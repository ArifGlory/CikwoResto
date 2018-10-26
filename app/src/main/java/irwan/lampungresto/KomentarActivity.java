package irwan.lampungresto;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class KomentarActivity extends AppCompatActivity {
    String getKey;
    private ActionBar actionBar;
    DatabaseReference ref,ref2;
    ListView listView;
    ArrayList<String> komen = new ArrayList<String>();
    EditText txtKomen;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_komentar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        actionBar = getSupportActionBar();
        actionBar.setTitle("Cikwo Resto");
        actionBar.setSubtitle("Halaman Komentar");

        txtKomen = (EditText) findViewById(R.id.txtkomentar);

        getKey = getIntent().getExtras().getString("key");
      //  Toast.makeText(KomentarActivity.this,getKey,Toast.LENGTH_SHORT).show();

        listView = (ListView) findViewById(R.id.listView);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("resto/ugsNmb5ix7hgSqNjFgAXUec39zv1/menuList/" + getKey);
        ref.child("komentar").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!komen.isEmpty()){
                    komen.clear();
                }
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    //Toast.makeText(KomentarActivity.this,""+child.child("komentar").getValue().toString(),Toast.LENGTH_SHORT).show();
                    komen.add(child.child("email").getValue().toString()+" : \n"+""+child.child("komentar").getValue().toString()+"( "+child.child("tanggal").getValue().toString()+" )");
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(KomentarActivity.this, android.R.layout.simple_list_item_1, komen);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public void komen(View view) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String waktu = dateFormat.format(new Date()); // Find todays date

        Map mParent = new HashMap();
        mParent.put("uid", "" + firebaseAuth.getCurrentUser().getUid());
        mParent.put("email", "" + firebaseAuth.getCurrentUser().getEmail());
        mParent.put("tanggal", "" + waktu);
        mParent.put("komentar", "" + txtKomen.getText().toString());

        ref.child("komentar").push().setValue(mParent);
        txtKomen.setText(null);
    }
}
