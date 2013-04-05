package bahir;

import java.io.File;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.Before;
import org.yaml.snakeyaml.Yaml;

public class DatabaseTestCase {

    public static SessionFactory sessionFactory;

    @Before
    public void setUp() throws Exception {
        Configuration configuration = new Configuration();
        configuration.configure();
        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).buildServiceRegistry();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);

        Yaml yaml = new Yaml();
        Map<String, List<Object>> all = (Map<String, List<Object>>) yaml.load(
                FileUtils.readFileToString(
                new File("src/test/resources/datasource.yml")));

        Session databaseSession = sessionFactory.openSession();
        databaseSession.beginTransaction();
        for(Object user: all.get("users"))
            databaseSession.save(user);
        databaseSession.getTransaction().commit();
        databaseSession.close();
    }
}
