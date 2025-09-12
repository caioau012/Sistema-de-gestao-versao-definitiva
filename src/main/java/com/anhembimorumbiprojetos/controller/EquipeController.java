package com.anhembimorumbiprojetos.controller;

import java.util.List;

import com.anhembimorumbiprojetos.db.DB;
import com.anhembimorumbiprojetos.model.dao.EquipeDao;
import com.anhembimorumbiprojetos.model.entities.Equipe;
import com.anhembimorumbiprojetos.model.entities.Membro;

public class EquipeController {
	
	private EquipeDao equipeDao;

	public EquipeController(){
		this.equipeDao = new EquipeDao(DB.getConnection());
	}

	public void adicionarEquipe(Equipe equipe){
		try{
			equipeDao.adicionarEquipe(equipe);
			System.out.println("Equipe adicionada com sucesso: " + equipe.getNome());
		}
		catch (Exception e){
			System.err.println("Erro ao adicionar equipe: " + e.getMessage());
			throw new RuntimeException("Falha ao adicionar equipe", e);
		}
	}
	
	public void atualizarEquipe(Equipe equipe){
		try{
			equipeDao.atualizar(equipe);
			System.out.println("Equipe atualizada com sucesso: " + equipe.getNome());
		}
		catch (Exception e){
			System.err.println("Erro ao atualizar equipe: " + e.getMessage());
			throw new RuntimeException("Erro ao atualizar equipe", e);
		}
	}
	
	public void removerEquipe(int id){
		try{
			equipeDao.excluir(id);
			System.out.println("Equipe removida com sucesso. ID: " + id);
		}
		catch (Exception e){
			System.err.println("Erro ao remover equipe: " + e.getMessage());
			throw new RuntimeException("Erro ao excluir equipe", e);
		}
	}

	public Equipe obterEquipe(int id){
		try{
			Equipe equipe = equipeDao.buscarPorId(id);
			if (equipe == null){
				System.out.println("Equipe não encontrada. ID: " + id);
			}
			return equipe;
		}
		catch (Exception e){
			System.err.println("Erro ao obter equipe: " + e.getMessage());
			throw new RuntimeException("Falha ao obter equipe", e);
		}
	}
	
	public List<Equipe> listarEquipes(){
		try {
			List<Equipe> equipes = equipeDao.listarTodos();
			System.out.println("Listando " + equipes.size() + " equipes");
			return equipes;
		}
		catch(Exception e) {
			System.err.println("Erro ao listar equipes: " + e.getMessage());
			throw new RuntimeException("Falha ao listar equipes",e);
		}
	}
	
	public void adicionarMembro(int equipeId, Membro membro) {
		try {
			equipeDao.adicionarMembro(equipeId, membro);
			System.out.println("Adicionando membro: " + membro.getId());
		}
		catch (Exception e) {
			System.err.println("Erro ao adicionar membro: " + e.getMessage());
			throw new RuntimeException("Falha ao adicionar membro", e);
		}
	}
	
	public void removerMembro(int equipeId, int membroId) {
		try {
			equipeDao.removerMembro(equipeId, membroId);
			System.out.println("Removendo membro: " + membroId + " da equipe: " + equipeId);
		}
		catch(Exception e) {
			System.err.println("Erro ao remover membro: " + e.getMessage());
			throw new RuntimeException("Falha ao remover membro", e);
		}
	}
	
	public List<Membro> listarMembrosPorEquipe(int equipeId){
		try {
			List<Membro> membros = equipeDao.listarMembrosPorEquipe(equipeId);
			System.out.println("Listando " + membros.size() + " membros da equipe ID: " + equipeId);
			return membros;
		}
		catch(Exception e) {
			System.err.println("Erro ao listar membros por equipe: " + e.getMessage());
			throw new RuntimeException("Falha ao listar membros por equipe",e);
		}
	}
}
