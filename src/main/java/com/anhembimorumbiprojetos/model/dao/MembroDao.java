package com.anhembimorumbiprojetos.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.anhembimorumbiprojetos.db.DB;
import com.anhembimorumbiprojetos.db.DbException;
import com.anhembimorumbiprojetos.model.entities.Membro;

public class MembroDao {
	
	private Connection conn;
	
	public MembroDao(Connection conn) {
		this.conn = conn;
	}
	
	public void adicionarMembro (Membro membro) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO membro (nome, email, equipeId) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			st.setString(1,  membro.getNome());
			st.setString(2, membro.getEmail());
			st.setInt(3,  membro.getEquipeId());
			
			int rowsAffected = st.executeUpdate();
			
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					membro.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException ("Erro inesperado! Nenhuma linha afetado!");
			}
		}
		catch(SQLException e) {
			throw new DbException (e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}
	
	public void atualizar(Membro membro) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE membro SET id = ?, nome = ?, email = ?, equipeId = ? WHERE id = ?");
			st.setInt(1,  membro.getId());
			st.setString(2, membro.getNome());
			st.setString(3,  membro.getEmail());
			st.setInt(4,  membro.getEquipeId());
			st.setInt(5,  membro.getId());
			
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
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM membro WHERE id = ?");
			st.setInt(1,  id);
			int rowsAffected = st.executeUpdate();
			if(rowsAffected == 0) {
				throw new DbException ("Membro não encontrado");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}
	
	public Membro buscarPorId(int id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM membro WHERE id = ?");
			st.setInt(1,  id);
			rs = st.executeQuery();
			if (rs.next()) {
				Membro membro = instanciarMembro(rs);
				return membro;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}
	
	public List<Membro> listarTodos(){
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Membro> membros = new ArrayList<>();
		try {
			st = conn.prepareStatement("SELECT * FROM membro");
			rs = st.executeQuery();
			
			while (rs.next()) {
				membros.add(instanciarMembro(rs));
			}
			
			return membros;
			
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	public List<Membro> listarPorEquipe(int equipeId){
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Membro> membros = new ArrayList<>();
		try {
			st = conn.prepareStatement("SELECT * FROM membro WHERE equipeId = ?");
			st.setInt(1, equipeId);
			rs = st.executeQuery();
			
			while (rs.next()) {
				membros.add(instanciarMembro(rs));
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
		membro.setEquipeId(rs.getInt("equipeId"));
		return membro;
	}
}
