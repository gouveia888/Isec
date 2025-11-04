package pt.isec.pd.spring_boot.exemplo1.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pt.isec.pd.spring_boot.exemplo1.models.MovieCharacter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

//V2 => Alternativa com os ficheiros no projeto/jar (main/resources)
@RestController
@RequestMapping("movies/harry-potter/characters/v2")
public class HarryPotterCharactersControllerV2 {

    @GetMapping
    public ResponseEntity<?> getCharacters(@RequestParam(name = "index", required = false) Integer index) {

        try (InputStream is = getClass().getResourceAsStream("/harry-potter-resources/data/characters-description.json")){

            if (is == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(MediaType.parseMediaType("text/plain"))
                        .body("File not found: characters-description.json");
            }

            ObjectMapper mapper = new ObjectMapper();
            List<MovieCharacter> characterList = mapper.readValue(is, new TypeReference<List<MovieCharacter>>() {});

            if(index != null && (index < 0 || index >= characterList.size())){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.parseMediaType("text/plain"))
                        .body("Out of range index: " + index);
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/json"))
                    .body(index == null ? characterList : characterList.get(index));

        }catch(SecurityException | IOException ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("images/{file-name}")
    public ResponseEntity<?> getImage(@PathVariable("file-name") String fileName) {
        try (InputStream is = getClass().getResourceAsStream("/harry-potter-resources/images/"+fileName.toLowerCase())){

            if (is == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.parseMediaType("text/plain"))
                        .body("File not found: " + fileName);
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(is.readAllBytes());

        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}