package com.example.jonsmauricio.eyesfood.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.DialogFragment;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.EyesFoodApi;
import com.example.jonsmauricio.eyesfood.data.api.model.Food;
import com.example.jonsmauricio.eyesfood.data.prefs.SessionPrefs;
import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;

public class UploadImageDialogFragment extends DialogFragment {
    /** The system calls this to get the DialogFragment's layout, regardless
     of whether it's being displayed as a dialog or an embedded fragment. */

    //FoodImage request code
    private int PICK_IMAGE_REQUEST = 1;
    private int CAMERA_REQUEST = 2;

    private Uri filePath = null;
    String absolutePath;

    Button fromCamera, fromGallery;
    RadioButton front, ingredients, nutrients;
    ImageView photoSelected;

    Food Alimento;
    String tipoAlimento;
    String barCode;

    Retrofit mRestAdapter;
    EyesFoodApi mEyesFoodApi;

    //Obtengo token e id de Usuario
    private String userIdFinal;

    private String urlUpload;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        View view = inflater.inflate(R.layout.dialog_upload_images, container, false);

        //barCode = getArguments().getString("barCode");

        Toolbar toolbar = view.findViewById(R.id.toolbarUploadImages);

        toolbar.setTitle(getResources().getString(R.string.title_upload_images));

        Alimento = (Food) getArguments().getSerializable("Alimento");
        //1 aprobado
        //2 pendiente
        tipoAlimento = (String) getArguments().getSerializable("tipo");
        barCode = Alimento.getBarCode();

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_close_black_24dp);
        }

        setHasOptionsMenu(true);

        // Crear conexión al servicio REST
        mRestAdapter = new Retrofit.Builder()
                .baseUrl(EyesFoodApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Crear conexión a la API de EyesFood
        mEyesFoodApi = mRestAdapter.create(EyesFoodApi.class);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        fromCamera = getView().findViewById(R.id.btUploadCamera);
        fromGallery = getView().findViewById(R.id.btUploadGallery);
        photoSelected = getView().findViewById(R.id.iv_upload_photo_selected);
        front = getView().findViewById(R.id.rbFront);
        ingredients = getView().findViewById(R.id.rbIngredients);
        nutrients = getView().findViewById(R.id.rbNutrients);

        fromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST);
            }
        });
        fromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    showFileChooser();
            }
        });
        userIdFinal = SessionPrefs.get(getContext()).getUserId();
        urlUpload = EyesFoodApi.BASE_URL + "images/" + userIdFinal + "/" + barCode;
    }

    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_new_foods, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_foods_comments).setVisible(false);
        menu.findItem(R.id.action_foods_complaint).setVisible(false);
        menu.findItem(R.id.action_foods_add_photos).setVisible(false);
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                Bitmap thumbnail = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 10, bytes);

                File destination = new File(Environment.getExternalStorageDirectory(),"temp.jpg");
                FileOutputStream fo;
                try {
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                absolutePath = destination.getAbsolutePath();
                /*Picasso.with(getContext())
                        .load(filePath)
                        .resize(800,800)
                        .into(photoSelected);*/

                Glide.with(getContext())
                        .load(filePath)
                        .into(photoSelected);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            filePath = data.getData();
            absolutePath = getPath(filePath);

                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 10, bytes);

                File destination = new File(Environment.getExternalStorageDirectory(),"temp.jpg");
                FileOutputStream fo;
                try {
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                absolutePath = destination.getAbsolutePath();
            Picasso.with(getContext())
                    .load(filePath)
                    .resize(800,800)
                    .into(photoSelected);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.sendFood) {
            if(filePath == null){
                showEmptyUploadDialog();
            }else if (!front.isChecked() && !ingredients.isChecked() && !nutrients.isChecked()){
                showEmptyRadioDialog();
            }
            else {
                //Uploading code
                try {
                    String url;
                    if (front.isChecked()){
                        url = urlUpload + "/0/";
                    }else if(ingredients.isChecked()){
                        url = urlUpload + "/1/";
                    }else{
                        url = urlUpload + "/2/";
                    }
                    url = url +tipoAlimento;
                    Log.d("myTag", url);
                    String uploadId = UUID.randomUUID().toString();
                        //Creating a multi part request
                        new MultipartUploadRequest(getContext(), uploadId, url)
                                .addFileToUpload(absolutePath, "myFile") //Adding file
                                .setNotificationConfig(new UploadNotificationConfig()
                                        .setCompletedMessage(getResources().getString(R.string.body_notification_upload_images))
                                        .setTitle(getResources().getString(R.string.title_notification_upload_images)))
                                .setMaxRetries(2)
                                .startUpload(); //Starting the upload

                        showSuccesDialog();

                } catch (Exception exc) {
                    Toast.makeText(getContext(), exc.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        } else if(id == android.R.id.home){
            dismiss();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showEmptyRadioDialog() {
        new AlertDialog.Builder(getContext())
                .setIcon(null)
                .setTitle(getResources().getString(R.string.title_failed_solitude_new_foods))
                .setMessage(getResources().getString(R.string.message_failed_solitude_upload_rb))
                .setPositiveButton(getResources().getString(R.string.ok_dialog), null)
                .show();
    }

    public void showSuccesDialog(){
        new AlertDialog.Builder(getContext())
                .setIcon(null)
                .setTitle(getResources().getString(R.string.title_success_solitude_new_foods))
                .setMessage(getResources().getString(R.string.message_success_solitude_new_foods))
                .setPositiveButton(getResources().getString(R.string.ok_dialog), new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }

                })
                .show();
    }

    public void showEmptyUploadDialog(){
        new AlertDialog.Builder(getContext())
                .setIcon(null)
                .setTitle(getResources().getString(R.string.title_failed_solitude_new_foods))
                .setMessage(getResources().getString(R.string.message_failed_solitude_upload))
                .setPositiveButton(getResources().getString(R.string.ok_dialog), null)
                .show();
    }
    //method to get the file path from uri
    public String getPath(Uri uri) {
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContext().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
}