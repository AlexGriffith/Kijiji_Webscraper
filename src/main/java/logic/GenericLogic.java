package logic;

import dal.GenericDAL;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 *
 * @author Shariar (Shawn) Emami
 * @param <E> - entity type
 * @param <T> - DAL type
 */
public abstract class GenericLogic< E, T extends GenericDAL<E>>{
    
    private final T DAL;

    GenericLogic( T dal) {
        this.DAL = dal;
    }
    
    protected final T dal(){
        return DAL;
    }
    
    protected <R> R get( Supplier<R> supplier){
        R r;
        DAL.beginTransaction();
        r = supplier.get();
        DAL.closeTransaction();
        return r;
    }
    
    public void add( E entity){
        DAL.beginTransaction();
        DAL.save(entity);
        DAL.commitAndCloseTransaction();
    }
    
    public void delete( E entity){
        DAL.beginTransaction();
        DAL.delete(entity);
        DAL.commitAndCloseTransaction();
    }
    
    public void detach( E entity){
        DAL.beginTransaction();
        DAL.detach(entity);
        DAL.commitAndCloseTransaction();
    }
    
    public E update( E entity){
        DAL.beginTransaction();
        E e = DAL.update(entity);
        DAL.commitAndCloseTransaction();
        return e;
    }
    
    /**
     * this method is used to send a list of all names to be used form table
     * column headers. by having all names in one location there is less chance of mistakes.
     * 
     * this list must be in the same order as getColumnCodes and extractDataAsList
     * 
     * @return list of all column display names.
     */
    abstract public List<String> getColumnNames();
    
    /**
     * this method returns a list of column names that match the official column
     * names in the db. by having all names in one location there is less chance of mistakes.
     * 
     * this list must be in the same order as getColumnNames and extractDataAsList
     * 
     * @return list of all column names in DB.
     */
    abstract public List<String> getColumnCodes();
    
    /**
     * return the list of values of all columns (variables) in given entity.
     * 
     * this list must be in the same order as getColumnNames and getColumnCodes
     * 
     * @param e - given Entity to extract data from.
     * @return list of extracted values
     */
    abstract public List<?> extractDataAsList( E e);
    
    abstract public E createEntity(Map<String, String[]> requestData);
    
    abstract public List<E> getAll();
    
    abstract public E getWithId( int id);
    
    public List<E> search( String search){
        throw new UnsupportedOperationException("Method: search( String) not implemented");
    }
}
