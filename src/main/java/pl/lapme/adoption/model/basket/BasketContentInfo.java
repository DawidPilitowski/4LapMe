package pl.lapme.adoption.model.basket;

import lombok.Data;
import pl.lapme.adoption.model.Animal;

@Data
public class BasketContentInfo {

    private Animal animalInfo;
    private int quantity;

    // nowy koszyk -> ilosc jakiegokolwiek zwierza = 0
}
