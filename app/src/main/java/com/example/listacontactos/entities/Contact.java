package com.example.listacontactos.entities;


public class Contact  {
    public String nome;
    public String apelido;
    public int numero;
    public int idade;
    public String email;
    public String endereco;


    public Contact(String nome, String apelido, int numero, int idade, String email, String endereco){
        this.nome = nome;
        this.apelido = apelido;
        this.numero = numero;
        this.idade = idade;
        this.email = email;
        this.endereco = endereco;
    }


    // GETTERS
    public String getNome(){
        return nome;
    }

    public String getApelido(){
        return apelido;
    }

    public int getNumero(){
        return numero;
    }

    public int getIdade(){
        return idade;
    }

    public String getEmail(){
        return email;
    }

    public String getEndereco(){
        return endereco;
    }


    // SETTERS
    public void setNome(String nome){
        this.nome = nome;
    }
    public void setApelido(String apelido){
        this.apelido = apelido;
    }
    public void SetNumero(int numero){
        this.numero = numero;
    }
    public void SetIdade(int idade){
        this.idade = idade;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void SetEndereco(String endereco){
        this.endereco = endereco;
    }

}
