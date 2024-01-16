package com.birdshoe.projemanage.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.birdshoe.projemanage.R
import com.birdshoe.projemanage.databinding.ActivityMainBinding
import com.birdshoe.projemanage.databinding.ActivityMyProfileBinding
import com.birdshoe.projemanage.firebase.FirestoreClass
import com.birdshoe.projemanage.models.User
import com.birdshoe.projemanage.utils.Constants
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException

class MyProfileActivity : BaseActivity() {

    companion object {
        private const val READ_STORAGE_PERMISSION_CODE = 1
        private const val PICK_IMAGE_REQUEST_CODE = 2

    }

    private var binding: ActivityMyProfileBinding? = null
    private var mSelectedImageFileUri: Uri? = null
    private var mProfileImageURL: String = ""
    private lateinit var mUserDetails: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyProfileBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setUpActionBar()

        FirestoreClass().loadUserData(this)

        binding?.ivProfileUserImage?.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                showImageChooser()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_STORAGE_PERMISSION_CODE
                )
            }
        }

        binding?.btnUpdate?.setOnClickListener {
            if (mSelectedImageFileUri != null) {
                uploadUserImage()
            }else{
                showProgressDialog(resources.getString(R.string.please_wait))

                updateUserProfileData()
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //TODO Show Image Chooser
            }
        } else {
            Toast.makeText(
                this,
                "Oops, you just denied the permission for storage. You can also allow it from the settings",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    //TODO Interesante entender la función showImageChooser con el resultLauncher vs lesson 249
    private fun showImageChooser() {
        var galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(galleryIntent)

    }

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val imageProfile: ImageView = findViewById(R.id.iv_profile_user_image)

                mSelectedImageFileUri = data?.data

                try {
                    Glide
                        .with(this@MyProfileActivity)
                        .load(mSelectedImageFileUri)
                        .centerCrop()
                        .placeholder(R.drawable.ic_user_place_holder)
                        .into(imageProfile)

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

    private fun setUpActionBar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar_my_profile_activity)
        setSupportActionBar(toolbar)
        val actionbar = supportActionBar

        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionbar.title = resources.getString(R.string.my_profile_title)
        }
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    fun setUserDataInUI(user: User) {
        val imageProfile: ImageView = findViewById(R.id.iv_profile_user_image)
        val userName: TextView = findViewById(R.id.et_name)
        val userEmail: TextView = findViewById(R.id.et_email)
        val userPhone: TextView = findViewById(R.id.et_mobile)

        mUserDetails = user

        Glide
            .with(this@MyProfileActivity)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(imageProfile)

        userName.text = user.name
        userEmail.text = user.email
        if (user.mobile != 0L) {
            userPhone.text = user.mobile.toString()
        }
    }

    private fun getFileExtension(uri: Uri?): String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri!!))
    }

    private fun uploadUserImage() {
        showProgressDialog(resources.getString(R.string.please_wait))

        if (mSelectedImageFileUri != null) {

            val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
                "USER_IMAGE" + System.currentTimeMillis()
                        + "." + getFileExtension(mSelectedImageFileUri)
            )

            sRef.putFile(mSelectedImageFileUri!!).addOnSuccessListener { taskSnapshot ->
                Log.e(
                    "Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )

                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                    Log.i("Downloadable Image URL", uri.toString())
                    mProfileImageURL = uri.toString()

                    updateUserProfileData()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(
                    this@MyProfileActivity,
                    exception.message,
                    Toast.LENGTH_LONG
                ).show()
                hideProgressDialog()
            }
        }
    }

    fun profileUpdateSuccess() {
        hideProgressDialog()
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun updateUserProfileData() {
        val userHashMap = HashMap<String, Any>()

        if (mProfileImageURL.isNotEmpty() && mProfileImageURL != mUserDetails.image) {
            userHashMap[Constants.IMAGE] = mProfileImageURL
        }

        if (binding?.etName?.text.toString() != mUserDetails.name) {
            userHashMap[Constants.NAME] = binding?.etName?.text.toString()
        }

        if (binding?.etMobile?.text.toString() != mUserDetails.mobile.toString()) {
            userHashMap[Constants.MOBILE] = binding?.etMobile?.text.toString().toLong()
        }

        FirestoreClass().updateUserProfileData(this,userHashMap)

    }
}