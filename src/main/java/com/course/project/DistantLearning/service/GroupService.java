package com.course.project.DistantLearning.service;

import com.course.project.DistantLearning.dto.response.LectorResponse;
import com.course.project.DistantLearning.dto.response.MessageResponse;
import com.course.project.DistantLearning.dto.response.StudentResponse;
import com.course.project.DistantLearning.dto.response.UpdateGroupResponse;
import com.course.project.DistantLearning.models.Group;
import com.course.project.DistantLearning.models.Lector;
import com.course.project.DistantLearning.models.Student;
import com.course.project.DistantLearning.repository.GroupRepository;
import com.course.project.DistantLearning.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private StudentRepository studentRepository;

    public List<Group> getGroups() {
        return groupRepository.findAll().stream().sorted((a1, b1) -> Long.compare(a1.getId(), b1.getId())).toList();
    }

    public Optional<Group> getGroupById(Long idGroup) { return groupRepository.findById(idGroup); }

    public void createGroup(Group group) {
        groupRepository.save(group);
    }

    public MessageResponse updateGroup(Long idGroup, UpdateGroupResponse updateGroupResponse) {
        Optional<Group> groupData = groupRepository.findById(idGroup);

        if (groupData.isPresent()) {
            Group _group = groupData.get();
            _group.setName(updateGroupResponse.getName());
            List<Student> studentList = new ArrayList<>();
            for(var student: updateGroupResponse.getStudentResponseList()) {
                studentList.add(studentRepository.findById(student.getId()).get());
            }

            _group.setStudentList(studentList);
            groupRepository.save(_group);
            return new MessageResponse("Group has updated!");
        }
        else {
            return new MessageResponse("Error! Group has not updated!");
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
