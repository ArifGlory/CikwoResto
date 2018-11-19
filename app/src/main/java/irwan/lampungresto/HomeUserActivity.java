package irwan.lampungresto;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import irwan.lampungresto.Kelas.ProfilFragment;
import irwan.lampungresto.Kelas.SharedVariable;
import irwan.lampungresto.Kelas.UserPreference;

public class HomeUserActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseReference ref;
    private FirebaseAuth fAuth;
    private FirebaseAuth.AuthStateListener fStateListener;
    private String displayname;
    FirebaseUser fbUser;
    UserPreference mUserpref;
    Intent i;

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ViewPager viewPages;
    private TabLayout tabLayout;
    private PagerAdapter pagerAdapter;
    private ActionBar actionBar;
    private String[] tabTitle = {"Profil Resto"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda_user);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Firebase.setAndroidContext(this);
        FirebaseApp.initializeApp(getApplicationContext());
        ref = FirebaseDatabase.getInstance().getReference();
        fAuth = FirebaseAuth.getInstance();
        fbUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserpref = new UserPreference(this);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView txtNamaProfil = (TextView) headerView.findViewById(R.id.txtNama);
        txtNamaProfil.setText(SharedVariable.nama);

        viewPages =  (ViewPager) findViewById(R.id.view_pager);
        pagerAdapter = new FragmentAdapter(getSupportFragmentManager());
        viewPages.setAdapter(pagerAdapter);

        actionBar = getSupportActionBar();
        actionBar.setTitle(tabTitle[0]);
        actionBar.setSubtitle("Cikwo Resto");
        viewPages.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                actionBar.setTitle(tabTitle[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPages);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_restaurant_menu_black_24dp);
    }

    void goToFragment(Fragment fragment, boolean isTop) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.fragmen_home, fragment);
        if (!isTop)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int pos) {
            switch (pos) {
                case 0:
                    return new ProfilFragment();
            }
            return  null;
        }

        public int getCount() {
            return 1;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.beranda_user, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_logoutuser) {
            fAuth.signOut();
            mUserpref.setBagian("none");
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }else if (id == R.id.nav_resep){
            i = new Intent(getApplicationContext(),ListResepActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
