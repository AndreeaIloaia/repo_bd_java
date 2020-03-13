package repo;

import domain.Meci;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import repo.validator.ValidatorMeci;
import utils.JDBCUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RepoMeci implements IRepoMeci{
    private static final Logger logger = LogManager.getLogger(RepoMeci.class);
    private JDBCUtils dbUtils;
    private ValidatorMeci validatorMeci;

    public RepoMeci(Properties properties) {
        dbUtils = new JDBCUtils(properties);
        logger.info("Constructor finished!");
    }

    public ValidatorMeci getValidatorMeci() {
        return validatorMeci;
    }

    public void setValidatorMeci(ValidatorMeci validatorMeci) {
        this.validatorMeci = validatorMeci;
    }

    public Iterable<Meci> all() {
        try {
            Connection con = dbUtils.getConnection();
            List<Meci> list = new ArrayList<>();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from Meci");
            while (rs.next()) {
                int id = rs.getInt("ID_meci");
                String nume = rs.getString("Denumire");
                int pret = rs.getInt("Pret");
                int locuri = rs.getInt("Locuri");
                Meci meci = new Meci(id, nume, pret, locuri);
                //System.out.println(meci);
                list.add(meci);
            }
            logger.info("Selection succeeded");
            con.close();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Meci cauta(Integer id) {
        if (id == null)
            throw new IllegalArgumentException("Id-ul nu poate sa fie null!\n");

        try (Connection con = dbUtils.getConnection();) {
            Meci meci = null;
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from Meci where ID_meci=" + id);
            while (rs.next()) {
                String denumire = rs.getString("Denumire");
                int pret = rs.getInt("Pret");
                int locuri = rs.getInt("Locuri");
                meci = new Meci(id, denumire, pret, locuri);
            }
            return meci;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
        Adauga o entitate de tip Meci in baza de date
        params: meci: Meci
        returns: entitatea daca nu s-a adaugat, null daca s-a adaugat sau daca entitatea primita e nula
        throw error - daca meciul este null
     */
    public Meci adaugaMeci(Meci meci) {
        if (meci == null) {
            throw new IllegalArgumentException("Entitatea nu poate sa fie null!\n");
        } else {
            String insert = "insert into Meci values (?,?,?,?)";
            try (Connection connection = dbUtils.getConnection()) {
                PreparedStatement pstm = connection.prepareStatement(insert);
                pstm.setInt(1, meci.getId());
                pstm.setString(2, meci.getDenumire());
                pstm.setInt(3, meci.getPret());
                pstm.setInt(4, meci.getLocuri());
                pstm.execute();
                logger.info("Meciul a fost adaugat");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /*
        Modifica o entitate de tip Meci in baza de date dupa id
        params id: int
        params denumire: string
        params pret: int
        params locuri: int
        returns: entitatea daca nu s-a adaugat, null daca s-a adaugat sau daca entitatea primita e nula
     */
    public Meci updateMeci(Integer id, String denumire, int pret, int locuri) {
        if (id == null) {
            throw new IllegalArgumentException("ID-ul nu poate fi null!\n");
        } else if (cauta(id) == null) {
            return cauta(id);
        } else {
            String update = "update Meci set Denumire = ?, Pret = ?, Locuri = ? where ID_meci = ?";
            try (Connection connection = dbUtils.getConnection()) {
                PreparedStatement pstm = connection.prepareStatement(update);
                pstm.setString(1, denumire);
                pstm.setInt(2, pret);
                pstm.setInt(3, locuri);
                pstm.setInt(4, id);
                pstm.execute();
                logger.info("Meciul a fost modificat");
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
    public Meci stergeMeci(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Entitatea nu poate sa fie null!\n");
        } else if (cauta(id) == null) {
            return cauta(id);
        } else {
            String delete = "delete from Meci where ID_meci = ?";
            try (Connection connection = dbUtils.getConnection()) {
                PreparedStatement pstm = connection.prepareStatement(delete);
                pstm.setInt(1, id);
                pstm.execute();
                logger.info("Meciul a fost sters");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
