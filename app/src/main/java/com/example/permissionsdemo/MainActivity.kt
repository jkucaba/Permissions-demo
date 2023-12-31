package com.example.permissionsdemo

import android.app.AlertDialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {

    private val cameraResultLauncher : ActivityResultLauncher<String> =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()){
            isGranted ->
            if(isGranted){
                Toast.makeText(this, "Permission granted for camera", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "Permission denied for camera", Toast.LENGTH_LONG).show()
            }
        }

    private val cameraAndLocationResultLauncher : ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()){
            permissions ->
            permissions.entries.forEach{
                val permissionName = it.key
                val isGranted = it.value
                if(isGranted){
                    if(permissionName == android.Manifest.permission.ACCESS_FINE_LOCATION){
                        Toast.makeText(this, "Permission granted for fine location", Toast.LENGTH_LONG).show()
                    }else if(permissionName == android.Manifest.permission.ACCESS_COARSE_LOCATION){
                        Toast.makeText(this, "Permission granted for coarse location", Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(this, "Permission granted for camera", Toast.LENGTH_LONG).show()
                    }
                }else{
                    if(permissionName == android.Manifest.permission.ACCESS_FINE_LOCATION){
                        Toast.makeText(this, "Permission denied for fine location", Toast.LENGTH_LONG).show()
                    }else if(permissionName == android.Manifest.permission.ACCESS_COARSE_LOCATION){
                        Toast.makeText(this, "Permission denied for coarse location", Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(this, "Permission denied for camera", Toast.LENGTH_LONG).show()
                    }
                }
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCameraPre : Button = findViewById(R.id.btnCameraPermission)
        btnCameraPre.setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA) ){
                showRationaleDialog("Permission Demo requires camera access",
                    "Camera cannot be used because Camera access is denied")
            }else{
                cameraAndLocationResultLauncher.launch(
                    arrayOf(android.Manifest.permission.CAMERA,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    private fun showRationaleDialog(
        title: String,
        message: String,
    ){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel"){ dialog, _->
                dialog.dismiss()
            }
        builder.create().show()
    }

}