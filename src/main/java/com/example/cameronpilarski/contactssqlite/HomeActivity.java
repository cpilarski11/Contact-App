package com.example.cameronpilarski.contactssqlite;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by cameronpilarski on 11/30/17.
 *
 *  this activity acts as the home screen for viewing your particular set
 *  of contacts. Displays contacts in a list view
 *
 *  TODO need to order contacts alphabetically
 *  search is alphabetical though..
 *
 *  TODO need to handle storing images on internal storage
 *  TODO need to add reference to database
 *
 */

public class HomeActivity extends Activity implements SearchView.OnQueryTextListener {

    // reference to database
    DatabaseHelper myDb;

    private SystemUIManager system_ui_manager;

    private SearchView mSearchView;
    private ListView lv;


    // array list for contact first and last names
    public ArrayList<String> first_last_array;
    public ArrayList<String> contacts_array;

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        // hide bottom bar
        system_ui_manager = new SystemUIManager(this);
        system_ui_manager.hideView();

        myDb = new DatabaseHelper(this);

        //viewAll();

        first_last_array = new ArrayList<String>();
        contacts_array = new ArrayList<String>();

        addToList();
        addToContactList();

        //System.out.println(contacts_array.get(0));

        // references to elements
        Button add = (Button) findViewById(R.id.add_contact);
        mSearchView = (SearchView) findViewById(R.id.search_contact);
        // Get reference of widgets from XML layout
        lv = (ListView) findViewById(R.id.contact_list);


        // DataBind ListView with items from ArrayAdapter
        lv.setAdapter(new ArrayAdapter<String>(HomeActivity.this, R.layout.row, first_last_array));

        lv.setTextFilterEnabled(true);
        setupSearchView();
        //closeKeyboard();


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go to add contact activity
                Intent myIntent = new Intent(HomeActivity.this, AddContactActivity.class);
                startActivity(myIntent);
            }
        });


        lv.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //String cities = String.valueOf(parent.getItemAtPosition(position));
                        //Toast.makeText(HomeActivity.this, cities, Toast.LENGTH_LONG).show();
                        Intent myIntent = new Intent(HomeActivity.this, ContactActivity.class);
                        myIntent.putExtra("CONTACT", contacts_array.get(position));
                        startActivity(myIntent);
                    }
                }
        );

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                final Dialog dialog = new Dialog(HomeActivity.this); // Context, this, etc.
                dialog.setContentView(R.layout.dialog3);
                dialog.setTitle("Edit Contact");
                dialog.show();

                Button cancel = (Button) dialog.findViewById(R.id.dialog3_cancel);
                Button delete = (Button) dialog.findViewById(R.id.dialog3_delete);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                // delete contact from database
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // display toast
                        // return to home screen
                        //myDb.deleteData(id_attribute);
                        //Intent myIntent = new Intent(HomeActivity.this, EditContactActivity.class);
                        //startActivity(myIntent);

                        String s = contacts_array.get(i);
                        String[] attributeData = s.split("/");

                        //System.out.println(contactData);

                        //for (String t : attributeData)
                        //System.out.println(t);

                        String id_attribute = attributeData[0];
                        myDb.deleteData(id_attribute);
                        //System.out.println(s);
                        dialog.dismiss();
                        Toast.makeText(HomeActivity.this, "Contact deleted", Toast.LENGTH_LONG).show();
                        Intent myIntent = new Intent(HomeActivity.this, HomeActivity.class);
                        startActivity(myIntent);

                    }
                });
                return true;
            }
        });
    }

    private void setupSearchView() {
        // set to true to dismiss keyboard automatically
        mSearchView.setIconifiedByDefault(true);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setSubmitButtonEnabled(true);
        mSearchView.setQueryHint("Search Here");
    }

    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            lv.clearTextFilter();
        } else {
            lv.setFilterText(newText.toString());
        }
        return true;
    }

    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    public void addToList(){
        //TextView NoContacts = (TextView) findViewById(R.id.no_contacts);
        Cursor res = myDb.getContactFirstLast();
        if(res.getCount() == 0) {
            // show message
            showMessage("Contacts","No Contacts");

            return;
        }
        while (res.moveToNext()) {
            int i = 0;
            first_last_array.add(res.getString(i) + " " + res.getString(i+1));

        }
    }

    public void addToContactList(){
        Cursor res = myDb.getAllContactData();
        if(res.getCount() == 0) {
            // show message
            //showMessage("Contacts","No Contacts to show");
            return;
        }
        while (res.moveToNext()) {
            int i = 0;
            contacts_array.add(res.getString(i) + "/" + res.getString(i+1) + "/" + res.getString(i+2) + "/" +
                    res.getString(i+3) + "/" + res.getString(i+4) + "/" + res.getString(i+5) + "/" + res.getString(i+6) + "/" + res.getString(i+7));

        }
    }


    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}