package cz.cvut.fel.ear.onlinesystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication()
public class OnlineSystemApplication {

  public static void main(String[] args) {
    SpringApplication.run(OnlineSystemApplication.class, args);
  }

}
