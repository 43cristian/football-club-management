package iss.service;

import iss.model.Club;
import iss.model.Player;
import iss.model.Position;
import iss.repository.PlayerRepository;
import iss.validation.PlayerValidator;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PlayerService {
    private PlayerRepository playerRepository;
    private PlayerValidator playerValidator;

    public PlayerService(PlayerRepository playerRepository, PlayerValidator playerValidator) {
        this.playerRepository = playerRepository;
        this.playerValidator = playerValidator;
    }

    public Player addPlayer(String firstName, String lastName, LocalDate birthDate, int shirtNumber, List<Position> positionList, String nationality, Club club) {
        Player player = new Player(null, firstName, lastName, birthDate, shirtNumber, positionList, nationality, club);
        playerValidator.validate(player);
        return playerRepository.save(player);
    }

    public Player getPlayerById(Long id) {
        Optional<Player> player = playerRepository.findById(id);

        if (player.isPresent()) {
            return player.get();
        }

        throw new EntityNotFoundException("Player not found");
    }

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    public Player updatePlayer(Player player) {
        playerValidator.validate(player);
        return playerRepository.update(player);
    }

    public List<Player> getPlayersByClub(Club club) {
        return playerRepository.findByClub(club);
    }
}
