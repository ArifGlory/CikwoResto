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
public class RecycleViewHolderListResep extends RecyclerView.ViewHolder {

    public TextView txtNamaResep,txtDeskripsi;
    public ImageView imgRecipe;
    public CardView cardlist_item;


    public RecycleViewHolderListResep(View itemView) {
        super(itemView);

        txtNamaResep = (TextView) itemView.findViewById(R.id.txtNamaResep);
        txtDeskripsi = (TextView) itemView.findViewById(R.id.txtDeskripsiResep);
        imgRecipe = (ImageView) itemView.findViewById(R.id.imgRecipe);
        cardlist_item = (CardView) itemView.findViewById(R.id.cardlist_item);

    }
}
