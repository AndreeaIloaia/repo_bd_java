package repo;

import domain.Casier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repo.validator.ValidatorCasier;
import utils.JDBCUtils;

import java.sql.*;
import java.util.Properties;

public class RepoCasier implements IRepoCasier {
    private static final Logger logger = LogManager.getLogger(RepoMeci.class);
    private JDBCUtils dbUtils;
    private ValidatorCasier validatorCasier;

    public ValidatorCasier getValidatorCasier() {
        return validatorCasier;
    }

    public void setValidatorCasier(ValidatorCasier validatorCasier) {
        this.validatorCasier = validatorCasier;
    }

    public RepoCasier(Properties properties) {
        dbUtils = new JDBCUtils(properties);
        logger.info("Constructor finished!");
    }

    public void all() {
        try {
            Connection con = dbUtils.getConnection();

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from Casier");
            while (rs.next()) {
                int id = rs.getInt("ID_casier");
                String nume = rs.getString("Nume");
                String parola = rs.getString("Parola");
                Casier casier = new Casier(id, nume, parola);
                System.out.println(casier);
            }
            logger.info("Selection succeeded");
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*
        Cauta o entitate de tip Casier dupa un ID
        params: id-Integer
        returns: entitatea, daca exista in BD, null, daca nu exista
        throws exception - daca id-ul e null
     */
    public Casier cauta(Integer id) {
        if (id == null)
            throw new IllegalArgumentException("Id-ul nu poate sa fie null!\n");

        try (Connection con = dbUtils.getConnection();) {
            Casier casier = null;
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from Casier where ID_casier=" + id);
            while (rs.next()) {
                String nume = rs.getString("Nume");
                String parola = rs.getString("Parola");
                casier = new Casier(id, nume, parola);
            }
            return casier;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
        Adauga o entitate de tip Casier in baza de date
        params: casier: Casier
        returns: entitatea daca nu s-a adaugat, null daca s-a adaugat sau daca entitatea primita e nula
        throw error - daca casier este null
     */
    public Casier adaugaCasier(Casier casier) {
        if (casier == null) {
            throw new IllegalArgumentException("Entitatea nu poate sa fie null!\n");
        } else {
            String insert = "insert into Casier values (?,?,?)";
            try (Connection connection = dbUtils.getConnection();) {
                PreparedStatement pstm = connection.prepareStatement(insert);
                pstm.setInt(1, casier.getId());
                pstm.setString(2, casier.getNume());
                pstm.setString(3, casier.getParola());
                pstm.execute();
                logger.info("Casier a fost adaugat");
                return null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return casier;
        }
    }

    /*
        Modifica o entitate de tip Casier in baza de date dupa id
        params id: int
        params nume: string
        returns: entitatea daca nu s-a adaugat, null daca s-a adaugat sau daca entitatea primita e nula
     */
    public Casier updateCasier(Integer id, String nume, String parola) {
        if (id == null) {
            throw new IllegalArgumentException("ID-ul nu poate fi null!\n");
        } else if (cauta(id) == null) {
            return cauta(id);
        } else {
            String update = "update Casier set Nume = ?, Parola = ? where ID_casier = ?";
            try (Connection connection = dbUtils.getConnection();) {
                PreparedStatement pstm = connection.prepareStatement(update);
                pstm.setString(1, nume);
                pstm.setString(2, parola);
                pstm.setInt(3, id);
                pstm.execute();
                logger.info("Casier a fost modificat");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /*
        Sterge o entitate de tip Casier in baza de date dupa id
        params id: Integer
        returns: entitatea daca nu s-a sters, null daca s-a adaugat sau daca entitatea primita e nula
     */
    public Casier stergeCasier(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Entitatea nu poate sa fie null!\n");
        } else if (cauta(id) == null) {
            return cauta(id);
        } else {
            String delete = "delete from Casier where ID_casier= ?";
            try (Connection connection = dbUtils.getConnection();) {
                PreparedStatement pstm = connection.prepareStatement(delete);
                pstm.setInt(1, id);
                pstm.execute();
                logger.info("Casier a fost sters");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
