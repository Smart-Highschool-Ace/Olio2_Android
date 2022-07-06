package org.gsm.olio.view.fragment.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.exception.ApolloException
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.gsm.olio.repository.LoginRepository
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository : LoginRepository) : ViewModel() {
    private val _token = MutableLiveData<String>()
    val token : LiveData<String> get() = _token

    init {
        _token.value = ""
    }

    fun loginCheck(){
        viewModelScope.launch(Dispatchers.IO) {
            val response = try{
                _token.value?.let { repository.loginCheck(it)}?.execute()
               }catch (e : ApolloException){
                    e.printStackTrace()
               }

        }
    }

    fun getToken(token : String){
        _token.value = token
    }

}