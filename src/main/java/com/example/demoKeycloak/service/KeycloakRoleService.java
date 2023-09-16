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

        CustomResponse customResponse=new CustomResponse();

        RoleResource roleResource=keycloak.realm(realm).roles().get(roleName);

        try{

             roleResource.toRepresentation();

             customResponse.setMessage("Role "+roleName+" already exists!");
             customResponse.setStatusCode(HttpStatus.CONFLICT.value());

             return customResponse;

        }
        catch(Exception e){
            RoleRepresentation roleRepresentation=new RoleRepresentation();
            roleRepresentation.setName(roleName);

            keycloak.realm(realm).roles().create(roleRepresentation);
            roleRepresentation=keycloak.realm(realm).roles().get(roleName).toRepresentation();

            customResponse.setStatusCode(HttpStatus.CREATED.value());
            customResponse.setMessage("Role "+roleName+" created Successfully");
            customResponse.setData(roleRepresentation);

            return customResponse;

        }
    }

    public CustomResponse getAllRoles(){
        CustomResponse customResponse=new CustomResponse();

        List<RoleRepresentation>roleRepresentationList=keycloak.realm(realm).roles().list();

        if(roleRepresentationList.size()==0){
            customResponse.setMessage("Sorry! No role exists.");
        }
        customResponse.setStatusCode(HttpStatus.OK.value());
        customResponse.setData(roleRepresentationList);

        return customResponse;
    }

    public CustomResponse assignRoleToUser(String userId, String roleName){
        UserResource userResource=keycloak.realm(realm).users().get(userId);
        RoleResource roleResource=keycloak.realm(realm).roles().get(roleName);

        UserRepresentation userRepresentation;
        RoleRepresentation roleRepresentation;

        CustomResponse customResponse=new CustomResponse();

        try{
            userResource.toRepresentation();
        }
        catch(Exception e){
            customResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            customResponse.setMessage("User with given id not found!");
            customResponse.setErrorMessage(e.getMessage());

            return customResponse;
        }

        try{
            roleRepresentation=roleResource.toRepresentation();
        }
        catch (Exception e){
            customResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            customResponse.setMessage("Role with given name does not exists!");
            customResponse.setErrorMessage(e.getMessage());

            return customResponse;
        }

        userResource.roles().realmLevel().add(Arrays.asList(roleRepresentation));

        userRepresentation=keycloak.realm(realm).users().get(userId).toRepresentation();

        customResponse.setStatusCode(HttpStatus.OK.value());
        customResponse.setMessage("Role is assigned to the given user");
        customResponse.setData(userRepresentation);

        return customResponse;

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
            customResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
            customResponse.setMessage("User with given id not found!");
            customResponse.setErrorMessage(e.getMessage());

            return customResponse;
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

        customResponse.setStatusCode(HttpStatus.OK.value());
        customResponse.setMessage("Role from given user is successfully revoked");
        customResponse.setData(userRepresentation);

        return customResponse;
    }


    public CustomResponse deleteRole(String roleName){
        CustomResponse customResponse=new CustomResponse();

        RoleResource roleResource=keycloak.realm(realm).roles().get(roleName);

        try{
            roleResource.toRepresentation();

            roleResource.remove();

            customResponse.setStatusCode(HttpStatus.NO_CONTENT.value());
            customResponse.setMessage("Role is successfully deleted");
            return customResponse;
        }
        catch (Exception e){
            customResponse.setMessage("Given role does not  exists!");
            customResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());

            return customResponse;
        }
    }
}
