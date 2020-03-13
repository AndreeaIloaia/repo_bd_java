package repo.validator;

import domain.Bilet;

public class ValidatorBilet implements Validator<Bilet> {
    @Override
    public void validate(Bilet bilet) {
        String msg = "";
        if(bilet.getId_casier() < 0){
            msg += "ID-ul casierului trebuie sa fie pozitiv";
        }
        if(bilet.getId_meci() < 0){
            msg += "ID-ul meciului trebuie sa fie pozitiv";
        }
        if(bilet.getLocuri() <= 0){
            msg += "Nr de locuri trebuie sa fie pozitiv";
        }
        if(bilet.getNume().equals("")){
            msg += "Numele nu poate fi vid";
        }
        if(msg.length() > 0){
            throw new ValidationException(msg);
        }
    }
}