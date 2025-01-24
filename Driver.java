import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Driver class.
 *
 * @author David Andrews
 * @version 1.0
 */
public class Driver extends TestRunner {
    /**
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Driver driver = new Driver();
        System.exit(driver.runTests() > 0 ? 1 : 0); // have to stop potential infinite loops
    }

    @Test
    private void testLineupConstructor() {
        Lineup<Integer> lineup = new Lineup<>(1, 2, 3, 4);

        Assert.assertEqual(getValue(lineup, "lineup"), Arrays.asList(1, 2, 3, 4),
                "Lineup constructor is incorrect");

        Lineup<Integer> invalidLineup1 = new Lineup<Integer>(null, 2, 3, 4);

        Assert.assertEqual(getValue(invalidLineup1, "lineup"), new ArrayList<>(),
                "Lineup constructor is incorrect");

        Lineup<Integer> invalidLineup2 = new Lineup<Integer>(1, null, 3, 4);

        Assert.assertEqual(getValue(invalidLineup2, "lineup"), new ArrayList<>(),
                "Lineup constructor is incorrect");

        Lineup<Integer> invalidLineup3 = new Lineup<Integer>(1, 2, null, 4);

        Assert.assertEqual(getValue(invalidLineup3, "lineup"), new ArrayList<>(),
                "Lineup constructor is incorrect");

        Lineup<Integer> invalidLineup4 = new Lineup<Integer>(1, 2, 3, null);

        Assert.assertEqual(getValue(invalidLineup4, "lineup"), new ArrayList<>(),
                "Lineup constructor is incorrect");
    }

    @Test
    private void testToString() {
        Lineup<Integer> lineup = new Lineup<>(1, 2, 3, 4);

        Assert.assertEqual(lineup.toString(), "1 -> 2 -> 3 -> 4", "Lineup toString() is incorrect");

        setValue(lineup, "lineup", new ArrayList<>(Arrays.asList(1, 2, 3)));

        Assert.assertEqual(lineup.toString(), "1 -> 2 -> 3 -> null",
                "Lineup toString() is incorrect");

        setValue(lineup, "lineup", new ArrayList<>(Arrays.asList(1, 2)));

        Assert.assertEqual(lineup.toString(), "1 -> 2 -> null -> null",
                "Lineup toString() is incorrect");

        setValue(lineup, "lineup", new ArrayList<>(Arrays.asList(1)));

        Assert.assertEqual(lineup.toString(), "1 -> null -> null -> null",
                "Lineup toString() is incorrect");

        setValue(lineup, "lineup", new ArrayList<>());

        Assert.assertEqual(lineup.toString(), "null -> null -> null -> null",
                "Lineup toString() is incorrect");
    }

    @Test
    private void testAdd() {
        Lineup<Integer> lineup = new Lineup<>(1, 2, 3, 4);

        Assert.assertTrue(!lineup.add(5), "Lineup add() is incorrect");

        Assert.assertTrue(!lineup.add(null), "Lineup add() is incorrect");

        Assert.assertEqual(getValue(lineup, "lineup"), Arrays.asList(1, 2, 3, 4),
                "Lineup add() is incorrect");

        // add ascending to the end

        setValue(lineup, "lineup", new ArrayList<>(Arrays.asList(1, 2, 3)));

        Assert.assertTrue(lineup.add(0), "Lineup add() is incorrect");
        Assert.assertEqual(getValue(lineup, "lineup"), Arrays.asList(0, 1, 2, 3),
                "Lineup add() is incorrect");

        // add ascending to the middle

        setValue(lineup, "lineup", new ArrayList<>(Arrays.asList(1, 3, 4)));

        Assert.assertTrue(lineup.add(2), "Lineup add() is incorrect");
        Assert.assertEqual(getValue(lineup, "lineup"), Arrays.asList(1, 2, 3, 4),
                "Lineup add() is incorrect");

        // add ascending to the beginning

        setValue(lineup, "lineup", new ArrayList<>(Arrays.asList(2, 3, 4)));

        Assert.assertTrue(lineup.add(1), "Lineup add() is incorrect");
        Assert.assertEqual(getValue(lineup, "lineup"), Arrays.asList(1, 2, 3, 4),
                "Lineup add() is incorrect");

        // add descending to the beginning

        setValue(lineup, "lineup", new ArrayList<>(Arrays.asList(3, 2, 1)));
        setValue(lineup, "isAscending", false);

        Assert.assertTrue(lineup.add(4), "Lineup add() is incorrect");
        Assert.assertEqual(getValue(lineup, "lineup"), Arrays.asList(4, 3, 2, 1),
                "Lineup add() is incorrect");

        // add descending to the end
        setValue(lineup, "lineup", new ArrayList<>(Arrays.asList(3, 2, 1)));

        Assert.assertTrue(lineup.add(0), "Lineup add() is incorrect");
        Assert.assertEqual(getValue(lineup, "lineup"), Arrays.asList(3, 2, 1, 0),
                "Lineup add() is incorrect");

        // add descending to the middle

        setValue(lineup, "lineup", new ArrayList<>(Arrays.asList(3, 1, 0)));

        Assert.assertTrue(lineup.add(2), "Lineup add() is incorrect");
        Assert.assertEqual(getValue(lineup, "lineup"), Arrays.asList(3, 2, 1, 0),
                "Lineup add() is incorrect");
    }

    @Test
    private void testRemove() {
        Lineup<String> lineup = new Lineup<>("a", "b", "b", "c");

        Assert.assertTrue(!lineup.remove("d"), "Lineup remove() is incorrect");
        Assert.assertTrue(!lineup.remove(null), "Lineup remove() is incorrect");
        Assert.assertEqual(getValue(lineup, "lineup"), Arrays.asList("a", "b", "b", "c"),
                "Lineup remove() is incorrect");

        Assert.assertTrue(lineup.remove("b"), "Lineup remove() is incorrect");
        Assert.assertEqual(getValue(lineup, "lineup"), Arrays.asList("a", "b", "c"),
                "Lineup remove() is incorrect");
    }

    @Test
    private void testReverseLineup() {
        Lineup<Integer> lineup = new Lineup<>(1, 2, 3, 4);

        lineup.reverseLineup();

        Assert.assertEqual(getValue(lineup, "lineup"), Arrays.asList(4, 3, 2, 1),
                "Lineup reverseLineup() is incorrect");
        Assert.assertEqual(getValue(lineup, "isAscending"), false,
                "Lineup reverseLineup() is incorrect");
    }

    @Test
    private void testContains() {
        Lineup<Integer> lineup = new Lineup<>(1, 2, 3, 4);

        Assert.assertTrue(lineup.contains(2), "Lineup contains() is incorrect");

        Assert.assertTrue(!lineup.contains(5), "Lineup contains() is incorrect");
    }

    @Test
    private void testSize() {
        Lineup<Integer> lineup = new Lineup<>(null, null, null, null);

        Assert.assertEqual(0, lineup.size(), "Lineup size() is incorrect");

        lineup.add(1);
        lineup.add(2);

        Assert.assertEqual(lineup.size(), 2, "Lineup size() is incorrect");

        lineup.add(3);
        lineup.add(4);

        Assert.assertEqual(lineup.size(), 4, "Lineup size() is incorrect");

        lineup.remove(3);

        Assert.assertEqual(lineup.size(), 3, "Lineup size() is incorrect");
    }
}


class TestRunner {
    public int runTests() {
        List<Method> tests = getTests();

        System.out.printf("Running %d test%s\n", tests.size(), tests.size() == 1 ? "" : "s");

        long startTime = System.currentTimeMillis();

        int passed = 0;
        int failed = 0;

        LinkedHashMap<Method, String> failures = new LinkedHashMap<>();

        for (Method test : tests) {
            System.out.printf("Running " + test.getName() + " ... ");
            String stdout = invokeTestMethod(test, 1000);
            if (stdout == null) {
                System.out.println("ok");
                passed++;
                continue;
            }
            System.out.println("FAILED");
            failures.put(test, stdout);
            failed++;
        }

        System.out.println();

        if (!failures.isEmpty()) {
            System.out.println("Failures:");

            System.out.println();

            for (Method test : failures.keySet()) {
                System.out.println(" ---- " + test.getName() + " ---- ");
                System.out.print(failures.get(test));
                System.out.println();
            }

            System.out.println("Failures:");
            for (Method test : failures.keySet()) {
                System.out.println("    " + test.getName());
            }

            System.out.println();
        }

        System.out.printf("Summary: %s. %d passed, %d failed. finished in %.2fs\n",
                failed == 0 ? "ok" : "FAILED", passed, failed,
                (System.currentTimeMillis() - startTime) / 1000.0);

        return failed;
    }

    private List<Method> getTests() {
        return Arrays.stream(this.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(Test.class))
                .collect(Collectors.toList());
    }

    private String invokeTestMethod(Method method, long timeoutInMillis) {
        method.setAccessible(true);
        final boolean[] failed = {false};
        final boolean[] timeoutOccurred = {false};

        Thread testThread = new Thread(() -> {
            try {
                method.invoke(this);
            } catch (Exception e) {
                e.getCause().printStackTrace();
                failed[0] = true;
            }
        });

        String stdout = captureConsole(() -> {
            testThread.start();
            try {
                testThread.join(timeoutInMillis);
                if (testThread.isAlive()) {
                    testThread.interrupt();
                    timeoutOccurred[0] = true;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                failed[0] = true;
            }
        });

        if (failed[0] || timeoutOccurred[0]) {
            return stdout + (timeoutOccurred[0]
                    ? "(Did not finish within the allowed time and may contain an infinite loop)\n"
                    : "");
        }
        return null;
    }

    // maybe just make captureConsole() and stopCapturingConsole() methods instead? lambdas are
    // annoying with scope and capturing
    protected static String captureConsole(Runnable r) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayOutputStream err = new ByteArrayOutputStream();

        PrintStream originalOut = System.out;
        PrintStream originalErr = System.err;

        System.setOut(new PrintStream(out));
        System.setErr(new PrintStream(err));

        r.run();

        System.setOut(originalOut);
        System.setErr(originalErr);

        return out.toString() + err.toString();
    }

    protected static <T> Field getField(Class<T> className, String fieldName) {
        try {
            Field field = className.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            System.err.println("You do not have a " + fieldName + " field in Olivia!");
            System.exit(1);
        }
        return null;
    }

    protected static Object getValue(Object obj, Field field) {
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            System.err.println("Cannot obtain access to the " + field.getName() + " field!");
            System.exit(1);
        }
        return null;
    }

    protected static void setValue(Object obj, Field field, Object value) {
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            System.err.println("Cannot obtain access to the " + field.getName() + " field!");
            System.exit(1);
        }
    }

    protected static Object getValue(Object obj, String fieldName) {
        return getValue(obj, getField(obj.getClass(), fieldName));
    }

    protected static void setValue(Object obj, String fieldName, Object value) {
        setValue(obj, getField(obj.getClass(), fieldName), value);
    }

    protected static Object getStaticValue(Field field) {
        try {
            return field.get(null);
        } catch (IllegalAccessException e) {
            System.err.println("Cannot obtain access to the " + field.getName() + " field!");
            System.exit(1);
        }
        return null;
    }

    protected static void setStaticValue(Field field, Object value) {
        try {
            field.set(null, value);
        } catch (IllegalAccessException e) {
            System.err.println("Cannot obtain access to the " + field.getName() + " field!");
            System.exit(1);
        }
    }

    protected static Object getStaticValue(Class<?> className, String fieldName) {
        return getStaticValue(getField(className, fieldName));
    }

    protected static void setStaticValue(Class<?> className, String fieldName, Object value) {
        setStaticValue(getField(className, fieldName), value);
    }
}


@Retention(RetentionPolicy.RUNTIME)
@interface Test {
}


class Assert {
    public static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    public static void assertEqual(Object actual, Object expected, String message) {
        if (expected == null && actual == null) {
            return;
        }

        if (expected == null || !expected.equals(actual)) {
            if (expected instanceof Double && actual instanceof Double
                    || expected instanceof Float && actual instanceof Float) {
                double expectedDouble = (double) expected;
                double actualDouble = (double) actual;
                if (Math.abs(expectedDouble - actualDouble) < 1e-6) {
                    return;
                }
            }
            throw new AssertionError(
                    String.format("%s\nExpected '%s' but got '%s'", message, expected, actual));
        }
    }

    public static void assertInstanceOf(Object obj, Class<?> className, String message) {
        if (obj == null || !className.isInstance(obj)) {
            throw new AssertionError(String.format("%s\nExpected instance of '%s' but got '%s'",
                    message, className.getName(), obj == null ? "null" : obj.getClass().getName()));
        }
    }
}