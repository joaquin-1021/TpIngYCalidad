package fr.codecake.spotifyclone.catalogcontext.domain;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class FavoriteId implements Serializable {
    private static final long serialVersionUID = 1L;

    // Id de la canción (público)
    private UUID songPublicId;

    // Email del usuario
    private String userEmail;

    public FavoriteId() { }

    public FavoriteId(UUID songPublicId, String userEmail) {
        this.songPublicId = songPublicId;
        this.userEmail = userEmail;
    }

    public UUID getSongPublicId() { return songPublicId; }
    public void setSongPublicId(UUID songPublicId) { this.songPublicId = songPublicId; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FavoriteId)) return false;
        FavoriteId that = (FavoriteId) o;
        return Objects.equals(songPublicId, that.songPublicId)
                && Objects.equals(userEmail, that.userEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songPublicId, userEmail);
    }
}
