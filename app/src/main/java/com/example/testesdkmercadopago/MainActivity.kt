package com.example.testesdkmercadopago

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.testesdkmercadopago.databinding.ActivityMainBinding
import com.mercadopago.MercadoPagoConfig

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: PaymentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MercadoPagoConfig.setAccessToken(Constants.MERCADO_PAGO_ACCESS_TOKEN)

        viewModel = ViewModelProvider(this).get(PaymentViewModel::class.java)

        viewModel.paymentResult.observe(this) { result ->
            result.fold(
                onSuccess = { url ->
                    binding.txtResult.text = getString(R.string.payment_url, url)
                },
                onFailure = { error ->
                    binding.txtResult.text = getString(R.string.error_payment, error.message)
                }
            )
        }

        binding.btnPagar.setOnClickListener {
            val value = binding.edtValue.text.toString()
            val description = binding.edtDescription.text.toString()
            viewModel.pay(value, description)
        }
    }

}