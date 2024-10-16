package edu.ucf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Euler
{
    public static void main(String[] args) throws IOException {
        System.out.println("Welcome!");
        int[][] adjacencyMatrix = getInputFile();
        int startVertex = getStartVertex(adjacencyMatrix);

        List<Integer> circuit = fleury(adjacencyMatrix,startVertex);

        if (circuit != null)
            saveResults(circuit, startVertex);
        else
            System.out.println("No Eulerian circuit found");

        System.out.println("Euler circuit: " + circuit);

        System.out.println("\nGoodbye!");
    }

    public static boolean allTrue(boolean[] array)
    {
        for(boolean b : array) if(!b) return false;
        return true;
    }

    private static int[][] convertListToArray(List<int[]> list)
    {
        int[][] array = new int[list.size()][list.size()];
        for (int i = 0; i < list.size(); i++)
        {
            array[i] = list.get(i);
        }
        return array;
    }

    private static boolean containsEulerCircuit(int[][] G)
    {
        for (int[] vertex: G)
        {
            int vertexDegree = 0;
            for (int weight: vertex)
                if (weight != 0) vertexDegree++;

            if (vertexDegree % 2 != 0) return false;
        }
        return true;
    }

    private static int edgeCount(int[][] G)
    {
        int edgeCount = 0;
        for (int[] vertex: G)
        {
            for (int weight : vertex)
                if (weight != 0) edgeCount++;
        }
        
        return edgeCount / 2;
    }
    
    public static List<Integer> fleury(int[][] G, int startVertex)
    {
        if (!containsEulerCircuit(G)) return null;

        boolean[] visited = new boolean[G.length];
        visited[startVertex] = true;

        List<Integer> circuit = new LinkedList<>();
        circuit.add(startVertex);

        int[][] tempGraph = G.clone();
        int edgeCount = edgeCount(tempGraph);
        int x = startVertex;
        while (edgeCount > 0)
        {
            boolean hasVertex = false;
            int y = 0;
            do {
                if (tempGraph[x][y] != 0)
                {
                    int xWeight = tempGraph[x][y];
                    int yWeight = tempGraph[y][x];
                    tempGraph[x][y] = tempGraph[y][x] = 0;
                    if (validateWithPrims(tempGraph, y, visited))
                    {
                        circuit.add(y);
                        edgeCount--;
                        x = y;
                        visited[x] = true;
                        hasVertex = true;
                    }
                    else
                    {
                        tempGraph[x][y] = xWeight;
                        tempGraph[y][x] = yWeight;
                    }
                }
                y++;
            } while (!hasVertex && y < G.length);
        }

        return circuit;
    }

    private static int[][] getInputFile()
    {
        boolean hasInput = false;
        while(!hasInput)
        {
            try
            {
                System.out.print("Enter input filename(leave blank for default): ");
                Scanner keyb = new Scanner(System.in);
                String input = keyb.nextLine();
                if (input.isEmpty()) return readDefaultInput("Data.txt");
                else
                {
                    File f = new File(input);
                    if (f.exists() && !f.isDirectory())
                    {
                        hasInput = true;
                        return readInput(input);
                    } else System.out.println("File Not Found!");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return null;
    }

    private static int getStartVertex(int[][] adjacencyMatrix)
    {
        boolean hasInput = false;
        while (!hasInput)
        {
            try {
                System.out.print("\nEnter start vertex: ");
                Scanner keyb = new Scanner(System.in);
                int input = Integer.parseInt(keyb.nextLine());
                if (input >= 0 && input <= adjacencyMatrix.length - 1)
                {
                    hasInput = true;
                    return input;
                }
                else System.out.println("Vertex out of bounds");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public static boolean isValidEdge(int i, int j, boolean[] visited)
    {
        if (i == j || visited[i] == visited[j])
            return false;
        return true;
    }

    private static boolean[] primHelper(int[][] G, int startVertex) {
        boolean[] visited = new boolean[G.length];
        visited[startVertex] = true;

        int edgeCount = 0;
        while (edgeCount < G.length - 1)
        {
            int min = Integer.MAX_VALUE;
            int x = -1, y = -1;

            for (int i = 0; i < G.length; i++)
            {
                for (int j = 0; j < G.length; j++)
                {
                    if (!visited[j] && G[i][j] != 0)
                    {
                        if (min > G[i][j] && isValidEdge(i,j,visited))
                        {
                            min = G[i][j];
                            x = i;
                            y = j;
                        }
                    }
                }
            }

            if (x != -1 && y != -1)
                visited[y] = true;

            edgeCount++;
        }
        return visited;
    }

    public static int[][] readDefaultInput(String filename)
    {
        List<int[]> adjacencyList = new ArrayList<>();
        InputStream inputStream = Euler.class.getClassLoader().getResourceAsStream(filename);
        if (inputStream != null)
        {
            try (Scanner scanner = new Scanner(inputStream))
            {
                while (scanner.hasNextLine())
                {
                    String line = scanner.nextLine();
                    String[] lineParts = line.split("\\s+");
                    String[] strArray = lineParts[1].split(",");
                    int[] intArray = new int[strArray.length];
                    for(int i = 0; i < strArray.length; i++)
                    {
                        intArray[i] = Integer.parseInt(strArray[i]);
                    }
                    adjacencyList.add(intArray);
                }
            }
        } else System.out.println("Resource Not Found");

        return convertListToArray(adjacencyList);
    }

    private static int[][] readInput(String filename)
    {
        List<int[]> adjacencyList = new ArrayList<>();
        try
        {
            Scanner scanner = new Scanner(new File(filename));

            while (scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                String[] lineParts = line.split("\\s+");
                String[] strArray = lineParts[1].split(",");
                int[] intArray = new int[strArray.length];
                for(int i = 0; i < strArray.length; i++)
                {
                    intArray[i] = Integer.parseInt(strArray[i]);
                }
                adjacencyList.add(intArray);
            }

            scanner.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return convertListToArray(adjacencyList);
    }

    private static void saveResults(List<Integer> circuit, int startVertex) throws IOException {

        File jarFile = new File(Euler.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        File filepath = new File(jarFile.getParentFile(), startVertex+"-euler_circuit.txt");
        System.out.println("\n"+filepath);
        Path outputFilename = Path.of(filepath.toURI());
        try
        {
            String fileContents = "";
            for (int i = 0; i < circuit.size(); i++) {
                if (i == circuit.size() - 1) fileContents += circuit.get(i);
                else fileContents += circuit.get(i)+",";
            }
            Files.writeString(outputFilename,fileContents);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static boolean validateWithPrims(int[][] G, int startVertex, boolean[] visited)
    {
        boolean[] primResults = primHelper(G, startVertex);
        if (visited.length != primResults.length) return false;
        for (int i = 0; i < primResults.length; i++) {
            if (visited[i]) primResults[i] = visited[i];
        }
        return allTrue(primResults);
    }

}