package pt.isec.pd.spring_boot.exemplo1.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//Nao pode ter verbos no HTTP do genero criar/criar, apagar, atualizar
@RestController
@RequestMapping("movies/harry-potter-resources/characters")
public class HarryPotterCharactersController {

    @GetMapping
    public ResponseEntity<?> getCharacters(@RequestParam(name="index",required = false) Integer index) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .contentType(MediaType.parseMediaType("text/plain"))
                .body("Endpoint not implemented yet!");
    }

    @GetMapping("images/{fileName}") //parte do caminho variavel
    public ResponseEntity<?> getImage(@PathVariable("fileName") String fileName) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .contentType(MediaType.parseMediaType("text/plain"))
                .body("Endpoint not implemented yet!");
    }

    @GetMapping("images")
    public ResponseEntity<?> getFileNames(@RequestParam(name="index",required = false) Integer index) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .contentType(MediaType.parseMediaType("text/plain"))
                .body("Endpoint not implemented yet!");
    }

    @GetMapping("images/{fileName}/length")
    public ResponseEntity<?> getImageLength(@PathVariable("fileName") String fileName) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .contentType(MediaType.parseMediaType("text/plain"))
                .body("Endpoint not implemented yet!");
    }

    @PostMapping("images")
    public ResponseEntity<?> uploadNewFile(@PathVariable("fileName") String fileName,
                                           @RequestHeader(HttpHeaders.CONTENT_TYPE) String contentType,
                                           @RequestBody byte[] fileContent) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .contentType(MediaType.parseMediaType("text/plain")) //podemos definir header e body
                .body("Endpoint not implemented yet!");
    }
}
