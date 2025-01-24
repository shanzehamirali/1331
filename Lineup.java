import java.util.ArrayList;
import java.util.Collections;

public class Lineup<T extends Comparable<T>> {
    private ArrayList<T> lineup;
    private boolean isAscending;

    public Lineup(T first, T second, T third, T fourth) {
        lineup = new ArrayList<>(4);
        if (first != null && second != null && third != null && fourth != null) {
            lineup.add(first);
            lineup.add(second);
            lineup.add(third);
            lineup.add(fourth);
            isAscending = true;
        }
        else {
            lineup = new ArrayList<>();
            isAscending = true;
        }
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (T element : lineup) {
            sb.append(element).append(" -> ");
        }
        if (lineup.size() < 4) {
            sb.append("null");
        }
        return sb.toString().trim();
    }

    public boolean add(T element) {
        if (element == null || lineup.size() == 4) {
            return false;
        }
        for (int i = 0; i < lineup.size(); i++) {
            if (isAscending) {
                if (element.compareTo(lineup.get(i)) <= 0) {
                    lineup.add(i, element);
                    return true;
                }
            } else {
                if (element.compareTo(lineup.get(i)) >= 0) {
                    lineup.add(i, element);
                    return true;
                }
            }
        }
        lineup.add(element);
        return true;
    }

    public boolean remove(T element) {
        return lineup.remove(element);
    }

    public void reverseLineup() {
        Collections.reverse(lineup);
        isAscending = !isAscending;
    }

    public boolean contains(T element) {
        return lineup.contains(element);
    }

    public int size() {
        return lineup.size();
    }

    public static void main(String[] args) {
        Lineup<Integer> one = new Lineup<>(4, 2, 1, null);
        Lineup<String> two = new Lineup<>("Apple", "Apple", "Banana", null);

        System.out.println(one.toString());
        System.out.println(two.toString());

        one.add(3);
        System.out.println(one.toString());

        two.remove("Apple");
        System.out.println(two.toString());

        one.reverseLineup();
        System.out.println(one.toString());

        System.out.println(two.contains("Banana"));

        System.out.println(one.size());
        System.out.println(two.size());
    }
}
