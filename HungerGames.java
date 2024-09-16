package games;

import java.util.ArrayList;

/**
 * This class contains methods to represent the Hunger Games using BSTs.
 * Moves people from input files to districts, eliminates people from the game,
 * and determines a possible winner.
 * 
 * @author Pranay Roni
 * @author Maksims Kurjanovics Kravcenko
 * @author Kal Pandit
 */
public class HungerGames {

    private ArrayList<District> districts;  // all districts in Panem.
    private TreeNode            game;       // root of the BST. The BST contains districts that are still in the game.

    /**
     * ***** DO NOT REMOVE OR UPDATE this method *********
     * Default constructor, initializes a list of districts.
     */
    public HungerGames() {
        districts = new ArrayList<>();
        game = null;
        StdRandom.setSeed(2023);
    }

    /**
     * ***** DO NOT REMOVE OR UPDATE this method *********
     * Sets up Panem, the universe in which the Hunger Games takes place.
     * Reads districts and people from the input file.
     * 
     * @param filename will be provided by client to read from using StdIn
     */
    public void setupPanem(String filename) { 
        StdIn.setFile(filename);  // open the file - happens only once here
        setupDistricts(filename); 
        setupPeople(filename);
    }

    /**
     * Reads the following from input file:
     * - Number of districts
     * - District ID's (insert in order of insertion)
     * Insert districts into the districts ArrayList in order of appearance.
     * 
     * @param filename will be provided by client to read from using StdIn
     */
    public void setupDistricts (String filename) {

        // WRITE YOUR CODE HERE
        int numOfDistricts = StdIn.readInt(); ////read Number of districts from input file line 1

        for(int i = 0; i < numOfDistricts; i++){ //for lines 1-numOfDistricts

            District newDistrict = new District(StdIn.readInt()); //Create a new district with ID from file
            districts.add(i, newDistrict); //Add new district to districts ArrayList

        }
    }

    /**
     * Reads the following from input file (continues to read from the SAME input file as setupDistricts()):
     * Number of people
     * Space-separated: first name, last name, birth month (1-12), age, district id, effectiveness
     * Districts will be initialized to the instance variable districts
     * 
     * Persons will be added to corresponding district in districts defined by districtID
     * 
     * @param filename will be provided by client to read from using StdIn
     */
    public void setupPeople (String filename) {

        int numOfPeople = StdIn.readInt(); // number of people in file

        for(int i = 0; i < numOfPeople; i++){ //for lines 0-last person

            //initialize variables with data on people
            String fN = StdIn.readString();
            String lN = StdIn.readString();
            int bM = StdIn.readInt();
            int a = StdIn.readInt();
            int dID = StdIn.readInt();
            int e = StdIn.readInt();
            
            Person newPerson = new Person(bM, fN, lN, a, dID, e); //create a new Person object

            //if person is 12 or older or younger than 18, then set Tessera to true
            //these people are tributes!
            if(a >= 12 && a < 18){
                newPerson.setTessera(true);
            }
            
            for(int j = 0; j < districts.size(); j++){ //for 0 to end of district ArrayList

                //if person is born in an EVEN month AND the DistrictID matches the person's DistrictID
                if(bM % 2 == 0 && districts.get(j).getDistrictID() == dID){ 
                    districts.get(j).addEvenPerson(newPerson); //add person to even population
                } 

                //if person is born in an ODD month AND the DistrictID matches the person's DistrictID
                if(bM % 2 != 0 && districts.get(j).getDistrictID() == dID){
                    districts.get(j).addOddPerson(newPerson); //add person to even population
                }
            }

        }
    }

