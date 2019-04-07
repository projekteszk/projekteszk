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
import projekteszk.entities.Spot;
import projekteszk.repositories.SpotRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(value=SpotController.class, secure=false)
public class SpotControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SpotRepository spotRepository;
    
    @Test
    public void testGetById() throws Exception {
        Spot mockSpot = new Spot();
        mockSpot.setId(1);
        mockSpot.setName("Teszt horgászhely");
        mockSpot.setDesc("Ez csak egy teszt");
        
        Mockito.when(spotRepository.findById(Mockito.anyInt()))
               .thenReturn(Optional.of(mockSpot));
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/spots/1")
                .accept(MediaType.APPLICATION_JSON);
        
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String exceptedJson = this.mapToJson(mockSpot);
        String outputInJson = result.getResponse().getContentAsString();
        
        assertThat(outputInJson).isEqualTo(exceptedJson);
    }
    
    @Test
    public void testGetByName() throws Exception {
        Spot mockSpot = new Spot();
        mockSpot.setId(1);
        mockSpot.setName("Teszt horgászhely");
        mockSpot.setDesc("Ez csak egy teszt");
        
        Mockito.when(spotRepository.findByName(Mockito.anyString()))
               .thenReturn(Optional.of(mockSpot));
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/spots/search?name=Teszt Klub 1")
                .accept(MediaType.APPLICATION_JSON);
        
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String exceptedJson = this.mapToJson(mockSpot);
        String outputInJson = result.getResponse().getContentAsString();
        
        assertThat(outputInJson).isEqualTo(exceptedJson);
    }
    
    @Test
    public void testGetAll() throws Exception {
        Spot mockSpot1 = new Spot();
        mockSpot1.setId(1);
        mockSpot1.setName("Teszt horgászhely 1");
        mockSpot1.setDesc("Ez csak egy teszt");
        
        Spot mockSpot2 = new Spot();
        mockSpot2.setId(2);
        mockSpot2.setName("Teszt horgászhely 2");
        mockSpot2.setDesc("Ez csak egy teszt");
        
        List<Spot> spotList = new ArrayList<>();
        spotList.add(mockSpot1);
        spotList.add(mockSpot2);
        
        Mockito.when(spotRepository.findAll())
               .thenReturn(spotList);
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/spots")
                .accept(MediaType.APPLICATION_JSON);
        
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String exceptedJson = this.mapToJson(spotList);
        String outputInJson = result.getResponse().getContentAsString();
        
        assertThat(outputInJson).isEqualTo(exceptedJson);
    }
    
    @Test
    public void testPost() throws Exception {
        Spot mockSpot = new Spot();
        mockSpot.setId(1);
        mockSpot.setName("Teszt horgászhely");
        mockSpot.setDesc("Ez csak egy teszt");
        
        String inputInJson = this.mapToJson(mockSpot);
        
        Mockito.when(spotRepository.save(Mockito.any(Spot.class)))
               .thenReturn(mockSpot);
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/spots")
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
        Spot mockSpot = new Spot();
        mockSpot.setId(1);
        mockSpot.setName("Teszt horgászhely");
        mockSpot.setDesc("Ez csak egy teszt");
        
        String inputInJson = this.mapToJson(mockSpot);
        
        Mockito.when(spotRepository.findById(Mockito.anyInt()))
               .thenReturn(Optional.of(mockSpot));
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/api/spots/1")
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
        Spot mockSpot = new Spot();
        mockSpot.setId(1);
        mockSpot.setName("Teszt horgászhely");
        mockSpot.setDesc("Ez csak egy teszt");
        
        Mockito.when(spotRepository.findById(Mockito.anyInt()))
               .thenReturn(Optional.of(mockSpot));
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/spots/1")
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
