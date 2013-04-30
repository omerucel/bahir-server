package server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import message.RequestAddFriend;
import message.RequestLogin;
import message.RequestRegister;
import message.RequestSendFriendList;
import message.RequestUpdateFriend;
import message.ResponseAddFriend;
import message.ResponseError;
import message.ResponseLogin;
import message.ResponseSendFriendList;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import server.model.AccessToken;
import server.model.User;
import server.model.UserFriend;
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

    private User getUserByToken(String token)
    {
        Session dbSession = getDbSessionFactory().openSession();

        AccessToken accessToken = (AccessToken) dbSession.createCriteria(AccessToken.class)
                .add(Restrictions.eq("token", token))
                .uniqueResult();

        dbSession.close();

        return accessToken.getUser();
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
            mainClient.writeObject(new ResponseError("Girdiğiniz bilgiler hatalı."));
            return;
        }

        String saltedPassword = DigestUtils.sha256Hex(user.getSalt() + request.getPassword());
        if (user.getPassword().equals(saltedPassword))
        {
            mainClient.writeObject(new ResponseError("Girdiğiniz bilgiler hatalı."));
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
            mainClient.writeObject(new ResponseError("Seçtiğiniz kullanıcı adı ile daha önce bir hesap açılmış."));
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

    public synchronized void addFriend(MainClient mainClient, RequestAddFriend request)
    {
        User user = getUserByToken(request.getToken());
        if (user != null)
        {
            mainClient.writeObject(new ResponseError("Oturum açmanız gerekiyor."));
            return;
        }

        User friend = getUserByUsername(request.getUsername());
        if (user != null)
        {
            mainClient.writeObject(new ResponseError("Arkadaş olarak eklemek istediğiniz kullanıcı adı sistemde bulunmamakta."));
            return;
        }

        Session dbSession = getDbSessionFactory().openSession();

        UserFriend userFriend = (UserFriend) dbSession.createCriteria(UserFriend.class)
                .add(Restrictions.eq("user", user))
                .add(Restrictions.eq("friend", friend))
                .uniqueResult();

        if (userFriend != null)
        {
            mainClient.writeObject(new ResponseError("Zaten ilgili kullanıcı arkadaş listenize eklenmiş. Arkadaş listenizde görünebilmesi için arkadaşlık isteğinizi onaylaması gerekmektedir."));
            return;
        }

        // TODO : friend kullanıcısı online durumdaysa, ilgili kullanıcıya arkadaşlık isteği durumu iletilmeli.

        userFriend = new UserFriend();
        userFriend.setFriend(friend);
        userFriend.setUser(user);
        userFriend.setIsApproved(false);
        dbSession.save(userFriend);
        dbSession.close();

        mainClient.writeObject(new ResponseAddFriend());
    }

    public synchronized void updateFriend(MainClient mainClient, RequestUpdateFriend request)
    {
        User user = getUserByToken(request.getToken());
        if (user != null)
        {
            mainClient.writeObject(new ResponseError("Oturum açmanız gerekiyor."));
            return;
        }

        Session dbSession = getDbSessionFactory().openSession();
        UserFriend userFriend = (UserFriend) dbSession.createCriteria(UserFriend.class)
                .add(Restrictions.eq("user.username", request.getUsername()))
                .uniqueResult();
        if (userFriend == null)
        {
            mainClient.writeObject(new ResponseError("İşlem yapmak istediğiniz kullanıcı arkadaş listenizde bulunmamakta."));
            return;
        }

        userFriend.setIsApproved(request.getStatus());

        // TODO : İlgili kullanıcı online ise arkadaşlık isteği onaylama durumu bilgisi iletilmeli.
    }

    public synchronized void sendFriendList(MainClient mainClient, RequestSendFriendList request)
    {
        User user = getUserByToken(request.getToken());
        if (user != null)
        {
            mainClient.writeObject(new ResponseError("Oturum açmanız gerekiyor."));
            return;
        }

        Session dbSession = getDbSessionFactory().openSession();
        List<UserFriend> friendList = (List<UserFriend>) dbSession.createCriteria(UserFriend.class)
                             .add(Restrictions.or(
                                 Restrictions.eq("user", user), Restrictions.eq("friend", user)))
                             .list();

        ArrayList<String> mapList = new ArrayList<String>();
        for(UserFriend userFriend : friendList)
        {
            if (userFriend.getUser().equals(user))
            {
                mapList.add(userFriend.getFriend().getUsername());
            }else{
                mapList.add(userFriend.getUser().getUsername());
            }
        }

        mainClient.writeObject(new ResponseSendFriendList(mapList));

        // TODO : Online olan ve olmayan arkadaş durumları listede belirtilmeli.
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
