package tikape.harjoitus.database;

import java.sql.*;
import java.util.*;

public interface Dao<T, K> {

    T haeYksi(K key) throws SQLException;

    List<T> haeKaikki() throws SQLException;

    void tallenna(T object) throws SQLException;
    
    void poista(K key) throws SQLException;
}
