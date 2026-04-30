package iss.validation;

import iss.exceptions.ValidationException;
import iss.model.Player;

public class PlayerValidator implements IValidator<Long, Player>{
    @Override
    public void validate(Player player) {
        if (player == null) {
            throw new ValidationException("Player is null");
        }

        if (player.getFirstName() == null) {
            throw new ValidationException("Player first name is null");
        }

        if (player.getLastName() == null) {
            throw new ValidationException("Player last name is null");
        }

        if (player.getBirthDate() == null) {
            throw new ValidationException("Player birth date is null");
        }

        if (player.getClub() == null) {
            throw new ValidationException("Player club is null");
        }

        if (player.getPositionList() == null) {
            throw new ValidationException("Player position list is null");
        }

        if (player.getNationality()  == null) {
            throw new ValidationException("Player nationality is null");
        }

        if (player.getFirstName().length() < 2) {
            throw new ValidationException("Player first name should be longer at least 2 characters");
        }

        if (player.getLastName().length() < 2) {
            throw new ValidationException("Player last name should be longer at least 2 characters");
        }

        if (player.getNationality().length() < 3) {
            throw new ValidationException("Player nationality should be longer at least 3 characters");
        }

        if (player.getPositionList().isEmpty()) {
            throw new ValidationException("Player should have at least one position");
        }

        if (player.getShirtNumber() < 1 || player.getShirtNumber() > 99) {
            throw new ValidationException("Shirt number should be between 1 and 99");
        }
    }
}
