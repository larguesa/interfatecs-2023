import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

public class terminus {
  public static void main(String[] args) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    String line;
    String[] parts;

    Harbor harbor = new Harbor();
    while ((line = reader.readLine()) != null && !line.isEmpty()) {
      parts = line.split(" ");
      String ship = parts[0];
      int terminal = Integer.valueOf(parts[1]);
      long instant = Long.valueOf(parts[2]);

      harbor.addEvent(new Event(ship, terminal, instant));
    }
    harbor.processEvents();
    harbor.printResult();
  }
}

class Harbor {
  Terminal[] terminals;
  Map<String, Ship> idsToShips;
  List<Event> events;
  Queue<Ship> unifiedQueue = new PriorityQueue<>(INITIAL_EVENT_CAPACITY, (s1, s2) -> {
    if (s1.inQueueSince == s2.inQueueSince) {
      // System.out.println(s1.name + " " + s1.inQueueSince);
      // System.out.println(s2.name + " " + s2.inQueueSince);
      // throw new IllegalStateException("Two ships in queue for the same time");
      return -1;
    }
    return Long.compare(s1.inQueueSince, s2.inQueueSince);
  });

  static final int INITIAL_SHIP_CAPACITY = 1000;
  static final int INITIAL_EVENT_CAPACITY = 1000;
  static final int TERMINUS_NUM_TERMINALS = 7;

  public Harbor() {
    this.events = new ArrayList<>(INITIAL_EVENT_CAPACITY);
    this.idsToShips = new HashMap<>(INITIAL_SHIP_CAPACITY);
    this.terminals = new Terminal[TERMINUS_NUM_TERMINALS + 1];
    for (int i = 1; i <= TERMINUS_NUM_TERMINALS; i++) {
      terminals[i] = new Terminal(i);
    }
  }

  public void addEvent(Event event) {
    events.add(event);
  }

  public void printStatus(Event event) {
    System.out.println("====================================");
    System.out.println(event.shipId + " " + event.terminalNumber + " " + event.instant);
    System.out.println("-----");
    for (int i = 1; i < terminals.length; i++) {
      Terminal terminal = terminals[i];
      System.out.print(terminal.number + ": ");
      System.out.print((terminal.attending == null ? "        " : terminal.attending.name));
      System.out.print(" | ");
      System.out.println(terminal.queue.stream().map(it -> it.name).collect(Collectors.joining(", ")));
    }
    System.out.println("====================================");
  }

  public void processEvents() {
    Collections.sort(events);

    for (Event event : events) {
      if (event.terminalNumber == 0) {
        shipRequestedToLeave(event.shipId, event.instant);
      } else {
        shipRequestedAttendanceAt(event.shipId, event.terminalNumber, event.instant);
      }
      // printStatus(event);
    }
  }

  public void shipRequestedAttendanceAt(String shipId, int terminalNumber, long instant) {
    Ship ship = idsToShips.get(shipId);
    if (ship == null) {
      ship = new Ship(shipId, instant);
      idsToShips.put(shipId, ship);
    }

    if (terminalNumber == 0 || terminalNumber > 7) {
      throw new IllegalArgumentException("Invalid terminal number");
    }

    boolean attendNextFromAnyQueue = false;
    int terminalNumberToAttendNext = -1;

    if (ship.currentTerminal != null && ship.currentTerminal != terminals[terminalNumber]) {
      ship.changedTerminal = true;
    }

    if (ship.inQueue) {
      if (ship.currentTerminal == terminals[terminalNumber]) {
        // throw new IllegalStateException("Ship is already queued in the same
        // terminal");
      } else {
        unifiedQueue.remove(ship);
        ship.currentTerminal.removeFromQueue(ship);
      }
    } else if (ship.inAttendance) {
      if (ship.currentTerminal == terminals[terminalNumber]) {
        // throw new IllegalStateException("Ship is already been attended in the same
        // terminal");
      } else {
        if (ship.currentTerminal.hasQueue()) {
          unifiedQueue.remove(ship.currentTerminal.queue.peek());
          ship.currentTerminal.attendNext();
        } else {
          attendNextFromAnyQueue = true;
          terminalNumberToAttendNext = ship.currentTerminal.number;
        }
      }
    }

    if (terminals[terminalNumber].isFree()) {
      terminals[terminalNumber].attend(ship);
    } else {
      Terminal livre = getFirstAvailableTerminal();
      ship.changedTerminal = true;
      if (livre == null) {
        terminals[terminalNumber].queue(ship, instant);
        unifiedQueue.add(ship);
      } else {
        ship.waitedInQueue = true;
        livre.attend(ship);
      }
    }

    if (attendNextFromAnyQueue) {
      Ship nextShip = getNextShipToBeAttended();
      if (null != nextShip) {
        shipRequestedAttendanceAt(nextShip.name, terminalNumberToAttendNext, instant);
      }
    }
  }

