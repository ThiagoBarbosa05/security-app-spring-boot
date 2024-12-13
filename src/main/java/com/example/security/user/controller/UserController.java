package com.example.security.user.controller;

import com.example.security.dto.ExceptionMessageResponse;
import com.example.security.user.dto.UserProfileResponse;
import com.example.security.storage.service.StorageService;
import com.example.security.user.dto.*;
import com.example.security.user.model.User;
import com.example.security.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@RestController
@RequestMapping("/api/users")
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private StorageService storageService;


  @Tag(name = "User")
  @Operation(summary = "Registar um usuário", description = "Essa rota é responsável pelo registro de um novo usuário")
  @ApiResponses({
    @ApiResponse(responseCode = "201", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = RegisterUserResponse.class))}),
    @ApiResponse(responseCode = "409", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessageResponse.class))}),
    @ApiResponse(responseCode = "400", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessageResponse.class))})
  })
  @PostMapping("/registration")
  public ResponseEntity<Object> showRegistrationForm(
    @Valid
    @RequestBody
    RegisterUserRequest registerUserRequest
  ) {

    User userRegistered = userService.registerNewUser(registerUserRequest);

    RegisterUserResponse response = new RegisterUserResponse(userRegistered.getId());

    return new ResponseEntity<>(response, HttpStatus.CREATED);
  }

  @Tag(name = "User")
  @Operation(summary = "Autenticar o usuário", description = "Essa rota é responsável pela autenticação de um usuário")
  @ApiResponses({
    @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class))}),
    @ApiResponse(responseCode = "401", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessageResponse.class))})
  })
  @PostMapping("/authenticate")
  public ResponseEntity<Object> authenticate(
    @Valid
    @RequestBody
    LoginUserRequest loginUserRequest
  ) {
    String accessToken = userService.authenticateUser(loginUserRequest);
    AuthenticationResponse response = new AuthenticationResponse(accessToken);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @Tag(name = "User")
  @Operation(summary = "Informações do usuário", description = "Essa rota é responsável em mostrar as informações do usuário")
  @ApiResponses({
    @ApiResponse(responseCode = "200", content = { @Content(mediaType = "application/json", schema = @Schema(implementation =UserProfileResponse.class))}),
    @ApiResponse(responseCode = "404", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessageResponse.class))})
  })
  @SecurityRequirement(name = "jwt_auth")
  @GetMapping("/profile")
  public ResponseEntity<Object> getProfile(HttpServletRequest request) {

    User user = (User) request.getAttribute("user");

    UserProfileResponse response = new UserProfileResponse(
      user.getId(),
      user.getFirstName(),
      user.getLastName(),
      user.getEmail(),
      user.getAvatar(),
      user.getCreatedAt()
    );

    return new ResponseEntity<>(response, HttpStatus.OK);
  }


  @Tag(name = "User")
  @Operation(summary = "Update da imagem de perfil", description = "Essa rota é responsável por atualizar a imagem de perfil de um usuário")
  @ApiResponses({
    @ApiResponse(responseCode = "200"),
    @ApiResponse(responseCode = "400", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessageResponse.class))}),
    @ApiResponse(responseCode = "404", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessageResponse.class))})
  })
  @SecurityRequirement(name = "jwt_auth")
  @PatchMapping(
    value = "/update/avatar/{userId}",
    consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}// Note the consumes in the mapping
    )
  public ResponseEntity<Object> updateAvatar(
    @RequestParam("file") MultipartFile file,
    @PathVariable String userId
  ) {
    String key = storageService.uploadFile(file);
    UpdateImageProfileRequest updateImageProfileRequest = new UpdateImageProfileRequest(key, UUID.fromString(userId));
    userService.updateProfileImage(updateImageProfileRequest);

    return new ResponseEntity<>("", HttpStatus.OK);
  }

  @Tag(name = "User")
  @Operation(summary = "Redefinição de senha", description = "Essa rota é responsável em enviar um email para redefinir a senha do usuário")
  @ApiResponses({
    @ApiResponse(responseCode = "200"),
    @ApiResponse(responseCode = "404", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessageResponse.class))})

  })
  @PostMapping("/recovery-password")
  public ResponseEntity<Object> recoveryPassword(
    @Valid
    @RequestBody
    ResetPasswordRequest resetPasswordRequest
  ) {
    userService.sendEmailToResetPassword(resetPasswordRequest);

    return new ResponseEntity<>("ok", HttpStatus.OK);
  }

  @Tag(name = "User")
  @Operation(summary = "Atualização de senha", description = "Essa rota é responsável em atualizar a senha do usuário")
  @ApiResponses({
    @ApiResponse(responseCode = "200"),
    @ApiResponse(responseCode = "400", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessageResponse.class))})
  })
  @PatchMapping("/update-password")
  public ResponseEntity<Object> updatePassword(
    @Valid
    @RequestBody UpdateUserPasswordRequest updateUserPasswordRequest
    ) {
     userService.updateUserPassword(updateUserPasswordRequest);

    return new ResponseEntity<>("Success", HttpStatus.OK);
  }

  @Tag(name = "User")
  @Operation(summary = "Atualizar informações do usuário", description = "Essa rota é responsável em atualizar as informações de um usuário")
  @ApiResponses({
    @ApiResponse(responseCode = "200"),
    @ApiResponse(responseCode = "400", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessageResponse.class))}),
    @ApiResponse(responseCode = "404", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessageResponse.class))})
  })
  @SecurityRequirement(name = "jwt_auth")

  @PutMapping("/update/profile")
  public ResponseEntity<Object> updateUserProfile(
    @Valid
    @RequestBody UpdateUserRequest updateUserRequest,
    HttpServletRequest request
    ) {
    User user = (User) request.getAttribute("user");


    userService.updateUser(updateUserRequest, user);

    return new ResponseEntity<>("ok", HttpStatus.OK);
  }

  @Tag(name = "User")
  @Operation(summary = "Deletar usuário", description = "Essa rota é responsável em deletar o usuário")
  @ApiResponses({
    @ApiResponse(responseCode = "209"),
    @ApiResponse(responseCode = "401", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionMessageResponse.class))})
  })
  @SecurityRequirement(name = "jwt_auth")

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteUser(@PathVariable String id) {
    userService.deleteUser(UUID.fromString(id));

    return new ResponseEntity<>("Ok", HttpStatus.NO_CONTENT);
  }
}
