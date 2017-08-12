package com.example.vadim.dpapp.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.vadim.dpapp.adapters.CompliteTaskAdapter;
import com.example.vadim.dpapp.application.DBHelper;
import com.example.vadim.dpapp.R;
import com.example.vadim.dpapp.application.RESTController;
import com.example.vadim.dpapp.containers.ActivContainer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ElementActivity extends AppCompatActivity {
    static final int GALLERY_REQUEST = 1;
    static final int CAMERA_REQUEST = 3;
    private static final int REQUEST_ID_READ_WRITE_PERMISSION = 100 ;
    private static final int CAMERA_RESULT = 101;
    static String shtrihhh="";
    ImageView imageView;
    EditText editTextName;
    EditText editCode;
    EditText editTextTypeActiv;
    EditText editTextShtrihCode;
    Spinner spinner;
    Button loadImage;
    String code;
    String name;
    String shtrih;
    String strImage="";
    String contractor = null;
    boolean flag;
    DBHelper dbHelper;
    RESTController rest;
    Button scaning;
    Intent intent;
    boolean flagPhoto=false;
    boolean anotherFlag = true;
    Context context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(this);
        dbHelper.create_db();
        context = this;
        rest = new RESTController(this,ElementActivity.class.getSimpleName());
        setContentView(R.layout.activ_element);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageView = (ImageView) findViewById(R.id.imageView3);
        editCode = (EditText) findViewById(R.id.editCodeActiv);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextShtrihCode = (EditText) findViewById(R.id.editTextShtrihCode);
        editTextTypeActiv = (EditText) findViewById(R.id.editTextTypeActiv);
        loadImage = (Button) findViewById(R.id.loadImg);
        registerForContextMenu(loadImage);
        /*loadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });*/
        scaning = (Button) findViewById(R.id.scaning);
        scaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ElementActivity.this, Pizdec.class);
                startActivityForResult(intent, 2);
            }
        });
        spinner = (Spinner) findViewById(R.id.activ_spiner_with_contr);
        getInfoElement();
        rest.getContractors(spinner,contractor);
        rest.getActivImage(imageView,shtrih);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_menu_save);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anotherFlag = true;
                if(editTextName.getText().toString().equals("")){
                    editTextName.setHint("Заполните поле!");
                    editTextName.setHintTextColor(Color.RED);
                    anotherFlag = false;
                }
                if(editTextShtrihCode.getText().toString().equals("")){
                    editTextShtrihCode.setHint("Заполните поле!");
                    editTextShtrihCode.setHintTextColor(Color.RED);
                    anotherFlag = false;
                }
                if(anotherFlag) {
                    if (flagPhoto) {
                        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                        int optimalSize = 500;
                        double optimalWidth;
                        double optimalHeight;

                        if (bitmap.getWidth() > bitmap.getHeight()) {
                            double x = bitmap.getWidth() / optimalSize;
                            optimalWidth = optimalSize;
                            optimalHeight = bitmap.getHeight() / x;
                        } else {
                            double x = bitmap.getHeight() / optimalSize;
                            optimalHeight = optimalSize;
                            optimalWidth = bitmap.getWidth() / x;
                        }
                        bitmap = Bitmap.createScaledBitmap(bitmap, (int) optimalWidth,
                                (int) optimalHeight, false);
                        strImage = convertBitmapToBase64String(bitmap);
                    }
                    Snackbar.make(view, "Изменения успешно сохранены", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    rest.sendActiv(editCode.getText().toString(), editTextName.getText().toString(), editTextTypeActiv.getText().toString(), editTextShtrihCode.getText().toString(), strImage, spinner.getSelectedItem().toString());
                    //finish();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (imageReturnedIntent == null) {
            return;
        }
        Bitmap bitmap = null;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST:
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        flagPhoto = true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    bitmap = ScaleImage(bitmap);
                    imageView.setImageBitmap(bitmap);
                    flagPhoto = true;
                    break;
                case 2:
                    editTextShtrihCode.setText(ElementActivity.shtrihhh);
                    break;
                case CAMERA_RESULT:
                    bitmap = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    imageView.setImageBitmap(bitmap);
                    strImage = convertBitmapToBase64String(bitmap);
                    break;
            }
        }
    }
    public void getInfoElement(){
        Intent intent = getIntent();
        name = intent.getStringExtra("nameActiv");
        shtrih = intent.getStringExtra("shtrihCode");
        code = intent.getStringExtra("code");
        flag = intent.getBooleanExtra("flag",true);
        contractor = intent.getStringExtra("contragent");

        ArrayList<ActivContainer> list = dbHelper.getAllActiv();
        if(flag){
            if(list.size()!=0){
                int lastCode=Integer.parseInt(list.get(list.size() - 1).getCode());
                lastCode++;
                code = String.valueOf(lastCode);
            }
            else{
                code=String.valueOf(1);
            }
        }

        editCode.setText(code);
        editTextName.setText(name);
        editTextShtrihCode.setText(shtrih);
        if(intent.getStringExtra("photo")!=null) {
            imageView.setImageBitmap(convertBase64StringToBitmap(intent.getStringExtra("photo")));
        }
        else {
            imageView.setImageResource(R.drawable.nophoto);
        }
    }

    public static  Bitmap convertBase64StringToBitmap(String source) {
        byte[] rawBitmap = Base64.decode(source.getBytes(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(rawBitmap, 0, rawBitmap.length);
        return bitmap;
    }
    public static String convertBitmapToBase64String(Bitmap source) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        source.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] image = stream.toByteArray();
        String encodedImage = Base64.encodeToString(image, Base64.DEFAULT);
        return encodedImage;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_camera, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Intent photoPickerIntent;
        switch (item.getItemId()) {
            case R.id.camera:
                //photoPickerIntent = new Intent(Intent.ACTION_CAMERA_BUTTON);
                //photoPickerIntent.setType("image/*");
                //startActivityForResult(photoPickerIntent, CAMERA_REQUEST);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //intent.setAction(Intent.ACTION_CAMERA_BUTTON);
                //intent.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(KeyEvent.ACTION_DOWN,
                //        KeyEvent.KEYCODE_CAMERA));
                startActivityForResult(intent, CAMERA_RESULT);
                return true;
            case R.id.photo_gallery:
                photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public Bitmap ScaleImage(Bitmap bitmap){
        int optimalSize = 500;
        double optimalWidth;
        double optimalHeight;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            double x = bitmap.getWidth() / optimalSize;
            optimalWidth = optimalSize;
            optimalHeight = bitmap.getHeight() / x;
        } else {
            double x = bitmap.getHeight() / optimalSize;
            optimalHeight = optimalSize;
            optimalWidth = bitmap.getWidth() / x;
        }
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) optimalWidth,
                (int) optimalHeight, false);
        return bitmap;
    }


    @Override
    public void registerForContextMenu(View view) {
        super.registerForContextMenu(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openContextMenu(v);
            }
        });
    }
}
