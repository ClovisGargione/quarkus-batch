package br.com.sourcesystems.model;

import java.time.Instant;

import org.apache.commons.csv.CSVRecord;

public class Registro {

    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    private Instant dataLeitura;

    public Registro() {}

    public Registro(String nome, String email, String telefone, String cpf) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.cpf = cpf;
    }

    public Registro(final CSVRecord record) {
        this(
            record.get("nome"),
            record.get("email"),
            record.get("telefone"),
            record.get("cpf")
        );
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Instant getDataLeitura() {
        return dataLeitura;
    }

    public void setDataLeitura(Instant dataLeitura) {
        this.dataLeitura = dataLeitura;
    }
}
