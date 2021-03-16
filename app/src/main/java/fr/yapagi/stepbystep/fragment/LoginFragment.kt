package fr.yapagi.stepbystep.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.vvalidator.form
import fr.yapagi.stepbystep.AccountActivity
import fr.yapagi.stepbystep.DashboardActivity
import fr.yapagi.stepbystep.R
import fr.yapagi.stepbystep.databinding.FragmentLoginBinding


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
                val intent = Intent(activity?.applicationContext, AccountActivity::class.java)
                startActivity(intent)
            }
        }

        super.onViewCreated(view, savedInstanceState)
    }
}