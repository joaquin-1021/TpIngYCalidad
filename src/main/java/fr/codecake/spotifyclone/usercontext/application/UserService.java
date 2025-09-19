package fr.codecake.spotifyclone.usercontext.application;

import fr.codecake.spotifyclone.usercontext.ReadUserDTO;
import fr.codecake.spotifyclone.usercontext.domain.User;
import fr.codecake.spotifyclone.usercontext.mapper.UserMapper;
import fr.codecake.spotifyclone.usercontext.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Sincroniza el usuario local con el IdP.
     * Si existe en DB y el IdP tiene updated_at posterior, actualiza.
     * Si no existe, lo crea.
     */
    @Transactional
    public void syncWithIdp(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        User incoming = mapOauth2AttributesToUser(attributes);

        Optional<User> existingOpt = userRepository.findOneByEmail(incoming.getEmail());
        if (existingOpt.isPresent()) {
            Instant idpModified = extractUpdatedAt(attributes.get("updated_at"));
            if (idpModified != null) {
                Instant dbLastModified = existingOpt.get().getLastModifiedDate();
                if (dbLastModified == null || idpModified.isAfter(dbLastModified)) {
                    log.debug("Updating user {} from IdP (idpModified={}, dbModified={})",
                            incoming.getEmail(), idpModified, dbLastModified);
                    updateUser(incoming);
                } else {
                    log.debug("No update required for user {} (IdP not newer)", incoming.getEmail());
                }
            } else {
                log.debug("No updated_at in IdP attributes; skipping update for {}", incoming.getEmail());
            }
        } else {
            log.info("Creating new user from IdP: {}", incoming.getEmail());
            userRepository.saveAndFlush(incoming);
        }
    }

    /**
     * Devuelve el usuario autenticado como DTO leyendo del SecurityContext.
     * Es tolerante a casos sin autenticación o principal no esperado.
     */
    public ReadUserDTO getAuthenticatedUserFromSecurityContext() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            log.debug("No authenticated user in SecurityContext");
            return null;
        }
        Object principal = auth.getPrincipal();
        if (!(principal instanceof OAuth2User)) {
            log.debug("Principal is not OAuth2User: {}", principal != null ? principal.getClass() : "null");
            return null;
        }

        OAuth2User oAuth2User = (OAuth2User) principal;
        User user = mapOauth2AttributesToUser(oAuth2User.getAttributes());
        return userMapper.readUserDTOToUser(user);
    }

    @Transactional
    public void updateUser(User user) {
        Optional<User> userToUpdateOpt = userRepository.findOneByEmail(user.getEmail());
        if (userToUpdateOpt.isPresent()) {
            User u = userToUpdateOpt.get();
            u.setEmail(user.getEmail());
            u.setImageUrl(user.getImageUrl());
            u.setLastName(user.getLastName());
            u.setFirstName(user.getFirstName());
            userRepository.saveAndFlush(u);
        } else {
            log.warn("updateUser called but user not found by email: {}. Creating it.", user.getEmail());
            userRepository.saveAndFlush(user);
        }
    }

    private User mapOauth2AttributesToUser(Map<String, Object> attributes) {
        User user = new User();
        String sub = String.valueOf(attributes.get("sub"));

        String username = null;
        if (attributes.get("preferred_username") != null) {
            username = String.valueOf(attributes.get("preferred_username")).toLowerCase();
        }

        if (attributes.get("given_name") != null) {
            user.setFirstName(String.valueOf(attributes.get("given_name")));
        } else if (attributes.get("name") != null) {
            user.setFirstName(String.valueOf(attributes.get("name")));
        }

        if (attributes.get("family_name") != null) {
            user.setLastName(String.valueOf(attributes.get("family_name")));
        }

        if (attributes.get("email") != null) {
            user.setEmail(String.valueOf(attributes.get("email")));
        } else if (sub.contains("|") && (username != null && username.contains("@"))) {
            user.setEmail(username);
        } else {
            // Fallback extremo: almacenamos sub (único en el IdP)
            user.setEmail(sub);
        }

        if (attributes.get("picture") != null) {
            user.setImageUrl(String.valueOf(attributes.get("picture")));
        }

        return user;
    }

    public Optional<ReadUserDTO> getByEmail(String email) {
        Optional<User> oneByEmail = userRepository.findOneByEmail(email);
        return oneByEmail.map(userMapper::readUserDTOToUser);
    }

    public boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        if (!auth.isAuthenticated()) return false;
        Object principal = auth.getPrincipal();
        return !(principal instanceof String && "anonymousUser".equals(principal));
    }

    /**
     * Intenta obtener un Instant desde distintos formatos comunes de IdP:
     * - Instant
     * - Number (segundos o milisegundos)
     * - String ISO-8601 o epoch (seg/ ms)
     */
    private Instant extractUpdatedAt(Object raw) {
        if (raw == null) return null;

        try {
            if (raw instanceof Instant) {
                return (Instant) raw;
            }
            if (raw instanceof Number) {
                long v = ((Number) raw).longValue();
                // Heurística: si parece milisegundos (>= 10^12), convertir como millis
                return v >= 1_000_000_000_000L ? Instant.ofEpochMilli(v) : Instant.ofEpochSecond(v);
            }
            String s = String.valueOf(raw).trim();
            // ISO-8601?
            try {
                return Instant.parse(s);
            } catch (Exception ignore) {
                // Epoch en string (segundos o milisegundos)
                long v = Long.parseLong(s);
                return v >= 1_000_000_000_000L ? Instant.ofEpochMilli(v) : Instant.ofEpochSecond(v);
            }
        } catch (Exception e) {
            log.warn("Could not parse updated_at attribute: {} ({})", raw, raw.getClass().getSimpleName(), e);
            return null;
        }
    }
}
