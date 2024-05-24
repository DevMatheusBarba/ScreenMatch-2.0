package br.com.alura.screenmatch.model;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table (name = "series")
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;
    private Integer totalTemporadas;
    private Double avaliacao;
    private String escritor;
    @Enumerated(EnumType.STRING)
    private Categoria genero;
    private List<String> atores;
    private String poster;
    private String sinopse;

    @Transient
    private List<Episodio> episodios = new ArrayList<>();


    public Serie(){}

    public Serie(DadosSerie dadosSerie) {
        this.titulo = dadosSerie.titulo();
        this.totalTemporadas = dadosSerie.totalTemporadas();
        this.avaliacao = OptionalDouble.of(Double.valueOf(dadosSerie.avaliacao())).orElse(0);
        this.escritor = dadosSerie.escritor();
        this.genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim());
        this.atores = Optional.of(List.of(dadosSerie.atores().split(","))).orElse(Collections.singletonList("Não foi possivel coletar"));
        this.poster = dadosSerie.poster();
        this.sinopse = dadosSerie.sinopse().trim();
    }

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public String getEscritor() {
        return escritor;
    }

    public Categoria getGenero() {
        return genero;
    }

    public List<String> getAtores() {
        return atores;
    }

    public String getPoster() {
        return poster;
    }

    public String getSinopse() {
        return sinopse;
    }

    @Override
    public String toString() {
        return
                "genero=" + genero +
                ", titulo='" + titulo + '\'' +
                ", totalTemporadas=" + totalTemporadas +
                ", avaliacao=" + avaliacao +
                ", escritor='" + escritor + '\'' +

                ", atores=" + atores +
                ", poster='" + poster + '\'' +
                ", sinopse='" + sinopse + '\'';

    }
}
