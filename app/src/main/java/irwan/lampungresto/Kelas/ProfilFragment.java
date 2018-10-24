package irwan.lampungresto.Kelas;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import irwan.lampungresto.MenuActivity;
import irwan.lampungresto.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilFragment extends Fragment {


    public ProfilFragment() {
        // Required empty public constructor
    }
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profil, container, false);

        Button btnMenu = v.findViewById(R.id.btnMenu);
        TextView txtSejarah = (TextView) v.findViewById(R.id.tvNumber);
        TextView   txtAlamat = (TextView) v.findViewById(R.id.tvNumber5);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MenuActivity.class);
                intent.putExtra("key", "ugsNmb5ix7hgSqNjFgAXUec39zv1");
                startActivity(intent);
            }
        });
        txtSejarah.setText("Cikwo Resto adalah salah satu restoran yang menyajikan masakan kuliner khas lampung" +
                " yang berdiri sejak februari 2015. Cikwo Resto dibuka dengan alasan sebagai jawaban atas sulitnya " +
                "bagi masyarakat untuk menemukan masakan khas lampung di Bandarlampung Oleh Isna Adianti. ");
        txtAlamat.setText("Jl Kimaja (Kimaja Icon), samping Jaya Bakery," +
                " 10m sebelum fly over TanjungSeneng, Sepang Jaya, " +
                "Labuhan Ratu, Sepang Jaya, Kedaton, " +
                "Kota Bandar Lampung, Lampung 35213");
        return v;
    }

}
