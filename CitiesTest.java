import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class CitiesTest {

    // Test for parseCityFile by writing a temporary file with sample input.
    @Test
    public void testParseCityFile(@TempDir Path tempDir) throws IOException {
        // Create a temporary file inside tempDir.
        Path tempFile = tempDir.resolve("cities.txt");
        try (BufferedWriter writer = Files.newBufferedWriter(tempFile)) {
            writer.write("Cleveland, Chicago\n");
            writer.write("Houston, Denver\n");
            writer.write("New York, Chicago\n");
        }

        List<String[]> pairs = Cities.parseCityFile(tempFile.toString());
        assertNotNull(pairs, "The list of pairs should not be null");
        assertEquals(3, pairs.size(), "There should be three pairs parsed");

        String[] firstPair = pairs.get(0);
        // Since BuildCityMap trims the strings, we compare trimmed values
        assertEquals("Cleveland", firstPair[0].trim(), "Expected 'Cleveland' as first element");
        assertEquals("Chicago", firstPair[1].trim(), "Expected 'Chicago' as second element");
    }

    // Test BuildCityMap with a small set of pairs (bidirectional mapping)
    @Test
    public void testBuildCityMap() {
        List<String[]> pairs = new ArrayList<>();
        pairs.add(new String[]{"A", "B"});
        pairs.add(new String[]{"B", "C"});
        pairs.add(new String[]{"C", "A"});

        Map<String, List<String>> cityMap = Cities.BuildCityMap(pairs);
        assertEquals(3, cityMap.size(), "There should be three keys in the map");

        // Check that each city is connected to the other two.
        assertTrue(cityMap.get("A").contains("B"), "'A' should be connected to 'B'");
        assertTrue(cityMap.get("A").contains("C"), "'A' should be connected to 'C'");
        assertTrue(cityMap.get("B").contains("A"), "'B' should be connected to 'A'");
        assertTrue(cityMap.get("B").contains("C"), "'B' should be connected to 'C'");
        assertTrue(cityMap.get("C").contains("A"), "'C' should be connected to 'A'");
        assertTrue(cityMap.get("C").contains("B"), "'C' should be connected to 'B'");
    }

    // Test direct connectivity using isTransitiveConnected.
    @Test
    public void testIsTransitiveConnectedDirect() {
        List<String[]> pairs = new ArrayList<>();
        pairs.add(new String[]{"New York", "Boston"});
        Map<String, List<String>> cityMap = Cities.BuildCityMap(pairs);
        // Direct connectivity should return true.
        assertTrue(Cities.isTransitiveConnected(cityMap, "New York", "Boston"),
                "New York and Boston are directly connected");
    }

    // Test transitive connectivity (via an intermediate city).
    @Test
    public void testIsTransitiveConnectedTransitive() {
        List<String[]> pairs = new ArrayList<>();
        pairs.add(new String[]{"New York", "Boston"});
        pairs.add(new String[]{"Boston", "Philadelphia"});
        pairs.add(new String[]{"Philadelphia", "Atlanta"});
        Map<String, List<String>> cityMap = Cities.BuildCityMap(pairs);
        // There is a path: New York -> Boston -> Philadelphia -> Atlanta.
        assertTrue(Cities.isTransitiveConnected(cityMap, "New York", "Atlanta"),
                "New York should be transitively connected to Atlanta");
    }

    // Test for cities that are not connected.
    @Test
    public void testIsTransitiveConnectedNotConnected() {
        List<String[]> pairs = new ArrayList<>();
        pairs.add(new String[]{"CityA", "CityB"});
        pairs.add(new String[]{"CityC", "CityD"});
        Map<String, List<String>> cityMap = Cities.BuildCityMap(pairs);
        // CityA and CityC are in separate connected components.
        assertFalse(Cities.isTransitiveConnected(cityMap, "CityA", "CityC"),
                "CityA and CityC should not be connected");
    }

    // Test the special case when the starting city is the same as the ending city.
    @Test
    public void testIsTransitiveConnectedSameCity() {
        List<String[]> pairs = new ArrayList<>();
        pairs.add(new String[]{"CityA", "CityB"});
        Map<String, List<String>> cityMap = Cities.BuildCityMap(pairs);
        // A city should be considered connected to itself.
        assertTrue(Cities.isTransitiveConnected(cityMap, "CityA", "CityA"),
                "A city should be connected to itself");
    }
}
