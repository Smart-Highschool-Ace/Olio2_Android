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
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.notify
import org.gsm.olio.MyApplication
import org.gsm.olio.R
import org.gsm.olio.databinding.FragmentLoginBinding
import org.gsm.olio.util.Constants.SERVER_CLIENT_ID
import org.gsm.olio.util.Constants.TAG
import org.gsm.olio.util.GoogleLogin
import java.util.*


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val binding: FragmentLoginBinding by viewBinding(CreateMethod.INFLATE)
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var requestActivity: ActivityResultLauncher<Intent>
    private val lvm: LoginViewModel by viewModels()
    private val gso by lazy { GoogleLogin(requireContext()) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = with(binding) {
        initViews()
        initGso()
        resultActivity()

        return root
    }

    private fun initViews() = with(binding) {
        lifecycleOwner = this@LoginFragment
        fragment = this@LoginFragment
        viewModel = lvm
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        var firstExecution = MyApplication.prefs.firstExecution
        if (!firstExecution) {
            Log.d(
                TAG,
                "LoginFragment_onCreate: ${findNavController().navigate(R.id.action_loginFragment_to_baseGuideFragment)} "
            )
            MyApplication.prefs.firstExecution = true
        }
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        checkGso()
        super.onStart()
    }

    private fun checkGso() {
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        account?.run {
            gso.signOut(requireContext())
        }
    }

    private fun initGso() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(SERVER_CLIENT_ID)
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
            lvm.setLoading(true)
            if (it.resultCode == Activity.RESULT_OK) {
                try {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                    val account = task.result!!

                    lvm.loginCheck(account.idToken.toString())

                    lvm.firstLogin.observe(viewLifecycleOwner, androidx.lifecycle.Observer { it ->
                        Log.d(TAG, "resultActivity: $it")
                        if (it == true) {
                            findNavController().navigate(R.id.action_loginFragment_to_firstLoginFragment)
                        } else if (it == false) {
                            findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                        }
                        lvm.setLoading(false)
                    } )

                } catch (e: ApiException) {
                    e.printStackTrace()
                }
            } else {
                Toast.makeText(requireContext(), "학교 아이디를 사용해 주세요.", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "resultActivity: ${it.resultCode}")
                lvm.setLoading(false)
            }
        }
    }


}