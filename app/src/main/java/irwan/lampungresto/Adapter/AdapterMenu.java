package irwan.lampungresto.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import irwan.lampungresto.KeranjangFragment;
import irwan.lampungresto.KomentarActivity;
import irwan.lampungresto.MenuActivity;
import irwan.lampungresto.R;
import irwan.lampungresto.TambahMenuActivity;

/**
 * Created by fujimiya on 10/4/18.
 */

public class AdapterMenu extends RecyclerView.Adapter<AdapterMenu.ViewHolder> {
    private ArrayList<String> NamaMenu;
    private ArrayList<String> HargaMenu;
    private ArrayList<String> UrlGambar;
    private ArrayList<String> KunciMenu;

    DatabaseReference ref,ref2;
    Context context;
    private TextView resultText;
    Double ratingku = 0.0;
    int totalRating = 0;
    public AdapterMenu(Context contxt,ArrayList<String> Nama,ArrayList<String> Harga,ArrayList<String> urlG, ArrayList<String> KeyRes){
        NamaMenu = Nama;
        HargaMenu = Harga;
        UrlGambar = urlG;
        KunciMenu = KeyRes;
        context = contxt;
    }

    @Override
    public AdapterMenu.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_rv_menu, parent, false);
        AdapterMenu.ViewHolder vh = new AdapterMenu.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final AdapterMenu.ViewHolder holder, final int position) {
        Glide.with(context)
                .load(UrlGambar.get(position))
                .into(holder.imageView);
        holder.Txt_NamaMenu.setText(NamaMenu.get(position));
        holder.Txt_Harga.setText("Rp "+getMoney(HargaMenu.get(position))+",-");
        holder.btnKomentar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, KomentarActivity.class);
                intent.putExtra("key", KunciMenu.get(position));
                context.startActivity(intent);
            }
        });
        holder.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,""+KunciMenu.get(position),Toast.LENGTH_SHORT).show();
//                FirebaseDatabase database = FirebaseDatabase.getInstance();
//                final DatabaseReference myFirebaseRef = database.getReference(KunciMenu.get(position));
                showInputDialog(MenuActivity.resto_id,KunciMenu.get(position));
            }
        });
//        holder.ratingBar.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    // TODO perform your action here
//                    Toast.makeText(context," Rating "+holder.ratingBar.getRating(), Toast.LENGTH_SHORT).show();
//                }
//                return true;
//            }
//        });
        FirebaseDatabase database2 = FirebaseDatabase.getInstance();
        ref2 = database2.getReference("resto/ugsNmb5ix7hgSqNjFgAXUec39zv1/menuList/"+KunciMenu.get(position));
        ref2.child("rating").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                totalRating = 0;
                ratingku = 0.0;
                for(DataSnapshot child : dataSnapshot.getChildren()){
                    //float get = Float.parseFloat(child.child("nilai").getValue().toString());
                    String A = child.child("nilai").getValue().toString();
                    Double get = Double.parseDouble(A);
                    ratingku = ratingku + get;
                    //Toast.makeText(context," Rating "+A, Toast.LENGTH_SHORT).show();
                    totalRating++;
                }
                holder.ratingBar.setRating((float) (ratingku/totalRating));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                ref = database.getReference("resto/ugsNmb5ix7hgSqNjFgAXUec39zv1/menuList/"+KunciMenu.get(position));
                ref.child("rating").child(firebaseAuth.getCurrentUser().getUid()).child("nilai").setValue(""+holder.ratingBar.getRating());
                //Toast.makeText(context," Rating "+holder.ratingBar.getRating(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected void showInputDialog(final String id_resto, final String id_menu) {
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("keranjang");
        // get prompts.xml view
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(promptView);

        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Pesan", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        resultText.setText("Hello, " + editText.getText());
                        Map mParent = new HashMap();
                        mParent.put("uid", ""+firebaseAuth.getCurrentUser().getUid());
                        mParent.put("id_resto",""+id_resto);
                        mParent.put("id_menu",""+id_menu);
                        mParent.put("jumlah",""+editText.getText());
                        //ref.child("keranjang").child("contoh").setValue("isi contoh");
                        ref.push().setValue(mParent).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // View view1 = view;
                                Toast.makeText(context,"Pesanan Tersimpan",Toast.LENGTH_SHORT).show();
                                KeranjangFragment.status = "1";
                            }
                        });

                    }
                })
                .setNegativeButton("Batal",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    @Override
    public int getItemCount() {
        return NamaMenu.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Txt_NamaMenu;
        TextView Txt_Harga;
        ImageView imageView;
        public CardView cvMain,btnKomentar;
        public RatingBar ratingBar;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            Txt_NamaMenu = itemView.findViewById(R.id.tv_title);
            Txt_Harga = itemView.findViewById(R.id.tvharga);
            cvMain = (CardView) itemView.findViewById(R.id.cv_main);
            btnKomentar = (CardView) itemView.findViewById(R.id.btnKomentar);
            ratingBar = itemView.findViewById(R.id.rating);
        }
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
