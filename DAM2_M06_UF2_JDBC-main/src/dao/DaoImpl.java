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
    private static final String GET_LAST_CARD= "SELECT id_card FROM game WHERE id = (SELECT MAX (id) FROM game)";
    private static final String GET_CARD= "SELECT * FROM card WHERE id = ??";
    private static final String DELETE_CARD= "DELETE FROM game WHERE id_card = ?";
    private static final String CLEAR_DECK= "DELETE FROM card WHERE id_player = ?";
    private static final String UPDATE_VICTORIES= "UPDATE player SET victories = victories + 1 WHERE id = ?";
    private static final String UPDATE_GAMES= "UPDATE player SET games = games + 1 WHERE id = ?";
    private static final String SAVE_GAME= "INSERT INTO game (id_card) VALUES (?)";
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
        Card lastCard = null;
        try(PreparedStatement ps = con.prepareStatement(select)){
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    id = rs.getInt("id_card");
                    lastCard = getCard(id);
                }
            }
        }
        return lastCard;
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
        String select = GET_CARD;
        Card card = null;
        select = select.replace("??", ""+cardId);
        try(PreparedStatement ps = con.prepareStatement(select)){
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    int id = rs.getInt("id");
                    int id_player = rs.getInt("id_player");
                    String number = rs.getString("number");
                    String color = rs.getString("color");
                    card = new Card(id,number, color, id_player);
                }
            }
        }
        return card;
    }

    @Override
    public void saveGame(Card card) throws SQLException {
        String insert = SAVE_GAME;
        try (PreparedStatement ps = con.prepareStatement(insert)){
            ps.setInt(1, card.getId());
            ps.executeUpdate();
        }
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
        String delete = DELETE_CARD;
        try(PreparedStatement ps = con.prepareStatement(delete)){
            ps.setInt(1, card.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void clearDeck(int playerId) throws SQLException {
        String delete = CLEAR_DECK;
        try(PreparedStatement ps = con.prepareStatement(delete)){
            ps.setInt(1, playerId);
            ps.executeUpdate();
        }
    }

    @Override
    public void addVictories(int playerId) throws SQLException {
        String update = UPDATE_VICTORIES;
        try(PreparedStatement ps = con.prepareStatement(update)){
            ps.setInt(1, playerId);
            ps.executeUpdate();
        }
    }

    @Override
    public void addGames(int playerId) throws SQLException {
        String update = UPDATE_GAMES;
        try(PreparedStatement ps = con.prepareStatement(update)){
            ps.setInt(1, playerId);
            ps.executeUpdate();
        }
    }
}
