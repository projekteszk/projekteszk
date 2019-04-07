package projekteszk.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Date;
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
import projekteszk.entities.Order;
import projekteszk.entities.Ticket;
import projekteszk.repositories.OrderRepository;
import projekteszk.repositories.TicketRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(value=OrderController.class, secure=false)
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private OrderRepository orderRepository;
    
    @Test
    public void testGetAll() throws Exception {
        Order mockOrder = new Order();
        mockOrder.setId(1);
        
        Order mockOrder2 = new Order();
        mockOrder2.setId(2);
        
        List<Order> orderList = new ArrayList<>();
        orderList.add(mockOrder);
        orderList.add(mockOrder2);
        
        Mockito.when(orderRepository.findAll())
               .thenReturn(orderList);
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/orders")
                .accept(MediaType.APPLICATION_JSON);
        
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String exceptedJson = this.mapToJson(orderList);
        String outputInJson = result.getResponse().getContentAsString();
        
        assertThat(outputInJson).isEqualTo(exceptedJson);
    }
    
    /*
    @Test
    public void testGetById() throws Exception {
        Order mockOrder = new Order();
        mockOrder.setId(1);
        mockOrder.setStart(new Date());
        mockOrder.setStart(new Date());
        
        Mockito.when(orderRepository.findById(Mockito.anyInt()))
               .thenReturn(Optional.of(mockOrder));
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/orders/1")
                .accept(MediaType.APPLICATION_JSON);
        
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        String exceptedJson = this.mapToJson(mockOrder);
        String outputInJson = result.getResponse().getContentAsString();
        
        assertThat(outputInJson).isEqualTo(exceptedJson);
    }
    */
    
    @Test
    public void testPost() throws Exception {
        Order mockOrder = new Order();
        mockOrder.setId(1);
        
        String inputInJson = this.mapToJson(mockOrder);
        
        Mockito.when(orderRepository.save(Mockito.any(Order.class)))
               .thenReturn(mockOrder);
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/orders")
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
        Order mockOrder = new Order();
        mockOrder.setId(1);
        mockOrder.setStart(new Date());
        mockOrder.setStart(new Date());
        
        Mockito.when(orderRepository.findById(Mockito.anyInt()))
               .thenReturn(Optional.of(mockOrder));
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/api/orders/1")
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