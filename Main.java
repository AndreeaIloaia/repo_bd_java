import config.AppConfig;
import domain.Bilet;
import domain.Casier;
import domain.Meci;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import repo.RepoBilet;

public class Main {
    public static void main(String[] args) {
        ApplicationContext factory = new ClassPathXmlApplicationContext("Meci.xml");
        RepoBilet repoBilet = factory.getBean(RepoBilet.class);
        repoBilet.all();


    }
}
