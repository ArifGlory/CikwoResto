package irwan.lampungresto;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import irwan.lampungresto.Kelas.SharedVariable;

public class RekeningActivity extends AppCompatActivity {

    TextView txtNoRekNow;
    EditText etNoRek;
    Button btnSimpanRek;
    DialogInterface.OnClickListener listener;
    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekening);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(RekeningActivity.this);
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();

        txtNoRekNow = (TextView) findViewById(R.id.txtNoRekNow);
        etNoRek = (EditText) findViewById(R.id.etNoRek);
        btnSimpanRek = (Button) findViewById(R.id.signUpBtn);

        txtNoRekNow.setText(SharedVariable.noRekCikwo);

        btnSimpanRek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getNoRek = etNoRek.getText().toString();
                if (getNoRek.equals("") || getNoRek.length() == 0){
                    customToast("Input data harus diisi");
                }else {
                    ref.child("resto").child(SharedVariable.userID).child("noRekening").setValue(etNoRek.getText().toString());
                    txtNoRekNow.setText(getNoRek);
                }
            }
        });

    }

    public  void customToast(String s){
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.toast_root));

        TextView text = (TextView) layout.findViewById(R.id.toast_error);
        text.setText(s);
        Toast toast = new Toast(getApplicationContext());// Get Toast Context
        toast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);// Set
        toast.setDuration(Toast.LENGTH_SHORT);// Set Duration
        toast.setView(layout); // Set Custom View over toast
        toast.show();// Finally show toast
    }
}
