package boundary;

import entity.Category;
import entity.Ingredient;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Stateless
public class IngredientResource {

    @PersistenceContext
    EntityManager entityManager;

    @EJB
    CategoryResource catManager;

    // To feed the database
    boolean done = false;

    /**
     * Method that returns an ingredient for an id given
     * @param id
     * @return Ingredient
     */
    public Ingredient findById(String id) {
        return entityManager.find(Ingredient.class, id);
    }

    /**
     * Method that returns all the ingredients
     * @return List of Ingredient
     */
    public List<Ingredient> findAll(){
        Query query = entityManager.createNamedQuery("Ingredient.findAll", Ingredient.class);
        query.setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH); // Solve cache issues
        return query.getResultList();
    }

    /**
     * Method that returns ingredients for a name given
     * @param name of the ingredient we're looking for
     * @return List of Ingredient
     */
    public List<Ingredient> findByName(String name){
        Query query = entityManager.createQuery("SELECT i FROM Ingredient i where i.name = :name ");
        query.setParameter("name", name);
        query.setHint("javax.persistence.cache.storeMode", CacheStoreMode.REFRESH); // Solve cache issues
        return query.getResultList();
    }

    /**
     * Method that inserts an ingredient into the database
     * @param ingredient to add
     * @return the ingredient added
     */
    public Ingredient insert(Ingredient ingredient) {
        ingredient.setId(UUID.randomUUID().toString());
        return this.entityManager.merge(ingredient);
    }

    /**
     * Method that creates fake insertions into the database
     */
    public void feedCatalog(){
        if (!done) {
            Category category = catManager.insert(new Category("Salade"));

            this.insert(new Ingredient(category,"Laitue",1.00, "A salad with a french name"));
            this.insert(new Ingredient(category,"Roquette",1.00, "A salad with a french name"));
            this.insert(new Ingredient(category,"Mache",1.00, "A salad with a french name"));

            category = catManager.insert(new Category("Crudité"));

            this.insert(new Ingredient(category,"Carotte",1.50, "A carrot with a french name"));
            this.insert(new Ingredient(category,"Concombre",1.50, "A cucumber with a french name"));
            this.insert(new Ingredient(category,"Tomate",1.50, "A tomato with a french name"));

            category = catManager.insert(new Category("Charcuterie"));

            this.insert(new Ingredient(category,"Jambon",2.50, "Vegan people would hate you !"));
            this.insert(new Ingredient(category,"Jambon cru",2.50, "Vegan people would hate you !"));

            category = catManager.insert(new Category("Viande"));

            this.insert(new Ingredient(category,"Burger",3.00, "Vegan people would hate you !"));
            this.insert(new Ingredient(category,"Confit",3.00, "Vegan people would hate you !"));

            category = catManager.insert(new Category("Fromage"));

            this.insert(new Ingredient(category,"Emmental",1.50, "Is it swiss or french ?"));
            this.insert(new Ingredient(category,"Comté",1.50, "For strong people like you!"));

            category = catManager.insert(new Category("Sauce"));

            this.insert(new Ingredient(category,"Vinaigrette",0.50, "At first it was wine but something weird happened"));
            this.insert(new Ingredient(category,"Moutarde",0.50, "You don't choose mustard it's it that chooses you"));

            done = true;
        }
    }

}
