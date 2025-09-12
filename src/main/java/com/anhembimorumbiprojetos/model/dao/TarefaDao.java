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
import com.anhembimorumbiprojetos.model.entities.Tarefa;

public class TarefaDao {

    private Connection conn;

    public TarefaDao(){

    }

    public TarefaDao(Connection conn){
        if(conn == null){
            throw new IllegalArgumentException ("Conexão não pode ser nula");
        }
        this.conn = conn;
    }

    public void setConnection(Connection conn){
        if(conn == null){
            throw new IllegalArgumentException ("Conexão não pode ser nula");
        }
        this.conn = conn;
    }

    public void verificarConexao(){
        if(conn == null){
            throw new DbException("Conexão com o banco de dados não estabelecida");
        }
    }

    public void adicionarTarefa(Tarefa tarefa){
        verificarConexao();
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("INSERT INTO tarefa (titulo, descricao, dataInicio, dataTermino, status, projetoId, membroId) VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            st.setString(1, tarefa.getTitulo());
            st.setString(2, tarefa.getDescricao());
            st.setDate(3, Date.valueOf(tarefa.getDataInicio()));
            st.setDate(4, Date.valueOf(tarefa.getDataTermino()));
            st.setString(5, tarefa.getStatus());
            st.setInt(6, tarefa.getProjetoId());
            st.setInt(7, tarefa.getMembroId());

            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0){
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()){
                    int id = rs.getInt(1);
                    tarefa.setId(id);
                }
                DB.closeResultSet(rs);
            }
            else {
                throw new DbException("Erro inesperado, nenhuma linha afetada!");
            }
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(st);
        }
    }

    public void atualizarTarefa(Tarefa tarefa){
        verificarConexao();
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("UPDATE tarefa SET titulo = ?, descricao = ?, dataInicio = ?, dataTermino = ?, status = ?, projetoId = ?, membroId = ? WHERE id = ?");
            st.setString(1, tarefa.getTitulo());
            st.setString(2, tarefa.getDescricao());
            st.setDate(3, Date.valueOf(tarefa.getDataInicio()));
            st.setDate(4, Date.valueOf(tarefa.getDataTermino()));
            st.setString(5, tarefa.getStatus());
            st.setInt(6, tarefa.getProjetoId());
            st.setInt(7, tarefa.getMembroId());
            st.setInt(8, tarefa.getId());

            st.executeUpdate();
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(st);
        }
    }

    public void excluir(int id){
        verificarConexao();
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement("DELETE FROM tarefa WHERE id = ?");
            st.setInt(1, id);
            int rowsAffected = st.executeUpdate();
            if (rowsAffected == 0){
                throw new DbException("Tarefa não encontrada");
            }
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(st);
        }
    }

    public Tarefa buscarPorId(int id){
        verificarConexao();
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement("SELECT * FROM tarefa WHERE id = ?");
            st.setInt(1, id);
            rs = st.executeQuery();
            if(rs.next()){
                Tarefa tarefa = instanciarTarefa(rs);
                return tarefa;
            }
            return null;
        }
        catch (SQLException e){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }
    public List<Tarefa> listarTodos(){
        verificarConexao();
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Tarefa> tarefas = new ArrayList<>();
        try{
            st = conn.prepareStatement("SELECT * FROM tarefa ORDER BY titulo");
            rs = st.executeQuery();
            while(rs.next()){
                Tarefa tarefa = instanciarTarefa(rs);
                tarefas.add(tarefa);
            }

            return tarefas;
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    public List<Tarefa> listarPorProjeto(int projetoId){
        verificarConexao();
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Tarefa> tarefas = new ArrayList<>();
        try{
            st = conn.prepareStatement("SELECT * FROM tarefa WHERE projetoId = ? ORDER BY titulo");
            st.setInt(1, projetoId);
            rs = st.executeQuery();

            while(rs.next()){
                Tarefa tarefa = instanciarTarefa(rs);
                tarefas.add(tarefa);
            }
            return tarefas;
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    public List<Tarefa> listarPorMembro(int membroId){
        verificarConexao();
        PreparedStatement st = null;
        ResultSet rs = null;
        List<Tarefa> tarefas = new ArrayList<>();
        try{
            st = conn.prepareStatement("SELECT * FROM tarefa WHERE membroId = ? ORDER BY titulo");
            st.setInt(1, membroId);
            rs = st.executeQuery();

            while(rs.next()){
                Tarefa tarefa = instanciarTarefa(rs);
                tarefas.add(tarefa);
            }
            return tarefas;
        }
        catch(SQLException e){
            throw new DbException(e.getMessage());
        }
        finally{
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private Tarefa instanciarTarefa(ResultSet rs) throws SQLException{
        Tarefa tarefa = new Tarefa();
        tarefa.setId(rs.getInt("id"));
        tarefa.setTitulo(rs.getString("titulo"));
        tarefa.setDescricao(rs.getString("descricao"));
        java.sql.Date dataInicioSql = rs.getDate("dataInicio");
        if(dataInicioSql != null){
            tarefa.setDataInicio(dataInicioSql.toLocalDate());
        }
        else{
            tarefa.setDataInicio(null);
        }
        java.sql.Date dataTerminoSql = rs.getDate("dataTermino");
        if(dataTerminoSql != null){
            tarefa.setDataTermino(dataTerminoSql.toLocalDate());
        }
        else{
            tarefa.setDataTermino(null);
        }
        tarefa.setStatus(rs.getString("status"));
        tarefa.setProjetoId(rs.getInt("projetoId"));
        tarefa.setMembroId(rs.getInt("membroId"));

        return tarefa;
    }
}
