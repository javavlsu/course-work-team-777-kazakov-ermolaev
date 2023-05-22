package com.course.project.DistantLearning.service;

import com.course.project.DistantLearning.dto.request.CreateDisciplineRequest;
import com.course.project.DistantLearning.models.Discipline;
import com.course.project.DistantLearning.models.Group;
import com.course.project.DistantLearning.models.Lector;
import com.course.project.DistantLearning.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DisciplineService {
    @Autowired
    DisciplineRepository disciplineRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    LectorRepository lectorRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupRepository groupRepository;

    public String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        return currentPrincipalName;
    }

    public List<Discipline> getAllDiscipline() {
        return new ArrayList<>(disciplineRepository.findAll());
    }

    public List<Discipline> getLectorDiscipline(Long idLector) {
        return new ArrayList<>(lectorRepository.findById(idLector).get().getDisciplineList());
    }

    public List<Discipline> getStudentDiscipline(Long idGroup) {
        return new ArrayList<>(groupRepository.findById(idGroup).get().getDisciplineList());
    }

    public void createDiscipline(CreateDisciplineRequest createDisciplineRequest) {
        List<Lector> lectorList = new ArrayList<>();
        Discipline discipline = new Discipline();

        if (!createDisciplineRequest.getLectorResponseList().isEmpty()) {
            for(var lector: createDisciplineRequest.getLectorResponseList()) {
                lectorList.add(lectorRepository.findById(lector.getId()).get());
            }
        }

        discipline.setTitle(createDisciplineRequest.getTitle());

        if (!lectorList.isEmpty()) {
            discipline.setLector(lectorList);
        }
        disciplineRepository.save(discipline);
    }
}
