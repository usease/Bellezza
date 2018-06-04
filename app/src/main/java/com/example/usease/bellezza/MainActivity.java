package com.example.usease.bellezza;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Toolbar mToolbar;

    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;

    private TabLayout mTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTab = (TabLayout) findViewById(R.id.main_tab);

        String locale_lang = Locale.getDefault().getDisplayLanguage();
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), getApplicationContext());
        mViewPager = (ViewPager) findViewById(R.id.main_view_pager);

        mViewPager.setAdapter(mPagerAdapter);

        mTab.setupWithViewPager(mViewPager);


        mToolbar = (Toolbar) findViewById(R.id.main_app_bar);
        setSupportActionBar(mToolbar);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();


        //Navigation View
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        if(mAuth.getCurrentUser() != null) {

            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_menu_drawer_admin);
        } else {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.activity_menu_drawer);

        }
        navigationView.setNavigationItemSelectedListener(this);

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

        if (mCurrentUser != null) {

            getMenuInflater().inflate(R.menu.admin_menu, menu);

        } else {

            getMenuInflater().inflate(R.menu.main_menu, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.admin_menu_logout) {

            mAuth.signOut();
            Intent main_intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(main_intent);
            finish();

        } else if (item.getItemId() == R.id.admin_menu_orders)  {

            Intent orders_intent = new Intent(MainActivity.this, OrdersActivity.class);
            startActivity(orders_intent);

        } else if (item.getItemId() == R.id.admin_menu_add_new) {

            Intent add_new_intent = new Intent(MainActivity.this, AddNewProductActivity.class);
            startActivity(add_new_intent);
        } else if (item.getItemId() == R.id.main_menu_login) {

            Intent login_intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(login_intent);
        } else if (item.getItemId() == R.id.main_menu_my_cart) {

            Intent my_orders_intent = new Intent (MainActivity.this, MyOrdersActivity.class);
            startActivity(my_orders_intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_my_cart) {

            Intent intent = new Intent (MainActivity.this, MyOrdersActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_my_favourites) {

            Intent intent = new Intent (MainActivity.this, WishlistActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_about_us) {
            Intent intent = new Intent (MainActivity.this, ScrollingActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_add_new) {

            Intent add_new_product = new Intent(this, AddNewProductActivity.class);
            startActivity(add_new_product);

        } else if(id == R.id.nav_view_orders){
            Intent orders_intent = new Intent(this, OrdersActivity.class);
            startActivity(orders_intent);
        } else if (id == R.id.nav_log_out) {

            mAuth.signOut();
            Intent main_intent = new Intent(this, MainActivity.class);
            startActivity(main_intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
