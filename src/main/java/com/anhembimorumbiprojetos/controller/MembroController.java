package com.anhembimorumbiprojetos.controller;

import java.util.List;

import com.anhembimorumbiprojetos.model.dao.MembroDao;
import com.anhembimorumbiprojetos.model.entities.Membro;

public class MembroController {
		
	private MembroDao membroDao;
	
	public MembroController() {
		this.membroDao = new MembroDao();
	}
	
	public void adicionarMembro(Membro membro) {
		try {
			membroDao.adicionarMembro(membro);
			System.out.println("Membro adicionado com sucesso: " + membro.getNome());
		}
		catch (Exception e) {
			System.err.println("Erro ao adicionar membro: " + e.getMessage());
			throw new RuntimeException("Falha ao adicionar membro", e);
		}
				
	}
	
	public void atualizarMembro(Membro membro) {
		try {
			membroDao.atualizar(membro);
			System.out.println("Membro atualizado com sucesso: " + membro.getNome());
		}
		catch (Exception e) {
			System.err.println("Erro ao atualizar membro: " + e.getMessage());
			throw new RuntimeException("Falha ao atualizar membro", e);
		}
	}
	
	public void removerMembro(int id) {
		try {
			membroDao.excluir(id);
			System.out.println("Membro removido com sucesso. ID: " + id);
		}
		catch (Exception e) {
			System.err.println("Erro ao remover membro: " + e.getMessage());
			throw new RuntimeException("Falha ao remover membro", e);
		}
	}
	
	public Membro obterMembro(int id) {
		try {
			Membro membro = membroDao.buscarPorId(id);
			if (membro == null) {
				System.out.println("Membro não encontrado. ID: " + id );
			}
			return membro;
		}
		catch(Exception e) {
			System.err.println("Erro ao obter membro: " + e.getMessage());
			throw new RuntimeException("Falha ao obter membro", e);
		}
	}
	
	public List<Membro> listarMembros(){
		try {
			List<Membro> membros = membroDao.listarTodos();
			System.out.println("Listando " + membros.size() + " membros");
			return membros;
		}
		catch (Exception e) {
			System.err.println("Erro ao listar membros: " + e.getMessage());
			throw new RuntimeException("Falha ao listar membros", e);
		}
	}
	
	public List<Membro> listarMembrosPorEquipe(int equipeId){
		try {
			List<Membro> membros = membroDao.listarPorEquipe(equipeId);
			System.out.println("Listando " + membros.size() + " membros da equipe ID: " + equipeId);
			return membros;
		}
		catch (Exception e) {
			System.err.println("Erro ao listar membros por equipe: " + e.getMessage());
			throw new RuntimeException("Falha ao listar membros por equipe",e);
		}
	}
	
	public boolean validarMembro(Membro membro) {
		if (membro.getNome() == null || membro.getNome().trim().isEmpty()) {
            System.err.println("Nome do membro é obrigatório");
            return false;
        }
        
        if (membro.getEmail() == null || membro.getEmail().trim().isEmpty()) {
            System.err.println("Email do membro é obrigatório");
            return false;
        }
        
        if (!membro.getEmail().contains("@")) {
            System.err.println("Email inválido");
            return false;
        }
        
        return true;
    }
	
}
