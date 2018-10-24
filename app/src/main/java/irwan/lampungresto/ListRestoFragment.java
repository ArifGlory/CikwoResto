package irwan.lampungresto;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import irwan.lampungresto.Adapter.AdapterResto;
import irwan.lampungresto.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListRestoFragment extends Fragment {
    View view;
    private ArrayList<String> NamaREsto = new ArrayList<String>();
    private ArrayList<String> AlamatResto = new ArrayList<String>();
    private ArrayList<String> KeyResto = new ArrayList<String>();


    private ArrayList<String> NamaREstoCari = new ArrayList<String>();
    private ArrayList<String> AlamatRestoCari = new ArrayList<String>();
    private ArrayList<String> KeyRestoCari = new ArrayList<String>();

    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    Firebase firebase;
    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;

    public ListRestoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list_resto, container, false);

        Firebase.setAndroidContext(view.getContext());
        FirebaseApp.initializeApp(view.getContext());
        ref = FirebaseDatabase.getInstance().getReference();

        ref.child("resto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!NamaREsto.isEmpty()){
                    NamaREsto.clear();
                }
                if(!AlamatResto.isEmpty()){
                    AlamatResto.clear();
                }
                if(!KeyResto.isEmpty()){
                    KeyResto.clear();
                }

                for(DataSnapshot child : dataSnapshot.getChildren()){
                    //Toast.makeText(view.getContext(),""+child.child("displayName").getValue().toString(),Toast.LENGTH_SHORT).show();
                    NamaREsto.add(child.child("displayName").getValue().toString());
                    AlamatResto.add(child.child("alamat").getValue().toString());
                    KeyResto.add(child.getKey());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        rvView = view.findViewById(R.id.rv_main_barang);
        rvView.setHasFixedSize(true);

        rvView.setLayoutManager(new GridLayoutManager(view.getContext(), 1));
        adapter = new AdapterResto(view.getContext(),NamaREsto,AlamatResto,KeyResto);
        rvView.setAdapter(adapter);

        final SearchView searchView = view.findViewById(R.id.svCari);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!newText.isEmpty()) {
                    //Toast.makeText(view.getContext(), "" + newText, Toast.LENGTH_SHORT).show();
                    if(!NamaREstoCari.isEmpty()){
                        NamaREstoCari.clear();
                    }
                    if(!AlamatRestoCari.isEmpty()){
                        AlamatRestoCari.clear();
                    }
                    if(!KeyRestoCari.isEmpty()){
                        KeyRestoCari.clear();
                    }


                    for(int a =0; a < NamaREsto.size(); a++){
                        if(NamaREsto.get(a).toLowerCase().contains(newText.toLowerCase()) || AlamatResto    .get(a).toLowerCase().contains(newText.toLowerCase())){
                            //Toast.makeText(view.getContext(), "" + namabarang.get(a).toString(), Toast.LENGTH_SHORT).show();
                            NamaREstoCari.add(NamaREsto.get(a));
                            AlamatRestoCari.add(AlamatResto.get(a));
                            KeyRestoCari.add(KeyResto.get(a));
                        }
                    }
                    adapter = new AdapterResto(view.getContext(),NamaREstoCari,AlamatRestoCari,KeyRestoCari);
                    rvView.setAdapter(adapter);
                }else{
                    adapter = new AdapterResto(view.getContext(),NamaREsto,AlamatResto,KeyResto);
                    rvView.setAdapter(adapter);
                }
                return false;
            }
        });

        return  view;
    }

}
