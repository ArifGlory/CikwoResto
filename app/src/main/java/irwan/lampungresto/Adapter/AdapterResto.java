package irwan.lampungresto.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.security.Key;
import java.util.ArrayList;

import irwan.lampungresto.MenuActivity;
import irwan.lampungresto.R;

/**
 * Created by fujimiya on 10/4/18.
 */

public class AdapterResto extends RecyclerView.Adapter<AdapterResto.ViewHolder> {
    private ArrayList<String> NamaREsto;
    private ArrayList<String> AlamatResto;
    private ArrayList<String> KeyResto;

    Context context;

    public AdapterResto(Context contxt,ArrayList<String> Resto,ArrayList<String> Alamat, ArrayList<String> KeyRes){
        NamaREsto = Resto;
        AlamatResto = Alamat;
        KeyResto = KeyRes;
        context = contxt;

    }
    @Override
    public AdapterResto.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_rv_resto, parent, false);
        AdapterResto.ViewHolder vh = new AdapterResto.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final AdapterResto.ViewHolder holder, final int position) {
        holder.Txt_NamaResto.setText(NamaREsto.get(position));
        holder.Txt_AlamatResto.setText(AlamatResto.get(position));
        holder.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MenuActivity.class);
                intent.putExtra("key", KeyResto.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return NamaREsto.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Txt_NamaResto;
        TextView Txt_AlamatResto;
        public CardView cvMain;
        public ViewHolder(View itemView) {
            super(itemView);
            Txt_NamaResto = itemView.findViewById(R.id.nama_resto);
            Txt_AlamatResto = itemView.findViewById(R.id.alamat_resto);
            cvMain = (CardView) itemView.findViewById(R.id.cv_main);
        }
    }
}
