package com.anhembimorumbiprojetos.model.entities;

import java.time.LocalDate;
import java.util.Objects;

public class Tarefa {
	
	private int id;
	private String titulo;
	private String descricao;
	private LocalDate dataInicio;
	private LocalDate dataTermino;
	private String status;
	private int projetoId;
	private int membroId;
	
	public Tarefa() {
	}

	public Tarefa(int id, String titulo, String descricao, LocalDate dataInicio, LocalDate dataTermino, String status,
			int projetoId, int membroId) {
		this.id = id;
		this.titulo = titulo;
		this.descricao = descricao;
		this.dataInicio = dataInicio;
		this.dataTermino = dataTermino;
		this.status = status;
		this.projetoId = projetoId;
		this.membroId = membroId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
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

	public int getProjetoId() {
		return projetoId;
	}

	public void setProjetoId(int projetoId) {
		this.projetoId = projetoId;
	}

	public int getMembroId() {
		return membroId;
	}

	public void setMembroId(int membroId) {
		this.membroId = membroId;
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
		Tarefa other = (Tarefa) obj;
		return id == other.id;
	}
}
