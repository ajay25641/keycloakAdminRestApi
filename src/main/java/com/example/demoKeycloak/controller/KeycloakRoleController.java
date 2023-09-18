package com.example.demoKeycloak.controller;


import com.example.demoKeycloak.Responses.CustomResponse;
import com.example.demoKeycloak.Validations.ValidParam;
import com.example.demoKeycloak.service.KeycloakRoleService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@RestController
@Validated
public class KeycloakRoleController {

    @Autowired
    private KeycloakRoleService keycloakRoleService;

    @PostMapping("/createrole")
    public ResponseEntity<CustomResponse> createRole(@ValidParam @RequestParam String roleName){

        CustomResponse customResponse;

        customResponse=keycloakRoleService.createRole(roleName);

        return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);
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

        CustomResponse customResponse;

        customResponse=keycloakRoleService.assignRoleToUser(id,roleName);

        return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);

    }

    @PutMapping("/revokerolefromuser")
    public ResponseEntity<CustomResponse> revokeRoleFromUser(
            @ValidParam @RequestParam String id,
            @ValidParam @RequestParam String roleName){

        CustomResponse customResponse;

        customResponse=keycloakRoleService.revokeRoleFromUser(id, roleName);

        return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);

    }


    @DeleteMapping("/deleterole")
    public ResponseEntity<CustomResponse> deleteRole(@ValidParam @RequestParam String roleName){
        CustomResponse customResponse;

        customResponse=keycloakRoleService.deleteRole(roleName);

        return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);
    }
}
