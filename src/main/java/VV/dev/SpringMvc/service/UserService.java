package VV.dev.SpringMvc.service;

import VV.dev.SpringMvc.custom_exceptions.user.UserEmailAlreadyExistsException;
import VV.dev.SpringMvc.custom_exceptions.user.UserHasPetException;
import VV.dev.SpringMvc.model.UserDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class UserService {
    private Map<Long, UserDTO> users;
    private Long nextId = 0L;

    public UserService() {
        this.users = new HashMap<>();
    }

    public UserDTO saveUser(UserDTO userDTO) {
        if (findUserByEmail(userDTO.getEmail()) != null) {
            throw new UserEmailAlreadyExistsException("User with email " + userDTO.getEmail() + " already exists");
        }
        userDTO.setId(++nextId);
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

    public UserDTO findUserByEmail(String email) {
        return users.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public List<UserDTO> getAllUsers() {
        return users.values().stream().toList();
    }

    public void clear() {
        users.clear();
    }

}
