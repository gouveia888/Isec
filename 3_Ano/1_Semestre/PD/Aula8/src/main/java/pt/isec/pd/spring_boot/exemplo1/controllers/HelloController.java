package pt.isec.pd.spring_boot.exemplo1.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController //class que pode ser mapeada para servir pedidos HTTP
@RequestMapping("hello") //mapeia a classe para o caminho com prefixo /hello
public class HelloController {
    //@GetMapping("/hello")
    @GetMapping()//mapeia o método para o caminho /hello
    public String hello() {
        return "Hello!";
    } //para testar no browser: http://localhost:8080/hello
                                               //no cmd testar com curl -i http://localhost:8080/hello
    //@GetMapping("/hello/{lang}") //mapeia o método para o caminho /hello/{lang}
    /*@GetMapping("{lang}")
    public String helloLanguages(@PathVariable("lang") String language,
                                 @RequestParam(name = "name", required = false, defaultValue = "") String name) {

        return switch (language.toUpperCase()){
            case "UK" -> "Hello " + name + "!";
            case "PT" -> "Ola' " + name + "!";
            case "ES" -> "Hola " + name + "!";
            case "FR" -> "Salut " + name + "!";
            default -> "Unsupported language!"; //nao se deve usar por questoes de UI
        };
    }*/

    @GetMapping("{lang}") //para testar no cmd curl -i "http://localhost:8080/hello/pt?name=Tiago%20Filipe"
    public ResponseEntity<String> helloLanguages(@PathVariable("lang") String language,
                                                 @RequestParam(name = "name", required = false) String name) { //indicar o required é obrigatorio

        String responseBody;

        switch (language.toUpperCase()){
            case "UK" -> responseBody = "Hello";
            case "PT" -> responseBody = "Ola'";
            case "ES" -> responseBody = "Hola";
            case "FR" -> responseBody = "Salut";
            default -> {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not supported language:" + language + ".");
            }
        }

	String responseSuffix = name==null ? "!" : " "+name+"!";
	responseBody += responseSuffix;

        return ResponseEntity.ok(responseBody);
    }
}


