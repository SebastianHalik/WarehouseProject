package API;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;
import sample.Data;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebMvcTest(controllers = WareHouseController.class)


public class Tests {


    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    WareHouseController wareHouseController;

    @Before
    public void setUp() {
        this.mvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getFillTest() throws Exception{
        mvc.perform(MockMvcRequestBuilders
                .get("/api/fulfillment/{wh_id}/fill",0))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllWHTest() throws Exception{
        mvc.perform(MockMvcRequestBuilders
                .get("/api/fulfillment"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteItemTest() throws Exception{
        mvc.perform(MockMvcRequestBuilders
                .delete("/api/fulfillment/{item_id}",0))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllItemsTest() throws Exception{
        mvc.perform(MockMvcRequestBuilders
                .get("/api/fulfillment/{id}/products",0))
                .andExpect(status().isOk());
    }


    @Test
    public void getRatingNotExistingTest() throws Exception{
        mvc.perform(MockMvcRequestBuilders
                .get("/api/{item_id}/rating",-5))
                .andExpect(status().isNotFound());
    }
}
