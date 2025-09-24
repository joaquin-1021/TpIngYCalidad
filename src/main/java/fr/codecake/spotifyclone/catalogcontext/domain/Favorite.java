package fr.codecake.spotifyclone.catalogcontext.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity
@Table(
        name = "favorite_song",
        indexes = {
                @Index(name = "idx_favorite_user_email", columnList = "user_email"),
                @Index(name = "idx_favorite_song_public_id", columnList = "song_public_id")
        }
)
public class Favorite implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private FavoriteId id;

    // Columnas mapeadas desde el embeddable (para nombres explícitos)
    @Column(name = "song_public_id", nullable = false, insertable = false, updatable = false)
    private java.util.UUID songPublicIdShadow;

    @Column(name = "user_email", nullable = false, length = 255, insertable = false, updatable = false)
    private String userEmailShadow;

    public Favorite() { }

    public Favorite(FavoriteId id) {
        this.id = id;
        syncShadows();
    }

    public static Favorite of(java.util.UUID songPublicId, String userEmail) {
        Favorite fav = new Favorite(new FavoriteId(songPublicId, userEmail));
        return fav;
    }

    public FavoriteId getId() { return id; }
    public void setId(FavoriteId id) {
        this.id = id;
        syncShadows();
    }

    // Helpers convenientes
    public java.util.UUID getSongPublicId() { return id != null ? id.getSongPublicId() : null; }
    public String getUserEmail() { return id != null ? id.getUserEmail() : null; }
    public void setSongPublicId(java.util.UUID songPublicId) {
        ensureId();
        id.setSongPublicId(songPublicId);
        syncShadows();
    }
    public void setUserEmail(String userEmail) {
        ensureId();
        id.setUserEmail(userEmail);
        syncShadows();
    }

    private void ensureId() {
        if (this.id == null) this.id = new FavoriteId();
    }

    private void syncShadows() {
        this.songPublicIdShadow = getSongPublicId();
        this.userEmailShadow = getUserEmail();
    }
}
