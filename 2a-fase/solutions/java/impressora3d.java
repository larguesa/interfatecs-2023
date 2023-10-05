import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class impressora3d {

  public static void main(String[] args) throws IOException {

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    StringTokenizer st = new StringTokenizer(br.readLine());

    int n = Integer.parseInt(st.nextToken()); // largura da impressora
    int m = Integer.parseInt(st.nextToken()); // altura da impressora
    int q = Integer.parseInt(st.nextToken()); // número de comandos de impressão

    if (n < 0 || n > 10000000 || m < 0 || m > 100000 || q < 0 || q > 100000) {
      throw new IllegalArgumentException("Invalid input");
    }

    long[] impressora = new long[n + 2];

    for (int i = 0; i < q; i++) {
      st = new StringTokenizer(br.readLine());
      int a = Integer.parseInt(st.nextToken()); // início do intervalo de impressão
      int b = Integer.parseInt(st.nextToken()); // término do intervalo de impressão
      int c = Integer.parseInt(st.nextToken()); // quantidade de material

      impressora[a] += c;
      impressora[b + 1] -= c;
    }

    long alturaMaxima = 0;
    long alturaAtual = 0;
    for (int i = 1; i <= n; i++) {
      alturaAtual += impressora[i];
      alturaMaxima = Math.max(alturaMaxima, alturaAtual);
    }

    if (alturaMaxima > m) {
      System.out.println("invalida");
    } else {
      System.out.println(alturaMaxima);
    }

  }

}