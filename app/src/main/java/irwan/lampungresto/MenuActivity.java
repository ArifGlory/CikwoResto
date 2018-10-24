package irwan.lampungresto;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import irwan.lampungresto.Adapter.AdapterMenu;
import irwan.lampungresto.Adapter.AdapterResto;

public class MenuActivity extends AppCompatActivity {
    Firebase firebase;
    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;

    private ArrayList<String> NamaMenu = new ArrayList<String>();
    private ArrayList<String> HargaMenu= new ArrayList<String>();
    private ArrayList<String> UrlGambar = new ArrayList<String>();
    private ArrayList<String> kunciMenu = new ArrayList<String>();

    private ArrayList<String> NamaMenuCari = new ArrayList<String>();
    private ArrayList<String> HargaMenuCari = new ArrayList<String>();
    private ArrayList<String> UrlGambarCari = new ArrayList<String>();
    private ArrayList<String> kunciMenuCari = new ArrayList<String>();

    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public static String resto_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //Toast.makeText(MenuActivity.this,""+getIntent().getExtras().getString("key"),Toast.LENGTH_SHORT).show();
        resto_id = getIntent().getExtras().getString("key");
        Firebase.setAndroidContext(MenuActivity.this);
        FirebaseApp.initializeApp(MenuActivity.this);
        ref = FirebaseDatabase.getInstance().getReference();
        ref.child("resto").child(getIntent().getExtras().getString("key")).child("menuList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!NamaMenu.isEmpty()){
                    NamaMenu.clear();
                }
                if(!HargaMenu.isEmpty()){
                    HargaMenu.clear();
                }
                if(!UrlGambar.isEmpty()){
                    UrlGambar.clear();
                }
                if(!kunciMenu.isEmpty()){
                    kunciMenu.clear();
                }

                for(DataSnapshot child : dataSnapshot.getChildren()){
                    //Toast.makeText(MenuActivity.this,""+child.child("downloadUrl").getValue().toString(),Toast.LENGTH_SHORT).show();
                    NamaMenu.add(child.child("namaMenu").getValue().toString());
                    HargaMenu.add(child.child("harga").getValue().toString());
                    UrlGambar.add(child.child("downloadUrl").getValue().toString());
                    kunciMenu.add(child.getKey());
                }

                adapter.notifyDataSetChanged();
                //Toast.makeText(MenuActivity.this,""+UrlGambar.size(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        rvView = (RecyclerView) findViewById(R.id.rv_main_menu);
        rvView.setHasFixedSize(true);

        rvView.setLayoutManager(new GridLayoutManager(MenuActivity.this, 1));
        adapter = new AdapterMenu(MenuActivity.this,NamaMenu,HargaMenu,UrlGambar,kunciMenu);
        rvView.setAdapter(adapter);

        final SearchView searchView = (SearchView) findViewById(R.id.svCari);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.isEmpty()) {
                    //Toast.makeText(view.getContext(), "" + newText, Toast.LENGTH_SHORT).show();
                    if(!NamaMenuCari.isEmpty()){
                        NamaMenuCari.clear();
                    }
                    if(!HargaMenuCari.isEmpty()){
                        HargaMenuCari.clear();
                    }
                    if(!UrlGambarCari.isEmpty()){
                        UrlGambarCari.clear();
                    }
                    if(!kunciMenuCari.isEmpty()){
                        kunciMenuCari.clear();
                    }



                    for(int a =0; a < NamaMenu.size(); a++){
                        if(NamaMenu.get(a).toLowerCase().contains(newText.toLowerCase())){
                            //Toast.makeText(view.getContext(), "" + namabarang.get(a).toString(), Toast.LENGTH_SHORT).show();
                            NamaMenuCari.add(NamaMenu.get(a));
                            HargaMenuCari.add(HargaMenu.get(a));
                            UrlGambarCari.add(UrlGambar.get(a));
                            kunciMenuCari.add(kunciMenu.get(a));
                        }
                    }
                    adapter = new AdapterMenu(MenuActivity.this,NamaMenuCari,HargaMenuCari,UrlGambarCari,kunciMenuCari);
                    rvView.setAdapter(adapter);
                }else{
                    adapter = new AdapterMenu(MenuActivity.this,NamaMenu,HargaMenu,UrlGambar,kunciMenu);
                    rvView.setAdapter(adapter);
                }
                return false;
            }
        });

    }
}
