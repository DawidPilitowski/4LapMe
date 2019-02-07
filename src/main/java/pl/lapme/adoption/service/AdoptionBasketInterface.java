package pl.lapme.adoption.service;

import pl.lapme.adoption.model.Animal;
import pl.lapme.adoption.model.basket.AdoptionBasket;
import pl.lapme.adoption.model.basket.BasketContentInfo;

import java.util.Map;

public interface AdoptionBasketInterface {


    void addAnimal(Animal animal, int quanity);

    void updateAnimal(Long id, int quantity);

    void removeAnimal(Animal animal);

    boolean isEmpty();

    int getQuantityTotal();

}
