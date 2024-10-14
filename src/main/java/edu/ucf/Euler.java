package edu.ucf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Euler
{
    public static void main(String[] args)
    {
        System.out.println("Welcome!");
        int[][] adjacencyMatrix = getInputFile();
        int[][] test = {{0,1,0,0},{1,0,0,0},{0,0,0,1},{0,0,1,0}} ;
        boolean b = Prim(test,3);
        System.out.println("Goodbye!");
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

    public static boolean isValidEdge(int i, int v,
                               boolean[] visited)
    {
        if (i == v)
            return false;
        if (visited[i] == false && visited[v] == false)
            return false;
        else if (visited[i] == true && visited[v] == true)
            return false;
        return true;
    }

    public static boolean Prim(int[][] G, int startVertex) {
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
                    // not in selected and there is an edge
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
            {
                System.out.println(x + " - " + y + " :  " + G[x][y]);
                visited[y] = true;
            }
            edgeCount++;
        }
        return allTrue(visited);
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

}