package server.model;

public class Professor extends Pessoa {

    private String areaEstudo;

    public Professor(String cpf, String nome, String endereco, String areaEstudo) {
        super(cpf, nome, endereco);
        this.areaEstudo = areaEstudo;
    }

    public String getAreaEstudo() {
        return areaEstudo;
    }

    public void setAreaEstudo(String areaEstudo) {
        this.areaEstudo = areaEstudo;
    }

    @Override
    public String toString() {
        return "Professor{" +
                "cpf='" + getCpf() + '\'' +
                ", nome='" + getNome() + '\'' +
                ", endereco='" + getEndereco() + '\'' +
                ", areaEstudo='" + areaEstudo + '\'' +
                '}';
    }
}
