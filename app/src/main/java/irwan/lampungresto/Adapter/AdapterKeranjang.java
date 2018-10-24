package irwan.lampungresto.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import irwan.lampungresto.MenuActivity;
import irwan.lampungresto.R;

/**
 * Created by fujimiya on 10/5/18.
 */

public class AdapterKeranjang extends RecyclerView.Adapter<AdapterKeranjang.ViewHolder> {

    private ArrayList<String> NamaMenu;
    private ArrayList<String> HargaMenu;
    private ArrayList<String> UrlGambar;
    private ArrayList<String> Jumlah;

    Context context;
    public AdapterKeranjang(Context contxt, ArrayList<String> Nama, ArrayList<String> Harga, ArrayList<String> urlG, ArrayList<String> Jumlahget){
        NamaMenu = Nama;
        HargaMenu = Harga;
        UrlGambar = urlG;
        Jumlah = Jumlahget;
        context = contxt;
    }

    @Override
    public AdapterKeranjang.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_rv_keranjang, parent, false);
        AdapterKeranjang.ViewHolder vh = new AdapterKeranjang.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(AdapterKeranjang.ViewHolder holder, int position) {
        Glide.with(context)
                .load(UrlGambar.get(position))
                .into(holder.imageView);
        holder.Txt_NamaMenu.setText(NamaMenu.get(position));
        holder.Txt_Jumlah.setText(Jumlah.get(position));
        holder.Txt_Harga.setText("Rp "+getMoney(HargaMenu.get(position))+",-");
        holder.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return NamaMenu.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Txt_NamaMenu;
        TextView Txt_Harga;
        TextView Txt_Jumlah;
        ImageView imageView;

        public CardView cvMain;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_menu);
            Txt_NamaMenu = itemView.findViewById(R.id.txt_namaMenu);
            Txt_Harga = itemView.findViewById(R.id.txt_Harga);
            Txt_Jumlah = itemView.findViewById(R.id.txt_jumlah);
            cvMain = (CardView) itemView.findViewById(R.id.cv_main);
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
