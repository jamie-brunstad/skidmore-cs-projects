import java.util.List;
import java.security.cert.TrustAnchor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class SATsolvers {


//clauses.add(new Integer[] {(int)Math.random()*10,(int)Math.random()*10 , (int)Math.random()*10});

    public static void main(String[] args) {
        ArrayList<Integer[]> clauses = new ArrayList<Integer[]>();
        int numVars = 30;
        for (int i = 0; i < 100; i++) {
            Integer[] clause = new Integer[3];
            for (int j = 0; j < 3; j++) {
                clause[j] = ((int)(Math.random()*numVars) + 1) * ((int)(Math.random() * 2) == 1 ? 1 : -1);
                
            }
            clauses.add(clause);
        }
        //code for printing out clauses
        for (int i = 0; i < clauses.size(); i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(" "+clauses.get(i)[j]);
            }
            System.out.println();
        }
        //now clauses has 10 clauses of size 3 with random ints -10 ....-1 .... 1 .... 10
        double Seven8StartTime = System.nanoTime();
        int test78Sat = Seven8SatSolver(clauses, numVars);
        double Seven8EndTime = System.nanoTime();
        System.out.println("7/8 Approx. : "+test78Sat + " runtime (s): " + (Seven8EndTime-Seven8StartTime)/1000000000);
        double GSATStartTime = System.nanoTime();
        int testGSAT = GSATsolver(clauses, numVars);
        double GSATEndTime = System.nanoTime();
        System.out.println("GSAT: "+testGSAT + " runtime (s): " + (GSATEndTime-GSATStartTime)/1000000000);
        
       


        

        //put data into lists:
        String[] Seven8SATData = new String[2];
        Seven8SATData[0] = String.valueOf(test78Sat);
        Seven8SATData[1] = String.valueOf((Seven8EndTime-Seven8StartTime)/1000000000);

        String[] GSATData = new String[2];
        GSATData[0] = String.valueOf(testGSAT);
        GSATData[1] = String.valueOf((GSATEndTime-GSATStartTime)/1000000000);

        // String[] DPLLData = new String[2];
        // DPLLData[0] = String.valueOf(testDPLL);
        // DPLLData[1] = String.valueOf((DPLLEndTime - DPLLStartTime) /1000000000);


        try{
            FileWriter file = new FileWriter("SAT_output.csv", true);
            PrintWriter writer = new PrintWriter(file);
            writer.print(Seven8SATData[0]);
            writer.print(",");
            writer.print(Seven8SATData[1]);
            writer.print(",");
            writer.print(GSATData[0]);
            writer.print(",");
            writer.print(GSATData[1]);
            writer.print(",");
            // writer.print(DPLLData[0]);
            // writer.print(",");
            // writer.print(DPLLData[1]);
            writer.println();
            writer.close();
        } catch (IOException e){
            System.out.println("Error has occurred");
        }
        
    }
    public static int Seven8SatSolver(ArrayList<Integer[]> clauses, int number_of_variables){
        boolean[] truthtable = new boolean[number_of_variables];
        for (int i = 0; i < truthtable.length; i++) {
          truthtable[i] =  (int)Math.random()*2 == 1 ? true : false;
        }
        //now we have assigned our truth table for our clauses.
        
        return countSatisfiedClauses(clauses, truthtable);
    }

    public static int GSATsolver(ArrayList<Integer[]> clauses, int number_of_variables){
        boolean[] truthtable = new boolean[number_of_variables];
        for (int i = 0; i < truthtable.length; i++) {
          truthtable[i] =  (int)Math.random()*2 == 1 ? true : false;
        }
        //weve got ourselves a truth table!!
        int currentBest = 0;
        for (int i = 0; i < truthtable.length; i++) {
            truthtable[i] = !truthtable[i];
            //see how this performs
            int temp = countSatisfiedClauses(clauses, truthtable);
            //if it is better than current best, make it the best
            if(temp > currentBest){
                currentBest = temp;
            }
            //revert table back to normal before next iteration
            truthtable[i] = !truthtable[i];
        }
        return currentBest;

    }

   
    public static int countSatisfiedClauses(ArrayList<Integer[]> clauses, boolean[] truthAssignment){
        int numSatisfied = 0;
        for (int i = 0; i < clauses.size(); i++) {
            for (int j = 0; j < 3; j++) {
                if(truthAssignment[Math.abs(clauses.get(i)[j])-1]){
                    //this implies that xi is true and !xi is false
                    //check if we are dealing with a positive literal, not a negation
                    if(clauses.get(i)[j] > 0){
                        numSatisfied++;
                        break;
                    }
                }else{
                    //this implies that the xi is false and the !xi is true
                    if(clauses.get(i)[j] < 0){
                        numSatisfied++;
                        break;
                    }
                    
                }
                
            }
        }
        
        return numSatisfied;
    }
    
}
