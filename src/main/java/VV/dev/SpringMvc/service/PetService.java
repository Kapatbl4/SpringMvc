package VV.dev.SpringMvc.service;

import VV.dev.SpringMvc.custom_exceptions.user.UserNotFoundException;
import VV.dev.SpringMvc.model.PetDTO;
import VV.dev.SpringMvc.model.UserDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class PetService {
    private Map<Long, PetDTO> pets;
    private Long nextId = 0L;
    private UserService userService;

    public PetService(UserService userService) {
        this.pets = new HashMap<>();
        this.userService = userService;
    }

    public PetDTO savePet(PetDTO petDTO) {
        UserDTO userDTO = userService.findUserById(petDTO.getUserId());
        if (userDTO == null) {
            throw new UserNotFoundException("Невозможно назначить пользователя: пользователь не найден");
        }
        petDTO.setId(++nextId);
        pets.put(petDTO.getId(), petDTO);
        userDTO.getPets().add(petDTO);
        userService.updateUser(userDTO.getId(), userDTO);
        return petDTO;
    }

    public PetDTO updatePet(Long id, PetDTO petDTO) {
        PetDTO currentPetDTO = pets.get(id);
        if (currentPetDTO == null) {
            throw new NoSuchElementException("Pet with id " + id + " not found");
        }
        UserDTO oldUserDTO = userService.findUserById(currentPetDTO.getUserId());
        UserDTO newUserDTO = userService.findUserById(petDTO.getUserId());
        if (newUserDTO == null) {
            throw new UserNotFoundException("Невозможно назначить пользователя: пользователь не найден");
        }
        petDTO.setId(id);
        pets.put(id, petDTO);
        if (!oldUserDTO.equals(newUserDTO)) {
            oldUserDTO.getPets().remove(currentPetDTO);
            newUserDTO.getPets().add(petDTO);
            userService.updateUser(oldUserDTO.getId(), oldUserDTO);
            userService.updateUser(newUserDTO.getId(), newUserDTO);
        }
        return petDTO;
    }

    public PetDTO findPetById(Long id) {
        PetDTO petDTO = pets.get(id);
        if (petDTO == null) {
            throw new NoSuchElementException("Pet with id " + id + " not found");
        }
        return petDTO;
    }

    public void deletePet(Long id) {
        PetDTO currentPetDTO = pets.get(id);
        if (currentPetDTO == null) {
            throw new NoSuchElementException("Pet with id " + id + " not found");
        }
        UserDTO userDTO = userService.findUserById(currentPetDTO.getUserId());
        userDTO.getPets().remove(currentPetDTO);
        userService.updateUser(userDTO.getId(), userDTO);
        pets.remove(id);
    }

    public void clear() {
        pets.clear();
    }
}
