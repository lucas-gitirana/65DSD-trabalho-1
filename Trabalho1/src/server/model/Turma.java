package server.model;

import java.util.ArrayList;

public class Turma {

    private String codigo;
    private int qtdAlunos;
    private ArrayList<Pessoa> pessoas = new ArrayList<>();

    public Turma() {
    }

    public Turma(String codigo, int qtdAlunos) {
        this.codigo = codigo;
        this.qtdAlunos = qtdAlunos;
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
                ", codigo='" + codigo + '\'' +
                ", qtdAlunos=" + qtdAlunos +
                ", pessoas=" + pessoas +
                '}';
    }
}
