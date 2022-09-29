package org.gsm.olio.view.fragment.login

import android.text.Editable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.Optional
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import okhttp3.MultipartBody
import org.gsm.olio.BuildConfig
import org.gsm.olio.MyApplication
import org.gsm.olio.repository.FirstLoginRepository
import org.gsm.olio.util.Constants
import org.gsm.olio.util.Constants.TAG
import javax.inject.Inject

@HiltViewModel
class FirstLoginViewModel @Inject constructor(
    private val repository: FirstLoginRepository
) : ViewModel() {

    var job: Job? = null
    private val loadError = MutableLiveData<String?>()
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError("Exception: ${throwable.localizedMessage}")
    }
    private val _url = MutableLiveData<String>()
    val url: LiveData<String> get() = _url

    private val _key = MutableLiveData<String>()
    val key: LiveData<String> get() = _key

    private val _name = MutableLiveData<Optional<String>>()
    val name: LiveData<Optional<String>> get() = _name

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _img = MutableLiveData<MultipartBody.Part>()
    val img: LiveData<MultipartBody.Part> get() = _img

    private val _imgType = MutableLiveData<String>()
    val imgType: LiveData<String> get() = _imgType


    init {
        _loading.value = false
    }


    fun uploadImage(name: String, mimeType: String) {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = name?.run { repository.uploadImage(this, mimeType) }
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    val data = response?.body()!!
                    data.run {
                        _url.postValue(this.data.url.replace(BuildConfig.POST_PROFILE_URL, ""))
                        _key.postValue(this.data.key)
                        Log.e(TAG, "uploadImage Key: ${this.data.key} ")
                    }
                } else {
                    onError("Error : ${response.message()}")
                }
            }

        }

    }

    suspend fun postProfile() {
        _loading.value = true
        Log.d(TAG, "postProfile: ${_loading.value.toString()}")
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = url.value?.run {
                img.value?.let { it1 -> repository.postProfile(this, it1) }
            }
            withContext(Dispatchers.Main) {
                response?.run {
                    if (this.isSuccessful) {
                        Log.d(TAG, "postProfile: Success")
                        _loading.value = false
                        Log.d(TAG, "postProfile: ${_loading.value.toString()}")
                    } else {
                        Log.d(TAG, "postProfile: ${response.message()}")
                        onError("Error : ${response.message()}")
                        _loading.value = false
                    }
                }
            }

        }
    }

    fun createUser(name: Optional<String>) {
        _loading.value = true
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = imgType.value.let { repository.createUser(name, Optional.presentIfNotNull(BuildConfig.POST_PROFILE_URL+key.value+"."+it+"?width=497&height=662")).execute() }

            withContext(Dispatchers.Main) {
                _loading.postValue(false)
            }

        }

    }

    fun getName(name: Optional<String>) {
        _name.value = name
    }

    fun getImg(img: MultipartBody.Part,type : String) {
        _img.value = img
        _imgType.value = type.replace("image/","")
    }

    private fun onError(message: String) {
        loadError.value = message
        _loading.value = false
    }


}
