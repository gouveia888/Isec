function search_bar() {
    var barra = document.querySelector('.search-bar');
    var icon = document.querySelector('#pesquisa');

    if(barra.style.display==='none' || barra.style.display === ''){
        barra.style.display = 'block';
        } else {
            barra.style.display = 'none';
        }

    if(icon.classList.contains('fa-search')){
        icon.classList.add('fa-close')
        icon.classList.remove('fa-search')
        barra.focus();
        } else {
            icon.classList.add('fa-search')
            icon.classList.remove('fa-close')
            
        }
}

window.addEventListener('scroll', function () {
    var menu = document.querySelector('.megamenu')
    var icons = document.querySelectorAll('.scrolled')
    var posicaoscroll = window.scrollY;
    var Lviewport = window.innerWidth;

    if (posicaoscroll > 129 && Lviewport > 525) {
        menu.style.position = 'fixed';
        menu.style.top = '0';
        icons[0].style.display='block';     
        icons[1].style.display='block';
        icons[2].style.display='block';
    } else {
        menu.style.position = 'static';
        icons[0].style.display='none';
        icons[1].style.display='none';
        icons[2].style.display='none';  
    }

});

function ajustarConteudoDoBotao() {
    var donate = document.querySelector('#donate')
    var info = document.querySelector('#get-help')
    var Lviewport = window.innerWidth;

    if (Lviewport < 525) {
        donate.textContent = '';
        info.textContent = '';
    } else {
        donate.textContent = 'Donate';
        info.textContent = 'Get Help';
    }
}

function doacao(){
    
    var popup = document.querySelector('#popup')
    
    if(popup.style.display==='block'){
        popup.style.display='none';
    }else{
        popup.style.display='block';
    }
}

function don_pais(){

    var don_periodica = document.querySelector('#periodica')
    var unica_lab = document.querySelector('label[for="unica"]')
    var don_periodica_lab = document.querySelector('label[for="periodica"]')
    var pais = document.getElementsByClassName('paises')
    var mensal = document.querySelector('#mensal')
    var quantidade = document.querySelector('#inserido')
    var valor4 = document.querySelector('#valor_inserido')
    var valor1_lab = document.querySelector('label[for="vinte"]')
    var valor2_lab = document.querySelector('label[for="quarenta"]')
    var valor3_lab = document.querySelector('label[for="oitenta"]')
    var valor4_lab = document.querySelector('label[for="inserido"]')
    var valor1 = document.querySelector('#vinte')
    var valor2 = document.querySelector('#quarenta')
    var valor3 = document.querySelector('#oitenta')
    var cartao = document.querySelector('#cartao')
    var cartao1 = document.querySelector('#cartao1')
    var paypal = document.querySelector('#paypal')
    var multibanco = document.querySelector('#multibanco')
    var mbway = document.querySelector('#mbway')
    var debito = document.querySelector('#debito')
    var cartao_lab = document.querySelector('label[for="cartao"]')
    var cartao1_lab = document.querySelector('label[for="cartao1"]')
    var paypal_lab = document.querySelector('label[for="paypal"]')
    var multibanco_lab = document.querySelector('label[for="multibanco"]')
    var mbway_lab = document.querySelector('label[for="mbway"]')
    var debito_lab = document.querySelector('label[for="debito"]')
    var pagamento_unico = document.querySelector('#pagamento_unico')
    var pagamento_periodica = document.querySelector('#pagamento_periodica')

    if(don_periodica.checked){
        pais[0].style.display='none';
        pais[1].style.display='none';
        mensal.style.display='block';
        don_periodica_lab.style.backgroundColor='yellow';
        unica_lab.style.backgroundColor='white';
        pagamento_unico.style.display='none';
        pagamento_periodica.style.display='block';
    }else{
        pais[0].style.display='inline';
        pais[1].style.display='inline';
        mensal.style.display='none';
        don_periodica_lab.style.backgroundColor='white';
        unica_lab.style.backgroundColor='yellow';
        pagamento_unico.style.display='block';
        pagamento_periodica.style.display='none';
    }

    if(quantidade.checked){
        valor4.style.display='inline'
        valor4_lab.style.backgroundColor='yellow';
        cal_refeicao(valor4.value)
    }else{
        valor4.style.display='none'
        valor4_lab.style.backgroundColor='white';
    }

    if(valor1.checked){
        valor1_lab.style.backgroundColor='yellow';
        cal_refeicao(valor1.value)
    }else{
        valor1_lab.style.backgroundColor='white';
    }

    if(valor2.checked){
        valor2_lab.style.backgroundColor='yellow';
        cal_refeicao(valor2.value)
    }else{
        valor2_lab.style.backgroundColor='white';
    }

    if(valor3.checked){
        valor3_lab.style.backgroundColor='yellow';
        cal_refeicao(valor3.value)
    }else{
        valor3_lab.style.backgroundColor='white';
    }

    if(cartao.checked){
        cartao_lab.style.backgroundColor='yellow';
    }else{
        cartao_lab.style.backgroundColor='white';
    }

    if(cartao1.checked){
        cartao1_lab.style.backgroundColor='yellow';
    }else{
        cartao1_lab.style.backgroundColor='white';
    }

    if(paypal.checked){
        paypal_lab.style.backgroundColor='yellow';
    }else{
        paypal_lab.style.backgroundColor='white';
    }

    if(multibanco.checked){
        multibanco_lab.style.backgroundColor='yellow';
    }else{
        multibanco_lab.style.backgroundColor='white';
    }

    if(mbway.checked){
        mbway_lab.style.backgroundColor='yellow';
    }else{
        mbway_lab.style.backgroundColor='white';
    }

    if(debito.checked){
        debito_lab.style.backgroundColor='yellow';
    }else{
        debito_lab.style.backgroundColor='white';
    }
}

