package com.canpolata.canpolat.mortgagesabc;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.EdgeDetail;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.charts.SeriesLabel;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by Canpolat on 26/03/2016.
 */
public class ResultsMain extends MainActivity {

    public static double monthlyPayment;
    public static double totalPayment;
    public static double totalInterest;
    public static double totalMortgage;
    public static double percentInterest;
    public static double percentRepayment;

    final private int COLOR_EDGE = Color.parseColor("#22000000");
    public final int COLOR_PINK = Color.parseColor("#FF4081");
    final private int COLOR_BACK = Color.parseColor("#0166BB66");
    public final int COLOR_GREEN = Color.parseColor("#68EFAD");
    public final int COLOR_PURPLE = Color.parseColor("#691A99");
    public final int COLOR_GREY = Color.parseColor("#C5CAE9");

    private DecoView arcView;
    private DecoView decoView;
    public int mBack1Index;
    public int mSeries1Index;
    public int mSeries2Index;
    //here I will need to set the max data value for the curve
    private final float mSeriesMax = 100f;
    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private List<Person> persons;
    private ArrayList<AHBottomNavigationItem> bottomNavigationItems = new ArrayList<>();
    //public AHBottomNavigationViewPager viewPager;
    //public FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        initUI();
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //layoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        initializeData();
        initializeAdapter();

        decoView = (DecoView) findViewById(R.id.dynamicArcView);
        arcView = (DecoView)findViewById(R.id.dynamicArcView);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }


    }


    private void initializeData(){

        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        Bundle b = getIntent().getExtras();
        monthlyPayment = b.getDouble("amount");
        totalPayment = b.getDouble("interest");
        totalMortgage = b.getDouble("totalMortgage");
        totalInterest = b.getDouble("totalInterest");

        persons = new ArrayList<>();
        persons.add(new Person("Monthly payments","£ " + decimalFormat.format(monthlyPayment), R.drawable.ic_account_balance_black_36dp, R.color.colorActiveSmall));
        persons.add(new Person("Yearly payments", "£ " + decimalFormat.format(totalPayment), R.drawable.ic_assessment_black_36dp, R.color.colorActiveSmall));
        persons.add(new Person("Total Mortgage", "£ " + decimalFormat.format(totalMortgage), R.drawable.ic_attach_money_black_36dp, R.color.colorActiveSmall));

    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(persons);
        recyclerView.setAdapter(adapter);
    }

    private void initUI(){

        setContentView(R.layout.activity_pie_chart);
        final AHBottomNavigation bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);

        //floatingActionButton = (FloatingActionButton) findViewById(R.id.floating_action_button);

// Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.home, R.drawable.ic_home_white_24dp, R.color.colorPrimary);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.resetbar, R.drawable.ic_cached_white_24dp, R.color.colorPrimary);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(R.string.search, R.drawable.ic_search_white_24dp, R.color.colorPrimary);

// Add items
        bottomNavigationItems.add(item1);
        bottomNavigationItems.add(item2);
        bottomNavigationItems.add(item3);

// Set background color
        //bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));

// Disable the translation inside the CoordinatorLayout
        //bottomNavigation.setBehaviorTranslationEnabled(false);

// Change colors
        bottomNavigation.addItems(bottomNavigationItems);
        bottomNavigation.setAccentColor(Color.parseColor("#3F51B5"));
        bottomNavigation.setInactiveColor(Color.parseColor("#747474"));

// Force to tint the drawable (useful for font with icon for example)
        bottomNavigation.setForceTint(true);

// Force the titles to be displayed (against Material Design guidelines!)
        bottomNavigation.setForceTitlesDisplay(true);

// Use colored navigation with circle reveal effect
        //bottomNavigation.setColored(true);

// Set current item programmatically
        bottomNavigation.setCurrentItem(1);

// Customize notification (title, background, typeface)
        bottomNavigation.setNotificationBackgroundColor(Color.parseColor("#ff4081"));

// Add or remove notification for each item

        bottomNavigation.setNotification("2", 2);


