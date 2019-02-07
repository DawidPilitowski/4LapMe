package pl.lapme.adoption.components;

import lombok.Getter;
import org.springframework.stereotype.Component;
import pl.lapme.adoption.model.Category;
import pl.lapme.adoption.model.MainCategory;
import pl.lapme.adoption.model.Subcategory;
import pl.lapme.adoption.service.CategoryService;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Component
public class CategoryComponent {

    private CategoryService categoryService;
    private Map<MainCategory, List<Subcategory>> categoryMap = new HashMap<>();
    private Map<String, Map<String, Category>> categoryMapHashMap = new HashMap<>();

    private void createCategory(String mainCategoryName, String... subcategoriesNames) {
        List<Subcategory> subcategories = new ArrayList<>();
        for (String nameOfSub : subcategoriesNames) {
            subcategories.add(new Subcategory(null, nameOfSub));
        }

        MainCategory mainCategory = new MainCategory(null, mainCategoryName);

        for (Subcategory subcategory : subcategories) {
            Category category = new Category(null, mainCategory, subcategory);
            category = categoryService.checkAndSaveIfNotExists(category);

            addToHashMap(mainCategory, subcategory, category);
        }
        categoryMap.put(mainCategory, subcategories);
    }

    private void addToHashMap(MainCategory mainCategory, Subcategory subcategory, Category category) {
        Map<String, Category> catMap = categoryMapHashMap.get(mainCategory.getNameCategory());
        if (catMap == null) {
            catMap = new HashMap<>();
        }
        catMap.put(subcategory.getName(), category);
        categoryMapHashMap.put(mainCategory.getNameCategory(), catMap);
    }

    public List<Category> betterFind(String main, String sub) {
        if (main.isEmpty()) {
            List<Category> list = categoryMapHashMap.values()
                    .stream()
                    .flatMap(map -> map.values().stream())
                    .collect(Collectors.toList());
            return list;
        }
        if (sub.isEmpty()) {
            return find(main);
        }
        return Arrays.asList(categoryMapHashMap.get(main).get(sub));
    }

    public Category find(String main, String sub) {
        return categoryMapHashMap.get(main).get(sub);
    }

    public List<Category> find(String main) {
        return categoryMapHashMap.get(main).values().stream().collect(Collectors.toList());
    }

    public CategoryComponent(CategoryService categoryService) {
        this.categoryService = categoryService;

        createCategory("Aktualności",
                "Psy bez rodowodu",
                "Psy ze schronisk",
                "Akita",
                "Amstaff",
                "Basset Hound",
                "Beagle",
                "Bernardyn",
                "Berneński Pies Pasterski",
                "Bokser",
                "Buldog",
                "Chihuahua",
                "Cocker Spaniel",
                "Doberman",
                "Golden Retriever",
                "Husky",
                "Jamnik",
                "Labrador",
                "Maltańczyk",
                "Mops",
                "Owczarek",
                "Pit Bull",
                "Rottweiler",
                "Sznaucer",
                "Szpic miniaturowy",
                "York",
                "Pozostałe rasy"
                );

        createCategory("Wolontariat",
                "region:",
                "-pomorskie",
                "-mazowieckie",
                "-śląskie",
                "-małopolskie",
                "-warmińsko-mazurskie"
                );

        createCategory("Możesz pomóc",
                "1%",
                "domy tymczasowe",
                "dary rzeczowe",
                "spacery"
                );

        createCategory("Adopcja",
                "adopcja kotów",
                "adopcja psów",
                "adopcja-pozostałe zwierzęta",
                "wirtualna adopcja ",
                "warunki adopcji",
                "szczęśliwe zakończenia"
                );
        createCategory("Hodowcy",
                "-psy",
                "-koty",
                "-małpki",
                "-gady",
                "-inne"
                );
        createCategory("Kontakt",
                "napisz do nas",
                "kontakt dla hodowców",
                "kontakt dla schronisk"
                );
//        createCategory("Adopcja",
//                "Psy bez rodowodu",
//                "Psy ze schronisk",
//                "Akita",
//                "Amstaff",
//                "Basset Hound",
//                "Beagle",
//                "Bernardyn",
//                "Berneński Pies Pasterski",
//                "Bokser",
//                "Buldog",
//                "Chihuahua",
//                "Cocker Spaniel",
//                "Doberman",
//                "Golden Retriever",
//                "Husky",
//                "Jamnik",
//                "Labrador",
//                "Maltańczyk",
//                "Mops",
//                "Owczarek",
//                "Pit Bull",
//                "Rottweiler",
//                "Sznaucer",
//                "Szpic miniaturowy",
//                "York",
//                "Pozostałe rasy"
//        );
//
//        createCategory("Koty",
//                "Koty bez rodowodu",
//                "Koty ze schronisk",
//                "Bengalski",
//                "Brytyjski",
//                "Burmański",
//                "Devon Rex",
//                "Maine Coon",
//                "Norweski",
//                "Perski",
//                "Ragdoll",
//                "Rosyjski",
//                "Sfinks",
//                "Syberyjski",
//                "Syjamski",
//                "Pozostałe rasy"
//        );
//
//        createCategory("Aligatory",
//                "Krokodyl",
//                "Forfiter",
//                "Inne"
//        );
//
//        createCategory("Duże",
//                "Słoń",
//                "Hipopotam",
//                "Gazela",
//                "Żyrafa",
//                "Wielbłąd",
//                "Zebra",
//                "Niedźwiedź"
//        );
//        createCategory("Duże koty",
//                "Lew",
//                "Tygrys",
//                "Puma",
//                "Gepard",
//                "Jaguar",
//                "Ryś"
//        );
//        createCategory("Inne",
//                "Lis",
//                "Królik",
//                "Wiewiórka",
//                "Świnka morska",
//                "Wąż",
//                "Pająk",
//                "Ryby",
//                "Szop pracz",
//                "Bóbr",
//                "Lemur",
//                "Małpa",
//                "Żaba",
//                "Bazyliszek",
//                "Kameleon",
//                "Legwan",
//                "Żółw"
//        );
    }
}


