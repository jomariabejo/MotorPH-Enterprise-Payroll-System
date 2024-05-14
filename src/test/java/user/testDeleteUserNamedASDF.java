package user;

import com.jomariabejo.motorph.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class testDeleteUserNamedASDF {
    // make sure that u have a username with a value of asdf
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
    }

    @Test
    void userTableShouldRemoveUserNamedASDF() throws SQLException {
        userRepository.deleteUser("asdf");
    }
}
