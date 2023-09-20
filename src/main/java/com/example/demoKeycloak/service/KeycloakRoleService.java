package com.example.demoKeycloak.service;


import com.example.demoKeycloak.Responses.CustomResponse;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RoleResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class KeycloakRoleService {

    @Autowired
    private Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    public CustomResponse createRole(String roleName){

        RoleResource roleResource=keycloak.realm(realm).roles().get(roleName);

        try{

             roleResource.toRepresentation();

             return CustomResponse
                     .getBuilder()
                     .statusCode(HttpStatus.CONFLICT.value())
                     .message("Role "+roleName+" already exists!")
                     .build();
        }
        catch(Exception e){
            RoleRepresentation roleRepresentation=new RoleRepresentation();
            roleRepresentation.setName(roleName);

            keycloak.realm(realm).roles().create(roleRepresentation);
            roleRepresentation=keycloak.realm(realm).roles().get(roleName).toRepresentation();

            return CustomResponse
                    .getBuilder()
                    .message("Role "+roleName+" created Successfully")
                    .statusCode(HttpStatus.CREATED.value())
                    .data(roleRepresentation)
                    .build();
        }
    }

    public CustomResponse getAllRoles(){
        CustomResponse customResponse=new CustomResponse();

        List<RoleRepresentation>roleRepresentationList=keycloak.realm(realm).roles().list();

        String message=null;

        if(roleRepresentationList.isEmpty()){
            message="Sorry! No role exists.";
        }

        return CustomResponse
                .getBuilder()
                .message(message)
                .statusCode(HttpStatus.OK.value())
                .data(roleRepresentationList)
                .build();
    }

    public CustomResponse assignRoleToUser(String userId, String roleName){
        UserResource userResource=keycloak.realm(realm).users().get(userId);
        RoleResource roleResource=keycloak.realm(realm).roles().get(roleName);

        UserRepresentation userRepresentation;
        RoleRepresentation roleRepresentation;
        try{
            userResource.toRepresentation();
        }
        catch(Exception e){
            return CustomResponse.getBuilder()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .message("User with given id not found!")
                    .errorMessages(e.getMessage()).build();
        }

        try{
            roleRepresentation=roleResource.toRepresentation();
        }
        catch (Exception e){
            return CustomResponse.getBuilder()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .message("Role with given name does not exists!")
                    .errorMessages(e.getMessage()).build();

        }

        userResource.roles().realmLevel().add(Collections.singletonList(roleRepresentation));

        userRepresentation=keycloak.realm(realm).users().get(userId).toRepresentation();

        return CustomResponse.getBuilder()
                .statusCode(HttpStatus.OK.value())
                .message("Role is assigned to the given user.")
                .data(userRepresentation).build();
    }

    public CustomResponse revokeRoleFromUser(String userId,String roleName){
        CustomResponse customResponse=new CustomResponse();

        UserResource userResource=keycloak.realm(realm).users().get(userId);

        UserRepresentation userRepresentation;
        RoleRepresentation roleToRemove=null;

        try{
            userResource.toRepresentation();
        }
        catch(Exception e){
            return CustomResponse.getBuilder()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .message("User with given id not found!")
                    .errorMessages(e.getMessage()).build();
        }

        for(RoleRepresentation role:userResource.roles().realmLevel().listAll()){
            if(role.getName().equals(roleName)){
                roleToRemove=role;
            }
        }
        if(roleToRemove!=null){
            userResource.roles().realmLevel().remove(Collections.singletonList(roleToRemove));
        }

        userRepresentation=keycloak.realm(realm).users().get(userId).toRepresentation();

        return CustomResponse.getBuilder()
                .message("Role from given user is successfully revoked")
                .statusCode(HttpStatus.OK.value())
                .data(userRepresentation)
                .build();
    }


    public CustomResponse deleteRole(String roleName){
        RoleResource roleResource=keycloak.realm(realm).roles().get(roleName);

        try{
            roleResource.toRepresentation();

            roleResource.remove();
            return CustomResponse.getBuilder().statusCode(HttpStatus.NO_CONTENT.value()).build();
        }
        catch (Exception e){
            return CustomResponse.getBuilder()
                    .message("Given role does not  exists!")
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .build();
        }
    }
}
