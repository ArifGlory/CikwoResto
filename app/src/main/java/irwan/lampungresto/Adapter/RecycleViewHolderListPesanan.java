package irwan.lampungresto.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import irwan.lampungresto.R;

/**
 * Created by Glory on 03/10/2016.
 */
public class RecycleViewHolderListPesanan extends RecyclerView.ViewHolder {

    public TextView txtNamaMenu,txtHarga,txtTanggal;
    public ImageView img_iconlistMotor;
    public CardView cardlist_item;
    public RelativeLayout relaList;

    public RecycleViewHolderListPesanan(View itemView) {
        super(itemView);

        txtNamaMenu = (TextView) itemView.findViewById(R.id.txt_namaMotor);
        txtHarga = (TextView) itemView.findViewById(R.id.txt_platNomor);
        txtTanggal = (TextView) itemView.findViewById(R.id.txtTanggalBeli);
        img_iconlistMotor = (ImageView) itemView.findViewById(R.id.img_iconlistMotor);
        cardlist_item = (CardView) itemView.findViewById(R.id.cardlist_item);
        relaList = (RelativeLayout) itemView.findViewById(R.id.relaList);

    }
}
