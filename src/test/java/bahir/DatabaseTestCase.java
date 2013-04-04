package bahir;

import java.io.File;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.yaml.snakeyaml.Yaml;

public class DatabaseTestCase extends TestCase {

    public static SessionFactory sessionFactory;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        if (sessionFactory == null)
        {
            Configuration configuration = new Configuration();
            configuration.configure();
            ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).buildServiceRegistry();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }

        Yaml yaml = new Yaml();
        Map<String, List<Object>> all = (Map<String, List<Object>>) yaml.load(
                FileUtils.readFileToString(
                new File("src/test/resources/datasource.yml")));

        sessionFactory.getCurrentSession().beginTransaction();
        for(Object user: all.get("users"))
            sessionFactory.getCurrentSession().save(user);
        sessionFactory.getCurrentSession().getTransaction().commit();
    }
}
