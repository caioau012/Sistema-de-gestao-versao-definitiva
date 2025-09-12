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
	
	public MembroDao() {
	}
	
	public MembroDao(Connection conn) {
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
	
	public void adicionarMembro (Membro membro) {
		verificarConexao();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO membro (nome, email, equipeId) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			st.setString(1,  membro.getNome());
			st.setString(2, membro.getEmail());
			if (membro.getEquipeId() > 0) {
				st.setInt(3,  membro.getEquipeId());
			}
			else {
				st.setNull(3, java.sql.Types.INTEGER);
			}
			
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
				throw new DbException ("Erro inesperado! Nenhuma linha afetada!");
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
		verificarConexao();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE membro SET nome = ?, email = ?, equipeId = ? WHERE id = ?");
			st.setString(1, membro.getNome());
			st.setString(2,  membro.getEmail());
			if (membro.getEquipeId() > 0) {
                st.setInt(3, membro.getEquipeId());
            } else {
                st.setNull(3, java.sql.Types.INTEGER);
            }
			st.setInt(4,  membro.getId());
			
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
		verificarConexao();
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
		verificarConexao();
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Membro> membros = new ArrayList<>();
		try {
			st = conn.prepareStatement("SELECT * FROM membro ORDER BY nome");
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
		verificarConexao();
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Membro> membros = new ArrayList<>();
		try {
			st = conn.prepareStatement("SELECT * FROM membro WHERE equipeId = ? ORDER BY nome");
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
	
	public List<Membro> listarSemEquipe() {
        verificarConexao();
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Membro> membros = new ArrayList<>();
        
        try {
            st = conn.prepareStatement("SELECT * FROM membro WHERE equipeId IS NULL ORDER BY nome");
            rs = st.executeQuery();
            
            while (rs.next()) {
                membros.add(instanciarMembro(rs));
            }
            return membros;
        } catch(SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeResultSet(rs);
            DB.closeStatement(st);
        }
    }
	
	private Membro instanciarMembro(ResultSet rs) throws SQLException {
		Membro membro = new Membro();
		membro.setId(rs.getInt("id"));
		membro.setNome(rs.getString("nome"));
		membro.setEmail(rs.getString("email"));
		int equipeId = rs.getInt("equipeId");
		if (rs.wasNull()) {
			membro.setEquipeId(0);
		} else {
			membro.setEquipeId(equipeId);
		}
		return membro;
	}
}
