---
Homework Assignment 2: Genetic Algorithms
---
**Due: Tuesday, September 26, 2023, midnight**

Genetic algorithms are a family of techniques for optimizing solutions to complicated problems.  They are based on a form of artificial evolution:  individual strategies compete with each other based on a computed "fitness" score.  Each strategy is described by a chromosome, which is a sequence of individual genes. The most successful strategies in one generation are allowed to reproduce, creating a new population of strategies who continue the competition.  Repeated over multiple rounds, this approach can identify strategies with high fitness scores even when the fitness function itself is very complicated.

For purposes of this homework we will use a fitness function that isn't too hard to understand.  Nevertheless, by using a genetic algorithm we will gain experience with this method.  Since the individual strategies are described by a chromosome which is a sequence of genes, this will give us an opportunity to work with sequences -- specifically, the class `ArrayList`.  We've studied this some in lectures.  For further details, we encourage you to reference the [online javadoc](https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html) for the class.

# The Scenario

Each strategy in our scenario will have a chromosome consisting of a sequence of letters.  Their fitness will be evaluated using a function with two parts.  First, compare each letter to its mirror partner (i.e., the first with the last, second with the second-to-last, etc.).  Each match ***increases*** fitness by 1, and each mismatch ***decreases*** fitness by 1.  Second, compare each letter with the preceding one.  Each match ***decreases*** fitness by 1.

An experiment consists of the following steps:
1. Initialize the population with ***n*** individuals generated at random.
2. Rank them according to their fitness scores.
3. For each round of evolution:
    1. Determine the ***k*** winners of the previous round
    2. Generate a new generation from the old, where each new individual is the offspring of two randomly chosen winners from the previous round, with some randomly chosen mutations.  (See details below.)
    3. Rank the new generation according to their fitness score.
4. At the end, take the individual(s) with the best fitness as the solution.

## Parameters

An experiment of this form is characterized by a number of parameters:
* The number of individuals in the population, ***n*** (100)
* The number of winners in each generation, ***k*** (15)
* The number of evolution rounds to run, ***r*** (100)
* The initial chromosome size, ***c_0*** (8)
* The maximum chromosome size, ***c_max*** (20)
* Chance per round of a mutation in each gene, ***m*** (0.01)
* Number of letters possible per gene, ***g*** (4)

Suggested values for each are shown in parentheses.

# Coding Details

You will want to define two classes for this assignment.  `GA_Simulation` will hold methods that run the simulation as a whole.  `Individual` will hold details of an individual solution competing to be the best.  Because many of the methods in this class involve randomness, we will not require formal unit testing on this assignment.  You are still encouraged to find ways to ensure that your programs are computing results correctly.

## Class Individual

This class should hold its chromosome in an `ArrayList<Character>`.  It will need two constructors, one to generate initial population members with a random sequence of chromosomes, and one to generate offspring from two parents in subsequent generations.  It will also need a method to compute and return the fitness of the individual, according to the criteria decribed above.

The initial generator will allocate a sequence of the desired length, choosing each genetic letter randomly.  The version with two parents will pick a prefix of random length from the first parent, and a suffix of random length from the second, then concatenate them together.  If the result is longer than the maximum chromosome length, it truncates letters from the end.  For example, suppose the first parent has chromosome AABB and the second has chromosome CCDD.  The method will randomly pick one of A, AA, AAB, or AABB from the first parent, and D, DD, CDD, or CCDD from the second.  Let's say the random number generator picks 3 then 2.  The offspring chromosome would be AABDD.  As a final mutation step, for each letter in the offspring chromosome, replace it with a randomly chosen letter if a number randomly chosen between 0 and 1 is less than ***m***.  (Note that `ThreadLocalRandom.current().nextDouble()` will generate such a random number.)

To assist with the tasks above, we offer implementations of a few utility methods below.  For all random number generation, you will need to `import java.util.concurrent.ThreadLocalRandom;` at the top of your code.

    /** Choose a letter at random, in the range 
     *  from A to the number of letters indicated */
    private Character randomLetter(int num_dna_letters) {
      return Character.valueOf((char)(65+ThreadLocalRandom.current().nextInt(num_dna_letters)));
    }

The `toString` method assumes that the class has a field called `chromosome` holding the `ArrayList` of genes.

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

## Class GA_Simulation

This class should have a single constructor that takes all the experimental parameters listed above as arguments, and stores them in fields.  (You do not need to make getters or setters for them, since they are for internal use only.)  It will also need the following other methods:

* `init` initializes a population of the desired size, calling the `Individual` setup constructor for each one.
* `rankPopulation` should sort the population members according to their fitness level.  We're going to give you this one (see below), since we won't cover sorting until later in the course.  Here we rely on one of Java's builtin sort functions.
* `evolve` selects the winners (the first ***k*** elements of the population after ranking), and from them randomly selects two parents for each member of the next generation.  Create the new population using the second constructor for `Individual`.
* `describeGeneration` should print some statistics about the current generation.  Specifically, please show the fitness of the top individual in the generation, the ***k***th individual, and the least fit (last ranking) individual, plus the actual chromosome of the best individual.
* `run` will run the entire experiment.  First it will initialize the population, rank it, and describe it.  Then, for each round, it will evolve the population, rank it, and describe it.
* `main` will create a new `GA_Simulation` object and run it.

Because `GA_Simulation` will have a `main` method, you can run it to conduct the experiment.  Look at the output and play with the parameters.  How many rounds of evolution does the program take to generate a perfect palindrome?

Here is the code for the fitness sorter:

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

