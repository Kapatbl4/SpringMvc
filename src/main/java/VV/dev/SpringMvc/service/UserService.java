package VV.dev.SpringMvc.service;

import VV.dev.SpringMvc.exceptions.custom_exceptions.UserEmailAlreadyExistsException;
import VV.dev.SpringMvc.exceptions.custom_exceptions.UserHasPetException;
import VV.dev.SpringMvc.model.UserDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {
    private final Map<Long, UserDTO> users;
    private final AtomicLong nextId = new AtomicLong(0L);

    public UserService() {
        this.users = new HashMap<>();
    }

    public UserDTO saveUser(UserDTO userDTO) {
        if (findUserByEmail(userDTO.getEmail()).isPresent()) {
            throw new UserEmailAlreadyExistsException("User with email " + userDTO.getEmail() + " already exists");
        }
        userDTO.setId(nextId.incrementAndGet());
        if (userDTO.getPets() == null) {
            userDTO.setPets(new ArrayList<>());
        }
        users.put(userDTO.getId(), userDTO);
        return userDTO;
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        if (users.get(id) == null) {
            throw new NoSuchElementException("User with id " + id + " does not exists");
        }
        userDTO.setId(id);
        users.put(id, userDTO);
        return userDTO;
    }

    public UserDTO findUserById(Long id) {
        UserDTO userDTO = users.get(id);
        if (userDTO == null) {
            throw new NoSuchElementException("User does not exists");
        }
        return userDTO;
    }

    public void deleteUserById(Long id) {
        UserDTO userDTO = users.get(id);
        if (userDTO == null) {
            throw new NoSuchElementException("User does not exists");
        }
        if (!userDTO.getPets().isEmpty()) {
            throw new UserHasPetException("У пользователя есть питомцы");
        }

        users.remove(id);
    }

    public Optional<UserDTO> findUserByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    public List<UserDTO> getAllUsers() {
        return users.values().stream().toList();
    }

    public void clear() {
        users.clear();
    }

}
