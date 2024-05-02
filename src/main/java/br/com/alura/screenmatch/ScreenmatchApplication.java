package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ScreenmatchApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        var consumiApi = new ConsumoAPI();
        var json = consumiApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=ccb5ae90");
        System.out.println(json);

        var conversor = new ConverteDados();
        DadosSerie dados = conversor.obtemDados(json, DadosSerie.class);
        System.out.println(dados);
    }
}
