package pt.isec.pd.spring_boot.exemplo1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    //alterar os argumentos por causa do caminho das pastas
	private static String resourceDirectory;

    public static String getResourceDirectory() {
        return resourceDirectory;
    }

	public static void main(String[] args) {
		if(args.length < 1) {
			System.out.println("Must provide at least one argument in the command line: " +
					"path to the directory where data and image files are located " +
					"(Harry Potter movie characters).");
			return;
		}

		resourceDirectory = args[0];

		SpringApplication.run(Application.class, args);
	}

}
