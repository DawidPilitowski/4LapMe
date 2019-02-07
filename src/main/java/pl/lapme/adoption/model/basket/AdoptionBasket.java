package pl.lapme.adoption.model.basket;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import pl.lapme.adoption.model.Animal;
import pl.lapme.adoption.model.User;
import pl.lapme.adoption.service.AdoptionBasketInterface;

import java.util.ArrayList;
import java.util.List;


@Component
@Scope(scopeName = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
@Data
public class AdoptionBasket implements AdoptionBasketInterface {

    private int orderNum;
    private User userInfo;
    private final List<BasketContentInfo> basketContents = new ArrayList<>();

    private BasketContentInfo findAnimalById(Long id) {
        for (BasketContentInfo content : this.basketContents) {
            if (content.getAnimalInfo().getId().equals(id)) {
                return content;
            }
        }
        return null;
    }


    @Override
    public void addAnimal(Animal animal, int quanity) {
        BasketContentInfo content = this.findAnimalById(animal.getId());

        if (content == null) {
            content = new BasketContentInfo();
            content.setQuantity(0);
            content.setAnimalInfo(animal);
            this.basketContents.add(content);
        }
        int newQuantity = content.getQuantity() + quanity;
        if (newQuantity <= 0) {
            this.basketContents.remove(content);
        } else {
            content.setQuantity(newQuantity);
        }
    }

    @Override
    public void updateAnimal(Long id, int quantity){
        BasketContentInfo content = this.findAnimalById(id);

        if(content != null) {
            if(quantity <= 0){
                this.basketContents.remove(content);
            } else {
                content.setQuantity(quantity);
            }
        }
    }

    @Override
    public void removeAnimal(Animal animal) {
        BasketContentInfo content = this.findAnimalById(animal.getId());
        if (content != null) {
            this.basketContents.remove(content);
        }
    }

    @Override
    public boolean isEmpty() {
        return this.basketContents.isEmpty();
    }

    // calkowita liczba zwierzat
    @Override
    public int getQuantityTotal() {
        int quantity = 0;
        for (BasketContentInfo content : this.basketContents) {
            quantity += content.getQuantity();
        }
        return quantity;
    }


}


