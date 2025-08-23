package server.model;

import java.util.ArrayList;

public class Turma {

    private int id;
    private String disciplina;
    private int qtdAlunos;
    private ArrayList<Pessoa> pessoas = new ArrayList<>();

    public Turma() {
    }

    public Turma(int id, String disciplina, int qtdAlunos) {
        this.id = id;
        this.disciplina = disciplina;
        this.qtdAlunos = qtdAlunos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
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
                ", disciplina='" + disciplina + '\'' +
                ", qtdAlunos=" + qtdAlunos +
                ", pessoas=" + pessoas +
                '}';
    }
}
