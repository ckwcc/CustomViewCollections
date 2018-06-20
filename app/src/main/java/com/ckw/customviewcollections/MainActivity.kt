package com.ckw.customviewcollections

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.view.ViewGroup
import com.ckw.customviewcollections.drag_view.DragViewActivity
import com.ckw.customviewcollections.level_view.LevelViewActivity
import com.ckw.customviewcollections.ruler_view.RulerViewActivity
import com.ckw.customviewcollections.star_view.StarViewActivity
import com.ckw.customviewcollections.study.interpolator_evaluator.InterpolatorEvaluatorActivity
import com.ckw.customviewcollections.study.ofobject.OfObjectActivity
import com.ckw.customviewcollections.tag_view.TagViewActivity


class MainActivity : AppCompatActivity(), ItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        var datas = ArrayList<String>()
        datas.add("RulerView:尺子选择器")
        datas.add("LevelView:进度显示器")
        datas.add("TagViewLayout:流式布局")
        datas.add("DragView:简单拖动的View")
        datas.add("StarView:点击显示得分图形控件")
        datas.add("插值器和Evaluator的自定义使用")
        datas.add("ValueAnimator.ofObject的使用")

        val adapter = SimpleAdapter(this,datas)
        recyclerView.adapter = adapter

        adapter.setItemClickListener(this)

    }

    override fun setOnItemClick(position: Int) {
        var intent: Intent? = null
        when (position) {
            0 -> {
                intent = Intent(this,RulerViewActivity::class.java)
                startActivity(intent)
            }
            1 -> {
                intent = Intent(this,LevelViewActivity::class.java)
                startActivity(intent)
            }
            2 -> {
                intent = Intent(this,TagViewActivity::class.java)
                startActivity(intent)
            }
            3 -> {
                intent = Intent(this,DragViewActivity::class.java)
                startActivity(intent)
            }
            4 -> {
                intent = Intent(this,StarViewActivity::class.java)
                startActivity(intent)
            }
            5 -> {
                intent = Intent(this, InterpolatorEvaluatorActivity::class.java)
                startActivity(intent)
            }
            6 -> {
                intent = Intent(this, OfObjectActivity::class.java)
                startActivity(intent)
            }
        }

    }


}

interface ItemClickListener{
    fun setOnItemClick(position: Int)
}

 class SimpleAdapter(private val context: Context, datas: ArrayList<String>) : RecyclerView.Adapter<SimpleAdapter.ViewHolder>() {

    private val dataList: List<String>

    init {
        this.dataList = datas
    }

     private var  listener: ItemClickListener? = null

     fun setItemClickListener(listener: ItemClickListener) {
         this.listener = listener
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_recycler,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mTextView.setText(dataList[position])
        holder.itemView.setOnClickListener {
            listener!!.setOnItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var mTextView: TextView

        init {
            mTextView = itemView.findViewById(R.id.tv_custom)

        }
    }
}
