package com.tmobile.federation.services;

import com.tmobile.federation.dao.IAMUserDAO;
import com.tmobile.federation.dao.IAMUserDAOImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;

import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author ThangTQ
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestContext.class })
public class CreateFedIdServiceTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CreateFedIdservice createFedIdservice;

    @Before
    public void setUp() {

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
          .standaloneSetup(createFedIdservice)
          .build();
    }

    @Test
    public void testGetFedIDService() throws Exception {
        IAMUserDAO iamUserDAO = new IAMUserDAOImpl();
        createFedIdservice.setIAMUserDAO(iamUserDAO);
        ResultActions resultActions = mockMvc
          .perform(get("/provisioning/v1/fedId?iamUserId=U-82bf6ba6-88f5-4049-b86c-860dca0c9fb3&email=email@mobile.com&tenantId=xyz123")
            .contentType("application/json")
            .header("authorization", "admin:123123")
            .header("transactionId", "123123")
          )
          .andExpect(status().isOk());
        System.out.println("passed xxxx");
        resultActions.andDo(MockMvcResultHandlers.print());

    }
}
