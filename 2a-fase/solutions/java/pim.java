import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class pim {

  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    int n = Integer.parseInt(br.readLine());

    StringBuilder sb = new StringBuilder();
    for (int i = 1; i <= n; i++) {
      if (i % 4 == 0) {
        sb.append("pim ");
      } else {
        sb.append(i).append(" ");
      }
    }

    // Remove o último espaço e imprime a sequência
    System.out.println(sb.toString().trim());
  }
}
