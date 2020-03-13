package repo;

import domain.Bilet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repo.validator.ValidatorBilet;
import utils.JDBCUtils;

import java.sql.*;
import java.util.Properties;

public class RepoBilet implements IRepoBilet {
    private static final Logger logger = LogManager.getLogger(RepoBilet.class);
    private ValidatorBilet validatorBilet;
    private JDBCUtils dbUtils;

    public ValidatorBilet getValidatorBilet() {
        return validatorBilet;
    }

    public void setValidatorBilet(ValidatorBilet validatorBilet) {
        this.validatorBilet = validatorBilet;
    }

    public RepoBilet(Properties properties) {
        dbUtils = new JDBCUtils(properties);
        logger.info("Constructor finished!");
    }

    public void all() {
        try {
            //Connection con = DriverManager.getConnection(this.url);
            Connection con = dbUtils.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from Bilet");
            while (rs.next()) {
                int id_meci = rs.getInt("ID_meci");
                int id_casier = rs.getInt("ID_casier");
                int locuri = rs.getInt("Locuri");
                String nume = rs.getString("Nume");
                Bilet bilet = new Bilet(id_casier, id_meci, locuri, nume);
                System.out.println(bilet);
            }
            logger.info("Selection succeeded");
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
        Adauga o entitate de tip Bilet in baza de date
        params: bilet: Bilet
        returns: entitatea daca nu s-a adaugat, null daca s-a adaugat sau daca entitatea primita e nula
        throw error - daca biletul este null
     */
    public Bilet adaugaBilet(Bilet bilet) {
        if (bilet == null) {
            throw new IllegalArgumentException("Entitatea nu poate sa fie null!\n");
        } else {
            String insert = "insert into Bilet values (?,?,?,?)";
            try (Connection connection = dbUtils.getConnection()) {
                PreparedStatement pstm = connection.prepareStatement(insert);
                pstm.setInt(1, bilet.getId_meci());
                pstm.setInt(2, bilet.getId_casier());
                pstm.setInt(3, bilet.getLocuri());
                pstm.setString(4, bilet.getNume());
                pstm.execute();
                logger.info("Biletul a fost adaugat");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /*
        Cauta o entitate de tip Casier dupa un ID
        params: id-Integer
        returns: entitatea, daca exista in BD, null, daca nu exista
        throws exception - daca id-ul e null
     */
    public Bilet cauta(Integer id_casier, Integer id_meci) {
        if (id_casier == null || id_meci == null)
            throw new IllegalArgumentException("Id-ul nu poate sa fie null!\n");

        try (Connection con = dbUtils.getConnection();) {
            Bilet bilet = null;
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from Bilet where ID_casier=" + id_casier + " and ID_meci=" + id_meci);
            while (rs.next()) {
                int locuri = rs.getInt("Locuri");
                String nume = rs.getString("Nume");
                bilet = new Bilet(id_meci, id_casier, locuri, nume);
            }
            return bilet;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
        Modifica o entitate de tip Bilet in baza de date dupa id_meci si id_casier
        params id_meci: int
        params id_casier : int
        params nume: string
        params locuri: int
        returns: entitatea daca nu s-a adaugat null daca s-a adaugat
        throws error: daca id-urile sunt nule sau daca entitatea nu exista
     */
    public Bilet updateBilet(Integer id_meci, Integer id_casier, int locuri, String nume) {
        if (id_meci == null || id_casier == null) {
            throw new IllegalArgumentException("ID-ul nu poate fi null!\n");
        } else if (cauta(id_casier, id_meci) == null) {
            throw new IllegalArgumentException("Entitatea nu exista!\n");
        } else {
            String update = "update Bilet set Locuri = ?, Nume = ? where ID_meci = ? and ID_casier = ?";
            try (Connection connection = dbUtils.getConnection()) {
                PreparedStatement pstm = connection.prepareStatement(update);
                pstm.setInt(1, locuri);
                pstm.setString(2, nume);
                pstm.setInt(3, id_meci);
                pstm.setInt(4, id_casier);
                pstm.execute();
                logger.info("Biletul a fost modificat");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /*
        Sterge o entitate de tip Meci in baza de date dupa id
        params id: int
        returns: entitatea daca nu s-a sters, null daca s-a adaugat sau daca entitatea primita e nula
     */
    public Bilet stergeBilet(Integer id_meci, Integer id_casier) {
        if (id_meci == null || id_casier == null) {
            throw new IllegalArgumentException("ID-ul nu poate fi null!\n");
        } else if (cauta(id_casier, id_meci) == null) {
            throw new IllegalArgumentException("Entitatea nu exista!\n");
        } else {
            String delete = "delete from Bilet where ID_meci = ? and ID_casier = ?";
            try (Connection connection = dbUtils.getConnection()) {
                PreparedStatement pstm = connection.prepareStatement(delete);
                pstm.setInt(1, id_meci);
                pstm.setInt(2, id_casier);
                pstm.execute();
                logger.info("Biletul a fost sters");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
