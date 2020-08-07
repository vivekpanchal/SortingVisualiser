package com.vivek.sortingvisualiser

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_sort.setOnClickListener {
            sort_view.swap()
        }

        btn_suffle.setOnClickListener {
            sort_view.shuffle()
        }
    }
}