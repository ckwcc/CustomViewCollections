package com.ckw.customviewcollections

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import com.ckw.customviewcollections.rulerview.RulerView
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.TextView
import android.view.ViewGroup
import com.ckw.customviewcollections.rulerview.RulerViewActivity


class MainActivity : AppCompatActivity(), ItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        var datas = ArrayList<String>()
        datas.add("RulerView:尺子选择器")

        val adapter = SimpleAdapter(this,datas)
        recyclerView.adapter = adapter

        adapter.setItemClickListener(this)

    }

    override fun setOnItemClick(position: Int) {
        if(position == 0){
            startActivity(Intent(this,RulerViewActivity::class.java))
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
