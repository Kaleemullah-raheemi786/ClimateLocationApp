package com.example.climatelocationapp.views

import android.app.Activity
import android.content.pm.PackageManager
import androidx.biometric.BiometricPrompt;
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.climatelocationapp.R
import com.example.climatelocationapp.databinding.FragmentLoginScreenBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.concurrent.Executor


class LoginScreenFragment : Fragment() {

    private lateinit var binding: FragmentLoginScreenBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginScreenBinding.inflate(layoutInflater, container, false)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        binding.btnLogin.isEnabled = false
        binding.imgFinger.setOnClickListener {
            checkDeviceHasBiometric()
        }
        setExecutor()

        return binding.root
    }

    private fun setExecutor() {
        // creating a variable for our Executor
        executor = ContextCompat.getMainExecutor(requireContext())
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence,
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(context,
                        "Authentication error: $errString", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult,
                ) {
                    super.onAuthenticationSucceeded(result)
                    Toast.makeText(context, "Authentication succeeded!", Toast.LENGTH_SHORT).show()
                    fetchLocation(fusedLocationProviderClient)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(context, "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric login for my app")
            .setSubtitle("Log in using your biometric credential")
            .setNegativeButtonText("Use account password")
            .build()

        binding.btnLogin.setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }
    }

    private fun checkDeviceHasBiometric() {
        val biometricManager: androidx.biometric.BiometricManager =
            androidx.biometric.BiometricManager.from(requireContext())
        when (biometricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Toast.makeText(
                    context,
                    "You can use the fingerprint sensor to login",
                    Toast.LENGTH_SHORT
                ).show()
                binding.btnLogin.isEnabled = true
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Toast.makeText(
                    context,
                    "This device doesnot have a fingerprint sensor",
                    Toast.LENGTH_SHORT
                ).show()
                binding.btnLogin.isEnabled = false
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Toast.makeText(
                    context,
                    "The biometric sensor is currently unavailable",
                    Toast.LENGTH_SHORT
                ).show()
                binding.btnLogin.isEnabled = false
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Toast.makeText(
                    context,
                    "Your device doesn't have fingerprint saved,please check your security settings",
                    Toast.LENGTH_SHORT
                ).show()
                binding.btnLogin.isEnabled = false
            }
        }
    }
   fun fetchLocation(fusedLocationProviderClient: FusedLocationProviderClient){
        val task = fusedLocationProviderClient.lastLocation
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireContext() as Activity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
        }
        task.addOnSuccessListener {
            val lat = it.latitude
            val lon = it.longitude
            findNavController().navigate(
                R.id.action_loginScreenFragment_to_climateLocationFragment,
                Bundle().apply {
                    putString("lat", lat.toString())
                    putString("lon", lon.toString())
                })
        }
    }
}