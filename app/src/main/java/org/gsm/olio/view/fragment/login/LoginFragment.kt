package org.gsm.olio.view.fragment.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.notify
import org.gsm.olio.R
import org.gsm.olio.databinding.FragmentLoginBinding
import org.gsm.olio.util.Constants.TAG


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val binding by lazy { FragmentLoginBinding.inflate(layoutInflater) }
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var requestActivity: ActivityResultLauncher<Intent>
    private val lvm: LoginViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = with(binding) {
        lifecycleOwner = this@LoginFragment
        fragment = this@LoginFragment
        initGso()
        resultActivity()

        return root
    }

    override fun onStart() {
        checkGso()
        super.onStart()
    }

    private fun checkGso() {
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        account?.run { findNavController().navigate(R.id.action_loginFragment_to_mainFragment) }
    }

    private fun initGso() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_client_id))
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

    }

    fun signIn() {
        var signInIntent: Intent = googleSignInClient.signInIntent
        requestActivity.launch(signInIntent)
        Log.d(TAG, "signIn: 클릭")
    }

    private fun resultActivity() {
        requestActivity = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                try {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                    val account = task.result!!

                    lvm.loginCheck(account.idToken.toString())
                    Log.d(TAG, "resultActivity: Token : ${account.idToken}")

                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                } catch (e: ApiException) {
                    e.printStackTrace()
                }
            }else{
                Toast.makeText(requireContext(), "학교 아이디를 사용해 주세요.", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "resultActivity: ${it.resultCode}")
            }
        }
      }


}