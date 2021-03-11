package fr.yapagi.stepbystep.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.vvalidator.form
import fr.yapagi.stepbystep.R
import fr.yapagi.stepbystep.databinding.FragmentRegisterBinding


private lateinit var binding : FragmentRegisterBinding

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        form {
            input(binding.etName){
                isNotEmpty()
                length().atLeast(3)
            }
            input(binding.etEmail){
                isNotEmpty()
                length().atLeast(6)
                matches("^[a-zA-Z0-9.!#\$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*\$")
            }
            input(binding.etPassword){
                isNotEmpty()
                length().atLeast(8)
            }
            input(binding.etAdresse){
                isNotEmpty()
                length().atLeast(5)
            }

            submitWith(binding.btnRegister) { result ->

            }
        }
        super.onViewCreated(view, savedInstanceState)
    }
}