package com.anhembimorumbiprojetos.model.entities;

import java.util.Objects;

public class Membro {
	
	private int id;
	private String nome;
	private String email;
	private int equipeId;
	
	public Membro() {
	}

	public Membro(int id, String nome, String email, int equipeId) {
		this.id = id;
		this.nome = nome;
		this.email = email;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
		Membro other = (Membro) obj;
		return id == other.id;
	}

}
