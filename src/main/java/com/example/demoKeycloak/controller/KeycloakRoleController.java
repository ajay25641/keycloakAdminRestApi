package com.example.demoKeycloak.controller;


import com.example.demoKeycloak.Responses.CustomResponse;
import com.example.demoKeycloak.service.KeycloakRoleService;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;



@RestController
public class KeycloakRoleController {

    @Autowired
    private KeycloakRoleService keycloakRoleService;

    @PostMapping("/createrole")
    public ResponseEntity<CustomResponse> createRole(@RequestParam String roleName){

        CustomResponse customResponse;

        if(roleName==null){
            customResponse=new CustomResponse();
            customResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            customResponse.setMessage("Please provide roleName");

            return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);
        }

        customResponse=keycloakRoleService.createRole(roleName);

        return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);
    }
    @GetMapping("/getallroles")
    public ResponseEntity<CustomResponse> getAllRoles(){
        CustomResponse customResponse=keycloakRoleService.getAllRoles();

        return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);
    }

    @PutMapping("/assignroletouser")
    public ResponseEntity<CustomResponse> assignRoleToUser(@RequestParam String id, @RequestParam String roleName){
        CustomResponse customResponse=new CustomResponse();

        if(id==null){
            customResponse.setMessage("Please provide id of the user!");
            customResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());

            return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);
        }
        if(roleName==null){
            customResponse.setMessage("Please provide roleName!");
            customResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());

            return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);
        }

        customResponse=keycloakRoleService.assignRoleToUser(id,roleName);

        return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);

    }

    @PutMapping("/revokerolefromuser")
    public ResponseEntity<CustomResponse> revokeRoleFromUser(@RequestParam String id, @RequestParam String roleName){
        CustomResponse customResponse=new CustomResponse();

        if(id==null){
            customResponse.setMessage("Please provide id of the user!");
            customResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());

            return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);
        }
        if(roleName==null){
            customResponse.setMessage("Please provide roleName!");
            customResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());

            return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);
        }

        customResponse=keycloakRoleService.revokeRoleFromUser(id, roleName);

        return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);

    }


    @DeleteMapping("/deleterole")
    public ResponseEntity<CustomResponse> deleteRole(@RequestParam String roleName){
        CustomResponse customResponse;

        if(roleName==null){
            customResponse=new CustomResponse();

            customResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
            customResponse.setMessage("Please provide roleName");

            return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);
        }

        customResponse=keycloakRoleService.deleteRole(roleName);

        return ResponseEntity.status(customResponse.getStatusCode()).body(customResponse);
    }
}
