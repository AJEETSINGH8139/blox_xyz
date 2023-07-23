import java.util.ArrayList;
import java.util.List;

class Point {
    int x;
    int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

public class WarehouseOptimization {
    // Scenario (a) - Strategic location based on median of x and y coordinates
    public static Point findStrategicLocation(List<Point> factories) {
        // Calculate median of x-coordinates
        int xMedian = factories.stream().mapToInt(p -> p.x).sorted().toArray()[factories.size() / 2];

        // Calculate median of y-coordinates
        int yMedian = factories.stream().mapToInt(p -> p.y).sorted().toArray()[factories.size() / 2];

        return new Point(xMedian, yMedian);
    }

    // Scenario (b) - Optimum solution using Dijkstra's algorithm
    // Assuming 0-based indexing for the grid cells
    public static int findOptimumSolution(int m, int[][] noTrespass, List<Point> factories) {
        int[][] grid = new int[m][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                grid[i][j] = 1; // Set all cells to be accessible by default
            }
        }

        for (int[] noTrespassCell : noTrespass) {
            int x = noTrespassCell[0];
            int y = noTrespassCell[1];
            grid[x][y] = -1; // Mark cells representing no trespass areas as inaccessible
        }

        int totalDistance = 0;

        for (Point factory : factories) {
            int distance = dijkstra(grid, m, factory);
            if (distance == Integer.MAX_VALUE) {
                // If there's no path to the factory, return -1 indicating infeasibility
                return -1;
            }
            totalDistance += distance;
        }

        return totalDistance;
    }

    private static int dijkstra(int[][] grid, int m, Point source) {
        int[][] dist = new int[m][m];
        boolean[][] visited = new boolean[m][m];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                dist[i][j] = Integer.MAX_VALUE;
            }
        }

        dist[source.x][source.y] = 0;

        for (int count = 0; count < m * m - 1; count++) {
            int minDist = Integer.MAX_VALUE;
            int x = -1, y = -1;

            for (int i = 0; i < m; i++) {
                for (int j = 0; j < m; j++) {
                    if (!visited[i][j] && dist[i][j] < minDist) {
                        minDist = dist[i][j];
                        x = i;
                        y = j;
                    }
                }
            }

            if (x == -1 || y == -1) {
                break;
            }

            visited[x][y] = true;

            // Relaxation step
            if (x > 0 && !visited[x - 1][y] && grid[x - 1][y] != -1 && dist[x][y] + grid[x - 1][y] < dist[x - 1][y]) {
                dist[x - 1][y] = dist[x][y] + grid[x - 1][y];
            }
            if (x < m - 1 && !visited[x + 1][y] && grid[x + 1][y] != -1
                    && dist[x][y] + grid[x + 1][y] < dist[x + 1][y]) {
                dist[x + 1][y] = dist[x][y] + grid[x + 1][y];
            }
            if (y > 0 && !visited[x][y - 1] && grid[x][y - 1] != -1 && dist[x][y] + grid[x][y - 1] < dist[x][y - 1]) {
                dist[x][y - 1] = dist[x][y] + grid[x][y - 1];
            }
            if (y < m - 1 && !visited[x][y + 1] && grid[x][y + 1] != -1
                    && dist[x][y] + grid[x][y + 1] < dist[x][y + 1]) {
                dist[x][y + 1] = dist[x][y] + grid[x][y + 1];
            }
        }

        return dist[source.x][source.y];
    }

    public static void main(String[] args) {
        // Example for scenario (a)
        List<Point> factories = new ArrayList<>();
        factories.add(new Point(1, 2));
        factories.add(new Point(3, 5));
        factories.add(new Point(7, 4));

        Point strategicLocation = findStrategicLocation(factories);
        System.out.println("Strategic Warehouse Location: (" + strategicLocation.x + ", " + strategicLocation.y + ")");

        // Example for scenario (b)
        int m = 5; // Grid size is 5x5
        int[][] noTrespass = { { 1, 2 }, { 2, 2 }, { 3, 3 } }; // Cells representing no trespass areas
        List<Point> factoriesGrid = new ArrayList<>();
        factoriesGrid.add(new Point(0, 1));
        factoriesGrid.add(new Point(4, 4));

        int totalDistance = findOptimumSolution(m, noTrespass, factoriesGrid);
        if (totalDistance != -1) {
            System.out.println("Total Distance Traveled: " + totalDistance);
        } else {
            System.out.println("No feasible path found.");
        }
    }
}
