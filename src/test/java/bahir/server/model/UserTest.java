package bahir.server.model;

import bahir.DatabaseTestCase;

public class UserTest extends DatabaseTestCase{

    public void testCreate()
    {
        User user = new User();
        user.setEmail("test@test.com");
        user.setUsername("username");
        user.setPassword("password");
        sessionFactory.openSession().save(user);

        assertEquals(new Long(3), user.getId());
    }
}
