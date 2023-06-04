package com.course.project.DistantLearning.service;

import com.course.project.DistantLearning.dto.request.CreateOrUpdateDisciplineRequest;
import com.course.project.DistantLearning.dto.response.LectorResponse;
import com.course.project.DistantLearning.dto.response.MessageResponse;
import com.course.project.DistantLearning.models.Discipline;
import com.course.project.DistantLearning.models.Group;
import com.course.project.DistantLearning.models.Lector;
import com.course.project.DistantLearning.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class DisciplineService {

    @Autowired
    DisciplineRepository disciplineRepository;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    GroupService groupService;

    public Optional<Discipline> getDisciplineById(Long idDiscipline) { return disciplineRepository.findById(idDiscipline); }

    public Boolean existsByTitle(String title) { return disciplineRepository.existsByTitle(title); }

    public List<Discipline> getDiscipline(Long idUser) {
        List<Discipline> disciplines = new ArrayList<>();

        List<String> roles = roleService.getUserRoles(userService.getUserByID(idUser));

        if(roles.contains("ROLE_ADMIN")) {
            disciplines.addAll(disciplineRepository.findAll());
        } else if(roles.contains("ROLE_LECTOR") & !roles.contains("ROLE_ADMIN")) {
            disciplines.addAll(userService.getAuthorizeLector().getDisciplineList());
        } else {
            disciplines.addAll(userService.getStudent().getGroup().getDisciplineList());
        }

        return disciplines.stream().sorted(Comparator.comparingLong(Discipline::getId)).toList();
    }

    public void createDiscipline(CreateOrUpdateDisciplineRequest createOrUpdateDisciplineRequest) {
        Discipline discipline = new Discipline();
        List<Lector> lectorList = new ArrayList<>();

        if (!createOrUpdateDisciplineRequest.getLectorResponseList().isEmpty()) {
            for(var lector: createOrUpdateDisciplineRequest.getLectorResponseList()) {
                userService.getLectorById(lector.getId()).ifPresent(lectorList::add);
            }
        }

        discipline.setTitle(createOrUpdateDisciplineRequest.getTitle());

        if (!lectorList.isEmpty()) {
            discipline.setLector(lectorList);
        }
        disciplineRepository.save(discipline);
    }

    public MessageResponse updateDiscipline(Long idDiscipline, CreateOrUpdateDisciplineRequest createOrUpdateDisciplineRequest) {
        Optional<Discipline> disciplineData = disciplineRepository.findById(idDiscipline);

        if (disciplineData.isPresent()) {
            Discipline _discipline = disciplineData.get();
            List<Lector> lectorList = new ArrayList<>();
            List<Group> groupList = new ArrayList<>();

            _discipline.setTitle(createOrUpdateDisciplineRequest.getTitle());

            for(var lector: createOrUpdateDisciplineRequest.getLectorResponseList()) {
                userService.getLectorById(lector.getId()).ifPresent(lectorList::add);
            }

            for(var group: createOrUpdateDisciplineRequest.getGroupList()) {
                groupService.getGroupById(group.getId()).ifPresent(groupList::add);
            }

            _discipline.setGroupList(groupList);
            _discipline.setLector(lectorList);
            disciplineRepository.save(_discipline);
            return new MessageResponse("Update discipline has finished successfully");
        } else {
            return new MessageResponse("Error! discipline lector has stopped");
        }
    }

    public List<Group> getGroupInByDisciplineId(Long idDiscipline) {
        List<Group> groups = new ArrayList<>();
        disciplineRepository.findById(idDiscipline).ifPresent(value -> groups.addAll(value.getGroupList()));
        return groups.stream().sorted(Comparator.comparingLong(Group::getId)).toList();
    }

    public List<Group> getGroupOutByDisciplineId(Long idDiscipline) {
        List<Group> groupInDiscipline = new ArrayList<>();
        disciplineRepository.findById(idDiscipline).ifPresent(value -> groupInDiscipline.addAll(value.getGroupList()));
        List<Group> groupOutDiscipline = groupService.getGroups();
        System.out.println(groupOutDiscipline.size());

        if (!groupInDiscipline.isEmpty()) {
            for (var group: groupInDiscipline) {
                System.out.println(groupOutDiscipline.size());
                groupOutDiscipline.remove(group);

            }
        }

        return groupOutDiscipline.stream().sorted(Comparator.comparingLong(Group::getId)).toList();
    }

    public List<LectorResponse> getLectorsInByDisciplineId(Long idDiscipline) {
        List<Lector> lectors = new ArrayList<>();
        disciplineRepository.findById(idDiscipline).ifPresent(value -> lectors.addAll(value.getLector()));
        List<LectorResponse> listLector = new ArrayList<>();
        if (!lectors.isEmpty()) {
            for(var lector: lectors) {
                var lectorResponse = new LectorResponse();
                lectorResponse.setId(lector.getId());
                lectorResponse.setName(lector.getUser().getFullName());
                lectorResponse.setEmail(lector.getUser().getEmail());

                listLector.add(lectorResponse);
            }
        }
        return listLector.stream().sorted(Comparator.comparingLong(LectorResponse::getId)).toList();
    }

    public List<LectorResponse> getLectorsOutByDisciplineId(Long idDiscipline) {
        List<Lector> lectorInDiscipline = new ArrayList<>();
        disciplineRepository.findById(idDiscipline).ifPresent(value -> lectorInDiscipline.addAll(value.getLector()));
        List<Lector> lectorOutDiscipline = userService.getAllLectors();
        List<LectorResponse> listLector = new ArrayList<>();

        for (var lector: lectorInDiscipline) {
            lectorOutDiscipline.remove(lector);
        }

        if (!lectorOutDiscipline.isEmpty()) {
            for(var lector: lectorOutDiscipline) {
                var lectorResponse = new LectorResponse();
                lectorResponse.setId(lector.getId());
                lectorResponse.setName(lector.getUser().getFullName());
                lectorResponse.setEmail(lector.getUser().getEmail());

                listLector.add(lectorResponse);
            }
        }
        return listLector.stream().sorted(Comparator.comparingLong(LectorResponse::getId)).toList();
    }

    public void deleteDiscipline(Long idDiscipline) {
        disciplineRepository.deleteById(idDiscipline);
    }
}
