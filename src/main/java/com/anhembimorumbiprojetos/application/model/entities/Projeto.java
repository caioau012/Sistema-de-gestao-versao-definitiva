package com.anhembimorumbiprojetos.model.entities;

import java.time.LocalDate;
import java.util.Objects;

public class Projeto {
	private int id;
	private String nome;
	private String descricao;
	private LocalDate dataInicio;
	private LocalDate dataTermino;
	private String status;
	private int equipeId;
	
	public Projeto() {
	}

	public Projeto(int id, String nome, String descricao, LocalDate dataInicio, LocalDate dataTermino, String status,
			int equipeId) {
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.dataInicio = dataInicio;
		this.dataTermino = dataTermino;
		this.status = status;
		this.equipeId = equipeId;
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

	public LocalDate getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(LocalDate dataInicio) {
		this.dataInicio = dataInicio;
	}

	public LocalDate getDataTermino() {
		return dataTermino;
	}

	public void setDataTermino(LocalDate dataTermino) {
		this.dataTermino = dataTermino;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getEquipeId() {
		return equipeId;
	}

	public void setEquipeId(int equipeId) {
		this.equipeId = equipeId;
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
		Projeto other = (Projeto) obj;
		return id == other.id;
	}
}
