package dao;

import entities.Activity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public class ActivityDAO implements Dao<Activity> {

    private EntityManager entityManager;

    public ActivityDAO(){
        this.entityManager = Persistence.createEntityManagerFactory("devtest").createEntityManager();
    }

    @Override
    public Optional<Activity> get(int id) {
        return Optional.ofNullable(entityManager.find(Activity.class, id));
    }

    @Override
    public List<Activity> getAll() {

        TypedQuery<Activity> query = entityManager.createQuery("SELECT a FROM Activity a",Activity.class);
        return query.getResultList();

    }

    @Override
    public void save(Activity a) {
        Execute(em -> em.persist(a) );
    }

    @Override
    public void update(Activity a, String[] params) {
        a.setTitle(Objects.requireNonNull(params[0], "Title cannot be null"));
        a.setItemCode(Objects.requireNonNull(params[1], "ItemCode cannot be null"));
        Execute(em -> em.merge(a) );
    }
    public void updateTitle(Activity a, String title) {
        a.setTitle(Objects.requireNonNull(title, "Title cannot be null"));
        Execute(em -> em.merge(a) );
    }
    public void updateItemCode(Activity a, String itemCode) {
        a.setItemCode(Objects.requireNonNull(itemCode, "ItemCode cannot be null"));
        Execute(em -> em.merge(a) );
    }

    public void updateTotalCost(Activity a) {
        a.setTotalCost();
        Execute(em -> em.merge(a) );
    }

    @Override
    public void delete(Activity a) {
        Execute(em -> em.remove(a));
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
