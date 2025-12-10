package cz.cvut.fel.ear.onlinesystem.service.security;

import cz.cvut.fel.ear.onlinesystem.model.Role;
import cz.cvut.fel.ear.onlinesystem.model.StudentTest;
import cz.cvut.fel.ear.onlinesystem.model.User;
import cz.cvut.fel.ear.onlinesystem.repository.StudentTestRepository;
import cz.cvut.fel.ear.onlinesystem.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("securityService")
public class SecurityService {

    private final UserRepository userRepository;
    private final StudentTestRepository studentTestRepository;

    public SecurityService(UserRepository userRepository,
                           StudentTestRepository studentTestRepository) {
        this.userRepository = userRepository;
        this.studentTestRepository = studentTestRepository;
    }

    public Integer getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        String username = auth.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        return user != null ? user.getId() : null;
    }

    public boolean isOwnedAttempt(int attemptId) {
        Integer currentId = getCurrentUserId();
        if (currentId == null) return false;

        StudentTest st = studentTestRepository.findById(attemptId).orElse(null);
        if (st == null || st.getUser() == null) return false;

        return st.getUser().getId().equals(currentId);
    }

    public boolean isCurrentUserStudent() {
        Integer id = getCurrentUserId();
        if (id == null) return false;
        return userRepository.findById(id)
                .map(u -> u.getRole() == Role.STUDENT)
                .orElse(false);
    }

}
