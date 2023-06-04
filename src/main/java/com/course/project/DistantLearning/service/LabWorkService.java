package com.course.project.DistantLearning.service;

import com.course.project.DistantLearning.models.Discipline;
import com.course.project.DistantLearning.models.LabWork;
import com.course.project.DistantLearning.repository.LabWorkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LabWorkService {
    @Autowired
    LabWorkRepository labWorkRepository;

    @Autowired
    DisciplineService disciplineService;

    public Boolean existsByTitle(String title) { return labWorkRepository.existsByTitle(title); }

    public List<LabWork> getLabWorkByDiscipline (Long idDiscipline) {
        List<LabWork> labWorks = new ArrayList<>();
        Optional<Discipline> discipline = disciplineService.getDisciplineById(idDiscipline);
        discipline.ifPresent(value -> labWorks.addAll(labWorkRepository.findByDiscipline(value)));

        return labWorks;

    }

    public Optional<LabWork> getLabWorkById(Long idLabWork) {
        return labWorkRepository.findById(idLabWork);
    }

    public boolean createLabWork(Long idDiscipline, LabWork labWork) {
        Optional<Discipline> discipline = disciplineService.getDisciplineById(idDiscipline);
        if (discipline.isPresent()) {
            labWork.setDiscipline(discipline.get());
            labWork.setStatus("None");
            labWorkRepository.save(labWork);
            return true;
        }
        return false;

    }

    public boolean updateLabWork(Long idLabWork, LabWork labWork) {
        Optional<LabWork> labWorkData = labWorkRepository.findById(idLabWork);

        if (labWorkData.isPresent()) {
            LabWork _labWork = labWorkData.get();
            _labWork.setTitle(labWork.getTitle());
            _labWork.setManual(labWork.getManual());
            _labWork.setDeadline(labWork.getDeadline());
            _labWork.setStatus("None");
            labWorkRepository.save(_labWork);
            return true;
        }
        else {
            return false;
        }
    }

    public void deleteLabWork(Long idLabWork) { labWorkRepository.deleteById(idLabWork); }

}
