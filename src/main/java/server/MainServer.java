package server;

import java.util.HashMap;
import java.util.UUID;
import message.RequestLogin;
import message.RequestRegister;
import message.ResponseLogin;
import message.ResponseUnauthorized;
import message.ResponseUsernameAlreadyInUse;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import server.model.AccessToken;
import server.model.User;
import socket.ServerAbstract;

public class MainServer extends ServerAbstract{
    private int port;
    private SessionFactory dbSessionFactory;
    private HashMap<String, MainClient> clients;

    public MainServer(int port, SessionFactory dbSessionFactory) {
        super(port);
        this.dbSessionFactory = dbSessionFactory;
        clients = new HashMap<String, MainClient>();
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

    private User getUserByUsername(String username)
    {
        Session dbSession = getDbSessionFactory().openSession();

        User user = (User) dbSession.createCriteria(User.class)
                .add(Restrictions.eq("username", username))
                .uniqueResult();

        dbSession.close();

        return user;
    }

    private String createToken(MainClient mainClient, User user)
    {
        Session dbSession = getDbSessionFactory().openSession();
        String token = DigestUtils.sha256Hex(UUID.randomUUID().toString());

        AccessToken accessToken = new AccessToken();
        accessToken.setUser(user);
        accessToken.setToken(token);
        dbSession.save(accessToken);

        dbSession.close();

        mainClient.setUser(user);
        clients.put(mainClient.getClientId(), mainClient);

        return token;
    }

    public synchronized void logout(MainClient mainClient)
    {
        if (clients.containsKey(mainClient.getClientId()))
            clients.remove(mainClient.getClientId());
    }

    public void login(MainClient mainClient, RequestLogin request)
    {
        User user = getUserByUsername(request.getUsername());

        if (user == null)
        {
            mainClient.writeObject(new ResponseUnauthorized());
            return;
        }

        String saltedPassword = DigestUtils.sha256Hex(user.getSalt() + request.getPassword());
        if (user.getPassword().equals(saltedPassword))
        {
            mainClient.writeObject(new ResponseUnauthorized());
            return;
        }

        mainClient.writeObject(
                new ResponseLogin(createToken(mainClient, user)));
    }

    public synchronized void register(MainClient mainClient, RequestRegister request)
    {
        User user = getUserByUsername(request.getUsername());

        if (user != null)
        {
            mainClient.writeObject(new ResponseUsernameAlreadyInUse());
            return;
        }

        Session dbSession = getDbSessionFactory().openSession();

        String salt = DigestUtils.sha256Hex(UUID.randomUUID().toString());
        String password = DigestUtils.sha256Hex(salt + request.getPassword());

        user = new User();
        user.setUsername(request.getUsername());
        user.setSalt(salt);
        user.setPassword(password);
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        dbSession.save(user);

        dbSession.close();

        mainClient.writeObject(
                new ResponseLogin(createToken(mainClient, user)));
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
        new MainServer(8080, sessionFactory).run();
    }
}
