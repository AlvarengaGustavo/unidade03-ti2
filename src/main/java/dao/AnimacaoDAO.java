package dao;

import model.Animacao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class AnimacaoDAO extends DAO {
    public AnimacaoDAO() {
        super();
        conectar();
    }

    public void finalize() {
        close();
    }

    public boolean insert(Animacao animacao) {
        boolean status = false;
        try {
            String sql = "INSERT INTO animacao (genero, nome, duracao, quantidade, datafabricacao, datalancamento) "
                    + "VALUES ('" + animacao.getGenero() + "', "
                    + "'" + animacao.getNome() + "', " + animacao.getDuracao() + ", ?, ?);";
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setTimestamp(1, Timestamp.valueOf(animacao.getDataFabricacao()));
            st.setDate(2, Date.valueOf(animacao.getDataLancamento()));
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public Animacao get(int id) {
        Animacao animacao = null;

        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * FROM animacao WHERE id=" + id;
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                animacao = new Animacao(rs.getInt("id"), rs.getString("nome"), (float) rs.getDouble("duracao"),
                        rs.getString("genero"),
                        rs.getTimestamp("datafabricacao").toLocalDateTime(),
                        rs.getDate("datalancamento").toLocalDate());
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return animacao;
    }

    public List<Animacao> get() {
        return get("");
    }

    public List<Animacao> getOrderByID() {
        return get("id");
    }

    public List<Animacao> getOrderByNome() {
        return get("nome");
    }

    public List<Animacao> getOrderByDuracao() {
        return get("duracao");
    }

    private List<Animacao> get(String orderBy) {
        List<Animacao> animacoes = new ArrayList<Animacao>();

        try {
            Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String sql = "SELECT * FROM animacao" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Animacao f = new Animacao(rs.getInt("id"), rs.getString("nome"), (float) rs.getDouble("duracao"),
                        rs.getString("genero"),
                        rs.getTimestamp("datafabricacao").toLocalDateTime(),
                        rs.getDate("datalancamento").toLocalDate());
                animacoes.add(f);
            }
            st.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return animacoes;
    }

    public boolean update(Animacao animacao) {
        boolean status = false;
        try {
            String sql = "UPDATE animacao SET nome = '" + animacao.getNome() + "', "
                    + "duracao = " + animacao.getDuracao() + ", "
                    + "genero = '" + animacao.getGenero() + "', "
                    + "datafabricacao = ?, "
                    + "datalancamento = ? WHERE id = " + animacao.getId();
            PreparedStatement st = conexao.prepareStatement(sql);
            st.setTimestamp(1, Timestamp.valueOf(animacao.getDataFabricacao()));
            st.setDate(2, Date.valueOf(animacao.getDataLancamento()));
            st.executeUpdate();
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }

    public boolean delete(int id) {
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            st.executeUpdate("DELETE FROM animacao WHERE id = " + id);
            st.close();
            status = true;
        } catch (SQLException u) {
            throw new RuntimeException(u);
        }
        return status;
    }
    
}

