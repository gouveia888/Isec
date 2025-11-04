package pt.isec.pd.spring_boot.exemplo1.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import pt.isec.pd.spring_boot.exemplo1.Application;
import pt.isec.pd.spring_boot.exemplo1.models.MovieCharacter;

import java.io.*;
import java.util.List;

//Nao pode ter verbos no HTTP do genero criar/criar, apagar, atualizar
//Pasta deve estar fora da pasta do projeto porque senao nao consegue correr o jar direito
//com o jar e com a pasta nos resouces fica dificil gerir os ficheiros
@RestController
@RequestMapping("movies/harry-potter-resources/characters")
public class HarryPotterCharactersController {

    public HarryPotterCharactersController(ContentNegotiatingViewResolver contentNegotiatingViewResolver) {
    }

    @GetMapping
    public ResponseEntity<?> getCharacters(@RequestParam(name="index",required = false) Integer index) { //http://localhost:8080/movies/harry-potter-resources/characters?index=3

        try (InputStream is = new FileInputStream(Application.getResourceDirectory() +
                File.separator + "data" + File.separator + "characters-description.json")) {
            ObjectMapper mapper = new ObjectMapper();
            List<MovieCharacter> characterList = mapper.readValue(is, new TypeReference<List<MovieCharacter>>() {
            });

            //Gson gson = new Gson();
            //Type type = new TypeToken<List<MovieCharacter>>() {}.getType();
            //List<MovieCharacter> characterList = gson.fromJson(new InputStreamReader(is), type);

            if (index != null && (index < 0 || index >= characterList.size())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND) //erro 404
                        .contentType(MediaType.parseMediaType("text/plain"))
                        .body("Character with index " + index);
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/json"))
                    .body(index == null ? characterList : characterList.get(index));

        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND) //erro 404
                    .contentType(MediaType.parseMediaType("text/plain"))
                    .body("Character with index " + index);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("images/{fileName}") //parte do caminho variavel
    public ResponseEntity<?> getImage(@PathVariable("fileName") String fileName) { //http://localhost:8080/movies/harry-potter-resources/characters/images/test.png

        try(InputStream is = new FileInputStream(Application.getResourceDirectory() +
                File.separator + "images" + File.separator + fileName)) {

            byte[] imageData = is.readAllBytes();

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG) //definir o tipo de conteudo
                    .body(imageData); //corpo da resposta

        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.parseMediaType("text/plain"))
                    .body("Image file " + fileName + " not found.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("images")
    public ResponseEntity<?> getFileNames(@RequestParam(name="index",required = false) Integer index) { //http://localhost:8080/movies/harry-potter-resources/characters/images

        try{

            File imagesDir = new File(Application.getResourceDirectory() +
                    File.separator + "images");


        String [] contentList = imagesDir.list();

        if(contentList == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error reading images directory where da images are located.");
        }

        if(index == null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/json")) //definir o tipo de conteudo
                    .body(contentList);
        }

        if(index < 0 || index >= contentList.length) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.parseMediaType("text/plain"))
                    .body(contentList[index]);
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG) //definir o tipo de conteudo
                .body(imagesDir.list()); //corpo da resposta

        } catch (SecurityException e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("images/{fileName}/length")
    public ResponseEntity<?> getImageLength(@PathVariable("fileName") String fileName) {

        try{
            File imageFile = new File(Application.getResourceDirectory() +
                    File.separator + "images" + File.separator + fileName.toLowerCase());

            if (!imageFile.exists() || !imageFile.isFile()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.parseMediaType("text/plain"))
                        .body("Image file " + fileName + " not found.");
            }
            long fileLength = imageFile.length();

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("text/plain"))
                    .body(fileLength);

        } catch (SecurityException e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping
    public ResponseEntity<?> updateNewFile(@PathVariable("new-file-name") String fileName,
                                        @RequestHeader(HttpHeaders.CONTENT_TYPE) String contentType,
                                        @RequestBody byte[] fileContent) { //http://localhost:8080/movies/harry-potter-resources/characters/images

        try{
            File file = new File(Application.getResourceDirectory() +
                    File.separator + "images" + File.separator + fileName);

            if(file.exists()) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .contentType(MediaType.parseMediaType("text/plain"))
                        .body("File " + fileName + " already exists.");
            }

            if(!contentType.equalsIgnoreCase(MediaType.IMAGE_PNG.toString())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .contentType(MediaType.parseMediaType("text/plain"))
                        .body("Only PNG images are supported.");
            }

            try(FileOutputStream fout = new FileOutputStream(file)) {
                fout.write(fileContent);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("text/plain"))
                    .body("File " + fileName + " created successfully with " + fileContent.length + " bytes.");

        }catch (SecurityException e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
