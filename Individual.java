import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Holds details of an Individual competing to be the best and survive to reproduce
 */
public class Individual {
    ArrayList<Character> chromosome = new ArrayList<Character>();


    /** Choose a letter at random, in the range 
     *  from A to the number of letters indicated */
    private Character randomLetter(int num_dna_letters) {
        return Character.valueOf((char)(65+ThreadLocalRandom.current().nextInt(num_dna_letters)));
      }

    /** Expresses the individual's chromosome 
     *  as a String, for display purposes */
    public String toString() {
        StringBuilder builder = new StringBuilder(chromosome.size());
        for(Character ch: chromosome) {
          builder.append(ch);
        }
        return builder.toString();
        //return chromosome.stream().map(e->e.toString()).collect(Collectors.joining());
      }

    /**
     * Constructor to generate the initial population with a random chromosome sequence
     * @param c0 initial chromosome size
     * @param g number of letters possible per gene
     */
    public Individual(int c0, int g){
        for(int i = 0; i < c0; i++){
            chromosome.add(randomLetter(g));
        }
    }

    /**
     * Constructor to generate offspring from two parents for a new generation
     * @param parent1 random Individual from the initial or previous generation
     * @param parent2 random Individual from the initial or previous generation
     * @param c_max maximum chromosome size
     * @param m chance per round of a mutation in each gene
     * @param g number of letters possible in each gene
     */
    public Individual(Individual parent1, Individual parent2, int c_max, double m, int g){
        int prefixLength = ThreadLocalRandom.current().nextInt(parent1.chromosome.size()+1);
        for (int i = 0; i < prefixLength; i++){
            chromosome.add(parent1.chromosome.get(i));
        }

        int suffixLength =  ThreadLocalRandom.current().nextInt(parent2.chromosome.size()+1);
        for (int i = 0; i < suffixLength; i++){
            chromosome.add(parent2.chromosome.get(i));
        }

        while (chromosome.size() > c_max){
            chromosome.remove(chromosome.size()-1);
        }

        for (int i = 0; i < chromosome.size(); i++){
            if (ThreadLocalRandom.current().nextDouble() < m){
                chromosome.set(i, randomLetter(g));
            }
        }
    }

    /**
     * Calculates the fitness of an Individual based on whether each letter matches its mirror partner and whether each letter matches the preceding one 
     * @return the fitness score of the Individual 
     */
    public int getFitness(){
        int fitness = 0;
        for (int i = 0; i < chromosome.size(); i++){ 
            if (chromosome.get(i) == chromosome.get(chromosome.size()-i-1)){
                fitness += 1;
            }
            else{
                fitness -= 1;
            }
        }

        for (int i = 1; i < chromosome.size(); i++){
            if (chromosome.get(i) == chromosome.get(chromosome.size()-1)){
                fitness -= 1;
            }
        }
        return fitness;
    }
    
}
