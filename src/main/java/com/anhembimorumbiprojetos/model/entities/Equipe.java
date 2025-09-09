package com.anhembimorumbiprojetos.model.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Equipe {
	private int id;
	private String nome;
	private String descricao;
	private LocalDate dataCriacao;
	private List<Membro> membros = new ArrayList<>();
	
	public Equipe() {
	}

	public Equipe(int id, String nome, String descricao, LocalDate dataCriacao, List<Membro> membros) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.dataCriacao = dataCriacao;
		this.membros = membros;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public LocalDate getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDate dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public List<Membro> getMembros() {
		return membros;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Equipe other = (Equipe) obj;
		return id == other.id;
	}

	public void adicionarMembro(Membro membro) {
		membros.add(membro);
	}
	
	public void removerMembro(Membro membro) {
		membros.remove(membro);
	}
}
