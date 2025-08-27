package server.controller;

import server.model.Aluno;
import server.model.Pessoa;

import java.util.ArrayList;

public class AlunoController extends CrudController<Aluno, String>{

    private ArrayList<Aluno> alunos = new ArrayList<>();
    private TurmaController turmaController;

    @Override
    public String handleMessage(String[] partes) throws Exception {
        String operacao = partes[1];

        switch (operacao) {
            case "INSERT":
                this.create(new Aluno(partes[2], partes[3], partes[4], partes[5]));
                return "";

            case "UPDATE":
                this.update(new Aluno(partes[2], partes[3], partes[4], partes[5]));
                return "Aluno atualizada com sucesso";

            case "GET":
                return this.getById(partes[2], false).toString();

            case "DELETE":
                this.delete(partes[2]);
                return "Aluno removida com sucesso";

            case "LIST":
                return this.listAll();
        }

        return "";
    }

    @Override
    public Aluno getById(String cpf, boolean ignoreEmpty, boolean ignoreNotFound) throws Exception {
        if (alunos.isEmpty() && !ignoreEmpty) {
            throw new Exception("Sem alunos cadastrados");
        }
        for (Aluno a : alunos) {
            if (a.getCpf().equalsIgnoreCase(cpf))
                return a;
        }
        if (!ignoreNotFound) {
            throw new Exception("Aluno não encontrado!");
        }
        return null;
    }

    @Override
    public void create(Aluno aluno) throws Exception {
        if (getById(aluno.getCpf(), true, true) != null) {
            throw new Exception("Já existe um aluno com esse CPF");
        }
        this.alunos.add(aluno);
    }

    @Override
    public void update(Aluno newAluno) throws Exception {
        Aluno oldAluno = getById(newAluno.getCpf(), true);
        oldAluno.setNome(newAluno.getNome());
        oldAluno.setEndereco(newAluno.getEndereco());
        oldAluno.setMatricula(newAluno.getMatricula());
    }

    @Override
    public void delete(String cpf) throws Exception {
        Aluno aluno = getById(cpf, false);
        if(turmaController != null){
            turmaController.deleteAlunoFromAllTurmas(aluno);
        }
        alunos.remove(aluno);
    }

    @Override
    public String listAll() throws Exception {
        if (alunos.isEmpty()) {
            return "0";
        }

        return "0" + alunos.size() +
                ", Alunos: " + alunos;
    }

    public void setTurmaController(TurmaController turmaController) {
        this.turmaController = turmaController;
    }
}
