package irwan.lampungresto.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import irwan.lampungresto.DetailOrderActivity;
import irwan.lampungresto.MenuActivity;
import irwan.lampungresto.R;

/**
 * Created by fujimiya on 10/8/18.
 */




public class AdapterOrder extends RecyclerView.Adapter<AdapterOrder.ViewHolder> {
    private ArrayList<String> Faktur;
    private ArrayList<String> Tanggal;
    private ArrayList<String> Total;
    Context context;
    public AdapterOrder(Context getcontext, ArrayList<String> getFaktur, ArrayList<String> getTanggal, ArrayList<String> getTotal){
        Faktur =  getFaktur;
        Tanggal = getTanggal;
        Total = getTotal;
        context = getcontext;
    }

    @Override
    public AdapterOrder.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_rv_order, parent, false);
        AdapterOrder.ViewHolder vh = new AdapterOrder.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(AdapterOrder.ViewHolder holder, final int position) {
        holder.Txt_Faktur.setText("FAKTUR/"+Faktur.get(position));
        holder.Txt_Tanggal.setText("Tanggal : "+Tanggal.get(position));
        holder.Txt_Harga.setText("Rp "+getMoney(Total.get(position))+",00-");
        holder.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailOrderActivity.class);
                intent.putExtra("faktur", Faktur.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Faktur.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Txt_Faktur;
        TextView Txt_Tanggal;
        TextView Txt_Harga;
        CardView cvMain;
        public ViewHolder(View itemView) {
            super(itemView);
            Txt_Faktur = itemView.findViewById(R.id.txt_faktur);
            Txt_Tanggal = itemView.findViewById(R.id.txt_tanggal);
            Txt_Harga = itemView.findViewById(R.id.total_pesanan);
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