  public void shipRequestedToLeave(String shipId, long instant) {
    Ship ship = idsToShips.get(shipId);
    if (ship == null) {
      throw new IllegalArgumentException("Ship not found");
    }

    if (ship.inAttendance) {
      ship.currentTerminal.attendNext();
    } else if (ship.inQueue) {
      unifiedQueue.remove(ship);
      ship.currentTerminal.removeFromQueue(ship);
    } else {
      throw new IllegalStateException("Ship is not in attendance nor in queue");
    }
    ship.markLeavingAt(instant);
  }

  public Terminal getFirstAvailableTerminal() {
    for (int i = 1; i < terminals.length; i++) {
      Terminal terminal = terminals[i];
      if (terminal.isFree()) {
        return terminal;
      }
    }
    return null;
  }

  public Ship getNextShipToBeAttended() {
    return unifiedQueue.poll();
  }

  public void printResult() {
    List<Ship> ships = idsToShips.values().stream().filter(it -> it.leftAt != -1).collect(Collectors.toList());
    Collections.sort(ships, (s1, s2) -> {
      if (s1.leftAt == s2.leftAt) {
        throw new IllegalStateException("Two ships left at the same time");
      }
      return Long.compare(s1.leftAt, s2.leftAt);
    });

    for (Ship ship : ships) {
      System.out.print(ship.name + " " + String.format(Locale.ROOT, "%.2f", ship.cost()));
      if (ship.waitedInQueue) {
        System.out.print(" E");
      }
      if (ship.changedTerminal) {
        System.out.print(" X");
      }
      System.out.println();
    }
  }

}

class Event implements Comparable<Event> {
  String shipId;
  int terminalNumber;
  long instant;

  public Event(String shipId, int terminalNumber, long instant) {
    this.shipId = shipId;
    this.terminalNumber = terminalNumber;
    this.instant = instant;
  }

  @Override
  public int compareTo(Event other) {
    return Long.compare(this.instant, other.instant);
  }
}

class Terminal {
  int number;
  Ship attending;
  Queue<Ship> queue;

  public Terminal(int number) {
    this.number = number;
    this.attending = null;
    this.queue = new LinkedList<>();
  }

  public boolean isFree() {
    return attending == null;
  }

  public boolean hasQueue() {
    return !queue.isEmpty();
  }

  public void attend(Ship ship) {
    if (!isFree()) {
      throw new IllegalStateException("Terminal is not free");
    }
    attending = ship;
    ship.markAttendanceAt(this);
  }

  public void attendNext() {
    if (!hasQueue()) {
      attending = null;
      return;
    }
    attending = queue.poll();
    attending.markAttendanceAt(this);

  }

  public void queue(Ship ship, long instant) {
    if (isFree()) {
      throw new IllegalStateException("Terminal is free");
    }
    queue.add(ship);
    ship.markQueuedAt(this, instant);
  }

  public void removeFromQueue(Ship ship) {
    boolean removed = queue.remove(ship);
    if (!removed) {
      throw new IllegalStateException("Ship is not in queue");
    }
  }

}

class Ship {
  String name;
  long arrivalTime;

  boolean inAttendance;

  boolean inQueue;

  long inQueueSince;

  Terminal currentTerminal;

  long leftAt = -1;

  boolean waitedInQueue;
  boolean changedTerminal;

  public Ship(String name, long arrivalTime) {
    this.name = name;
    this.arrivalTime = arrivalTime;
  }

  public void markAttendanceAt(Terminal terminal) {
    inAttendance = true;
    inQueue = false;
    currentTerminal = terminal;
    inQueueSince = -1;
  }

  public void markLeavingAt(long instant) {
    inAttendance = false;
    inQueue = false;
    inQueueSince = -1;
    currentTerminal = null;
    leftAt = instant;

  }

  public void markQueuedAt(Terminal terminal, long instant) {
    inQueue = true;
    inQueueSince = instant;
    currentTerminal = terminal;
    waitedInQueue = true;
  }

  public double cost() {
    long totalTime = leftAt - arrivalTime;
    double extra = 1;
    if (waitedInQueue) {
      extra += 0.05;
    }
    if (changedTerminal) {
      extra += 0.07;
    }
    return totalTime * extra / 123.457 * 4.59;
  }

}
