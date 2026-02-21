package VV.dev.SpringMvc.controllers;

import VV.dev.SpringMvc.model.UserDTO;
import VV.dev.SpringMvc.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserDTOControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setup() {
        userService.clear();
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createUser() throws Exception {
        var user = new UserDTO(
                "Victor",
                "vvi93@yandex.ru",
                32
        );

        String userJson = objectMapper.writeValueAsString(user);

        String createdUserJson = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
                )
                .andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDTO userDTOResponse = objectMapper.readValue(createdUserJson, UserDTO.class);
        Assertions.assertEquals(user.getName(), userDTOResponse.getName());
        Assertions.assertEquals(user.getEmail(), userDTOResponse.getEmail());
        Assertions.assertNotNull(userDTOResponse.getId());
    }

    @Test
    void getUserById() throws Exception {
        var user = new UserDTO(
                "Victor",
                "vvi93@yandex.ru",
                32
        );
        user = userService.saveUser(user);

        String foundUserJson = mockMvc.perform(get("/api/users/{id}", user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDTO foundUserDTO = objectMapper.readValue(foundUserJson, UserDTO.class);

        org.assertj.core.api.Assertions.assertThat(user)
                .usingRecursiveComparison()
                .isEqualTo(foundUserDTO);
    }

    @Test
    void updateUser() throws Exception {
        var user = new UserDTO(
                "Victor",
                "vvi93@yandex.ru",
                32
        );
        user = userService.saveUser(user);

        UserDTO userDTOToUpdate = new UserDTO(
                "Victor",
                "vvi93@yandex.com",
                33
        );

        String userToUpdateJson = objectMapper.writeValueAsString(userDTOToUpdate);


        String updatedUserJson = mockMvc.perform(put("/api/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userToUpdateJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDTO updatedUserDTO = objectMapper.readValue(updatedUserJson, UserDTO.class);

        Assertions.assertEquals(updatedUserDTO.getEmail(), userDTOToUpdate.getEmail());
        Assertions.assertEquals(updatedUserDTO.getAge(), userDTOToUpdate.getAge());
        Assertions.assertEquals(updatedUserDTO.getId(), user.getId());
    }

    @Test
    void deleteUser() throws Exception {
        var user = new UserDTO(
                "Victor",
                "vvi93@yandex.ru",
                32
        );
        user = userService.saveUser(user);

        var user1 = new UserDTO(
                "Dmitriy",
                "vdi96@yandex.ru",
                30
        );
        user1 = userService.saveUser(user1);

        mockMvc.perform(delete("/api/users/{id}", user.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/users/{id}", user.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void userNotFound() throws Exception {
        mockMvc.perform(get("/api/users/{id}", Integer.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void createNotValidUser() throws Exception {
        var user = new UserDTO(
                "",
                "vvi93@yandex.ru",
                32
        );
        String userJson = objectMapper.writeValueAsString(user);
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson)
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteUserNotExist() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", Integer.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

}