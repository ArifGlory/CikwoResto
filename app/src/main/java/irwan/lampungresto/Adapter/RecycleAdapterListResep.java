package irwan.lampungresto.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

import irwan.lampungresto.DetailMenuActivity;
import irwan.lampungresto.DetailResepActivity;
import irwan.lampungresto.FragmentHomeResto;
import irwan.lampungresto.Kelas.SharedVariable;
import irwan.lampungresto.ListResepActivity;
import irwan.lampungresto.R;


/**
 * Created by Glory on 03/10/2016.
 */
public class RecycleAdapterListResep extends RecyclerView.Adapter<RecycleViewHolderListResep> {


    LayoutInflater inflater;
    Context context;
    Intent i;
    public static List<String> list_nama = new ArrayList();
    public static List<String> list_deskripsi = new ArrayList();
    public static List<String> list_key = new ArrayList();
    public static List<String> list_downloadURL = new ArrayList();
    public static List<String> list_detailResep = new ArrayList();
    public static List<String> list_alatBahan = new ArrayList();
    String key = "";
    Firebase Vref,refLagi;
    Bitmap bitmap;
    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;


    String[] nama ={"Beat Hitam","Revo Kuning"};
    String[] plat ={"BE 6390 BQ ","BE 6018 ME"};

    public RecycleAdapterListResep(final Context context) {

        this.context = context;
        inflater = LayoutInflater.from(context);
        Firebase.setAndroidContext(this.context);
        FirebaseApp.initializeApp(context.getApplicationContext());
        ref = FirebaseDatabase.getInstance().getReference();

        ref.child("resto").child(SharedVariable.uIDCikwo).child("resepList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                list_downloadURL.clear();
                list_deskripsi.clear();
                list_nama.clear();
                list_key.clear();
                list_detailResep.clear();
                list_alatBahan.clear();

                ListResepActivity.progressBar.setVisibility(View.VISIBLE);
                for (DataSnapshot child : dataSnapshot.getChildren()){
                    String key = child.getKey();
                    String namaMenu = child.child("namaResep").getValue().toString();
                    String deskripsi = child.child("deskripsi").getValue().toString();
                    String downloadURL = child.child("downloadUrl").getValue().toString();
                    String detail = child.child("detailResep").getValue().toString();
                    String alatbahan = child.child("alatBahan").getValue().toString();

                    list_key.add(key);
                    list_nama.add(namaMenu);
                    list_deskripsi.add(deskripsi);
                    list_downloadURL.add(downloadURL);
                    list_detailResep.add(detail);
                    list_alatBahan.add(alatbahan);
                }
                ListResepActivity.progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    @Override
    public RecycleViewHolderListResep onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemlist_listresep, parent, false);
        //View v = inflater.inflate(R.layout.item_list,parent,false);
        RecycleViewHolderListResep viewHolderChat = new RecycleViewHolderListResep(view);
        return viewHolderChat;
    }

    @Override
    public void onBindViewHolder(RecycleViewHolderListResep holder, final int position) {

        Resources res = context.getResources();


       holder.txtNamaResep.setText(list_nama.get(position).toString());
       holder.txtDeskripsi.setText(list_deskripsi.get(position).toString());
        //pake library glide buat load gambar dari URL

        Glide.with(context.getApplicationContext())
                .load(list_downloadURL.get(position).toString())
                .asBitmap().fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imgRecipe);


        holder.txtDeskripsi.setOnClickListener(clicklistener);
        holder.cardlist_item.setOnClickListener(clicklistener);
        holder.txtNamaResep.setOnClickListener(clicklistener);


        holder.txtNamaResep.setTag(holder);
        holder.txtDeskripsi.setTag(holder);

        holder.cardlist_item.setTag(holder);


    }

    View.OnClickListener clicklistener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            RecycleViewHolderListResep vHolder = (RecycleViewHolderListResep) v.getTag();
            int position = vHolder.getPosition();
          //  Toast.makeText(context.getApplicationContext(), "Item diklik", Toast.LENGTH_SHORT).show();

            i = new Intent(context.getApplicationContext(), DetailResepActivity.class);
            i.putExtra("nama",list_nama.get(position).toString());
            i.putExtra("deskripsi",list_deskripsi.get(position).toString());
            i.putExtra("url",list_downloadURL.get(position).toString());
            i.putExtra("key",list_key.get(position).toString());
            i.putExtra("detail",list_detailResep.get(position).toString());
            i.putExtra("alatBahan",list_alatBahan.get(position).toString());
            context.startActivity(i);

        }
    };



    public int getItemCount() {

       return list_nama == null ? 0 : list_nama.size();
     //  return nama.length;

    }



}
