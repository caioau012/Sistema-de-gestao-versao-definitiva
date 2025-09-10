package com.anhembimorumbiprojetos.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.anhembimorumbiprojetos.db.DB;
import com.anhembimorumbiprojetos.db.DbException;
import com.anhembimorumbiprojetos.model.entities.Equipe;
import com.anhembimorumbiprojetos.model.entities.Membro;

public class EquipeDao {
	
	private Connection conn;
	
	public EquipeDao() {
	}
	
	public EquipeDao(Connection conn) {
		if (conn == null) {
			throw new IllegalArgumentException("Conexão não pode ser nula");
		}
		this.conn = conn;
	}
	
	public void setConnection(Connection conn) {
		if (conn == null) {
			throw new IllegalArgumentException("Conexão não pode ser nula");
		}
		this.conn = conn;
	}
	
	private void verificarConexao() {
        if (conn == null) {
            throw new DbException("Conexão com o banco de dados não estabelecida");
        }	
	}
	
	public void adicionarEquipe (Equipe equipe) {
		verificarConexao();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO equipe (nome, descricao, dataCriacao) VALUES (?, ?, ?)" ,Statement.RETURN_GENERATED_KEYS);
			st.setString(1, equipe.getNome());
			st.setString(2,  equipe.getDescricao());
			st.setDate(3, Date.valueOf(equipe.getDataCriacao()));
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected >0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					equipe.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Erro inesperado! Nenhuma linha afetada!");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());	
		}
		finally {
			DB.closeStatement(st);
		}
	}
	
	public void atualizar(Equipe equipe) {
		verificarConexao();
		PreparedStatement st =  null;
		try {
			st = conn.prepareStatement("UPDATE equipe SET nome = ?, descricao = ?, dataCriacao = ? WHERE id = ?");
			st.setString(1,  equipe.getNome());
			st.setString(2,  equipe.getDescricao());
			st.setDate(3,  Date.valueOf(equipe.getDataCriacao()));
			st.setInt(4,  equipe.getId());
			
			st.executeUpdate();
		}
		catch(SQLException e) {
			throw new DbException (e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}
	
	public void excluir(int id) {
		verificarConexao();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM equipe WHERE id = ?");
			st.setInt(1, id);
			int rowsAffected = st.executeUpdate();
			if (rowsAffected == 0) {
				throw new DbException("Membro não encontrado");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}
	
	public Equipe buscarPorId(int id) {
		verificarConexao();
		 PreparedStatement st = null;
		 ResultSet rs = null;
		 try {
			 st = conn.prepareStatement("SELECT * FROM equipe WHERE id = ?");
			 st.setInt(1,  id);
			 rs = st.executeQuery();
			 if (rs.next()) {
				 Equipe equipe = instanciarEquipe(rs);
				 return equipe;
			 }
			 return null;
		 }
		 catch (SQLException e) {
			 throw new DbException (e.getMessage());
		 }
		 finally {
			 DB.closeStatement(st);
			 DB.closeResultSet(rs);
		 }
	}
	
	public List<Equipe> listarTodos(){
		verificarConexao();
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Equipe> equipes = new ArrayList<>();
		try {
			st = conn.prepareStatement("SELECT * FROM equipe ORDER BY nome");
			rs = st.executeQuery();
			
			while(rs.next()) {
				equipes.add(instanciarEquipe(rs));
			}
			
			return equipes;
		}
		catch(SQLException e) {
			throw new DbException (e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	public void adicionarMembro(int equipeId, Membro membro) {
		verificarConexao();
		PreparedStatement st = null;
		try {
			if(membro.getId() == 0) {
				MembroDao membroDao = new MembroDao(conn);
				membroDao.adicionarMembro(membro);
			}
			
			st = conn.prepareStatement("UPDATE membro SET equipeId = ? WHERE id = ?");
			st.setInt(1,  equipeId);
			st.setInt(2,  membro.getId());
			
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	} 
	
	public void salvarEquipeComMembros(Equipe equipe) {
		try {
			adicionarEquipe(equipe);
			
			for (Membro membro : equipe.getMembros()) {
				adicionarMembro(equipe.getId(), membro);
			}
		}
		catch(Exception e) {
			throw new DbException(e.getMessage());
		}
	}
	
	public void removerMembro(int equipeId, int membroId) {
		verificarConexao();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE membro SET equipeId = NULL WHERE id = ? AND equipeId = ?");
			st.setInt(1,  membroId);
			st.setInt(2, equipeId);
			
			int rowsAffected = st.executeUpdate();
			if (rowsAffected == 0) {
				throw new DbException("Membro não encontrado na equipe ou não existe");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}
	
	public List<Membro> listarMembrosPorEquipe(int equipeId){
		verificarConexao();
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Membro> membros = new ArrayList<>();
		
		try {
			st = conn.prepareStatement("SELECT * FROM membro WHERE equipeId = ? ORDER BY nome");
			st.setInt(1,  equipeId);
			rs = st.executeQuery();
			
			while(rs.next()) {
				Membro membro = instanciarMembro(rs);
				membros.add(membro);
			}
			return membros;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	private Membro instanciarMembro(ResultSet rs) throws SQLException{
		Membro membro = new Membro();
		membro.setId(rs.getInt("id"));
		membro.setNome(rs.getString("nome"));
		membro.setEmail(rs.getString("email"));
		 int equipeId = rs.getInt("equipeId");
	        if (!rs.wasNull()) {
	            membro.setEquipeId(equipeId);
	        } else {
	            membro.setEquipeId(0);
	        }
		return membro;
	}
	
	private Equipe instanciarEquipe(ResultSet rs) throws SQLException{
		Equipe equipe = new Equipe();
		equipe.setId(rs.getInt("id"));
		equipe.setNome(rs.getString("nome"));
		equipe.setDescricao(rs.getString("descricao"));
		java.sql.Date dataSql = rs.getDate("dataCriacao");
		if (dataSql != null) {
			equipe.setDataCriacao(dataSql.toLocalDate());
		}
		else {
			equipe.setDataCriacao(null);
		}
		int equipeId = equipe.getId();
		List<Membro> membros = listarMembrosPorEquipe(equipeId);
		equipe.setMembros(membros);
		
		return equipe;
	}
}
