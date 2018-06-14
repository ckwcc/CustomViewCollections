package com.ckw.customviewcollections.tag_view

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ckw.customviewcollections.R

/**
 * Created by ckw
 * on 2018/6/13.
 * 流式布局
 */
class TagViewActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag_view)
        val tagViewLayout = findViewById<TagViewLayout>(R.id.tag_view_layout)
        for (index in 0 until 10){
            val tagView = TagView(this)
            tagView.setMargins(4,4,4,4)//单位是dp
            tagView.setTagViewCorner(20)//单位是dp
            tagView.setTagViewBackground(Color.YELLOW)
            tagView.setTagViewText("世界那么大,看看")
            tagView.setTagViewTextSize(10)//单位是sp
            tagView.setTagViewTextColor(Color.RED)
            tagViewLayout.addTagView(tagView)
        }

        for (index in 0 until 10){
            val tagView = TagView(this)
            tagView.setMargins(4,4,4,4)
            tagView.setTagViewBackground(Color.RED)
            tagView.setTagViewCorner(index)
            tagView.setTagViewText("中国人")
            tagView.setTagViewTextSize(10)
            tagView.setTagViewTextColor(Color.BLACK)
            tagViewLayout.addTagView(tagView)
        }

    }
}