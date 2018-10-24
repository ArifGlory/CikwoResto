package irwan.lampungresto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ProgressBar;
import android.widget.TextView;

import irwan.lampungresto.Adapter.RecycleAdapterListPesanan;

public class ListPesananActivity extends AppCompatActivity {

    Intent i;
    RecyclerView recycler_listResep;
    RecycleAdapterListPesanan adapter;
    public static ProgressBar progressBar;
    TextView txtInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pesanan);

        txtInfo = (TextView) findViewById(R.id.txtInfo);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        recycler_listResep= (RecyclerView) findViewById(R.id.recycler_listlevel);
        adapter = new RecycleAdapterListPesanan(this);
        recycler_listResep.setAdapter(adapter);
        recycler_listResep.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),HomeRestoActivity.class);
        startActivity(i);
    }
}
