package com.anhembimorumbiprojetos.model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.anhembimorumbiprojetos.db.DB;
import com.anhembimorumbiprojetos.db.DbException;
import com.anhembimorumbiprojetos.model.entities.Projeto;

public class ProjetoDao {
	
	private Connection conn;
	
	public ProjetoDao() {
	}
	
	public ProjetoDao(Connection conn) {
		if(conn == null) {
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
	
	public void adicionarProjeto(Projeto projeto) {
		verificarConexao();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("INSERT INTO projeto (nome, descricao, dataInicio, dataTermino, status, equipeId) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			st.setString(1,  projeto.getNome());
			st.setString(2,  projeto.getDescricao());
			LocalDate inicio = projeto.getDataInicio();
			LocalDate termino = projeto.getDataTermino();

			st.setDate(3, inicio != null ? Date.valueOf(inicio) : null);
			st.setDate(4, termino != null ? Date.valueOf(termino) : null);

			st.setString(5,  projeto.getStatus());
			st.setInt(6,  projeto.getEquipeId());
			
			int rowsAffected = st.executeUpdate();
			if (rowsAffected >0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					projeto.setId(id);
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
	
	public void atualizarProjeto(Projeto projeto) {
		verificarConexao();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE projeto SET nome = ?, descricao = ?, dataInicio = ?, dataTermino = ?, status = ?, equipeId = ? WHERE id = ?");
			st.setString(1,  projeto.getNome());
			st.setString(2,  projeto.getDescricao());
			LocalDate inicio = projeto.getDataInicio();
			LocalDate termino = projeto.getDataTermino();

			st.setDate(3, inicio != null ? Date.valueOf(inicio) : null);
			st.setDate(4, termino != null ? Date.valueOf(termino) : null);

			st.setString(5,  projeto.getStatus());
			st.setInt(6,  projeto.getEquipeId());
			st.setInt(7, projeto.getId());
			
			st.executeUpdate();
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}
	
	public void excluir(int id) {
		verificarConexao();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM projeto WHERE id = ?");
			st.setInt(1, id);
			int rowsAffected = st.executeUpdate();
			if (rowsAffected == 0) {
				throw new DbException("Projeto não encontrado");
			}
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}
	
	public Projeto buscarPorId(int id) {
		verificarConexao();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM projeto WHERE id = ?");
			st.setInt(1,  id);
			rs = st.executeQuery();
			if(rs.next()) {
				Projeto projeto = instanciarProjeto(rs);
				return projeto;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException (e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}
	
	public List<Projeto> listarTodos(){
		verificarConexao();
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Projeto> projetos = new ArrayList<>();
		try {
			st = conn.prepareStatement("SELECT * FROM projeto ORDER BY nome");
			rs = st.executeQuery();
			
			while(rs.next()) {
				projetos.add(instanciarProjeto(rs));
			}
			return projetos;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}
	
	public List<Projeto> listarPorStatus(String status){
		verificarConexao();
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Projeto> projetos = new ArrayList<>();
		try {
			st = conn.prepareStatement("SELECT * FROM projeto WHERE status = ? ORDER BY nome");
			st.setString(1,  status);
			rs = st.executeQuery();
			
			while(rs.next()) {
				projetos.add(instanciarProjeto(rs));
			}
			return projetos;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}
	
	private Projeto instanciarProjeto(ResultSet rs) throws SQLException {
		Projeto projeto = new Projeto();
		projeto.setId(rs.getInt("id"));
		projeto.setNome(rs.getString("nome"));
		projeto.setDescricao(rs.getString("descricao"));
		java.sql.Date dataInicioSql = rs.getDate("dataInicio");
		if (dataInicioSql != null) {
			projeto.setDataInicio(dataInicioSql.toLocalDate());
		}
		else {
			projeto.setDataInicio(null);
		}
		java.sql.Date dataTerminoSql = rs.getDate("dataTermino");
		if( dataTerminoSql != null) {
			projeto.setDataTermino(dataTerminoSql.toLocalDate());
		}
		else {
			projeto.setDataTermino(null);
		}
		projeto.setStatus(rs.getString("status"));
		projeto.setEquipeId(rs.getInt("equipeId"));
		
		return projeto;
	}
}
