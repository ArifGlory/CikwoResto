package irwan.lampungresto;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import irwan.lampungresto.Adapter.AdapterKeranjang;
import irwan.lampungresto.Adapter.AdapterMenu;


/**
 * A simple {@link Fragment} subclass.
 */
public class KeranjangFragment extends Fragment {

    View view;
    Firebase firebase;
    DatabaseReference refKeranjang,refMenu,refOrder,refKeranjangHapus;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;

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

    private String waktu;
    String keyOrder;
    public static String status = "1";

    public KeranjangFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_keranjang, container, false);
        btn_order = view.findViewById(R.id.btnorder);
        txt_total = view.findViewById(R.id.txt_total);
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Firebase.setAndroidContext(view.getContext());
        FirebaseApp.initializeApp(view.getContext());
        refKeranjang = FirebaseDatabase.getInstance().getReference();
        refKeranjangHapus = FirebaseDatabase.getInstance().getReference();
        refMenu = FirebaseDatabase.getInstance().getReference();
        refOrder = FirebaseDatabase.getInstance().getReference();
        refKeranjang.child("keranjang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                total = 0;
                subtotal = 0;
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
                for(final DataSnapshot child : dataSnapshot.getChildren()){
                    if(firebaseAuth.getCurrentUser().getUid().toString().equals(child.child("uid").getValue().toString()) && status == "1"){
                        refMenu.child("resto").child(child.child("id_resto").getValue().toString()).child("menuList").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot child2 : dataSnapshot.getChildren()){
                                    if(child2.getKey().toString().equals(child.child("id_menu").getValue().toString())) {
                                        Toast.makeText(view.getContext(), "Keambil lagi", Toast.LENGTH_SHORT).show();
                                        NamaMenu.add(child2.child("namaMenu").getValue().toString());
                                        HargaMenu.add(child2.child("harga").getValue().toString());
                                        UrlGambar.add(child2.child("downloadUrl").getValue().toString());
                                        Jumlah.add(child.child("jumlah").getValue().toString());
                                        subtotal = Integer.parseInt(child2.child("harga").getValue().toString()) * Integer.parseInt(child.child("jumlah").getValue().toString());
                                        total+=subtotal;
                                    }
                                }
//                                for (int a = 0; a < NamaMenu.size(); a++){
//                                    Toast.makeText(view.getContext(), "" + NamaMenu.get(a) +", "+HargaMenu.get(a)+", "+Jumlah.get(a)+", "+UrlGambar.get(a), Toast.LENGTH_SHORT).show();
//                                }
                                adapter.notifyDataSetChanged();
                                txt_total.setText("Rp "+getMoney(""+total)+",00");
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        rvView = view.findViewById(R.id.rv_main_keranjang);
        rvView.setHasFixedSize(true);

        rvView.setLayoutManager(new GridLayoutManager(view.getContext(), 1));
        adapter = new AdapterKeranjang(view.getContext(),NamaMenu,HargaMenu,UrlGambar,Jumlah);
        rvView.setAdapter(adapter);
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int bulan = calendar.get(Calendar.MONTH)+1;

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                waktu = dateFormat.format(new Date()); // Find todays date

                if(total > 0) {
                    Map mParent = new HashMap();
                    mParent.put("uid", "" + firebaseAuth.getCurrentUser().getUid());
                    mParent.put("tanggal", "" + waktu);
                    mParent.put("total", "" + total);
                    mParent.put("status", "1");
                    keyOrder = refOrder.child("order").push().getKey();
                    refOrder.child("order").child(keyOrder).setValue(mParent);
                }
                for(int b=0; b<NamaMenu.size();b++){
                    Map mdetail = new HashMap();
                    mdetail.put("nama_menu", ""+NamaMenu.get(b));
                    mdetail.put("harga",""+HargaMenu.get(b));
                    mdetail.put("jumlah",""+Jumlah.get(b));
                    mdetail.put("url_gambar",""+UrlGambar.get(b));
                    mdetail.put("id_order",""+keyOrder);

                    refOrder.child("order_detail").push().setValue(mdetail);
                }
                //Toast.makeText(view.getContext(),""+keyOrder,Toast.LENGTH_SHORT).show();

                refKeranjangHapus.child("keranjang").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!keykunci.isEmpty()){
                            keykunci.clear();
                        }
                        status = "2";

                        for(DataSnapshot child : dataSnapshot.getChildren()){
                            if(firebaseAuth.getCurrentUser().getUid().toString().equals(child.child("uid").getValue().toString())){
                                //Toast.makeText(view.getContext(),"UID : "+child.getKey().toString(),Toast.LENGTH_SHORT).show();
                                //keykunci.add(child.getKey().toString());
                                refKeranjang.child("keranjang").child(child.getKey().toString()).setValue(null);

                            }
                            total = 0;
                        }

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

                        adapter.notifyDataSetChanged();
                        total = 0;
                        txt_total.setText("Rp "+getMoney(""+total)+",00");
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        return view;
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

}
