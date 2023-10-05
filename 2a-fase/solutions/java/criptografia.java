import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

class User implements Comparable<User> {
  String name;
  String hash;

  public User(String name, String hash) {
    this.name = name;
    this.hash = hash;
  }

  @Override
  public int compareTo(User other) {
    return this.name.compareTo(other.name);
  }
}

public class criptografia {

  public static void main(String[] args) throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    List<User> users = new ArrayList<>();
    Map<String, String> passwordsToHashes = new HashMap<>();

    while (true) {
      String line = in.readLine();
      if (line.equals("ACABOU"))
        break;
      String[] parts = line.split(" ");
      String username = parts[0];
      String password = parts[1];
      String hash = passwordsToHashes.get(password);
      if (hash == null) {
        hash = calculateHash(password);
        passwordsToHashes.put(password, hash);
      }
      users.add(new User(username, hash));
    }

    Collections.sort(users);

    for (User user : users) {
      System.out.println("usuario...: " + user.name);
      System.out.println("valor hash: " + user.hash);
      System.out.println("------------------------------");
    }
  }

  public static String calculateHash(String password) {
    int s = 0;
    for (int i = 0; i < password.length(); i++) {
      s += (i + 1) * (int) password.charAt(i);
    }

    List<Integer> primeFactors = PrimeFactors.primeFactors(s);

    StringBuilder hash = new StringBuilder();
    hash.append(s);
    for (int factor : primeFactors) {
      hash.append(factor);
    }

    return hash.toString();
  }
}

class PrimeFactors {

  public static List<Integer> primeFactors(int number) {
    List<Integer> factors = new ArrayList<>();
    while (number % 2 == 0) {
      factors.add(2);
      number /= 2;
    }
    for (int i = 3; i <= Math.sqrt(number); i += 2) {
      while (number % i == 0) {
        factors.add(i);
        number /= i;
      }
    }
    if (number > 2) {
      factors.add(number);
    }
    return factors;
  }
}
