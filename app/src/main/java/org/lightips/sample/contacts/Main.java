package org.lightips.sample.contacts;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.lightips.sample.contacts.entity.Contact;
import org.lightips.sample.contacts.fragment.AboutFragment;
import org.lightips.sample.contacts.fragment.ContactsFragment;
import org.lightips.sample.contacts.service.http.JsonHttpHandler;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class Main extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    protected static final String TAG = Main.class.getSimpleName();

    private ProgressDialog progressDialog;
    private JsonHttpHandler jsonHttpHandler;
    private boolean destroyed = false;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        jsonHttpHandler = new JsonHttpHandler(this.getApplicationContext());
        jsonHttpHandler.setPartUrl("contacts");
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        this.currentPosition = position;
        FragmentManager fragmentManager = getFragmentManager();
        switch (position) {
            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, ContactsFragment.newInstance(position + 1))
                        .commit();
                break;
            case 1:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, AboutFragment.newInstance(position + 1))
                        .commit();
                break;
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_contact);
                break;
            case 2:
                mTitle = getString(R.string.title_about);
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            if(this.currentPosition == 0){
                getMenuInflater().inflate(R.menu.contact, menu);
            }
            else{
                getMenuInflater().inflate(R.menu.main, menu);
            }
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_search) {
            Toast.makeText(this, "Search action.", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_refresh) {
            new FetchContactTask().execute();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyed = true;
    }

    private void displayResponse(List<Contact> contacts) {
        ContactsFragment contactsFragment = (ContactsFragment)getFragmentManager().findFragmentById(R.id.container);
        contactsFragment.update(contacts);
    }


    private void handleSearch() {
        ContactsFragment contactsFragment = (ContactsFragment)getFragmentManager().findFragmentById(R.id.container);
        contactsFragment.search();
    }


    private class FetchContactTask extends AsyncTask<Void, Void, List<Contact>> {

        @Override
        protected void onPreExecute() {
            showLoadingProgressDialog();

        }

        @Override
        protected List<Contact> doInBackground(Void... params) {
            final String url = getString(R.string.base_uri) + "contacts";

            List<Contact> contacts = null;
            HttpGet httpGet=new HttpGet(url);
            //取得HTTP response
            try {
                HttpResponse response=new DefaultHttpClient().execute(httpGet);
                //若状态码为200
                if(response.getStatusLine().getStatusCode()==200){
                    //取出应答字符串
                    HttpEntity entity=response.getEntity();
                    try {
                        String result = EntityUtils.toString(entity, HTTP.UTF_8);
                        ObjectMapper objectMapper = new ObjectMapper();
                        Contact[] value=objectMapper.readValue(result,Contact[].class);
                        contacts = Lists.newArrayList(value);
                    } catch (IOException e) {
                        Log.e(TAG,"",e);
                    }
                }
            } catch (IOException e) {
                Log.e(TAG, "", e);
            }

//            Contact[] value= jsonHttpHandler.getForObject(Contact[].class);
//            contacts = Lists.newArrayList(value);

            return contacts;

        }

        @Override
        protected void onPostExecute(List<Contact> result) {
            dismissProgressDialog();
            displayResponse(result);
        }

    }


    public void showLoadingProgressDialog() {
        this.showProgressDialog("Loading. Please wait...");
    }

    public void showProgressDialog(CharSequence message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
        }

        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && !destroyed) {
            progressDialog.dismiss();
        }
    }

}
