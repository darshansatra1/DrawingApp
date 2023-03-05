package com.example.drawingapp


import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get

class MainActivity : AppCompatActivity() {

    private var drawingView:DrawingView? = null
    private var imageButtonCurrentPaint: ImageButton? = null
    val requestPermission:ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            permissions->
            permissions.entries.forEach{
                val permissionName = it.key
                val isGranted = it.value
                if (isGranted){
                    Toast.makeText(this,"Permissiopn granted now you can read the storage files",
                    Toast.LENGTH_LONG).show()

                    val pickIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

                }else{
                    if(permissionName==android.Manifest.permission.READ_EXTERNAL_STORAGE){
                        Toast.makeText(this,"Permission denied",
                            Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawingView = findViewById(R.id.drawing_view)
        drawingView?.setSizeForBrush(20.toFloat())

        val linearLayoutPaintColors = findViewById<LinearLayout>(R.id.ll_paint_colors)

        imageButtonCurrentPaint = linearLayoutPaintColors[1] as ImageButton
        imageButtonCurrentPaint!!.setImageDrawable(
            ContextCompat.getDrawable(this,R.drawable.pallet_pressed)
        )



        val ib_brush: ImageButton = findViewById(R.id.ib_brush)
        ib_brush.setOnClickListener{
            showBrushSizeChooserDialog()
        }

        val ibGallery:ImageButton = findViewById(R.id.ib_gallery)
        ibGallery.setOnClickListener{
            requestStoragePermission()
        }
    }

    private fun showBrushSizeChooserDialog(){
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.dialog_brush_size)
        brushDialog.setTitle("Brush Size: ")
        val smallBtn = brushDialog.findViewById<ImageButton>(R.id.ib_small_brush)
        val mediumBtn = brushDialog.findViewById<ImageButton>(R.id.ib_medium_brush)
        val largeBtn = brushDialog.findViewById<ImageButton>(R.id.ib_large_brush)
        smallBtn.setOnClickListener{
            drawingView?.setSizeForBrush(10.toFloat())
            brushDialog.dismiss()
        }

        mediumBtn.setOnClickListener{
            drawingView?.setSizeForBrush(20.toFloat())
            brushDialog.dismiss()
        }

        largeBtn.setOnClickListener{
            drawingView?.setSizeForBrush(30.toFloat())
            brushDialog.dismiss()
        }

        brushDialog.show()

    }

    fun paintClicked(view:View){
        if(view!=imageButtonCurrentPaint){
            val imageButton = view as ImageButton
            val colorTag = imageButton.tag.toString()
            drawingView?.setColor(colorTag)

            imageButton.setImageDrawable(
                ContextCompat.getDrawable(this,R.drawable.pallet_pressed)
            )
            imageButtonCurrentPaint!!.setImageDrawable(
                ContextCompat.getDrawable(this,R.drawable.pallet_normal)
            )
            imageButtonCurrentPaint = view
        }
    }

    private fun requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
        )){
            showRationaleDialog("Drawing App","Drawing App "+
            "needs to Access your external storage")
        }else{
            requestPermission.launch(arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ))
        }

    }

    private fun showRationaleDialog(
        title:String,
        message:String
    ){
        val builder:AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel"){dialog,_->
                dialog.dismiss()
            }

        builder.create().show()
    }
}