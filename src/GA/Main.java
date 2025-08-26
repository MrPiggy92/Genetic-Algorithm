package GA;

public class Main {
    char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', ' '};
    public static void main (String[] args) {
        Main main = new Main();
        String aim = "GENETIC ALGORITHMS ROCK";
        int gens = main.evolve(aim);
        System.out.print("It took ");
        System.out.print(gens);
        System.out.print(" generations to evolve ");
        System.out.println(aim);
    }
    private int evolve(String finish) {
        // Initialisation
        String[] candidates = new String[200];
        for (int i = 0; i < candidates.length; i ++) {
            candidates[i] = "";
            int end = (int)(Math.random() * 51);
            //end = 5;
            for (int j = 0; j < end; j ++) {
                String toAdd = String.valueOf(alphabet[(int)(Math.random()*27)]);
                if (Math.random() < 0.5) toAdd = toAdd.toUpperCase();
                candidates[i] += toAdd;
            }
        }
        int gen = 0;
        while (true) {
            // Gen0
            System.out.print("Gen");
            System.out.print(gen);
            //System.out.println();
            int[] scores = new int[candidates.length];
            for (int i = 0; i < candidates.length; i ++) {
                scores[i] = eval(candidates[i], finish);
            }
            //if (gen % 500 == 0) display(candidates, scores);
            boolean stop = false;
            for (String s : candidates) {
                if (s.equals(finish)) {
                    stop = true;
                    display(candidates, scores);
                }
            }
            if (stop) break;
            String[] elites = new String[5];
            candidates = select(candidates, scores, 0.5);
            for (int i = 0; i < 5; i ++) {
                elites[i] = candidates[i];
            }
            candidates = breed(candidates, 195, 5);
            for (int i = 195; i < 200; i ++) {
                candidates[i] = elites[i-195];
            }
            //for (String i : newCandidates) System.out.println(i);
            gen ++;
            System.out.println();
            /*try {
                Thread.sleep(50);
            } catch (InterruptedException e) {}*/
            
        }
        return gen;
    }
    private String[] select(String[] candidates, int[] scores, double proportion) {
        int num = (int) (proportion * candidates.length);
        String[] parents = new String[num];
        for (int i = 0; i < num; i ++) {
            int max = -50;
            int index = -1;
            for (int j = 0; j < scores.length; j ++) {
                if (scores[j] > max) {
                    max = scores[j];
                    index = j;
                }
            }
            parents[i] = candidates[index];
            scores[index] = -50;
        }
        return parents;
    }
    private String[] breed(String[] parents, int numChildren, int numElites) {
        String[] children = new String[numChildren+numElites];
        String p1 = null; String p2 = null;
        for (int i = 0; i < numChildren; i ++) {
            while (p1 == p2) {
                p1 = parents[(int)(Math.random()*parents.length)];
                p2 = parents[(int)(Math.random()*parents.length)];
            }
            children[i] = breed(p1, p2);
        }
        return children;
    }
    private String breed(String s1, String s2) {
        StringBuilder s3 = new StringBuilder();
        int maxLength = Math.max(s1.length(), s2.length());
        for (int i = 0; i < maxLength; i ++) {
            if (s1.length() > i && s2.length() > i) {
                s3.append(Math.random() < 0.5 ? String.valueOf(s1.charAt(i)) : String.valueOf(s2.charAt(i)));
            } else if (s1.length() > i) {
                s3.append(String.valueOf(s1.charAt(i)));
            } else if (s2.length() > i) {
                s3.append(String.valueOf(s2.charAt(i)));
            }
        }
        double mutationRate = 0.1;
        Integer[] insertPos = new Integer[s3.length()];
        Integer[] deletePos = new Integer[s3.length()];
        for (int i = 0; i < s3.length(); i ++) {
            if (Math.random() < mutationRate) {
                s3.setCharAt(i, Math.random() < 0.5 ? alphabet[(int)(Math.random()*27)] : String.valueOf(alphabet[(int)(Math.random()*27)]).toUpperCase().charAt(0));
            }
            if (Math.random() < mutationRate) {
                for (int j = 0; j < s3.length(); j ++) {
                    if (insertPos[j] == null) {
                        insertPos[j] = i;
                        break;
                    }
                }
            }
            if (Math.random() < mutationRate) {
                for (int j = 0; j < s3.length(); j ++) {
                    if (deletePos[j] == null) {
                        deletePos[j] = i;
                        break;
                    }
                }
            }
        }
        for (Integer index : insertPos) {
            if (index != null) s3.insert(index.intValue(), (char)alphabet[(int)(Math.random()*27)]);
        }
        for (Integer index : deletePos) {
            try {
                if (index != null) s3.deleteCharAt(index.intValue());
            } catch (StringIndexOutOfBoundsException e) {}
        }
        return s3.toString();
    }
    private void display(String[] candidates, int[] scores) {
        for (int i = 0; i < candidates.length; i ++) {
            System.out.print(candidates[i]);
            for (int j = 0; j < (55-candidates[i].length()); j ++) {
                System.out.print(" ");
            }
            System.out.print(scores[i]);
            System.out.println();
        }
    }
    private int eval(String candidate, String finish) {
        int m = candidate.length();
        int n = finish.length();

        // Create a 2D array to store the dynamic programming results
        int[][] dp = new int[m + 1][n + 1];

        // Initialize the base cases
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        // Fill in the DP array using the recurrence relation
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (candidate.charAt(i - 1) == finish.charAt(j - 1)) {
                    // Characters match, no operation needed
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // Characters don't match, consider the minimum of insert, remove, and replace
                    dp[i][j] = 1 + Math.min(
                            // Insert
                            dp[i][j - 1],
                            Math.min(
                                    // Remove
                                    dp[i - 1][j],
                                    // Replace
                                    dp[i - 1][j - 1]));
                }
            }
        }

        // Result is stored in the bottom-right cell of the DP array
        return -dp[m][n];
    }
}
