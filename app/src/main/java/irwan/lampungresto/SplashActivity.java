package irwan.lampungresto;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Calendar;

import irwan.lampungresto.Kelas.SharedVariable;
import irwan.lampungresto.Kelas.UserPreference;

public class SplashActivity extends AppCompatActivity {

    ProgressBar progressBar;
    Intent i;
    int delay =  3000;
    DatabaseReference ref,refDevice;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    private static final long time = 3;
    private CountDownTimer mCountDownTimer;
    private long mTimeRemaining;
    private String now;
    String activeDeviceKirim;
    UserPreference mUserpref;
    String bagian;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(SplashActivity.this);
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        mUserpref = new UserPreference(this);
        bagian = mUserpref.getBagian();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        int versionCode = BuildConfig.VERSION_CODE;
        Calendar calendar = Calendar.getInstance();
        int bulan = calendar.get(Calendar.MONTH)+1;
        now = ""+calendar.get(Calendar.DATE)+"-"+bulan+"-"+calendar.get(Calendar.YEAR);
        FirebaseUser fbUser = FirebaseAuth.getInstance().getCurrentUser();


        if (fbUser != null) {
            // User already signed in
            // get the FCM token
            String token = FirebaseInstanceId.getInstance().getToken();
            SharedVariable.nama = fAuth.getCurrentUser().getDisplayName();
            SharedVariable.userID = fAuth.getCurrentUser().getUid();

            mCountDownTimer = new CountDownTimer(time * 1000, 50) {
                @Override
                public void onTick(long millisUnitFinished) {
                    mTimeRemaining = ((millisUnitFinished / 1000) + 1);

                }

                @Override
                public void onFinish() {

                    if (bagian.equals("pembeli")){
                        i = new Intent(SplashActivity.this, HomeUserActivity.class);
                        startActivity(i);
                        finish();
                    }else if (bagian.equals("resto")){
                        i = new Intent(SplashActivity.this, HomeRestoActivity.class);
                        startActivity(i);
                        finish();
                    }else if (bagian.equals("none")){
                        i = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }

                }
            };
            mCountDownTimer.start();

            if (bagian.equals("pembeli")){
                ref.child("pembeli").child(fAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String cek = (String) dataSnapshot.child("check").getValue();
//                        Log.d("cek:",cek);
                        SharedVariable.check = cek;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }else if (bagian.equals("resto")){

                ref.child("resto").child(fAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String cek = (String) dataSnapshot.child("check").getValue();
                        String status = (String) dataSnapshot.child("status").getValue();

                        Log.d("cek:",cek);
                        SharedVariable.check = cek;
                        SharedVariable.userID = fAuth.getCurrentUser().getUid();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }



        } else {

            progressBar.setVisibility(View.GONE);
            i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
        }
    }
}