    /**
     * Adds a district to the game BST.
     * If the district is already added, do nothing
     * 
     * @param root        the TreeNode root which we access all the added districts
     * @param newDistrict the district we wish to add
     */
    public void addDistrictToGame(TreeNode root, District newDistrict) {        
        
        //set TreeNode game to TreeNode root
        TreeNode ptr = root; //set pointer to parameter root
        TreeNode prev = null; //previous will follow pointer

        //start a new tree if game is null
        if(game == null){
            game = new TreeNode(newDistrict, null, null); //set game to a new TreeNode with newDistrict
            ptr = game;
        }

        int dID = newDistrict.getDistrictID(); //dID holds the ID on the parameter newDistrict
        int ptrID = ptr.getDistrict().getDistrictID(); //ptrID holds the ID of the district from ptr

        TreeNode newTreeNode = new TreeNode(newDistrict, null, null); //new Treenode incase root is null

        //while root is not null
        while(ptr != null){
            ptrID = ptr.getDistrict().getDistrictID(); //update pointer ID

            if(dID == ptrID){ //if the ID of the parameter newDistrict is the same as the ID at ptr
                break;
            }
            
            if(dID < ptrID){ //if the ID of the parameter newDistrict is the less than ID at ptr
                prev = ptr; //move previous up to pointer
                ptr = ptr.getLeft(); //set ptr to the value left of it
            } else {
                prev = ptr; //move previous up to pointer
                ptr = ptr.getRight(); //else set ptr to the value right of it
            }
        }
        
        //exit while loop
        if(prev == null){ //if previous is null
            root = newTreeNode; //root is now newTreeNode with parameter newDistrict
        } else if(dID < ptrID){ //if the ID of parameter newDistrict is less than the ID at ptr
            prev.setLeft(newTreeNode); //set previous's left value to newTreeNode
        } else {
            prev.setRight(newTreeNode); //else set previous's right value to newTreeNode
        }

        districts.remove(newDistrict); //remove parameter newDistrict from ArrayList districts
    }

    /**
     * Searches for a district inside of the BST given the district id.
     * 
     * @param id the district to search
     * @return the district if found, null if not found
     */
     

     public District findDistrict(int id) {
        TreeNode ptr = game; //set ptr to game
        return helper(ptr, id); //use helper method to change ptr recursively
    }

    public District helper(TreeNode ptr, int id){
        if(ptr == null) { //if ptr is null
            return null; //not found
        }
        
        if(id == ptr.getDistrict().getDistrictID()) { //if id = ptr, return District at ptr
            return ptr.getDistrict();
        } else {
            if(id < ptr.getDistrict().getDistrictID()){ //if game ID is less than the parameter ID
                ptr = ptr.getLeft(); //ptr = value left of ptr on tree
                return helper(ptr, id); //recursive: run findDistrict on the value left of game
            } else {
                ptr = ptr.getRight(); //ptr = value right of ptr on tree
                return helper(ptr, id); //recursive: run findDistrict on the value right of game
            }
        }
    }

    /**
     * Selects two duelers from the tree, following these rules:
     * - One odd person and one even person should be in the pair.
     * - Dueler with Tessera (age 12-18, use tessera instance variable) must be
     * retrieved first.
     * - Find the first odd person and even person (separately) with Tessera if they
     * exist.
     * - If you can't find a person, use StdRandom.uniform(x) where x is the respective 
     * population size to obtain a dueler.
     * - Add odd person dueler to person1 of new DuelerPair and even person dueler to
     * person2.
     * - People from the same district cannot fight against each other.
     * 
     * @return the pair of dueler retrieved from this method.
     */
     private Person oddRandom(TreeNode temp, DuelPair returned){
        ArrayList<Person> people;
        people = temp.getDistrict().getOddPopulation();
        if(people.size() > 0){
            int random = StdRandom.uniform(temp.getDistrict().getOddPopulation().size());
            if(returned.getPerson2() != null && returned.getPerson2().getDistrictID() == people.get(random).getDistrictID()){
                return null;
            }
            returned.setPerson1(people.get(random));
            temp.getDistrict().getOddPopulation().remove(people.get(random));
        }

        return returned.getPerson1();
     }

     private Person evenRandom(TreeNode temp, DuelPair returned){
        
        ArrayList<Person> people;
        people = temp.getDistrict().getEvenPopulation();

        if(people.size() > 0){
            int random = StdRandom.uniform(temp.getDistrict().getEvenPopulation().size());
            if(returned.getPerson1() != null && returned.getPerson1().getDistrictID() == people.get(random).getDistrictID()){
                return null;
            }
            returned.setPerson2(people.get(random));
            temp.getDistrict().getEvenPopulation().remove(people.get(random));
        }

        return returned.getPerson2();
     }

