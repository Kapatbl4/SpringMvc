package VV.dev.SpringMvc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class PetDTO {
    private Long id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    private Long userId;

    public PetDTO(String name, Long userId) {
        this.name = name;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PetDTO petDTO)) return false;
        return Objects.equals(id, petDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
