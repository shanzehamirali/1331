public class Main {
    public static void main(String[] args) {
        Integer[] arr = {10, 1, 5, 7};
        System.out.println(findMin(arr));
    }

    public static <T> int findMin(T[] values) {
        T min = values[0];
        for (int i = 1; i < values.length; ++i) {
            if (values[i].compareTo(min) < 0) {
                min = values[i];
            }
        }
        return min;
    }
}