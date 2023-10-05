import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class facildemais {

  public static void main(String[] args) throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    int n = Integer.parseInt(in.readLine().trim());

    int howManyOdds = 0;
    for (int i = 0; i < n; i++) {
      long position = Long.parseLong(in.readLine().trim());

      if (position > 1) {
        howManyOdds++;
      }
    }
    if (howManyOdds % 2 == 0) {
      System.out.println("par");
    } else {
      System.out.println("impar");
    }

  }
}
