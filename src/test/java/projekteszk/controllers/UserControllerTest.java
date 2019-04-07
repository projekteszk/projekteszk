package projekteszk.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import projekteszk.entities.User;
import projekteszk.repositories.UserRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(value=UserController.class, secure=false)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;
    
    @Test
    public void testGetById() throws Exception {
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setRole(User.Role.ROLE_USER);
        mockUser.setName("Teszt User");
        mockUser.setPass("pass");
        mockUser.setEmail("user@email.com");
        mockUser.setPhone("06301234567");
        mockUser.setAddress("1000 Teszt, Teszt utca 1.");
        
        Mockito.when(userRepository.findById(Mockito.anyInt()))
               .thenReturn(Optional.of(mockUser));
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/users/1")
                .accept(MediaType.APPLICATION_JSON);
        
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String exceptedJson = this.mapToJson(mockUser);
        String outputInJson = result.getResponse().getContentAsString();
        
        assertThat(outputInJson).isEqualTo(exceptedJson);
    }
    
    @Test
    public void testGetByName() throws Exception {
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setRole(User.Role.ROLE_USER);
        mockUser.setName("Teszt User");
        mockUser.setPass("pass");
        mockUser.setEmail("user@email.com");
        mockUser.setPhone("06301234567");
        mockUser.setAddress("1000 Teszt, Teszt utca 1.");
        
        Mockito.when(userRepository.findByName(Mockito.anyString()))
               .thenReturn(Optional.of(mockUser));
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/users/search?name=Teszt Klub 1")
                .accept(MediaType.APPLICATION_JSON);
        
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String exceptedJson = this.mapToJson(mockUser);
        String outputInJson = result.getResponse().getContentAsString();
        
        assertThat(outputInJson).isEqualTo(exceptedJson);
    }
    
    @Test
    public void testGetAll() throws Exception {
        User mockUser1 = new User();
        mockUser1.setId(1);
        mockUser1.setRole(User.Role.ROLE_USER);
        mockUser1.setName("Teszt User 1");
        mockUser1.setPass("pass");
        mockUser1.setEmail("user@email.com");
        mockUser1.setPhone("06301234567");
        mockUser1.setAddress("1000 Teszt, Teszt utca 1.");
        
        User mockUser2 = new User();
        mockUser2.setId(2);
        mockUser2.setRole(User.Role.ROLE_USER);
        mockUser2.setName("Teszt User 2");
        mockUser2.setPass("pass");
        mockUser2.setEmail("user@email.com");
        mockUser2.setPhone("06301234567");
        mockUser2.setEmail("1000 Teszt, Teszt utca 1.");
        
        List<User> userList = new ArrayList<>();
        userList.add(mockUser1);
        userList.add(mockUser2);
        
        Mockito.when(userRepository.findAll())
               .thenReturn(userList);
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/users")
                .accept(MediaType.APPLICATION_JSON);
        
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String exceptedJson = this.mapToJson(userList);
        String outputInJson = result.getResponse().getContentAsString();
        
        assertThat(outputInJson).isEqualTo(exceptedJson);
    }
    
    @Test
    public void testPatchToAdmin() throws Exception {
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setRole(User.Role.ROLE_USER);
        mockUser.setName("Teszt User");
        mockUser.setPass("pass");
        mockUser.setEmail("user@email.com");
        mockUser.setPhone("06301234567");
        mockUser.setAddress("1000 Teszt, Teszt utca 1.");
        
        Mockito.when(userRepository.findById(Mockito.anyInt()))
               .thenReturn(Optional.of(mockUser));
        
        mockUser.setRole(User.Role.ROLE_ADMIN);
        
        String inputInJson = this.mapToJson(mockUser);
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/api/users/1/to-admin")
                .accept(MediaType.APPLICATION_JSON)
                .content(inputInJson)
                .contentType(MediaType.APPLICATION_JSON);
        
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String outputInJson = response.getContentAsString();
        
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
            
    @Test
    public void testPut() throws Exception {
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setRole(User.Role.ROLE_USER);
        mockUser.setName("Teszt User");
        mockUser.setPass("pass");
        mockUser.setEmail("user@email.com");
        mockUser.setPhone("06301234567");
        mockUser.setAddress("1000 Teszt, Teszt utca 1.");
        
        String inputInJson = this.mapToJson(mockUser);
        
        Mockito.when(userRepository.findById(Mockito.anyInt()))
               .thenReturn(Optional.of(mockUser));
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/users/1")
                .accept(MediaType.APPLICATION_JSON)
                .content(inputInJson)
                .contentType(MediaType.APPLICATION_JSON);
        
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String outputInJson = response.getContentAsString();
        
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
    
    @Test
    public void testDelete() throws Exception {
        User mockUser = new User();
        mockUser.setId(1);
        mockUser.setRole(User.Role.ROLE_USER);
        mockUser.setName("Teszt User");
        mockUser.setPass("pass");
        mockUser.setEmail("user@email.com");
        mockUser.setPhone("06301234567");
        mockUser.setAddress("1000 Teszt, Teszt utca 1.");
        
        Mockito.when(userRepository.findById(Mockito.anyInt()))
               .thenReturn(Optional.of(mockUser));
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        
        mockMvc.perform(requestBuilder).andExpect(status().isOk());
    }
    
    // Objektumból JSON string-et gyárt
    private String mapToJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}
