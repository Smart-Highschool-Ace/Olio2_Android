package org.gsm.olio.view.fragment.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import org.gsm.olio.R
import org.gsm.olio.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private val binding by lazy { FragmentLoginBinding.inflate(layoutInflater) }
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var requestActivity : ActivityResultLauncher<Intent>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = with(binding) {
        lifecycleOwner = this@LoginFragment
        fragment = this@LoginFragment
        initGso()


        return root
    }

    override fun onStart() {
        checkGso()
        super.onStart()
    }

    private fun checkGso(){
        val account = GoogleSignIn.getLastSignedInAccount(requireContext())
        account?.run { findNavController().navigate(R.id.action_loginFragment_to_mainFragment) }
    }

    private fun initGso(){
      val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestId()
            .build()

       googleSignInClient = GoogleSignIn.getClient(requireContext(),gso)

    }




}