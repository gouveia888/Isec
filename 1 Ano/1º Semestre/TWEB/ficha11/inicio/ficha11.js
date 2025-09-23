/**************************************************
 * Javascript - Ficha 11
 * ************************************************/

window.onload = function () {
    init(); 
};



var form = document.querySelector('form');
// Evento para que o submit não submeta qualquer dados
form.addEventListener('submit', function (event) {
    event.preventDefault();
});



function validaNome(fld) {
    var letters = /^[A-zÀ-ú]+$/;
    if (fld.value.trim().match(letters)) 
		return true;
    return false;
}


function init() {
    painel.style.visibility = 'hidden';
    painelstt.style.display="none";
    nExame.value='';
    ntp.value='';
    pnome.focus();

    msg.style.display='none';

    painelstt.style.classList.remove('reprovado');
    painelstt.style.classList.remove('aprovado');

    ntp.setCustomValidity('');
    pnome.setCustomValidity('');
    unome.setCustomValidity('');
}

const painel = document.getElementById("notaTP-painel");
const ntp = document.getElementById("notaTP");
const nExame = document.getElementById("notaExame");
const painelstt = document.getElementById("painel-status");
const pnome = document.getElementById("pNome");
const unome = document.getElementById("uNome");


function trabalho(){

    var nao = document.getElementById("tpNao");
    
    if(nao.checked){
        painel.style.visibility = 'hidden';
    }else{
        painel.style.visibility = 'visible';
    }
}

function calculaNotaFinal(ntp, nExame){
    var notaFinal = (ntp*0.08)+(nExame*0.6);
    return Math.round(notaFinal);
}

function setStatus(notaFinal, pnome, unome, txt=''){

    var mensagem = document.getElementById("msg");
    var situacao = document.getElementById("situacao");
    var nota = document.getElementById("notaFinal");
    var pnome = document.getElementById("pNome");
    var unome = document.getElementById("uNome");

    painelstt.style.display='block';
  
    if(notaFinal>=10){
        painelstt.classList.add('aprovado');
        situacao.textContent = 'Aluno ' + pnome.value + ' ' + unome.value + ' Aprovado!';
    }else{
        painelstt.classList.add('reprovado');
        situacao.textContent = 'Aluno '; + pnome.value + ' ' + unome.value + ' Reprovado!';
    }

    nota.textContent = notaFinal;
    
    if(txt!=''){
        mensagem.style.display='inline'
        mensagem.innerHTML = txt;
    }else{
        mensagem.style.display='none'
    }   

}

var btnnotaFinal = document.getElementById("btnCalcularNota");
    btnnotaFinal.addEventListener('click',validaFormulario);

function validaFormulario() {
    var sim = document.getElementById("tpSim");
    var nExame = document.getElementById("notaExame").value;

    if (form.checkValidity()) {
        if (sim.checked && ntp.value.length < 1) {
            ntp.setCustomValidity('Introduza a nota!'); //nota do preenchimento
            return;
        }

            if(!validaNome(pnome)){
                pnome.setCustomValidity('Nome Invalido! Especifique o primeiro nome');
                return;
            }

            if(!validaNome(unome)){
                unome.setCustomValidity('Nome Invalido! Especifique o ultimo nome');
                return;
            }

        var notaexame = nExame;
        var nota = calculaNotaFinal(ntp.value, nExame);
        var txt = '';

        if (notaexame < 7) {
            if (nota >= 10){
                nota = 9;
                txt = 'Sem mínimos em Exame!';
            }
        }
        setStatus(nota, txt);
    } else {
        form.querySelectorAll(':invalid')[0].focus(); //colocar cursor no elemento
    }
}


/*tpNao.addEventListener('click',function()){

}*/


document.getElementById('tpNao').addEventListener('change', function(){
    trabalho();

});

document.getElementById('tpSim').addEventListener('change', function(){
    trabalho();

});

document.getElementById('btnCalcularNota').addEventListener('click', function(){
    calculaNotaFinal();

});

document.getElementById('btnReset').addEventListener('click', function(){
    init();

});