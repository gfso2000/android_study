package com.example.i325639.myapplication.contacts;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.i325639.myapplication.R;

public class ContactActivity extends AppCompatActivity implements
        ContactsListFragment.OnContactsInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
    }

    @Override
    public void onContactSelected(Uri contactUri) {
//        Intent intent = new Intent(this, ContactDetailActivity.class);
//        intent.setData(contactUri);
//        startActivity(intent);
    }

    @Override
    public void onSelectionCleared() {

    }
}
