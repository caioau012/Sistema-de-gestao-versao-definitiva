package com.anhembimorumbiprojetos.controller;

import java.util.List;

import com.anhembimorumbiprojetos.db.DB;
import com.anhembimorumbiprojetos.model.dao.ProjetoDao;
import com.anhembimorumbiprojetos.model.entities.Projeto;

public class ProjetoController {
	
	private ProjetoDao projetoDao;
	
	public ProjetoController() {
		this.projetoDao = new ProjetoDao(DB.getConnection());
	}
	
	public void adicionarProjeto(Projeto projeto) {
		try {
			projetoDao.adicionarProjeto(projeto);
			System.out.println("Projeto adicionado com sucesso: " + projeto.getNome());
		}
		catch (Exception e) {
			System.err.println("Erro ao adicionar projeto: " + e.getMessage());
			throw new RuntimeException("Falha ao adicionar projeto", e);
		}
	}
	
	public void atualizarProjeto(Projeto projeto) {
		try {
			projetoDao.atualizarProjeto(projeto);
			System.out.println("Projeto atualizado com sucesso: " + projeto.getNome());
		}
		catch(Exception e) {
			System.err.println("Erro ao atualizar projeto: " + e.getMessage());
			throw new RuntimeException("Falha ao atualizar projeto", e);
		}
	}
	
	public void removerProjeto(int id) {
		try {
			projetoDao.excluir(id);
			System.out.println("Projeto removido com sucesso. ID: " + id);
		}
		catch(Exception e) {
			System.err.println("Erro ao remover projeto: " + e.getMessage());
			throw new RuntimeException("Falha ao remover projeto", e);
		}
	}
	
	public Projeto obterProjeto(int id) {
		try {
			Projeto projeto = projetoDao.buscarPorId(id);
			if (projeto == null) {
				System.out.println("Projeto não encontrado. ID: " + id);
			}
			return projeto;
		}
		catch(Exception e) {
			System.err.println("Erro ao obter projeto: " + e.getMessage());
			throw new RuntimeException("Falha ao obter projeto",e);
		}
	}
	
	public List<Projeto> listarProjetos(){
		try {
			List<Projeto> projetos = projetoDao.listarTodos();
			System.out.println("Listando " + projetos.size() + " projetos");
			return projetos;
		}
		catch(Exception e) {
			System.err.println("Erro ao listar projetos: " + e.getMessage());
			throw new RuntimeException("Falha ao listar projetos", e);
		}
	}
	
	public List<Projeto> listarProjetosPorStatus(String status){
		try {
			List<Projeto> projetos = projetoDao.listarPorStatus(status);
			System.out.println("Listando " + projetos.size() + " projetos de status " + status);
			return projetos;
		}
		catch(Exception e) {
			System.err.println("Erro ao listar projetos: " + e.getMessage());
			throw new RuntimeException("Falha ao listar projetos", e);
		}
	}
}
