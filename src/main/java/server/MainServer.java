package server;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import socket.ServerAbstract;

public class MainServer extends ServerAbstract{
    private int port;
    private SessionFactory dbSessionFactory;

    public MainServer(int port, SessionFactory dbSessionFactory) {
        super(port);
        this.dbSessionFactory = dbSessionFactory;
    }

    public int getPort()
    {
        return port;
    }

    public SessionFactory getDbSessionFactory()
    {
        return dbSessionFactory;
    }

    public void setUp() {
        // Sunucu olayları için olay yönetici atanıyor.
        setEventHandler(new MainServerEventHandler(this));
    }

    public static void main(String args[])
    {
        // Hibernate için ayarlamalar
        Configuration configuration = new Configuration();
        configuration.configure();
        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .buildServiceRegistry();
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

        // Sunucu çalıştırılıyor.
        MainServer server = new MainServer(8080, sessionFactory);
        server.run();
    }
}
