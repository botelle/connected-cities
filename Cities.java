import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Cities {
    public static final String CONNECTED = "CONNECTED";
    public static final String NOTCONNECTED = "NOT CONNECTED";
    public static void main (String... args) {
        if (args.length != 3) {
            System.out.println("Usage: java Cities starting city> <ending city> <input filename>");
            System.exit(1);
        }
        System.out.println("Starting city: " + args[0]);
        System.out.println("Ending city: " + args[1]);
        System.out.println("Input filename: " + args[2]);
        String startingCity = args[0];
        String endingCity = args[1];
        String inputFilename = args[2];

        Map<String, List<String>> cityNeighborMap = BuildCityMap(parseCityFile(inputFilename));
        if (startingCity.equals(endingCity)) {
            System.out.println(CONNECTED);
            System.exit(0);
        }
        if (isTransitiveConnected(cityNeighborMap, startingCity, endingCity)) {// .contains(endingCity)) {
            System.out.println(CONNECTED);
        }
        else {
            System.out.println(NOTCONNECTED);
        }




    }
    public static List<String[]> parseCityFile(String inputFilename) {
        Path filePath = Paths.get(inputFilename);

        try (Stream<String> lines = Files.lines(filePath)) {
            return lines
                    .map(line -> line.split(","))
                    .map(parts -> new String[]{parts[0], parts[1]})
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }
    public static boolean isTransitiveConnected(Map<String, List<String>> cityMap, String start, String end) {
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();

        // Start from the starting city.
        visited.add(start);
        queue.add(start);

        while (!queue.isEmpty()) {
            String currentCity = queue.poll();

            // If the destination is reached, cities are connected.
            if (currentCity.equals(end)) {
                return true;
            }

            // Retrieve neighbors, or an empty list if there are none.
            List<String> neighbors = cityMap.getOrDefault(currentCity, Collections.emptyList());
            for (String neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        return false;
    }
    public static Map<String, List<String>>  BuildCityMap(List<String[]> pairs) {
        Map<String, List<String>> cityMap = new HashMap<String, List<String>>();
        for (String[] pair : pairs) {
            String startingCity = pair[0].trim();
            String endingCity = pair[1].trim();
            cityMap.computeIfAbsent(startingCity, k -> new ArrayList<>()).add(endingCity);
            cityMap.computeIfAbsent(endingCity, k -> new ArrayList<>()).add(startingCity);
        }
        return cityMap;
    }
}
