package irwan.lampungresto;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import irwan.lampungresto.Adapter.RecycleAdapterListMenu;
import irwan.lampungresto.Kelas.UserPreference;


public class FragmentHomeResto extends Fragment {

    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    private String displayname;
    FirebaseUser fbUser;
    UserPreference mUserpref;
    DatabaseReference ref;
    private String status;

    FloatingActionButton btnCreate;
    Intent i;
    RecyclerView recycler_listMenu;
    public static ProgressBar progressBar;
    TextView txtInfo;
    RecycleAdapterListMenu adapter;

    public FragmentHomeResto() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Firebase.setAndroidContext(getActivity());
        FirebaseApp.initializeApp(getActivity());
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserpref = new UserPreference(getActivity());

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_resto, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnCreate = (FloatingActionButton) view.findViewById(R.id.btnCreate);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        txtInfo = (TextView) view.findViewById(R.id.txtInfo);

        recycler_listMenu = (RecyclerView) view.findViewById(R.id.recycler_listlevel);
        adapter = new RecycleAdapterListMenu(getActivity());
        recycler_listMenu.setAdapter(adapter);
        recycler_listMenu.setLayoutManager(new LinearLayoutManager(getActivity()));

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(getActivity(),TambahMenuActivity.class);
                startActivity(i);
            }
        });

    }

    public void refreshList(){
        adapter.getDataMenu();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
