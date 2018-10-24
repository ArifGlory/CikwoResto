package irwan.lampungresto.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import irwan.lampungresto.DetailPesananActivity;
import irwan.lampungresto.R;


/**
 * Created by Glory on 03/10/2016.
 */
public class RecycleAdapterDetailPesanan extends RecyclerView.Adapter<RecycleViewHolderListMenu> {


    LayoutInflater inflater;
    Context context;
    Intent i;
    public static List<String> list_nama = new ArrayList();
    public static List<String> list_harga = new ArrayList();
    public static List<String> list_jumlah = new ArrayList();
    public static List<String> list_key = new ArrayList();
    public static List<String> list_downloadURL = new ArrayList();
    String key = "";
    Firebase Vref,refLagi;
    Bitmap bitmap;
    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;


    String[] nama ={"Beat Hitam","Revo Kuning"};
    String[] plat ={"BE 6390 BQ ","BE 6018 ME"};

    public RecycleAdapterDetailPesanan(final Context context) {

        this.context = context;
        inflater = LayoutInflater.from(context);
        Firebase.setAndroidContext(this.context);
        FirebaseApp.initializeApp(context.getApplicationContext());
        ref = FirebaseDatabase.getInstance().getReference();

        ref.child("order_detail").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DetailPesananActivity.progressBar.setVisibility(View.VISIBLE);
                list_downloadURL.clear();
                list_harga.clear();
                list_jumlah.clear();
                list_key.clear();
                list_nama.clear();


                for (DataSnapshot child : dataSnapshot.getChildren()){

                    String keyOrder = child.child("id_order").getValue().toString();
                    String namaMenu = child.child("nama_menu").getValue().toString();
                    String harga = child.child("harga").getValue().toString();
                    String jumlah = child.child("jumlah").getValue().toString();
                    String dwnloadURL = child.child("url_gambar").getValue().toString();

                    if (keyOrder.equals(DetailPesananActivity.keyOrder)){
                        list_downloadURL.add(dwnloadURL);
                        list_nama.add(namaMenu);
                        list_jumlah.add(jumlah);
                        list_harga.add(harga);
                    }
                }
                DetailPesananActivity.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    @Override
    public RecycleViewHolderListMenu onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlist_detailpesanan, parent, false);
        //View v = inflater.inflate(R.layout.item_list,parent,false);
        RecycleViewHolderListMenu viewHolderChat = new RecycleViewHolderListMenu(view);
        return viewHolderChat;
    }

    @Override
    public void onBindViewHolder(RecycleViewHolderListMenu holder, final int position) {

        Resources res = context.getResources();


       holder.txtNamaMenu.setText(list_nama.get(position).toString());
       holder.txtHarga.setText("Rp. "+list_harga.get(position).toString());
        holder.txtJumlahPesan.setText(list_jumlah.get(position).toString()+" porsi");
        //pake library glide buat load gambar dari URL
        Glide.with(context.getApplicationContext())
                .load(list_downloadURL.get(position).toString())
                .asBitmap().fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.img_iconlistMotor);


        holder.txtHarga.setOnClickListener(clicklistener);
        holder.cardlist_item.setOnClickListener(clicklistener);
        holder.txtNamaMenu.setOnClickListener(clicklistener);
        holder.relaList.setOnClickListener(clicklistener);
        holder.img_iconlistMotor.setOnClickListener(clicklistener);


        holder.txtNamaMenu.setTag(holder);
        holder.txtHarga.setTag(holder);
        holder.img_iconlistMotor.setTag(holder);
        holder.relaList.setTag(holder);


    }

    View.OnClickListener clicklistener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            RecycleViewHolderListMenu vHolder = (RecycleViewHolderListMenu) v.getTag();
            int position = vHolder.getPosition();
           // Toast.makeText(context.getApplicationContext(), "Item diklik", Toast.LENGTH_SHORT).show();

        }
    };



    public int getItemCount() {

       return list_nama == null ? 0 : list_nama.size();
        //return nama.length;

    }



}
