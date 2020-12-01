package com.uniso.lpdm.cafeteria_cap7;

public class Localidade {
    public String nome, endereco;

    public static final Localidade[] localidades = {
            new Localidade("aqui",
                    "aqui mesmo"),
            new Localidade("la",
                    "não é aqui, mas é lá"),
            new Localidade("ali",
                    "não aqui, nem lá, mas é ali")
    };

    public Localidade(String nome, String endereco) {
        this.nome = nome;
        this.endereco = endereco;
    }

    @Override
    public String toString() {
        return nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
}
