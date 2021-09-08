package com.example.calculadoradebitcoin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.net.URL
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    val API_URL = "https://www.mercadobitcoin.net/api/BTC/ticker/"
    var cotacaoBitcoin:Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buscarCotacao()
        //listener do botão calcular
        val btn_calcular = findViewById<Button>(R.id.btn_calcular)
        btn_calcular.setOnClickListener {
            calcular()
        }
    }
    fun calcular(){
        val txt_valor = findViewById<TextView>(R.id.txt_valor);
        val txt_qtd_bitcoins = findViewById<TextView>(R.id.txt_qtd_bitcoins);


        if(txt_valor.text.isEmpty()) {
            txt_valor.error = "Preencha um valor"
            return
        }
        val valor_digitado = txt_valor.text.toString().replace(",", ".").
        toDouble()
        val resultado = if(cotacaoBitcoin > 0) valor_digitado / cotacaoBitcoin else 0.0
        txt_qtd_bitcoins.text = "%.8f".format(resultado)

    }

    fun buscarCotacao(){


        //iniciar uma tarefa assincrona
        doAsync {


            //Acessar a API e buscar seu resultado
            val resposta = URL(API_URL).readText()

            //Acessando a cotação da String em Json
            cotacaoBitcoin = JSONObject(resposta).getJSONObject("ticker").getDouble("last")

            //Formatação em moeda
            val f= NumberFormat.getCurrencyInstance(Locale("pt", "br"))
            val cotacaoFormatada = f.format(cotacaoBitcoin)


            uiThread {
                //Atualizando a tela com a cotação atual
                val txt_cotacao = findViewById<TextView>(R.id.txt_cotacao)
                txt_cotacao.setText("$cotacaoFormatada")

            }

        }

    }



}