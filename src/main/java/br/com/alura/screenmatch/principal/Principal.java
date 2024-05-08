package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=ccb5ae90";

    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();

    public void menu() {
        System.out.println("Digite o nome da serie: ");
        var serie = leitura.nextLine();
        var json = consumoAPI.obterDados(ENDERECO + serie.replace(" ", "+") + API_KEY);

        DadosSerie dadosSerie = conversor.obtemDados(json, DadosSerie.class);
        System.out.println(dadosSerie);

        List<DadosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
            json = consumoAPI.obterDados(ENDERECO + serie.replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporadas = conversor.obtemDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporadas);
        }

//        temporadas.forEach(System.out::println);
//        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("\nTop 10 episódios");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(10)
                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d))
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);
//
//        System.out.println("Digite o Episodio buscado");
//        String buscaEpisodio = leitura.nextLine();
//        Optional<Episodio> episodioBuscado = episodios.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(buscaEpisodio.toUpperCase()))
//                .findFirst();
//
//        if (episodioBuscado.isPresent()){
//            System.out.println("Episódio encontrado");
//            System.out.println("Temporada :" + episodioBuscado.get().getTemporada());
//        }else {
//            System.out.println("Episodio não encontrado");
//        }


//        System.out.println("A partir de que ano deseja ver os Episodios");
//        int ano = leitura.nextInt();
//        leitura.nextLine();
//
//        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
//
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//
//        episodios.stream()
//                .filter( e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca) )
//                .forEach(e -> System.out.println(
//                        "Temporada: " + e.getTemporada() +
//                        " Episódio: " + e.getTitulo() +
//                                " Data de lançamento " + e.getDataLancamento().format(formatador)
//                ));

        Map <Integer, Double> avaliacaoPorTemporadas = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));

        System.out.println(avaliacaoPorTemporadas);
    }
}
