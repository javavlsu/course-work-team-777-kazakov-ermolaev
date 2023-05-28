package com.course.project.DistantLearning.service;

import com.course.project.DistantLearning.dto.request.CreateOrUpdateDisciplineRequest;
import com.course.project.DistantLearning.dto.response.LectorResponse;
import com.course.project.DistantLearning.dto.response.MessageResponse;
import com.course.project.DistantLearning.models.Discipline;
import com.course.project.DistantLearning.models.Group;
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
    private DisciplineRepository disciplineRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private GroupService groupService;



    public String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public List<Discipline> getAllDiscipline() {
        return disciplineRepository.findAll();
    }

    public Optional<Discipline> getDisciplineById(Long idDiscipline) { return disciplineRepository.findById(idDiscipline); }

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

        return disciplines.stream().sorted((a1, b1) -> Long.compare(a1.getId(), b1.getId())).toList();
    }

    public void createDiscipline(CreateOrUpdateDisciplineRequest createOrUpdateDisciplineRequest) {
        Discipline discipline = new Discipline();
        List<Lector> lectorList = new ArrayList<>();

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

    public MessageResponse updateDiscipline(Long idDiscipline, CreateOrUpdateDisciplineRequest createOrUpdateDisciplineRequest) {
        Optional<Discipline> disciplineData = disciplineRepository.findById(idDiscipline);

        if (disciplineData.isPresent()) {
            Discipline _discipline = disciplineData.get();
            List<Lector> lectorList = new ArrayList<>();
            List<Group> groupList = new ArrayList<>();

            _discipline.setTitle(createOrUpdateDisciplineRequest.getTitle());

            if (!createOrUpdateDisciplineRequest.getLectorResponseList().isEmpty()) {
                for(var lector: createOrUpdateDisciplineRequest.getLectorResponseList()) {
                    lectorList.add(userService.getLectorById(lector.getId()).get());
                }
                _discipline.setLector(lectorList);
            }

            if (!createOrUpdateDisciplineRequest.getGroupList().isEmpty()) {
                for(var group: createOrUpdateDisciplineRequest.getGroupList()) {
                    groupList.add(groupService.getGroupById(group.getId()).get());
                }
                _discipline.setGroupList(groupList);
            }

            disciplineRepository.save(_discipline);
            return new MessageResponse("Update discipline has finished successfully");
        } else {
            return new MessageResponse("Error! discipline lector has stopped");
        }
    }

    public List<Group> getGroupInByDisciplineId(Long idDiscipline) {
        return disciplineRepository.findById(idDiscipline).get().getGroupList()
                .stream().sorted((a1, b1) -> Long.compare(a1.getId(), b1.getId())).toList();
    }

    public List<Group> getGroupOutByDisciplineId(Long idDiscipline) {
        List<Group> groupInDiscipline = disciplineRepository.findById(idDiscipline).get().getGroupList();
        List<Group> groupOutDiscipline = groupService.getGroups();

        if (!groupInDiscipline.isEmpty()) {
            for (var group: groupInDiscipline) {
                groupOutDiscipline.remove(group);
            }
        }

        return groupOutDiscipline.stream().sorted((a1, b1) -> Long.compare(a1.getId(), b1.getId())).toList();
    }

    public List<LectorResponse> getLectorsInByDisciplineId(Long idDiscipline) {
        var lectors = disciplineRepository.findById(idDiscipline).get().getLector();
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
        return listLector.stream().sorted((a1, b1) -> Long.compare(a1.getId(), b1.getId())).toList();
    }

    public List<LectorResponse> getLectorsOutByDisciplineId(Long idDiscipline) {
        List<Lector> lectorInDiscipline = disciplineRepository.findById(idDiscipline).get().getLector();
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
        return listLector.stream().sorted((a1, b1) -> Long.compare(a1.getId(), b1.getId())).toList();
    }

    public void deleteDiscipline(Long idDiscipline) {
        disciplineRepository.deleteById(idDiscipline);
    }
}
