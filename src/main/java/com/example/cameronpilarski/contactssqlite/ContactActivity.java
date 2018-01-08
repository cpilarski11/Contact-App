package com.example.cameronpilarski.contactssqlite;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by cameronpilarski on 12/1/17.
 *
 * this screen acts as the focus activity for viewing a particular contact
 * selected from the list view
 *
 * * uses a circular image view for profile image from
 * online github repository:
 *
 * https://github.com/hdodenhof/CircleImageView
 *
 *
 */

public class ContactActivity extends Activity {

    private SystemUIManager system_ui_manager;

    DatabaseHelper myDb;

    private String contactData;

    private String id_attribute, first_attribute, last_attribute, phone_attribute, email_attribute, add1_attribute, add2_attribute, image_attribute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);

        // hide bottom bar
        system_ui_manager = new SystemUIManager(this);
        system_ui_manager.hideView();

        myDb = new DatabaseHelper(this);

        contactData = getIntent().getStringExtra("CONTACT");
        String[] attributeData = contactData.split("/");

        //System.out.println(contactData);

        for (String t : attributeData)
            System.out.println(t);

        id_attribute = attributeData[0];
        first_attribute = attributeData[1];
        last_attribute = attributeData[2];
        phone_attribute = attributeData[3];
        email_attribute = attributeData[4];
        add1_attribute = attributeData[5];
        add2_attribute = attributeData[6];
        image_attribute = attributeData[7];

        byte[] data = Base64.decode(image_attribute, Base64.DEFAULT);
        final Bitmap bitmap = BitmapFactory.decodeByteArray(data , 0, data.length);


        // declare elements from layout

        Button deleteContact = (Button) findViewById(R.id.delete_contact_button);
        ImageButton backButton = (ImageButton) findViewById(R.id.back_button_contact);
        ImageButton editButton = (ImageButton) findViewById(R.id.edit_contact_button);
        CircleImageView profileImage = (CircleImageView) findViewById(R.id.profile_image_contact);
        profileImage.setBackgroundResource(R.drawable.head);
        TextView first = (TextView) findViewById(R.id.first);
        TextView last = (TextView) findViewById(R.id.last);
        TextView phone = (TextView) findViewById(R.id.phone);
        TextView email = (TextView) findViewById(R.id.email);
        TextView address1 = (TextView) findViewById(R.id.address1);
        TextView address2 = (TextView) findViewById(R.id.address2);
        TextView firstTop = (TextView) findViewById(R.id.first_);
        TextView lastTop = (TextView) findViewById(R.id.last_);

        // set textviews

        first.setText(first_attribute);
        last.setText(last_attribute);
        phone.setText(phone_attribute);
        email.setText(email_attribute);
        address1.setText(add1_attribute);
        address2.setText(add2_attribute);

        firstTop.setText(first_attribute);
        lastTop.setText(last_attribute);

        profileImage.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        profileImage.setImageBitmap(bitmap);


        // handle on clicks

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent (ContactActivity.this, HomeActivity.class);
                startActivity(myIntent);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send intent to edit contact, passing data within intent
                final Dialog dialog = new Dialog(ContactActivity.this); // Context, this, etc.
                dialog.setContentView(R.layout.dialog2);
                dialog.setTitle("Edit Contact");
                dialog.show();

                Button cancel = (Button) dialog.findViewById(R.id.dialog_cancel);
                Button edit = (Button) dialog.findViewById(R.id.dialog_delete);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                // delete contact from database
                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // display toast
                        // return to home screen
                        Intent myIntent = new Intent(ContactActivity.this, EditContactActivity.class);
                        myIntent.putExtra("CONTACT", contactData);
                        startActivity(myIntent);
                    }
                });
            }
        });

        deleteContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(ContactActivity.this); // Context, this, etc.
                dialog.setContentView(R.layout.dialog);
                dialog.setTitle("Delete Contact");
                dialog.show();

                Button cancel = (Button) dialog.findViewById(R.id.dialog_cancel);
                Button delete = (Button) dialog.findViewById(R.id.dialog_delete);

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
                        myDb.deleteData(id_attribute);
                        Toast.makeText(ContactActivity.this, "Contact Deleted", Toast.LENGTH_LONG).show();
                        Intent myIntent = new Intent(ContactActivity.this, HomeActivity.class);
                        startActivity(myIntent);
                    }
                });

            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send to full screen activity
                // pass bitmap
                Intent myIntent = new Intent(ContactActivity.this, FullScreenActivity.class);
                startActivity(myIntent);
            }
        });





    }

    private byte[] bitmapToByte(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public void writeJpegImageToFile(Bitmap bitmap, FileOutputStream jpegFileStream) {
        // use JPEG quality of 80 (scale 1 - 100)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, jpegFileStream);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
