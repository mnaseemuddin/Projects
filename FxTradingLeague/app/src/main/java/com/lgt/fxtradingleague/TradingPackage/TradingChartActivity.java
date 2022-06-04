package com.lgt.fxtradingleague.TradingPackage;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.jjoe64.graphview.GraphView;
import com.lgt.fxtradingleague.InitChartTrading.MyMarkerView;
import com.lgt.fxtradingleague.R;

import java.util.ArrayList;

public class TradingChartActivity extends AppCompatActivity {
    GraphView gv_Trading_graph;
    ImageView ivBackToolbar;
    TextView tvToolbarTitle,tv_Withdraw;
    private LineChart pieChar_TradingData;
    private SeekBar seekBarX, seekBarY;
    private TextView tvX, tvY;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trading_details_chart);
        initView(); //SelectTradingMaster
    }

    private void initView() {
        pieChar_TradingData = findViewById(R.id.pieChar_TradingData);
        gv_Trading_graph = findViewById(R.id.gv_Trading_graph);
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle);
        ivBackToolbar = findViewById(R.id.ivBackToolbar);
        tv_Withdraw = findViewById(R.id.tv_Withdraw);
        ivBackToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tvX = findViewById(R.id.tvXMax);
        tvY = findViewById(R.id.tvYMax);

        seekBarX = findViewById(R.id.seekBar1);

        seekBarY = findViewById(R.id.seekBar2);
        seekBarY.setMax(180);
        tvToolbarTitle.setText("contest_type");

        tv_Withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TradingChartActivity.this,SelectTradingMaster.class));
            }
        });

        {
            // background color
            pieChar_TradingData.setBackgroundColor(Color.WHITE);

            // disable description text
            pieChar_TradingData.getDescription().setEnabled(false);

            // enable touch gestures
            pieChar_TradingData.setTouchEnabled(true);

            // set listeners
            // pieChar_TradingData.setOnChartValueSelectedListener((OnChartValueSelectedListener) this);
            pieChar_TradingData.setDrawGridBackground(false);

            // create marker to display box when values are selected
            MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);

            // Set the marker to the chart
            mv.setChartView(pieChar_TradingData);
            pieChar_TradingData.setMarker(mv);

            // enable scaling and dragging
            pieChar_TradingData.setDragEnabled(true);
            pieChar_TradingData.setScaleEnabled(true);
            // chart.setScaleXEnabled(true);
            // chart.setScaleYEnabled(true);

            // force pinch zoom along both axis
            pieChar_TradingData.setPinchZoom(true);
        }

        XAxis xAxis;
        {   // // X-Axis Style // //
            xAxis = pieChar_TradingData.getXAxis();

            // vertical grid lines
            xAxis.enableGridDashedLine(10f, 10f, 0f);
        }

        YAxis yAxis;
        {   // // Y-Axis Style // //
            yAxis = pieChar_TradingData.getAxisLeft();

            // disable dual axis (only use LEFT axis)
            pieChar_TradingData.getAxisRight().setEnabled(false);

            // horizontal grid lines
            yAxis.enableGridDashedLine(10f, 10f, 0f);

            // axis range
            yAxis.setAxisMaximum(200f);
            yAxis.setAxisMinimum(-50f);
        }


        {   // // Create Limit Lines // //
            LimitLine llXAxis = new LimitLine(9f, "Index 10");
            llXAxis.setLineWidth(4f);
            llXAxis.enableDashedLine(10f, 10f, 0f);
            llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            llXAxis.setTextSize(10f);

            LimitLine ll1 = new LimitLine(150f, "Upper Limit");
            ll1.setLineWidth(4f);
            ll1.enableDashedLine(10f, 10f, 0f);
            ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
            ll1.setTextSize(10f);

            LimitLine ll2 = new LimitLine(-30f, "Lower Limit");
            ll2.setLineWidth(4f);
            ll2.enableDashedLine(10f, 10f, 0f);
            ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
            ll2.setTextSize(10f);

            // draw limit lines behind data instead of on top
            yAxis.setDrawLimitLinesBehindData(true);
            xAxis.setDrawLimitLinesBehindData(true);

            // add limit lines
            yAxis.addLimitLine(ll1);
            yAxis.addLimitLine(ll2);
            //xAxis.addLimitLine(llXAxis);
        }

        // add data
        seekBarX.setProgress(45);
        seekBarY.setProgress(180);
        setData(45, 180);

        // draw points over time
        pieChar_TradingData.animateX(1500);

        // get the legend (only possible after setting data)
        Legend l = pieChar_TradingData.getLegend();

        // draw legend entries as lines
        l.setForm(Legend.LegendForm.LINE);
    }
    // setup data
    private void setData(int count, float range) {

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * range) - 30;
            values.add(new Entry(i, val, getResources().getDrawable(R.drawable.star)));
        }

        LineDataSet set1;

        if (pieChar_TradingData.getData() != null &&
                pieChar_TradingData.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) pieChar_TradingData.getData().getDataSetByIndex(0);
            set1.setValues(values);
            set1.notifyDataSetChanged();
            pieChar_TradingData.getData().notifyDataChanged();
            pieChar_TradingData.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "DataSet 1");

            set1.setDrawIcons(false);

            // draw dashed line
            set1.enableDashedLine(10f, 5f, 0f);

            // black lines and points
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);

            // line thickness and point size
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);

            // draw points as solid circles
            set1.setDrawCircleHole(false);

            // customize legend entry
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            // text size of values
            set1.setValueTextSize(9f);

            // draw selection line as dashed
            set1.enableDashedHighlightLine(10f, 5f, 0f);

            // set the filled area
            set1.setDrawFilled(true);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return pieChar_TradingData.getAxisLeft().getAxisMinimum();
                }
            });

            // set color of filled area
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.drawable.fade_red);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1); // add the data sets

            // create a data object with the data sets
            LineData data = new LineData(dataSets);

            // set data
            pieChar_TradingData.setData(data);
        }
    }
}
