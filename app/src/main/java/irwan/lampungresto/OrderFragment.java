package irwan.lampungresto;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import irwan.lampungresto.Adapter.AdapterOrder;
import irwan.lampungresto.Adapter.AdapterResto;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {
    private ArrayList<String> Faktur = new ArrayList<String>();
    private ArrayList<String> Tanggal = new ArrayList<String>();
    private ArrayList<String> Total = new ArrayList<String>();

    private ArrayList<String> FakturCari = new ArrayList<String>();
    private ArrayList<String> TanggalCari = new ArrayList<String>();
    private ArrayList<String> TotalCari = new ArrayList<String>();

    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;


    Firebase firebase;
    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;


    public OrderFragment() {
        // Required empty public constructor
    }
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_order, container, false);

        Firebase.setAndroidContext(view.getContext());
        FirebaseApp.initializeApp(view.getContext());
        ref = FirebaseDatabase.getInstance().getReference();

//        for(int s=0; s < 100; s++){
//            Faktur.add("ertyugdd");
//            Tanggal.add("09-10-2018");
//            Total.add("30000");
//        }

        ref.child("order").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!Faktur.isEmpty()){
                    Faktur.clear();
                }
                if(!Tanggal.isEmpty()){
                    Tanggal.clear();
                }
                if(!Total.isEmpty()){
                    Total.clear();
                }
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    Faktur.add(child.getKey());
                    Tanggal.add(child.child("tanggal").getValue().toString());
                    Total.add(child.child("total").getValue().toString());
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
        adapter = new AdapterOrder(view.getContext(),Faktur,Tanggal,Total);
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
                    if(!FakturCari.isEmpty()){
                        FakturCari.clear();
                    }
                    if(!TanggalCari.isEmpty()){
                        TanggalCari.clear();
                    }
                    if(!TotalCari.isEmpty()){
                        TotalCari.clear();
                    }


                    for(int a =0; a < Faktur.size(); a++){
                        if(Faktur.get(a).toLowerCase().contains(newText.toLowerCase()) || Tanggal.get(a).toLowerCase().contains(newText.toLowerCase())){
                            //Toast.makeText(view.getContext(), "" + namabarang.get(a).toString(), Toast.LENGTH_SHORT).show();
                            FakturCari.add(Faktur.get(a));
                            TanggalCari.add(Tanggal.get(a));
                            TotalCari.add(Total.get(a));
                        }
                    }
                    adapter = new AdapterOrder(view.getContext(),FakturCari,TanggalCari,TotalCari);
                    rvView.setAdapter(adapter);
                }else{
                    adapter = new AdapterOrder(view.getContext(),Faktur,Tanggal,Total);
                    rvView.setAdapter(adapter);
                }
                return false;
            }
        });

        return view;
    }

}
