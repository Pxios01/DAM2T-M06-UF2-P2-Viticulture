package dao;

import model.Card;
import model.Player;
import utils.Number;

import java.sql.*;
import java.util.ArrayList;

public class DaoImpl implements Dao{

    private Connection con;
    private Statement st;
    private ResultSet rs;

    private static final String GET_USER = "SELECT * FROM player WHERE player.user = '??' AND player.password = '##'";
    private static final String GET_CARDS = "SELECT * FROM card LEFT JOIN game ON card.id = game.id WHERE id_player = ?? AND game.id IS NULL";
    private static final String GET_IDCARD= "SELECT ifnull (max(id), 0) + 1 as id FROM card WHERE id_player =  ?? ";
    private static final String INSERT_CARDS = "INSERT INTO card (id_player, number, color) VALUES (??, '##', '€€')";

    @Override
    public void connect() throws SQLException {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Base_Datos_Java?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", "root", "Pxios7131");
    }

    @Override
    public void disconnect() throws SQLException {
        con.close();
    }

    @Override
    public int getLastIdCard(int playerId) throws SQLException {
        String select = GET_IDCARD;
        String idPlayer = "" + playerId;
        select = select.replace("??", idPlayer);
        int id = 0;
        try(PreparedStatement ps = con.prepareStatement(select)){;
            try(ResultSet rs = ps.executeQuery()){
            while(rs.next()){
                id = rs.getInt("id");
            }
            }
        }


        return id;
    }

    @Override
    public Card getLastCard() throws SQLException {
        String select = GET_LAST_CARD;
        int  id = 0;
        try(PreparedStatement ps = con.prepareStatement(select)){
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    id = rs.getInt("id_card");
                    getCard(id);
                }
            }
        }
    }

    @Override
    public Player getPlayer(String user, String pass) throws SQLException {
        String select = GET_USER;
        Player pl = null;
        select = select.replace("??", user);
        select = select.replace("##", pass);
        try(PreparedStatement ps = con.prepareStatement(select)){
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    int games = rs.getInt("games");
                    int victories = rs.getInt("victories");
                    pl = new Player(id, name, games, victories);
                }

            }
        }
        return pl;
    }

    @Override
    public ArrayList<Card> getCards(int playerId) throws SQLException {
        ArrayList<Card> arrayCards = new ArrayList<Card>();
        String select = GET_CARDS;
        String idPlayer = "" + playerId;
        select = select.replace("??", idPlayer);
        try (PreparedStatement ps = con.prepareStatement(select)){
            try (ResultSet rs = ps.executeQuery()){
                Card newCard = null;
                while(rs.next()){
                    int id = rs.getInt("id");
                    String numberString = rs.getString("number");
                    String colorString = rs.getString("color");
                    int id_player = rs.getInt("id_player");
                    newCard = new Card(id, numberString, colorString, id_player);
                    arrayCards.add(newCard);
                }
            }
        }
        return arrayCards;
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
        String insert = INSERT_CARDS;
        insert = insert.replace("??", "" +card.getPlayerId());
        insert = insert.replace("##", card.getNumber());
        insert = insert.replace("€€", card.getColor());
        try(PreparedStatement ps = con.prepareStatement(insert)){
            ps.executeUpdate();
        }

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
