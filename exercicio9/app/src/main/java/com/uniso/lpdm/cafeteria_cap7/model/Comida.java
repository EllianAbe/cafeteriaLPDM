package com.uniso.lpdm.cafeteria_cap7.model;

public class Comida {
    public String nome, descricao;

    public static final Comida[] comidas = {
            new Comida("Porcheta recheada",
                    "carne de porco assada com muito tempero, caldo de legumes e farofa."),
            new Comida("Aligot",
                    "Muita cremosidade com batatas amassadas e muito queijo."),
            new Comida("Sanduiche de carne louca",
                             "Pão recheado com carne desfiada, legumes, molho e cebola roxa.")
    };

    public Comida(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