// Set listener
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, boolean wasSelected) {


                if (position == 2) {
                    bottomNavigation.setNotification("", 2);
                    Intent intent = new Intent(ResultsMain.this, WebViewActivity.class);
                    startActivity(intent);

                } else {

                    if (position == 0) {

                        Intent intent = new Intent(ResultsMain.this, MainActivity.class);
                        startActivity(intent);

                    }
                }

                if (position == 1){

                    PlaceholderFragment pl2 = new PlaceholderFragment();
                    pl2.reset();
                    Toast.makeText(getApplicationContext(),"Chart will reanimate", Toast.LENGTH_SHORT).show();

                }

                }
            });

    }

    @SuppressLint("ValidFragment")
    public class PlaceholderFragment extends Fragment {


        private PieChartView chart;
        private PieChartData data;

        public boolean hasLabels = false;
        private boolean hasLabelsOutside = false;
        private boolean hasCenterCircle = false;
        private boolean hasCenterText1 = false;
        private boolean hasCenterText2 = false;
        private boolean hasLabelForSelected = false;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            View rootView = inflater.inflate(R.layout.box_results, container, false);



            chart = (PieChartView) rootView.findViewById(R.id.chart);
            chart.setOnValueTouchListener(new ValueTouchListener());

            update();

            hasCenterCircle = true;
            hasCenterText2 = true;
            hasCenterText1 = true;

            chart.startDataAnimation();
            generateData();

            toggleLabels();

            return rootView;
        }

        /*// MENU
        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.pie_chart, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
        }*/

        public void reset() {

            decoView.executeReset();
            decoView.deleteAll();
            update();

/*
            hasLabels = true;
            hasLabelsOutside = true;
            hasCenterCircle = true;
            hasCenterText1 = true;
            hasCenterText2 = true;
            hasLabelForSelected = true;*/

        }

        private void generateData() {



            Bundle b = getIntent().getExtras();
            monthlyPayment = b.getDouble("amount");
            totalPayment = b.getDouble("interest");
            totalMortgage = b.getDouble("totalMortgage");
            totalInterest = b.getDouble("totalInterest");

            DecimalFormat decimalFormat = new DecimalFormat("#");

            List<SliceValue> values = new ArrayList<SliceValue>();

            //add new slices to the pie
            SliceValue sliceValue = new SliceValue((float) totalInterest, COLOR_PURPLE);
            sliceValue.setLabel("£" + decimalFormat.format(totalInterest) + "  Total Interest");

            values.add(sliceValue);

            SliceValue tValue = new SliceValue((float) totalMortgage, COLOR_GREY);
            tValue.setLabel("£" + decimalFormat.format(totalMortgage) + "  Total Mortgage");
            values.add(tValue);

            data = new PieChartData(values);

            data.setHasLabels(hasLabels);

            data.setHasLabelsOnlyForSelected(hasLabelForSelected);
            data.setHasLabelsOutside(hasLabelsOutside);
            data.setHasCenterCircle(hasCenterCircle);

            if (hasCenterText1) {
                data.setCenterText1("interest");

                data.setCenterText1Color(COLOR_PINK);


                // Get roboto-italic font.
                Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");
                data.setCenterText1Typeface(tf);

                // Get font size from dimens.xml and convert it to sp(library uses sp values).
                data.setCenterText1FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                        (int) getResources().getDimension(R.dimen.pie_chart_text1_size)));


            }

            if (hasCenterText2) {

                data.setCenterText2("repayment");

                Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Italic.ttf");

                data.setCenterText2Typeface(tf);

                data.setCenterText2Color(COLOR_GREEN);

                data.setCenterText2FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                        (int) getResources().getDimension(R.dimen.pie_chart_text2_size)));


            }

            chart.setPieChartData(data);

        }


        private void toggleLabels() {
            hasLabels = !hasLabels;

            if (hasLabels) {
                hasLabelForSelected = false;
                chart.setValueSelectionEnabled(hasLabelForSelected);

                if (hasLabelsOutside) {
                    chart.setCircleFillRatio(0.7f);
                } else {
                    chart.setCircleFillRatio(1.0f);
                }
            }

            generateData();
        }



        private class ValueTouchListener implements PieChartOnValueSelectListener {



            @Override
            public void onValueSelected(int arcIndex, SliceValue value) {

                Bundle b = getIntent().getExtras();
                monthlyPayment = b.getDouble("amount");
                totalPayment = b.getDouble("interest");
                /*DecimalFormat decimalFormat = new DecimalFormat("#");

                value.setLabel("jmfkg" + monthlyPayment);*/
                Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onValueDeselected() {
                // TODO Auto-generated method stub

            }

        }

        protected void update() {

            DecimalFormat decimalFormat = new DecimalFormat("0");

            Bundle b = getIntent().getExtras();
            percentInterest = b.getDouble("percentInterest");
            percentRepayment = b.getDouble("percentRepayment");
            //percentTotal = b.getDouble("percentTotal");
            //monthlyPayment = b.getDouble("amount");
            //float j = (float) monthlyPayment;


            decoView.executeReset();
            decoView.deleteAll();

            float circleInset = getDimension(23) - (getDimension(46) * 0.3f);
            final SeriesItem seriesItem = new SeriesItem.Builder(COLOR_BACK)
                    .setRange(0, mSeriesMax, mSeriesMax)
                    .setChartStyle(SeriesItem.ChartStyle.STYLE_PIE)
                    .setInset(new PointF(circleInset, circleInset))
                    .build();

            mBack1Index = decoView.addSeries(seriesItem);

            SeriesItem series1Item = new SeriesItem.Builder(COLOR_PINK)
                    .setRange(0, mSeriesMax, 0)
                    .setInitialVisibility(false)
                    //the line of the curve
                    .setLineWidth(getDimension(26))
                    .setSeriesLabel(new SeriesLabel.Builder(decimalFormat.format(percentInterest) + "%%").setFontSize(9).build())
                    .setCapRounded(false)
                    .addEdgeDetail(new EdgeDetail(EdgeDetail.EdgeType.EDGE_INNER, COLOR_EDGE, 0.2f))
                    .setShowPointWhenEmpty(false)
                    .build();
            mSeries1Index = decoView.addSeries(series1Item);

            SeriesItem series2Item = new SeriesItem.Builder(COLOR_GREEN)
                    .setRange(0, mSeriesMax, 0)
                    .setInitialVisibility(false)
                    .setLineWidth(getDimension(26))
                    .setSeriesLabel(new SeriesLabel.Builder(decimalFormat.format(percentRepayment) + "%%").setFontSize(9).build())
                    .setCapRounded(false)
                    //.setChartStyle(SeriesItem.ChartStyle.STYLE_PIE)
                    .addEdgeDetail(new EdgeDetail(EdgeDetail.EdgeType.EDGE_INNER, COLOR_EDGE, 0.2f))
                    .setShowPointWhenEmpty(false)
                    .build();

            mSeries2Index = decoView.addSeries(series2Item);

     /*       SeriesItem series3Item = new SeriesItem.Builder(COLOR_PINK)
                    .setRange(0, mSeriesMax, 0)
                    .setInitialVisibility(false)
                    .setLineWidth(getDimension(26))
                    .setSeriesLabel(new SeriesLabel.Builder("Yrly" + decimalFormat.format(percentTotal)).setFontSize(11).build())
                    .setCapRounded(false)
                    //.setChartStyle(SeriesItem.ChartStyle.STYLE_PIE)
                    .addEdgeDetail(new EdgeDetail(EdgeDetail.EdgeType.EDGE_INNER, COLOR_EDGE, 0.2f))
                    .setShowPointWhenEmpty(false)
                    .build();*/

            //mSeries3Index = decoView.addSeries(series3Item);

            // this is how you call a method inside another class
            PlaceholderFragment t = new PlaceholderFragment();
            t.setupEvents();

        }

        protected void setupEvents(){

            double pI = percentInterest;
            float f1 = (float) pI;

            double pR = percentRepayment;
            float f2 = (float) pR;


            Bundle b = getIntent().getExtras();
            percentInterest = b.getDouble("percentInterest");
            percentRepayment = b.getDouble("percentRepayment");

            arcView.executeReset();


            addAnimation(arcView, mSeries1Index, f1, 3000);
            addAnimation(arcView, mSeries2Index, f2, 11000);

            arcView.addEvent(new DecoEvent.Builder(100)
                    .setIndex(mSeries1Index)
                    .setDelay(11000)
                    .setDuration(5000)
                    .build());

            //addAnimation(arcView, mSeries3Index, f3, 19000);
/*
            arcView.addEvent(new DecoEvent.Builder(79)
                    .setIndex(mSeries2Index)
                    .setDelay(19000)
                    .setDuration(1000)
                    .build());*/

            /*arcView.addEvent(new DecoEvent.Builder(50)
                    .setIndex(mSeries1Index)
                    .setDelay(19000)
                    .setDuration(5000)
                    .build());*/

            /*arcView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_COLOR_CHANGE, COLOR_BACK)
                    .setIndex(mBack1Index)
                    .setDelay(27000)
                    .setDuration(2000)
                    .setListener(new DecoEvent.ExecuteEventListener() {
                        @Override
                        public void onEventStart(DecoEvent event) {
                            //imgView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_avatar_group));
                        }

                        @Override
                        public void onEventEnd(DecoEvent event) {

                        }
                    })
                    .build());*/

            //addFinishAnimation(arcView, mSeries3Index, 1250, 30000, null);
            addFinishAnimation(arcView, mSeries2Index, 1500, 30000, null);
            addFinishAnimation(arcView, mSeries1Index, 1750, 30000, null);



        }

        }
        private void addAnimation(final DecoView arcView,
                                  int series, float moveTo, int delay) {
            DecoEvent.ExecuteEventListener listener = new DecoEvent.ExecuteEventListener() {
                @Override
                public void onEventStart(DecoEvent event) {

                }

                @Override
                public void onEventEnd(DecoEvent event) {
                }

            };

            arcView.addEvent(new DecoEvent.Builder(moveTo)
                    .setIndex(series)
                    .setDelay(delay)
                    .setDuration(4000)
                    .setListener(listener)
                    .build());
        }


        private void addFinishAnimation(final DecoView arcView, final int series, final int duration, int delay, final View view) {
            arcView.addEvent(new DecoEvent.Builder(0)
                    .setIndex(series)
                    .setDelay(delay)
                    .setDuration(duration)
                    .setListener(new DecoEvent.ExecuteEventListener() {
                        @Override
                        public void onEventStart(DecoEvent event) {
                            arcView.getChartSeries(series).getSeriesItem().setSeriesLabel(null);
                        }

                        @Override
                        public void onEventEnd(DecoEvent event) {
                        }
                    })
                    .build());
        }


    protected float getDimension(float base) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, base, getResources().getDisplayMetrics());
    }





}
