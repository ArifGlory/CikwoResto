package irwan.lampungresto.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import  irwan.lampungresto.R;

/**
 * Created by Glory on 03/10/2016.
 */
public class RecycleViewHolderListMenu extends RecyclerView.ViewHolder {

    public TextView txtNamaMenu,txtHarga,txtJumlahPesan;
    public ImageView img_iconlistMotor;
    public CardView cardlist_item;
    public RelativeLayout relaList;

    public RecycleViewHolderListMenu(View itemView) {
        super(itemView);

        txtNamaMenu = (TextView) itemView.findViewById(R.id.txt_namaMotor);
        txtHarga = (TextView) itemView.findViewById(R.id.txt_platNomor);
        img_iconlistMotor = (ImageView) itemView.findViewById(R.id.img_iconlistMotor);
        cardlist_item = (CardView) itemView.findViewById(R.id.cardlist_item);
        relaList = (RelativeLayout) itemView.findViewById(R.id.relaList);
        txtJumlahPesan = (TextView) itemView.findViewById(R.id.txtJumlahPesan);

    }
}
