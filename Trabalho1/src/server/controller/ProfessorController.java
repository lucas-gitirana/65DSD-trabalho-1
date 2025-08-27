package server.controller;

import server.model.Professor;
import server.model.Pessoa;

import java.util.ArrayList;

public class ProfessorController extends CrudController<Professor, String>{

    private ArrayList<Professor> professores = new ArrayList<>();
    private TurmaController turmaController;

    @Override
    public String handleMessage(String[] partes) throws Exception {
        String operacao = partes[1];

        switch (operacao) {
            case "INSERT":
                this.create(new Professor(partes[2], partes[3], partes[4], partes[5]));
                return "";

            case "UPDATE":
                this.update(new Professor(partes[2], partes[3], partes[4], partes[5]));
                return "Professor atualizado com sucesso";

            case "GET":
                return this.getById(partes[2], false).toString();

            case "DELETE":
                this.delete(partes[2]);
                return "Professor removido com sucesso";

            case "LIST":
                return this.listAll();
        }

        return "";
    }

    @Override
    public Professor getById(String cpf, boolean ignoreEmpty, boolean ignoreNotFound) throws Exception {
        if (professores.isEmpty() && !ignoreEmpty) {
            throw new Exception("Sem professores cadastrados");
        }
        for (Professor p : professores) {
            if (p.getCpf().equalsIgnoreCase(cpf))
                return p;
        }
        if (!ignoreNotFound) {
            throw new Exception("Professor não encontrado!");
        }
        return null;
    }

    @Override
    public void create(Professor professor) throws Exception {
        if (getById(professor.getCpf(), true, true) != null) {
            throw new Exception("Já existe um professor com esse CPF");
        }
        this.professores.add(professor);
    }

    @Override
    public void update(Professor newProfessor) throws Exception {
        Professor oldProfessor = getById(newProfessor.getCpf(), true);
        oldProfessor.setNome(newProfessor.getNome());
        oldProfessor.setEndereco(newProfessor.getEndereco());
        oldProfessor.setAreaEstudo(newProfessor.getAreaEstudo());
    }

    @Override
    public void delete(String cpf) throws Exception {
        Professor professor = getById(cpf, false);
        if(turmaController != null){
            turmaController.deleteProfessorFromAllTurmas(professor);
        }
        professores.remove(professor);
    }

    @Override
    public String listAll() throws Exception {
        if (professores.isEmpty()) {
            return "0";
        }

        return "0" + professores.size() +
                ", Professores: " + professores;
    }

    public void setTurmaController(TurmaController turmaController) {
        this.turmaController = turmaController;
    }
}
