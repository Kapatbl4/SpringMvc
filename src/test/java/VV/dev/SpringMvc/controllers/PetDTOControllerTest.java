package VV.dev.SpringMvc.controllers;

import VV.dev.SpringMvc.model.PetDTO;
import VV.dev.SpringMvc.model.UserDTO;
import VV.dev.SpringMvc.service.PetService;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PetDTOControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PetService petService;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        petService.clear();
        userService.clear();
        var user1 = new UserDTO(
                "Victor",
                "vvi93@yandex.ru",
                32
        );

        var user2 = new UserDTO(
                "Dmitriy",
                "vdi96@yandex.ru",
                30
        );
        userService.saveUser(user1);
        userService.saveUser(user2);
    }


    @Test
    void createPet() throws Exception {
        var pet = new PetDTO(
                "Barsik",
                1L
        );

        String petJson = objectMapper.writeValueAsString(pet);

        String createPetJson = mockMvc.perform(post("/api/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(petJson)
                ).andExpect(status().is(201))
                .andReturn()
                .getResponse()
                .getContentAsString();

        PetDTO petDTOResponse = objectMapper.readValue(createPetJson, PetDTO.class);

        Assertions.assertEquals(pet.getName(), petDTOResponse.getName());
        Assertions.assertNotNull(petDTOResponse.getId());
    }

    @Test
    void getPet() throws Exception {
        var pet = new PetDTO(
                "Barsik",
                1L
        );
        pet = petService.savePet(pet);

        String foundPetJson = mockMvc.perform(get("/api/pets/{id}", pet.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PetDTO foundPetDTO = objectMapper.readValue(foundPetJson, PetDTO.class);

        org.assertj.core.api.Assertions.assertThat(pet)
                .usingRecursiveComparison()
                .isEqualTo(foundPetDTO);
    }

    @Test
    void updatePet() throws Exception {
        var pet = new PetDTO(
                "Barsik",
                1L
        );
        pet = petService.savePet(pet);

        var petToUpdate = new PetDTO(
                "Murzik",
                2L
        );

        String petToUpdateJson = objectMapper.writeValueAsString(petToUpdate);

        String updatedPetJson = mockMvc.perform(put("/api/pets/{id}", pet.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(petToUpdateJson)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PetDTO updatedPetDTO = objectMapper.readValue(updatedPetJson, PetDTO.class);

        Assertions.assertEquals(updatedPetDTO.getName(), petToUpdate.getName());
        Assertions.assertEquals(updatedPetDTO.getUserId(), petToUpdate.getUserId());
        Assertions.assertEquals(pet.getId(), updatedPetDTO.getId());

        org.assertj.core.api.Assertions.assertThat(userService.findUserById(1L).getPets().isEmpty());
        org.assertj.core.api.Assertions.assertThat(!userService.findUserById(2L).getPets().isEmpty());
    }

    @Test
    void deletePet() throws Exception {
        var pet = new PetDTO(
                "Barsik",
                1L
        );
        pet = petService.savePet(pet);

        mockMvc.perform(delete("/api/pets/{id}", pet.getId()))
                .andExpect(status().isNoContent());
        Assertions.assertTrue(userService.findUserById(1L).getPets().isEmpty());
    }
}