package com.example.jonsmauricio.eyesfood.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.jonsmauricio.eyesfood.R;
import com.example.jonsmauricio.eyesfood.data.api.UserDataApi;
import com.example.jonsmauricio.eyesfood.data.api.model.Measure;
import com.example.jonsmauricio.eyesfood.data.prefs.SessionPrefs;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Fragmento para el contenido principal
 */
public class ProfileFragment extends Fragment {

    private String userIdFinal;
    Retrofit mRestAdapter;
    UserDataApi mUserDataApi;

    private List<Measure> listaPeso;
    private List<Measure> listaGrasa;
    private List<Measure> listaCintura;
    private List<Measure> listaA1c;
    private List<Measure> listaPreGlu;
    private List<Measure> listaPostGlu;
    private List<Measure> listaPresion;

    private List<List<Measure>> listaGraficos;
    //Este argumento del fragmento representa el título de cada sección
    public static final String ARG_SECTION_TITLE = "section_number";

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager viewPager;
    private LinearLayout sliderDotsPanel;
    private int dotsCount;
    private ImageView[] dots;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    public static ProfileFragment newInstance(String sectionTitle) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_TITLE, sectionTitle);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        viewPager = view.findViewById(R.id.vpViewPagerProfile);
        sliderDotsPanel = view.findViewById(R.id.llSliderDotsPanelProfile);

        // Set up the ViewPager with the sections adapter.
        mViewPager = view.findViewById(R.id.profileContainer);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.profileTabs);
        tabLayout.setupWithViewPager(mViewPager);

        userIdFinal = SessionPrefs.get(getContext()).getUserId();
        //graph = (GraphView) view.findViewById(R.id.graph);

        // Crear conexión al servicio REST
        mRestAdapter = new Retrofit.Builder()
                .baseUrl(UserDataApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Crear conexión a la API de EyesFood
        mUserDataApi = mRestAdapter.create(UserDataApi.class);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listaGraficos = new ArrayList<>();
        listaGraficos.clear();
        loadWeight(userIdFinal);

        /*SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateInString = "2017-11-30 17:22:17";

        try {
            Date date = formatter.parse(dateInString);
            Log.d("fecha",date.toString());
            Log.d("fecha",formatter.format(date));
            java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date d1 = sdf.parse("21/12/2012 17:22:17");
            Date d2 = sdf.parse("22/12/2012 17:22:17");
            Date d3 = sdf.parse("23/12/2012 17:22:17");
            Date d4 = sdf.parse("24/12/2012 17:22:17");
            Date d5 = sdf.parse("25/12/2012 17:22:17");
            Date d6 = sdf.parse("26/12/2012 17:22:17");

            GraphView graph = (GraphView) view.findViewById(R.id.graph);

            // you can directly pass Date objects to DataPoint-Constructor
            // this will convert the Date to double via Date#getTime()
            LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                    new DataPoint(d1, 1),
                    new DataPoint(d2, 5),
                    new DataPoint(d3, 3),
                    new DataPoint(d4, 1),
                    new DataPoint(d5, 5),
                    new DataPoint(d6, 3)
            });
            graph.addSeries(series);

            // set date label formatter
            graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
            graph.getGridLabelRenderer().setNumHorizontalLabels(3); // only 3 because of the space

            // set manual x bounds to have nice steps
            graph.getViewport().setMinX(d1.getTime());
            graph.getViewport().setMaxX(d6.getTime());
            graph.getViewport().setXAxisBoundsManual(true);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    TabGeneral tabGeneral = new TabGeneral();
                    return tabGeneral;
                case 1:
                    TabSpecific tabSpecific = new TabSpecific();
                    return tabSpecific;
                case 2:
                    TabAllergy tabAllergy = new TabAllergy();
                    return tabAllergy;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "GENERAL";
                case 1:
                    return "ESPECÍFICO";
                case 2:
                    return "ALERGIAS";
            }
            return null;
        }
    }

    public void loadWeight(final String userId){
        Call<List<Measure>> call = mUserDataApi.getWeight(userId);
        call.enqueue(new Callback<List<Measure>>() {
            @Override
            public void onResponse(Call<List<Measure>> call,
                                   Response<List<Measure>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                listaPeso = response.body();
                makeList(listaPeso);
                loadFat(userId);
            }

            @Override
            public void onFailure(Call<List<Measure>> call, Throwable t) {

            }
        });
    }

    public void loadFat(final String userId){
        Call<List<Measure>> call = mUserDataApi.getFat(userId);
        call.enqueue(new Callback<List<Measure>>() {
            @Override
            public void onResponse(Call<List<Measure>> call,
                                   Response<List<Measure>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                listaGrasa = response.body();
                makeList(listaGrasa);
                loadWaist(userId);
            }

            @Override
            public void onFailure(Call<List<Measure>> call, Throwable t) {

            }
        });
    }

    public void loadWaist(final String userId){
        Call<List<Measure>> call = mUserDataApi.getWaist(userId);
        call.enqueue(new Callback<List<Measure>>() {
            @Override
            public void onResponse(Call<List<Measure>> call,
                                   Response<List<Measure>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                listaCintura = response.body();
                makeList(listaCintura);
                loadA1c(userId);
            }

            @Override
            public void onFailure(Call<List<Measure>> call, Throwable t) {

            }
        });
    }

    public void loadA1c(final String userId){
        Call<List<Measure>> call = mUserDataApi.getA1c(userId);
        call.enqueue(new Callback<List<Measure>>() {
            @Override
            public void onResponse(Call<List<Measure>> call,
                                   Response<List<Measure>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                listaA1c = response.body();
                makeList(listaA1c);
                loadGluPre(userId);
            }

            @Override
            public void onFailure(Call<List<Measure>> call, Throwable t) {

            }
        });
    }

    public void loadGluPre(final String userId){
        Call<List<Measure>> call = mUserDataApi.getPreglucose(userId);
        call.enqueue(new Callback<List<Measure>>() {
            @Override
            public void onResponse(Call<List<Measure>> call,
                                   Response<List<Measure>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                listaPreGlu = response.body();
                makeList(listaPreGlu);
                loadGluPost(userId);
            }

            @Override
            public void onFailure(Call<List<Measure>> call, Throwable t) {

            }
        });
    }

    public void loadGluPost(final String userId){
        Call<List<Measure>> call = mUserDataApi.getPostglucose(userId);
        call.enqueue(new Callback<List<Measure>>() {
            @Override
            public void onResponse(Call<List<Measure>> call,
                                   Response<List<Measure>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                listaPostGlu = response.body();
                makeList(listaPostGlu);
                loadPressure(userId);
            }

            @Override
            public void onFailure(Call<List<Measure>> call, Throwable t) {

            }
        });
    }

    public void loadPressure(final String userId){
        Call<List<Measure>> call = mUserDataApi.getPressure(userId);
        call.enqueue(new Callback<List<Measure>>() {
            @Override
            public void onResponse(Call<List<Measure>> call,
                                   Response<List<Measure>> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                listaPresion = response.body();
                makeList(listaPresion);
                makeGraphs(listaGraficos);
            }

            @Override
            public void onFailure(Call<List<Measure>> call, Throwable t) {

            }
        });
    }

    private void makeGraphs(List<List<Measure>> listaGraficos) {
        ViewPagerProfileAdapter viewPagerProfileAdapter = new ViewPagerProfileAdapter(getContext(), listaGraficos);
        viewPager.setAdapter(viewPagerProfileAdapter);
        dotsCount = viewPagerProfileAdapter.getCount();
        dots = new ImageView[dotsCount];

        for(int i = 0; i<dotsCount; i++){
            dots[i] = new ImageView(getContext());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(8, 0, 8, 0);
            sliderDotsPanel.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dot_black));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int i;
                for(i=0; i<dotsCount; i++){
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.nonactive_dot));
                }

                dots[position].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dot_black));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void makeList(List<Measure> listaMedida){
        listaGraficos.add(listaMedida);
    }
}