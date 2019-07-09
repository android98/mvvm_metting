package com.example.mvvmmeeting.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mvvmmeeting.Adapters.ViewPagerAdapter;
import com.example.mvvmmeeting.fragment_main_all;
import com.example.mvvmmeeting.Fragments.fragment_main_favorites;
import com.example.mvvmmeeting.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    ImageView btnMenu;
    FloatingActionButton btnAdd;
    RecyclerView mainRecycler;
    LinearLayoutManager mainManager;
    ViewPager vPager;
    TabLayout tabLayout;
    ImageView imgDrawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        vPager = findViewById(R.id.vPager);
        setUpViewPager();

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(vPager);
        setupIcons();

        btnMenu = findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.RIGHT);
            }
        });

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddMeetingActivity.class);
                startActivity(intent);
            }
        });


    }

    private void setUpViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new fragment_main_all());
        adapter.addFragment(new fragment_main_favorites());

        vPager.setAdapter(adapter);

    }

    private void setupIcons() {
        LinearLayout tabLinearlayoutAll = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.tab_layout_item, null
        );
        TextView txtall = tabLinearlayoutAll.findViewById(R.id.txtTabLayout);
        ImageView imgAll = tabLinearlayoutAll.findViewById(R.id.imgTabLayout);
        txtall.setText("کل");
        imgAll.setImageResource(R.drawable.ic_notes);
        tabLayout.getTabAt(0).setCustomView(tabLinearlayoutAll);


        LinearLayout tabLinearLayoutFavorite = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.tab_layout_item, null);
        TextView txtfavorite = tabLinearLayoutFavorite.findViewById(R.id.txtTabLayout);
        ImageView imgfavorite = tabLinearLayoutFavorite.findViewById(R.id.imgTabLayout);
        txtfavorite.setText("علاقه ها ");
        imgfavorite.setImageResource(R.drawable.ic_star);
        tabLayout.getTabAt(1).setCustomView(tabLinearLayoutFavorite);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_ezafekardan) {
            // Handle the camera action
            Intent intent = new Intent(MainActivity.this, AddMeetingActivity.class);
            startActivity(intent);

        } /*else if (id == R.id.nav_aboutus) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
