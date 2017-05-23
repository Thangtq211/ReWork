package com.tmobile.federation.services;


import com.tmobile.federation.dao.IAMUserDAO;
import com.tmobile.federation.dao.IAMUserDAOImpl;
import com.tmobile.federation.services.CreateFedIdservice;
import com.tmobile.federation.services.TestContext;
import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Admin on 5/11/2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestContext.class })
public class OrgGetFedIdServiceTest {

    private MockMvc mockMvc;

    @InjectMocks
    private CreateFedIdservice fedIdService;

    @InjectMocks
    private IAMUserDAOImpl iamUserDAO;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
          .standaloneSetup(fedIdService)
          .build();
    }

    @Test
    public void testInvalidHeader() throws Exception{
        mockMvc.perform(get("/provisioning/v1/fedId")
          .contentType("application/json"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.statusCode").value(HttpStatus.UNAUTHORIZED.value()+" "+HttpStatus.UNAUTHORIZED.getReasonPhrase()))
          .andExpect(jsonPath("$.object").value(IsNull.nullValue()));
    }

    @Test
    public void testWithoutRequestParam() throws Exception{
        mockMvc.perform(get("/provisioning/v1/fedId")
          .contentType("application/json")
          .header("authorization","admin:123123"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()+" "+HttpStatus.BAD_REQUEST.getReasonPhrase()))
          .andExpect(jsonPath("$.object.errorDescription").value("Missing mandatory parameter"))
          .andExpect(jsonPath("$.object.errorDetail").value("email or iamuserid"));
    }

    @Test
    public void testWithIAMUSerIDRequestParam() throws Exception{
        mockMvc.perform(get("/provisioning/v1/fedId?iamUserId=user")
          .contentType("application/json")
          .header("authorization","admin:123123"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()+" "+HttpStatus.BAD_REQUEST.getReasonPhrase()))
          .andExpect(jsonPath("$.object.errorDescription").value("Missing mandatory parameter"))
          .andExpect(jsonPath("$.object.errorDetail").value("tenantId"));
    }

    @Test
    public void testWithAllRequestParam() throws Exception{
        fedIdService.setIAMUserDAO(iamUserDAO);
        mockMvc.perform(get("/provisioning/v1/fedId?iamUserId=user&email=info@tmobile.com&tenantId=123123")
          .contentType("application/json")
          .header("authorization","admin:123123"))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.statusCode").value(HttpStatus.BAD_REQUEST.value()+" "+HttpStatus.BAD_REQUEST.getReasonPhrase()))
          .andExpect(jsonPath("$.object").value("User Does Not Exists"));
    }
}