function tipo_doador(){

    var don_empresa = document.querySelector('#don_empresa')
    var particular = document.getElementsByClassName('particular')
    var empresa = document.getElementsByClassName('empresa')
    var empresa_lab = document.querySelector('label[for="don_empresa"]')
    var particular_lab = document.querySelector('label[for="don_part"]')

    if(don_empresa.checked){
        empresa[0].style.display='inline';
        particular[0].style.display='none';
        empresa_lab.style.backgroundColor='yellow';
        particular_lab.style.backgroundColor='white';
    }else{
        empresa[0].style.display='none';
        particular[0].style.display='inherit';
        empresa_lab.style.backgroundColor='white';
        particular_lab.style.backgroundColor='yellow';
    }
}

function cal_refeicao(valor) {

    var num_refeicoes = Math.floor(valor / 1.5);
    var texto_ref = document.querySelector('#refeicoes');

    texto_ref.innerHTML = `<p>O seu donativo permitirá fornecer aproximadamente ${num_refeicoes} refeições.<p>`;

    if(valor>=40){
        var pessoas = num_refeicoes*2;
        texto_ref.innerHTML = `<p>O seu donativo permitirá alimentar diariamente ${pessoas} pessoas.</p>`;
    }
}

function menu(){
    
    var menu = document.querySelector('.megamenu');

    if (menu.style.display === "block") {
      menu.style.display = "none";
    } else {
      menu.style.display = "block";
    }
}


document.addEventListener('DOMContentLoaded', function () {

    var Lviewport = window.innerWidth;

    if (Lviewport < 525) {
        
        var submenu = document.querySelectorAll('.sub-menu');
  
        for (var i = 0; i < submenu.length; i++) {
            submenu[i].style.display = 'none';
          }
      
        var menuItems = document.querySelectorAll('.menu');
      
        // Para cada item de menu, adiciona um ouvinte de evento de clique
        menuItems.forEach(function (menuItem) {
          menuItem.addEventListener('click', function (event) {
            // Impede o comportamento padrão do link
            event.preventDefault();
      
            var currentSubMenu = this.querySelector('.sub-menu');
      
            // Oculta todos os outros submenus, exceto o atual
            submenu.forEach(function (subMenu) {
              if (subMenu !== currentSubMenu) {
                subMenu.style.display = 'none';
              }
            });
      
            if (currentSubMenu.style.display === 'none') {
              currentSubMenu.style.display = 'block';
            } else {
              currentSubMenu.style.display = 'none';
            }
          });
        });

    }
     
  });

  


window.addEventListener('resize', ajustarConteudoDoBotao);
ajustarConteudoDoBotao();

document.querySelector('.pesquisa').addEventListener('click', function () {
    search_bar();
});

document.querySelector('#donate').addEventListener('click', function () {
    doacao();
});

document.querySelector('#donate_menu').addEventListener('click', function () {
    doacao();
});

document.querySelector('#periodica').addEventListener('change', function () {
    don_pais();
});

document.querySelector('#unica').addEventListener('change', function () {
    don_pais();
});

document.querySelector('#don_part').addEventListener('change', function () {
    tipo_doador();
});

document.querySelector('#don_empresa').addEventListener('change', function () {
    tipo_doador();
});

document.querySelector('#vinte').addEventListener('change', function () {
    don_pais();
});

document.querySelector('#quarenta').addEventListener('change', function () {
    don_pais();
});

document.querySelector('#oitenta').addEventListener('change', function () {
    don_pais();
});

document.querySelector('#inserido').addEventListener('change', function () {
    don_pais();
});

document.querySelector('#valor_inserido').addEventListener('change', function () {
    don_pais();
});

document.querySelector('#hamburger').addEventListener('click', function () {
    menu();
});

document.querySelector('#cartao').addEventListener('change', function () {
    don_pais();
});

document.querySelector('#cartao1').addEventListener('change', function () {
    don_pais();
});

document.querySelector('#paypal').addEventListener('change', function () {
    don_pais();
});

document.querySelector('#multibanco').addEventListener('change', function () {
    don_pais();
});

document.querySelector('#mbway').addEventListener('change', function () {
    don_pais();
});

document.querySelector('#debito').addEventListener('change', function () {
    don_pais();
});