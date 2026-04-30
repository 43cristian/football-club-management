package iss.validation;

import iss.exceptions.ValidationException;
import iss.model.Club;

import java.nio.file.Files;
import java.nio.file.Paths;

public class ClubValidator implements IValidator<Long, Club> {
    @Override
    public void validate(Club club) {
        if (club == null){
            throw new ValidationException("Club is null");
        }

        if (club.getName() == null) {
            throw new ValidationException("Name is  null");
        }

        if (club.getLogoPath() == null) {
            throw new ValidationException("Logo path is null");
        }

        if (club.getNation() == null) {
            throw new ValidationException("Nation is null");
        }

        if (club.getPrimaryColor()  == null) {
            throw new ValidationException("Primary color is null");
        }

        if (club.getSecondaryColor()  == null) {
            throw new ValidationException("Secondary color is null");
        }

        if (club.getPrimaryColor().length() != 6) {
            throw new ValidationException("Primary color length should be 6");
        }

        if  (club.getSecondaryColor().length() != 6) {
            throw new ValidationException("Secondary color length should be 6");
        }

        if (club.getNation().length() < 3) {
            throw new ValidationException("Nation length should be at least 3");
        }

        if (club.getName().length() < 3) {
            throw new ValidationException("Name length should be at least 3");
        }

        if ("".equals(club.getLogoPath())) {
            throw new ValidationException("Logo path is empty");
        }

        validateHexColor(club.getPrimaryColor(), "Primary color");
        validateHexColor(club.getSecondaryColor(), "Secondary color");

        validatePath(club.getLogoPath());
    }

    private void validateHexColor(String color, String colorName) {
        String regexHex = "^#?[0-9a-fA-F]{6}$";
        if (!color.matches(regexHex)) {
            throw new ValidationException(colorName + " is not a valid hex color");
        }
    }

    private void validatePath(String path) {
        if (!Files.exists(Paths.get(path))) {
            throw new ValidationException("Invalid logo path; File not found");
        }
    }
}
