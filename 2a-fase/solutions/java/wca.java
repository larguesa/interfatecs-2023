import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class Competidor {
  int id;
  String nome;
}

class Participacao implements Comparable<Participacao> {
  Competidor competidor;
  TempoParticipacao[] tempos;
  TempoParticipacao media;
  TempoParticipacao minimo;

  Participacao(Competidor competidor, String tempo1, String tempo2, String tempo3, String tempo4, String tempo5) {
    this.competidor = competidor;
    this.tempos = new TempoParticipacao[5];
    this.tempos[0] = new TempoParticipacao(tempo1);
    this.tempos[1] = new TempoParticipacao(tempo2);
    this.tempos[2] = new TempoParticipacao(tempo3);
    this.tempos[3] = new TempoParticipacao(tempo4);
    this.tempos[4] = new TempoParticipacao(tempo5);
    Arrays.sort(tempos);
    int numDNFs = Arrays.stream(this.tempos).map(TempoParticipacao::retorne1SeDNF)
        .collect(Collectors.summingInt(Integer::intValue));
    if (numDNFs == 0) {
      this.minimo = tempos[0];
      this.media = new TempoParticipacao((tempos[1].totalEmMili + tempos[2].totalEmMili
          + tempos[3].totalEmMili) / 3);
    } else {
      this.minimo = tempos[0];
      if (numDNFs == 1) {
        this.media = new TempoParticipacao((tempos[1].totalEmMili + tempos[2].totalEmMili
            + tempos[3].totalEmMili) / 3);
      } else if (numDNFs >= 2) {
        this.media = new TempoParticipacao("0:00:000");
      }
    }
  }

  @Override
  public int compareTo(Participacao o) {
    if (media.totalEmMili == o.media.totalEmMili) {
      if (minimo.totalEmMili == o.minimo.totalEmMili) {
        return competidor.nome.compareTo(o.competidor.nome);
      }
      if (minimo.totalEmMili == 0) {
        return 1;
      } else if (o.minimo.totalEmMili == 0) {
        return -1;
      }
      return Long.compare(minimo.totalEmMili, o.minimo.totalEmMili);
    }
    if (media.totalEmMili == 0) {
      return 1;
    } else if (o.media.totalEmMili == 0) {
      return -1;
    }
    return Long.compare(media.totalEmMili, o.media.totalEmMili);
  }
}

class TempoParticipacao implements Comparable<TempoParticipacao> {
  String tempo;
  long totalEmMili;
  int minutos;
  int segundos;
  int milis;

  TempoParticipacao(String tempo) {
    this.tempo = tempo;
    this.totalEmMili = getTotalEmMili();
    String[] partes = tempo.split(":");
    this.minutos = Integer.valueOf(partes[0]);
    this.segundos = Integer.valueOf(partes[1]);
    this.milis = Integer.valueOf(partes[2]);
  }

  TempoParticipacao(long totalEmMili) {
    this.totalEmMili = totalEmMili;
    this.minutos = (int) (totalEmMili / 60000);
    this.segundos = (int) ((totalEmMili % 60000) / 1000);
    this.milis = (int) (totalEmMili % 1000);
    this.tempo = getComoEntrada();
  }

  @Override
  public int compareTo(TempoParticipacao o) {
    if (retorne1SeDNF() == 1) {
      return 1;
    } else if (o.retorne1SeDNF() == 1) {
      return -1;
    } else {
      return Long.compare(totalEmMili, o.totalEmMili);
    }
  }

  private long getTotalEmMili() {
    String[] partes = tempo.split(":");
    int minutos = Integer.valueOf(partes[0]);
    int segundos = Integer.valueOf(partes[1]);
    int mili = Integer.valueOf(partes[2]);
    return minutos * 60 * 1000 + segundos * 1000 + mili;
  }

  int retorne1SeDNF() {
    return tempo.equals("0:00:000") ? 1 : 0;
  }

  String getComoEntrada() {
    return minutos + ":" + String.format("%02d", segundos) + ":" + String.format("%03d", milis);
  }

  String getParaSaida() {
    if (minutos == 0 && segundos == 0 && milis == 0) {
      return String.format("%12s", "DNF");
    }
    String tempo = "";
    if (minutos > 0) {
      tempo += minutos + ":";
    }
    tempo += String.format("%02d", segundos) + ":" + String.format("%03d", milis);
    return String.format("%12s", tempo);
  }
}

class Prova {
  String nome;
  List<Participacao> participacoes;

  Prova(String nome) {
    this.nome = nome;
    participacoes = new ArrayList<>();
  }

  void addParticipacao(Participacao participacao) {
    participacoes.add(participacao);
  }

  void ordenar() {
    Collections.sort(participacoes);
  }

  void imprimirResultado() {
    System.out.println(nome);
    for (Participacao participacao : participacoes) {
      System.out.printf("%3s %-20s%12s%12s%n", participacao.competidor.id, participacao.competidor.nome,
          participacao.media.getParaSaida(), participacao.minimo.getParaSaida());
    }
  }
}

public class wca {

  public static void main(String[] args) throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    Map<Integer, Competidor> idsParaCompetidores;
    List<Prova> provas = new ArrayList<>(10);

    String linha;
    int numComp = Integer.valueOf(in.readLine());
    idsParaCompetidores = new HashMap<>(numComp);
    for (int i = 0; i < numComp; i++) {
      linha = in.readLine();
      int primeiroEspaco = linha.indexOf(" ");
      Competidor competidor = new Competidor();
      competidor.id = Integer.valueOf(linha.substring(0, primeiroEspaco));
      competidor.nome = linha.substring(primeiroEspaco + 1);
      idsParaCompetidores.put(competidor.id, competidor);
    }

    while (!(linha = in.readLine()).equals("FIM")) {
      int primeiroEspaco = linha.indexOf(" ");
      Prova prova = new Prova(linha.substring(primeiroEspaco + 1));

      int numParticipacoes = Integer.valueOf(linha.substring(0, primeiroEspaco));
      for (int i = 0; i < numParticipacoes; i++) {
        linha = in.readLine();
        String[] partes = linha.split(" ");
        Participacao participacao = new Participacao(idsParaCompetidores.get(Integer.valueOf(partes[0])), partes[1],
            partes[2], partes[3], partes[4], partes[5]);
        prova.addParticipacao(participacao);
      }

      provas.add(prova);
    }

    System.out.println(".Id.Nome.......................Media......Melhor");
    for (Prova prova : provas) {
      prova.ordenar();
      prova.imprimirResultado();
    }

  }
}
