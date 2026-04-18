import java.util.*;

public class Bah {

    static final int SIZE = 3; // 3x3 puzzle
    static final String GOAL = "123804765";
    // Goal state:
    // 1 2 3
    // 8 0 4
    // 7 6 5

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int option;
        boolean finished = false;

        String start = randomState();

        do{
            System.out.println();
            System.out.println("Initial state:");
            System.out.println();
            printState(start);

            System.out.println("Which search algorithm would you like to use?");
            System.out.println("1. Breadth-first search");
            System.out.println("2. Iterative-Depth-First Search");
            System.out.println("3. A* search");
            System.out.println("4. Randomize puzzle");
            System.out.println("5. Exit");
            System.out.println();

            option = sc.nextInt();
            if(option == 1){
                System.out.println();
                bfs(start);
            }
            else if(option == 2){
                System.out.println();
                System.out.println("Enter the maximum depth:");
                int maxDepth = sc.nextInt();
                System.out.println();
                idfs(start,maxDepth);
            }
            else if(option == 3){
                System.out.println();
                a_star(start);
            }
            else if(option == 4){
                start = randomState();
            }
            else if(option == 5){
                System.out.println();
                System.out.println("Goodbye.");
                finished = true;
            }
            else{
                System.out.println("Invalid option.");
            }
        }while(!finished);

    }

    static String randomState(){ // Generate a random puzzle state
        List<Character> tiles = new ArrayList<>(); // Create a list of tiles
        for(char x = '0'; x <= '8'; x++){  // Add numbers 0-8 to the list
            tiles.add(x);
        }

        Collections.shuffle(tiles); // Shuffle the list of numbers
        StringBuilder sb = new StringBuilder();
        for(char x : tiles){ // Convert the shuffled list to a string
            sb.append(x);
        }

        return sb.toString(); // Return the puzzle state
    }

    static void printState(String state){ // Print the puzzle state
        for(int i = 0; i < state.length(); i++){
            if(state.charAt(i) == '0'){ // Print a space if the tile is 0
                System.out.print("  ");
            }
            else{
                System.out.print(state.charAt(i) + " ");
            }
            if((i + 1) % SIZE == 0){ // Print a newline after every row
                System.out.println();
            }
        }
        System.out.println();
    }

    static String swap(String state, int x, int y){ // Swap two tiles in a puzzle state
        char[]arr = state.toCharArray(); // Convert the initial state string to an array of characters
        char temp = arr[x]; // Holds the value of the tile at position x
        arr[x] = arr[y]; // Swap the values of the tiles at positions x and y
        arr[y] = temp;
        return String.valueOf(arr); // Return the new state string
    }

    static List<String> getNeighbors(String state){ // Get the neighbors of a state
        List<String> neighbors = new ArrayList<>(); // Create a list to hold the neighbors

        int blank = state.indexOf('0'); // Get the position of the blank tile
        int row = blank / SIZE; // Calculate the row and column of the blank tile
        int col = blank % SIZE;

        int[][] directions = { // Directions that will be checked
                {-1, 0}, // Up
                {1, 0}, // Down
                {0, -1}, // Left
                {0, 1} // Right
        };

        for(int[] d : directions){ // Check each direction
            int newRow = row + d[0]; // Calculate the new row and column
            int newCol = col + d[1];

            if(newRow >= 0 && newRow < SIZE && newCol >= 0 && newCol < SIZE){ // Check if the new position is within the bounds of the puzzle
                int newPos = newRow * SIZE + newCol; // Calculate the string index equivalent of the new position
                neighbors.add(swap(state, blank, newPos)); // Swap the tiles and add the new state to the list
            }
        }
        return neighbors; // Return the list of neighbors
    }

    static void bfs(String start){ // Breadth-first search
        Queue<String> queue = new LinkedList<>(); // Create a queue to hold the states to be visited
        Set<String> visited = new HashSet<>(); // Create a set to hold visited states

        queue.add(start); // Add the initial state to the queue
        visited.add(start); // Add the initial state to the visited set

        while(!queue.isEmpty()){
            String current = queue.poll(); // Remove and return the first element of the queue

            printState(current);

            if(current.equals(GOAL)){ // Check if the goal state has been reached
                System.out.println("Goal reached!");
                return;
            }

            for(String neighbor : getNeighbors(current)){ // Go through each neighbor of the current state
                if(!visited.contains(neighbor)){ // Check if the neighbor has not been visited yet
                    visited.add(neighbor); // PRETTY SURE THIS PART IS WRONG, ADDING TO QUEUE DOESN'T EQUAL VISITING
                    queue.add(neighbor); // Add the neighbor to the queue
                }
            }
        }

        System.out.println("No solution was found.");
    }

    static boolean dls(String state, int remainingDepth, int currentDepth, Set<String> path){ // Depth-limited search
        System.out.println("Depth: "+currentDepth);
        printState(state);

        if(state.equals(GOAL)){ // Check if the goal state has been reached
            return true;
        }

        if(remainingDepth == 0){ // Check if the maximum depth has been reached
            return false;
        }

        path.add(state); // Add the current state to the path

        for(String neighbor : getNeighbors(state)){ // Go through each neighbor of the current state
            if(!path.contains(neighbor)){ // Check if the neighbor in the branch has been visited yet
                if(dls(neighbor, remainingDepth - 1, currentDepth + 1, path)){ // Check if the goal state has been reached as you go down the branch
                    return true;
                }
            }
        }

        path.remove(state); // If no goal is reached, remove the current state from the path so it can be visited again in a different branch
        return false;
    }

    static void idfs(String start, int maxDepth){ // Iterative deepening depth-first search
        for(int depth = 0; depth <= maxDepth; depth++){ // Check each depth from 0 to maxDepth
            Set<String> path = new HashSet<>();
            if(dls(start, depth, 0, path)){ // Check if the goal state has been reached at the current depth
                System.out.println("Goal reached at depth "+depth+"!");
                return; // Exit the loop if the goal state is found
            }
        }
        System.out.println("No solution was found.");
    }

    static int getDistance (String state){ // Calculate the Manhattan Distance between two states
        int distance = 0; // Initialize the distance variable
        for(int i = 0; i < state.length(); i++){ // Iterate through each tile in the state
            if(state.charAt(i) != '0'){ // Ignore the blank tile
                char tile = state.charAt(i); // Get the character value of the current tile
                int current_row = i / SIZE; // Calculate the row of the current tile
                int current_col = i % SIZE; // Calculate the column of the current tile
                int goal_row = Bah.GOAL.indexOf(tile) / SIZE; // Calculate the row of the goal tile
                int goal_col = Bah.GOAL.indexOf(tile) % SIZE; // Calculate the column of the goal tile
                distance += Math.abs(current_row - goal_row) + Math.abs(current_col - goal_col); // Calculate the distance between the current tile and the goal tile
            }
        }
        return distance; // Return the value of the distance variable
    }

    static void a_star (String start){ // A* search
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.f)); // Create a priority queue to hold the nodes sorted by the f(n) value
        Set<String> visited = new HashSet<>(); // Create a set to hold visited states

        Node startN = new Node(start, 0, getDistance(start)); // Create a node for the initial state
        pq.add(startN); // Add the initial node to the priority queue

        while(!pq.isEmpty()){ // Keep looping until the priority queue is empty
            Node current = pq.poll(); // Get the node with the lowest f(n) value

            printState(current.state); // Print the current state

            if(current.state.equals(GOAL)){ // Check if the goal state has been reached
                System.out.println("Goal reached!");
                return;
            }

            visited.add(current.state); // Add the current state to the visited set

            for(String neighbor : getNeighbors(current.state)){ // Go through each neighbor of the current state
                if(!visited.contains(neighbor)){ // Check if the neighbor has been visited
                    int g = current.g + 1; // Calculate the g(n) value
                    int h = getDistance(neighbor); // Calculate the h(n) value

                    Node next = new Node(neighbor, g, h); // Create a node for the neighbor
                    pq.add(next); // Add the neighbor to the priority queue
                }
            }
        }
        System.out.println("No solution found.");
    }
}
