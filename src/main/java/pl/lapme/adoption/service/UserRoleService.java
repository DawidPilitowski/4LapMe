package pl.lapme.adoption.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.lapme.adoption.model.AppUserRole;
import pl.lapme.adoption.model.dto.ChooseRoleDto;
import pl.lapme.adoption.repository.UserRoleRopository;

import java.util.List;
import java.util.Optional;

@Service
public class UserRoleService {
    private final static String[] AVAILABLE_ROLES = {"ROLE_ADMIN", "ROLE_SHELTER_USER", "ROLE_BREEDER_USER"};
    @Autowired
    private UserRoleRopository userRoleRopository;

    public List<AppUserRole> getAll() {
        return userRoleRopository.findAll();
    }

    public ChooseRoleDto getRoleToSelect() {
        ChooseRoleDto chooseRoleDto = new ChooseRoleDto();

        Optional<AppUserRole> roleAdmin = userRoleRopository.findByName("ROLE_ADMIN");
        if (roleAdmin.isPresent()) {
            AppUserRole role = roleAdmin.get();
            chooseRoleDto.setRole_admin("");
            chooseRoleDto.setRole_admin_id(role.getId());
            chooseRoleDto.setRole_admin_name("Admin");
        }
        Optional<AppUserRole> roleShelter = userRoleRopository.findByName("ROLE_SHELTER_USER");
        if (roleShelter.isPresent()) {
            AppUserRole role = roleShelter.get();
            chooseRoleDto.setRole_shelter("");
            chooseRoleDto.setRole_shelter_id(role.getId());
            chooseRoleDto.setRole_shelter_name("Shelter");
        }
        Optional<AppUserRole> roleBreeder = userRoleRopository.findByName("ROLE_BREEDER_USER");
        if (roleBreeder.isPresent()) {
            AppUserRole role = roleBreeder.get();
            chooseRoleDto.setRole_breeder("");
            chooseRoleDto.setRole_breeder_id(role.getId());
            chooseRoleDto.setRole_breeder_name("Breeder");
        }
        return chooseRoleDto;
    }
}
