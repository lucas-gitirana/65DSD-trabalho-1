package server.controller;

import server.model.Pessoa;

import java.util.List;

public abstract class CrudController<T, ID> {
    public abstract String handleMessage(String[] partes) throws Exception;
    public abstract T getById(ID id, boolean ignoreEmpty) throws Exception;
    public abstract void create(T obj) throws Exception;
    public abstract void update(T obj) throws Exception;
    public abstract void delete(ID id) throws Exception;
    public abstract List<T> listAll();
}
