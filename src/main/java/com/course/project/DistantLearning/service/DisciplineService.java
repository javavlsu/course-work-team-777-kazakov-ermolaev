package com.course.project.DistantLearning.service;

import com.course.project.DistantLearning.dto.request.CreateOrUpdateDisciplineRequest;
import com.course.project.DistantLearning.models.Discipline;
import com.course.project.DistantLearning.models.Lector;
import com.course.project.DistantLearning.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DisciplineService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DisciplineRepository disciplineRepository;

    public String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return currentPrincipalName;
    }

    public List<Discipline> getAllDiscipline() {
        return disciplineRepository.findAll();
    }

    public List<Discipline> getDiscipline(Long idUser) {
        List<Discipline> disciplines = new ArrayList<>();

        List<String> roles = roleService.getUserRoles(userService.getUserByID(idUser));

        if(roles.contains("ROLE_ADMIN")) {
            disciplineRepository.findAll().forEach(disciplines::add);
        } else if(roles.contains("ROLE_LECTOR") & !roles.contains("ROLE_ADMIN")) {
            userService.getAuthorizeLector().getDisciplineList().forEach(disciplines::add);
        } else {
            userService.getStudent().getGroup().getDisciplineList().forEach(disciplines::add);
        }

        return disciplines;
    }

    public void createDiscipline(CreateOrUpdateDisciplineRequest createOrUpdateDisciplineRequest) {
        List<Lector> lectorList = new ArrayList<>();
        Discipline discipline = new Discipline();

        if (!createOrUpdateDisciplineRequest.getLectorResponseList().isEmpty()) {
            for(var lector: createOrUpdateDisciplineRequest.getLectorResponseList()) {
                lectorList.add(userService.getLectorById(lector.getId()).get());
            }
        }

        discipline.setTitle(createOrUpdateDisciplineRequest.getTitle());

        if (!lectorList.isEmpty()) {
            discipline.setLector(lectorList);
        }
        disciplineRepository.save(discipline);
    }


//    public void updateDiscipline(Long idDiscipline, CreateOrUpdateDisciplineRequest createOrUpdateDisciplineRequest) {
//        Optional<Discipline> discipline = disciplineRepository.findById(idDiscipline);
//        if (discipline.isPresent()) {
//            Discipline _discipline = discipline.get();
//            _discipline.setTitle(createOrUpdateDisciplineRequest.getTitle());
//            List<Lector> listLector = new ArrayList<>();
//
//            _discipline.setLector(createOrUpdateDisciplineRequest.getTitle());
//        }
//    }
}
