package dao;

import model.Card;
import model.Player;

import java.sql.*;
import java.util.ArrayList;

public class DaoImpl implements Dao{

    private Connection con;
    private Statement st;
    private ResultSet rs;
    @Override
    public void connect() throws SQLException {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Base_Datos_Java", "root", "Pxios7131");
    }

    @Override
    public void disconnect() throws SQLException {

    }

    @Override
    public int getLastIdCard(int playerId) throws SQLException {
        st = con.createStatement();
        rs = st.executeQuery("SELECT id_card FROM game WHERE id = (SELECT MAX(id) FROM game");

        return ;
    }

    @Override
    public Card getLastCard() throws SQLException {
        return null;
    }

    @Override
    public Player getPlayer(String user, String pass) throws SQLException {
        st = con.createStatement();
        rs = st.executeQuery("SELECT * FROM player WHERE player.user = "+ user + "AND player.password = "+ pass);
        Player pl;
        while(rs.next()){
            int id = rs.getInt("id");
            String name = rs.getString("name");
            int games = rs.getInt("games");
            int victories = rs.getInt("victories");
            pl = new Player(id, name, games, victories);
        }
        return pl;
    }

    @Override
    public ArrayList<Card> getCards(int playerId) throws SQLException {
        return null;
    }

    @Override
    public Card getCard(int cardId) throws SQLException {
        return null;
    }

    @Override
    public void saveGame(Card card) throws SQLException {

    }

    @Override
    public void saveCard(Card card) throws SQLException {

    }

    @Override
    public void deleteCard(Card card) throws SQLException {

    }

    @Override
    public void clearDeck(int playerId) throws SQLException {

    }

    @Override
    public void addVictories(int playerId) throws SQLException {

    }

    @Override
    public void addGames(int playerId) throws SQLException {

    }
}
