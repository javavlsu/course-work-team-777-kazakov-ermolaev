package com.course.project.DistantLearning.service;

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

    public List<Discipline> getDiscipline(Long idGroup) {
        return new ArrayList<>(groupRepository.findById(idGroup).get().getDisciplineList());
    }

    public void createDiscipline(Discipline discipline) {
        List<Lector> lectorList = new ArrayList<>();
        Lector lector = lectorRepository.findByUser(userRepository.findByUsername(getCurrentUserName()).get()).get();

        lectorList.add(lector);
        discipline.setLector(lectorList);
        disciplineRepository.save(discipline);
    }
}
