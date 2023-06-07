package com.course.project.DistantLearning.service;

import com.course.project.DistantLearning.dto.request.CreateOrUpdateDisciplineRequest;
import com.course.project.DistantLearning.dto.response.LectorResponse;
import com.course.project.DistantLearning.dto.response.StudentResponse;
import com.course.project.DistantLearning.models.*;
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

    @Autowired
    ScoreRepository scoreRepository;

    @Autowired
    StudentRepository studentRepository;

    public Optional<Discipline> getDisciplineById(Long idDiscipline) { return disciplineRepository.findById(idDiscipline); }

    public Boolean existsByTitle(String title) { return disciplineRepository.existsByTitle(title); }

    public List<Discipline> getDiscipline(Long idUser) {
        List<Discipline> disciplines = new ArrayList<>();

        List<String> roles = new ArrayList<>();
        userService.getUserByID(idUser).ifPresent(value -> roles.addAll(roleService.getUserRoles(value)));
        if (!roles.isEmpty()){
            if(roles.contains("ROLE_ADMIN")) {
                disciplines.addAll(disciplineRepository.findAll());
            } else if(roles.contains("ROLE_LECTOR") & !roles.contains("ROLE_ADMIN")) {
                disciplines.addAll(userService.getAuthorizeLector().getDisciplineList());
            } else {
                disciplines.addAll(userService.getStudent().getGroup().getDisciplineList());
            }
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

    public Boolean updateDiscipline(Long idDiscipline, CreateOrUpdateDisciplineRequest createOrUpdateDisciplineRequest) {
        Optional<Discipline> disciplineData = disciplineRepository.findById(idDiscipline);

        if (disciplineData.isPresent()) {
            try {
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

                for (var group: groupList) {
                    for (var student: group.getStudentList()) {
                        if (scoreRepository.findByStudentAndDiscipline(student, _discipline).isEmpty()) {
                            Score score = new Score(student, _discipline, 0);
                            scoreRepository.save(score);
                        }
                    }
                }

                for (var group: getGroupOutByDisciplineId(_discipline.getId())) {
                    for (var student: group.getStudentList()) {
                        scoreRepository.findByStudentAndDiscipline(student, _discipline)
                                .ifPresent(value -> scoreRepository.deleteById(value.getId()));
                    }
                }

                return true;
            } catch (Exception e) {
                return false;
            }

        } else {
            return false;
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
        List<Group> groupOutDiscipline = groupService.getAllGroups();

        for (var group: groupInDiscipline) {
            groupOutDiscipline.remove(group);
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

    public List<StudentResponse> getScoresForDiscipline(Long idDiscipline) {
        List<StudentResponse> studentResponseList = new ArrayList<>();
        Optional<Discipline> discipline = disciplineRepository.findById(idDiscipline);
        if (discipline.isPresent()) {
            for (var student: studentRepository.findAll()) {
                Optional<Score> score = scoreRepository.findByStudentAndDiscipline(student, discipline.get());
                if (score.isPresent()) {
                    StudentResponse studentResponse = new StudentResponse();
                    studentResponse.setName(student.getUser().getFullName());
                    studentResponse.setGroupName(student.getGroup().getName());
                    studentResponse.setEmail(student.getUser().getEmail());
                    studentResponse.setScore(score.get().getScore());
                    studentResponseList.add(studentResponse);
                }
            }
        }
        return studentResponseList;
    }
}
