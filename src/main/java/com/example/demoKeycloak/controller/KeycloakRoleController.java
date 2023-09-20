package com.example.demoKeycloak.controller;


import com.example.demoKeycloak.Responses.CustomResponse;
import com.example.demoKeycloak.Validations.ValidParam;
import com.example.demoKeycloak.service.KeycloakRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@RestController
@Validated
public class KeycloakRoleController {

    @Autowired
    private KeycloakRoleService keycloakRoleService;

    @PostMapping("/createrole")
    public ResponseEntity<CustomResponse> createRole(@ValidParam @RequestParam String roleName){

        return keycloakRoleService
                .createRole(roleName)
                .responseBuilder();
    }

    @GetMapping("/getallroles")
    public ResponseEntity<CustomResponse> getAllRoles(){
        return keycloakRoleService
                .getAllRoles()
                .responseBuilder();
    }

    @PutMapping("/assignroletouser")
    public ResponseEntity<CustomResponse> assignRoleToUser(
            @ValidParam @RequestParam String id,
            @ValidParam @RequestParam String roleName){

        return keycloakRoleService
                .assignRoleToUser(id,roleName)
                .responseBuilder();
    }

    @PutMapping("/revokerolefromuser")
    public ResponseEntity<CustomResponse> revokeRoleFromUser(
            @ValidParam @RequestParam String id,
            @ValidParam @RequestParam String roleName){

       return keycloakRoleService
               .revokeRoleFromUser(id, roleName)
               .responseBuilder();
    }

    @DeleteMapping("/deleterole")
    public ResponseEntity<CustomResponse> deleteRole(@ValidParam @RequestParam String roleName){
        return keycloakRoleService
                .deleteRole(roleName)
                .responseBuilder();
    }
}
