package server.controller;

import server.model.Pessoa;

import java.util.ArrayList;

public class PessoaController {

    private ArrayList<Pessoa> pessoas = new ArrayList<>();

    public Pessoa getPessoa(String cpf) throws Exception {
        if (pessoas.isEmpty()) {
            throw new Exception("Sem pessoas cadastradas");
        }

        for (Pessoa p : pessoas) {
            if (p.getCpf().equalsIgnoreCase(cpf))
                return p;
        }
        throw new Exception("Pessoa não encontrada!");
    }

    public void updatePessoa(Pessoa newPessoa) throws Exception {
        Pessoa oldPessoa = getPessoa(newPessoa.getCpf());
        oldPessoa.setNome(newPessoa.getNome());
        oldPessoa.setEndereco(newPessoa.getEndereco());
    }

    public void createPessoa(Pessoa pessoa) {
        this.pessoas.add(pessoa);
    }
}
