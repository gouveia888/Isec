package pt.isec.a2019112767.aula3

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pt.isec.a2019112767.aula3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var operando1: Double? = null
    private var operacaoPendente: String? = null
    private var aEscreverSegundoOperando = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*enableEdgeToEdge()
        setContentView(R.layout.activity_main)


            val tvMsg : TextView = findViewById(R.id.tvMsg)
            tvMsg.text = "DEIS-AMOV"
            tvMsg.textSize = 20F


            android:layout_weight="1" NO MANIFESTO DA PESO AOS ELEMENTOS
        */

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        binding.limpar.setOnClickListener(::methodOnAC)
        binding.resultado.setOnClickListener(::methodOnAC)
        binding.percentagem.setOnClickListener(::methodOnAC)
        binding.virgula.setOnClickListener(::methodOnAC)
        binding.igual.setOnClickListener(::methodOnAC)
        binding.n0.setOnClickListener(::methodOnAC)
        binding.n1.setOnClickListener(::methodOnAC)
        binding.n2.setOnClickListener(::methodOnAC)
        binding.n3.setOnClickListener(::methodOnAC)
        binding.n4.setOnClickListener(::methodOnAC)
        binding.n5.setOnClickListener(::methodOnAC)
        binding.n6.setOnClickListener(::methodOnAC)
        binding.n7.setOnClickListener(::methodOnAC)
        binding.n8.setOnClickListener(::methodOnAC)
        binding.n9.setOnClickListener(::methodOnAC)
        binding.divisao.setOnClickListener(::methodOnAC)
        binding.mais.setOnClickListener(::methodOnAC)
        binding.maismenos.setOnClickListener(::methodOnAC)
        binding.menos.setOnClickListener(::methodOnAC)
        binding.multiplicacao.setOnClickListener(::methodOnAC)


        /*binding.tvMsg.text = "DEIS-AMOV"
        binding.tvMsg.textSize = 20F
        binding.tvMsg.setTextColor(Color.BLACK)

        //val btn : Button = findViewById(R.id.btnOK)


        binding.btnOk.setOnClickListener{
            binding.tvMsg.text = "Clicou"
        }
        binding.btnOk.setOnClickListener(::methodOnClick1) //dois-pontos duplos :: são usados para criar referências a membros
    }//fim onCreate
    fun methodOnClick1(v : View){
        binding.tvMsg.text="Clicou forma 2"
    }

    fun methodOnClick2(v : View){
        binding.tvMsg.text="DEIS-AMOV"*/


    }



    fun methodOnAC(v : View){
        //binding.resultado.text = (v as Button).text
        val button = v as Button

        when(button.id){
            R.id.n0, R.id.n1, R.id.n2, R.id.n3, R.id.n4, R.id.n5, R.id.n6, R.id.n7, R.id.n8, R.id.n9 -> {
                adicionarDigito(button.text.toString())
            }
            // Botões de operação
            R.id.mais, R.id.menos, R.id.multiplicacao, R.id.divisao -> {
                escolherOperacao(button.text.toString())
            }
            // Botão de igual
            R.id.igual -> calcularResultado()

            // Botão de limpar (AC)
            R.id.limpar -> limparTudo()

            // Botão da vírgula
            R.id.virgula -> adicionarVirgula()

            // Outras operações
            R.id.maismenos -> inverterSinal()
            R.id.percentagem -> calcularPercentagem()
        }
    }

    private fun adicionarDigito(digito: String) {
        val visor = binding.resultado
        // Se o visor mostra "0" ou se estamos a começar a inserir o segundo número
        if (visor.text.toString() == "0" || aEscreverSegundoOperando) {
            visor.text = digito
            aEscreverSegundoOperando = false // Desativa a flag
        } else {
            // Caso contrário, anexa o dígito ao número existente
            visor.append(digito)
        }
    }

    private fun escolherOperacao(operacao: String) {
        val numeroNoVisor = binding.resultado.text.toString().toDoubleOrNull()
        val visor = binding.resultado
        // Se o utilizador clica num operador logo a seguir a outro,
        // apenas atualizamos a operação pendente.
        if (aEscreverSegundoOperando) {
            operacaoPendente = operacao
            visor.append(operacao)
            return // Sai da função para não fazer mais nada
        }

        // Se não houver número no visor, não faz nada
        if (numeroNoVisor == null) return

        // Se já temos um primeiro operando, devemos calcular o resultado
        // antes de aplicar a nova operação (ex: 3 + 2 * -> calcula 3+2 primeiro)
        if (operando1 != null && operacaoPendente != null) {
            calcularResultado()
            // Após calcular, o resultado fica no visor e o operando1 é atualizado com esse valor
            operando1 = binding.resultado.text.toString().toDoubleOrNull()
        } else {
            // Se for a primeira operação, apenas guarda o número do visor
            operando1 = numeroNoVisor
        }

        // Guarda a nova operação e prepara para o segundo número
        operacaoPendente = operacao
        aEscreverSegundoOperando = true
        visor.append(operacao)
    }


    private fun calcularResultado() {
        // Pega o segundo número do visor
        val operando2 = binding.resultado.text.toString().toDoubleOrNull()

        // Só calcula se tiver todos os dados necessários
        if (operando1 == null || operacaoPendente == null || operando2 == null) {
            return
        }

        // Realiza o cálculo
        val resultado = when (operacaoPendente) {
            "+" -> operando1!! + operando2
            "-" -> operando1!! - operando2
            "*" -> operando1!! * operando2
            "/" -> if (operando2 != 0.0) operando1!! / operando2 else Double.NaN // Evita divisão por zero
            else -> operando2
        }

        mostrarResultado(resultado)
        operando1 = null // Reinicia para permitir novas contas
    }

    private fun limparTudo() {
        binding.resultado.text = "0"
        operando1 = null
        operacaoPendente = null
        aEscreverSegundoOperando = false
    }

    private fun adicionarVirgula() {
        // Adiciona um ponto decimal apenas se ainda não houver um
        if (!binding.resultado.text.contains(".")) {
            binding.resultado.append(".")
        }
    }

    private fun inverterSinal() {
        val numeroAtual = binding.resultado.text.toString().toDoubleOrNull()
        if (numeroAtual != null) {
            mostrarResultado(-numeroAtual)
        }
    }

    private fun calcularPercentagem() {
        val numeroAtual = binding.resultado.text.toString().toDoubleOrNull()
        if (numeroAtual != null) {
            mostrarResultado(numeroAtual / 100.0)
        }
    }

    private fun mostrarResultado(resultado: Double) {
        // Formata o resultado para remover o ".0" se for inteiro e trata erros
        val textoResultado = if (resultado.isNaN() || resultado.isInfinite()) {
            "Erro"
        } else if (resultado % 1.0 == 0.0) {
            resultado.toLong().toString()
        } else {
            resultado.toString()
        }
        binding.resultado.text = textoResultado
    }
}