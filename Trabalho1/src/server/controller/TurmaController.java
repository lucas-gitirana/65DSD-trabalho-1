package server.controller;

import server.model.Aluno;
import server.model.Pessoa;
import server.model.Professor;
import server.model.Turma;
import java.util.ArrayList;

public class TurmaController extends CrudController<Turma, String>{

    private ArrayList<Turma> turmas = new ArrayList<>();

    private AlunoController alunoController;
    private ProfessorController professorController;

    public TurmaController(AlunoController alunoCtr, ProfessorController professorCtr) {
        this.alunoController = alunoCtr;
        this.professorController = professorCtr;
    }

    @Override
    public String handleMessage(String[] partes) throws Exception {
        String operacao = partes[1];

        switch (operacao) {
            case "INSERT":
                this.create(new Turma(
                    partes[3],
                    Integer.parseInt(partes[4]))
                );
                return "Turma " + turmas.size() + " incluída com sucesso";

            case "UPDATE":
                this.update(new Turma(
                    partes[3],
                    Integer.parseInt(partes[4]))
                );
                return "Turma atualizada com sucesso";

            case "GET":
                return this.getById(partes[2], false).toString();

            case "DELETE":
                this.delete(partes[2]);
                return "Turma removida com sucesso";

            case "LIST":
                return this.listAll();

            case "ADD_ALUNO":
                this.addAlunoTurma(partes[2], partes[3]);
                return "Aluno adicionado na turma com sucesso";

            case "DELETE_ALUNO":
                this.deleteAlunoTurma(partes[2], partes[3]);
                return "Aluno removido da turma com sucesso";

            case "ADD_PROFESSOR":
                this.addProfessorTurma(partes[2], partes[3]);
                return "Professor adicionado na turma com sucesso";

            case "DELETE_PROFESSOR":
                this.deleteProfessorTurma(partes[2], partes[3]);
                return "Professor removido da turma com sucesso";
        }

        return "";
    }

    private void addAlunoTurma(String cpf, String idTurma) throws Exception {
        Aluno aluno = alunoController.getById(cpf, true);
        Turma turma = getById(idTurma, true);
        turma.getAlunos().add(aluno);
    }

    private void deleteAlunoTurma(String cpf, String idTurma) throws Exception {
        Aluno aluno = alunoController.getById(cpf, true);
        Turma turma = getById(idTurma, true);
        turma.getAlunos().remove(aluno);
    }

    private void addProfessorTurma(String cpf, String idTurma) throws Exception {
        Professor professor = professorController.getById(cpf, true);
        Turma turma = getById(idTurma, true);
        turma.getProfessores().add(professor);
    }

    private void deleteProfessorTurma(String cpf, String idTurma) throws Exception {
        Professor professor = professorController.getById(cpf, true);
        Turma turma = getById(idTurma, true);
        turma.getProfessores().remove(professor);
    }

    public void deleteAlunoFromAllTurmas(Aluno aluno) {
        for (Turma turma : turmas){
            turma.getAlunos().remove(aluno);
        }
    }

    public void deleteProfessorFromAllTurmas(Professor professor) {
        for (Turma turma : turmas){
            turma.getProfessores().remove(professor);
        }
    }

    @Override
    public Turma getById(String codigo, boolean ignoreEmpty, boolean ignoreNotFound) throws Exception {
        if (turmas.isEmpty() && !ignoreEmpty) {
            throw new Exception("Sem turmas cadastradas");
        }

        for (Turma t : turmas) {
            if (t.getCodigo().equals(codigo))
                return t;
        }

        if (!ignoreNotFound) {
            throw new Exception("Turma não encontrada!");
        }

        return null;
    }

    @Override
    public void create(Turma t) throws Exception {
        if (getById(t.getCodigo(), true, true) != null) {
            throw new Exception("Já existe uma turma com esse código");
        }
        this.turmas.add(t);
    }

    @Override
    public void update(Turma newTurma) throws Exception {
        Turma oldTurma = getById(String.valueOf(newTurma.getCodigo()), true);
        oldTurma.setCodigo(newTurma.getCodigo());
        oldTurma.setQtdAlunos(newTurma.getQtdAlunos());
    }

    @Override
    public void delete(String s) throws Exception {
        turmas.remove(getById(s, false));
    }

    @Override
    public String listAll() {
        if (turmas.isEmpty()) {
            return "0";
        }

        return "0" + turmas.size() +
                ", Turmas: " + turmas;
    }
}
