/**************************************************
 * Javascript - Ficha 10
 * ************************************************/

function alerta2(){
    alert("Exercicio Javascript - Alert 2");
}

function click_texto(){
    document.querySelector('.title').textContent = 'Tecnologia Web - Javascript';
}

function out_texto(){
    document.querySelector('.title').textContent = 'Introdução ao Javascript';
}

function over_texto(){

    document.querySelector('.title').innerHTML='<h4>Introduçao ao javaascrpits</h4>';
}

function contorno(){

    //document.querySelector('.panel-animals').setAttribute('style','background-color:#FF000022');
    
    var painel = document.querySelector('.panel-animals');
    var estilo = document.querySelector('.panel-animals').classList.contains('border-active');
    console.log(estilo);
    console.log(painel);

    if(estilo == true){
        painel.classList.remove('border-active');
        painel.style.backgroundColor='white';
    }else{
        painel.classList.add('border-active');
        painel.style.backgroundColor='#FF000022';
    }
    
}

/*document.getElementById('btn-alert2').addEventListener('click', alerta2);

document.getElementById('btn-alert2').addEventListener('click', function(){
    alert('Exercicio Javascript - Alert 2');
}
);*/

//melhor implementação

document.getElementById('btn-alert2').addEventListener('click', function(){
    alerta2();
});

document.getElementById('btn-titulo').addEventListener('click', click_texto);
document.getElementById('btn-titulo').addEventListener('mouseover', over_texto);
document.getElementById('btn-titulo').addEventListener('mouseout', out_texto);

document.querySelector('.btn-border').addEventListener('click', contorno);