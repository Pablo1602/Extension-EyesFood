package com.example.jonsmauricio.eyesfood.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
import android.view.WindowManager;
import android.widget.Toast;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.EyesFoodApi;
import com.example.jonsmauricio.eyesfood.data.api.model.Food;
import com.example.jonsmauricio.eyesfood.data.api.model.NewFoodBody;
import com.example.jonsmauricio.eyesfood.data.prefs.SessionPrefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditMeasureFragment extends DialogFragment {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    Retrofit mRestAdapter;
    EyesFoodApi mEyesFoodApi;

    private String userIdFinal;

    public EditMeasureFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        View view = inflater.inflate(R.layout.fragment_edit_measure, container, false);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = view.findViewById(R.id.profileEditContainer);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.profileEditTabs);
        tabLayout.setupWithViewPager(mViewPager);

        Toolbar toolbar = view.findViewById(R.id.toolbarEditMeasure);
        toolbar.setTitle(getResources().getString(R.string.edit_measure_title));

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_close_black_24dp);
        }

        setHasOptionsMenu(true);

        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

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
        // TODO: 23-11-2017 Obtener referencias UI y setear
        userIdFinal = SessionPrefs.get(getContext()).getUserId();
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
        menu.findItem(R.id.searchHistory).setVisible(false);
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.sendFood).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home){
            refreshProfile();
            dismiss();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshProfile() {
        Bundle args = new Bundle();
        args.putString(ProfileFragment.ARG_SECTION_TITLE, "Mi perfil");

        Fragment fragmentProfile = ProfileFragment.newInstance("Mi perfil");
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentProfile.setArguments(args);

        fragmentManager
                .beginTransaction()
                .replace(R.id.llMainActivity, fragmentProfile, "fragmento_perfil")
                .commit();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    TabWeight tabWeight = new TabWeight();
                    return tabWeight;
                case 1:
                    TabFat tabFat = new TabFat();
                    return tabFat;
                case 2:
                    TabWaist tabWaist = new TabWaist();
                    return tabWaist;
                case 3:
                    TabA1c tabA1c = new TabA1c();
                    return tabA1c;
                case 4:
                    TabPreGlu tabPreGlu = new TabPreGlu();
                    return tabPreGlu;
                case 5:
                    TabPostGlu tabPostGlu = new TabPostGlu();
                    return tabPostGlu;
                case 6:
                    TabPressure tabPressure = new TabPressure();
                    return tabPressure;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "PESO";
                case 1:
                    return "GRASA";
                case 2:
                    return "CINTURA";
                case 3:
                    return "A1C";
                case 4:
                    return "GLUCOSA PREPRANDIAL";
                case 5:
                    return "GLUCOSA POSTPRANDIAL";
                case 6:
                    return "PRESIÓN ARTERIAL";
            }
            return null;
        }
    }
}