package com.example.medicalhelp;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.medicalhelp.databinding.ActivityMainBinding;
import com.example.medicalhelp.dbs.ApplicationDatabase;
import com.example.medicalhelp.utils.AppDbHandler;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    public final int[] MENU_ITEMS_IDES =
            {
                    R.id.nav_home,
                    R.id.nav_burns,
                    R.id.nav_trauma,
                    R.id.nav_hemorrhage,
                    R.id.nav_intoxication,
                    R.id.nav_freezing,
                    R.id.nav_asphyxia,
                    R.id.nav_sunstrike,
                    R.id.nav_drowning
            };
    NavController navController;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        _configureNavigationBar(drawer, MENU_ITEMS_IDES);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        AppDbHandler.initializeDatabase(
                this,
                ApplicationDatabase.class,
                "default",
                true
        );
        AppDbHandler.logEntries(this, ApplicationDatabase.class, "default");
        setCallerEventListener(navigationView, drawer);
    }

    private void setCallerEventListener(NavigationView navigationView, DrawerLayout drawer) {
        navigationView.setNavigationItemSelectedListener(
                item -> {
                    int helperCallerId = (R.id.nav_help);
                    if (item.getItemId() == helperCallerId) {
                        Intent callerIntent =
                                new Intent(Intent.ACTION_DIAL,
                                        Uri.parse(String.format("tel:%s", getString(R.string.medicalHelpTelephon))));
                        startActivity(callerIntent);
                        return false;
                    }
                    NavigationUI.onNavDestinationSelected(item, navController);
                    drawer.closeDrawer(GravityCompat.START);
                    return true;
                }
        );
    }

    //    Айдишники каждого элемента, для отображения в меню как бургер
    private void _configureNavigationBar(DrawerLayout drawer, int... ids) {
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                ids)
                .setOpenableLayout(drawer)
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}