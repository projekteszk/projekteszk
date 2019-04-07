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
import projekteszk.entities.Ticket;
import projekteszk.repositories.TicketRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(value=TicketController.class, secure=false)
public class TicketControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TicketRepository ticketRepository;
    
    @Test
    public void testGetAll() throws Exception {
        Ticket mockTicket = new Ticket();
        mockTicket.setId(1);
        mockTicket.setType("napi");
        mockTicket.setPrice(3000);
        
        Ticket mockTicket2 = new Ticket();
        mockTicket2.setId(2);
        mockTicket2.setType("heti");
        mockTicket2.setPrice(18000);
        
        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(mockTicket);
        ticketList.add(mockTicket2);
        
        Mockito.when(ticketRepository.findAll())
               .thenReturn(ticketList);
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/tickets")
                .accept(MediaType.APPLICATION_JSON);
        
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String exceptedJson = this.mapToJson(ticketList);
        String outputInJson = result.getResponse().getContentAsString();
        
        assertThat(outputInJson).isEqualTo(exceptedJson);
    }
    
    @Test
    public void testPost() throws Exception {
        Ticket mockTicket = new Ticket();
        mockTicket.setId(1);
        mockTicket.setType("napi");
        mockTicket.setPrice(3000);
        
        String inputInJson = this.mapToJson(mockTicket);
        
        Mockito.when(ticketRepository.save(Mockito.any(Ticket.class)))
               .thenReturn(mockTicket);
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/tickets")
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
    public void testDelete() throws Exception {
        Ticket mockTicket = new Ticket();
        mockTicket.setId(1);
        mockTicket.setType("napi");
        mockTicket.setPrice(3000);
        
        Mockito.when(ticketRepository.findById(Mockito.anyInt()))
               .thenReturn(Optional.of(mockTicket));
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/tickets/1")
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