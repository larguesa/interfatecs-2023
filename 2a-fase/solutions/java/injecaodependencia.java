import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class injecaodependencia {

  static Map<String, List<String>> adjVertices = new HashMap<>();
  static Set<String> visited = new HashSet<>();
  static Set<String> stack = new HashSet<>();

  public static void main(String[] args) throws IOException {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    int n = Integer.parseInt(br.readLine());

    for (int i = 0; i < n; i++) {
      String[] input = br.readLine().split(" ");
      String a = input[0];
      String b = input[1];

      adjVertices.putIfAbsent(a, new ArrayList<>());
      adjVertices.putIfAbsent(b, new ArrayList<>());
      adjVertices.get(a).add(b);
    }

    boolean hasCycle = false;
    for (String vertex : adjVertices.keySet()) {
      if (hasCycleUtil(vertex)) {
        hasCycle = true;
        break;
      }
    }

    System.out.println(hasCycle ? "usar injecao tardia" : "ok");
  }

  private static boolean hasCycleUtil(String vertex) {
    if (stack.contains(vertex))
      return true;
    if (visited.contains(vertex))
      return false;

    visited.add(vertex);
    stack.add(vertex);

    for (String neighbor : adjVertices.get(vertex)) {
      if (hasCycleUtil(neighbor)) {
        return true;
      }
    }

    stack.remove(vertex);
    return false;
  }
}
