package com.example.cameronpilarski.contactssqlite;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by cameronpilarski on 12/2/17.
 *
 *  this activity serves as a page tp edit contact info
 *
 */

public class EditContactActivity extends Activity {

    private SystemUIManager system_ui_manager;

    DatabaseHelper myDb;

    public static final int PICK_IMAGE = 1;

    private String contactData;

    private String id_attribute, image_attribute, first_attribute, last_attribute, phone_attribute, email_attribute, add1_attribute, add2_attribute;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_contact);

        // hide bottom bar
        system_ui_manager = new SystemUIManager(this);
        system_ui_manager.hideView();

        myDb = new DatabaseHelper(this);

        contactData = getIntent().getStringExtra("CONTACT");
        String[] attributeData = contactData.split("/");

        //System.out.println(contactData);

        //for (String t : attributeData)
        //System.out.println(t);

        id_attribute = attributeData[0];
        first_attribute = attributeData[1];
        last_attribute = attributeData[2];
        phone_attribute = attributeData[3];
        email_attribute = attributeData[4];
        add1_attribute = attributeData[5];
        add2_attribute = attributeData[6];
        image_attribute = attributeData[7];

        // references for elements
        Button cancelChanges = (Button) findViewById(R.id.cancel_changes);
        Button makeChanges = (Button) findViewById(R.id.make_changes);
        CircleImageView profileImage = (CircleImageView) findViewById(R.id.profile_image_edit_contact);
        final EditText first = (EditText) findViewById(R.id.firstName_field_);
        final EditText last = (EditText) findViewById(R.id.lastName_field_);
        final EditText phone = (EditText) findViewById(R.id.phone_field_);
        final EditText email = (EditText) findViewById(R.id.email_field_);
        final EditText address1 = (EditText) findViewById(R.id.address_field1_);
        final EditText address2 = (EditText) findViewById(R.id.address_field2_);

        // set textfields

        first.setText(first_attribute);
        last.setText(last_attribute);
        phone.setText(phone_attribute);
        email.setText(email_attribute);
        address1.setText(add1_attribute);
        address2.setText(add2_attribute);

        cancelChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // go back to contact page
                // pass contact info in intent
                Intent myIntent = new Intent(EditContactActivity.this, ContactActivity.class);
                myIntent.putExtra("CONTACT", contactData);
                startActivity(myIntent);


            }
        });

        makeChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // update database
                // send toast
                // intent back to home

                String first_ = first.getText().toString().trim();
                String last_ = last.getText().toString().trim();
                String phone_ = phone.getText().toString().trim();
                String email_ = email.getText().toString().trim();
                String add1_ = address1.getText().toString().trim();
                String add2_ = address2.getText().toString().trim();

                myDb.updateContactData(id_attribute, first_, last_, phone_, email_, add1_, add2_ );
                Toast.makeText(EditContactActivity.this, "Contact updated", Toast.LENGTH_LONG).show();
                // send back to home
                Intent myIntent = new Intent(EditContactActivity.this, HomeActivity.class);
                startActivity(myIntent);
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open images to select profile image
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(intent, PICK_IMAGE);
            }
        });

    }

    @Override
    public void onActivityResult ( int requestCode, int resultCode, Intent data){
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            CircleImageView pro_pic = (CircleImageView) findViewById(R.id.profile_image_edit_contact);
            // the address of the image on the SD Card.
            Uri imageUri = data.getData();

            // declare a stream to read the image data from the SD Card.
            InputStream inputStream;

            // we are getting an input stream, based on the URI of the image.
            try {
                inputStream = getContentResolver().openInputStream(imageUri);

                // get a bitmap from the stream.
                Bitmap image = BitmapFactory.decodeStream(inputStream);


                // show the image to the user
                pro_pic.setImageBitmap(image);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                // show a message to the user indictating that the image is unavailable.
                Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
            }

        }
        else{
            // no image was selected
            Toast.makeText(this, "No image selected", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
