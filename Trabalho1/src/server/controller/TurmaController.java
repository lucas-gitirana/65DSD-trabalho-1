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

            case "ADD_PESSOA":
                this.addPessoaTurma(partes[2], partes[3]);
                return "Pessoa adicionada na turma com sucesso";

            case "DELETE_PESSOA":
                this.deletePessoaTurma(partes[2], partes[3]);
                return "Pessoa removida da turma com sucesso";
        }

        return "";
    }

    private void addPessoaTurma(String cpf, String idTurma) throws Exception {
        Pessoa pessoa = pessoaController.getById(cpf, true);
        Turma turma = getById(idTurma, true);
        turma.getPessoas().add(pessoa);
    }

    private void deletePessoaTurma(String cpf, String idTurma) throws Exception {
        Pessoa pessoa = pessoaController.getById(cpf, true);
        Turma turma = getById(idTurma, true);
        turma.getPessoas().remove(pessoa);
    }

    public void deletePessoaFromAllTurmas(Pessoa pessoa){
        for(Turma turma : turmas){
            turma.getPessoas().remove(pessoa);
        }
    }

    @Override
    public Turma getById(String codigo, boolean ignoreEmpty) throws Exception {
        if (turmas.isEmpty() && !ignoreEmpty) {
            throw new Exception("Sem turmas cadastradas");
        }

        for (Turma t : turmas) {
            if (t.getCodigo().equals(codigo))
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
