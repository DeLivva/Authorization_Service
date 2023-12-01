package com.vention.authorization_service.service.impl;

import com.vention.authorization_service.domain.SecurityCredentialEntity;
import com.vention.authorization_service.domain.UserEntity;
import com.vention.authorization_service.dto.request.UserDeleteRequestDTO;
import com.vention.authorization_service.dto.request.UserProfileFillRequestDTO;
import com.vention.authorization_service.dto.request.UserUpdateRequestDTO;
import com.vention.authorization_service.dto.response.UserUpdateResponseDTO;
import com.vention.authorization_service.exception.DataNotFoundException;
import com.vention.authorization_service.exception.DuplicateDataException;
import com.vention.authorization_service.repository.SecurityCredentialRepository;
import com.vention.authorization_service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityCredentialRepository securityCredentialRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        testUser = mock();
    }


    @Test
    void testSaveUser() {
        // given
        // when
        doReturn(testUser).when(userRepository).save(any());
        UserEntity savedUser = userService.saveUser(testUser);
        // then
        verify(userRepository, times(1)).save(any(UserEntity.class));
        assertNotNull(savedUser.getId());
        assertSame(testUser, savedUser);
    }

    @Test
    void testGetUserByEmail() {
        // given
        // when
        doReturn(Optional.of(testUser)).when(userRepository).findByEmail(any(String.class));
        UserEntity userByEmail = userService.getByEmail("test");
        // then
        verify(userRepository, times(1)).findByEmail(any());
        assertSame(testUser, userByEmail);
    }

    @Test
    void testGetUserByEmailWillThrow() {
        // given
        // when
        doReturn(Optional.empty()).when(userRepository).findByEmail(any());
        try {
            userService.getByEmail("test");
            // then
            fail("Expected exception, but exception is not thrown");
        } catch (DataNotFoundException e) {
            // then
            assertEquals(e.getMessage(), "User not found with email: test");
            verify(userRepository, times(1)).findByEmail(any());
        }
    }

    @Test
    void testIsEmailUniqueTrue() {
        // given
        // when
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        boolean isUnique = userService.isEligibleForRegistration("test");
        // then
        verify(userRepository, times(1)).findByEmail(any());
        assertTrue(isUnique);
    }

    @Test
    void testIsEmailUniqueFalse() {
        // given
        // when
        doReturn(Optional.of(testUser)).when(userRepository).findByEmail(any());
        boolean isUnique = userService.isEligibleForRegistration("test");
        // then
        verify(userRepository, times(1)).findByEmail(any());
        assertFalse(isUnique);
    }

    @Test
    public void testFillProfileSuccess() {
        // when
        UserProfileFillRequestDTO request =
                new UserProfileFillRequestDTO(1L, "Abbos", "Akramov", "adam123", "911980669");

        UserEntity user = new UserEntity();
        SecurityCredentialEntity credentials = new SecurityCredentialEntity();
        user.setCredentials(credentials);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(securityCredentialRepository.findByUsername("adam123")).thenReturn(Optional.empty());
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);
        when(securityCredentialRepository.save(any(SecurityCredentialEntity.class))).thenReturn(credentials);

        UserEntity updatedUser = userService.fillProfile(request);

        // then
        assertEquals("Abbos", updatedUser.getFirstName());
        assertEquals("Akramov", updatedUser.getLastName());
        assertEquals("911980669", updatedUser.getPhoneNumber());
        assertEquals("adam123", updatedUser.getCredentials().getUsername());
        verify(userRepository, times(1)).save(user);
        verify(securityCredentialRepository, times(1)).save(credentials);
    }

    @Test
    public void testFillProfileUserNotFound() {
        // when
        UserProfileFillRequestDTO request = new UserProfileFillRequestDTO();
        request.setUserId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // then
        assertThrows(DataNotFoundException.class, () -> userService.fillProfile(request));
    }

    @Test
    public void testFillProfileUsernameExists() {
        // when
        UserProfileFillRequestDTO request = new UserProfileFillRequestDTO();
        request.setUserId(1L);
        request.setUsername("existing_username");
        UserEntity user = new UserEntity();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(securityCredentialRepository.findByUsername("existing_username")).thenReturn(Optional.of(new SecurityCredentialEntity()));

        // then
        assertThrows(DuplicateDataException.class, () -> userService.fillProfile(request));
    }

    @Test
    public void testDeleteUserSuccess() {
        UserDeleteRequestDTO request = new UserDeleteRequestDTO();
        request.setUserId(1L);
        UserEntity user = new UserEntity();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(request.getUserId());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
        assertTrue(user.getIsDeleted());
    }

    @Test
    public void testDeleteUserNotFoundException() {
        UserDeleteRequestDTO request = new UserDeleteRequestDTO();
        request.setUserId(1L);
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> userService.deleteUser(2L));
    }

    @Test
    public void testUpdateUserNotFoundException() {
        UserUpdateRequestDTO request = new UserUpdateRequestDTO();
        request.setUserId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DataNotFoundException.class, () -> userService.updateUser(request));
    }

    @Test
    public void testUpdateUserNoPasswordSuccess() {
        UserUpdateRequestDTO request = new UserUpdateRequestDTO();
        request.setUserId(1L);
        request.setFirstName("Jacky");
        request.setLastName("Chan");
        request.setPhoneNumber("123456789");
        request.setUsername("jacky123");
        UserEntity user = new UserEntity();
        SecurityCredentialEntity credentials = new SecurityCredentialEntity();
        user.setCredentials(credentials);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(securityCredentialRepository.save(any(SecurityCredentialEntity.class))).thenReturn(credentials);
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        UserUpdateResponseDTO response = userService.updateUser(request);
        assertEquals("Jacky", response.getFirstName());
        assertEquals("Chan", response.getLastName());
        assertEquals("123456789", response.getPhoneNumber());
        assertEquals("jacky123", response.getUsername());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
        verify(securityCredentialRepository, times(1)).save(credentials);
    }

    @Test
    public void testUpdateUserWithPasswordSuccess() {
        UserUpdateRequestDTO request = new UserUpdateRequestDTO();
        request.setUserId(1L);
        request.setFirstName("Jacky");
        request.setLastName("Chan");
        request.setPhoneNumber("123456789");
        request.setUsername("jacky123");
        request.setPassword("password");
        UserEntity user = new UserEntity();
        SecurityCredentialEntity credentials = new SecurityCredentialEntity();
        user.setCredentials(credentials);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(securityCredentialRepository.save(any(SecurityCredentialEntity.class))).thenReturn(credentials);
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        UserUpdateResponseDTO response = userService.updateUser(request);
        assertEquals("Jacky", response.getFirstName());
        assertEquals("Chan", response.getLastName());
        assertEquals("123456789", response.getPhoneNumber());
        assertEquals("jacky123", response.getUsername());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
        verify(securityCredentialRepository, times(1)).save(credentials);
    }
}