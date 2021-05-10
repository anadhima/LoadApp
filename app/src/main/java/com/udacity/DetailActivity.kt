package com.udacity

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*
import kotlinx.android.synthetic.main.description.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.content_detail)
        setSupportActionBar(toolbar)


        val status = intent.getStringExtra("fileStatus")
        val file = intent.getStringExtra("fileName")

        file_name_label.text = file
        file_status_label.text = status

        if (status == "Success") {
            file_status_label.setTextColor(Color.GREEN)
        } else if (status == "Fail") {
            file_status_label.setTextColor(Color.RED)
        }

        coordinateMotion()

        ok_button.setOnClickListener {
            finish()
        }
    }
    private fun coordinateMotion() {
    val appBarLayout: AppBarLayout = findViewById(R.id.appbar_layout)
    val motionLayout: MotionLayout = findViewById(R.id.motion_layout)

    val listener = AppBarLayout.OnOffsetChangedListener { unused, verticalOffset ->
        val seekPosition = -verticalOffset / appBarLayout.totalScrollRange.toFloat()
        motionLayout.progress = seekPosition
    }

    appBarLayout.addOnOffsetChangedListener(listener)

}
}


