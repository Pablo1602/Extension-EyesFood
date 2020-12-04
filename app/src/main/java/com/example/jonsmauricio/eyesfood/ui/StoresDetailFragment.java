package com.example.jonsmauricio.eyesfood.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.EyesFoodApi;
import com.example.jonsmauricio.eyesfood.data.api.model.Store;
import com.squareup.picasso.Picasso;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StoresDetailFragment extends DialogFragment {
    /** The system calls this to get the DialogFragment's layout, regardless
     of whether it's being displayed as a dialog or an embedded fragment. */

    final String baseFotoTienda = EyesFoodApi.BASE_URL_ADMIN+"uploads/tiendas/";
    Store tienda;
    int products;
    private ImageView storePhoto;
    private TextView storeName, storePhone, storeWeb, storeEmail, storeAddress, storeDescription, storeFacebook, storeTwitter, storeInstagram;
    private RatingBar storeRating;
    Retrofit mRestAdapter;
    EyesFoodApi mEyesFoodApi;
    Button foods;
    int menu;
    String barcode;
    String url;
    String address;
    String email;
    String phone;
    Intent intent;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        View view = inflater.inflate(R.layout.fragment_store_detail, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbarStoreDetail);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        progressDialog= new ProgressDialog(getActivity());
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        setHasOptionsMenu(true);

        storePhoto = view.findViewById(R.id.ivStoreDetail);
        storeName = view.findViewById(R.id.tvStoreDetailName);
        //storeRating = view.findViewById(R.id.rbStoreDetailRating);
        storePhone = view.findViewById(R.id.tvStorePhone);
        storeWeb = view.findViewById(R.id.tvStoreWeb);
        storeEmail = view.findViewById(R.id.tvStoreEmail);
        storeAddress = view.findViewById(R.id.tvStoreAddress);
        storeDescription = view.findViewById(R.id.tvStoreDetailDescription);
        foods = view.findViewById(R.id.btFoodsStore);

        mRestAdapter = new Retrofit.Builder()
                .baseUrl(EyesFoodApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Crear conexión a la API de EyesFood
        mEyesFoodApi = mRestAdapter.create(EyesFoodApi.class);



        Bundle bundle = this.getArguments();
        if (bundle != null) {
            tienda = (Store) bundle.getSerializable("Tienda");
            products = bundle.getInt("Alimentos");
            menu = bundle.getInt("Menu");
            if (menu==2){
                barcode = bundle.getString("barcode");
            }
            toolbar.setTitle(tienda.getName());
            showStoreData(tienda);
        }

        foods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(products>0) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("Tienda", tienda);
                    bundle.putInt("Menu", menu);
                    if (menu==2){
                        bundle.putString("barcode", barcode);
                    }
                    FoodsStoreFragment detailFragment = new FoodsStoreFragment();
                    detailFragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(android.R.id.content, detailFragment)
                            .addToBackStack(null)
                            .commit();
                }
                else{
                    Toast.makeText(getActivity(), "La tienda no registra alimentos", Toast.LENGTH_LONG).show();
                }
            }
        });

        if (!url.isEmpty()) {
            storeWeb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        url = "http://" + url;
                    }
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                }
            });
        }

        if (!email.isEmpty()) {
            storeEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
                    startActivity(intent);
                }
            });
        }

        if (!address.isEmpty()) {
            storeAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.co.in/maps?q=" + address));
                    startActivity(intent);
                }
            });
        }

        /*storePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                startActivity(intent);
            }
        });*/

        return view;
    }

    private void showStoreData(Store tienda) {
        if (tienda.getPhoto()!=null){
            Picasso.with(getContext())
                    .load(baseFotoTienda + tienda.getPhoto())
                    .into(storePhoto);
        }else{
            Picasso.with(getContext())
                    .load(baseFotoTienda + "default.jpg")
                    .into(storePhoto);
        }

        storeName.setText(tienda.getName());
        //storeRating.setRating(tienda.getReputation());
        storePhone.setText(tienda.getPhone());
        storeWeb.setText(tienda.getWeb());
        storeEmail.setText(tienda.getEmail());
        storeAddress.setText(tienda.getAddress());
        storeDescription.setText(tienda.getDescription());
        url = tienda.getWeb();
        address = tienda.getAddress();
        email = tienda.getEmail();
        phone = tienda.getPhone();
        //getListFoods();
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
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(menu.findItem(R.id.action_settings)!=null){
            menu.findItem(R.id.action_settings).setVisible(false);
            menu.findItem(R.id.searchHistory).setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            StoresFragment storesFragment = new StoresFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            Bundle bundle = new Bundle();
            Log.d("myTag1","Menu = "+menu);
            if (menu==2){
                bundle.putString("barcode", barcode);
            }
            bundle.putInt("Menu", menu);
            storesFragment.setArguments(bundle);
            fragmentManager.beginTransaction().replace(android.R.id.content, storesFragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