     public void preOrder(TreeNode temp, int oddOrEven, DuelPair returned) {

        ArrayList<Person> people;

        //NULL
        if(temp == null){
            return;
        }

        //ODD
        if(oddOrEven == 1){
            people = temp.getDistrict().getOddPopulation();
            for(int i = 0; i < temp.getDistrict().getOddPopulation().size(); i++){
                if(temp.getDistrict().getOddPopulation().get(i).getTessera() == true && returned.getPerson1() == null){
                    returned.setPerson1(people.get(i));
                    temp.getDistrict().getOddPopulation().remove(people.get(i));
                } 
            }
        }

        //EVEN
        if(oddOrEven == 2){
            people = temp.getDistrict().getEvenPopulation();
            for(int i = 0; i < temp.getDistrict().getEvenPopulation().size(); i++){
                if(temp.getDistrict().getEvenPopulation().get(i).getTessera() == true && returned.getPerson2() == null) {
                    returned.setPerson2(people.get(i));
                    temp.getDistrict().getEvenPopulation().remove(people.get(i));
                }
            }
        }
        
        //ODD RANDOM
        if(oddOrEven == 3 && returned.getPerson1() == null) {
            oddRandom(temp, returned);
        }

        //EVEN RANDOM
        if((oddOrEven == 4) && returned.getPerson2() == null) {
            evenRandom(temp, returned);
            
        }

        preOrder(temp.getLeft(), oddOrEven, returned);

        preOrder(temp.getRight(), oddOrEven, returned);
     }

     public DuelPair selectDuelers() {
        
       DuelPair finalPair = new DuelPair();
       for(int i = 1; i <= 4; i++){
        preOrder(game, i, finalPair);
       }
       return finalPair; // update this line
    }


    /**
     * Deletes a district from the BST when they are eliminated from the game.
     * Districts are identified by id's.
     * If district does not exist, do nothing.
     * 
     * This is similar to the BST delete we have seen in class.
     * 
     * @param id the ID of the district to eliminate
     */
    
     private TreeNode min(TreeNode x){
        if(x.getLeft() == null){
            return x;
        } else {
            return min(x.getLeft());
        }
     }

     private TreeNode deleteMin(TreeNode x){
        if(x.getLeft() == null){
            return x.getRight();
        }
        x.setLeft(deleteMin(x.getLeft()));
        return x;
     }

     private TreeNode delete(TreeNode x, int key){
        if(x == null){
            return null;
        }

        if(key < x.getDistrict().getDistrictID()){
            x.setLeft(delete(x.getLeft(), key));
        } else if(key > x.getDistrict().getDistrictID()){
            x.setRight(delete(x.getRight(), key));
        } else {
            if(x.getLeft() == null){
                return x.getRight();
            } else if(x.getRight() == null){
                return x.getLeft();
            }
            
            TreeNode t = x;
            x = min(x.getRight());
            x.setRight(deleteMin(t.getRight()));
            x.setLeft(t.getLeft());
        }
        return x;
    }

     public void eliminateDistrict(int id) {

        // WRITE YOUR CODE HERE
        game = delete(game, id);
    }

    /**
     * Eliminates a dueler from a pair of duelers.
     * - Both duelers in the DuelPair argument given will duel
     * - Winner gets returned to their District
     * - Eliminate a District if it only contains a odd person population or even
     * person population
     * 
     * @param pair of persons to fight each other.
     */
    public void eliminateDueler(DuelPair pair) {

        // WRITE YOUR CODE HERE

        District person1District = findDistrict(pair.getPerson1().getDistrictID());
        District person2District = findDistrict(pair.getPerson2().getDistrictID());

        //Person 1 is NULL
        if(pair.getPerson1() == null){
                person2District.addOddPerson(pair.getPerson2());
        }

        //Person 2 is NULL
        if(pair.getPerson2() == null){
                person1District.addEvenPerson(pair.getPerson1());
        }

        //Both Person1 and Person2 exist
        if(pair.getPerson1() != null && pair.getPerson2() != null){
            Person winner  = pair.getPerson1().duel(pair.getPerson2());
            District winnerDistrict = findDistrict(winner.getDistrictID());

            if(winner.getBirthMonth() % 2 == 0){
                winnerDistrict.addEvenPerson(winner);
            }

            if(winner.getBirthMonth() % 2 != 0){
                winnerDistrict.addOddPerson(winner);
            }
        }

        if(person1District.getEvenPopulation().size() == 0 ||
            person1District.getOddPopulation().size() == 0){
                eliminateDistrict(person1District.getDistrictID());
        }
        
        if(person2District.getEvenPopulation().size() == 0 ||
            person2District.getOddPopulation().size() == 0){
                eliminateDistrict(person2District.getDistrictID());
        }
    }

    /**
     * ***** DO NOT REMOVE OR UPDATE this method *********
     * 
     * Obtains the list of districts for the Driver.
     * 
     * @return the ArrayList of districts for selection
     */
    public ArrayList<District> getDistricts() {
        return this.districts;
    }

    /**
     * ***** DO NOT REMOVE OR UPDATE this method *********
     * 
     * Returns the root of the BST
     */
    public TreeNode getRoot() {
        return game;
    }
}
