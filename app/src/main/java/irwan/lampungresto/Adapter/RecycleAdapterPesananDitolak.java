package irwan.lampungresto.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import irwan.lampungresto.ListPesananActivity;
import irwan.lampungresto.R;


/**
 * Created by Glory on 03/10/2016.
 */
public class RecycleAdapterPesananDitolak extends RecyclerView.Adapter<RecycleViewHolderListPesanan> {


    LayoutInflater inflater;
    Context context;
    Intent i;
    public static List<String> listKeyUser = new ArrayList();
    public static List<String> list_nama = new ArrayList();
    public static List<String> listPhoneUser = new ArrayList();
    public static List<String> listTotalHarga = new ArrayList();
    public static List<String> listKeyOrder = new ArrayList();
    public static List<String> listTanggal = new ArrayList();
    public static List<String> listStatus = new ArrayList();
    public static List<String> list_downloadURL = new ArrayList();
    String key = "";
    Firebase Vref,refLagi;
    Bitmap bitmap;
    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    String statusPesanan = "";


    String[] nama ={"Beat Hitam","Revo Kuning"};
    String[] plat ={"BE 6390 BQ ","BE 6018 ME"};

    public RecycleAdapterPesananDitolak(final Context context) {

        this.context = context;
        inflater = LayoutInflater.from(context);
        Firebase.setAndroidContext(this.context);
        FirebaseApp.initializeApp(context.getApplicationContext());
        ref = FirebaseDatabase.getInstance().getReference();

        ref.child("order").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ListPesananActivity.progressBar.setVisibility(View.VISIBLE);
                listKeyOrder.clear();
                listTanggal.clear();
                listTotalHarga.clear();
                listStatus.clear();


                for (DataSnapshot child : dataSnapshot.getChildren()){
                    String keyOrder = child.getKey();
                    String tanggal = child.child("tanggal").getValue().toString();
                    String total = child.child("total").getValue().toString();
                    String uidUser = child.child("uid").getValue().toString();
                    String status = child.child("status").getValue().toString();
                    int index = tanggal.indexOf(" ");
                    String subTanggal = tanggal.substring(0,index);

                    if (status.equals("3")){
                        listKeyOrder.add(keyOrder);
                        listTanggal.add(subTanggal);
                        listKeyUser.add(uidUser);
                        listTotalHarga.add(total);
                        listStatus.add(status);
                    }

                }
                ListPesananActivity.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (!listKeyOrder.isEmpty()){

        }

    }




    @Override
    public RecycleViewHolderListPesanan onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlist_listpesanan, parent, false);
        //View v = inflater.inflate(R.layout.item_list,parent,false);
        RecycleViewHolderListPesanan viewHolderChat = new RecycleViewHolderListPesanan(view);
        return viewHolderChat;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(RecycleViewHolderListPesanan holder, final int position) {

        Resources res = context.getResources();
        if (listStatus.get(position).toString().equals("1")){
            statusPesanan = "Menunggu Konfirmasi";
        }else if (listStatus.get(position).toString().equals("2")){
            statusPesanan = "Dikirim";
        }else if (listStatus.get(position).toString().equals("3")){
            statusPesanan = "Ditolak";
        }

        holder.txtNamaMenu.setText("Pesanan "+ (position+1));
        holder.txtHarga.setText("Rp. "+listTotalHarga.get(position).toString());
        holder.txtTanggal.setText(listTanggal.get(position).toString());
        holder.txtStatus.setText(statusPesanan);

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

            RecycleViewHolderListPesanan vHolder = (RecycleViewHolderListPesanan) v.getTag();

                int position = vHolder.getPosition();
                // Toast.makeText(context.getApplicationContext(), "Item diklik", Toast.LENGTH_SHORT).show();
                i = new Intent(context.getApplicationContext(), DetailPesananActivity.class);
                i.putExtra("keyPembeli",listKeyUser.get(position).toString());
                i.putExtra("keyOrder",listKeyOrder.get(position).toString());
                  i.putExtra("status",listStatus.get(position).toString());
                context.startActivity(i);



        }
    };



    public int getItemCount() {

       return listTotalHarga == null ? 0 : listTotalHarga.size();
       //return nama.length;

    }



}
