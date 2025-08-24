package server.model;

import java.util.ArrayList;

public class Turma {

    private int id;
    private String codigo;
    private int qtdAlunos;
    private ArrayList<Pessoa> pessoas = new ArrayList<>();

    public Turma() {
    }

    public Turma(int id, String codigo, int qtdAlunos) {
        this.id = id;
        this.codigo = codigo;
        this.qtdAlunos = qtdAlunos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public int getQtdAlunos() {
        return qtdAlunos;
    }

    public void setQtdAlunos(int qtdAlunos) {
        this.qtdAlunos = qtdAlunos;
    }

    public ArrayList<Pessoa> getPessoas() {
        return pessoas;
    }

    public void setPessoas(ArrayList<Pessoa> pessoas) {
        this.pessoas = pessoas;
    }

    @Override
    public String toString() {
        return "Turma{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", qtdAlunos=" + qtdAlunos +
                ", pessoas=" + pessoas +
                '}';
    }
}
