package com.event.repository;

import com.event.entity.Role;
import com.event.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@DataJpaTest
public class EventJpaTest {

    @Autowired
    private BookingRepository booking_repo;

    @Autowired
    private EventRepository event_repo;

    @Autowired
    private RoleRepository role_repo;

    @Autowired
    private UserRepository user_repo;

    @Test
    @DisplayName("findById should return correct user")
    public void findById(){

        //arrange
        Role role = new Role();
        role.setName("USER");
        role_repo.save(role);

        User user = new User();
        user.setName("user");
        user.setPassword("user123");
        user.setEmail("user@somedomain.com");
        user.setRoles(Set.of(role));
        user.setBookings(List.of());
        user_repo.save(user);


        //act
        Optional<User> user_tmp1 = user_repo.findById(1);
        Optional<User> user_tmp2 = user_repo.findById(221);


        //asssert
        assertThat(user_tmp1.get().getName()).isEqualTo("user");
        assertThat(user_tmp1.get().getPassword()).isEqualTo("user123");
        assertThat(user_tmp1.get().getEmail()).isEqualTo("user@somedomain.com");


        assertThat(user_tmp2.isEmpty());

    }

    @Test
    @DisplayName("findByName should return correct user")
    public void findByName(){

        //arrange
        Role role1 = new Role();
        role1.setName("USER");
        Role role2 = new Role();
        role2.setName("ADMIN");
        role_repo.save(role1);
        role_repo.save(role2);

        User user1 = new User();
        user1.setName("testuser1");
        user1.setPassword("test123");
        user1.setEmail("testuser1@somedomain.com");
        user1.setRoles(Set.of(role1));
        user1.setBookings(List.of());

        User user2 = new User();
        user2.setName("testuser2");
        user2.setPassword("test123");
        user2.setEmail("testuser2@somedomain.com");
        user2.setRoles(Set.of(role2));
        user2.setBookings(List.of());

        user_repo.save(user1);
        user_repo.save(user2);


        //act
        Optional<User> user_tmp1 = user_repo.findByName("testuser1");
        Optional<User> user_tmp2 = user_repo.findByName("testuser4");


        //assert
        assertThat(user_tmp1.get().getName()).isEqualTo("testuser1");
        assertThat(user_tmp2.isEmpty());
    }

    @Test
    @DisplayName("user_repo.save should return exception when incorrect data")
    public void testUserRepoSave(){
        //arrange
        Role role = new Role();
        role.setName("USER");

        User user = new User();
        user.setName("test");
        user.setPassword("test123");
        user.setEmail("user@somedomain.com");
        user.setRoles(Set.of(role));
        user.setBookings(List.of());
        user_repo.save(user);

        User user1 = new User();
        user1.setId(1);
        user1.setName("test");
        user1.setPassword("test123");
        user1.setEmail("user@somedomain.com");
        user1.setRoles(Set.of(role));
        user1.setBookings(List.of());


        //assert
        assertThrows(UnsupportedOperationException.class, () -> user_repo.save(user1));
    }
}
