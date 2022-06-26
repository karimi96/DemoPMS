package com.karimi.googlemap.widget

import android.app.Dialog
import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import com.karimi.googlemap.R
import com.karimi.googlemap.activity.MapsActivity2
import kotlinx.android.synthetic.main.activity_maps2.*

class DialogWidget(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {
          var activity : MapsActivity2 = MapsActivity2()

    init {
        inflate(context, R.layout.dialog_get_user,this)
//        activity.init(this)
    }


//    override fun showDialog() {
//        Toast.makeText(context,"helo" , Toast.LENGTH_SHORT).show()
//        val metrics = resources.displayMetrics
//            val width = metrics.widthPixels
//            val height = metrics.heightPixels
//            val dialog = Dialog(context)
//            dialog.setContentView(R.layout.dialog_get_user)
//            dialog.setCancelable(false)
//            dialog.window?.setLayout(
//                ((6.3 * width) / 7).toInt(),
//                ViewGroup.LayoutParams.WRAP_CONTENT)
//            dialog.window?.setBackgroundDrawableResource(R.drawable.border_dialog)
//            dialog.show()
//    }


//
//    override fun showDialog() {
//        val metrics = resources.displayMetrics
//            val width = metrics.widthPixels
//            val height = metrics.heightPixels
//            val dialog = Dialog(context)
//            dialog.setContentView(R.layout.dialog_get_user)
//            dialog.setCancelable(false)
//            dialog.window?.setLayout(
//                ((6.3 * width) / 7).toInt(),
//                ViewGroup.LayoutParams.WRAP_CONTENT)
//            dialog.window?.setBackgroundDrawableResource(R.drawable.border_dialog)
//            dialog.show()
//    }



}
