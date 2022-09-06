package org.gsm.olio.view.fragment.login

import android.util.JsonToken
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.exception.ApolloException
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.gsm.olio.MyApplication
import org.gsm.olio.repository.LoginRepository
import org.gsm.olio.util.Constants.TAG
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: LoginRepository) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _firstLogin = MutableLiveData<Boolean>()
    val firstLogin: LiveData<Boolean> get() = _firstLogin

    init {
        _firstLogin.value = false
        _loading.value = false
    }

    fun loginCheck(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = try {
                _loading.postValue(true)
                repository.loginCheck(token).execute()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
            val data = response?.data?.login

            data?.let {
                it?.token?.run {
                    Log.d(TAG, "loginCheck \n token : $token \n jwt : ${data?.token} ")
                    MyApplication.prefs.token = this
                    Log.d(TAG, "loginCheck: jwt : ${MyApplication.prefs.token}")
                    _firstLogin.postValue(it.joined)
                    _loading.postValue(false)
                }

                it?.error?.run {
                    Log.e(TAG, "loginCheck: ${data?.error}")
                    _loading.postValue(false)
                }

            }

        }

    }


}