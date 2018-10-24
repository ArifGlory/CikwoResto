package irwan.lampungresto;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import irwan.lampungresto.Kelas.SharedVariable;
import irwan.lampungresto.Kelas.UserPreference;
import irwan.lampungresto.Kelas.Utils;

public class LoginRestoActivity extends AppCompatActivity {



    EditText emailid, password;
    Button loginButton;
    TextView forgotPassword, signUp,daftarPenjual,keLoginPembeli;
    Intent i;
    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    ProgressBar progressBar;
    DialogInterface.OnClickListener listener;
    private String userID;
    UserPreference mUserPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_resto);
        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(LoginRestoActivity.this);
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        mUserPref = new UserPreference(this);
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();
        if (fbUser!=null){
            // User already signed in

            // get the FCM token
            String token = FirebaseInstanceId.getInstance().getToken();
            // save the user info in the database to users/UID/
            // we'll use the UID as part of the path
           /* User user = new User(fbUser.getUid(), fbUser.getDisplayName(), token);
            database.child("users").child(user.uid).setValue(user);*/

            // go to intent activity
            // Intent intent = new Intent(this, SplashActivity.class);
            // startActivity(intent);
        }

        //deklarasi variabel
        emailid = (EditText) findViewById(R.id.login_emailid);
        password = (EditText) findViewById(R.id.login_password);
        loginButton = (Button) findViewById(R.id.loginBtn);
        keLoginPembeli = (TextView) findViewById(R.id.keLoginPembeli);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        daftarPenjual = (TextView) findViewById(R.id.daftarPenjual);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });
        daftarPenjual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // buat pindah ke register activiti
                i = new Intent(getApplicationContext(),RegisterRestoActivity.class);
                startActivity(i);
            }
        });
        keLoginPembeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }
        });
    }

    private void matikanKomponen(){
        emailid.setEnabled(false);
        password.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);
    }

    private  void hidupkanKomponen(){
        emailid.setEnabled(true);
        password.setEnabled(true);
        progressBar.setVisibility(View.GONE);
        loginButton.setEnabled(true);
    }

    private void checkValidation() {
        String getEmailId = emailid.getText().toString();
        String getPassword = password.getText().toString();
        matikanKomponen();

        Pattern p = Pattern.compile(Utils.regEx);
        Matcher m = p.matcher(getEmailId);

        if (getEmailId.equals("") || getEmailId.length() == 0
                || getPassword.equals("") || getPassword.length() == 0) {

            customToast("Email dan password harus diisi");
            hidupkanKomponen();
        } else if (!m.find()) {
            customToast("Email anda tidak valid");
            hidupkanKomponen();
        } else {
            doLogin(getEmailId, getPassword);

        }
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

    private void doLogin(final String email,String passwordUser){
        fAuth.signInWithEmailAndPassword(email,passwordUser).addOnCompleteListener(LoginRestoActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()){
                    hidupkanKomponen();
                    customToast("Login Gagal, periksa kembali email dan password anda");
                    //Toast.makeText(getApplicationContext(), "Login Gagal, periksa kembali email dan password anda", Toast.LENGTH_LONG).show();
                }else{
                    // Successfully signed in
                    SharedVariable.nama = fAuth.getCurrentUser().getDisplayName();
                    SharedVariable.userID = fAuth.getCurrentUser().getUid();
                    // get the Firebase user
                    FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();

                    // get the FCM token
                    String token = FirebaseInstanceId.getInstance().getToken();
                    userID = fbUser.getUid();
                    mUserPref.setBagian("resto");
                    i = new Intent(LoginRestoActivity.this,SplashActivity.class);
                    startActivity(i);

                }


            }
        });
    }
}
