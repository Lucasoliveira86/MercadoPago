package com.example.testesdkmercadopago

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadopago.client.payment.PaymentClient
import com.mercadopago.client.payment.PaymentCreateRequest
import com.mercadopago.client.payment.PaymentPayerRequest
import com.mercadopago.exceptions.MPApiException
import com.mercadopago.exceptions.MPException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal

class PaymentViewModel : ViewModel() {

    private val _paymentResult = MutableLiveData<Result<String>>()
    val paymentResult: LiveData<Result<String>> get() = _paymentResult

    fun pay(value: String, description: String) {
        if (value.isEmpty() || description.isEmpty()) {
            _paymentResult.value = Result.failure(Exception("Por favor, preencha todos os campos."))
            return
        }

        try {
            val amount = BigDecimal(value)
            viewModelScope.launch {
                try {
                    val resultString = withContext(Dispatchers.IO) {
                        val client = PaymentClient()
                        val createRequest = PaymentCreateRequest.builder()
                            .transactionAmount(amount)
                            .token("your_cardtoken") // Valor fixo para simulação
                            .description(description)
                            .installments(1)
                            .paymentMethodId("visa")
                            .payer(
                                PaymentPayerRequest.builder()
                                    .email("dummy_email@example.com")
                                    .build()
                            )
                            .build()
                        val payment = client.create(createRequest)
                        payment.toString()
                    }
                    _paymentResult.value = Result.success(resultString)
                } catch (ex: MPApiException) {
                    _paymentResult.value = Result.failure(
                        Exception("Erro no MercadoPago. Status: ${ex.apiResponse.statusCode}, Conteúdo: ${ex.apiResponse.content}")
                    )
                } catch (ex: MPException) {
                    _paymentResult.value = Result.failure(ex)
                }
            }
        } catch (e: Exception) {
            _paymentResult.value = Result.failure(Exception("Valor inválido. Verifique o valor informado."))
        }
    }
}
