package server.controller;

import server.model.Pessoa;
import server.model.Turma;
import java.util.ArrayList;
import java.util.List;

public class TurmaController extends CrudController<Turma, String>{

    private ArrayList<Turma> turmas = new ArrayList<>();
    private PessoaController pessoaController;

    public TurmaController(PessoaController pessoaController) {
        this.pessoaController = pessoaController;
    }

    @Override
    public String handleMessage(String[] partes) throws Exception {
        String operacao = partes[1];

        switch (operacao) {
            case "INSERT":
                this.create(new Turma(
                    (turmas.size() + 1),
                    partes[3],
                    Integer.parseInt(partes[4]))
                );
                return "Turma " + turmas.size() + " incluída com sucesso";
            case "UPDATE":
                this.update(new Turma(
                    Integer.parseInt(partes[2]),
                    partes[3],
                    Integer.parseInt(partes[4]))
                );
                return "Turma atualizada com sucesso";
            case "GET":
                return this.getById(partes[2], false).toString();
            case "ADD_PESSOA":
                this.addPessoaTurma(partes[2], partes[3]);
                return "Pessoa adicionada na turma com sucesso";
        }

        return "";
    }

    private void addPessoaTurma(String cpf, String idTurma) throws Exception {
        Pessoa pessoa = pessoaController.getById(cpf, true);
        Turma turma = getById(idTurma, true);
        turma.getPessoas().add(pessoa);
    }

    @Override
    public Turma getById(String id, boolean ignoreEmpty) throws Exception {
        if (turmas.isEmpty() && !ignoreEmpty) {
            throw new Exception("Sem turmas cadastradas");
        }

        for (Turma t : turmas) {
            if (t.getId() == Integer.parseInt(id))
                return t;
        }
        throw new Exception("Turma não encontrada!");
    }

    @Override
    public void create(Turma t) {
        this.turmas.add(t);
    }

    @Override
    public void update(Turma newTurma) throws Exception {
        Turma oldTurma = getById(String.valueOf(newTurma.getId()), true);
        oldTurma.setDisciplina(newTurma.getDisciplina());
        oldTurma.setQtdAlunos(newTurma.getQtdAlunos());
    }

    @Override
    public void delete(String s) throws Exception {

    }

    @Override
    public List<Turma> listAll() {
        return List.of();
    }
}
