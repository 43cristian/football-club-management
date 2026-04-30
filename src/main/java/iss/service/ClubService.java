package iss.service;

import iss.model.Club;
import iss.model.User;
import iss.repository.ClubRepository;
import iss.validation.ClubValidator;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

public class ClubService {
    private ClubRepository clubRepository;
    private ClubValidator clubValidator;

    public ClubService(ClubRepository clubRepository, ClubValidator clubValidator) {
        this.clubRepository = clubRepository;
        this.clubValidator = clubValidator;
    }

    public Club addClub(String name, String nation, String logoPath, String primaryColor, String secondaryColor) {
        Club club = new Club(null, name, nation, logoPath, primaryColor, secondaryColor);
        clubValidator.validate(club);
        return clubRepository.save(club);
    }

    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }

    public Club getClubById(long id) {
        Optional<Club> club = clubRepository.findById(id);

        if (club.isPresent()) {
            return club.get();
        }

        throw new EntityNotFoundException("Club not found");
    }
}
