package org.gsm.olio.view.fragment.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.apollographql.apollo3.api.Optional
import com.bumptech.glide.Glide
import com.google.android.gms.common.api.Api
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.gsm.olio.R
import org.gsm.olio.databinding.FragmentFirstLoginBinding
import org.gsm.olio.util.Constants.TAG
import java.io.File


@AndroidEntryPoint
class FirstLoginFragment : Fragment(R.layout.fragment_first_login) {
    private val binding: FragmentFirstLoginBinding by viewBinding(CreateMethod.INFLATE)
    private val vm: FirstLoginViewModel by viewModels()
    lateinit var requestActivity: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initViews()
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resultActivity()
    }

    private fun initViews() = with(binding) {
        fragment = this@FirstLoginFragment
        lifecycleOwner = this@FirstLoginFragment
    }

    fun getProFileImage() {
        Log.d(TAG, "사진변경 호출")
        val chooserIntent = Intent(Intent.ACTION_CHOOSER)
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        chooserIntent.putExtra(Intent.EXTRA_INTENT, intent)
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "사용할 앱을 선택해주세요.")
        requestActivity.launch(chooserIntent)
    }


    private fun resultActivity() {
        requestActivity =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    CoroutineScope(Dispatchers.Main).launch {
                        val imagePath = result.data!!.data!!

                        val file = File(absolutelyPath(imagePath, requireContext()))
                        val requestFile = file.asRequestBody("image/*".toMediaType())
                        var body: MultipartBody.Part = MultipartBody.Part.createFormData(
                            "profile",
                            file.name,
                            requestFile
                        )
                        var mimeType = mimeType(file)
                        Log.d(TAG, "fileName : ${file.name}\n 확장자 : ${mimeType.toString()}")


                        imagePath?.run { setImage(this) }

                        if (mimeType != null) {
                            vm.getImg(body)
                            vm.uploadImage(file.name, mimeType)
                        }
                    }

                }
            }
    }

    private fun setImage(uri: Uri) {
        Glide.with(requireContext())
            .load(uri)
            .into(binding.imgFirstBack)
        binding.selectImg.visibility = View.GONE
    }


    // 절대경로 변환
    private fun absolutelyPath(path: Uri?, context: Context): String {
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()

        var result = c?.getString(index!!)

        return result!!
    }

    // 확장자 가져오기
    private fun mimeType(file: File): String? {
        val mimeTypeMap = MimeTypeMap.getSingleton()
        val extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(file).toString())
        Log.d(TAG, "mimeType: ${extension.toString()}")

        return mimeTypeMap.getMimeTypeFromExtension(extension)
    }

    fun createUser() = CoroutineScope(Dispatchers.Main).launch {
        vm.postProfile()
    }

}