package dao;

import entities.Laborer;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class LaborerDAO implements Dao<Laborer>{

    private EntityManager entityManager;

    public LaborerDAO(){
        this.entityManager = Persistence.createEntityManagerFactory("devtest").createEntityManager();
    }

    @Override
    public Optional<Laborer> get(int id) {
        return Optional.ofNullable(entityManager.find(Laborer.class, id));
    }

    @Override
    public List<Laborer> getAll() {
        TypedQuery<Laborer> query = entityManager.createQuery("SELECT a FROM Laborer a", Laborer.class);
        return query.getResultList();

    }

    @Override
    public void save(Laborer l) {
        Execute(em -> em.persist(l) );
    }

    @Override
    public void update(Laborer l, String[] params) {
        l.setTitle(Objects.requireNonNull(params[0], "Title cannot be null"));
        l.setItemCode(Objects.requireNonNull(params[1], "ItemCode cannot be null"));
        Execute(em -> em.merge(l) );
    }

    public void updateTotalCost(Laborer l, float value) {
        l.setTotalCost(value);
        Execute(em -> em.merge(l) );
    }
    @Override
    public void delete(Laborer l) {
        Execute(em -> em.remove(l));
    }

    public void updateTitle(Laborer l, String title) {
        l.setTitle(Objects.requireNonNull(title, "Title cannot be null"));
        Execute(em -> em.merge(l) );
    }
    public void updateItemCode(Laborer l, String itemCode) {
        l.setItemCode(Objects.requireNonNull(itemCode, "ItemCode cannot be null"));
        Execute(em -> em.merge(l) );
    }


    private void Execute(Consumer<EntityManager> action){
        try {
            entityManager.getTransaction().begin();
            action.accept(entityManager);
            entityManager.getTransaction().commit();
        }catch (Exception e){
            entityManager.getTransaction().rollback();
        }
    }

}
