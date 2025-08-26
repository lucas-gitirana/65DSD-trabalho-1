package server.controller;

import server.model.Pessoa;

import java.util.ArrayList;
import java.util.List;

public class PessoaController extends CrudController<Pessoa, String>{

    private ArrayList<Pessoa> pessoas = new ArrayList<>();
    private TurmaController turmaController;

    @Override
    public String handleMessage(String[] partes) throws Exception {
        String operacao = partes[1];

        switch (operacao) {
            case "INSERT":
                this.create(new Pessoa(partes[2], partes[3], partes[4]));
                return "";

            case "UPDATE":
                this.update(new Pessoa(partes[2], partes[3], partes[4]));
                return "Pessoa atualizada com sucesso";

            case "GET":
                return this.getById(partes[2], false).toString();

            case "DELETE":
                this.delete(partes[2]);
                return "Pessoa removida com sucesso";

            case "LIST":
                return this.listAll();
        }

        return "";
    }

    @Override
    public Pessoa getById(String cpf, boolean ignoreEmpty) throws Exception {
        if (pessoas.isEmpty() && !ignoreEmpty) {
            throw new Exception("Sem pessoas cadastradas");
        }

        for (Pessoa p : pessoas) {
            if (p.getCpf().equalsIgnoreCase(cpf))
                return p;
        }

        throw new Exception("Pessoa não encontrada!");
    }

    @Override
    public void create(Pessoa pessoa) throws Exception {
        Pessoa existente = null;

        try {
            existente = getById(pessoa.getCpf(), true);
        } catch (Exception e) {
            this.pessoas.add(pessoa);
        }
    }

    @Override
    public void update(Pessoa newPessoa) throws Exception {
        Pessoa oldPessoa = getById(newPessoa.getCpf(), true);
        oldPessoa.setNome(newPessoa.getNome());
        oldPessoa.setEndereco(newPessoa.getEndereco());
    }

    @Override
    public void delete(String s) throws Exception {
        Pessoa pessoa = getById(s, false);
        if(turmaController != null){
            turmaController.deletePessoaFromAllTurmas(pessoa);
        }
        pessoas.remove(pessoa);
    }

    @Override
    public String listAll() throws Exception{
        if (pessoas.isEmpty()) {
            return "0";
        }

        return "0" + pessoas.size() +
                ", Pessoas: " + pessoas;
    }

    public void setTurmaController(TurmaController turmaController) {
        this.turmaController = turmaController;
    }
}
