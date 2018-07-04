package com.ckw.customviewcollections.pie_chart

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.ckw.customviewcollections.R

/**
 * Created by ckw
 * on 2018/6/29.
 */
class PieChartActivity : AppCompatActivity(), SimplePieChartView.PieClickListener {
    override fun onPieClick(position: Int, percent: Float) {
        Toast.makeText(this,"当前点击的position："+position + ";百分比："+percent,Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pie_chart)
        val pieChart = findViewById<SimplePieChartView>(R.id.simple_pie_chart)

        val pie1 = PieBean()
        pie1.percent = 20f
        pie1.picColor = Color.BLUE
        pieChart.setPieBean(pie1)

        val pie2 = PieBean()
        pie2.percent = 10f
        pie2.picColor = Color.RED
        pieChart.setPieBean(pie2)

        val pie3 = PieBean()
        pie3.percent = 20f
        pie3.picColor = Color.GRAY
        pieChart.setPieBean(pie3)

        val pie4 = PieBean()
        pie4.percent = 30f
        pie4.picColor = Color.GREEN
        pieChart.setPieBean(pie4)

        val pie5 = PieBean()
        pie5.percent = 20f
        pie5.picColor = Color.LTGRAY
        pieChart.setPieBean(pie5)

        pieChart.startAnim()

        pieChart.setPieClickListener(this)


    }
}