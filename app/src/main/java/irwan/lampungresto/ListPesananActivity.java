package irwan.lampungresto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import irwan.lampungresto.Adapter.RecycleAdapterListPesanan;
import irwan.lampungresto.Adapter.RecycleAdapterPesananBelumVerifikasi;
import irwan.lampungresto.Adapter.RecycleAdapterPesananDiterima;
import irwan.lampungresto.Adapter.RecycleAdapterPesananDitolak;
import irwan.lampungresto.Adapter.RecycleAdapterPesananHariIni;

public class ListPesananActivity extends AppCompatActivity {

    Intent i;
    RecyclerView recycler_listResep;
    RecycleAdapterListPesanan adapter;
    RecycleAdapterPesananDiterima adapterPesananDiterima;
    RecycleAdapterPesananBelumVerifikasi adapterPesananBelumVerifikasi;
    RecycleAdapterPesananDitolak adapterPesananDitolak;
    RecycleAdapterPesananHariIni adapterPesananHariIni;
    public static ProgressBar progressBar;
    TextView txtInfo;
    Spinner sp_filter;
    String filterPesanan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pesanan);

        txtInfo = (TextView) findViewById(R.id.txtInfo);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        sp_filter = (Spinner) findViewById(R.id.sp_filter);
        adapterPesananDiterima = new RecycleAdapterPesananDiterima(this);
        adapterPesananBelumVerifikasi = new RecycleAdapterPesananBelumVerifikasi(this);
        adapterPesananDitolak = new RecycleAdapterPesananDitolak(this);
        adapterPesananHariIni = new RecycleAdapterPesananHariIni(this);

        recycler_listResep= (RecyclerView) findViewById(R.id.recycler_listlevel);
        adapter = new RecycleAdapterListPesanan(this);
        recycler_listResep.setAdapter(adapter);
        recycler_listResep.setLayoutManager(new LinearLayoutManager(this));

        sp_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                filterPesanan = adapterView.getItemAtPosition(i).toString();

                if (filterPesanan.equals("Pesanan Dikirim")){
                    recycler_listResep.setAdapter(adapterPesananDiterima);
                    recycler_listResep.setLayoutManager(new LinearLayoutManager(ListPesananActivity.this));
                }else if (filterPesanan.equals("Pesanan belum diverifikasi")){
                    recycler_listResep.setAdapter(adapterPesananBelumVerifikasi);
                    recycler_listResep.setLayoutManager(new LinearLayoutManager(ListPesananActivity.this));
                }else if (filterPesanan.equals("Pesanan Ditolak")){
                    recycler_listResep.setAdapter(adapterPesananDitolak);
                    recycler_listResep.setLayoutManager(new LinearLayoutManager(ListPesananActivity.this));
                }else if (filterPesanan.equals("Pesanan Hari ini")){
                    recycler_listResep.setAdapter(adapterPesananHariIni);
                    recycler_listResep.setLayoutManager(new LinearLayoutManager(ListPesananActivity.this));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),HomeRestoActivity.class);
        startActivity(i);
    }
}
