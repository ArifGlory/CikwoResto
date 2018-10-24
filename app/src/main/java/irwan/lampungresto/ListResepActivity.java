package irwan.lampungresto;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import irwan.lampungresto.Adapter.RecycleAdapterListResep;

public class ListResepActivity extends AppCompatActivity {


    FloatingActionButton btnTambah;
    Intent i;
    RecyclerView recycler_listResep;
    RecycleAdapterListResep adapter;
    public static ProgressBar progressBar;
    TextView txtInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_resep);

        btnTambah = (FloatingActionButton) findViewById(R.id.btnCreate);
        txtInfo = (TextView) findViewById(R.id.txtInfo);
        btnTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(getApplicationContext(),TambahResepActivity.class);
                startActivity(i);
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        recycler_listResep= (RecyclerView) findViewById(R.id.recycler_listlevel);
        adapter = new RecycleAdapterListResep(this);
        recycler_listResep.setAdapter(adapter);
        recycler_listResep.setLayoutManager(new LinearLayoutManager(this));


    }
}
