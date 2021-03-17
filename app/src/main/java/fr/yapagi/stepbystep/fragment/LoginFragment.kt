package fr.yapagi.stepbystep.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.vvalidator.form
import fr.yapagi.stepbystep.AccountActivity
import fr.yapagi.stepbystep.DashboardActivity
import fr.yapagi.stepbystep.R
import fr.yapagi.stepbystep.databinding.FragmentLoginBinding
import fr.yapagi.stepbystep.network.Authenticator
import fr.yapagi.stepbystep.network.DataListener


private lateinit var binding: FragmentLoginBinding

class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        form {
            input(binding.etPassword) {
                isNotEmpty()
                length().atLeast(8)
            }
            input(binding.etEmail) {
                isNotEmpty()
                length().atLeast(6)
                matches("^[a-zA-Z0-9.!#\$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*\$")
            }


            submitWith(binding.btnLogin) { result ->
                val auth = Authenticator()
                auth.login(binding.etEmail.text.toString(),
                    binding.etPassword.text.toString(), object: DataListener {
                        override fun onSuccess(data: Any?) {
                            val intent = Intent(activity?.applicationContext, AccountActivity::class.java)
                            startActivity(intent)
                        }

                        override fun onStart() {}

                        override fun onFailure(error: String) {}

                    })
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }
}