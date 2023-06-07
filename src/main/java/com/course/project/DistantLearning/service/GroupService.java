package com.course.project.DistantLearning.service;

import com.course.project.DistantLearning.dto.response.MessageResponse;
import com.course.project.DistantLearning.dto.response.StudentResponse;
import com.course.project.DistantLearning.dto.response.UpdateGroupResponse;
import com.course.project.DistantLearning.models.Group;
import com.course.project.DistantLearning.models.Score;
import com.course.project.DistantLearning.models.Student;
import com.course.project.DistantLearning.repository.GroupRepository;
import com.course.project.DistantLearning.repository.ScoreRepository;
import com.course.project.DistantLearning.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    @Autowired
    GroupRepository groupRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    ScoreRepository scoreRepository;

    public List<Group> getGroups() {
        return groupRepository.findAll().stream().sorted(Comparator.comparingLong(Group::getId)).toList();
    }

    public List<Group> getAllGroups() { return groupRepository.findAll(); }

    public Optional<Group> getGroupById(Long idGroup) { return groupRepository.findById(idGroup); }

    public void createGroup(Group group) {
        groupRepository.save(group);
    }

    public Boolean updateGroup(Long idGroup, UpdateGroupResponse updateGroupResponse) {
        Optional<Group> groupData = groupRepository.findById(idGroup);

        if (groupData.isPresent()) {
            try {
                Group _group = groupData.get();
                _group.setName(updateGroupResponse.getName());
                List<Student> studentList = new ArrayList<>();
                for(var student: updateGroupResponse.getStudentResponseList()) {
                    studentRepository.findById(student.getId()).ifPresent(studentList::add);
                }

                _group.setStudentList(studentList);
                groupRepository.save(_group);

                if (!_group.getDisciplineList().isEmpty()) {
                    for(var student: studentRepository.findAll()) {
                        for (var discipline: _group.getDisciplineList()) {
                            if (student.getGroup() == null && scoreRepository.findByStudentAndDiscipline(student, discipline).isPresent()) {
                                scoreRepository.findByStudentAndDiscipline(student, discipline)
                                        .ifPresent(value -> scoreRepository.deleteById(value.getId()));
                            }
                            else if (scoreRepository.findByStudentAndDiscipline(student, discipline).isEmpty()) {
                                Score score = new Score(student, discipline, 0);
                                scoreRepository.save(score);
                            }
                        }
                    }
                }
                return true;
            } catch (Exception e) {
                return false;
            }

        }
        else {
            return false;
        }
    }

    public void deleteGroup(Long idStudent) {
        groupRepository.deleteById(idStudent);
    }

    public Boolean existsByName(String nameGroup) {
        return groupRepository.existsByName(nameGroup);
    }

    public Optional<Group> findByGroupName(String nameGroup) { return groupRepository.findByName(nameGroup); }
}
