package com.fernando.connected_minds_api.services;

import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import com.fernando.connected_minds_api.exceptions.UserIsAlreadyExiststInCommunityOrGroupException;
import com.fernando.connected_minds_api.models.Group;
import com.fernando.connected_minds_api.models.User;
import com.fernando.connected_minds_api.repositories.GroupRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;

    public void addMemberOnGroup(UUID groupID, User member) {
        Group group = groupRepository.findById(groupID)
                .orElseThrow(() -> new EntityNotFoundException("Group is not exists"));
        
        Optional<User> user = group
                .getMembers()
                .stream().
                filter(userMember -> userMember.getId() == member.getId())
                .findFirst();

        if (user.isPresent()) {
                throw new UserIsAlreadyExiststInCommunityOrGroupException("User is already exists in group#%d".formatted(groupID));               
        }
        
        group.getMembers().add(member);

        groupRepository.save(group);
    }

    public boolean groupIsExists(UUID groupID) {
        return groupRepository.existsById(groupID);
    }
}
