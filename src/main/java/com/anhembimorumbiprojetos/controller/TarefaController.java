package com.anhembimorumbiprojetos.controller;

import java.util.List;

import com.anhembimorumbiprojetos.model.dao.TarefaDao;
import com.anhembimorumbiprojetos.model.entities.Tarefa;

public class TarefaController {
	
	private TarefaDao tarefaDao;
	
	public TarefaController() {
		this.tarefaDao = new TarefaDao();
	}
	
	public void adicionarTarefa(Tarefa tarefa) {
		try {
			tarefaDao.adicionarTarefa(tarefa);
			System.out.println("Tarefa adicionada com sucesso: " + tarefa.getTitulo());
		}
		catch(Exception e) {
			System.err.println("Erro ao adicionar tarefa: " + e.getMessage());
			throw new RuntimeException("Falha ao adicionar tarefa", e);
		}
	}
	
	public void atualizarTarefa(Tarefa tarefa) {
		try {
			tarefaDao.atualizarTarefa(tarefa);
			System.out.println("Tarefa atualizada com sucesso: " + tarefa.getTitulo());
		}
		catch (Exception e) {
			System.err.println("Erro ao atualizar tarefa: " + e.getMessage());
			throw new RuntimeException("Falha ao atualizar tarefa", e);
		}
	}
	
	public void removerTarefa(int id) {
		try {
			tarefaDao.excluir(id);
			System.out.println("Tarefa removida com sucesso. ID: " + id);
		}
		catch(Exception e) {
			System.err.println("Erro ao remover tarefa: " + e.getMessage());
			throw new RuntimeException("Falha ao remover tarefa", e);
		}
	}
	
	public Tarefa obterTarefa(int id) {
		try {
			Tarefa tarefa = tarefaDao.buscarPorId(id);
			if(tarefa == null) {
				System.out.println("Tarefa não encontrada. ID: " + id);
			}
			return tarefa;
		}
		catch (Exception e) {
			System.err.println("Erro ao obter tarefa: " + e.getMessage());
			throw new RuntimeException("Falha ao obter tarefa", e);
		}
	}
	
	public List<Tarefa> listarTarefas(){
		try {
			List<Tarefa> tarefas = tarefaDao.listarTodos();
			System.out.println("Listando " + tarefas.size() + " tarefas");
			return tarefas;
		}
		catch (Exception e) {
			System.err.println("Erro ao listar tarefas: " + e.getMessage());
			throw new RuntimeException("Falha ao listar tarefas", e);
		}
	}
	
	public List<Tarefa> listarTarefasPorProjeto(int projetoId){
		try {
			List<Tarefa> tarefas = tarefaDao.listarPorProjeto(projetoId);
			System.out.println("Listando " + tarefas.size() + " tarefas");
			return tarefas;
		}
		catch(Exception e) {
			System.err.println("Erro ao listar tarefas: " + e.getMessage());
			throw new RuntimeException("Falha ao listar tarefas", e);
		}
	}
	
	public List<Tarefa> listarTarefasPorMembro(int membroId){
		try {
			List<Tarefa> tarefas = tarefaDao.listarPorMembro(membroId);
			System.out.println("Listando " + tarefas.size() + " tarefas");
			return tarefas;
		}
		catch(Exception e) {
			System.err.println("Erro ao listar tarefas: " + e.getMessage());
			throw new RuntimeException("Falha ao listar tarefas", e);
		}
	}
}
