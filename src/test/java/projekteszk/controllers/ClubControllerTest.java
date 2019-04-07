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
import projekteszk.entities.Club;
import projekteszk.repositories.ClubRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(value=ClubController.class, secure=false)
public class ClubControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ClubRepository clubRepository;
    
    @Test
    public void testGetById() throws Exception {
        Club mockClub = new Club();
        mockClub.setId(1);
        mockClub.setName("Teszt Klub 1");
        mockClub.setShire("Hajdú-Bihar");
        
        Mockito.when(clubRepository.findById(Mockito.anyInt()))
               .thenReturn(Optional.of(mockClub));
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/clubs/1")
                .accept(MediaType.APPLICATION_JSON);
        
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String exceptedJson = this.mapToJson(mockClub);
        String outputInJson = result.getResponse().getContentAsString();
        
        assertThat(outputInJson).isEqualTo(exceptedJson);
    }
    
    @Test
    public void testGetByName() throws Exception {
        Club mockClub = new Club();
        mockClub.setId(1);
        mockClub.setName("Teszt Klub 1");
        mockClub.setShire("Hajdú-Bihar");
        
        Mockito.when(clubRepository.findByName(Mockito.anyString()))
               .thenReturn(Optional.of(mockClub));
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/clubs/search?name=Teszt Klub 1")
                .accept(MediaType.APPLICATION_JSON);
        
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String exceptedJson = this.mapToJson(mockClub);
        String outputInJson = result.getResponse().getContentAsString();
        
        assertThat(outputInJson).isEqualTo(exceptedJson);
    }
    
    @Test
    public void testGetAll() throws Exception {
        Club mockClub1 = new Club();
        mockClub1.setId(1);
        mockClub1.setName("Teszt Klub 1");
        mockClub1.setShire("Hajdú-Bihar");
        
        Club mockClub2 = new Club();
        mockClub2.setId(1);
        mockClub2.setName("Teszt Klub 2");
        mockClub2.setShire("Pest");
        
        List<Club> clubList = new ArrayList<>();
        clubList.add(mockClub1);
        clubList.add(mockClub2);
        
        Mockito.when(clubRepository.findAll())
               .thenReturn(clubList);
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/clubs")
                .accept(MediaType.APPLICATION_JSON);
        
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String exceptedJson = this.mapToJson(clubList);
        String outputInJson = result.getResponse().getContentAsString();
        
        assertThat(outputInJson).isEqualTo(exceptedJson);
    }
    
    @Test
    public void testPost() throws Exception {
        Club mockClub = new Club();
        mockClub.setId(1);
        mockClub.setName("Teszt Klub 1");
        mockClub.setShire("Hajdú-Bihar");
        
        String inputInJson = this.mapToJson(mockClub);
        
        Mockito.when(clubRepository.save(Mockito.any(Club.class)))
               .thenReturn(mockClub);
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/clubs")
                .accept(MediaType.APPLICATION_JSON)
                .content(inputInJson)
                .contentType(MediaType.APPLICATION_JSON);
        
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        String outputInJson = response.getContentAsString();
        
        assertThat(outputInJson).isEqualTo(inputInJson);
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
    
    @Test
    public void testPut() throws Exception {
        Club mockClub = new Club();
        mockClub.setId(1);
        mockClub.setName("Teszt Klub 1");
        mockClub.setShire("Hajdú-Bihar");
        
        String inputInJson = this.mapToJson(mockClub);
        
        Mockito.when(clubRepository.findById(Mockito.anyInt()))
               .thenReturn(Optional.of(mockClub));
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/clubs/1")
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
        Club mockClub = new Club();
        mockClub.setId(1);
        mockClub.setName("Teszt Klub 1");
        mockClub.setShire("Hajdú-Bihar");
        
        Mockito.when(clubRepository.findById(Mockito.anyInt()))
               .thenReturn(Optional.of(mockClub));
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/clubs/1")
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
