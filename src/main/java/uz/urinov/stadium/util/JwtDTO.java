package uz.urinov.stadium.util;

import lombok.Getter;
import lombok.Setter;
import uz.urinov.stadium.Prifile.ProfileRole;


@Setter
@Getter
public class JwtDTO {
    private String id;
    private String username;
    private ProfileRole role;

    public JwtDTO(String id) {
        this.id = id;
    }

    public JwtDTO(String id, ProfileRole role) {
        this.id = id;
        this.role = role;
    }

    public JwtDTO(String id, String username, ProfileRole role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }
}
