package server.controller;

public abstract class CrudController<T, ID> {
    public abstract String handleMessage(String[] partes) throws Exception;
    public abstract T getById(ID id, boolean ignoreEmpty, boolean ignoreNoFound) throws Exception;
    public abstract void create(T obj) throws Exception;
    public abstract void update(T obj) throws Exception;
    public abstract void delete(ID id) throws Exception;
    public abstract String listAll() throws Exception;

    public T getById(ID id, boolean ignoreEmpty) throws Exception {
        return getById(id, ignoreEmpty, false);
    }
}
