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
        CustomResponse customResponse=keycloakRoleService.getAllRoles();

        return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);
    }

    @PutMapping("/assignroletouser")
    public ResponseEntity<CustomResponse> assignRoleToUser(
            @ValidParam @RequestParam String id,
            @ValidParam @RequestParam String roleName){

        CustomResponse customResponse=keycloakRoleService.assignRoleToUser(id,roleName);

        return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);
    }

    @PutMapping("/revokerolefromuser")
    public ResponseEntity<CustomResponse> revokeRoleFromUser(
            @ValidParam @RequestParam String id,
            @ValidParam @RequestParam String roleName){

       CustomResponse customResponse=keycloakRoleService.revokeRoleFromUser(id, roleName);

       return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);
    }


    @DeleteMapping("/deleterole")
    public ResponseEntity<CustomResponse> deleteRole(@ValidParam @RequestParam String roleName){
        CustomResponse customResponse=keycloakRoleService.deleteRole(roleName);

        return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);
    }
}
