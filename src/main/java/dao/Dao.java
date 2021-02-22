package dao;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface Dao<T> {

    Optional<T> get(int id);

    List<T> getAll();

    void save(T t);

    void update(T t, String[] params);

    void delete(T t);

}