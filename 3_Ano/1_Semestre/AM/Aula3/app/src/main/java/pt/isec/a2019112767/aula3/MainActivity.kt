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

        /*binding.tvMsg.text = "DEIS-AMOV"
        binding.tvMsg.textSize = 20F
        binding.tvMsg.setTextColor(Color.BLACK)

        //val btn : Button = findViewById(R.id.btnOK)


        binding.btnOk.setOnClickListener{
            binding.tvMsg.text = "Clicou"
        }
        binding.btnOk.setOnClickListener(::methodOnClick1) //dois-pontos duplos :: são usados para criar referências a membros
    }
    fun methodOnClick1(v : View){
        binding.tvMsg.text="Clicou forma 2"
    }

    fun methodOnClick2(v : View){
        binding.tvMsg.text="DEIS-AMOV"*/
    }

    fun methodOnAC(v : View){
        binding.resultado.text = (v as Button).text
    }
}