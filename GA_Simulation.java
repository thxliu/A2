import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Runs the simulation by initializing and ranking a population to create a subsequent generation
 */
public class GA_Simulation {
    private int n;
    private int k;
    private int r;
    private int c_0;
    private int c_max;
    private double m;
    private int g; 
    private ArrayList<Individual> population;
    private ArrayList<Individual> newPopulation;
    
    /**
     * Constructor to store the parameters in fields 
     * @param n number of individuals in the population 
     * @param k number of winners per generation
     * @param r number of evolution rounds to run 
     * @param c_0 initial chromosome size
     * @param c_max maximum chromosome size
     * @param m chance per round of a mutation in each gene 
     * @param g number of letters possible per gene 
     */
    public GA_Simulation(int n, int k, int r, int c_0, int c_max, double m, int g){
        this.n = n;
        this.k = k;
        this.r = r;
        this.c_0 = c_0;
        this.c_max = c_max;
        this.m = m;
        this.g = g;
        this.population = new ArrayList<Individual>();
        this.newPopulation = new ArrayList<Individual>();
    }

    /**
     * Initializes a population by calling the Individual constructor to make new Individuals
     */
    public void init(){
        for (int i = 0; i < n; i++){
            population.add(new Individual(c_0, g));
        }
    }

     /** Sorts population by fitness score, best first */
     public void rankPopulation(ArrayList<Individual> pop) {
        // sort population by fitness
        Comparator<Individual> ranker = new Comparator<>() {
          // this order will sort higher scores at the front
          public int compare(Individual c1, Individual c2) {
            return (int)Math.signum(c2.getFitness()-c1.getFitness());
          }
        };
        pop.sort(ranker); 
      }

    /**
     * Randomly selects two parents from the first k winners of the population to make each member of the next generation
     */
    public void evolve(){
        rankPopulation(population);
        ArrayList<Individual> winners = new ArrayList<Individual>();
        for (int i = 0; i < k; i++){
            winners.add(population.get(i));
        }

        for (int i = 0; i < n; i++){
            Individual parent1 = winners.get(ThreadLocalRandom.current().nextInt(k));
            Individual parent2 = winners.get(ThreadLocalRandom.current().nextInt(k));

            newPopulation.add(new Individual(parent1, parent2, c_max, m, g));
        }

        population = newPopulation;
    }

    /**
     * Prints statistics of the current generation including the top, kth, and last individual as well as the chromosome of the best individual
     */
    public void describeGeneration(){
        Individual topIndividual = population.get(0);
        Individual kthIndividual = population.get(k-1);
        Individual bottomIndividual = population.get(n-1);

        System.out.println("Top Individual: " + topIndividual.getFitness());
        System.out.println(k + "th Individual: " + kthIndividual.getFitness());
        System.out.println("Bottom Individual: " + bottomIndividual.getFitness());
        System.out.println("Top Individual Chromosome: " + topIndividual.chromosome.toString());
    }

    /**
     * Runs the simulation a a whole by initializing a population and ranking it then evolving the population and repeating for each round
     */
    public void run(){
        init();
        rankPopulation(population);
        describeGeneration();
        
        for (int i = 0; i < r; i++){
            evolve();
            rankPopulation(population);
            describeGeneration();
        }

    }

    public static void main(String[] args) {
        // int n = 100;
        // int k = 15;
        // int r = 100;
        // int c_0 = 8;
        // int c_max = 20;
        // double m = 0.01;
        // int g = 4;
        GA_Simulation simulation = new GA_Simulation(100, 15, 100, 8, 20, 0.01, 4);
        simulation.run();
    }

}
