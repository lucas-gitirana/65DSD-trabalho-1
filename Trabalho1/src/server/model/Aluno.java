package server.model;

public class Aluno extends Pessoa {

    private String matricula;

    public Aluno(String cpf, String nome, String endereco, String matricula) {
        super(cpf, nome, endereco);
        this.matricula = matricula;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    @Override
    public String toString() {
        return "Aluno{" +
                "cpf='" + getCpf() + '\'' +
                ", nome='" + getNome() + '\'' +
                ", endereco='" + getEndereco() + '\'' +
                ", matricula='" + matricula + '\'' +
                '}';
    }
}
