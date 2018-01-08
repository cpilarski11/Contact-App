package com.example.cameronpilarski.contactssqlite;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by cameronpilarski on 11/30/17.
 *
 * this activity allows the user to add a new contact to the database
 *
 * uses a circular image view for profile image from
 * online github repository:
 *
 * https://github.com/hdodenhof/CircleImageView
 *
 *
 */

public class AddContactActivity extends Activity {

    private SystemUIManager system_ui_manager;

    DatabaseHelper myDb;

    private String id_, first_, last_, phone_, email_, address1_, address2_, image_;
    private Drawable pic;

    private Uri image;

    public static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact);

        // hide bottom bar
        system_ui_manager = new SystemUIManager(this);
        system_ui_manager.hideView();

        myDb = new DatabaseHelper(this);

        // declare elements from layout

        final CircleImageView profileImage = (CircleImageView) findViewById(R.id.profile_image_add_contact);
        Button addContact = (Button) findViewById(R.id.add_contact_button);
        ImageButton backButton = (ImageButton) findViewById(R.id.back_button_add_contact);
        final EditText firstName = (EditText) findViewById(R.id.firstName_field);
        final EditText lastName = (EditText) findViewById(R.id.lastName_field);
        final EditText phone = (EditText) findViewById(R.id.phone_field);
        final EditText email = (EditText) findViewById(R.id.email_field);
        final EditText address1 = (EditText) findViewById(R.id.address_field1);
        final EditText address2 = (EditText) findViewById(R.id.address_field2);

        //viewAll();

        // handle on clicks

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // back to home
                Intent myIntent = new Intent(AddContactActivity.this, HomeActivity.class);
                startActivity(myIntent);

            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open images to select profile image
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(intent, PICK_IMAGE);

            }
        });

        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add info to sqlite database
                long long_id = generateRandom(10);
                id_ = Long.toString(long_id);
                first_ = firstName.getText().toString().trim();
                last_ = lastName.getText().toString().trim();
                phone_ = phone.getText().toString().trim();
                email_ = email.getText().toString().trim();
                address1_ = address1.getText().toString().trim();
                address2_ = address2.getText().toString().trim();
                pic = profileImage.getDrawable();
                Bitmap bitmap = convertToBitmap(pic, 200, 200);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                byte[] bitmapdata = stream.toByteArray();
                image_ = Base64.encodeToString(bitmapdata, Base64.DEFAULT);
                //System.out.print(image_);


                // if any are empty, add a space so that
                // app doesnt crash on attributeData outOfBounds
                if (last_.equals("")){
                    last_ = " ";
                }

                if (phone_.equals("")){
                    phone_ = " ";
                }
                if (email_.equals("")){
                    email_ = " ";
                }
                if (address1_.equals("")){
                    address1_ = " ";
                }
                if (address2_.equals("")){
                    address2_ = " ";
                }

                // need at least a name
                if (!first_.equals("")){
                    //System.out.println(image_);
                    boolean isInserted = myDb.insertContactData(id_, first_, last_, phone_, email_, address1_, address2_, image_);
                    //boolean isInserted2 = myDb.insertIDData(id_);
                    if (isInserted) {
                        // display toast
                        Toast.makeText(AddContactActivity.this, "Contact Added", Toast.LENGTH_LONG).show();
                        // reset textfields
                        firstName.setText("");
                        lastName.setText("");
                        phone.setText("");
                        email.setText("");
                        address1.setText("");
                        address2.setText("");
                        //profileImage.setBackground(R.drawable.head);
                        // send back to home
                        Intent myIntent = new Intent(AddContactActivity.this, HomeActivity.class);
                        startActivity(myIntent);
                    } else {
                        // display error toast
                        Toast.makeText(AddContactActivity.this, "Failed to add contact", Toast.LENGTH_LONG).show();

                    }
                }
                else {
                    Toast.makeText(AddContactActivity.this, "Please enter a name", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    @Override
    public void onActivityResult ( int requestCode, int resultCode, Intent data){
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            CircleImageView pro_pic = (CircleImageView) findViewById(R.id.profile_image_add_contact);
            // the address of the image on the SD Card.
            Uri imageUri = data.getData();

            if (null != imageUri) {
                image = imageUri;
                // show the image to the user
                pro_pic.setImageURI(image);
            }

            // declare a stream to read the image data from the SD Card.
            InputStream inputStream;

            // we are getting an input stream, based on the URI of the image.
            try {
                inputStream = getContentResolver().openInputStream(imageUri);

                // get a bitmap from the stream.
                Bitmap image = BitmapFactory.decodeStream(inputStream);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                // show a message to the user indictating that the image is unavailable.
                Toast.makeText(this, "Unable to open image", Toast.LENGTH_LONG).show();
            }

        }else{
            // no image was selected
            Toast.makeText(this, "No image selected", Toast.LENGTH_LONG).show();
        }


    }


    // method for generating a random unique id of a particular length
    public static long generateRandom(int length) {
        Random random = new Random();
        char[] digits = new char[length];
        digits[0] = (char) (random.nextInt(9) + '1');
        for (int i = 1; i < length; i++) {
            digits[i] = (char) (random.nextInt(10) + '0');
        }
        return Long.parseLong(new String(digits));
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    public Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);

        return mutableBitmap;
    }
}
