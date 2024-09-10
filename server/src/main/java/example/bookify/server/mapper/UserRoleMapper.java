package example.bookify.server.mapper;

import example.bookify.server.model.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserRoleMapper {

    @Mapping(target = "authority", source = "type")
    UserRole roleTypeToRole(UserRole.RoleType type);

    default UserRole.RoleType roleToRoleType(UserRole role) {
        return role.getAuthority();
    }

    List<UserRole> roleTypeListToRoleList(List<UserRole.RoleType> roleTypes);

    List<UserRole.RoleType> roleListToRoleTypeList(List<UserRole> roles);
}
